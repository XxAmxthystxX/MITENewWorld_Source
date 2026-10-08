package com.mitenewworld.screen.tooltip;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.registry.ModItems;
import com.mitenewworld.item.metal.AlloyComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public abstract class AbstractAlloyTooltipComponent implements TooltipComponent {
    protected final List<Text> lines;

    protected AbstractAlloyTooltipComponent(List<Text> lines) {
        this.lines = lines;
    }

    @Override
    public int getHeight(TextRenderer textRenderer) {
        return lines.size() * 10;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        int max = 0;
        for (Text line : lines) {
            int w = textRenderer.getWidth(line);
            if (w > max) {
                max = w;
            }
        }
        return max;
    }

    @Override
    public void drawText(DrawContext context, TextRenderer textRenderer, int x, int y) {
        for (int i = 0; i < lines.size(); i++) {
            context.drawTextWithShadow(textRenderer, lines.get(i).asOrderedText(),
                    x, y + i * 10, 0xFFFFFFFF);
        }
    }
}





