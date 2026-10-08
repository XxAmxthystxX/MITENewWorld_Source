package com.mitenewworld.entity.mob.ai;
import com.mitenewworld.MITENewWorld;

import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.NavigationConditions;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class BreakBlockGoal extends Goal {
    private final MobEntity mob;
    private BlockPos targetPos;
    private int breakProgress;
    private int maxProgress;
    private final MobNavigation mobNavigation;
    private int cooldown;
    private boolean isPathDownwards;

    public BreakBlockGoal(MobEntity mob) {
        this.mob = mob;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
        this.mobNavigation = (MobNavigation) mob.getNavigation();
        this.cooldown = 0;
    }

    @Override
    public boolean canStart() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }
        if (mob.getTarget() == null) {
            return false;
        }
        mob.getLookControl().lookAt(mob.getTarget());
        if (!NavigationConditions.hasMobNavigation(mob) ||
                mob.isDead() ||
                !mobNavigation.isFollowingPath()) {
            return false;
        }

        Path path = mobNavigation.getCurrentPath();
        if (path == null || path.isFinished()) {
            return false;
        }

        this.isPathDownwards = isPathGoingDown(path);

        if (!(mob.horizontalCollision || mob.verticalCollision)) {
            return false;
        }
        return findBreakTarget();
    }

    private boolean isPathGoingDown(Path path) {
        int currentIndex = path.getCurrentNodeIndex();
        if (currentIndex >= path.getLength() - 1) {
            return false;
        }

        PathNode currentNode = path.getNode(currentIndex);
        PathNode nextNode = path.getNode(currentIndex + 1);

        return nextNode.y < currentNode.y;
    }

    private boolean findBreakTarget() {
        BlockPos mobPos = mob.getBlockPos();
        Direction facing = mob.getFacing();
        List<BlockPos> priorityChecks = new ArrayList<>();

        // 1. 检查前方区域（优先）
        if (facing.getAxis() != Direction.Axis.Y) {
            // 只对水平朝向检查前方区域
            priorityChecks.add(mobPos.offset(facing));
            priorityChecks.add(mobPos.offset(facing).up());
            priorityChecks.add(mobPos.offset(facing).up(2));
        }

        // 2. 检查正上方
        priorityChecks.add(mobPos.up());

        // 3. 检查侧面（如果朝向允许）
        if (facing.getAxis() != Direction.Axis.Y) {
            priorityChecks.add(mobPos.offset(facing.rotateYCounterclockwise()));
            priorityChecks.add(mobPos.offset(facing.rotateYClockwise()));
        } else {
            // 当朝向是垂直时，检查所有水平方向
            priorityChecks.add(mobPos.north());
            priorityChecks.add(mobPos.south());
            priorityChecks.add(mobPos.east());
            priorityChecks.add(mobPos.west());
        }

        // 4. 检查正下方（如果允许）
        if (isPathDownwards) {
            priorityChecks.add(mobPos.down());
        }

        // 按优先级检查
        for (BlockPos pos : priorityChecks) {
            if (isValidBreakTarget(pos)) {
                targetPos = pos;
                return true;
            }
        }

        // 5. 如果没有找到，检查更宽的范围
        return checkWiderRange(mobPos);
    }

    private boolean checkWiderRange(BlockPos mobPos) {
        // 检查周围3x3x3区域（除了已经检查过的位置）
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    // 跳过当前位置
                    if (dx == 0 && dy == 0 && dz == 0) continue;

                    BlockPos pos = mobPos.add(dx, dy, dz);
                    if (isValidBreakTarget(pos)) {
                        targetPos = pos;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isValidBreakTarget(BlockPos pos) {
        World world = mob.getEntityWorld();
        BlockState state = world.getBlockState(pos);

        if (state.isAir() ||
                state.getHardness(world, pos) < 0 ||
                !state.isSolidBlock(world, pos)) {
            return false;
        }
        if (state.isToolRequired()){
            if (!mob.getStackInHand(Hand.MAIN_HAND).isSuitableFor(state)) {
                return false;
            }
        }

        Vec3d center = Vec3d.ofCenter(pos);
        double distanceSq = mob.squaredDistanceTo(center);
        return distanceSq <= 9.0;
    }

    private void updateMaxProgress() {
        World world = mob.getEntityWorld();
        BlockState state = world.getBlockState(targetPos);
        ItemStack tool = mob.getStackInHand(Hand.MAIN_HAND);
        float hardness = state.getHardness(world, targetPos);
        float efficiency = 1.0f;
        if (!tool.isEmpty()) {
            efficiency = tool.getMiningSpeedMultiplier(state);
            if (tool.isSuitableFor(state)) {
                efficiency *= 1.5f;
            }
        }
        this.maxProgress = (int) (20 + hardness * 25 / efficiency);
    }

    @Override
    public void start() {
        this.breakProgress = 0;
        updateMaxProgress();
        mobNavigation.stop();
        mob.getLookControl().lookAt(targetPos.getX(), targetPos.getY(), targetPos.getZ());
        mob.getEntityWorld().setBlockBreakingInfo(mob.getId(), targetPos, 0);
        cooldown = 10;
    }

    @Override
    public boolean shouldContinue() {
        return targetPos != null &&
                breakProgress < maxProgress &&
                isValidBreakTarget(targetPos) &&
                mob.squaredDistanceTo(Vec3d.ofCenter(targetPos)) <= 9;
    }

    @Override
    public void stop() {
        if (targetPos != null) {
            mob.getEntityWorld().setBlockBreakingInfo(mob.getId(), targetPos, -1);
        }
        targetPos = null;
        cooldown = 20;
    }

    @Override
    public void tick() {
        if (targetPos == null) {
            return;
        }
        World world = mob.getEntityWorld();
        BlockState state = world.getBlockState(targetPos);
        Vec3d center = Vec3d.ofCenter(targetPos);
        if (breakProgress % 10 == 0) {
            world.playSound(
                    mob, targetPos, state.getSoundGroup().getHitSound(),
                    SoundCategory.HOSTILE, 0.8F, 0.8F + mob.getRandom().nextFloat() * 0.4F
            );
            world.addParticleClient(
                    new BlockStateParticleEffect(ParticleTypes.BLOCK, state),
                    center.x, center.y, center.z,
                    0.1, 0.1, 0.1
            );
            mob.swingHand(Hand.MAIN_HAND);
        }
        breakProgress++;
        int progress = Math.min(10, (int) ((float) breakProgress / maxProgress * 10));
        world.setBlockBreakingInfo(mob.getId(), targetPos, progress);
        if (breakProgress >= maxProgress) {
            world.breakBlock(targetPos, false, mob);
            stop();
            mob.getNavigation().recalculatePath();
        }
    }
}
