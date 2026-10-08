package com.mitenewworld.core;

import com.mitenewworld.item.ModFoodItems;
import com.mitenewworld.item.metal.ModBucketItems;
import com.mitenewworld.registry.ModItems;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class FuelMap {

    // 小于 400：保留真实热值
    public static final Map<Item, Integer> FUEL_MAP_LT_400 = new HashMap<>();

    // 按档位分类
    public static final Map<Item, Integer> FUEL_MAP_400 = new HashMap<>();
    public static final Map<Item, Integer> FUEL_MAP_800 = new HashMap<>();
    public static final Map<Item, Integer> FUEL_MAP_1600 = new HashMap<>();
    public static final Map<Item, Integer> FUEL_MAP_6400 = new HashMap<>();
    public static final Map<Item, Integer> FUEL_MAP_12800 = new HashMap<>();

    // 负值表：水桶、水碗（灭火）
    public static final Map<Item, Integer> FUEL_MAP_NEGATIVE = new HashMap<>();

    /** 统一查询表（含全部档位与负值） */
    private static final Map<Item, Integer> VALUES = new HashMap<>();

    static {
        // ================= < 400，保留真实热值 =================
        FUEL_MAP_LT_400.put(Items.OAK_SAPLING, 100);
        FUEL_MAP_LT_400.put(Items.SPRUCE_SAPLING, 100);
        FUEL_MAP_LT_400.put(Items.BIRCH_SAPLING, 100);
        FUEL_MAP_LT_400.put(Items.JUNGLE_SAPLING, 100);
        FUEL_MAP_LT_400.put(Items.ACACIA_SAPLING, 100);
        FUEL_MAP_LT_400.put(Items.DARK_OAK_SAPLING, 100);

        FUEL_MAP_LT_400.put(Items.BAMBOO, 50);
        FUEL_MAP_LT_400.put(Items.CHERRY_PLANKS, 100);

        FUEL_MAP_LT_400.put(Items.OAK_SLAB, 200);
        FUEL_MAP_LT_400.put(Items.SPRUCE_SLAB, 200);
        FUEL_MAP_LT_400.put(Items.BIRCH_SLAB, 200);
        FUEL_MAP_LT_400.put(Items.JUNGLE_SLAB, 200);
        FUEL_MAP_LT_400.put(Items.ACACIA_SLAB, 200);
        FUEL_MAP_LT_400.put(Items.DARK_OAK_SLAB, 200);
        FUEL_MAP_LT_400.put(Items.BAMBOO_SLAB, 200);
        FUEL_MAP_LT_400.put(Items.CHERRY_SLAB, 200);

        FUEL_MAP_LT_400.put(Items.OAK_STAIRS, 200);
        FUEL_MAP_LT_400.put(Items.SPRUCE_STAIRS, 200);
        FUEL_MAP_LT_400.put(Items.BIRCH_STAIRS, 200);
        FUEL_MAP_LT_400.put(Items.JUNGLE_STAIRS, 200);
        FUEL_MAP_LT_400.put(Items.ACACIA_STAIRS, 200);
        FUEL_MAP_LT_400.put(Items.DARK_OAK_STAIRS, 200);
        FUEL_MAP_LT_400.put(Items.BAMBOO_STAIRS, 200);
        FUEL_MAP_LT_400.put(Items.CHERRY_STAIRS, 200);

        FUEL_MAP_LT_400.put(Items.STICK, 25);
        FUEL_MAP_LT_400.put(Items.WHEAT, 50);
        FUEL_MAP_LT_400.put(Items.TORCH, 200);
        FUEL_MAP_LT_400.put(Items.BLAZE_POWDER, 300);

        // ================= 400 =================
        FUEL_MAP_400.put(Items.OAK_PLANKS, 400);
        FUEL_MAP_400.put(Items.SPRUCE_PLANKS, 400);
        FUEL_MAP_400.put(Items.BIRCH_PLANKS, 400);
        FUEL_MAP_400.put(Items.JUNGLE_PLANKS, 400);
        FUEL_MAP_400.put(Items.ACACIA_PLANKS, 400);
        FUEL_MAP_400.put(Items.DARK_OAK_PLANKS, 400);
        FUEL_MAP_400.put(Items.BAMBOO_PLANKS, 400);
        FUEL_MAP_400.put(Items.BAMBOO_BLOCK, 400);

        // ================= 800 =================
        FUEL_MAP_800.put(Items.OAK_LOG, 800);
        FUEL_MAP_800.put(Items.SPRUCE_LOG, 800);
        FUEL_MAP_800.put(Items.BIRCH_LOG, 800);
        FUEL_MAP_800.put(Items.JUNGLE_LOG, 800);
        FUEL_MAP_800.put(Items.ACACIA_LOG, 800);
        FUEL_MAP_800.put(Items.DARK_OAK_LOG, 800);
        FUEL_MAP_800.put(Items.CHERRY_LOG, 800);

        FUEL_MAP_800.put(Items.OAK_WOOD, 800);
        FUEL_MAP_800.put(Items.SPRUCE_WOOD, 800);
        FUEL_MAP_800.put(Items.BIRCH_WOOD, 800);
        FUEL_MAP_800.put(Items.JUNGLE_WOOD, 800);
        FUEL_MAP_800.put(Items.ACACIA_WOOD, 800);
        FUEL_MAP_800.put(Items.DARK_OAK_WOOD, 800);
        FUEL_MAP_800.put(Items.CHERRY_WOOD, 800);

        // ================= 1600 =================
        FUEL_MAP_1600.put(Items.COAL, 1600);
        FUEL_MAP_1600.put(Items.CHARCOAL, 1600);

        // ================= 6400 =================
        FUEL_MAP_6400.put(Items.BLAZE_ROD, 6400);

        // ================= 12800 =================
        FUEL_MAP_12800.put(Items.COAL_BLOCK, 12800);

        // ================= 负值表（灭火） =================
        FUEL_MAP_NEGATIVE.put(Items.WATER_BUCKET, -1600);
        FUEL_MAP_NEGATIVE.put(ModFoodItems.WATER_BOWL, -400);

        accumulate();
    }

    private FuelMap() {
    }

    private static void accumulate() {
        VALUES.putAll(FUEL_MAP_LT_400);
        VALUES.putAll(FUEL_MAP_400);
        VALUES.putAll(FUEL_MAP_800);
        VALUES.putAll(FUEL_MAP_1600);
        VALUES.putAll(FUEL_MAP_6400);
        VALUES.putAll(FUEL_MAP_12800);
        VALUES.putAll(FUEL_MAP_NEGATIVE);
    }

    private static void putIfAbsent(Item item, int value) {
        if (item != null && !VALUES.containsKey(item)) {
            VALUES.put(item, value);
        }
    }

    /** 遍历模组物品：能当燃料的补进表里；装水的水桶标记为灭火物品 */
    public static void registerModFuels() {
        // 模组自己的木制品 → 加进燃料表（服务器加载数据构建燃料表时触发）
        FuelRegistryEvents.BUILD.register((builder, context) -> {
            builder.add(ModItems.WOODEN_CUDGEL, 200);
            builder.add(ModItems.WOODEN_CULB, 200);

            // 遍历模组物品，燃料表认为可以当燃料的，按档位归一后放进本模组的燃料表
            FuelRegistry registry = builder.build();
            for (Item item : Registries.ITEM) {
                Identifier id = Registries.ITEM.getId(item);
                if (!id.getNamespace().equals("mitenewworld") || VALUES.containsKey(item)) {
                    continue;
                }
                ItemStack stack = new ItemStack(item);
                if (registry.isFuel(stack)) {
                    VALUES.put(item, normalize(registry.getFuelTicks(stack)));
                }
            }
        });

        // 装水的水桶 → 灭火物品
        for (Item item : Registries.ITEM) {
            Identifier id = Registries.ITEM.getId(item);
            if (!id.getNamespace().equals("mitenewworld") || VALUES.containsKey(item)) {
                continue;
            }
            if (item instanceof ModBucketItems bucket
                    && (bucket.fluid == Fluids.WATER || bucket.fluid == Fluids.FLOWING_WATER)) {
                VALUES.put(item, -1600);
            }
        }

        // 模组自身的木质燃料
        putIfAbsent(ModItems.WOODEN_CUDGEL, 200);
        putIfAbsent(ModItems.WOODEN_CULB, 200);
    }

    /** 值 → 档位代表值：<400 保留原值，其余取不小于它的最小档位值 */
    public static int normalize(int value) {
        if (value < 400) {
            return value;
        }
        if (value <= 400) {
            return 400;
        }
        if (value <= 800) {
            return 800;
        }
        if (value <= 1600) {
            return 1600;
        }
        if (value <= 6400) {
            return 6400;
        }
        return 12800;
    }

    /** 燃料值；-1 = 不是燃料 */
    public static int value(Item item) {
        return VALUES.getOrDefault(item, -1);
    }

    /** 是否灭火物品（水桶/水碗） */
    public static boolean isExtinguisher(Item item) {
        return value(item) < 0;
    }

    /** 燃料等级 1~5；0 = 不是燃料 */
    public static int tier(Item item) {
        int v = value(item);
        if (v <= 0) {
            return 0;
        }
        if (v <= 400) {
            return 1;
        }
        if (v <= 800) {
            return 2;
        }
        if (v <= 1600) {
            return 3;
        }
        if (v <= 6400) {
            return 4;
        }
        return 5;
    }

    /** 机器等级能否烧该燃料 */
    public static boolean accepts(int machineTier, Item item) {
        int t = tier(item);
        return t > 0 && t <= machineTier;
    }
}
