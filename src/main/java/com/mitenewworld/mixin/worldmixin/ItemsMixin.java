package com.mitenewworld.mixin.worldmixin;


import com.mitenewworld.MITENewWorld;
import com.mitenewworld.cover.ItemsCover;
import com.mitenewworld.registry.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(Items.class)
public class ItemsMixin {
    @Inject(method = "<clinit>",at = @At("HEAD"))
    private static void CoverRegister(CallbackInfo ci) {
        //ItemsCover.registeritemsCover();
    }

    @Inject(method = "register(Lnet/minecraft/registry/RegistryKey;Ljava/util/function/Function;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;", at = @At("HEAD"), cancellable = true)
    private static void CoverRegister(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings, CallbackInfoReturnable<Item> cir) {
        cir.cancel();
        Item item2 = ItemsCover.ItemCoverMap.getOrDefault(key, null);
        if (item2 != null) {
            cir.setReturnValue(item2);
        } else {
            MITENewWorld.LOGGER.warn("Item not found in ItemCoverMap: {}", key.getValue().getPath());
            cir.setReturnValue(ItemsCover.register(key, factory, settings));
        }
    }
}
