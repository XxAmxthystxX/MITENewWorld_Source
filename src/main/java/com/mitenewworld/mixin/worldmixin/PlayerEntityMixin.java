package com.mitenewworld.mixin.worldmixin;
import com.mitenewworld.MITENewWorld;

import com.google.common.math.IntMath;
import com.mitenewworld.registry.ModItems;
import com.mitenewworld.entity.player.AdventureLevel;
import com.mitenewworld.core.shadow.ShadowHungerManager;
import com.mitenewworld.core.shadow.ShadowPlayerEntity;
import com.mitenewworld.core.shadow.ShadowScreenHandler;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements ShadowPlayerEntity {

    @Shadow public abstract PlayerInventory getInventory();

    @Shadow
    private final PlayerAbilities abilities = new PlayerAbilities();
    @Shadow
    private int currentExplosionResetGraceTime;
    @Shadow
    private int sleepTimer;
    @Shadow
    public int experiencePickUpDelay;
    @Shadow
    public int experienceLevel;
    @Shadow
    private int lastPlayedLevelUpSoundTime;
    @Shadow
    private ItemStack selectedItem = ItemStack.EMPTY;
    @Shadow @Final
    PlayerInventory inventory;
    @Shadow @Final public PlayerScreenHandler playerScreenHandler;
    @Shadow
    public ScreenHandler currentScreenHandler;
    @Shadow
    private final ItemCooldownManager itemCooldownManager = new ItemCooldownManager();

    @Shadow public abstract boolean isInCreativeMode();

    @Shadow public abstract HungerManager getHungerManager();

    @Shadow protected HungerManager hungerManager;
    @Unique
    private final AdventureLevel adventureLevel = new AdventureLevel();
    /** 上一次姿态判定时是否处于游泳状态：背包替换只在"刚进入游泳"时扫一次 */
    @Unique
    private boolean wasSwimming = false;


    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }



    @Unique
    public int getExperienceLevel() {
        return experienceLevel;
    }

    @Inject(method = "getNextLevelExperience", at = @At("HEAD"), cancellable = true)
    public void getNextLevelExperience(CallbackInfoReturnable<Integer> cir) {
         cir.setReturnValue(10 * (this.experienceLevel + 1));
    }


    @Inject(method = "addExperienceLevels", at = @At("HEAD"), cancellable = true)
    public void addExperienceLevels(int levels, CallbackInfo ci) {
        ci.cancel();
        this.experienceLevel = IntMath.saturatedAdd(this.experienceLevel, levels);
        //adventureLevel.setAdventureLevel(this.experienceLevel);
        int i = this.experienceLevel / 5 ;
        setPlayerMaxHealth(i);
        setPlayerMaxFoodLevel(i);
        int b = ((ShadowHungerManager)hungerManager).getMaxFoodLevel();
        if (this.experienceLevel % 5 == 0 && (float) this.lastPlayedLevelUpSoundTime < (float) this.age - 100.0F) {
            float f = this.experienceLevel > 30 ? 1.0F : (float) this.experienceLevel / 30.0F;
            this.getEntityWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_LEVELUP, this.getSoundCategory(), f * 0.75F, 1.0F);
            this.lastPlayedLevelUpSoundTime = this.age;
        }

    }

    @Inject(method = "onDeath", at = @At("RETURN"))
    private void onDeath(DamageSource damageSource, CallbackInfo ci) {
        if (experienceLevel <= 0) {
            experienceLevel = experienceLevel - 1;
        }
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onTick(CallbackInfo ci) {
        ci.cancel();
        if(!isInCreativeMode() && !isSpectator()) {
            ((ShadowScreenHandler)this.playerScreenHandler).updateTick();
        }
        this.noClip = this.isSpectator();
        if (this.isSpectator() || this.hasVehicle()) {
            this.setOnGround(false);
        }
        if (this.experiencePickUpDelay > 0) {
            --this.experiencePickUpDelay;
        }
        if (this.isSleeping()) {
            this.sleepTimer++;
            if (this.sleepTimer > 100) {
                this.sleepTimer = 100;
            }
        } else if (this.sleepTimer > 0) {
            this.sleepTimer++;
            if (this.sleepTimer >= 110) {
                this.sleepTimer = 0;
            }
        }
        this.updateWaterSubmersionState();
        super.tick();
        double d = MathHelper.clamp(this.getX(), -2.9999999E7, 2.9999999E7);
        double e = MathHelper.clamp(this.getZ(), -2.9999999E7, 2.9999999E7);
        if (d != this.getX() || e != this.getZ()) {
            this.setPosition(d, this.getY(), e);
        }
        ++this.lastAttackedTicks;
        ItemStack itemStack = this.getMainHandStack();
        if (!ItemStack.areEqual(this.selectedItem, itemStack)) {
            if (!ItemStack.areItemsEqual(this.selectedItem, itemStack)) {
                this.resetLastAttackedTicks();
            }
            this.selectedItem = itemStack.copy();
        }
        if (!this.isSubmergedIn(FluidTags.WATER) && this.isEquipped(Items.TURTLE_HELMET)) {
            this.updateTurtleHelmet();
        }
        this.itemCooldownManager.update();
        this.updatePose();
        if (this.currentExplosionResetGraceTime > 0) {
            --this.currentExplosionResetGraceTime;
        }
    }

    @Shadow
    private boolean isEquipped(Item turtleHelmet) {
        return false;
    }

    @Shadow
    protected void updatePose() {

    }
    @Shadow
    private void updateTurtleHelmet() {
    }
    @Shadow
    public void resetLastAttackedTicks() {
    }

    @Shadow
    protected boolean updateWaterSubmersionState() {
        return false;
    }


    @Inject(method = "createPlayerAttributes", at = @At("RETURN"), cancellable = true)
    private static void onCreatePlayerAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.cancel();
        DefaultAttributeContainer.Builder builder = cir.getReturnValue();
        builder.add(EntityAttributes.ATTACK_DAMAGE, 1.0)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.1F)
                .add(EntityAttributes.ATTACK_SPEED)
                .add(EntityAttributes.LUCK)
                .add(EntityAttributes.BLOCK_INTERACTION_RANGE, 3.0)
                .add(EntityAttributes.ENTITY_INTERACTION_RANGE, 3.0)
                .add(EntityAttributes.BLOCK_BREAK_SPEED )
                .add(EntityAttributes.SUBMERGED_MINING_SPEED)
                .add(EntityAttributes.SNEAKING_SPEED)
                .add(EntityAttributes.MINING_EFFICIENCY)
                .add(EntityAttributes.SWEEPING_DAMAGE_RATIO)
                .add(EntityAttributes.MAX_HEALTH, 6.0);
    }

    @Unique
    @Override
    public void setPlayerMaxHealth(int Alevel) {
        this.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(MathHelper.clamp(6 + Alevel * 2 , 6, 40));
   }

    @Unique
    public void NotCanAct() {
        this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).setBaseValue(0.0);
        this.getAttributeInstance(EntityAttributes.BLOCK_BREAK_SPEED).setBaseValue(0.0);
    }

    @Unique
    public void CanAct() {
        this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).setBaseValue(1.0);
        this.getAttributeInstance(EntityAttributes.BLOCK_BREAK_SPEED).setBaseValue(1.0);
    }

    @Unique
    public void setPlayerMaxFoodLevel(int Alevel) {
        System.out.println("alevel " + Alevel);
        int j = 6 + Alevel * 2;
        int i = MathHelper.clamp(j , 6 , 20);
        ((ShadowHungerManager)getHungerManager()).setMaxFoodLevel(i);
    }

    @Unique
    public void setPlayerMaxWaterLevel(int Alevel) {
        ((ShadowHungerManager)getHungerManager()).setMaxWaterLevel(6 + Alevel * 2);
    }
    @Inject(method = "getExpectedPose", at = @At("HEAD"), cancellable = true)
    protected void updatePose(CallbackInfoReturnable<EntityPose> cir) {

        if (this.isSleeping()) {
            cir.setReturnValue(EntityPose.SLEEPING);
            return;
        }
        if (this.isSwimming()) {
            // 只在刚进入游泳状态时扫一次背包（原来每 tick 都扫 41 格并每 tick markDirty）
            if (!this.wasSwimming) {
                this.wasSwimming = true;
                Inventory inventory = this.getInventory();
                boolean changed = false;
                for (int i = 0; i < inventory.size(); i++) {
                    ItemStack original = inventory.getStack(i);
                    Item replacement = ModItems.FILL_IN_WATER.getOrDefault(original.getItem(), original.getItem());
                    if (replacement != original.getItem()) {
                        inventory.setStack(i, new ItemStack(replacement, original.getCount()));
                        changed = true;
                    }
                }
                if (changed) {
                    inventory.markDirty();
                }
            }
            cir.setReturnValue(EntityPose.SWIMMING);
            return;
        }
        this.wasSwimming = false;
        if (this.isGliding()) {
            cir.setReturnValue(EntityPose.GLIDING);
            return;
        }
        if (this.isUsingRiptide()) {
            cir.setReturnValue(EntityPose.SPIN_ATTACK);
            return;
        }
        if (this.isSneaking() && !this.abilities.flying) {
            cir.setReturnValue(EntityPose.CROUCHING);
            return;
        }
        cir.setReturnValue(EntityPose.STANDING);
    }





}



