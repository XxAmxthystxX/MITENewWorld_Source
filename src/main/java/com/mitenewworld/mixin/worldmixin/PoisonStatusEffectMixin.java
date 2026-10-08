package com.mitenewworld.mixin.worldmixin;
import com.mitenewworld.MITENewWorld;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.PoisonStatusEffect;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PoisonStatusEffect.class)
public class PoisonStatusEffectMixin {

    @Inject(method = "applyUpdateEffect", at = @At("HEAD"), cancellable = true)
    private void onApplyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier, CallbackInfoReturnable<Boolean> cir) {
        if (entity.isAlive()) {
            entity.damage(world, entity.getDamageSources().magic(), 1.0f);
        }
        cir.setReturnValue(true);
    }
}
