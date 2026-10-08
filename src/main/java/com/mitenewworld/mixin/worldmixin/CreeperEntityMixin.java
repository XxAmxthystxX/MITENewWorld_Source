package com.mitenewworld.mixin.worldmixin;
import com.mitenewworld.MITENewWorld;


import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreeperEntity.class)
public class CreeperEntityMixin {
    @Shadow
    private int fuseTime ;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        fuseTime = 10;
    }
    @Inject(method = "createCreeperAttributes" , at = @At("HEAD"), cancellable = true)
    private static void createCreeperAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.cancel();
        cir.setReturnValue(HostileEntity.createHostileAttributes().add(EntityAttributes.MOVEMENT_SPEED, 0.4));

	}
}
