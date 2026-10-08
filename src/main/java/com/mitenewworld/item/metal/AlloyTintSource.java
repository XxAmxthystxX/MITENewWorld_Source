package com.mitenewworld.item.metal;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.item.component.ModDataComponentTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;

public record AlloyTintSource() implements TintSource {

        // 2. 定义序列化编解码器（必须）
        public static final MapCodec<AlloyTintSource> CODEC = MapCodec.unit(AlloyTintSource::new);
        
        @Override
        public int getTint(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity user) {
            // 优先读 DYED_COLOR（穿戴模型与物品栏两条管线共用同一来源）
            DyedColorComponent dyed = stack.get(DataComponentTypes.DYED_COLOR);
            if (dyed != null) {
                return 0xFF000000 | dyed.rgb();
            }

            // 兜底：从合金配比现算
            AlloyComponent comp = stack.get(ModDataComponentTypes.ALLOY_COMPONENT);
            if (comp == null) {
                return 0xFFFFFFFF;
            } // 默认白色（不染色）

            return colorOf(comp);
        }

        public static int colorOf(AlloyComponent comp) {
            float r = 0f, g = 0f, b = 0f;
            for (var entry : comp.metals().entrySet()) {
                ModAlloyMetals.MetalType metal = ModAlloyMetals.byId(entry.getKey());
                if (metal == null) {
                    continue;
                }
                float weight = entry.getValue();
                int color = metal.color();
                r += ColorHelper.getRed(color) * weight;
                g += ColorHelper.getGreen(color) * weight;
                b += ColorHelper.getBlue(color) * weight;
            }
            int red = (int) Math.round(r);
            int green = (int) Math.round(g);
            int blue = (int) Math.round(b);
            return ColorHelper.getArgb(255, red, green, blue);
        }

        @Override
        public MapCodec<? extends TintSource> getCodec() {
            return CODEC;
        }
    }
