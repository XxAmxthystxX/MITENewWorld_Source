package com.mitenewworld.item.metal;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.item.component.ModDataComponentTypes;
import com.mitenewworld.item.metal.AlloyComponent;
import com.mitenewworld.item.metal.ModToolItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class ModDaggerItem extends ModToolItem implements ProjectileItem {

    public ModDaggerItem(ToolType toolType, Item.Settings settings) {
        super(toolType, settings);
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        AlloyComponent comp = stack.get(ModDataComponentTypes.ALLOY_COMPONENT);
        if (comp == null) {
            return;
        }

        float toughness = comp.toughness();
        int base = toolType.attackWearBase() + 5;
        int loss = Math.max(1, Math.round(base / (1f + toughness)));

        stack.damage(loss, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        return null;   // TODO
    }

}
