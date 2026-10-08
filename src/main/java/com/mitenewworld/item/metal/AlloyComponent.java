package com.mitenewworld.item.metal;
import com.mitenewworld.MITENewWorld;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

import java.util.*;

public record AlloyComponent(
        // === 核心数据 ===
        Map<String, Float> metals,
        Set<String> forcedSynergies,

        // === 固化属性 ===
        float durability,
        float toughness,
        float hardness,
        float enchantRarity,
        float weight,

        // === 共鸣列表 ===
        List<String> activeSynergies
) {
    private static final float QUANTIZE = 1000f;
    private static final float ZERO_THRESHOLD = 0.0005f;
    private static final float R_MIN = 0.1f;
    private static final float R_MAX = 2.0f;
    private static final Codec<Set<String>> STRING_SET_CODEC =
            Codec.STRING.listOf().xmap(LinkedHashSet::new, ArrayList::new);

    public static final Codec<AlloyComponent> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.unboundedMap(Codec.STRING, Codec.FLOAT)
                    .fieldOf("metals").forGetter(AlloyComponent::metals),
            STRING_SET_CODEC.optionalFieldOf("forced_synergies", Set.of())
                    .forGetter(AlloyComponent::forcedSynergies),
            Codec.FLOAT.fieldOf("durability").forGetter(AlloyComponent::durability),
            Codec.FLOAT.fieldOf("toughness").forGetter(AlloyComponent::toughness),
            Codec.FLOAT.fieldOf("hardness").forGetter(AlloyComponent::hardness),
            Codec.FLOAT.fieldOf("enchant_rarity").forGetter(AlloyComponent::enchantRarity),
            Codec.FLOAT.fieldOf("weight").forGetter(AlloyComponent::weight),
            Codec.STRING.listOf().fieldOf("synergies").forGetter(AlloyComponent::activeSynergies)
    ).apply(i, AlloyComponent::new));

    public static final PacketCodec<RegistryByteBuf, AlloyComponent> PACKET_CODEC =
            PacketCodec.of(AlloyComponent::write, AlloyComponent::read);

    private static void write(AlloyComponent comp, RegistryByteBuf buf) {
        // metals
        buf.writeVarInt(comp.metals().size());
        comp.metals().forEach((k, v) -> {
            buf.writeString(k);
            buf.writeFloat(v);
        });
        // forcedSynergies
        buf.writeVarInt(comp.forcedSynergies().size());
        comp.forcedSynergies().forEach(buf::writeString);
        // 固化属性
        buf.writeFloat(comp.durability());
        buf.writeFloat(comp.toughness());
        buf.writeFloat(comp.hardness());
        buf.writeFloat(comp.enchantRarity());
        buf.writeFloat(comp.weight());
        // activeSynergies
        buf.writeVarInt(comp.activeSynergies().size());
        comp.activeSynergies().forEach(buf::writeString);
    }

    private static AlloyComponent read(RegistryByteBuf buf) {
        // metals
        int metalsSize = buf.readVarInt();
        Map<String, Float> metals = new LinkedHashMap<>(metalsSize);
        for (int i = 0; i < metalsSize; i++) {
            metals.put(buf.readString(), buf.readFloat());
        }
        // forcedSynergies
        int forcedSize = buf.readVarInt();
        Set<String> forcedSynergies = new LinkedHashSet<>(forcedSize);
        for (int i = 0; i < forcedSize; i++) {
            forcedSynergies.add(buf.readString());
        }
        // 固化属性
        float durability    = buf.readFloat();
        float toughness     = buf.readFloat();
        float hardness      = buf.readFloat();
        float enchantRarity = buf.readFloat();
        float weight        = buf.readFloat();
        // activeSynergies
        int synSize = buf.readVarInt();
        List<String> activeSynergies = new ArrayList<>(synSize);
        for (int i = 0; i < synSize; i++) {
            activeSynergies.add(buf.readString());
        }

        return new AlloyComponent(
                metals, forcedSynergies,
                durability, toughness, hardness, enchantRarity, weight,
                activeSynergies);
    }
    // ============================================================
    //  派生方法（现算）
    // ============================================================

    /** 占比最高的金属 */
    public String dominant() {
        return metals.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("unknown");
    }

    /** ≥30% 且按降序、最多 3 个的金属 id 列表 */
    public List<String> display() {
        return metals.entrySet().stream()
                .filter(e -> e.getValue() >= 0.30f - 1e-4f)
                .sorted(Map.Entry.<String, Float>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();
    }

    /** 混合类型 */
    public MixType mixType() {
        return MixType.of(metals.size());
    }

    /** 加权平均颜色 */
    public int color() {
        float r = 0f, g = 0f, b = 0f;
        for (var e : metals.entrySet()) {
            ModAlloyMetals.MetalType m = ModAlloyMetals.byId(e.getKey());
            if (m == null) {
                continue;
            }
            float w = e.getValue();
            int c = m.color();
            r += ((c >> 16) & 0xFF) * w;
            g += ((c >>  8) & 0xFF) * w;
            b += ( c        & 0xFF) * w;
        }
        return (clamp8(r) << 16) | (clamp8(g) << 8) | clamp8(b);
    }

    /** ≥30% 的金属集合 */
    public Set<String> majorMetals() {
        Set<String> result = new LinkedHashSet<>();
        for (var e : metals.entrySet()) {
            if (e.getValue() >= 0.30f - 1e-4f) {
                result.add(e.getKey());
            }
        }
        return result;
    }


    /** 从原始配比融合出完整组件。唯一调用抗性链的地方。 */
    public static AlloyComponent compute(Map<String, Float> raw, Set<String> forcedSynergies) {
        Map<String, Float> quantized = quantize(normalize(raw));

        MixType mixType = MixType.of(quantized.size());

        AlloyProperties draft = new AlloyProperties();
        computeResistanceChain(quantized, draft);
        applyThresholdBonuses(quantized, draft);
        draft.scaleAll(mixType.multiplier, mixType.multiplier);
        draft.weight = computeWeight(quantized);

        // 自然共鸣（构造临时组件用于触发判定）
        AlloyComponent temp = new AlloyComponent(
                quantized, forcedSynergies,
                draft.durability, draft.toughness, draft.hardness,
                draft.enchantRarity, draft.weight,
                List.of());

        for (ModSynergies.SynergyEffect syn : ModSynergies.triggeredNatural(temp)) {
            syn.apply(draft);
            draft.synergies.add(syn.id());
        }

        return new AlloyComponent(
                quantized, forcedSynergies,
                draft.durability, draft.toughness, draft.hardness,
                draft.enchantRarity, draft.weight,
                List.copyOf(draft.synergies));
    }

    // ============================================================
    //  形态转换
    // ============================================================

    /** 附加形态共鸣，返回新组件 */
    public AlloyComponent withFormSynergy(String id) {
        return withSynergy(id);
    }

    /** 附加任意手动共鸣（形态 / 品质），返回新组件 */
    public AlloyComponent withSynergy(String id) {
        AlloyProperties draft = AlloyProperties.fromComponent(this);
        ModSynergies.applyManual(id, draft);

        Set<String> nextForced = new LinkedHashSet<>(forcedSynergies);
        nextForced.add(id);

        return new AlloyComponent(
                metals, nextForced,
                draft.durability, draft.toughness, draft.hardness,
                draft.enchantRarity, draft.weight,
                List.copyOf(draft.synergies));
    }

    // ============================================================
    //  装备合成
    // ============================================================

    /**
     * 累加多个组件为装备属性。
     * 耐久/硬度/附魔/重量求和，韧性取平均，共鸣并集。
     */
    public static AlloyComponent sumForEquipment(List<AlloyComponent> inputs, Map<String, Float> avgMetals) {

        AlloyProperties draft = new AlloyProperties();
        float toughSum = 0f;
        LinkedHashSet<String> synSet = new LinkedHashSet<>();

        for (AlloyComponent c : inputs) {
            draft.durability    += c.durability();
            draft.hardness      += c.hardness();
            draft.enchantRarity += c.enchantRarity();
            draft.weight        += c.weight();
            toughSum            += c.toughness();
            synSet.addAll(c.activeSynergies());
        }

        int n = inputs.size();
        draft.toughness = toughSum / n;
        draft.synergies.addAll(synSet);

        Map<String, Float> quantized = quantize(normalize(avgMetals));

        return new AlloyComponent(
                quantized, Set.of(),
                draft.durability, draft.toughness, draft.hardness,
                draft.enchantRarity, draft.weight,
                List.copyOf(draft.synergies));
    }

    // ============================================================
    //  合成判定
    // ============================================================

    public static boolean canCombine(List<AlloyComponent> comps) {
        if (comps.isEmpty()) {
            return false;
        }
        Set<String> base = comps.get(0).majorMetals();
        for (AlloyComponent c : comps) {
            if (!c.majorMetals().equals(base)) {
                return false;
            }
        }
        return true;
    }

    public static Map<String, Float> averageMetals(List<AlloyComponent> comps) {
        Map<String, Float> sum = new HashMap<>();
        for (AlloyComponent c : comps) {
            c.metals().forEach((k, v) -> sum.merge(k, v, Float::sum));
        }
        float n = comps.size();
        Map<String, Float> avg = new HashMap<>();
        sum.forEach((k, v) -> avg.put(k, v / n));
        return avg;
    }

    // ============================================================
    //  内部计算
    // ============================================================

    private static void computeResistanceChain(Map<String, Float> metals, AlloyProperties draft) {
        List<Map.Entry<String, Float>> sorted = sortByResistance(metals);
        if (sorted.isEmpty()) {
            return;
        }

        ModAlloyMetals.MetalType first = ModAlloyMetals.byId(sorted.get(0).getKey());
        if (first == null) {
            return;
        }

        draft.durability    = first.durability();
        draft.toughness     = first.toughness();
        draft.hardness      = first.hardness();
        draft.enchantRarity = first.enchantRarity();

        float w    = sorted.get(0).getValue();
        float rAdd = first.resistanceAdd();
        float rSub = first.resistanceSub();

        for (int i = 1; i < sorted.size(); i++) {
            Map.Entry<String, Float> e = sorted.get(i);
            ModAlloyMetals.MetalType m = ModAlloyMetals.byId(e.getKey());
            if (m == null) {
                continue;
            }
            float wi = e.getValue();

            draft.durability    = blend(draft.durability,    m.durability(),    w, wi, rAdd, rSub, m.resistanceAdd(), m.resistanceSub());
            draft.toughness     = blend(draft.toughness,     m.toughness(),     w, wi, rAdd, rSub, m.resistanceAdd(), m.resistanceSub());
            draft.hardness      = blend(draft.hardness,      m.hardness(),      w, wi, rAdd, rSub, m.resistanceAdd(), m.resistanceSub());
            draft.enchantRarity = blend(draft.enchantRarity, m.enchantRarity(), w, wi, rAdd, rSub, m.resistanceAdd(), m.resistanceSub());

            rAdd = clamp(rAdd * m.resistanceAdd(), R_MIN, R_MAX);
            rSub = clamp(rSub * m.resistanceSub(), R_MIN, R_MAX);
            w += wi;
        }
    }

    private static void applyThresholdBonuses(Map<String, Float> metals, AlloyProperties draft) {
        for (var e : metals.entrySet()) {
            ModAlloyMetals.MetalType m = ModAlloyMetals.byId(e.getKey());
            if (m == null) {
                continue;
            }
            float pct = e.getValue() * 100f;
            for (var be : m.bonuses().entrySet()) {
                if (pct + 1e-4f < be.getKey()) {
                    continue;
                }
                switch (be.getValue().stat()) {
                    case DURABILITY      -> draft.durability    += be.getValue().value();
                    case TOUGHNESS       -> draft.toughness     += be.getValue().value();
                    case HARDNESS        -> draft.hardness      += be.getValue().value();
                    case ENCHANT_RARITY  -> draft.enchantRarity += be.getValue().value();
                }
            }
        }
    }

    private static List<Map.Entry<String, Float>> sortByResistance(Map<String, Float> metals) {
        List<Map.Entry<String, Float>> list = new ArrayList<>(metals.entrySet());
        list.sort((a, b) -> {
            int cmp = Float.compare(b.getValue(), a.getValue());
            if (cmp != 0) {
                return cmp;
            }
            ModAlloyMetals.MetalType ma = ModAlloyMetals.byId(a.getKey());
            ModAlloyMetals.MetalType mb = ModAlloyMetals.byId(b.getKey());
            if (ma == null || mb == null) {
                return a.getKey().compareTo(b.getKey());
            }
            float ra = ma.resistanceAdd() + ma.resistanceSub();
            float rb = mb.resistanceAdd() + mb.resistanceSub();
            int rcmp = Float.compare(ra, rb);
            return rcmp != 0 ? rcmp : a.getKey().compareTo(b.getKey());
        });
        return list;
    }

    private static float blend(float pBase, float pFusion,
                               float wBase, float wFusion,
                               float rAdd, float rSub,
                               float fusionAdd, float fusionSub) {
        float r = (pFusion >= pBase)
                ? clamp(rAdd * fusionAdd, R_MIN, R_MAX)
                : clamp(rSub * fusionSub, R_MIN, R_MAX);
        float denom = wBase * r + wFusion;
        return denom <= 1e-6f ? pBase
                : (pBase * wBase * r + pFusion * wFusion) / denom;
    }

    private static float computeWeight(Map<String, Float> metals) {
        float w = 0f;
        for (var e : metals.entrySet()) {
            ModAlloyMetals.MetalType m = ModAlloyMetals.byId(e.getKey());
            if (m == null) {
                continue;
            }
            w += m.weight() * e.getValue();
        }
        return w;
    }

    private static Map<String, Float> normalize(Map<String, Float> raw) {
        float sum = 0f;
        for (float v : raw.values()) {
            sum += v;
        }
        if (sum <= 0f) {
            return Map.of();
        }
        Map<String, Float> out = new LinkedHashMap<>();
        float fs = sum;
        raw.forEach((k, v) -> out.put(k, v / fs));
        return out;
    }

    private static Map<String, Float> quantize(Map<String, Float> raw) {
        Map<String, Float> out = new LinkedHashMap<>();
        for (var e : raw.entrySet()) {
            float q = Math.round(e.getValue() * QUANTIZE) / QUANTIZE;
            if (q < ZERO_THRESHOLD) {
                continue;
            }
            out.put(e.getKey(), q);
        }
        return out;
    }

    private static int clamp8(float v) { return Math.clamp(Math.round(v), 0, 255); }
    private static float clamp(float v, float min, float max) { return Math.clamp(v, min, max); }

    public enum MixType {
        PURE(1.00f), BINARY(1.05f), TERNARY(1.10f), COMPLEX(1.15f);
        public final float multiplier;
        MixType(float m) { this.multiplier = m; }
        public static MixType of(int n) {
            return switch (n) {
                case 0, 1 -> PURE;
                case 2 -> BINARY;
                case 3 -> TERNARY;
                default -> COMPLEX;
            };
        }
    }
}
