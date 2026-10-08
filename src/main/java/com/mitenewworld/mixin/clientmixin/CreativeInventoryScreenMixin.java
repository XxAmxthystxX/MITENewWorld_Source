package com.mitenewworld.mixin.clientmixin;
import com.mitenewworld.MITENewWorld;


import com.mitenewworld.screen.ModInventoryScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CreativeInventoryScreen.class)
public class CreativeInventoryScreenMixin {

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private void setScreenOfinit(MinecraftClient instance, Screen screen) {
        instance.setScreen(new ModInventoryScreen(instance.player));
    }
    @Redirect(method = "handledScreenTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private void setScreenOfHandledScreenTick(MinecraftClient instance, Screen screen) {
        instance.setScreen(new ModInventoryScreen(instance.player));
    }
}
