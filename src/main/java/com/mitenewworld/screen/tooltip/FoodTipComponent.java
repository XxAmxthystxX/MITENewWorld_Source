package com.mitenewworld.screen.tooltip;
import com.mitenewworld.MITENewWorld;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public final class FoodTipComponent extends AbstractAlloyTooltipComponent {

    private FoodTipComponent(List<Text> lines) {
        super(lines);
    }

    /** 饱食度 + 饱和度 */
    public static FoodTipComponent of(int nutrition, float saturation) {
        List<Text> lines = new ArrayList<>();
        lines.add(AlloyTooltipHelper.header("tooltip.mitenewworld.food.header"));
        lines.add(AlloyTooltipHelper.positive("tooltip.mitenewworld.food.nutrition",
                String.valueOf(nutrition)));
        lines.add(AlloyTooltipHelper.positive("tooltip.mitenewworld.food.saturation",
                String.valueOf(Math.round(saturation))));
        return new FoodTipComponent(lines);
    }
}
