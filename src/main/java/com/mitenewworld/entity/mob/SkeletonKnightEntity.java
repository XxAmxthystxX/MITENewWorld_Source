package com.mitenewworld.entity.mob;
import com.mitenewworld.MITENewWorld;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SkeletonKnightEntity extends AbstractSkeletonEntity {

    private final BowAttackGoal<AbstractSkeletonEntity> bowAttackGoal;
    private final MeleeAttackGoal meleeAttackGoal;

    public SkeletonKnightEntity(EntityType<? extends AbstractSkeletonEntity> entityType, World world) {
        super(entityType, world);
        this.bowAttackGoal = new DistanceBowAttackGoal();
        this.meleeAttackGoal = new DistanceMeleeAttackGoal();
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(2, new AvoidSunlightGoal(this));
        this.goalSelector.add(3, new EscapeSunlightGoal(this, 1.0));
        this.goalSelector.add(3, new FleeEntityGoal<>(this, WolfEntity.class, 6.0F, 1.0, 1.2));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));

        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, TurtleEntity.class, 10, true, false, TurtleEntity.BABY_TURTLE_ON_LAND_FILTER));
    }

    public static DefaultAttributeContainer.Builder createShadowAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.FOLLOW_RANGE, 100.0)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.2F)
                .add(EntityAttributes.ATTACK_DAMAGE, 7.0)
                .add(EntityAttributes.ARMOR, 5.0)
                .add(EntityAttributes.MAX_HEALTH, 40.0f);
    }

    @Override
    public SoundEvent getStepSound() {
        return SoundEvents.ENTITY_SKELETON_STEP;
    }

    @Override
    public void updateAttackType() {
        if (this.getEntityWorld() == null || this.getEntityWorld().isClient()) {
            return;
        }

        this.goalSelector.remove(this.bowAttackGoal);
        this.goalSelector.remove(this.meleeAttackGoal);

        ItemStack heldItem = this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW));
        if (heldItem.isOf(Items.BOW)) {
            int interval = this.getHardAttackInterval();
            if (this.getEntityWorld().getDifficulty() != Difficulty.HARD) {
                interval = this.getRegularAttackInterval();
            }
            this.bowAttackGoal.setAttackInterval(interval);
            this.goalSelector.add(4, this.bowAttackGoal);
            this.goalSelector.add(4, this.meleeAttackGoal);
        } else {
            this.goalSelector.add(4, new MeleeAttackGoal(this, 1.2, false) {
                @Override
                public void start() {
                    super.start();
                    SkeletonKnightEntity.this.setAttacking(true);
                }
                @Override
                public void stop() {
                    super.stop();
                    SkeletonKnightEntity.this.setAttacking(false);
                }
            });
        }
    }

    @Override
    protected void initEquipment(Random random, LocalDifficulty localDifficulty) {
        super.initEquipment(random, localDifficulty);
        this.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        this.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
        this.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
        this.equipStack(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
        this.setEquipmentDropChance(EquipmentSlot.HEAD, 0.1F);
        this.setEquipmentDropChance(EquipmentSlot.CHEST, 0.1F);
        this.setEquipmentDropChance(EquipmentSlot.LEGS, 0.1F);
        this.setEquipmentDropChance(EquipmentSlot.FEET, 0.1F);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        entityData = super.initialize(world, difficulty, spawnReason, entityData);

        if (world instanceof ServerWorld serverWorld) {
            // 10% 概率生成坐骑（你之前改为 true 表示必定生成，按需调整）
            if (world.getRandom().nextFloat() < 0.1F) {
                final SkeletonHorseEntity horse = EntityType.SKELETON_HORSE.create(serverWorld, SpawnReason.STRUCTURE);
                if (horse != null) {
                    horse.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0F);
                    horse.initialize(serverWorld, difficulty, SpawnReason.STRUCTURE, null);

                    // 【关键】将骷髅马正式加入世界
                    serverWorld.spawnEntity(horse);

                    // 延迟到下一 tick 执行骑乘，避免客户端同步顺序问题
                    serverWorld.getServer().execute(() -> {
                        if (this.isAlive() && horse.isAlive()) {
                            this.startRiding(horse);
                        }
                    });
                }
            }
        }
        return entityData;
    }

    @Override
    public EntityDimensions getBaseDimensions(EntityPose pose) {
        return EntityDimensions.changing(0.72F, 2.388F);
    }

    // ========== 内部类 ==========

    class DistanceBowAttackGoal extends BowAttackGoal<AbstractSkeletonEntity> {
        public DistanceBowAttackGoal() {
            super(SkeletonKnightEntity.this, 1.0, 20, 15.0F);
        }

        @Override
        public boolean canStart() {
            return super.canStart() && isTargetFarEnough();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && isTargetFarEnough();
        }

        @Override
        public void start() {
            super.start();
            // 远程模式启动时，如果手持的不是弓则换成弓
            if (!SkeletonKnightEntity.this.getMainHandStack().isOf(Items.BOW)) {
                SkeletonKnightEntity.this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
            }
        }

        private boolean isTargetFarEnough() {
            LivingEntity target = SkeletonKnightEntity.this.getTarget();
            return target != null && SkeletonKnightEntity.this.squaredDistanceTo(target) >= 25.0;
        }
    }

    class DistanceMeleeAttackGoal extends MeleeAttackGoal {
        public DistanceMeleeAttackGoal() {
            super(SkeletonKnightEntity.this, 1.2, false);
        }

        @Override
        public boolean canStart() {
            if (!super.canStart()) {
                return false;
            }
            LivingEntity target = SkeletonKnightEntity.this.getTarget();
            return target != null && SkeletonKnightEntity.this.squaredDistanceTo(target) < 25.0;
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue()
                    && SkeletonKnightEntity.this.getTarget() != null
                    && SkeletonKnightEntity.this.squaredDistanceTo(SkeletonKnightEntity.this.getTarget()) < 25.0;
        }

        @Override
        public void start() {
            super.start();
            SkeletonKnightEntity.this.setAttacking(true);
            // 近战模式启动时，如果手持的是弓则换成铁剑
            if (SkeletonKnightEntity.this.getMainHandStack().isOf(Items.BOW)) {
                SkeletonKnightEntity.this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            }
        }

        @Override
        public void stop() {
            super.stop();
            SkeletonKnightEntity.this.setAttacking(false);
        }
    }
}
