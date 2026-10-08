package com.mitenewworld.mixin;
import com.mitenewworld.MITENewWorld;


import net.minecraft.component.type.FoodComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FoodComponent.Builder.class)
public class FoodComponentMixin {

    @Shadow
    private int nutrition;
    @Shadow
    private float saturationModifier;
    @Shadow
    private boolean canAlwaysEat;
    @Inject(method = "build", at = @At("RETURN"), cancellable = true)
    private void modifyBuildMethod(CallbackInfoReturnable<FoodComponent> cir) {
        float Saturation = this.calculateSaturation();
        FoodComponent modifiedComponent = new FoodComponent(this.nutrition, Saturation, this.canAlwaysEat);
        cir.setReturnValue(modifiedComponent);
    }


    @Unique
    private float calculateSaturation() {
        return this.saturationModifier ;
    }
}

