package com.mitenewworld.mixin.worldmixin;
import com.mitenewworld.MITENewWorld;


import com.mitenewworld.item.metal.ModToolItem;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    @Mutable
    @Shadow
    @Final
    public final PlayerEntity player ;


    @Shadow
    @Final
    private DefaultedList<ItemStack> main;

    public PlayerInventoryMixin(PlayerEntity player) {
        this.player = player;
    }

    @Inject(method = "setSelectedSlot" , at = @At("RETURN"))
    public void setSelectedSlot(int slot, CallbackInfo ci) {
        ItemStack itemStack = this.main.get(slot);
        double attackRange = 0.0;
        if (itemStack != ItemStack.EMPTY && itemStack.getItem() instanceof ModToolItem) {
            attackRange = ((ModToolItem) itemStack.getItem()).toolType().attackRange();
        }
        player.getAttributeInstance(EntityAttributes.ENTITY_INTERACTION_RANGE).setBaseValue(MathHelper.clamp(3 + attackRange , 3, 20));
    }
}
