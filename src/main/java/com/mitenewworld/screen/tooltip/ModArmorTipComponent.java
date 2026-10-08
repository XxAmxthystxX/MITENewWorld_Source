package com.mitenewworld.screen.tooltip;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.item.metal.AlloyComponent;
import com.mitenewworld.item.metal.ModArmorItem;
import com.mitenewworld.item.metal.ModToolItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public final class ModArmorTipComponent extends AbstractAlloyTooltipComponent {

    private ModArmorTipComponent(List<Text> lines) {
        super(lines);
    }

    /** 一级：护甲值、韧性、抗击退、耐久、重量 */
    public static ModArmorTipComponent primary(ItemStack stack, AlloyComponent comp) {
        List<Text> lines = new ArrayList<>();
        lines.add(AlloyTooltipHelper.header("tooltip.mitenewworld.armor.header"));

        float armorValue = comp.hardness() / ModArmorItem.ARMOR_VALUE_SCALE;
        float toughness = comp.toughness();
        float knockback = toughness * 0.15f;

        lines.add(AlloyTooltipHelper.positive("tooltip.mitenewworld.armor.value",
                AlloyTooltipHelper.fmt(armorValue)));
        lines.add(AlloyTooltipHelper.positive("tooltip.mitenewworld.armor.toughness",
                AlloyTooltipHelper.fmt(toughness)));
        lines.add(AlloyTooltipHelper.positive("tooltip.mitenewworld.armor.knockback",
                AlloyTooltipHelper.fmt(knockback)));
        lines.add(AlloyTooltipHelper.positive("tooltip.mitenewworld.armor.durability",
                String.valueOf(stack.getMaxDamage())));
        lines.add(AlloyTooltipHelper.negative("tooltip.mitenewworld.weight",
                AlloyTooltipHelper.fmt(comp.weight())));

        return new ModArmorTipComponent(lines);
    }


}
