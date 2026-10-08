package com.mitenewworld.mixin.worldmixin;
import com.mitenewworld.MITENewWorld;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * 只补心形粒子：血量由原版 heal() 自己加。
     * （原写法在 HEAD 里先 setHealth(f + amount) 又不 cancel，原版随后再加一次，等于回血翻倍）
     */
    @Inject(method = "heal", at = @At("HEAD"))
    public void heal(float amount, CallbackInfo ci) {
        if (amount <= 0.0F || this.getHealth() <= 0.0F) {
            return;
        }
        double a = this.random.nextGaussian() * 0.02;
        double b = this.random.nextGaussian() * 0.02;
        double c = this.random.nextGaussian() * 0.02;
        this.getEntityWorld().addParticleClient(ParticleTypes.HEART, this.getParticleX(1.0), this.getRandomBodyY() + 0.5, this.getParticleZ(1.0), a, b, c);
    }

    @Redirect(method = "damageEquipment", at = @At(value = "INVOKE" , target = "Ljava/lang/Math;max(FF)F"))
    public float damageNum(float a, float b) {
        return Math.max(a , b * 4);
    }
    @Shadow
    public void setHealth(float v) {
    }

    @Shadow
    public float getHealth() {
        return 0.0F;
    }
}
