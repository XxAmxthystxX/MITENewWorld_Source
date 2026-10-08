package com.mitenewworld.registry;
import com.mitenewworld.cover.ItemsCover;
import com.mitenewworld.item.DebugItem;
import com.mitenewworld.item.ModFoodItems;
import com.mitenewworld.item.RawOreItem;
import com.mitenewworld.item.ScrapMetalItem;
import com.mitenewworld.item.metal.ModDaggerItem;

import com.mitenewworld.MITENewWorld;

import com.mitenewworld.item.metal.AlloyBlockItem;
import com.mitenewworld.item.metal.AlloyItem;
import com.mitenewworld.item.metal.ModArmorItem;
import com.mitenewworld.item.metal.ModBucketItems;

import com.mitenewworld.item.metal.ModToolItem;
import com.mitenewworld.item.metal.ModBucketLevel;
import com.mitenewworld.tags.ModBlockTags;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ModItems {

    // ====================== 例外工具（原版 API 构造，固定数值） ======================
    // 走原版 Item.Settings.tool(...)，不参与合金系统。
    // 耐久用 .maxDamage() 覆盖（必须放在 .tool() 之后，否则会被 tool() 重置）。
    // 数值沿用旧 ModItems 的 (attackDamage, attackSpeed)。

    // ---------- 木板 ----------

    public static final Item WOODEN_CULB = ModItems.register(
            "wooden_culb",
            Item::new,
            new Item.Settings()
                    .tool(ToolMaterial.WOOD, ModBlockTags.SWORD_MINEABLE, 3f, -2.4f, 0f)
                    .maxDamage(100 * 2)
    );

    public static final Item WOODEN_CUDGEL = ModItems.register(
            "wooden_cudgel",
            Item::new,
            new Item.Settings()
                    .tool(ToolMaterial.WOOD, ModBlockTags.SWORD_MINEABLE, 2f, -1.8f, 0f)
                    .maxDamage(100 * 1)
    );

    // ---------- 燧石 ----------

    public static final Item FLINT_SHOVEL = ModItems.register(
            "flint_shovel",
            Item::new,
            new Item.Settings()
                    .tool(ToolMaterial.WOOD, ModBlockTags.SHOVEL_MINEABLE, 2f, -3.0f, 0f)
                    .maxDamage(100 * 1)
    );

    public static final Item FLINT_AXE = ModItems.register(
            "flint_axe",
            Item::new,
            new Item.Settings()
                    .tool(ToolMaterial.WOOD, ModBlockTags.AXE_MINEABLE, 4f, -3.2f, 0f)
                    .maxDamage(100 * 3)
    );

    public static final Item FLINT_HATCHET = ModItems.register(
            "flint_hatchet",
            Item::new,
            new Item.Settings()
                    .tool(ToolMaterial.WOOD, ModBlockTags.AXE_MINEABLE, 2f, -2.4f, 0f)
                    .maxDamage(100 * 1)
    );

    public static final Item FLINT_KNIFE = ModItems.register(
            "flint_knife",
            Item::new,
            new Item.Settings()
                    .tool(ToolMaterial.WOOD, ModBlockTags.SWORD_MINEABLE, 2f, -3.0f, 0f)
                    .maxDamage(100 * 1)
    );

    /** 燧石锻造锤：铸造台用的最低级锤子，固定 200 点耐久 */
    public static final Item FLINT_FORGING_HAMMER = ModItems.register(
            "flint_forging_hammer",
            (Item.Settings s) -> new ModToolItem(ModToolItem.ToolType.CASTING_HAMMER, s),
            ModToolItem.toolSettings().maxDamage(200)
    );

    // ---------- 锈铁 ----------

    public static final Item RUSTED_IRON_SWORD = ModItems.register(
            "rusted_iron_sword",
            Item::new,
            new Item.Settings()
                    .tool(ToolMaterial.STONE, ModBlockTags.SWORD_MINEABLE, 7f, -2.4f, 2f)
                    .maxDamage(300 * 2)
    );

    public static final Item RUSTED_IRON_DAGGER = ModItems.register(
            "rusted_iron_dagger",
            Item::new,
            new Item.Settings()
                    .tool(ToolMaterial.STONE, ModBlockTags.SWORD_MINEABLE, 4f, -1.8f, 0f)
                    .maxDamage(300 * 1)
    );

    public static final Item RUSTED_IRON_BATTLEAXE = ModItems.register(
            "rusted_iron_battleaxe",
            Item::new,
            new Item.Settings()
                    .tool(ToolMaterial.STONE, ModBlockTags.AXE_MINEABLE, 9f, -3.0f, 5f)
                    .maxDamage(300 * 4)
    );

    public static final Item RUSTED_IRON_WARHAMMER = ModItems.register(
            "rusted_iron_warhammer",
            Item::new,
            new Item.Settings()
                    .tool(ToolMaterial.STONE, ModBlockTags.PICKAXE_MINEABLE, 11f, -3.5f, 7f)
                    .maxDamage(300 * 5)
    );

    public static final Item RUSTED_IRON_SCYTHE = ModItems.register(
            "rusted_iron_scythe",
            Item::new,
            new Item.Settings()
                    .tool(ToolMaterial.STONE, ModBlockTags.SCYTHE_MINEABLE, 5f, -2.0f, 0f)
                    .maxDamage(300 * 2)
    );

    public static final Item RUSTED_IRON_SHOVEL = ModItems.register(
            "rusted_iron_shovel",
            Item::new,
            new Item.Settings()
                    .tool(ToolMaterial.STONE, ModBlockTags.SHOVEL_MINEABLE, 2f, -3.0f, 0f)
                    .maxDamage(300 * 1)
    );

    public static final Item RUSTED_IRON_PICKAXE = ModItems.register(
            "rusted_iron_pickaxe",
            Item::new,
            new Item.Settings()
                    .tool(ToolMaterial.STONE, ModBlockTags.PICKAXE_MINEABLE, 2f, -2.8f, 3f)
                    .maxDamage(300 * 3)
    );

    public static final Item RUSTED_IRON_AXE = ModItems.register(
            "rusted_iron_axe",
            Item::new,
            new Item.Settings()
                    .tool(ToolMaterial.STONE, ModBlockTags.AXE_MINEABLE, 4f, -3.2f, 3f)
                    .maxDamage(300 * 3)
    );

    public static final Item RUSTED_IRON_HOE = ModItems.register(
            "rusted_iron_hoe",
            Item::new,
            new Item.Settings()
                    .tool(ToolMaterial.STONE, ModBlockTags.SHOVEL_MINEABLE, 2f, -3.0f, 0f)
                    .maxDamage(300 * 1)
    );

    public static final Item RUSTED_IRON_MATTOCK = ModItems.register(
            "rusted_iron_mattock",
            Item::new,
            new Item.Settings()
                    .tool(ToolMaterial.STONE, ModBlockTags.SHOVEL_MINEABLE, 2f, -3.5f, 0f)
                    .maxDamage(300 * 4)
    );

    public static final Item RUSTED_IRON_HATCHET = ModItems.register(
            "rusted_iron_hatchet",
            Item::new,
            new Item.Settings()
                    .tool(ToolMaterial.STONE, ModBlockTags.AXE_MINEABLE, 4f, -2.4f, 0f)
                    .maxDamage(300 * 1)
    );

    public static final Item RUSTED_IRON_SHEARS = ModItems.register(
            "rusted_iron_shears",
            Item::new,
            new Item.Settings()
                    .tool(ToolMaterial.STONE, ModBlockTags.SHEARS_MINEABLE, 1f, -1.5f, 0f)
                    .maxDamage(300 * 2)
    );

    // ---------- 黑曜石 ----------

    public static final Item OBSIDIAN_SHOVEL = ModItems.register(
            "obsidian_shovel",
            Item::new,
            new Item.Settings()
                    .tool(ToolMaterial.DIAMOND, ModBlockTags.SHOVEL_MINEABLE, 4f, -3.0f, 0f)
                    .maxDamage(600)
    );

    public static final Item OBSIDIAN_AXE = ModItems.register(
            "obsidian_axe",
            Item::new,
            new Item.Settings()
                    .tool(ToolMaterial.DIAMOND, ModBlockTags.AXE_MINEABLE, 5f, -3.2f, 0f)
                    .maxDamage(600 * 3)
    );
    // ====================== 合金工具（运行时属性） ======================
// 注册时只给占位 Settings；属性在 createFromAlloy 时按合金填充

    public static final Item ALLOY_SWORD     = ModItems.register("alloy_sword",
            (Item.Settings s) -> new ModToolItem(ModToolItem.ToolType.SWORD, s),
            ModToolItem.toolSettings());

    public static final Item ALLOY_DAGGER    = ModItems.register("alloy_dagger",
            (Item.Settings s) -> new ModDaggerItem(ModToolItem.ToolType.DAGGER, s),
            ModToolItem.toolSettings());

    public static final Item ALLOY_BATTLEAXE = ModItems.register("alloy_battleaxe",
            (Item.Settings s) -> new ModToolItem(ModToolItem.ToolType.BATTLEAXE, s),
            ModToolItem.toolSettings());

    public static final Item ALLOY_WARHAMMER = ModItems.register("alloy_warhammer",
            (Item.Settings s) -> new ModToolItem(ModToolItem.ToolType.WARHAMMER, s),
            ModToolItem.toolSettings());

    public static final Item ALLOY_SCYTHE    = ModItems.register("alloy_scythe",
            (Item.Settings s) -> new ModToolItem(ModToolItem.ToolType.SCYTHE, s),
            ModToolItem.toolSettings());

    public static final Item ALLOY_SHOVEL    = ModItems.register("alloy_shovel",
            (Item.Settings s) -> new ModToolItem(ModToolItem.ToolType.SHOVEL, s),
            ModToolItem.toolSettings());

    public static final Item ALLOY_PICKAXE   = ModItems.register("alloy_pickaxe",
            (Item.Settings s) -> new ModToolItem(ModToolItem.ToolType.PICKAXE, s),
            ModToolItem.toolSettings());

    public static final Item ALLOY_AXE       = ModItems.register("alloy_axe",
            (Item.Settings s) -> new ModToolItem(ModToolItem.ToolType.AXE, s),
            ModToolItem.toolSettings());

    public static final Item ALLOY_HOE       = ModItems.register("alloy_hoe",
            (Item.Settings s) -> new ModToolItem(ModToolItem.ToolType.HOE, s),
            ModToolItem.toolSettings());

    public static final Item ALLOY_MATTOCK   = ModItems.register("alloy_mattock",
            (Item.Settings s) -> new ModToolItem(ModToolItem.ToolType.MATTOCK, s),
            ModToolItem.toolSettings());

    public static final Item ALLOY_HATCHET   = ModItems.register("alloy_hatchet",
            (Item.Settings s) -> new ModToolItem(ModToolItem.ToolType.HATCHET, s),
            ModToolItem.toolSettings());

    public static final Item ALLOY_SHEARS    = ModItems.register("alloy_shears",
            (Item.Settings s) -> new ModToolItem(ModToolItem.ToolType.SHEARS, s),
            ModToolItem.toolSettings());

    /** 铸造锤：只能用于铸造台锻造 */
    public static final Item ALLOY_CASTING_HAMMER = ModItems.register("alloy_casting_hammer",
            (Item.Settings s) -> new ModToolItem(ModToolItem.ToolType.CASTING_HAMMER, s),
            ModToolItem.toolSettings());
    
    
    public static final Item COPPER_BUCKET = ModItems.register(
            "copper_bucket",
            (Item.Settings settings) ->
                    new ModBucketItems(Fluids.EMPTY, ModBucketLevel.COPPER,settings) ,
            new Item.Settings().maxCount(8)
    );
    public static final Item IRON_BUCKET = ModItems.register(
            "iron_bucket",
            (Item.Settings settings) -> new ModBucketItems(Fluids.EMPTY, ModBucketLevel.IRON, settings),
            new Item.Settings().maxCount(8)
    );

    public static final Item SILVER_BUCKET = ModItems.register(
            "silver_bucket",
            (Item.Settings settings) -> new ModBucketItems(Fluids.EMPTY, ModBucketLevel.SILVER, settings),
            new Item.Settings().maxCount(8)
    );

    public static final Item GOLD_BUCKET = ModItems.register(
            "gold_bucket",
            (Item.Settings settings) -> new ModBucketItems(Fluids.EMPTY, ModBucketLevel.GOLD, settings),
            new Item.Settings().maxCount(8)
    );

    public static final Item MITHRIL_BUCKET = ModItems.register(
            "mithril_bucket",
            (Item.Settings settings) -> new ModBucketItems(Fluids.EMPTY, ModBucketLevel.MITHRIL, settings),
            new Item.Settings().maxCount(8)
    );

    public static final Item ANCIENT_METAL_BUCKET = ModItems.register(
            "ancient_metal_bucket",
            (Item.Settings settings) -> new ModBucketItems(Fluids.EMPTY, ModBucketLevel.ANCIENT_METAL, settings),
            new Item.Settings().maxCount(8)
    );

    public static final Item ADAMANTIUM_BUCKET = ModItems.register(
            "adamantium_bucket",
            (Item.Settings settings) -> new ModBucketItems(Fluids.EMPTY, ModBucketLevel.ADAMANTIUM, settings),
            new Item.Settings().maxCount(8)
    );

    public static final Item WATER_COPPER_BUCKET = ModItems.register(
            "water_copper_bucket",
            (Item.Settings settings) -> new ModBucketItems(Fluids.FLOWING_WATER, ModBucketLevel.COPPER, settings),
            new Item.Settings().recipeRemainder(COPPER_BUCKET).maxCount(1)
    );

    public static final Item WATER_IRON_BUCKET = ModItems.register(
            "water_iron_bucket",
            (Item.Settings settings) -> new ModBucketItems(Fluids.FLOWING_WATER, ModBucketLevel.IRON, settings),
            new Item.Settings().recipeRemainder(IRON_BUCKET).maxCount(1)
    );

    public static final Item WATER_SILVER_BUCKET = ModItems.register(
            "water_silver_bucket",
            (Item.Settings settings) -> new ModBucketItems(Fluids.FLOWING_WATER, ModBucketLevel.SILVER, settings),
            new Item.Settings().recipeRemainder(SILVER_BUCKET).maxCount(1)
    );

    public static final Item WATER_GOLD_BUCKET = ModItems.register(
            "water_gold_bucket",
            (Item.Settings settings) -> new ModBucketItems(Fluids.FLOWING_WATER, ModBucketLevel.GOLD, settings),
            new Item.Settings().recipeRemainder(GOLD_BUCKET).maxCount(1)
    );

    public static final Item WATER_MITHRIL_BUCKET = ModItems.register(
            "water_mithril_bucket",
            (Item.Settings settings) -> new ModBucketItems(Fluids.FLOWING_WATER, ModBucketLevel.MITHRIL, settings),
            new Item.Settings().recipeRemainder(MITHRIL_BUCKET).maxCount(1)
    );

    public static final Item WATER_ANCIENT_METAL_BUCKET = ModItems.register(
            "water_ancient_metal_bucket",
            (Item.Settings settings) -> new ModBucketItems(Fluids.FLOWING_WATER, ModBucketLevel.ANCIENT_METAL, settings),
            new Item.Settings().recipeRemainder(ANCIENT_METAL_BUCKET).maxCount(1)
    );

    public static final Item WATER_ADAMANTIUM_BUCKET = ModItems.register(
            "water_adamantium_bucket",
            (Item.Settings settings) -> new ModBucketItems(Fluids.FLOWING_WATER, ModBucketLevel.ADAMANTIUM, settings),
            new Item.Settings().recipeRemainder(ADAMANTIUM_BUCKET).maxCount(1)
    );

    public static final Item LAVA_COPPER_BUCKET = ModItems.register(
            "lava_copper_bucket",
            (Item.Settings settings) -> new ModBucketItems(Fluids.FLOWING_LAVA, ModBucketLevel.COPPER, settings),
            new Item.Settings().recipeRemainder(COPPER_BUCKET).maxCount(1)
    );

    public static final Item LAVA_IRON_BUCKET = ModItems.register(
            "lava_iron_bucket",
            (Item.Settings settings) -> new ModBucketItems(Fluids.FLOWING_LAVA, ModBucketLevel.IRON, settings),
            new Item.Settings().recipeRemainder(IRON_BUCKET).maxCount(1)
    );

    public static final Item LAVA_SILVER_BUCKET = ModItems.register(
            "lava_silver_bucket",
            (Item.Settings settings) -> new ModBucketItems(Fluids.FLOWING_LAVA, ModBucketLevel.SILVER, settings),
            new Item.Settings().recipeRemainder(SILVER_BUCKET).maxCount(1)
    );

    public static final Item LAVA_GOLD_BUCKET = ModItems.register(
            "lava_gold_bucket",
            (Item.Settings settings) -> new ModBucketItems(Fluids.FLOWING_LAVA, ModBucketLevel.GOLD, settings),
            new Item.Settings().recipeRemainder(GOLD_BUCKET).maxCount(1)
    );

    public static final Item LAVA_MITHRIL_BUCKET = ModItems.register(
            "lava_mithril_bucket",
            (Item.Settings settings) -> new ModBucketItems(Fluids.FLOWING_LAVA, ModBucketLevel.MITHRIL, settings),
            new Item.Settings().recipeRemainder(MITHRIL_BUCKET).maxCount(1)
    );

    public static final Item LAVA_ANCIENT_METAL_BUCKET = ModItems.register(
            "lava_ancient_metal_bucket",
            (Item.Settings settings) -> new ModBucketItems(Fluids.FLOWING_LAVA, ModBucketLevel.ANCIENT_METAL, settings),
            new Item.Settings().recipeRemainder(ANCIENT_METAL_BUCKET).maxCount(1)
    );

    public static final Item LAVA_ADAMANTIUM_BUCKET = ModItems.register(
            "lava_adamantium_bucket",
            (Item.Settings settings) -> new ModBucketItems(Fluids.FLOWING_LAVA, ModBucketLevel.ADAMANTIUM, settings),
            new Item.Settings().recipeRemainder(ADAMANTIUM_BUCKET).maxCount(1)
    );
    public static final Item STONE_COPPER_BUCKET = ModItems.register(
            "stone_copper_bucket",
            Item::new,
            new Item.Settings().maxCount(1)
    );

    public static final Item STONE_SILVER_BUCKET = ModItems.register(
            "stone_silver_bucket",
            Item::new,
            new Item.Settings().maxCount(1)
    );

    public static final Item STONE_GOLD_BUCKET = ModItems.register(
            "stone_gold_bucket",
            Item::new,
            new Item.Settings().maxCount(1)
    );

    public static final Item STONE_IRON_BUCKET = ModItems.register(
            "stone_iron_bucket",
            Item::new,
            new Item.Settings().maxCount(1)
    );

    public static final Item STONE_MITHRIL_BUCKET = ModItems.register(
            "stone_mithril_bucket",
            Item::new,
            new Item.Settings().maxCount(1)
    );

    public static final Item STONE_ANCIENT_METAL_BUCKET = ModItems.register(
            "stone_ancient_metal_bucket",
            Item::new,
            new Item.Settings().maxCount(1)
    );

    public static final Item STONE_ADAMANTIUM_BUCKET = ModItems.register(
            "stone_adamantium_bucket",
            Item::new,
            new Item.Settings().maxCount(1)
    );

    public static final Item ALLOY_HELMET     = ModItems.register("alloy_helmet",
            (Item.Settings s) -> new ModArmorItem(ModArmorItem.ArmorType.HELMET, s),
            new Item.Settings().maxCount(1));

    public static final Item ALLOY_CHESTPLATE = ModItems.register("alloy_chestplate",
            (Item.Settings s) -> new ModArmorItem(ModArmorItem.ArmorType.CHESTPLATE, s),
            new Item.Settings().maxCount(1));

    public static final Item ALLOY_LEGGINGS   = ModItems.register("alloy_leggings",
            (Item.Settings s) -> new ModArmorItem(ModArmorItem.ArmorType.LEGGINGS,  s),
            new Item.Settings().maxCount(1));

    public static final Item ALLOY_BOOTS      = ModItems.register("alloy_boots",
            (Item.Settings s) -> new ModArmorItem(ModArmorItem.ArmorType.BOOTS,  s),
            new Item.Settings().maxCount(1));

    public static final Item ALLOY_BODY       = ModItems.register("alloy_body",
            (Item.Settings s) -> new ModArmorItem(ModArmorItem.ArmorType.BODY,  s),
            new Item.Settings().maxCount(1));


    public static final Item ALLOY_CHAIN_HELMET     = ModItems.register("alloy_chain_helmet",
            (Item.Settings s) -> ModArmorItem.chain(ModArmorItem.ArmorType.HELMET,  s),
            new Item.Settings().maxCount(1));

    public static final Item ALLOY_CHAIN_CHESTPLATE = ModItems.register("alloy_chain_chestplate",
            (Item.Settings s) -> ModArmorItem.chain(ModArmorItem.ArmorType.CHESTPLATE,  s),
            new Item.Settings().maxCount(1));

    public static final Item ALLOY_CHAIN_LEGGINGS   = ModItems.register("alloy_chain_leggings",
            (Item.Settings s) -> ModArmorItem.chain(ModArmorItem.ArmorType.LEGGINGS,  s),
            new Item.Settings().maxCount(1));

    public static final Item ALLOY_CHAIN_BOOTS      = ModItems.register("alloy_chain_boots",
            (Item.Settings s) -> ModArmorItem.chain(ModArmorItem.ArmorType.BOOTS, s),
            new Item.Settings().maxCount(1));

    public static final Item ALLOY_CHAIN_BODY       = ModItems.register("alloy_chain_body",
            (Item.Settings s) -> ModArmorItem.chain(ModArmorItem.ArmorType.BODY, s),
            new Item.Settings().maxCount(1));

//ingredients
    public static final Item FLINT_FRAGMENT = ModItems.register("flint_fragment", new Item.Settings());
    public static final Item EMERALD_FRAGMENT = ModItems.register("emerald_fragment",new Item.Settings());
    public static final Item GLASS_FRAGMENT = ModItems.register("glass_fragment",  new Item.Settings());
    public static final Item DIAMOND_FRAGMENT = ModItems.register("diamond_fragment",  new Item.Settings());
    public static final Item NETHER_QUARTZ_FRAGMENT = ModItems.register("nether_quartz_fragment",  new Item.Settings());
    public static final Item OBSIDIAN_FRAGMENT = ModItems.register("obsidian_fragment",  new Item.Settings());

    public static final Item SINEW = ModItems.register("sinew",new Item.Settings());

    // ==================== 工具箱（13 金属各一，铸造台产出） ====================
    public static final Item COPPER_TOOLBOX      = ModItems.register("copper_toolbox",      new Item.Settings());
    public static final Item SILVER_TOOLBOX      = ModItems.register("silver_toolbox",      new Item.Settings());
    public static final Item IRON_TOOLBOX        = ModItems.register("iron_toolbox",        new Item.Settings());
    public static final Item TITANIUM_TOOLBOX    = ModItems.register("titanium_toolbox",    new Item.Settings());
    public static final Item MITHRIL_TOOLBOX     = ModItems.register("mithril_toolbox",     new Item.Settings());
    public static final Item ADAMANTIUM_TOOLBOX  = ModItems.register("adamantium_toolbox",  new Item.Settings());
    public static final Item ANCIENT_METAL_TOOLBOX = ModItems.register("ancient_metal_toolbox", new Item.Settings());
    public static final Item TIN_TOOLBOX         = ModItems.register("tin_toolbox",         new Item.Settings());
    public static final Item GOLD_TOOLBOX        = ModItems.register("gold_toolbox",        new Item.Settings());
    public static final Item ALUMINIUM_TOOLBOX   = ModItems.register("aluminium_toolbox",   new Item.Settings());
    public static final Item PLATINUM_TOOLBOX    = ModItems.register("platinum_toolbox",    new Item.Settings());
    public static final Item IRIDIUM_TOOLBOX     = ModItems.register("iridium_toolbox",     new Item.Settings());
    public static final Item STARLIGHT_TOOLBOX   = ModItems.register("starlight_toolbox",   new Item.Settings());

    /** 主导金属 id -> 工具箱（铸造模板按产物金属查询） */
    private static final Map<String, Item> TOOLBOXES = Map.ofEntries(
            Map.entry("copper", COPPER_TOOLBOX),
            Map.entry("silver", SILVER_TOOLBOX),
            Map.entry("iron", IRON_TOOLBOX),
            Map.entry("titanium", TITANIUM_TOOLBOX),
            Map.entry("mithril", MITHRIL_TOOLBOX),
            Map.entry("adamantium", ADAMANTIUM_TOOLBOX),
            Map.entry("ancient_metal", ANCIENT_METAL_TOOLBOX),
            Map.entry("tin", TIN_TOOLBOX),
            Map.entry("gold", GOLD_TOOLBOX),
            Map.entry("aluminium", ALUMINIUM_TOOLBOX),
            Map.entry("platinum", PLATINUM_TOOLBOX),
            Map.entry("iridium", IRIDIUM_TOOLBOX),
            Map.entry("starlight", STARLIGHT_TOOLBOX)
    );

    public static Item toolboxFor(String metalId) {
        return TOOLBOXES.getOrDefault(metalId, COPPER_TOOLBOX);
    }

    /** 主导金属 id -> 空桶（铸造模板按产物金属查询，空桶本身不带合金属性） */
    private static final Map<String, Item> BUCKETS = Map.ofEntries(
            Map.entry("copper", COPPER_BUCKET),
            Map.entry("silver", SILVER_BUCKET),
            Map.entry("gold", GOLD_BUCKET),
            Map.entry("iron", IRON_BUCKET),
            Map.entry("mithril", MITHRIL_BUCKET),
            Map.entry("ancient_metal", ANCIENT_METAL_BUCKET),
            Map.entry("adamantium", ADAMANTIUM_BUCKET)
    );

    public static Item bucketFor(String metalId) {
        return BUCKETS.getOrDefault(metalId, COPPER_BUCKET);
    }

    public static final Item SCRAP_METAL = ModItems.register(
            "scrap_metal",
            ScrapMetalItem::new,
            new Item.Settings()
    );

    public static final Item REFINING_POWDER = ModItems.register(
            "refining_powder",
            Item::new,
            new Item.Settings()
    );

    /** 方解石碎片：大理石（原版方解石）的碎料 */
    public static final Item CALCITE_FRAGMENT = ModItems.register(
            "calcite_fragment",
            Item::new,
            new Item.Settings()
    );

    // ====================== 粗矿（13 种，各自独立贴图） ======================

    public static final Item RAW_COPPER         = registerRawOre("copper", new Item.Settings());
    public static final Item RAW_SILVER         = registerRawOre("silver", new Item.Settings());
    public static final Item RAW_GOLD           = registerRawOre("gold", new Item.Settings());
    public static final Item RAW_IRON           = registerRawOre("iron", new Item.Settings());
    public static final Item RAW_TITANIUM       = registerRawOre("titanium", new Item.Settings());
    public static final Item RAW_MITHRIL        = registerRawOre("mithril", new Item.Settings());
    public static final Item RAW_ANCIENT_METAL  = registerRawOre("ancient_metal", new Item.Settings());
    public static final Item RAW_ADAMANTIUM     = registerRawOre("adamantium", new Item.Settings());
    public static final Item RAW_TIN            = registerRawOre("tin", new Item.Settings());
    public static final Item RAW_ALUMINIUM      = registerRawOre("aluminium", new Item.Settings());
    public static final Item RAW_PLATINUM       = registerRawOre("platinum", new Item.Settings());
    public static final Item RAW_IRIDIUM        = registerRawOre("iridium", new Item.Settings());
    public static final Item RAW_STARLIGHT      = registerRawOre("starlight", new Item.Settings());
    // ====================== 合金形态（4 种） ======================

    /** 合金锭：基准形态，无形态共鸣 */
    public static final Item ALLOY_INGOT = ModItems.register(
            "alloy_ingot",
            (Item.Settings s) -> new AlloyItem(AlloyItem.Form.INGOT, s),
            new Item.Settings()
    );

    /** 合金粒：1 粒 ≈ 0.11 锭，强制 "nugget" 共鸣 */
    public static final Item ALLOY_NUGGET = ModItems.register(
            "alloy_nugget",
            (Item.Settings s) -> new AlloyItem(AlloyItem.Form.NUGGET, s),
            new Item.Settings()
    );

    /** 合金块：1 块 = 9 锭，强制 "block" 共鸣 */
    public static final Item ALLOY_BLOCK = ModItems.register(
            "alloy_block",
            (Item.Settings s) -> new AlloyBlockItem(ModBlocks.ALLOY_BLOCK, s.useBlockPrefixedTranslationKey()),
            new Item.Settings()
    );

    /** 合金链条：1 链条 ≈ 0.45 锭属性、0.30 锭重量，强制 "chain" 共鸣 */
    public static final Item ALLOY_CHAIN = ModItems.register(
            "alloy_chain",
            (Item.Settings s) -> new AlloyItem(AlloyItem.Form.CHAIN, s),
            new Item.Settings().maxCount(16)
    );

    public static final Item DEBUG_ITEM = ModItems.register(
            "debug_item",
            DebugItem::new,
            new Item.Settings()
    );


    public static final HashMap<Item,Item> FILL_IN_WATER = new HashMap<>();
    static {
        FILL_IN_WATER.put(COPPER_BUCKET, WATER_COPPER_BUCKET);
        FILL_IN_WATER.put(SILVER_BUCKET, WATER_SILVER_BUCKET);
        FILL_IN_WATER.put(GOLD_BUCKET, WATER_GOLD_BUCKET);
        FILL_IN_WATER.put(IRON_BUCKET, WATER_IRON_BUCKET);
        FILL_IN_WATER.put(MITHRIL_BUCKET, WATER_MITHRIL_BUCKET);
        FILL_IN_WATER.put(ANCIENT_METAL_BUCKET, WATER_ANCIENT_METAL_BUCKET);
        FILL_IN_WATER.put(ADAMANTIUM_BUCKET, WATER_ADAMANTIUM_BUCKET);
        FILL_IN_WATER.put(LAVA_COPPER_BUCKET, STONE_COPPER_BUCKET);
        FILL_IN_WATER.put(LAVA_SILVER_BUCKET, STONE_SILVER_BUCKET);
        FILL_IN_WATER.put(LAVA_GOLD_BUCKET, STONE_GOLD_BUCKET);
        FILL_IN_WATER.put(LAVA_IRON_BUCKET, STONE_IRON_BUCKET);
        FILL_IN_WATER.put(LAVA_MITHRIL_BUCKET, STONE_MITHRIL_BUCKET);
        FILL_IN_WATER.put(LAVA_ANCIENT_METAL_BUCKET, STONE_ANCIENT_METAL_BUCKET);
        FILL_IN_WATER.put(LAVA_ADAMANTIUM_BUCKET, STONE_ADAMANTIUM_BUCKET);
        FILL_IN_WATER.put(ItemsCover.BOWL , ModFoodItems.WATER_BOWL);

    }









    public static Item register(String id, Item.Settings settings) {
        return ModItems.register(keyOf(id), Item::new, settings);
    }

    /** 粗矿：注册 id 为 raw_{metalId}，并把纯金属 id 传给物品 */
    public static Item registerRawOre(String metalId, Item.Settings settings) {
        return ModItems.register(keyOf("raw_" + metalId),
                (Item.Settings s) -> new RawOreItem(metalId, s),
                settings);
    }
    public static Item register(String id, Function<Item.Settings, Item> factory , Item.Settings settings) {
        return ModItems.register(keyOf(id), factory, settings);
    }
    public static Item register(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings) {
        Item item = factory.apply(settings.registryKey(key));
        if (item instanceof BlockItem blockItem) {
            blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
        }
        return Registry.register(Registries.ITEM, key, item);
    }
    private static RegistryKey<Item> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MITENewWorld.MOD_ID, id));
    }
    public static void registerModItem(){
        MITENewWorld.LOGGER.info("ModItems.registering Items") ;
    }
}








