package com.mitenewworld.item.component;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.item.metal.ModAlloyMetals;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import java.util.HashMap;
import java.util.Map;

/**
 * 铸造台物品组件：金属配比 + 台子等级 + 耐久上限/当前耐久。
 * 与合金系统同构：一个物品 + 一个组件描述它的全部信息。
 */
public record CastingTableComponent(Map<String, Float> metals, int level,
                                    float maxDurability, float durability) {

    public static final Codec<CastingTableComponent> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.unboundedMap(Codec.STRING, Codec.FLOAT)
                    .fieldOf("metals").forGetter(CastingTableComponent::metals),
            Codec.INT.fieldOf("level").forGetter(CastingTableComponent::level),
            Codec.FLOAT.fieldOf("max_durability").forGetter(CastingTableComponent::maxDurability),
            Codec.FLOAT.fieldOf("durability").forGetter(CastingTableComponent::durability)
    ).apply(i, CastingTableComponent::new));

    public static final PacketCodec<RegistryByteBuf, CastingTableComponent> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.map(HashMap::new, PacketCodecs.STRING, PacketCodecs.FLOAT),
            CastingTableComponent::metals,
            PacketCodecs.VAR_INT, CastingTableComponent::level,
            PacketCodecs.FLOAT, CastingTableComponent::maxDurability,
            PacketCodecs.FLOAT, CastingTableComponent::durability,
            CastingTableComponent::new
    );

    /** 主导金属（用于损坏掉落废金属） */
    public String dominant() {
        return metals.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("iron");
    }

    public static CastingTableComponent fresh(Map<String, Float> metals, int level, float maxDurability) {
        return new CastingTableComponent(metals, level, maxDurability, maxDurability);
    }

    /** 金属加权平均色（用于方块/物品调色） */
    public int color() {
        return colorOf(metals);
    }

    public static int colorOf(Map<String, Float> metals) {
        float r = 0f, g = 0f, b = 0f;
        for (var e : metals.entrySet()) {
            ModAlloyMetals.MetalType m = ModAlloyMetals.byId(e.getKey());
            if (m == null) {
                continue;
            }
            float w = e.getValue();
            int c = m.color();
            r += ((c >> 16) & 0xFF) * w;
            g += ((c >> 8) & 0xFF) * w;
            b += (c & 0xFF) * w;
        }
        return (clamp8(r) << 16) | (clamp8(g) << 8) | clamp8(b);
    }

    private static int clamp8(float v) {
        return Math.clamp(Math.round(v), 0, 255);
    }
}
