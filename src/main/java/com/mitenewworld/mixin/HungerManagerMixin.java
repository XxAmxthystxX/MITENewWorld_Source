package com.mitenewworld.mixin;
import com.mitenewworld.MITENewWorld;


import com.mitenewworld.core.shadow.ShadowHungerManager;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HungerManager.class)
public class HungerManagerMixin implements ShadowHungerManager {
    @Shadow
    public int foodLevel = 6;
    @Unique
    public int maxFoodLevel = 6;
    @Shadow
    private float saturationLevel = 6.0f;
    @Unique
    public int waterLevel = 6;
    @Unique
    public int maxWaterLevel = 6;
    @Shadow
    private float exhaustion;
    @Shadow
    private int foodTickTimer;

    @Inject(method = "addInternal", at = @At("HEAD"), cancellable = true)
    private void addInternal(int nutrition, float saturation, CallbackInfo ci) {
        ci.cancel();
        this.foodLevel = MathHelper.clamp(nutrition + this.foodLevel, 0, this.maxFoodLevel);
        this.saturationLevel = MathHelper.clamp(saturation + this.saturationLevel, 0.0F, (float)this.maxFoodLevel);
    }
    @Shadow
    private void addInternal(int nutrition, float saturation){

    }

    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
    private void add(int food, float saturationModifier, CallbackInfo ci) {
        ci.cancel();
        addInternal(food, saturationModifier);
    }
    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private void update(ServerPlayerEntity player, CallbackInfo ci) {
        ci.cancel();
        ServerWorld serverWorld = player.getEntityWorld();
        boolean bl = serverWorld.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION);
        Difficulty difficulty = serverWorld.getDifficulty();
        if (this.exhaustion > 4.0f) {
            this.exhaustion -= 4.0f;
            if (this.saturationLevel > 0.0f) {
                this.saturationLevel = Math.max(this.saturationLevel - 1.0f, 0.0f);
            } else if (difficulty != Difficulty.PEACEFUL) {
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }
        // 原版是 20 点里 >= 18 才回血（差 2 点），这里按当前最大饱食度等比例缩放，
        // 否则 maxFoodLevel 低于 18 时（冒险等级不足）永远无法自然回血
        if (bl && this.foodLevel >= this.maxFoodLevel - 2 && player.canFoodHeal()) {
            ++this.foodTickTimer;
            if (this.foodTickTimer >= 80) {
                player.heal(1.0f);
                this.addExhaustion(6.0f);
                this.foodTickTimer = 0;
            }
        } else if (this.foodLevel <= 0) {
            ++this.foodTickTimer;
            if (this.foodTickTimer >= 80) {
                if (player.getHealth() > 10.0f || difficulty == Difficulty.HARD || player.getHealth() > 1.0f && difficulty == Difficulty.NORMAL) {
                    player.damage(serverWorld, player.getDamageSources().starve(), 1.0f);
                }
                this.foodTickTimer = 0;
            }
        } else {
            this.foodTickTimer = 0;
        }
    }

    @Shadow
    public void addExhaustion(float v) {
    }
    @Inject(method = "readData", at = @At("HEAD"), cancellable = true)
    public void readData(ReadView view, CallbackInfo ci) {
        ci.cancel();
        this.foodLevel = view.getInt("foodLevel", 6);
        this.foodTickTimer = view.getInt("foodTickTimer", 0);
        this.saturationLevel = view.getFloat("foodSaturationLevel", 5.0f);
        this.exhaustion = view.getFloat("foodExhaustionLevel", 0.0f);
        this.maxFoodLevel = view.getInt("maxFoodLevel", 6);
        this.maxWaterLevel = view.getInt("maxWaterLevel", 6);
        this.waterLevel = view.getInt("waterLevel", 6);
    }
    @Inject(method = "writeData", at = @At("HEAD"), cancellable = true)
    public void writeData(WriteView view, CallbackInfo ci) {
        ci.cancel();
        view.putInt("foodLevel", this.foodLevel);
        view.putInt("foodTickTimer", this.foodTickTimer);
        view.putFloat("foodSaturationLevel", this.saturationLevel);
        view.putFloat("foodExhaustionLevel", this.exhaustion);
        view.putInt("maxFoodLevel", this.maxFoodLevel);
        view.putInt("maxWaterLevel", this.maxWaterLevel);
        view.putInt("waterLevel", this.waterLevel);

    }

    @Override
    public int getMaxFoodLevel() {
        return this.maxFoodLevel;
    }

    @Override
    public int getFoodLevel() {
        return this.foodLevel;
    }

    @Override
    public int getWaterLevel() {
        return this.waterLevel;
    }

    @Override
    public int getMaxWaterLevel() {
        return this.maxWaterLevel;
    }

    @Override
    public float getSaturationLevel() {
        return this.saturationLevel;
    }

    @Override
    public void setMaxFoodLevel(int MaxFoodLevel) {
        this.maxFoodLevel = MaxFoodLevel;
    }

    @Override
    public void setMaxWaterLevel(int MaxWaterLevel) {
        this.maxWaterLevel = MaxWaterLevel;
    }

    @Override
    public void setWaterLevel(int water) {
        this.waterLevel = water;
    }

    @Inject(method = "isNotFull", at = @At("HEAD"), cancellable = true)
    public void isNotFull(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.saturationLevel < this.maxFoodLevel);
    }

}
