package com.mitenewworld.item.metal;
import com.mitenewworld.MITENewWorld;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public final class ModSynergies {
    private ModSynergies() {}

    public static final String NAMESPACE = "mitenewworld";

    public static final RegistryKey<Registry<SynergyEffect>> SYNERGY_KEY =
            RegistryKey.ofRegistry(Identifier.of(NAMESPACE, "synergy"));

    public static final Registry<SynergyEffect> SYNERGY =
            FabricRegistryBuilder.createSimple(SYNERGY_KEY)
                    .attribute(RegistryAttribute.SYNCED)
                    .buildAndRegister();

    // ============================================================
    //  自然共鸣
    // ============================================================

    public static final SynergyEffect DURABLE = register("durable",
            Set.of("iron", "copper"),
            d -> { d.durability *= 1.20f; d.toughness *= 1.10f; });

    public static final SynergyEffect SHARP = register("sharp",
            Set.of("iron", "silver"),
            d -> { d.toughness *= 1.05f; d.hardness *= 1.15f; });

    public static final SynergyEffect CONDUCTIVE = register("conductive",
            Set.of("copper", "gold"),
            d -> { d.hardness *= 1.05f; d.enchantRarity *= 1.30f; });

    public static final SynergyEffect BRONZE = register("bronze",
            Set.of("copper", "tin"),
            d -> { d.durability *= 1.25f; d.toughness *= 1.20f; d.hardness *= 1.15f; });

    public static final SynergyEffect ORNATE = register("ornate",
            Set.of("silver", "gold"),
            d -> { d.toughness *= 0.95f; d.enchantRarity *= 1.50f; });

    public static final SynergyEffect MASTERWORK = register("masterwork",
            Set.of("iron", "copper", "tin"),
            d -> { d.durability *= 1.35f; d.toughness *= 1.25f;
                d.hardness *= 1.20f; d.enchantRarity *= 1.10f; });

    public static final SynergyEffect AEROSPACE = register("aerospace",
            Set.of("titanium", "aluminium"),
            d -> { d.durability *= 1.10f; d.toughness *= 1.30f; d.hardness *= 1.15f; });

    public static final SynergyEffect PLATED = register("plated",
            Set.of("platinum", "iridium"),
            d -> { d.durability *= 1.15f; d.toughness *= 1.25f;
                d.hardness *= 1.20f; d.enchantRarity *= 1.10f; });

    public static final SynergyEffect CELESTIAL = register("celestial",
            Set.of("starlight", "mithril"),
            d -> { d.durability *= 1.10f; d.enchantRarity *= 1.40f; });

    public static final SynergyEffect PRIMORDIAL = register("primordial",
            Set.of("ancient_metal", "adamantium"),
            d -> { d.durability *= 1.30f; d.toughness *= 1.25f;
                d.hardness *= 1.25f; d.enchantRarity *= 1.20f; });

    // ============================================================
    //  形态共鸣
    // ============================================================

    public static final SynergyEffect NUGGET = register("nugget",
            Set.of(),
            d -> d.scaleAll(0.11f, 0.11f));

    public static final SynergyEffect BLOCK = register("block",
            Set.of(),
            d -> d.scaleAll(9.0f, 9.0f));

    public static final SynergyEffect CHAIN = register("chain",
            Set.of(),
            d -> d.scaleAll(0.45f, 0.30f));

    // ============================================================
    //  品质共鸣（手动）：打造品质并入金属共鸣系统
    // ============================================================

    public static final SynergyEffect QUALITY_INFERIOR = register("quality_inferior",
            Set.of(), d -> d.scaleAll(0.90f, 1.0f));
    public static final SynergyEffect QUALITY_COMMON = register("quality_common",
            Set.of(), d -> { });
    public static final SynergyEffect QUALITY_FINE = register("quality_fine",
            Set.of(), d -> d.scaleAll(1.05f, 1.0f));
    public static final SynergyEffect QUALITY_EXCELLENT = register("quality_excellent",
            Set.of(), d -> d.scaleAll(1.10f, 1.0f));
    public static final SynergyEffect QUALITY_EPIC = register("quality_epic",
            Set.of(), d -> d.scaleAll(1.15f, 1.0f));
    public static final SynergyEffect QUALITY_LEGENDARY = register("quality_legendary",
            Set.of(), d -> d.scaleAll(1.20f, 1.0f));

    /** 按品质分取对应的品质共鸣（档位与颜色由 {@link QualityLevel} 单点定义） */
    public static SynergyEffect qualityFor(int score) {
        return byId(QualityLevel.fromScore(score).synergyId());
    }

    // ============================================================
    //  注册工具
    // ============================================================

    private static SynergyEffect register(String id, Set<String> required, Consumer<AlloyProperties> apply) {
        SynergyEffect effect = new SynergyEffect(id, required) {
            @Override public void apply(AlloyProperties draft) {
                apply.accept(draft);
            }
        };
        return Registry.register(SYNERGY, Identifier.of(NAMESPACE, id), effect);
    }

    // ============================================================
    //  查询
    // ============================================================

    public static Collection<SynergyEffect> all() {
        return SYNERGY.stream().toList();
    }

    public static SynergyEffect byId(String id) {
        return SYNERGY.get(Identifier.of(NAMESPACE, id));
    }

    public static SynergyEffect byIdentifier(Identifier id) {
        return SYNERGY.get(id);
    }

    // ============================================================
    //  触发
    // ============================================================

    /** 自然触发：只收集自然共鸣 */
    public static List<SynergyEffect> triggeredNatural(AlloyComponent comp) {
        List<SynergyEffect> result = new ArrayList<>();
        for (SynergyEffect syn : SYNERGY) {
            if (!syn.isNatural()) {
                continue;
            }
            if (matchesNatural(syn, comp)) {
                result.add(syn);
            }
        }
        return result;
    }

    private static boolean matchesNatural(SynergyEffect syn, AlloyComponent comp) {
        for (String metal : syn.requiredMetals()) {
            Float r = comp.metals().get(metal);
            if (r == null || r < 0.15f - 1e-4f) {
                return false;
            }
        }
        return true;
    }

    /** 手动应用：直接修改草稿纸并记录 id */
    public static void applyManual(String id, AlloyProperties draft) {
        SynergyEffect syn = byId(id);
        if (syn == null) {
            return;
        }
        syn.apply(draft);
        if (!draft.synergies.contains(id)) {
            draft.synergies.add(id);
        }
    }

    public static void applyManual(Identifier id, AlloyProperties draft) {
        SynergyEffect syn = byIdentifier(id);
        if (syn == null) {
            return;
        }
        syn.apply(draft);
        String sid = id.toString();
        if (!draft.synergies.contains(sid)) {
            draft.synergies.add(sid);
        }
    }

    public static void applyManual(SynergyEffect syn, AlloyProperties draft) {
        if (syn == null) {
            return;
        }
        syn.apply(draft);
        if (!draft.synergies.contains(syn.id())) {
            draft.synergies.add(syn.id());
        }
    }

    public static void registerModSynergies() {}



    /**
     * 共鸣效果。子类覆写 apply，直接修改草稿纸。
     *
     * <p>requiredMetals 为空集表示"不自然触发"，只能通过 applyManual 手动应用。
     */
    public abstract static class SynergyEffect {
        private final String id;
        private final Set<String> requiredMetals;

        protected SynergyEffect(String id, Set<String> requiredMetals) {
            this.id = id;
            this.requiredMetals = requiredMetals;
        }

        public String id() { return id; }
        public Set<String> requiredMetals() { return requiredMetals; }
        public boolean isNatural() { return !requiredMetals.isEmpty(); }

        /** 直接修改草稿纸 */
        public abstract void apply(AlloyProperties draft);
    }
}
