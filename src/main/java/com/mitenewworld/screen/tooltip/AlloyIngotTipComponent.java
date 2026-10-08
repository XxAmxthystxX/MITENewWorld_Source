package com.mitenewworld.screen.tooltip;

import com.mitenewworld.item.metal.AlloyComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 合金材料（粒 / 锭 / 块 / 链条）tooltip：
 * 一级：金属配比（始终显示）；
 * 手持玻璃片（放大镜）时追加「当合成为工具时：」属性块，
 * 展示合成工具 / 盔甲时会继承的固化属性。
 */
@Environment(EnvType.CLIENT)
public final class AlloyIngotTipComponent extends AbstractAlloyTooltipComponent {

    private AlloyIngotTipComponent(List<Text> lines) {
        super(lines);
    }

    public static AlloyIngotTipComponent of(AlloyComponent comp, boolean magnifier) {
        List<Text> lines = new ArrayList<>();

        // 金属配比：始终显示
        lines.add(AlloyTooltipHelper.header("tooltip.mitenewworld.alloy.metals"));
        for (Map.Entry<String, Float> e : comp.metals().entrySet()) {
            lines.add(AlloyTooltipHelper.metalLine(e.getKey(), e.getValue()));
        }

        // 手持玻璃片：固化属性预览 + 生效共鸣
        if (magnifier) {
            lines.add(AlloyTooltipHelper.header("tooltip.mitenewworld.alloy.tool_header"));
            lines.add(AlloyTooltipHelper.positive("tooltip.mitenewworld.alloy.hardness",
                    AlloyTooltipHelper.fmt(comp.hardness())));
            lines.add(AlloyTooltipHelper.positive("tooltip.mitenewworld.alloy.toughness",
                    AlloyTooltipHelper.fmt(comp.toughness())));
            lines.add(AlloyTooltipHelper.positive("tooltip.mitenewworld.alloy.durability",
                    AlloyTooltipHelper.fmt(comp.durability())));
            lines.add(AlloyTooltipHelper.positive("tooltip.mitenewworld.alloy.enchant",
                    AlloyTooltipHelper.fmt(comp.enchantRarity())));
            lines.add(AlloyTooltipHelper.negative("tooltip.mitenewworld.weight",
                    AlloyTooltipHelper.fmt(comp.weight())));

            List<String> syn = comp.activeSynergies();
            if (!syn.isEmpty()) {
                lines.add(AlloyTooltipHelper.header("tooltip.mitenewworld.alloy.synergies"));
                for (String id : syn) {
                    lines.add(AlloyTooltipHelper.synergyLine(id));
                }
            }
        }

        return new AlloyIngotTipComponent(lines);
    }
}
