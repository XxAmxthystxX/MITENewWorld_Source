package com.mitenewworld.screen.tooltip;

import com.mitenewworld.item.component.OreComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 废金属 tooltip：金属成分（锭当量占比）+ 重量。
 * 始终显示，与合金锭的配比展示保持一致。
 */
@Environment(EnvType.CLIENT)
public final class ScrapMetalTipComponent extends AbstractAlloyTooltipComponent {

    private ScrapMetalTipComponent(List<Text> lines) {
        super(lines);
    }

    public static ScrapMetalTipComponent of(OreComponent scrap) {
        List<Text> lines = new ArrayList<>();

        float total = Math.max(0.0001f, scrap.totalUnits());
        lines.add(AlloyTooltipHelper.header("tooltip.mitenewworld.scrap.header"));
        for (Map.Entry<String, Float> e : scrap.metals().entrySet()) {
            lines.add(AlloyTooltipHelper.metalLine(e.getKey(), e.getValue() / total));
        }
        lines.add(AlloyTooltipHelper.neutral("tooltip.mitenewworld.scrap.total",
                String.valueOf(AlloyTooltipHelper.fmt(scrap.totalUnits()))));
        lines.add(AlloyTooltipHelper.negative("tooltip.mitenewworld.weight",
                AlloyTooltipHelper.fmt(scrap.weight())));

        return new ScrapMetalTipComponent(lines);
    }
}
