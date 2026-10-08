package com.mitenewworld.item.metal;

import com.mitenewworld.registry.ModItems;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 各金属 100% 的默认合金制品。
 *
 * <p>合金体系是数据驱动的：同一件物品靠 {@link AlloyComponent} 区分材质，
 * 因此"纯金属版本"不需要新注册物品，只需生成带对应成分的 {@link ItemStack}。
 * 这里把它们集中产出，供创造模式物品栏直接取用。
 */
public final class ModDefaultAlloys {

    private ModDefaultAlloys() {
    }

    // ============================================================
    //  品类清单
    // ============================================================

    /** 材料形态（4 种）：锭 / 粒 / 块 / 链条 */
    private static final Map<Item, AlloyItem.Form> MATERIALS = new LinkedHashMap<>();

    /** 合金工具（13 种，含铸造锤） */
    private static final List<Item> TOOLS = List.of(
            ModItems.ALLOY_SWORD,
            ModItems.ALLOY_DAGGER,
            ModItems.ALLOY_BATTLEAXE,
            ModItems.ALLOY_WARHAMMER,
            ModItems.ALLOY_SCYTHE,
            ModItems.ALLOY_SHOVEL,
            ModItems.ALLOY_PICKAXE,
            ModItems.ALLOY_AXE,
            ModItems.ALLOY_HOE,
            ModItems.ALLOY_MATTOCK,
            ModItems.ALLOY_HATCHET,
            ModItems.ALLOY_SHEARS,
            ModItems.ALLOY_CASTING_HAMMER
    );

    /** 合金护甲（10 种：板甲 5 件 + 锁链甲 5 件） */
    private static final List<Item> ARMORS = List.of(
            ModItems.ALLOY_HELMET,
            ModItems.ALLOY_CHESTPLATE,
            ModItems.ALLOY_LEGGINGS,
            ModItems.ALLOY_BOOTS,
            ModItems.ALLOY_BODY,
            ModItems.ALLOY_CHAIN_HELMET,
            ModItems.ALLOY_CHAIN_CHESTPLATE,
            ModItems.ALLOY_CHAIN_LEGGINGS,
            ModItems.ALLOY_CHAIN_BOOTS,
            ModItems.ALLOY_CHAIN_BODY
    );

    static {
        MATERIALS.put(ModItems.ALLOY_INGOT, AlloyItem.Form.INGOT);
        MATERIALS.put(ModItems.ALLOY_NUGGET, AlloyItem.Form.NUGGET);
        MATERIALS.put(ModItems.ALLOY_BLOCK, AlloyItem.Form.BLOCK);
        MATERIALS.put(ModItems.ALLOY_CHAIN, AlloyItem.Form.CHAIN);
    }

    // ============================================================
    //  产出
    // ============================================================

    /** 材料形态：每金属 4 件 */
    public static List<ItemStack> materials() {
        List<ItemStack> out = new ArrayList<>();
        for (ModAlloyMetals.MetalType metal : ModAlloyMetals.METAL_TYPE) {
            MATERIALS.forEach((item, form) -> out.add(AlloyItem.pure(item, form, metal)));
        }
        return out;
    }

    /** 合金工具：每金属 13 件 */
    public static List<ItemStack> tools() {
        List<ItemStack> out = new ArrayList<>();
        for (ModAlloyMetals.MetalType metal : ModAlloyMetals.METAL_TYPE) {
            AlloyComponent comp = pure(metal);
            for (Item item : TOOLS) {
                if (item instanceof ModToolItem tool) {
                    out.add(named(tool.createFromAlloy(comp), item, metal));
                }
            }
        }
        return out;
    }

    /** 合金护甲：每金属 10 件 */
    public static List<ItemStack> armors() {
        List<ItemStack> out = new ArrayList<>();
        for (ModAlloyMetals.MetalType metal : ModAlloyMetals.METAL_TYPE) {
            AlloyComponent comp = pure(metal);
            for (Item item : ARMORS) {
                if (item instanceof ModArmorItem armor) {
                    out.add(named(armor.createFromAlloy(comp), item, metal));
                }
            }
        }
        return out;
    }

    // ============================================================
    //  工具方法
    // ============================================================

    /** 单一金属 100% 的合金成分 */
    public static AlloyComponent pure(ModAlloyMetals.MetalType metal) {
        return AlloyComponent.compute(Map.of(metal.id(), 1.0f), Set.of());
    }

    /**
     * 给工具 / 护甲标注金属名：金属在前 + " · " + 物品基础名，不加斜体。
     * 材料形态走 {@link AlloyItem#pure}，已有 .single 翻译键，不需要这里处理。
     */
    private static ItemStack named(ItemStack stack, Item item, ModAlloyMetals.MetalType metal) {
        MutableText name = Text.empty()
                .append(metalName(metal))
                .append(Text.literal(" · "))
                .append(Text.translatable(item.getTranslationKey()));
        name.setStyle(name.getStyle().withItalic(false));
        stack.set(DataComponentTypes.CUSTOM_NAME, name);
        return stack;
    }

    private static Text metalName(ModAlloyMetals.MetalType metal) {
        return Text.translatable("metal.mitenewworld." + metal.id());
    }
}
