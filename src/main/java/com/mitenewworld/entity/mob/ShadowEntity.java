package com.mitenewworld.entity.mob;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.entity.mob.ai.BreakBlockGoal;
import com.mitenewworld.entity.mob.ai.BreakTorchGoal;
import com.mitenewworld.entity.mob.ai.ShadowAttackGoal;
import com.mitenewworld.mixin.DamageUtilMixin;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ShadowEntity extends HostileEntity {


    public ShadowEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }


    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(4, new BreakBlockGoal(this));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, new BreakTorchGoal(this));

        this.initCustomGoals();
	}
    protected void initCustomGoals() {
        this.goalSelector.add(2, new ShadowAttackGoal(this, 1.0, false));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0));
        this.targetSelector.add(1, new RevengeGoal(this).setGroupRevenge(ZombifiedPiglinEntity.class));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, MerchantEntity.class, false));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }
    public static DefaultAttributeContainer.Builder createShadowAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.FOLLOW_RANGE, 50.0)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.3F)
                .add(EntityAttributes.ATTACK_DAMAGE, 3.0)
                .add(EntityAttributes.ARMOR, 2.0)
                .add(EntityAttributes.MAX_HEALTH, 50.0f)
                .add(EntityAttributes.GRAVITY, 0.05f);


    }

    @Override
    public boolean tryAttack(ServerWorld world, Entity target) {
        if (target instanceof LivingEntity entity) {
            float f = 15 - world.getBlockState(entity.getBlockPos()).getLuminance();
            if (entity.damage(world , this.getDamageSources().mobAttack(this) , f)) {
                this.onAttacking(target);
                this.playAttackSound();
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS ,1 , 200 , true , false));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canHit() {
        World world = this.getEntityWorld();

        BlockPos blockPos = this.getBlockPos();
        if ( world.getLightLevel(blockPos) >= 9) {
            return super.canHit();
        }
        return false;
    }

    @Override
    public boolean canPickupItem(ItemStack stack) {
        return false;
    }

    @Override
    public void tickMovement() {
        if (this.isAlive()) {
            if (this.isAffectedByDaylight()) {
                this.setOnFireFor(8.0f);
            }
        }
        super.tickMovement();
    }


}
