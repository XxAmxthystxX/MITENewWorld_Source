package com.mitenewworld.mixin.clientmixin;
import com.mitenewworld.MITENewWorld;


import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin   {

    @Inject(method = "onHealthUpdate", at = @At("HEAD"), cancellable = true)
    public void onHealthUpdate(HealthUpdateS2CPacket packet, CallbackInfo ci) {
        ci.cancel();
    }

}
