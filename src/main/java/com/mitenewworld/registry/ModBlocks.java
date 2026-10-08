package com.mitenewworld.registry;
import com.mitenewworld.block.AlloyBlock;
import com.mitenewworld.block.ModOres;
import com.mitenewworld.block.SingleBerryBushBlock;
import com.mitenewworld.block.alloyfurnace.ModBlastFurnace1Block;
import com.mitenewworld.block.alloyfurnace.ModBlastFurnace2Block;
import com.mitenewworld.block.alloyfurnace.ModBlastFurnace3Block;
import com.mitenewworld.block.alloyfurnace.ModBlastFurnace4Block;
import com.mitenewworld.block.alloyfurnace.ModBlastFurnace5Block;
import com.mitenewworld.block.alloyfurnace.ModBlastFurnaceInputPortBlock;
import com.mitenewworld.block.alloyfurnace.ModBlastFurnaceOutputPortBlock;
import com.mitenewworld.block.alloyfurnace.ModBlastFurnaceShellBlock;
import com.mitenewworld.block.castingtable.CastingTableDefaults;
import com.mitenewworld.block.castingtable.ModCastingTableBlock;
import com.mitenewworld.block.castingtable.ModFlintCastingTableBlock;
import com.mitenewworld.block.craftingtable.ModCraftingTableBlock;
import com.mitenewworld.block.furnace.ModFurnaceBlock;
import com.mitenewworld.item.component.CastingTableComponent;
import com.mitenewworld.item.component.ModDataComponentTypes;
import com.mitenewworld.item.metal.ModAlloyMetals;

import com.mitenewworld.MITENewWorld;
import com.mitenewworld.block.portal.UnderWorldPortalBlock;
import com.mitenewworld.block.portal.WorldBreakerBlock;
import com.mitenewworld.cover.ItemsCover;

import com.mitenewworld.registry.ModItems;
import com.mitenewworld.loot.ModLootTables;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class ModBlocks {

    // ==================== 金属块 ====================
    public static final Block COPPER_BLOCK = register(
            "copper_block",
            Block::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .requiresTool()
                    .strength(40.0F, 35.0F)  // 原 10.0F, 8.0F
    );
    public static final Block SILVER_BLOCK = register(
            "silver_block",
            Block::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .requiresTool()
                    .strength(45.0F, 40.0F)  // 原 12.0F, 8.0F
    );
    public static final Block GOLD_BLOCK = register(
            "gold_block",
            Block::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .requiresTool()
                    .strength(35.0F, 30.0F)  // 原 7.0F, 10.0F
    );
    public static final Block IRON_BLOCK = register(
            "iron_block",
            Block::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .requiresTool()
                    .strength(60.0F, 50.0F)  // 原 15.0F, 30.0F
    );
    public static final Block ANCIENT_METAL_BLOCK = register(
            "ancient_metal_block",
            Block::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .requiresTool()
                    .strength(150.0F, 120.0F)  // 原 20.0F, 60.0F
    );
    public static final Block MITHRIL_BLOCK = register(
            "mithril_block",
            Block::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .requiresTool()
                    .strength(100.0F, 80.0F)  // 原 35.0F, 60.0F
    );
    public static final Block ADAMANTIUM_BLOCK = register(
            "adamantium_block",
            Block::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .requiresTool()
                    .strength(220.0F, 200.0F)  // 原 120.0F, 1200.0F
    );

    // ==================== 粗矿块 ====================
    public static final Block RAW_COPPER_BLOCK = register(
            "raw_copper_block",
            Block::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .requiresTool()
                    .strength(30.0F, 30.0F)  // 原 7.0F, 15.0F
    );
    public static final Block RAW_SILVER_BLOCK = register(
            "raw_silver_block",
            Block::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .requiresTool()
                    .strength(35.0F, 35.0F)  // 原 10.0F, 20.0F
    );
    public static final Block RAW_GOLD_BLOCK = register(
            "raw_gold_block",
            Block::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .requiresTool()
                    .strength(25.0F, 25.0F)  // 原 5.0F, 30.0F
    );
    public static final Block RAW_IRON_BLOCK = register(
            "raw_iron_block",
            Block::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .requiresTool()
                    .strength(45.0F, 45.0F)  // 原 12.0F, 40.0F
    );
    public static final Block RAW_MITHRIL_BLOCK = register(
            "raw_mithril_block",
            Block::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .requiresTool()
                    .strength(70.0F, 70.0F)  // 原 35.0F, 50.0F
    );
    public static final Block RAW_ADAMANTIUM_BLOCK = register(
            "raw_adamantium_block",
            Block::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .requiresTool()
                    .strength(110.0F, 110.0F)  // 原 75.0F, 100.0F
    );

    // ==================== 矿石（与石头接近） ====================
    public static final Block GOLD_ORE_NETHERRACK = register(
            "gold_ore_netherrack",
            settings -> new ExperienceDroppingBlock(ConstantIntProvider.create(0), settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .requiresTool()
                    .strength(30.0F, 25.0F)  // 原 12.0F, 6.0F
    );
    public static final Block COAL_ORE = register(
            "coal_ore",
            settings -> new ExperienceDroppingBlock(ConstantIntProvider.create(0), settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .requiresTool()
                    .strength(30.0F, 25.0F)  // 原 4.0F, 6.0F
    );
    public static final Block DEEPSLATE_COAL_ORE = register(
            "deepslate_coal_ore",
            settings -> new ExperienceDroppingBlock(ConstantIntProvider.create(0), settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .requiresTool()
                    .strength(35.0F, 30.0F)  // 原 6.0F, 10.0F
                    .sounds(BlockSoundGroup.DEEPSLATE)
    );
    public static final Block COPPER_ORE = register(
            "copper_ore",
            settings -> new ExperienceDroppingBlock(ConstantIntProvider.create(0), settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.0F, 25.0F)  // 原 4.0F, 12.0F
    );
    public static final Block DEEPSLATE_COPPER_ORE = register(
            "deepslate_copper_ore",
            settings -> new ExperienceDroppingBlock(ConstantIntProvider.create(0), settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(38.0F, 30.0F)  // 原 6.0F, 12.0F
    );
    public static final Block SILVER_ORE = register(
            "silver_ore",
            settings -> new ExperienceDroppingBlock(ConstantIntProvider.create(0), settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(33.0F, 25.0F)  // 原 6.0F, 12.0F
    );
    public static final Block DEEPSLATE_SILVER_ORE = register(
            "deepslate_silver_ore",
            settings -> new ExperienceDroppingBlock(ConstantIntProvider.create(0), settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .requiresTool()
                    .strength(40.0F, 30.0F)  // 原 8.0F, 15.0F
                    .sounds(BlockSoundGroup.DEEPSLATE)
    );
    public static final Block IRON_ORE = register(
            "iron_ore",
            settings -> new ExperienceDroppingBlock(ConstantIntProvider.create(0), settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .requiresTool()
                    .strength(35.0F, 25.0F)  // 原 7.0F, 15.0F
    );
    public static final Block DEEPSLATE_IRON_ORE = register(
            "deepslate_iron_ore",
            settings -> new ExperienceDroppingBlock(ConstantIntProvider.create(0), settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .requiresTool()
                    .strength(45.0F, 30.0F)  // 原 10.0F, 3.0F（已修复抗爆错误）
                    .sounds(BlockSoundGroup.DEEPSLATE)
    );
    public static final Block GOLD_ORE = register(
            "gold_ore",
            settings -> new ExperienceDroppingBlock(ConstantIntProvider.create(0), settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .requiresTool()
                    .strength(30.0F, 25.0F)  // 原 3.0F, 3.0F
    );
    public static final Block DEEPSLATE_GOLD_ORE = register(
            "deepslate_gold_ore",
            settings -> new ExperienceDroppingBlock(ConstantIntProvider.create(0), settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .requiresTool()
                    .strength(35.0F, 30.0F)  // 原 4.5F, 3.0F
                    .sounds(BlockSoundGroup.DEEPSLATE)
    );
    public static final Block MITHRIL_ORE = register(
            "mithril_ore",
            settings -> new ExperienceDroppingBlock(ConstantIntProvider.create(0), settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .requiresTool()
                    .strength(40.0F, 30.0F)  // 原 12.0F, 6.0F
    );
    public static final Block ADAMANTIUM_ORE = register(
            "adamantium_ore",
            settings -> new ExperienceDroppingBlock(ConstantIntProvider.create(0), settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .requiresTool()
                    .strength(45.0F, 30.0F)  // 原 15.0F, 6.0F
    );

    // ==================== 工作台（保持不变：快速搬走） ====================
    public static final Block MOD_FLINT_CRAFTING_TABLE_BLOCK = register(
            "mod_flint_crafting_table_block",
            s -> new ModCraftingTableBlock(1, "flint_crafting_table", s),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(2.0F, 6.0F)
    );
    public static final Block MOD_COPPER_CRAFTING_TABLE_BLOCK = register(
            "mod_copper_crafting_table_block",
            s -> new ModCraftingTableBlock(2, "copper_crafting_table", s),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(2.0F, 6.0F)
    );
    public static final Block MOD_SILVER_CRAFTING_TABLE_BLOCK = register(
            "mod_silver_crafting_table_block",
            s -> new ModCraftingTableBlock(2, "silver_crafting_table", s),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(2.0F, 6.0F)
    );
    public static final Block MOD_GOLD_CRAFTING_TABLE_BLOCK = register(
            "mod_gold_crafting_table_block",
            s -> new ModCraftingTableBlock(2, "gold_crafting_table", s),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(2.0F, 6.0F)
    );
    public static final Block MOD_TIN_CRAFTING_TABLE_BLOCK = register(
            "mod_tin_crafting_table_block",
            s -> new ModCraftingTableBlock(2, "tin_crafting_table", s),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(2.0F, 6.0F)
    );
    public static final Block MOD_ALUMINIUM_CRAFTING_TABLE_BLOCK = register(
            "mod_aluminium_crafting_table_block",
            s -> new ModCraftingTableBlock(2, "aluminium_crafting_table", s),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(2.0F, 6.0F)
    );
    public static final Block MOD_IRON_CRAFTING_TABLE_BLOCK = register(
            "mod_iron_crafting_table_block",
            s -> new ModCraftingTableBlock(3, "iron_crafting_table", s),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(2.0F, 6.0F)
    );
    public static final Block MOD_TITANIUM_CRAFTING_TABLE_BLOCK = register(
            "mod_titanium_crafting_table_block",
            s -> new ModCraftingTableBlock(3, "titanium_crafting_table", s),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(2.0F, 6.0F)
    );
    public static final Block MOD_MITHRIL_CRAFTING_TABLE_BLOCK = register(
            "mod_mithril_crafting_table_block",
            s -> new ModCraftingTableBlock(4, "mithril_crafting_table", s),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(2.0F, 6.0F)
    );
    public static final Block MOD_PLATINUM_CRAFTING_TABLE_BLOCK = register(
            "mod_platinum_crafting_table_block",
            s -> new ModCraftingTableBlock(4, "platinum_crafting_table", s),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(2.0F, 6.0F)
    );
    public static final Block MOD_IRIDIUM_CRAFTING_TABLE_BLOCK = register(
            "mod_iridium_crafting_table_block",
            s -> new ModCraftingTableBlock(5, "iridium_crafting_table", s),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(2.0F, 6.0F)
    );
    public static final Block MOD_STARLIGHT_CRAFTING_TABLE_BLOCK = register(
            "mod_starlight_crafting_table_block",
            s -> new ModCraftingTableBlock(5, "starlight_crafting_table", s),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(2.0F, 6.0F)
    );
    public static final Block MOD_ANCIENT_METAL_CRAFTING_TABLE_BLOCK = register(
            "mod_ancient_metal_crafting_table_block",
            s -> new ModCraftingTableBlock(6, "ancient_metal_crafting_table", s),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(2.0F, 6.0F)
    );
    public static final Block MOD_ADAMANTIUM_CRAFTING_TABLE_BLOCK = register(
            "mod_adamantium_crafting_table_block",
            s -> new ModCraftingTableBlock(6, "adamantium_crafting_table", s),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(2.0F, 6.0F)
    );

    // ==================== 熔炉（保持不变：快速搬走） ====================
    public static final Block MOD_CLAY_FURNACE_BLOCK = register(
            "mod_clay_furnace_block",
            s -> new ModFurnaceBlock(1, 400, "clay_furnace", s),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(2.0F, 6.0F)
                    .luminance(createLightLevelFromLitBlockState(13))
    );
    public static final Block MOD_LARGE_CLAY_FURNACE_BLOCK = register(
            "mod_large_clay_furnace_block",
            s -> new ModFurnaceBlock(1, 600, "large_clay_furnace", s),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(2.0F, 6.0F)
                    .luminance(createLightLevelFromLitBlockState(13))
    );
    public static final Block MOD_SANDSTONE_FURNACE_BLOCK = register(
            "mod_sandstone_furnace_block",
            s -> new ModFurnaceBlock(2, 1200, "sandstone_furnace", s),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(2.0F, 6.0F)
                    .luminance(createLightLevelFromLitBlockState(13))
    );
    public static final Block MOD_STONE_FURNACE_BLOCK = register(
            "mod_stone_furnace_block",
            s -> new ModFurnaceBlock(2, 1200, "stone_furnace", s),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(2.0F, 6.0F)
                    .luminance(createLightLevelFromLitBlockState(13))
    );
    public static final Block MOD_OBSIDIAN_FURNACE_BLOCK = register(
            "mod_obsidian_furnace_block",
            s -> new ModFurnaceBlock(3, 3200, "obsidian_furnace", s),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(2.0F, 6.0F)
                    .luminance(createLightLevelFromLitBlockState(13))
    );
    public static final Block MOD_NETHERRACK_FURNACE_BLOCK = register(
            "mod_netherrack_furnace_block",
            s -> new ModFurnaceBlock(4, 6400, "netherrack_furnace", s),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(2.0F, 6.0F)
                    .luminance(createLightLevelFromLitBlockState(13))
    );

    // ==================== 其他方块（保持不变） ====================
    public static final Block BLUEBERRY_BUSH = register(
            "blueberry_bush",
            settings -> new SingleBerryBushBlock(settings, ModLootTables.BLUEBERRY_BUSH, 3, 1),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .ticksRandomly()
                    .noCollision()
                    .sounds(BlockSoundGroup.SWEET_BERRY_BUSH)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .strength(1.0f)
    );

    // ==================== 地下世界 ====================
    public static final Block UNDERWORLD_STONE = register(
            "underworld_stone",
            Block::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(12.0F, 10.0F)
    );
    public static final Block UNDERWORLD_DIRT = register(
            "underworld_dirt",
            Block::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIRT_BROWN)
                    .strength(10.0F, 30.0F)
    );
    public static final Block UNDERWORLD_GRASS = register(
            "underworld_grass",
            Block::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_BLACK)
                    .strength(30.0F, 30.0F)
    );
    public static final Block UNDERWORLD_COBBLESTONE = register(
            "underworld_cobblestone",
            Block::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(40.0F, 30.0F)
    );
    public static final Block EROSION_LOG = register(
            "erosion_log",
            Block::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIRT_BROWN)
                    .strength(35.0F, 30.0F)
    );
    public static final Block EROSION_LEAVES = register(
            "erosion_leaves",
            settings -> new TintedParticleLeavesBlock(1.0f, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIRT_BROWN)
                    .strength(15.0F, 30.0F)
                    .noCollision()
    );

    public static final Block VOID_GATE = register(
            "void_gate",
            UnderWorldPortalBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BROWN)
                    .pistonBehavior(PistonBehavior.BLOCK)
                    .solid()
                    .strength(-1.0f, 999999.0F)
    );

    public static final Block WORLD_BREAKER = register(
            "world_breaker",
            WorldBreakerBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BROWN)
                    .solid()
                    .strength(50.0f, 999999.0F)
    );

    // ==================== 合金炉：5 档材料 ×（外壳 / 原料口 / 产物口 / 熔炼核心） ====================
    private static AbstractBlock.Settings partSettings(float hardness) {
        return AbstractBlock.Settings.create()
                .mapColor(MapColor.STONE_GRAY)
                .requiresTool()
                .strength(hardness, hardness);
    }

    public static final Block SMOOTH_STONE_BRICK = register("smooth_stone_brick",
            s -> new ModBlastFurnaceShellBlock(1, s), partSettings(30.0F));
    public static final Block SMOOTH_STONE_BRICK_INPUT_PORT = register("smooth_stone_brick_input_port",
            s -> new ModBlastFurnaceInputPortBlock(1, s), partSettings(30.0F));
    public static final Block SMOOTH_STONE_BRICK_OUTPUT_PORT = register("smooth_stone_brick_output_port",
            s -> new ModBlastFurnaceOutputPortBlock(1, s), partSettings(30.0F));
    public static final Block SMOOTH_STONE_BRICK_SMELTING_CORE = register("smooth_stone_brick_smelting_core", ModBlastFurnace1Block::new, partSettings(35.0F).luminance(createLightLevelFromLitBlockState(14)));

    public static final Block OBSIDIAN_BRICK = register("obsidian_brick",
            s -> new ModBlastFurnaceShellBlock(2, s), partSettings(35.0F));
    public static final Block OBSIDIAN_BRICK_INPUT_PORT = register("obsidian_brick_input_port",
            s -> new ModBlastFurnaceInputPortBlock(2, s), partSettings(35.0F));
    public static final Block OBSIDIAN_BRICK_OUTPUT_PORT = register("obsidian_brick_output_port",
            s -> new ModBlastFurnaceOutputPortBlock(2, s), partSettings(35.0F));
    public static final Block OBSIDIAN_BRICK_SMELTING_CORE = register("obsidian_brick_smelting_core", ModBlastFurnace2Block::new, partSettings(40.0F).luminance(createLightLevelFromLitBlockState(14)));

    public static final Block BLACKSTONE_BRICK = register("blackstone_brick",
            s -> new ModBlastFurnaceShellBlock(3, s), partSettings(40.0F));
    public static final Block BLACKSTONE_BRICK_INPUT_PORT = register("blackstone_brick_input_port",
            s -> new ModBlastFurnaceInputPortBlock(3, s), partSettings(40.0F));
    public static final Block BLACKSTONE_BRICK_OUTPUT_PORT = register("blackstone_brick_output_port",
            s -> new ModBlastFurnaceOutputPortBlock(3, s), partSettings(40.0F));
    public static final Block BLACKSTONE_BRICK_SMELTING_CORE = register("blackstone_brick_smelting_core", ModBlastFurnace3Block::new, partSettings(45.0F).luminance(createLightLevelFromLitBlockState(14)));

    // 4 级（1600℃）：哭泣黑曜石砖 —— 猪灵以物易物 / 堡垒遗迹
    public static final Block QUARTZ_BRICK = register("quartz_brick",
            s -> new ModBlastFurnaceShellBlock(4, s), partSettings(45.0F));
    public static final Block QUARTZ_BRICK_INPUT_PORT = register("quartz_brick_input_port",
            s -> new ModBlastFurnaceInputPortBlock(4, s), partSettings(45.0F));
    public static final Block QUARTZ_BRICK_OUTPUT_PORT = register("quartz_brick_output_port",
            s -> new ModBlastFurnaceOutputPortBlock(4, s), partSettings(45.0F));
    public static final Block QUARTZ_BRICK_SMELTING_CORE = register("quartz_brick_smelting_core", ModBlastFurnace4Block::new, partSettings(50.0F).luminance(createLightLevelFromLitBlockState(14)));

    // 5 级（2000℃）：下界合金砖 —— 原版最耐热（岩浆不熔），终局材料
    public static final Block NETHERITE_BRICK = register("netherite_brick",
            s -> new ModBlastFurnaceShellBlock(5, s), partSettings(50.0F));
    public static final Block NETHERITE_BRICK_INPUT_PORT = register("netherite_brick_input_port",
            s -> new ModBlastFurnaceInputPortBlock(5, s), partSettings(50.0F));
    public static final Block NETHERITE_BRICK_OUTPUT_PORT = register("netherite_brick_output_port",
            s -> new ModBlastFurnaceOutputPortBlock(5, s), partSettings(50.0F));
    public static final Block NETHERITE_BRICK_SMELTING_CORE = register("netherite_brick_smelting_core", ModBlastFurnace5Block::new, partSettings(55.0F).luminance(createLightLevelFromLitBlockState(14)));

    // ==================== 铸造台 ====================

    /** 通用铸造台：金属 / 等级 / 耐久完全由 {@link CastingTableComponent} 决定 */
    public static final Block MOD_CASTING_TABLE = register(
            "mod_casting_table",
            ModCastingTableBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.IRON_GRAY)
                    .requiresTool()
                    .strength(40.0F, 60.0F)
    );

    /** 燧石铸造台：一级，独立模型贴图（不参与金属染色） */
    public static final Block MOD_FLINT_CASTING_TABLE = register(
            "mod_flint_casting_table",
            ModFlintCastingTableBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .requiresTool()
                    .strength(4.0F, 6.0F)
    );

    /** 13 种金属的铸造台（每种一份方块，便于物品栏 / 配方 / 语言区分，渲染走同一套染色） */
    private static final Map<String, Block> METAL_CASTING_TABLES = registerMetalCastingTables();

    private static Map<String, Block> registerMetalCastingTables() {
        Map<String, Block> out = new LinkedHashMap<>();
        for (String metalId : CastingTableDefaults.METAL_IDS) {
            ModAlloyMetals.MetalType metal = ModAlloyMetals.byId(metalId);
            if (metal == null) {
                MITENewWorld.LOGGER.warn("未知金属 {}，跳过其铸造台注册", metalId);
                continue;
            }
            CastingTableComponent tableComp = CastingTableDefaults.componentFor(metal);
            Item.Settings itemSettings = new Item.Settings().component(ModDataComponentTypes.CASTING_TABLE_COMPONENT, tableComp);
            out.put(metalId, registerWithItemSettings(
                    "mod_" + metalId + "_casting_table",
                    s -> new ModCastingTableBlock(metalId, s),
                    AbstractBlock.Settings.create()
                            .mapColor(MapColor.IRON_GRAY)
                            .requiresTool()
                            .strength(40.0F, 60.0F),
                    itemSettings));
        }
        return Collections.unmodifiableMap(out);
    }

    /** 只读视图：13 种金属铸造台 */
    public static Map<String, Block> metalCastingTables() {
        return METAL_CASTING_TABLES;
    }

    /** 按金属 id 取对应铸造台；找不到就退回通用铸造台 */
    public static Block castingTableFor(String metalId) {
        return METAL_CASTING_TABLES.getOrDefault(metalId, MOD_CASTING_TABLE);
    }

    /** 全部铸造台（通用 + 燧石 + 13 金属），供 BlockEntity / tint 批量注册 */
    public static Block[] allCastingTables() {
        Block[] out = new Block[METAL_CASTING_TABLES.size() + 2];
        out[0] = MOD_CASTING_TABLE;
        out[1] = MOD_FLINT_CASTING_TABLE;
        int i = 2;
        for (Block b : METAL_CASTING_TABLES.values()) {
            out[i++] = b;
        }
        return out;
    }

    // ==================== 合金块 ====================
    public static final Block ALLOY_BLOCK = registerBlockOnly(
            "alloy_block",
            AlloyBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.IRON_GRAY)
                    .requiresTool()
                    .strength(45.0F, 45.0F)
                    .sounds(BlockSoundGroup.METAL)
    );

    // ==================== 注册方法 ====================
    public static Block registerBlockOnly(String id, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        Identifier identifier = Identifier.of(MITENewWorld.MOD_ID, id);
        RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);
        Block block = factory.apply(settings.registryKey(blockKey));
        return Registry.register(Registries.BLOCK, blockKey, block);
    }

    public static Block register(String id, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        Identifier identifier = Identifier.of(MITENewWorld.MOD_ID, id);
        RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, identifier);
        Block block = factory.apply(settings.registryKey(blockKey));
        Block registeredBlock = Registry.register(Registries.BLOCK, blockKey, block);
        register(itemKey, setting -> new BlockItem(block, setting.useBlockPrefixedTranslationKey()), new Item.Settings());
        return registeredBlock;
    }

    /** 注册方块 + 同名 BlockItem，并可自定义 Item.Settings（用于塞默认组件） */
    public static Block registerWithItemSettings(String id,
                                                 Function<AbstractBlock.Settings, Block> factory,
                                                 AbstractBlock.Settings settings,
                                                 Item.Settings itemSettings) {
        Identifier identifier = Identifier.of(MITENewWorld.MOD_ID, id);
        RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, identifier);
        Block block = factory.apply(settings.registryKey(blockKey));
        Block registeredBlock = Registry.register(Registries.BLOCK, blockKey, block);
        register(itemKey, setting -> new BlockItem(block, setting.useBlockPrefixedTranslationKey()), itemSettings);
        return registeredBlock;
    }

    public static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int litLevel) {
        return state -> state.get(Properties.LIT) ? litLevel : 0;
    }

    public static void register(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings) {
        Item item = factory.apply(settings.registryKey(key));
        if (item instanceof BlockItem blockItem) {
            blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
        }
        Registry.register(Registries.ITEM, key, item);
    }

    public static void registerModBlocks() {
        ModOres.init();
        MITENewWorld.LOGGER.info("Registering Blocks");
    }
}
