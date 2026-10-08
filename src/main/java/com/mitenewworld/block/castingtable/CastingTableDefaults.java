package com.mitenewworld.block.castingtable;

import com.mitenewworld.item.component.CastingTableComponent;
import com.mitenewworld.item.metal.ModAlloyMetals;

import java.util.Map;

/**
 * 铸造台默认属性推导。
 *
 * <p>同一金属无论"物品栏里直接拿"还是"在铸造台上打出来"，都应该得到同一台子，
 * 所以等级 / 耐久的推导集中在这里作为唯一来源，避免两处各写一套数。
 *
 * <p>刻意不依赖 {@code MiningTier}：这个类会在方块注册阶段（很早）被加载，
 * 依赖面越小越不容易踩到静态初始化顺序问题。
 */
public final class CastingTableDefaults {

    private CastingTableDefaults() {}

    /** 没有明确金属时的兜底 */
    public static final String FALLBACK_METAL = "iron";

    /** 全部 13 种金属，顺序与 {@code ModAlloyMetals} 的常量一致 */
    public static final String[] METAL_IDS = {
            "copper", "silver", "iron", "titanium", "mithril", "adamantium",
            "ancient_metal", "tin", "gold", "aluminium", "platinum", "iridium",
            "starlight"
    };

    /**
     * 某种金属能做出来的铸造台等级（1~6）。
     * 分档与 {@code ModToolItem.MiningTier} 的硬度区间保持一致，只是换算成台子等级。
     */
    public static int levelFor(ModAlloyMetals.MetalType metal) {
        float h = metal.hardness();
        if (h >= 80f) {
            return 6;
        }
        if (h >= 55f) {
            return 5;
        }
        if (h >= 40f) {
            return 4;
        }
        if (h >= 20f) {
            return 3;
        }
        return 2;
    }

    /** 耐久上限直接沿用金属自身的耐久 */
    public static float maxDurabilityFor(ModAlloyMetals.MetalType metal) {
        return Math.max(1f, metal.durability());
    }

    public static CastingTableComponent componentFor(ModAlloyMetals.MetalType metal) {
        return CastingTableComponent.fresh(Map.of(metal.id(), 1.0f), levelFor(metal), maxDurabilityFor(metal));
    }
}
