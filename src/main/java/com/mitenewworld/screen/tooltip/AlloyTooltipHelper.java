package com.mitenewworld.screen.tooltip;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.registry.ModItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Map;

@Environment(EnvType.CLIENT)
public final class AlloyTooltipHelper {
    private AlloyTooltipHelper() {}

    public static final int COLOR_POSITIVE = 0x5555FF;   // 蓝
    public static final int COLOR_NEGATIVE = 0xFF5555;   // 红
    public static final int COLOR_NEUTRAL  = 0xAAAAAA;   // 灰

    private static final Map<String, Integer> SYNERGY_COLORS = Map.ofEntries(
            Map.entry("durable",     0xFFAA00),
            Map.entry("sharp",       0xFF5555),
            Map.entry("conductive",  0x55FFFF),
            Map.entry("bronze",      0xCC8844),
            Map.entry("ornate",      0xFF55FF),
            Map.entry("masterwork",  0xFFD700),
            Map.entry("aerospace",   0x88FFFF),
            Map.entry("plated",      0xDDDDDD),
            Map.entry("celestial",   0xCC88FF),
            Map.entry("primordial",  0x8844CC),
            Map.entry("nugget",      0xAAAAAA),
            Map.entry("block",       0xAAAAAA),
            Map.entry("chain",       0xAAAAAA),
            Map.entry("quality_inferior",  0x808080),
            Map.entry("quality_common",    0xFFFFFF),
            Map.entry("quality_fine",      0x55FF55),
            Map.entry("quality_excellent", 0x5555FF),
            Map.entry("quality_epic",      0xAA00AA),
            Map.entry("quality_legendary", 0xFFAA00)
    );

    public static int synergyColor(String id) {
        return SYNERGY_COLORS.getOrDefault(id, COLOR_NEUTRAL);
    }

    public static boolean isShiftDown() {
        MinecraftClient c = MinecraftClient.getInstance();
        return c.player != null && c.player.isSneaking();
    }

    public static boolean isHoldingMagnifier() {
        MinecraftClient c = MinecraftClient.getInstance();
        if (c.player == null) {
            return false;
        }
        return c.player.getMainHandStack().isOf(ModItems.GLASS_FRAGMENT)
                || c.player.getOffHandStack().isOf(ModItems.GLASS_FRAGMENT);
    }

    public static Text header(String key) {
        return Text.translatable(key).formatted(Formatting.GRAY);
    }

    public static Text positive(String key, Object... args) {
        return Text.translatable(key, args).withColor(COLOR_POSITIVE);
    }

    public static Text negative(String key, Object... args) {
        return Text.translatable(key, args).withColor(COLOR_NEGATIVE);
    }

    public static Text neutral(String key, Object... args) {
        return Text.translatable(key, args).withColor(COLOR_NEUTRAL);
    }

    public static Text synergyLine(String id) {
        return Text.empty()
                .append(Text.literal("  "))
                .append(Text.translatable("synergy.mitenewworld." + id))
                .withColor(synergyColor(id));
    }

    public static Text metalLine(String id, float ratio) {
        return Text.empty()
                .append(Text.literal("  "))
                .append(Text.translatable("metal.mitenewworld." + id))
                .append(Text.literal(": " + pct(ratio)))
                .withColor(COLOR_NEUTRAL);
    }

    public static String fmt(float v) {
        if (Math.abs(v - Math.round(v)) < 0.005f) {
            return String.valueOf(Math.round(v));
        }
        return String.format("%.2f", v);
    }

    public static String pct(float v) {
        return String.format("%.1f%%", v * 100);
    }
}
