package com.mitenewworld.item;

import com.mitenewworld.item.component.ModDataComponentTypes;
import com.mitenewworld.item.component.ModTooltipData;
import com.mitenewworld.item.component.OreComponent;
import com.mitenewworld.screen.tooltip.ScrapMetalTipComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Optional;

/**
 * 废金属：铸造台打爆 / 合金炉报废时的残余金属。
 *
 * <p>物品上带 {@link OreComponent}（Kind.SCRAP，锭当量配比）。
 * 展示逻辑：
 * <ul>
 *   <li>名称：废金属（主导金属 · 总锭当量），如「废金属（铁 · 7.5锭当量）」</li>
 *   <li>tooltip：完整金属配比 + 重量（始终显示，无需潜行）</li>
 * </ul>
 */
public class ScrapMetalItem extends Item {

    public ScrapMetalItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        OreComponent scrap = stack.get(ModDataComponentTypes.ORE_COMPONENT);
        if (scrap == null || scrap.dominant() == null) {
            return super.getName(stack);
        }

        MutableText name = Text.empty().append(super.getName(stack));
        name.append(Text.literal("（")
                .append(Text.translatable("metal.mitenewworld." + scrap.dominant()))
                .append(Text.literal(" · " + fmtUnits(scrap.totalUnits()) + "锭当量）"))
                .formatted(Formatting.GRAY));
        return name;
    }

    /** 锭当量格式：整数去掉小数，其余保留 1 位 */
    private static String fmtUnits(float v) {
        if (Math.abs(v - Math.round(v)) < 0.05f) {
            return String.valueOf(Math.round(v));
        }
        return String.format("%.1f", v);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        OreComponent scrap = stack.get(ModDataComponentTypes.ORE_COMPONENT);
        if (scrap == null) {
            return Optional.empty();
        }
        return Optional.of(new ModTooltipData(ScrapMetalTipComponent.of(scrap)));
    }
}
