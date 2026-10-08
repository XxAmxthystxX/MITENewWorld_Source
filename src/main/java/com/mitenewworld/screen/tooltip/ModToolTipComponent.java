package com.mitenewworld.screen.tooltip;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.item.metal.AlloyComponent;
import com.mitenewworld.item.metal.ModToolItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public final class ModToolTipComponent extends AbstractAlloyTooltipComponent {

    private ModToolTipComponent(List<Text> lines) {
        super(lines);
    }

    /** 一级：伤害、攻击距离加成、耐久、重量 */
    public static ModToolTipComponent primary(ItemStack stack, AlloyComponent comp) {
        List<Text> lines = new ArrayList<>();
        lines.add(AlloyTooltipHelper.header("tooltip.mitenewworld.weapon.header"));
        if (stack.getItem() instanceof ModToolItem modToolItem) {
            float damage = comp.hardness()
                    * modToolItem.toolType().attackMultiplier()
                    * ModToolItem.ATTACK_SCALE;
            float rangeBonus = (float) (modToolItem.toolType().attackRange() - ModToolItem.VANILLA_INTERACTION_RANGE);

            lines.add(AlloyTooltipHelper.positive("tooltip.mitenewworld.weapon.damage",
                    AlloyTooltipHelper.fmt(damage)));
            lines.add(rangeBonus >= 0
                    ? AlloyTooltipHelper.positive("tooltip.mitenewworld.weapon.range",
                    AlloyTooltipHelper.fmt(rangeBonus))
                    : AlloyTooltipHelper.negative("tooltip.mitenewworld.weapon.range",
                    AlloyTooltipHelper.fmt(rangeBonus)));
            lines.add(AlloyTooltipHelper.positive("tooltip.mitenewworld.weapon.durability",
                    String.valueOf(stack.getMaxDamage())));
            lines.add(AlloyTooltipHelper.negative("tooltip.mitenewworld.weight",
                    AlloyTooltipHelper.fmt(comp.weight())));
        }
        return new ModToolTipComponent(lines);
    }


}
