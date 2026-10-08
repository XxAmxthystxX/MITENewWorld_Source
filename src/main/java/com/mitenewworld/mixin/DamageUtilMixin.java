package com.mitenewworld.mixin;
import com.mitenewworld.MITENewWorld;


import com.mitenewworld.item.metal.ModArmorItem;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DamageUtil.class)
public class DamageUtilMixin {

    @Inject(method = "getDamageLeft", at = @At("HEAD"), cancellable = true)
    private static void getDamageLeft(LivingEntity armorWearer, float damageAmount, DamageSource damageSource, float armor, float armorToughness, CallbackInfoReturnable<Float> cir) {
        cir.cancel();
        float result;
        result = ModArmorItem.handleDamage(armorWearer, damageAmount, damageSource, armor, armorToughness);
        cir.setReturnValue(result);
    }

}
