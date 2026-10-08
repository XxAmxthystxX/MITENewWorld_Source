package com.mitenewworld.mixin.clientmixin;
import com.mitenewworld.MITENewWorld;


import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "canSprint()Z", at = @At("HEAD"), cancellable = true)
    private void canSprint(CallbackInfoReturnable<Boolean> cir) {
        cir.cancel();
        boolean bl = this.hasVehicle() || this.getAbilities().allowFlying || this.getHungerManager().getFoodLevel() > 0;
        cir.setReturnValue(bl);
    }


}
