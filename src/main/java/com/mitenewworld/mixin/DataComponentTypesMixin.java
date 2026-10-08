package com.mitenewworld.mixin;
import com.mitenewworld.MITENewWorld;


import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.util.Rarity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.component.DataComponentTypes.*;

@Mixin(DataComponentTypes.class)
public class DataComponentTypesMixin {
    @Mutable
    @Final
    @Shadow
    public static ComponentMap DEFAULT_ITEM_COMPONENTS = null;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void addComponents(CallbackInfo ci) {
        DEFAULT_ITEM_COMPONENTS = ComponentMap.builder()
                .add(MAX_STACK_SIZE, 16)
                .add(LORE, LoreComponent.DEFAULT)
                .add(ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT)
                .add(REPAIR_COST, 0)
                .add(ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT)
                .add(RARITY, Rarity.COMMON)
                .build();
    }
}
