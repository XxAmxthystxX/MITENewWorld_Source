package com.mitenewworld.screen.tooltip;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.item.metal.AlloyComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map; /**
 * 二级数据：金属配比 + 共鸣。
 * 三个组件（Armor / Weapon / Material）的 secondary 都返回这个。
 */
@Environment(EnvType.CLIENT)
public final class AlloyDetailTipComponent extends AbstractAlloyTooltipComponent {

    private AlloyDetailTipComponent(List<Text> lines) {
        super(lines);
    }

    public static AlloyDetailTipComponent of(AlloyComponent comp) {
        List<Text> lines = new ArrayList<>();

        // 金属配比
        lines.add(AlloyTooltipHelper.header("tooltip.mitenewworld.alloy.metals"));
        for (Map.Entry<String, Float> e : comp.metals().entrySet()) {
            lines.add(AlloyTooltipHelper.metalLine(e.getKey(), e.getValue()));
        }

        // 共鸣
        List<String> syn = comp.activeSynergies();
        if (!syn.isEmpty()) {
            lines.add(AlloyTooltipHelper.header("tooltip.mitenewworld.alloy.synergies"));
            for (String id : syn) {
                lines.add(AlloyTooltipHelper.synergyLine(id));
            }
        }

        return new AlloyDetailTipComponent(lines);
    }
}
