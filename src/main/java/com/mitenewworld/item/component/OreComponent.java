package com.mitenewworld.item.component;
import com.mitenewworld.MITENewWorld;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mitenewworld.item.metal.ModAlloyMetals;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * 粗矿 / 废金属共用的组件。
 * metals: 单个物品中每种金属的锭当量（1.0 = 1 锭）。
 * kind: 区分粗矿和废金属的语义。
 */
public record OreComponent(Map<String, Float> metals, Kind kind) {

    public enum Kind { ORE, SCRAP }

    // ============================================================
    //  序列化
    // ============================================================

    public static final Codec<OreComponent> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.unboundedMap(Codec.STRING, Codec.FLOAT)
                    .fieldOf("metals").forGetter(OreComponent::metals),
            Codec.STRING.xmap(Kind::valueOf, Enum::name)
                    .fieldOf("kind").forGetter(OreComponent::kind)
    ).apply(i, OreComponent::new));

    public static final PacketCodec<RegistryByteBuf, OreComponent> PACKET_CODEC =
            PacketCodec.of(OreComponent::write, OreComponent::read);

    private static void write(OreComponent comp, RegistryByteBuf buf) {
        buf.writeVarInt(comp.metals().size());
        comp.metals().forEach((k, v) -> {
            buf.writeString(k);
            buf.writeFloat(v);
        });
        buf.writeEnumConstant(comp.kind());
    }

    private static OreComponent read(RegistryByteBuf buf) {
        int size = buf.readVarInt();
        Map<String, Float> metals = new LinkedHashMap<>(size);
        for (int i = 0; i < size; i++) {
            metals.put(buf.readString(), buf.readFloat());
        }
        Kind kind = buf.readEnumConstant(Kind.class);
        return new OreComponent(metals, kind);
    }

    // ============================================================
    //  派生
    // ============================================================

    /** 总锭当量 */
    public float totalUnits() {
        float sum = 0f;
        for (float v : metals.values()) {
            sum += v;
        }
        return sum;
    }

    /** 主导金属 id */
    @Nullable
    public String dominant() {
        String best = null;
        float max = 0f;
        for (var e : metals.entrySet()) {
            if (e.getValue() > max) { max = e.getValue(); best = e.getKey(); }
        }
        return best;
    }

    /** 主导金属的占比 */
    public float dominantRatio() {
        String d = dominant();
        if (d == null) {
            return 0f;
        }
        float total = totalUnits();
        return total <= 0f ? 0f : metals.get(d) / total;
    }

    /** 重量 = Σ(metal.weight × 锭当量) */
    public float weight() {
        float w = 0f;
        for (var e : metals.entrySet()) {
            var m = ModAlloyMetals.byId(e.getKey());
            if (m != null) {
                w += m.weight() * e.getValue();
            }
        }
        return w;
    }

    /** 平均百分比（占该物品的锭当量），count 是栈内数量 */
    public float average(int count) {
        return count <= 0 ? 0f : totalUnits() / count;
    }

    // ============================================================
    //  工厂
    // ============================================================

    /** 粗矿：单一金属 */
    public static OreComponent ore(String metal, float percent) {
        return new OreComponent(new LinkedHashMap<>(Map.of(metal, percent)), Kind.ORE);
    }

    /** 废金属：保留残余配比 */
    public static OreComponent scrap(Map<String, Float> residual) {
        return new OreComponent(new LinkedHashMap<>(residual), Kind.SCRAP);
    }
}
