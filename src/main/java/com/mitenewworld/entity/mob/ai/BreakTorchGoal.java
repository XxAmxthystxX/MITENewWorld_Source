package com.mitenewworld.entity.mob.ai;
import com.mitenewworld.MITENewWorld;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public class BreakTorchGoal extends Goal {
    private final MobEntity mob;
    private int cooldown = 0;
    private static final int MAX_COOLDOWN = 100; // 执行间隔（tick）

    public BreakTorchGoal(MobEntity mob) {
        this.mob = mob;
    }

    @Override
    public boolean canStart() {
        return cooldown == 0 && this.mob.getTarget() != null;
    }

    @Override
    public void start() {
        World world = mob.getEntityWorld();
        if (world.isClient()) {
            return;
        }
        breakTorchesInAreaOptimized(world);
        cooldown = MAX_COOLDOWN;
    }

    @Override
    public void tick() {
        if (cooldown > 0) {
            cooldown--;
        }
    }


    private void breakTorchesInAreaOptimized(World world) {
        BlockPos center = mob.getBlockPos();
        int radius = 8; // 覆盖 16x16x16 区域

        // 使用可变位置，避免重复创建对象
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();

        for (int dx = -radius; dx < radius; dx++) {
            for (int dy = -radius; dy < radius; dy++) {
                for (int dz = -radius; dz < radius; dz++) {
                    mutablePos.set(center.getX() + dx, center.getY() + dy, center.getZ() + dz);
                    BlockState state = world.getBlockState(mutablePos);
                    if (state.isOf(Blocks.TORCH)|| state.isOf(Blocks.WALL_TORCH)) {

                        world.breakBlock(mutablePos, false, this.mob,3);
                    }
                }
            }
        }
    }
}
