package com.mitenewworld.item.component;
import com.mitenewworld.screen.tooltip.AbstractAlloyTooltipComponent;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.item.metal.AlloyComponent;
import com.mitenewworld.item.metal.ModArmorItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record ModTooltipData(AbstractAlloyTooltipComponent tooltipComponent) implements TooltipData {
}

