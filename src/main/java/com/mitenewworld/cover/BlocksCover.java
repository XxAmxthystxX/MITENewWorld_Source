/*
 * Decompiled with CFR 0.2.2 (FabricMC 7c48b8c4).
 */
package com.mitenewworld.cover;
import com.mitenewworld.block.ModTorchBlock;
import com.mitenewworld.block.ModWallTorchBlock;
import com.mitenewworld.block.SingleBerryBushBlock;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.cover.ItemsCover;
import net.minecraft.block.*;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.block.enums.BedPart;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.enums.SculkSensorPhase;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemKeys;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.TintedParticleEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ColorCode;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.BlockView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.TreeConfiguredFeatures;
import net.minecraft.world.gen.feature.UndergroundConfiguredFeatures;
import net.minecraft.world.gen.feature.VegetationConfiguredFeatures;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import static com.mitenewworld.block.ModTorchBlock.LIT;

/**
 * Contains all the minecraft BlocksCover.
 */
public class BlocksCover {

    public static final HashMap<RegistryKey<Block> , Block> BlockCoverMap = new HashMap<>();
     private static final AbstractBlock.ContextPredicate SHULKER_BOX_SUFFOCATES_PREDICATE = (state, world, pos) -> {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ShulkerBoxBlockEntity shulkerBoxBlockEntity) {
            return shulkerBoxBlockEntity.suffocates();
        }
        return true;
    };
    private static final AbstractBlock.ContextPredicate PISTON_SUFFOCATES_PREDICATE = (state, world, pos) -> state.get(PistonBlock.EXTENDED) == false;
    public static final Block AIR = BlocksCover.register(
            "air",
            AirBlock::new,
            AbstractBlock.Settings.create()
                    .replaceable()
                    .noCollision()
                    .dropsNothing()
                    .air()
    );
    public static final Block STONE = BlocksCover.register(
            "stone",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(30.5F, 30.0F)
    );
    public static final Block GRANITE = BlocksCover.register(
            "granite",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIRT_BROWN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(35.0F, 30.0F)
    );
    public static final Block POLISHED_GRANITE = BlocksCover.register(
            "polished_granite",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIRT_BROWN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(35.0F, 30.0F)
    );
    public static final Block DIORITE = BlocksCover.register(
            "diorite",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OFF_WHITE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(35.0F, 30.0F)
    );
    public static final Block POLISHED_DIORITE = BlocksCover.register(
            "polished_diorite",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OFF_WHITE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(35.0F, 30.0F)
    );
    public static final Block ANDESITE = BlocksCover.register(
            "andesite",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(35.0F, 30.0F)
    );
    public static final Block POLISHED_ANDESITE = BlocksCover.register(
            "polished_andesite",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(35.0F, 30.0F)
    );
    public static final Block GRASS_BLOCK = BlocksCover.register(
            "grass_block",
            GrassBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_GREEN)
                    .ticksRandomly()
                    .strength(10.0F, 2.0F)
                    .sounds(BlockSoundGroup.GRASS)
    );
    public static final Block DIRT = BlocksCover.register(
            "dirt",
            (AbstractBlock.Settings settings) ->
                    new ColoredFallingBlock(new ColorCode(0xFF964B00) , settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIRT_BROWN)
                    .strength(10.0F ,15.0F)
                    .sounds(BlockSoundGroup.GRAVEL)
    );
    public static final Block COARSE_DIRT = BlocksCover.register(
            "coarse_dirt",
            (AbstractBlock.Settings settings) ->
                    new ColoredFallingBlock(new ColorCode(0xFF964B00) , settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIRT_BROWN)
                    .strength(10.0F,15.0F)
                    .sounds(BlockSoundGroup.GRAVEL)
    );
    public static final Block PODZOL = BlocksCover.register(
            "podzol",
            SnowyBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .strength(10.0F,15.0F)
                    .sounds(BlockSoundGroup.GRAVEL)
    );
    public static final Block COBBLESTONE = BlocksCover.register(
            "cobblestone",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(30.0F, 25.0F)
    );
    public static final Block OAK_PLANKS = BlocksCover.register(
            "oak_planks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(10.0f, 15.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block SPRUCE_PLANKS = BlocksCover.register(
            "spruce_planks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(10.0f, 15.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block BIRCH_PLANKS = BlocksCover.register(
            "birch_planks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_YELLOW)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(10.0f, 15.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block JUNGLE_PLANKS = BlocksCover.register(
            "jungle_planks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIRT_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(10.0f, 15.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block ACACIA_PLANKS = BlocksCover.register(
            "acacia_planks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(10.0f, 15.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block CHERRY_PLANKS = BlocksCover.register(
            "cherry_planks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_WHITE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(10.0f, 15.0f)
                    .sounds(BlockSoundGroup.CHERRY_WOOD)
                    .burnable()
    );
    public static final Block DARK_OAK_PLANKS = BlocksCover.register(
            "dark_oak_planks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(10.0f, 15.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block PALE_OAK_WOOD = BlocksCover.register(
            "pale_oak_wood",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(20.0f, 20.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block PALE_OAK_PLANKS = BlocksCover.register(
            "pale_oak_planks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OFF_WHITE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(10.0f, 15.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block MANGROVE_PLANKS = BlocksCover.register(
            "mangrove_planks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.RED)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(10.0f, 15.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block BAMBOO_PLANKS = BlocksCover.register(
            "bamboo_planks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.YELLOW)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(10.0f, 15.0f)
                    .sounds(BlockSoundGroup.BAMBOO_WOOD)
                    .burnable()
    );
    public static final Block BAMBOO_MOSAIC = BlocksCover.register(
            "bamboo_mosaic",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.YELLOW)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(20.0f, 15.0f)
                    .sounds(BlockSoundGroup.BAMBOO_WOOD)
                    .burnable()
    );
    public static final Block OAK_SAPLING = BlocksCover.register(
            "oak_sapling",
            (AbstractBlock.Settings settings) -> new SaplingBlock(SaplingGenerator.OAK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .strength(2.0F, 1.0F)
                    .burnable()
                    .ticksRandomly()
                    .sounds(BlockSoundGroup.GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block SPRUCE_SAPLING = BlocksCover.register(
            "spruce_sapling",
            (AbstractBlock.Settings settings) -> new SaplingBlock(SaplingGenerator.SPRUCE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .ticksRandomly()
                    .strength(2.0F, 1.0F)
                    .burnable()
                    .sounds(BlockSoundGroup.GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block BIRCH_SAPLING = BlocksCover.register(
            "birch_sapling",
            (AbstractBlock.Settings settings) -> new SaplingBlock(SaplingGenerator.BIRCH, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .ticksRandomly()
                    .strength(2.0F, 1.0F)
                    .burnable()
                    .sounds(BlockSoundGroup.GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block JUNGLE_SAPLING = BlocksCover.register(
            "jungle_sapling",
            (AbstractBlock.Settings settings) -> new SaplingBlock(SaplingGenerator.JUNGLE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .ticksRandomly()
                    .strength(2.0F, 1.0F)
                    .burnable()
                    .sounds(BlockSoundGroup.GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block ACACIA_SAPLING = BlocksCover.register(
            "acacia_sapling",
            (AbstractBlock.Settings settings) -> new SaplingBlock(SaplingGenerator.ACACIA, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .ticksRandomly()
                    .strength(2.0F, 1.0F)
                    .burnable()
                    .sounds(BlockSoundGroup.GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block CHERRY_SAPLING = BlocksCover.register(
            "cherry_sapling",
            (AbstractBlock.Settings settings) -> new SaplingBlock(SaplingGenerator.CHERRY, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PINK)
                    .noCollision()
                    .ticksRandomly()
                    .strength(2.0F, 1.0F)
                    .burnable()
                    .sounds(BlockSoundGroup.CHERRY_SAPLING)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block DARK_OAK_SAPLING = BlocksCover.register(
            "dark_oak_sapling",
            (AbstractBlock.Settings settings) -> new SaplingBlock(SaplingGenerator.DARK_OAK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .ticksRandomly()
                    .strength(2.0F, 1.0F)
                    .burnable()
                    .sounds(BlockSoundGroup.GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block PALE_OAK_SAPLING = BlocksCover.register(
            "pale_oak_sapling",
            (AbstractBlock.Settings settings) -> new SaplingBlock(SaplingGenerator.PALE_OAK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.IRON_GRAY)
                    .noCollision()
                    .ticksRandomly()
                    .strength(2.0F, 1.0F)
                    .burnable()
                    .sounds(BlockSoundGroup.GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block MANGROVE_PROPAGULE = BlocksCover.register(
            "mangrove_propagule",
            (AbstractBlock.Settings settings) -> new PropaguleBlock(SaplingGenerator.MANGROVE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .ticksRandomly()
                    .strength(2.0F, 1.0F)
                    .burnable()
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block BEDROCK = BlocksCover.register(
            "bedrock",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(-1.0F, 3600000.0F)
                    .dropsNothing()
                    .allowsSpawning(BlocksCover::never)
    );
    public static final Block WATER = BlocksCover.register(
            "water",
            (AbstractBlock.Settings settings) -> new FluidBlock(Fluids.WATER, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WATER_BLUE)
                    .replaceable()
                    .noCollision()
                    .strength(100.0f)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .dropsNothing()
                    .liquid()
                    .sounds(BlockSoundGroup.INTENTIONALLY_EMPTY)
    );
    public static final Block LAVA = BlocksCover.register(
            "lava",
            (AbstractBlock.Settings settings) -> new FluidBlock(Fluids.LAVA, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BRIGHT_RED)
                    .replaceable()
                    .noCollision()
                    .ticksRandomly()
                    .strength(100.0f)
                    .luminance(state -> 15)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .dropsNothing()
                    .liquid()
                    .sounds(BlockSoundGroup.INTENTIONALLY_EMPTY)
    );
    public static final Block SAND = BlocksCover.register(
            "sand",
            (AbstractBlock.Settings settings) -> new SandBlock(new ColorCode(14406560), settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_YELLOW)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(6.0F ,5.0F)
                    .sounds(BlockSoundGroup.SAND)
    );
    public static final Block SUSPICIOUS_SAND = BlocksCover.register(
            "suspicious_sand",
            (AbstractBlock.Settings settings) -> new BrushableBlock(SAND, SoundEvents.ITEM_BRUSH_BRUSHING_SAND, SoundEvents.ITEM_BRUSH_BRUSHING_SAND, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_YELLOW)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(8.0F ,5.0F)
                    .sounds(BlockSoundGroup.SUSPICIOUS_SAND)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block RED_SAND = BlocksCover.register(
            "red_sand",
            (AbstractBlock.Settings settings) -> new SandBlock(new ColorCode(11098145), settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(6.0F ,5.0F)
                    .sounds(BlockSoundGroup.SAND)
    );
    public static final Block GRAVEL = BlocksCover.register(
            "gravel",
            (AbstractBlock.Settings settings) -> new ColoredFallingBlock(new ColorCode(-8356741), settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(8.0F ,5.0F)
                    .sounds(BlockSoundGroup.GRAVEL)
    );
    public static final Block SUSPICIOUS_GRAVEL = BlocksCover.register(
            "suspicious_gravel",
            (AbstractBlock.Settings settings) -> new BrushableBlock(GRAVEL, SoundEvents.ITEM_BRUSH_BRUSHING_GRAVEL, SoundEvents.ITEM_BRUSH_BRUSHING_GRAVEL, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(9.0F ,5.0F)
                    .sounds(BlockSoundGroup.SUSPICIOUS_GRAVEL)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block GOLD_ORE = BlocksCover.register(
            "gold_ore",
            (AbstractBlock.Settings settings) -> new ExperienceDroppingBlock(ConstantIntProvider.create(0), settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(3.0f, 3.0f)
    );
    public static final Block DEEPSLATE_GOLD_ORE = BlocksCover.register(
            "deepslate_gold_ore",
            (AbstractBlock.Settings settings) -> new ExperienceDroppingBlock(ConstantIntProvider.create(0), settings),
            AbstractBlock.Settings.copy(GOLD_ORE).mapColor(MapColor.DEEPSLATE_GRAY).strength(4.5f, 3.0f).sounds(BlockSoundGroup.DEEPSLATE)
    );
    public static final Block IRON_ORE = BlocksCover.register(
            "iron_ore",
            (AbstractBlock.Settings settings) -> new ExperienceDroppingBlock(ConstantIntProvider.create(0), settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(3.0f, 3.0f)
    );
    public static final Block DEEPSLATE_IRON_ORE = BlocksCover.register(
            "deepslate_iron_ore",
            (AbstractBlock.Settings settings) -> new ExperienceDroppingBlock(ConstantIntProvider.create(0), settings),
            AbstractBlock.Settings.copy(IRON_ORE).mapColor(MapColor.DEEPSLATE_GRAY).strength(4.5f, 3.0f).sounds(BlockSoundGroup.DEEPSLATE)
    );
    public static final Block COAL_ORE = BlocksCover.register(
            "coal_ore",
            (AbstractBlock.Settings settings) -> new ExperienceDroppingBlock(UniformIntProvider.create(0, 2), settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(3.0f, 3.0f)
    );
    public static final Block DEEPSLATE_COAL_ORE = BlocksCover.register(
            "deepslate_coal_ore",
            (AbstractBlock.Settings settings) -> new ExperienceDroppingBlock(UniformIntProvider.create(0, 2), settings),
            AbstractBlock.Settings.copy(COAL_ORE).mapColor(MapColor.DEEPSLATE_GRAY).strength(4.5f, 3.0f).sounds(BlockSoundGroup.DEEPSLATE)
    );
    public static final Block NETHER_GOLD_ORE = BlocksCover.register(
            "nether_gold_ore",
            (AbstractBlock.Settings settings) -> new ExperienceDroppingBlock(UniformIntProvider.create(0, 1), settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(3.0f, 3.0f)
                    .sounds(BlockSoundGroup.NETHER_GOLD_ORE)
    );
    public static final Block OAK_LOG = BlocksCover.register(
            "oak_log",
            PillarBlock::new,
            BlocksCover.createLogSettings(MapColor.OAK_TAN, MapColor.SPRUCE_BROWN, BlockSoundGroup.WOOD)
    );
    public static final Block SPRUCE_LOG = BlocksCover.register(
            "spruce_log",
            PillarBlock::new,
            BlocksCover.createLogSettings(MapColor.SPRUCE_BROWN, MapColor.BROWN, BlockSoundGroup.WOOD)
    );
    public static final Block BIRCH_LOG = BlocksCover.register(
            "birch_log",
            PillarBlock::new,
            BlocksCover.createLogSettings(MapColor.PALE_YELLOW, MapColor.OFF_WHITE, BlockSoundGroup.WOOD)
    );
    public static final Block JUNGLE_LOG = BlocksCover.register(
            "jungle_log",
            PillarBlock::new,
            BlocksCover.createLogSettings(MapColor.DIRT_BROWN, MapColor.SPRUCE_BROWN, BlockSoundGroup.WOOD)
    );
    public static final Block ACACIA_LOG = BlocksCover.register(
            "acacia_log",
            PillarBlock::new,
            BlocksCover.createLogSettings(MapColor.ORANGE, MapColor.STONE_GRAY, BlockSoundGroup.WOOD)
    );
    public static final Block CHERRY_LOG = BlocksCover.register(
            "cherry_log",
            PillarBlock::new,
            BlocksCover.createLogSettings(MapColor.TERRACOTTA_WHITE, MapColor.TERRACOTTA_GRAY, BlockSoundGroup.CHERRY_WOOD)
    );
    public static final Block DARK_OAK_LOG = BlocksCover.register(
            "dark_oak_log",
            PillarBlock::new,
            BlocksCover.createLogSettings(MapColor.BROWN, MapColor.BROWN, BlockSoundGroup.WOOD)
    );
    public static final Block PALE_OAK_LOG = BlocksCover.register(
            "pale_oak_log",
            PillarBlock::new,
            BlocksCover.createLogSettings(PALE_OAK_PLANKS.getDefaultMapColor(), PALE_OAK_WOOD.getDefaultMapColor(), BlockSoundGroup.WOOD)
    );
    public static final Block MANGROVE_LOG = BlocksCover.register(
            "mangrove_log",
            PillarBlock::new,
            BlocksCover.createLogSettings(MapColor.RED, MapColor.SPRUCE_BROWN, BlockSoundGroup.WOOD)
    );
    public static final Block MANGROVE_ROOTS = BlocksCover.register(
            "mangrove_roots",
            MangroveRootsBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(20.0F , 15F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.MANGROVE_ROOTS)
                    .nonOpaque()
                    .suffocates(BlocksCover::never)
                    .blockVision(BlocksCover::never)
                    .nonOpaque()
                    .burnable()
    );
    public static final Block MUDDY_MANGROVE_ROOTS = BlocksCover.register(
            "muddy_mangrove_roots",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .strength(20.0F , 15F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.MUDDY_MANGROVE_ROOTS)
    );
    public static final Block BAMBOO_BLOCK = BlocksCover.register(
            "bamboo_block",
            PillarBlock::new,
            BlocksCover.createLogSettings(MapColor.YELLOW, MapColor.DARK_GREEN, BlockSoundGroup.BAMBOO_WOOD)
    );
    public static final Block STRIPPED_SPRUCE_LOG = BlocksCover.register(
            "stripped_spruce_log",
            PillarBlock::new,
            BlocksCover.createLogSettings(MapColor.SPRUCE_BROWN, MapColor.SPRUCE_BROWN, BlockSoundGroup.WOOD)
    );
    public static final Block STRIPPED_BIRCH_LOG = BlocksCover.register(
            "stripped_birch_log",
            PillarBlock::new,
            BlocksCover.createLogSettings(MapColor.PALE_YELLOW, MapColor.PALE_YELLOW, BlockSoundGroup.WOOD)
    );
    public static final Block STRIPPED_JUNGLE_LOG = BlocksCover.register(
            "stripped_jungle_log",
            PillarBlock::new,
            BlocksCover.createLogSettings(MapColor.DIRT_BROWN, MapColor.DIRT_BROWN, BlockSoundGroup.WOOD)
    );
    public static final Block STRIPPED_ACACIA_LOG = BlocksCover.register(
            "stripped_acacia_log",
            PillarBlock::new,
            BlocksCover.createLogSettings(MapColor.ORANGE, MapColor.ORANGE, BlockSoundGroup.WOOD)
    );
    public static final Block STRIPPED_CHERRY_LOG = BlocksCover.register(
            "stripped_cherry_log",
            PillarBlock::new,
            BlocksCover.createLogSettings(MapColor.TERRACOTTA_WHITE, MapColor.TERRACOTTA_PINK, BlockSoundGroup.CHERRY_WOOD)
    );
    public static final Block STRIPPED_DARK_OAK_LOG = BlocksCover.register(
            "stripped_dark_oak_log",
            PillarBlock::new,
            BlocksCover.createLogSettings(MapColor.BROWN, MapColor.BROWN, BlockSoundGroup.WOOD)
    );
    public static final Block STRIPPED_PALE_OAK_LOG = BlocksCover.register(
            "stripped_pale_oak_log",
            PillarBlock::new,
            BlocksCover.createLogSettings(PALE_OAK_PLANKS.getDefaultMapColor(), PALE_OAK_PLANKS.getDefaultMapColor(), BlockSoundGroup.WOOD)
    );
    public static final Block STRIPPED_OAK_LOG = BlocksCover.register(
            "stripped_oak_log",
            PillarBlock::new,
            BlocksCover.createLogSettings(MapColor.OAK_TAN, MapColor.OAK_TAN, BlockSoundGroup.WOOD)
    );
    public static final Block STRIPPED_MANGROVE_LOG = BlocksCover.register(
            "stripped_mangrove_log",
            PillarBlock::new,
            BlocksCover.createLogSettings(MapColor.RED, MapColor.RED, BlockSoundGroup.WOOD)
    );
    public static final Block STRIPPED_BAMBOO_BLOCK = BlocksCover.register(
            "stripped_bamboo_block",
            PillarBlock::new,
            BlocksCover.createLogSettings(MapColor.YELLOW, MapColor.YELLOW, BlockSoundGroup.BAMBOO_WOOD)
    );
    public static final Block OAK_WOOD = BlocksCover.register(
            "oak_wood",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(20.0F,15.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block SPRUCE_WOOD = BlocksCover.register(
            "spruce_wood",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(20.0F,15.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block BIRCH_WOOD = BlocksCover.register(
            "birch_wood",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_YELLOW)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(20.0F,15.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block JUNGLE_WOOD = BlocksCover.register(
            "jungle_wood",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIRT_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(20.0F,15.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block ACACIA_WOOD = BlocksCover.register(
            "acacia_wood",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(20.0F,15.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block CHERRY_WOOD = BlocksCover.register(
            "cherry_wood",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_GRAY)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(20.0F,15.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.CHERRY_WOOD)
                    .burnable()
    );
    public static final Block DARK_OAK_WOOD = BlocksCover.register(
            "dark_oak_wood",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(20.0F,15.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block MANGROVE_WOOD = BlocksCover.register(
            "mangrove_wood",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.RED)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(20.0F,15.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block STRIPPED_OAK_WOOD = BlocksCover.register(
            "stripped_oak_wood",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(20.0F,15.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block STRIPPED_SPRUCE_WOOD = BlocksCover.register(
            "stripped_spruce_wood",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(20.0F,15.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block STRIPPED_BIRCH_WOOD = BlocksCover.register(
            "stripped_birch_wood",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_YELLOW)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(20.0F,15.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block STRIPPED_JUNGLE_WOOD = BlocksCover.register(
            "stripped_jungle_wood",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIRT_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(20.0F,15.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block STRIPPED_ACACIA_WOOD = BlocksCover.register(
            "stripped_acacia_wood",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(20.0F,15.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block STRIPPED_CHERRY_WOOD = BlocksCover.register(
            "stripped_cherry_wood",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_PINK)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(20.0F,15.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.CHERRY_WOOD)
                    .burnable()
    );
    public static final Block STRIPPED_DARK_OAK_WOOD = BlocksCover.register(
            "stripped_dark_oak_wood",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(20.0F,15.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block STRIPPED_PALE_OAK_WOOD = BlocksCover.register(
            "stripped_pale_oak_wood",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(PALE_OAK_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(20.0F,15.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block STRIPPED_MANGROVE_WOOD = BlocksCover.register(
            "stripped_mangrove_wood",
            PillarBlock::new,
            BlocksCover.createLogSettings(MapColor.RED, MapColor.RED, BlockSoundGroup.WOOD)
    );
    public static final Block OAK_LEAVES = BlocksCover.register(
            "oak_leaves",
            (AbstractBlock.Settings settings) -> new TintedParticleLeavesBlock(0.01f, settings),
            BlocksCover.createLeavesSettings(BlockSoundGroup.GRASS)
    );
    public static final Block SPRUCE_LEAVES = BlocksCover.register(
            "spruce_leaves",
            (AbstractBlock.Settings settings) -> new TintedParticleLeavesBlock(0.01f, settings),
            BlocksCover.createLeavesSettings(BlockSoundGroup.GRASS)
    );
    public static final Block BIRCH_LEAVES = BlocksCover.register(
            "birch_leaves",
            (AbstractBlock.Settings settings) -> new TintedParticleLeavesBlock(0.01f, settings),
            BlocksCover.createLeavesSettings(BlockSoundGroup.GRASS)
    );
    public static final Block JUNGLE_LEAVES = BlocksCover.register(
            "jungle_leaves",
            (AbstractBlock.Settings settings) -> new TintedParticleLeavesBlock(0.01f, settings),
            BlocksCover.createLeavesSettings(BlockSoundGroup.GRASS)
    );
    public static final Block ACACIA_LEAVES = BlocksCover.register(
            "acacia_leaves",
            (AbstractBlock.Settings settings) -> new TintedParticleLeavesBlock(0.01f, settings),
            BlocksCover.createLeavesSettings(BlockSoundGroup.GRASS)
    );
    public static final Block CHERRY_LEAVES = BlocksCover.register(
            "cherry_leaves",
            (AbstractBlock.Settings settings) -> new UntintedParticleLeavesBlock(0.1f, ParticleTypes.CHERRY_LEAVES, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PINK)
                    .strength(0.2f)
                    .ticksRandomly()
                    .sounds(BlockSoundGroup.CHERRY_LEAVES)
                    .nonOpaque()
                    .allowsSpawning(BlocksCover::canSpawnOnLeaves)
                    .suffocates(BlocksCover::never)
                    .blockVision(BlocksCover::never)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .solidBlock(BlocksCover::never)
    );
    public static final Block DARK_OAK_LEAVES = BlocksCover.register(
            "dark_oak_leaves",
            (AbstractBlock.Settings settings) -> new TintedParticleLeavesBlock(0.01f, settings),
            BlocksCover.createLeavesSettings(BlockSoundGroup.GRASS)
    );
    public static final Block PALE_OAK_LEAVES = BlocksCover.register(
            "pale_oak_leaves",
            (AbstractBlock.Settings settings) -> new UntintedParticleLeavesBlock(0.02f, ParticleTypes.PALE_OAK_LEAVES, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.IRON_GRAY)
                    .strength(0.2f)
                    .ticksRandomly()
                    .sounds(BlockSoundGroup.GRASS)
                    .nonOpaque()
                    .allowsSpawning(BlocksCover::canSpawnOnLeaves)
                    .suffocates(BlocksCover::never)
                    .blockVision(BlocksCover::never)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .solidBlock(BlocksCover::never)
    );
    public static final Block MANGROVE_LEAVES = BlocksCover.register(
            "mangrove_leaves",
            (AbstractBlock.Settings settings) -> new MangroveLeavesBlock(0.01f, settings),
            BlocksCover.createLeavesSettings(BlockSoundGroup.GRASS)
    );
    public static final Block AZALEA_LEAVES = BlocksCover.register(
            "azalea_leaves",
            (AbstractBlock.Settings settings) -> new UntintedParticleLeavesBlock(0.01f, TintedParticleEffect.create(ParticleTypes.TINTED_LEAVES, -9399763), settings),
            BlocksCover.createLeavesSettings(BlockSoundGroup.AZALEA_LEAVES)
    );
    public static final Block FLOWERING_AZALEA_LEAVES = BlocksCover.register(
            "flowering_azalea_leaves",
            (AbstractBlock.Settings settings) -> new UntintedParticleLeavesBlock(0.01f, TintedParticleEffect.create(ParticleTypes.TINTED_LEAVES, -9399763), settings),
            BlocksCover.createLeavesSettings(BlockSoundGroup.AZALEA_LEAVES)
    );
    public static final Block SPONGE = BlocksCover.register(
            "sponge",
            SpongeBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.YELLOW)
                    .strength(4.0F,1.0F)
                    .burnable()
                    .sounds(BlockSoundGroup.SPONGE)
    );
    public static final Block WET_SPONGE = BlocksCover.register(
            "wet_sponge",
            WetSpongeBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.YELLOW)
                    .strength(4.0F,1.0F)
                    .sounds(BlockSoundGroup.WET_SPONGE)
    );
    public static final Block GLASS = BlocksCover.register(
            "glass",
            TransparentBlock::new,
            AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(2.0F,2.0F)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
                    .allowsSpawning(BlocksCover::never)
                    .solidBlock(BlocksCover::never)
                    .suffocates(BlocksCover::never)
                    .blockVision(BlocksCover::never)
    );
    public static final Block LAPIS_ORE = BlocksCover.register(
            "lapis_ore",
            (AbstractBlock.Settings settings) -> new ExperienceDroppingBlock(UniformIntProvider.create(2, 5), settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(15.0f, 12.0f)
    );
    public static final Block DEEPSLATE_LAPIS_ORE = BlocksCover.register(
            "deepslate_lapis_ore",
            (AbstractBlock.Settings settings) -> new ExperienceDroppingBlock(UniformIntProvider.create(2, 5), settings),
            AbstractBlock.Settings.copy(LAPIS_ORE).mapColor(MapColor.DEEPSLATE_GRAY).strength(17.0f, 15.0f).sounds(BlockSoundGroup.DEEPSLATE)
    );
    public static final Block LAPIS_BLOCK = BlocksCover.register(
            "lapis_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LAPIS_BLUE)
                    .requiresTool()
                    .strength(10.0F, 10.0F)
    );
    public static final Block DISPENSER = BlocksCover.register(
            "dispenser",
            DispenserBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(3.5f)
    );
    public static final Block SANDSTONE = BlocksCover.register(
            "sandstone",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_YELLOW)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(30F, 25F)
    );
    public static final Block CHISELED_SANDSTONE = BlocksCover.register(
            "chiseled_sandstone",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_YELLOW)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(30F, 25F)
    );
    public static final Block CUT_SANDSTONE = BlocksCover.register(
            "cut_sandstone",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_YELLOW)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(30F, 25F)
    );
    public static final Block NOTE_BLOCK = BlocksCover.register(
            "note_block",
            NoteBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .sounds(BlockSoundGroup.WOOD)
                    .strength(2.0f)
                    .burnable()
    );
    public static final Block WHITE_BED = BlocksCover.registerBedBlock("white_bed", DyeColor.WHITE);
    public static final Block ORANGE_BED = BlocksCover.registerBedBlock("orange_bed", DyeColor.ORANGE);
    public static final Block MAGENTA_BED = BlocksCover.registerBedBlock("magenta_bed", DyeColor.MAGENTA);
    public static final Block LIGHT_BLUE_BED = BlocksCover.registerBedBlock("light_blue_bed", DyeColor.LIGHT_BLUE);
    public static final Block YELLOW_BED = BlocksCover.registerBedBlock("yellow_bed", DyeColor.YELLOW);
    public static final Block LIME_BED = BlocksCover.registerBedBlock("lime_bed", DyeColor.LIME);
    public static final Block PINK_BED = BlocksCover.registerBedBlock("pink_bed", DyeColor.PINK);
    public static final Block GRAY_BED = BlocksCover.registerBedBlock("gray_bed", DyeColor.GRAY);
    public static final Block LIGHT_GRAY_BED = BlocksCover.registerBedBlock("light_gray_bed", DyeColor.LIGHT_GRAY);
    public static final Block CYAN_BED = BlocksCover.registerBedBlock("cyan_bed", DyeColor.CYAN);
    public static final Block PURPLE_BED = BlocksCover.registerBedBlock("purple_bed", DyeColor.PURPLE);
    public static final Block BLUE_BED = BlocksCover.registerBedBlock("blue_bed", DyeColor.BLUE);
    public static final Block BROWN_BED = BlocksCover.registerBedBlock("brown_bed", DyeColor.BROWN);
    public static final Block GREEN_BED = BlocksCover.registerBedBlock("green_bed", DyeColor.GREEN);
    public static final Block RED_BED = BlocksCover.registerBedBlock("red_bed", DyeColor.RED);
    public static final Block BLACK_BED = BlocksCover.registerBedBlock("black_bed", DyeColor.BLACK);
    public static final Block POWERED_RAIL = BlocksCover.register(
            "powered_rail",
            PoweredRailBlock::new,
            AbstractBlock.Settings.create()
                    .noCollision()
                    .strength(4.0F, 2.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
    );
    public static final Block DETECTOR_RAIL = BlocksCover.register(
            "detector_rail",
            DetectorRailBlock::new,
            AbstractBlock.Settings.create()
                    .noCollision()
                    .strength(4.0F, 2.0F)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
    );
    public static final Block COBWEB = BlocksCover.register(
            "cobweb",
            CobwebBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WHITE_GRAY)
                    .sounds(BlockSoundGroup.COBWEB)
                    .solid()
                    .noCollision()
                    .requiresTool()
                    .strength(4.0f)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block SHORT_GRASS = BlocksCover.register(
            "short_grass",
            ShortPlantBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .replaceable()
                    .noCollision()
                    .strength(0.5F)
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XYZ)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block FERN = BlocksCover.register(
            "fern",
            ShortPlantBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .replaceable()
                    .noCollision()
                    .strength(0.5F)
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XYZ)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block DEAD_BUSH = BlocksCover.register(
            "dead_bush",
            DryVegetationBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .replaceable()
                    .noCollision()
                    .strength(0.5F)
                    .sounds(BlockSoundGroup.GRASS)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block BUSH = BlocksCover.register(
            "bush",
            BushBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .replaceable()
                    .noCollision()
                    .strength(0.5F)
                    .sounds(BlockSoundGroup.GRASS)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block SHORT_DRY_GRASS = BlocksCover.register(
            "short_dry_grass",
            ShortDryGrassBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.YELLOW)
                    .replaceable()
                    .noCollision()
                    .strength(0.5F)
                    .sounds(BlockSoundGroup.GRASS)
                    .burnable()
                    .offset(AbstractBlock.OffsetType.XYZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block TALL_DRY_GRASS = BlocksCover.register(
            "tall_dry_grass",
            TallDryGrassBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.YELLOW)
                    .replaceable()
                    .noCollision()
                    .strength(0.7F)
                    .sounds(BlockSoundGroup.GRASS)
                    .burnable()
                    .offset(AbstractBlock.OffsetType.XYZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block SEAGRASS = BlocksCover.register(
            "seagrass",
            SeagrassBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WATER_BLUE)
                    .replaceable()
                    .noCollision()
                    .strength(0.5F)
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block TALL_SEAGRASS = BlocksCover.register(
            "tall_seagrass",
            TallSeagrassBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WATER_BLUE)
                    .replaceable()
                    .noCollision()
                    .strength(0.7F )
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block PISTON_HEAD = BlocksCover.register(
            "piston_head",
            PistonHeadBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(1.5f)
                    .dropsNothing()
                    .pistonBehavior(PistonBehavior.BLOCK)
    );
    public static final Block WHITE_WOOL = BlocksCover.register(
            "white_wool",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WHITE)
                    .instrument(NoteBlockInstrument.GUITAR)
                    .strength(5.0F,5.0F)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block ORANGE_WOOL = BlocksCover.register(
            "orange_wool",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .instrument(NoteBlockInstrument.GUITAR)
                    .strength(5.0F,5.0F)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block MAGENTA_WOOL = BlocksCover.register(
            "magenta_wool",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.MAGENTA)
                    .instrument(NoteBlockInstrument.GUITAR)
                    .strength(5.0F,5.0F)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block LIGHT_BLUE_WOOL = BlocksCover.register(
            "light_blue_wool",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIGHT_BLUE)
                    .instrument(NoteBlockInstrument.GUITAR)
                    .strength(5.0F,5.0F)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block YELLOW_WOOL = BlocksCover.register(
            "yellow_wool",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.YELLOW)
                    .instrument(NoteBlockInstrument.GUITAR)
                    .strength(5.0F,5.0F)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block LIME_WOOL = BlocksCover.register(
            "lime_wool",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIME)
                    .instrument(NoteBlockInstrument.GUITAR)
                    .strength(5.0F,5.0F)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block PINK_WOOL = BlocksCover.register(
            "pink_wool",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PINK)
                    .instrument(NoteBlockInstrument.GUITAR)
                    .strength(5.0F,5.0F)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block GRAY_WOOL = BlocksCover.register(
            "gray_wool",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .instrument(NoteBlockInstrument.GUITAR)
                    .strength(5.0F,5.0F)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block LIGHT_GRAY_WOOL = BlocksCover.register(
            "light_gray_wool",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIGHT_GRAY)
                    .instrument(NoteBlockInstrument.GUITAR)
                    .strength(5.0F,5.0F)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block CYAN_WOOL = BlocksCover.register(
            "cyan_wool",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.CYAN)
                    .instrument(NoteBlockInstrument.GUITAR)
                    .strength(5.0F,5.0F)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block PURPLE_WOOL = BlocksCover.register(
            "purple_wool",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PURPLE)
                    .instrument(NoteBlockInstrument.GUITAR)
                    .strength(5.0F,5.0F)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block BLUE_WOOL = BlocksCover.register(
            "blue_wool",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLUE)
                    .instrument(NoteBlockInstrument.GUITAR)
                    .strength(5.0F,5.0F)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block BROWN_WOOL = BlocksCover.register(
            "brown_wool",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BROWN)
                    .instrument(NoteBlockInstrument.GUITAR)
                    .strength(5.0F,5.0F)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block GREEN_WOOL = BlocksCover.register(
            "green_wool",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GREEN)
                    .instrument(NoteBlockInstrument.GUITAR)
                    .strength(5.0F,5.0F)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block RED_WOOL = BlocksCover.register(
            "red_wool",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.RED)
                    .instrument(NoteBlockInstrument.GUITAR)
                    .strength(5.0F,5.0F)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block BLACK_WOOL = BlocksCover.register(
            "black_wool",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLACK)
                    .instrument(NoteBlockInstrument.GUITAR)
                    .strength(5.0F,5.0F)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block MOVING_PISTON = BlocksCover.register(
            "moving_piston",
            PistonExtensionBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .solid()
                    .strength(-1.0f)
                    .dynamicBounds()
                    .dropsNothing()
                    .nonOpaque()
                    .solidBlock(BlocksCover::never)
                    .suffocates(BlocksCover::never)
                    .blockVision(BlocksCover::never)
                    .pistonBehavior(PistonBehavior.BLOCK)
    );
    public static final Block DANDELION = BlocksCover.register(
            "dandelion",
            (AbstractBlock.Settings settings) -> new FlowerBlock(StatusEffects.SATURATION, 0.35f, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .strength(0.5F)
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block TORCHFLOWER = BlocksCover.register(
            "torchflower",
            (AbstractBlock.Settings settings) -> new FlowerBlock(StatusEffects.NIGHT_VISION, 5.0f, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .strength(0.5F)
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block POPPY = BlocksCover.register(
            "poppy",
            (AbstractBlock.Settings settings) -> new FlowerBlock(StatusEffects.NIGHT_VISION, 5.0f, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .strength(0.5F)
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block BLUE_ORCHID = BlocksCover.register(
            "blue_orchid",
            (AbstractBlock.Settings settings) -> new FlowerBlock(StatusEffects.SATURATION, 0.35f, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .strength(0.5F)
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block ALLIUM = BlocksCover.register(
            "allium",
            (AbstractBlock.Settings settings) -> new FlowerBlock(StatusEffects.FIRE_RESISTANCE, 3.0f, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .strength(0.5F)
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block AZURE_BLUET = BlocksCover.register(
            "azure_bluet",
            (AbstractBlock.Settings settings) -> new FlowerBlock(StatusEffects.BLINDNESS, 11.0f, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .strength(0.5F)
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block RED_TULIP = BlocksCover.register(
            "red_tulip",
            (AbstractBlock.Settings settings) -> new FlowerBlock(StatusEffects.WEAKNESS, 7.0f, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .strength(0.5F)
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block ORANGE_TULIP = BlocksCover.register(
            "orange_tulip",
            (AbstractBlock.Settings settings) -> new FlowerBlock(StatusEffects.WEAKNESS, 7.0f, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .strength(0.5F)
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block WHITE_TULIP = BlocksCover.register(
            "white_tulip",
            (AbstractBlock.Settings settings) -> new FlowerBlock(StatusEffects.WEAKNESS, 7.0f, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .strength(0.5F)
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block PINK_TULIP = BlocksCover.register(
            "pink_tulip",
            (AbstractBlock.Settings settings) -> new FlowerBlock(StatusEffects.WEAKNESS, 7.0f, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .strength(0.5F)
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block OXEYE_DAISY = BlocksCover.register(
            "oxeye_daisy",
            (AbstractBlock.Settings settings) -> new FlowerBlock(StatusEffects.REGENERATION, 7.0f, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .strength(0.5F)
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block CORNFLOWER = BlocksCover.register(
            "cornflower",
            (AbstractBlock.Settings settings) -> new FlowerBlock(StatusEffects.JUMP_BOOST, 5.0f, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .strength(0.5F)
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block WITHER_ROSE = BlocksCover.register(
            "wither_rose",
            (AbstractBlock.Settings settings) -> new WitherRoseBlock(StatusEffects.WITHER, 7.0f, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .strength(0.5F)
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block LILY_OF_THE_VALLEY = BlocksCover.register(
            "lily_of_the_valley",
            (AbstractBlock.Settings settings) -> new FlowerBlock(StatusEffects.POISON, 11.0f, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .strength(0.5F)
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block BROWN_MUSHROOM = BlocksCover.register(
            "brown_mushroom",
            (AbstractBlock.Settings settings) -> new MushroomPlantBlock(TreeConfiguredFeatures.HUGE_BROWN_MUSHROOM, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BROWN)
                    .noCollision()
                    .ticksRandomly()
                    .strength(0.5F)
                    .sounds(BlockSoundGroup.GRASS)
                    .luminance(state -> 1)
                    .postProcess(BlocksCover::always)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block RED_MUSHROOM = BlocksCover.register(
            "red_mushroom",
            (AbstractBlock.Settings settings) -> new MushroomPlantBlock(TreeConfiguredFeatures.HUGE_RED_MUSHROOM, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.RED)
                    .noCollision()
                    .ticksRandomly()
                    .strength(0.5F)
                    .sounds(BlockSoundGroup.GRASS)
                    .postProcess(BlocksCover::always)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block GOLD_BLOCK = BlocksCover.register(
            "gold_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GOLD)
                    .instrument(NoteBlockInstrument.BELL)
                    .requiresTool()
                    .strength(12.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
    );
    public static final Block IRON_BLOCK = BlocksCover.register(
            "iron_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.IRON_GRAY)
                    .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresTool()
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
    );
    public static final Block BRICKS = BlocksCover.register(
            "bricks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(35F, 30F)
    );
    public static final Block TNT = BlocksCover.register(
            "tnt",
            TntBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BRIGHT_RED)
                    .strength(2.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .burnable()
                    .solidBlock(BlocksCover::never)
    );
    public static final Block BOOKSHELF = BlocksCover.register(
            "bookshelf",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(8.0F)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block CHISELED_BOOKSHELF = BlocksCover.register(
            "chiseled_bookshelf",
            ChiseledBookshelfBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(6.5f)
                    .sounds(BlockSoundGroup.CHISELED_BOOKSHELF)
                    .burnable()
    );
    public static final Block ACACIA_SHELF = BlocksCover.register(
            "acacia_shelf",
            ShelfBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .sounds(BlockSoundGroup.SHELF)
                    .burnable()
                    .strength(3.0f, 3.0f)
    );
    public static final Block BAMBOO_SHELF = BlocksCover.register(
            "bamboo_shelf",
            ShelfBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .sounds(BlockSoundGroup.SHELF)
                    .burnable()
                    .strength(3.0f, 3.0f)
    );
    public static final Block BIRCH_SHELF = BlocksCover.register(
            "birch_shelf",
            ShelfBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .sounds(BlockSoundGroup.SHELF)
                    .burnable()
                    .strength(3.0f, 3.0f)
    );
    public static final Block CHERRY_SHELF = BlocksCover.register(
            "cherry_shelf",
            ShelfBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .sounds(BlockSoundGroup.SHELF)
                    .burnable()
                    .strength(3.0f, 3.0f)
    );
    public static final Block CRIMSON_SHELF = BlocksCover.register(
            "crimson_shelf",
            ShelfBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .sounds(BlockSoundGroup.SHELF)
                    .burnable()
                    .strength(3.0f, 3.0f)
    );
    public static final Block DARK_OAK_SHELF = BlocksCover.register(
            "dark_oak_shelf",
            ShelfBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .sounds(BlockSoundGroup.SHELF)
                    .burnable()
                    .strength(3.0f, 3.0f)
    );
    public static final Block JUNGLE_SHELF = BlocksCover.register(
            "jungle_shelf",
            ShelfBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .sounds(BlockSoundGroup.SHELF)
                    .burnable()
                    .strength(3.0f, 3.0f)
    );
    public static final Block MANGROVE_SHELF = BlocksCover.register(
            "mangrove_shelf",
            ShelfBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .sounds(BlockSoundGroup.SHELF)
                    .burnable()
                    .strength(3.0f, 3.0f)
    );
    public static final Block OAK_SHELF = BlocksCover.register(
            "oak_shelf",
            ShelfBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .sounds(BlockSoundGroup.SHELF)
                    .burnable()
                    .strength(3.0f, 3.0f)
    );
    public static final Block PALE_OAK_SHELF = BlocksCover.register(
            "pale_oak_shelf",
            ShelfBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .sounds(BlockSoundGroup.SHELF)
                    .burnable()
                    .strength(3.0f, 3.0f)
    );
    public static final Block SPRUCE_SHELF = BlocksCover.register(
            "spruce_shelf",
            ShelfBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .sounds(BlockSoundGroup.SHELF)
                    .burnable()
                    .strength(3.0f, 3.0f)
    );
    public static final Block WARPED_SHELF = BlocksCover.register(
            "warped_shelf",
            ShelfBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .sounds(BlockSoundGroup.SHELF)
                    .burnable()
                    .strength(3.0f, 3.0f)
    );
    public static final Block MOSSY_COBBLESTONE = BlocksCover.register(
            "mossy_cobblestone",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(30F, 25F)
    );
    public static final Block OBSIDIAN = BlocksCover.register(
            "obsidian",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(50.0F, 1200.0F)
    );
    public static final Block TORCH = BlocksCover.register(
            "torch",
            (AbstractBlock.Settings settings) -> new ModTorchBlock(settings,ParticleTypes.FLAME),
            AbstractBlock.Settings.create()
                    .noCollision()
                    .luminance(state -> state.get(LIT) ? 14 : 0)
                    .sounds(BlockSoundGroup.WOOD)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .strength(0.3F )
    );
    public static final Block WALL_TORCH = BlocksCover.register(
            "wall_torch",
            (AbstractBlock.Settings settings) -> new ModWallTorchBlock(settings ,ParticleTypes.FLAME ),
            BlocksCover.copyLootTable(TORCH, true)
                    .noCollision()
                    .luminance(state -> state.get(LIT) ? 14 : 0)
                    .sounds(BlockSoundGroup.WOOD)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .strength(0.3F , 1.0F)
    );
    public static final Block FIRE = BlocksCover.register(
            "fire",
            FireBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BRIGHT_RED)
                    .replaceable()
                    .noCollision()
                    .breakInstantly()
                    .luminance(state -> 15)
                    .sounds(BlockSoundGroup.WOOL)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block SOUL_FIRE = BlocksCover.register(
            "soul_fire",
            SoulFireBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIGHT_BLUE)
                    .replaceable()
                    .noCollision()
                    .breakInstantly()
                    .luminance(state -> 10)
                    .sounds(BlockSoundGroup.WOOL)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block SPAWNER = BlocksCover.register(
            "spawner",
            SpawnerBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(15.0f)
                    .sounds(BlockSoundGroup.SPAWNER)
                    .nonOpaque()
    );
    public static final Block CREAKING_HEART = BlocksCover.register(
            "creaking_heart",
            CreakingHeartBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(10.0f)
                    .sounds(BlockSoundGroup.CREAKING_HEART)
    );
    public static final Block OAK_STAIRS = BlocksCover.registerStairsBlock("oak_stairs", OAK_PLANKS);
    public static final Block CHEST = BlocksCover.register(
            "chest",
            (AbstractBlock.Settings settings) -> new ChestBlock(() -> BlockEntityType.CHEST, SoundEvents.BLOCK_CHEST_OPEN, SoundEvents.BLOCK_CHEST_CLOSE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(5.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block REDSTONE_WIRE = BlocksCover.register(
            "redstone_wire",
            RedstoneWireBlock::new,
            AbstractBlock.Settings.create()
                    .noCollision()
                    .breakInstantly()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block DIAMOND_ORE = BlocksCover.register(
            "diamond_ore",
            (AbstractBlock.Settings settings) -> new ExperienceDroppingBlock(UniformIntProvider.create(3, 7), settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(25.0f, 3.0f)
    );
    public static final Block DEEPSLATE_DIAMOND_ORE = BlocksCover.register(
            "deepslate_diamond_ore",
            (AbstractBlock.Settings settings) -> new ExperienceDroppingBlock(UniformIntProvider.create(3, 7), settings),
            AbstractBlock.Settings.copy(DIAMOND_ORE).mapColor(MapColor.DEEPSLATE_GRAY).strength(30.0f, 3.0f).sounds(BlockSoundGroup.DEEPSLATE)
    );
    public static final Block DIAMOND_BLOCK = BlocksCover.register(
            "diamond_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIAMOND_BLUE)
                    .requiresTool()
                    .strength(15.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
    );
    public static final Block CRAFTING_TABLE = BlocksCover.register(
            "crafting_table",
            CraftingTableBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(5.5f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block WHEAT = BlocksCover.register(
            "wheat",
            CropBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(state -> state.get(CropBlock.AGE) >= 6 ? MapColor.YELLOW : MapColor.DARK_GREEN)
                    .noCollision()
                    .ticksRandomly()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.CROP)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block FARMLAND = BlocksCover.register(
            "farmland",
            FarmlandBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIRT_BROWN)
                    .ticksRandomly()
                    .strength(7.0f , 3.0f)
                    .sounds(BlockSoundGroup.GRAVEL)
                    .blockVision(BlocksCover::always)
                    .suffocates(BlocksCover::always)
    );
    public static final Block FURNACE = BlocksCover.register(
            "furnace",
            FurnaceBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(5.0f)
                    .luminance(BlocksCover.createLightLevelFromLitBlockCovertate(13))
    );
    public static final Block OAK_SIGN = BlocksCover.register(
            "oak_sign",
            (AbstractBlock.Settings settings) -> new SignBlock(WoodType.OAK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block SPRUCE_SIGN = BlocksCover.register(
            "spruce_sign",
            (AbstractBlock.Settings settings) -> new SignBlock(WoodType.SPRUCE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(SPRUCE_LOG.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block SPRUCE_WALL_SIGN = BlocksCover.register(
            "spruce_wall_sign",
            (AbstractBlock.Settings settings) -> new WallSignBlock(WoodType.SPRUCE, settings),
            BlocksCover.copyLootTable(SPRUCE_SIGN, true).mapColor(SPRUCE_LOG.getDefaultMapColor()).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f).burnable()
    );
    public static final Block BIRCH_SIGN = BlocksCover.register(
            "birch_sign",
            (AbstractBlock.Settings settings) -> new SignBlock(WoodType.BIRCH, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_YELLOW)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block ACACIA_SIGN = BlocksCover.register(
            "acacia_sign",
            (AbstractBlock.Settings settings) -> new SignBlock(WoodType.ACACIA, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block CHERRY_SIGN = BlocksCover.register(
            "cherry_sign",
            (AbstractBlock.Settings settings) -> new SignBlock(WoodType.CHERRY, settings),
            AbstractBlock.Settings.create()
                    .mapColor(CHERRY_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block CHERRY_WALL_SIGN = BlocksCover.register(
            "cherry_wall_sign",
            (AbstractBlock.Settings settings) -> new WallSignBlock(WoodType.CHERRY, settings),
            BlocksCover.copyLootTable(CHERRY_SIGN, true)
                    .mapColor(CHERRY_LOG.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block JUNGLE_SIGN = BlocksCover.register(
            "jungle_sign",
            (AbstractBlock.Settings settings) -> new SignBlock(WoodType.JUNGLE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(JUNGLE_LOG.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block JUNGLE_WALL_SIGN = BlocksCover.register(
            "jungle_wall_sign",
            (AbstractBlock.Settings settings) -> new WallSignBlock(WoodType.JUNGLE, settings),
            BlocksCover.copyLootTable(JUNGLE_SIGN, true)
                    .mapColor(JUNGLE_LOG.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block DARK_OAK_SIGN = BlocksCover.register(
            "dark_oak_sign",
            (AbstractBlock.Settings settings) -> new SignBlock(WoodType.DARK_OAK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(DARK_OAK_LOG.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block DARK_OAK_WALL_SIGN = BlocksCover.register(
            "dark_oak_wall_sign",
            (AbstractBlock.Settings settings) -> new WallSignBlock(WoodType.DARK_OAK, settings),
            BlocksCover.copyLootTable(DARK_OAK_SIGN, true)
                    .mapColor(DARK_OAK_LOG.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block PALE_OAK_SIGN = BlocksCover.register(
            "pale_oak_sign",
            (AbstractBlock.Settings settings) -> new SignBlock(WoodType.PALE_OAK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(PALE_OAK_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block PALE_OAK_WALL_SIGN = BlocksCover.register(
            "pale_oak_wall_sign",
            (AbstractBlock.Settings settings) -> new WallSignBlock(WoodType.PALE_OAK, settings),
            BlocksCover.copyLootTable(PALE_OAK_SIGN, true)
                    .mapColor(PALE_OAK_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block MANGROVE_SIGN = BlocksCover.register(
            "mangrove_sign",
            (AbstractBlock.Settings settings) -> new SignBlock(WoodType.MANGROVE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MANGROVE_LOG.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block MANGROVE_WALL_SIGN = BlocksCover.register(
            "mangrove_wall_sign",
            (AbstractBlock.Settings settings) -> new WallSignBlock(WoodType.MANGROVE, settings),
            BlocksCover.copyLootTable(MANGROVE_SIGN, true)
                    .mapColor(MANGROVE_LOG.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block BAMBOO_SIGN = BlocksCover.register(
            "bamboo_sign",
            (AbstractBlock.Settings settings) -> new SignBlock(WoodType.BAMBOO, settings),
            AbstractBlock.Settings.create()
                    .mapColor(BAMBOO_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block BAMBOO_WALL_SIGN = BlocksCover.register(
            "bamboo_wall_sign",
            (AbstractBlock.Settings settings) -> new WallSignBlock(WoodType.BAMBOO, settings),
            BlocksCover.copyLootTable(BAMBOO_SIGN, true)
                    .mapColor(BAMBOO_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block OAK_DOOR = BlocksCover.register(
            "oak_door",
            (AbstractBlock.Settings settings) -> new DoorBlock(BlockSetType.OAK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(OAK_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(12.0f , 5.0f)
                    .nonOpaque()
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block LADDER = BlocksCover.register(
            "ladder",
            LadderBlock::new,
            AbstractBlock.Settings.create()
                    .strength(2.0f)
                    .sounds(BlockSoundGroup.LADDER)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block RAIL = BlocksCover.register(
            "rail",
            RailBlock::new,
            AbstractBlock.Settings.create()
                    .noCollision()
                    .strength(4.0F, 2.0F)
                    .sounds(BlockSoundGroup.METAL)
    );
    public static final Block COBBLESTONE_STAIRS = BlocksCover.registerStairsBlock("cobblestone_stairs", COBBLESTONE);
    public static final Block OAK_WALL_SIGN = BlocksCover.register(
            "oak_wall_sign",
            (AbstractBlock.Settings settings) -> new WallSignBlock(WoodType.OAK, settings),
            BlocksCover.copyLootTable(OAK_SIGN, true)
                    .mapColor(MapColor.OAK_TAN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block BIRCH_WALL_SIGN = BlocksCover.register(
            "birch_wall_sign",
            (AbstractBlock.Settings settings) -> new WallSignBlock(WoodType.BIRCH, settings),
            BlocksCover.copyLootTable(BIRCH_SIGN, true)
                    .mapColor(MapColor.PALE_YELLOW)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block ACACIA_WALL_SIGN = BlocksCover.register(
            "acacia_wall_sign",
            (AbstractBlock.Settings settings) -> new WallSignBlock(WoodType.ACACIA, settings),
            BlocksCover.copyLootTable(ACACIA_SIGN, true)
                    .mapColor(MapColor.ORANGE)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block OAK_HANGING_SIGN = BlocksCover.register(
            "oak_hanging_sign",
            (AbstractBlock.Settings settings) -> new HangingSignBlock(WoodType.OAK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(OAK_LOG.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block OAK_WALL_HANGING_SIGN = BlocksCover.register(
            "oak_wall_hanging_sign",
            (AbstractBlock.Settings settings) -> new WallHangingSignBlock(WoodType.OAK, settings),
            BlocksCover.copyLootTable(OAK_HANGING_SIGN, true)
                    .mapColor(OAK_LOG.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block SPRUCE_HANGING_SIGN = BlocksCover.register(
            "spruce_hanging_sign",
            (AbstractBlock.Settings settings) -> new HangingSignBlock(WoodType.SPRUCE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(SPRUCE_LOG.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block SPRUCE_WALL_HANGING_SIGN = BlocksCover.register(
            "spruce_wall_hanging_sign",
            (AbstractBlock.Settings settings) -> new WallHangingSignBlock(WoodType.SPRUCE, settings),
            BlocksCover.copyLootTable(SPRUCE_HANGING_SIGN, true)
                    .mapColor(MapColor.OAK_TAN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block BIRCH_HANGING_SIGN = BlocksCover.register(
            "birch_hanging_sign",
            (AbstractBlock.Settings settings) -> new HangingSignBlock(WoodType.BIRCH, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_YELLOW)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block ACACIA_HANGING_SIGN = BlocksCover.register(
            "acacia_hanging_sign",
            (AbstractBlock.Settings settings) -> new HangingSignBlock(WoodType.ACACIA, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block CHERRY_HANGING_SIGN = BlocksCover.register(
            "cherry_hanging_sign",
            (AbstractBlock.Settings settings) -> new HangingSignBlock(WoodType.CHERRY, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_PINK)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block JUNGLE_HANGING_SIGN = BlocksCover.register(
            "jungle_hanging_sign",
            (AbstractBlock.Settings settings) -> new HangingSignBlock(WoodType.JUNGLE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(JUNGLE_LOG.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block JUNGLE_WALL_HANGING_SIGN = BlocksCover.register(
            "jungle_wall_hanging_sign",
            (AbstractBlock.Settings settings) -> new WallHangingSignBlock(WoodType.JUNGLE, settings),
            BlocksCover.copyLootTable(JUNGLE_HANGING_SIGN, true)
                    .mapColor(JUNGLE_LOG.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block DARK_OAK_HANGING_SIGN = BlocksCover.register(
            "dark_oak_hanging_sign",
            (AbstractBlock.Settings settings) -> new HangingSignBlock(WoodType.DARK_OAK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(DARK_OAK_LOG.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block DARK_OAK_WALL_HANGING_SIGN = BlocksCover.register(
            "dark_oak_wall_hanging_sign",
            (AbstractBlock.Settings settings) -> new WallHangingSignBlock(WoodType.DARK_OAK, settings),
            BlocksCover.copyLootTable(DARK_OAK_HANGING_SIGN, true)
                    .mapColor(DARK_OAK_LOG.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block PALE_OAK_HANGING_SIGN = BlocksCover.register(
            "pale_oak_hanging_sign",
            (AbstractBlock.Settings settings) -> new HangingSignBlock(WoodType.PALE_OAK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(PALE_OAK_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block PALE_OAK_WALL_HANGING_SIGN = BlocksCover.register(
            "pale_oak_wall_hanging_sign",
            (AbstractBlock.Settings settings) -> new WallHangingSignBlock(WoodType.PALE_OAK, settings),
            BlocksCover.copyLootTable(PALE_OAK_HANGING_SIGN, true)
                    .mapColor(PALE_OAK_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block CRIMSON_HANGING_SIGN = BlocksCover.register(
            "crimson_hanging_sign",
            (AbstractBlock.Settings settings) -> new HangingSignBlock(WoodType.CRIMSON, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DULL_PINK)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
    );
    public static final Block WARPED_HANGING_SIGN = BlocksCover.register(
            "warped_hanging_sign",
            (AbstractBlock.Settings settings) -> new HangingSignBlock(WoodType.WARPED, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_AQUA)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
    );
    public static final Block MANGROVE_HANGING_SIGN = BlocksCover.register(
            "mangrove_hanging_sign",
            (AbstractBlock.Settings settings) -> new HangingSignBlock(WoodType.MANGROVE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MANGROVE_LOG.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block MANGROVE_WALL_HANGING_SIGN = BlocksCover.register(
            "mangrove_wall_hanging_sign",
            (AbstractBlock.Settings settings) -> new WallHangingSignBlock(WoodType.MANGROVE, settings),
            BlocksCover.copyLootTable(MANGROVE_HANGING_SIGN, true)
                    .mapColor(MANGROVE_LOG.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block BAMBOO_HANGING_SIGN = BlocksCover.register(
            "bamboo_hanging_sign",
            (AbstractBlock.Settings settings) -> new HangingSignBlock(WoodType.BAMBOO, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.YELLOW)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block BIRCH_WALL_HANGING_SIGN = BlocksCover.register(
            "birch_wall_hanging_sign",
            (AbstractBlock.Settings settings) -> new WallHangingSignBlock(WoodType.BIRCH, settings),
            BlocksCover.copyLootTable(BIRCH_HANGING_SIGN, true)
                    .mapColor(MapColor.PALE_YELLOW)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block ACACIA_WALL_HANGING_SIGN = BlocksCover.register(
            "acacia_wall_hanging_sign",
            (AbstractBlock.Settings settings) -> new WallHangingSignBlock(WoodType.ACACIA, settings),
            BlocksCover.copyLootTable(ACACIA_HANGING_SIGN, true)
                    .mapColor(MapColor.ORANGE)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block CHERRY_WALL_HANGING_SIGN = BlocksCover.register(
            "cherry_wall_hanging_sign",
            (AbstractBlock.Settings settings) -> new WallHangingSignBlock(WoodType.CHERRY, settings),
            BlocksCover.copyLootTable(CHERRY_HANGING_SIGN, true)
                    .mapColor(MapColor.TERRACOTTA_PINK)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block CRIMSON_WALL_HANGING_SIGN = BlocksCover.register(
            "crimson_wall_hanging_sign",
            (AbstractBlock.Settings settings) -> new WallHangingSignBlock(WoodType.CRIMSON, settings),
            BlocksCover.copyLootTable(CRIMSON_HANGING_SIGN, true)
                    .mapColor(MapColor.DULL_PINK)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
    );
    public static final Block WARPED_WALL_HANGING_SIGN = BlocksCover.register(
            "warped_wall_hanging_sign",
            (AbstractBlock.Settings settings) -> new WallHangingSignBlock(WoodType.WARPED, settings),
            BlocksCover.copyLootTable(WARPED_HANGING_SIGN, true)
                    .mapColor(MapColor.DARK_AQUA)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
    );
    public static final Block BAMBOO_WALL_HANGING_SIGN = BlocksCover.register(
            "bamboo_wall_hanging_sign",
            (AbstractBlock.Settings settings) -> new WallHangingSignBlock(WoodType.BAMBOO, settings),
            BlocksCover.copyLootTable(BAMBOO_HANGING_SIGN, true)
                    .mapColor(MapColor.YELLOW)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
    );
    public static final Block LEVER = BlocksCover.register(
            "lever",
            LeverBlock::new,
            AbstractBlock.Settings.create()
                    .noCollision()
                    .strength(0.5f)
                    .sounds(BlockSoundGroup.STONE)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block STONE_PRESSURE_PLATE = BlocksCover.register(
            "stone_pressure_plate",
            (AbstractBlock.Settings settings) -> new PressurePlateBlock(BlockSetType.STONE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .solid()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .noCollision()
                    .strength(2.0f)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block IRON_DOOR = BlocksCover.register(
            "iron_door",
            (AbstractBlock.Settings settings) -> new DoorBlock(BlockSetType.IRON, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.IRON_GRAY)
                    .strength(60.0f, 120.0f)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block OAK_PRESSURE_PLATE = BlocksCover.register(
            "oak_pressure_plate",
            (AbstractBlock.Settings settings) -> new PressurePlateBlock(BlockSetType.OAK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(OAK_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(2.0f)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block SPRUCE_PRESSURE_PLATE = BlocksCover.register(
            "spruce_pressure_plate",
            (AbstractBlock.Settings settings) -> new PressurePlateBlock(BlockSetType.SPRUCE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(SPRUCE_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(2.0f)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block BIRCH_PRESSURE_PLATE = BlocksCover.register(
            "birch_pressure_plate",
            (AbstractBlock.Settings settings) -> new PressurePlateBlock(BlockSetType.BIRCH, settings),
            AbstractBlock.Settings.create()
                    .mapColor(BIRCH_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(2.0f)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block JUNGLE_PRESSURE_PLATE = BlocksCover.register(
            "jungle_pressure_plate",
            (AbstractBlock.Settings settings) -> new PressurePlateBlock(BlockSetType.JUNGLE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(JUNGLE_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(2.0f)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block ACACIA_PRESSURE_PLATE = BlocksCover.register(
            "acacia_pressure_plate",
            (AbstractBlock.Settings settings) -> new PressurePlateBlock(BlockSetType.ACACIA, settings),
            AbstractBlock.Settings.create()
                    .mapColor(ACACIA_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(2.0f)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block CHERRY_PRESSURE_PLATE = BlocksCover.register(
            "cherry_pressure_plate",
            (AbstractBlock.Settings settings) -> new PressurePlateBlock(BlockSetType.CHERRY, settings),
            AbstractBlock.Settings.create()
                    .mapColor(CHERRY_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(2.0f)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block DARK_OAK_PRESSURE_PLATE = BlocksCover.register(
            "dark_oak_pressure_plate",
            (AbstractBlock.Settings settings) -> new PressurePlateBlock(BlockSetType.DARK_OAK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(DARK_OAK_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(2.0f)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block PALE_OAK_PRESSURE_PLATE = BlocksCover.register(
            "pale_oak_pressure_plate",
            (AbstractBlock.Settings settings) -> new PressurePlateBlock(BlockSetType.PALE_OAK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(PALE_OAK_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(2.0f)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block MANGROVE_PRESSURE_PLATE = BlocksCover.register(
            "mangrove_pressure_plate",
            (AbstractBlock.Settings settings) -> new PressurePlateBlock(BlockSetType.MANGROVE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MANGROVE_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(2.0f)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block BAMBOO_PRESSURE_PLATE = BlocksCover.register(
            "bamboo_pressure_plate",
            (AbstractBlock.Settings settings) -> new PressurePlateBlock(BlockSetType.BAMBOO, settings),
            AbstractBlock.Settings.create()
                    .mapColor(BAMBOO_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(2.0f)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block REDSTONE_ORE = BlocksCover.register(
            "redstone_ore",
            RedstoneOreBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .ticksRandomly()
                    .luminance(BlocksCover.createLightLevelFromLitBlockCovertate(9))
                    .strength(15.0f, 20.0f)
    );
    public static final Block DEEPSLATE_REDSTONE_ORE = BlocksCover.register(
            "deepslate_redstone_ore",
            RedstoneOreBlock::new,
            AbstractBlock.Settings.copy(REDSTONE_ORE).mapColor(MapColor.DEEPSLATE_GRAY)
                    .strength(17.0f, 25.0f)
                    .sounds(BlockSoundGroup.DEEPSLATE)
    );
    public static final Block REDSTONE_TORCH = BlocksCover.register(
            "redstone_torch",
            RedstoneTorchBlock::new,
            AbstractBlock.Settings.create()
                    .noCollision()
                    .strength(0.5f)
                    .luminance(BlocksCover.createLightLevelFromLitBlockCovertate(7))
                    .sounds(BlockSoundGroup.WOOD)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block REDSTONE_WALL_TORCH = BlocksCover.register(
            "redstone_wall_torch",
            WallRedstoneTorchBlock::new,
            BlocksCover.copyLootTable(REDSTONE_TORCH, true)
                    .noCollision()
                    .strength(0.5f)
                    .luminance(BlocksCover.createLightLevelFromLitBlockCovertate(7))
                    .sounds(BlockSoundGroup.WOOD)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block STONE_BUTTON = BlocksCover.register(
            "stone_button",
            (AbstractBlock.Settings settings) -> new ButtonBlock(BlockSetType.STONE, 20, settings),
            BlocksCover.createButtonSettings()
    );
    public static final Block SNOW = BlocksCover.register(
            "snow",
            SnowBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WHITE)
                    .replaceable()
                    .nonOpaque()
                    .ticksRandomly()
                    .strength(0.5f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.SNOW)
                    .blockVision((state, world, pos) -> state.get(SnowBlock.LAYERS) >= 8)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block ICE = BlocksCover.register(
            "ice",
            IceBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_PURPLE)
                    .slipperiness(0.98f)
                    .ticksRandomly()
                    .strength(5.0f , 5.0f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
                    .allowsSpawning((state, world, pos, entityType) -> entityType == EntityType.POLAR_BEAR)
                    .solidBlock(BlocksCover::never)
    );
    public static final Block SNOW_BLOCK = BlocksCover.register(
            "snow_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WHITE)
                    .strength(3.0F)
                    .sounds(BlockSoundGroup.SNOW)
    );
    public static final Block CACTUS = BlocksCover.register(
            "cactus",
            CactusBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .ticksRandomly()
                    .strength(5.0f)
                    .sounds(BlockSoundGroup.WOOL)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block CACTUS_FLOWER = BlocksCover.register(
            "cactus_flower",
            CactusFlowerBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PINK)
                    .noCollision()
                    .strength(1.0f)
                    .burnable()
                    .sounds(BlockSoundGroup.CACTUS_FLOWER)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block CLAY = BlocksCover.register(
            "clay",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIGHT_BLUE_GRAY)
                    .instrument(NoteBlockInstrument.FLUTE)
                    .strength(11.0f , 6.0f)
                    .sounds(BlockSoundGroup.GRAVEL)
    );
    public static final Block SUGAR_CANE = BlocksCover.register(
            "sugar_cane",
            SugarCaneBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .ticksRandomly()
                    .strength(2.0F,1.0F)
                    .sounds(BlockSoundGroup.GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block JUKEBOX = BlocksCover.register(
            "jukebox",
            JukeboxBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIRT_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0f, 6.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block OAK_FENCE = BlocksCover.register(
            "oak_fence",
            FenceBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(OAK_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(12.0F,5.0F)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block NETHERRACK = BlocksCover.register(
            "netherrack",
            NetherrackBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(25.0f , 15.0f)
                    .sounds(BlockSoundGroup.NETHERRACK)
    );
    public static final Block SOUL_SAND = BlocksCover.register(
            "soul_sand",
            SoulSandBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BROWN)
                    .instrument(NoteBlockInstrument.COW_BELL)
                    .strength(10.0f,15.0f)
                    .velocityMultiplier(0.4f)
                    .sounds(BlockSoundGroup.SOUL_SAND)
                    .allowsSpawning(BlocksCover::always)
                    .solidBlock(BlocksCover::always)
                    .blockVision(BlocksCover::always)
                    .suffocates(BlocksCover::always)
    );
    public static final Block SOUL_SOIL = BlocksCover.register(
            "soul_soil",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BROWN)
                    .strength(11.0f,15.0f)
                    .sounds(BlockSoundGroup.SOUL_SOIL)
    );
    public static final Block BASALT = BlocksCover.register(
            "basalt",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(35F, 30F)
                    .sounds(BlockSoundGroup.BASALT)
    );
    public static final Block POLISHED_BASALT = BlocksCover.register(
            "polished_basalt",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(37.0f, 35.0f)
                    .sounds(BlockSoundGroup.BASALT)
    );
    public static final Block SOUL_TORCH = BlocksCover.register(
            "soul_torch",
            (AbstractBlock.Settings settings) -> new TorchBlock(ParticleTypes.SOUL_FIRE_FLAME, settings),
            AbstractBlock.Settings.create()
                    .noCollision()
                    .strength(0.3f)
                    .luminance(state -> 10)
                    .sounds(BlockSoundGroup.WOOD)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block SOUL_WALL_TORCH = BlocksCover.register(
            "soul_wall_torch",
            (AbstractBlock.Settings settings) -> new WallTorchBlock(ParticleTypes.SOUL_FIRE_FLAME, settings),
            BlocksCover.copyLootTable(SOUL_TORCH, true)
                    .noCollision()
                    .strength(0.3f)
                    .luminance(state -> 10)
                    .sounds(BlockSoundGroup.WOOD)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block COPPER_TORCH = BlocksCover.register(
            "copper_torch",
            (AbstractBlock.Settings settings) -> new TorchBlock(ParticleTypes.COPPER_FIRE_FLAME, settings),
            AbstractBlock.Settings.create()
                    .noCollision()
                    .strength(0.3f)
                    .luminance(state -> 14)
                    .sounds(BlockSoundGroup.WOOD)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block COPPER_WALL_TORCH = BlocksCover.register(
            "copper_wall_torch",
            (AbstractBlock.Settings settings) -> new WallTorchBlock(ParticleTypes.COPPER_FIRE_FLAME, settings),
            BlocksCover.copyLootTable(COPPER_TORCH, true)
                    .noCollision()
                    .strength(0.3f)
                    .luminance(state -> 14)
                    .sounds(BlockSoundGroup.WOOD)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block GLOWSTONE = BlocksCover.register(
            "glowstone",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_YELLOW)
                    .instrument(NoteBlockInstrument.PLING)
                    .strength(5.0F)
                    .sounds(BlockSoundGroup.GLASS)
                    .luminance(state -> 10)
                    .solidBlock(BlocksCover::never)
    );
    public static final Block NETHER_PORTAL = BlocksCover.register(
            "nether_portal",
            NetherPortalBlock::new,
            AbstractBlock.Settings.create()
                    .noCollision()
                    .ticksRandomly()
                    .strength(-1.0f)
                    .sounds(BlockSoundGroup.GLASS)
                    .luminance(state -> 11)
                    .pistonBehavior(PistonBehavior.BLOCK)
    );
    public static final Block CARVED_PUMPKIN = BlocksCover.register(
            "carved_pumpkin",
            CarvedPumpkinBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .allowsSpawning(BlocksCover::always)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block JACK_O_LANTERN = BlocksCover.register(
            "jack_o_lantern",
            CarvedPumpkinBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .luminance(state -> 15)
                    .allowsSpawning(BlocksCover::always)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block CAKE = BlocksCover.register(
            "cake",
            CakeBlock::new,
            AbstractBlock.Settings.create()
                    .solid()
                    .strength(0.5f)
                    .sounds(BlockSoundGroup.WOOL)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block REPEATER = BlocksCover.register(
            "repeater",
            RepeaterBlock::new,
            AbstractBlock.Settings.create()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.STONE)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block WHITE_STAINED_GLASS = BlocksCover.registerStainedGlassBlock("white_stained_glass", DyeColor.WHITE);
    public static final Block ORANGE_STAINED_GLASS = BlocksCover.registerStainedGlassBlock("orange_stained_glass", DyeColor.ORANGE);
    public static final Block MAGENTA_STAINED_GLASS = BlocksCover.registerStainedGlassBlock("magenta_stained_glass", DyeColor.MAGENTA);
    public static final Block LIGHT_BLUE_STAINED_GLASS = BlocksCover.registerStainedGlassBlock("light_blue_stained_glass", DyeColor.LIGHT_BLUE);
    public static final Block YELLOW_STAINED_GLASS = BlocksCover.registerStainedGlassBlock("yellow_stained_glass", DyeColor.YELLOW);
    public static final Block LIME_STAINED_GLASS = BlocksCover.registerStainedGlassBlock("lime_stained_glass", DyeColor.LIME);
    public static final Block PINK_STAINED_GLASS = BlocksCover.registerStainedGlassBlock("pink_stained_glass", DyeColor.PINK);
    public static final Block GRAY_STAINED_GLASS = BlocksCover.registerStainedGlassBlock("gray_stained_glass", DyeColor.GRAY);
    public static final Block LIGHT_GRAY_STAINED_GLASS = BlocksCover.registerStainedGlassBlock("light_gray_stained_glass", DyeColor.LIGHT_GRAY);
    public static final Block CYAN_STAINED_GLASS = BlocksCover.registerStainedGlassBlock("cyan_stained_glass", DyeColor.CYAN);
    public static final Block PURPLE_STAINED_GLASS = BlocksCover.registerStainedGlassBlock("purple_stained_glass", DyeColor.PURPLE);
    public static final Block BLUE_STAINED_GLASS = BlocksCover.registerStainedGlassBlock("blue_stained_glass", DyeColor.BLUE);
    public static final Block BROWN_STAINED_GLASS = BlocksCover.registerStainedGlassBlock("brown_stained_glass", DyeColor.BROWN);
    public static final Block GREEN_STAINED_GLASS = BlocksCover.registerStainedGlassBlock("green_stained_glass", DyeColor.GREEN);
    public static final Block RED_STAINED_GLASS = BlocksCover.registerStainedGlassBlock("red_stained_glass", DyeColor.RED);
    public static final Block BLACK_STAINED_GLASS = BlocksCover.registerStainedGlassBlock("black_stained_glass", DyeColor.BLACK);
    public static final Block OAK_TRAPDOOR = BlocksCover.register(
            "oak_trapdoor",
            (AbstractBlock.Settings settings) -> new TrapdoorBlock(BlockSetType.OAK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(7.0f)
                    .nonOpaque()
                    .allowsSpawning(BlocksCover::never)
                    .burnable()
    );
    public static final Block SPRUCE_TRAPDOOR = BlocksCover.register(
            "spruce_trapdoor",
            (AbstractBlock.Settings settings) -> new TrapdoorBlock(BlockSetType.SPRUCE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(7.0f)
                    .nonOpaque()
                    .allowsSpawning(BlocksCover::never)
                    .burnable()
    );
    public static final Block BIRCH_TRAPDOOR = BlocksCover.register(
            "birch_trapdoor",
            (AbstractBlock.Settings settings) -> new TrapdoorBlock(BlockSetType.BIRCH, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_YELLOW)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(7.0f)
                    .nonOpaque()
                    .allowsSpawning(BlocksCover::never)
                    .burnable()
    );
    public static final Block JUNGLE_TRAPDOOR = BlocksCover.register(
            "jungle_trapdoor",
            (AbstractBlock.Settings settings) -> new TrapdoorBlock(BlockSetType.JUNGLE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIRT_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(7.0f)
                    .nonOpaque()
                    .allowsSpawning(BlocksCover::never)
                    .burnable()
    );
    public static final Block ACACIA_TRAPDOOR = BlocksCover.register(
            "acacia_trapdoor",
            (AbstractBlock.Settings settings) -> new TrapdoorBlock(BlockSetType.ACACIA, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(7.0f)
                    .nonOpaque()
                    .allowsSpawning(BlocksCover::never)
                    .burnable()
    );
    public static final Block CHERRY_TRAPDOOR = BlocksCover.register(
            "cherry_trapdoor",
            (AbstractBlock.Settings settings) -> new TrapdoorBlock(BlockSetType.CHERRY, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_WHITE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(7.0f)
                    .nonOpaque()
                    .allowsSpawning(BlocksCover::never)
                    .burnable()
    );
    public static final Block DARK_OAK_TRAPDOOR = BlocksCover.register(
            "dark_oak_trapdoor",
            (AbstractBlock.Settings settings) -> new TrapdoorBlock(BlockSetType.DARK_OAK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(7.0f)
                    .nonOpaque()
                    .allowsSpawning(BlocksCover::never)
                    .burnable()
    );
    public static final Block PALE_OAK_TRAPDOOR = BlocksCover.register(
            "pale_oak_trapdoor",
            (AbstractBlock.Settings settings) -> new TrapdoorBlock(BlockSetType.PALE_OAK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(PALE_OAK_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(7.0f)
                    .nonOpaque()
                    .allowsSpawning(BlocksCover::never)
                    .burnable()
    );
    public static final Block MANGROVE_TRAPDOOR = BlocksCover.register(
            "mangrove_trapdoor",
            (AbstractBlock.Settings settings) -> new TrapdoorBlock(BlockSetType.MANGROVE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.RED)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(7.0f)
                    .nonOpaque()
                    .allowsSpawning(BlocksCover::never)
                    .burnable()
    );
    public static final Block BAMBOO_TRAPDOOR = BlocksCover.register(
            "bamboo_trapdoor",
            (AbstractBlock.Settings settings) -> new TrapdoorBlock(BlockSetType.BAMBOO, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.YELLOW)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(7.0f)
                    .nonOpaque()
                    .allowsSpawning(BlocksCover::never)
                    .burnable()
    );
    public static final Block STONE_BRICKS = BlocksCover.register(
            "stone_bricks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(35F, 30F)
    );
    public static final Block MOSSY_STONE_BRICKS = BlocksCover.register(
            "mossy_stone_bricks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(35F, 30F)
    );
    public static final Block CRACKED_STONE_BRICKS = BlocksCover.register(
            "cracked_stone_bricks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(35F, 30F)
    );
    public static final Block CHISELED_STONE_BRICKS = BlocksCover.register(
            "chiseled_stone_bricks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(35F, 30F)
    );
    public static final Block PACKED_MUD = BlocksCover.register(
            "packed_mud",
            AbstractBlock.Settings.copy(DIRT)
                    .strength(6.0f, 3.0f)
                    .sounds(BlockSoundGroup.PACKED_MUD)
    );
    public static final Block MUD_BRICKS = BlocksCover.register(
            "mud_bricks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(14.0F, 10.0F)
                    .sounds(BlockSoundGroup.MUD_BRICKS)
    );
    public static final Block INFESTED_STONE = BlocksCover.register(
            "infested_stone",
            (AbstractBlock.Settings settings) -> new InfestedBlock(STONE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIGHT_BLUE_GRAY)
    );
    public static final Block INFESTED_COBBLESTONE = BlocksCover.register(
            "infested_cobblestone",
            (AbstractBlock.Settings settings) -> new InfestedBlock(COBBLESTONE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIGHT_BLUE_GRAY)
    );
    public static final Block INFESTED_STONE_BRICKS = BlocksCover.register(
            "infested_stone_bricks",
            (AbstractBlock.Settings settings) -> new InfestedBlock(STONE_BRICKS, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIGHT_BLUE_GRAY)
    );
    public static final Block INFESTED_MOSSY_STONE_BRICKS = BlocksCover.register(
            "infested_mossy_stone_bricks",
            (AbstractBlock.Settings settings) -> new InfestedBlock(MOSSY_STONE_BRICKS, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIGHT_BLUE_GRAY)
    );
    public static final Block INFESTED_CRACKED_STONE_BRICKS = BlocksCover.register(
            "infested_cracked_stone_bricks",
            (AbstractBlock.Settings settings) -> new InfestedBlock(CRACKED_STONE_BRICKS, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIGHT_BLUE_GRAY)
    );
    public static final Block INFESTED_CHISELED_STONE_BRICKS = BlocksCover.register(
            "infested_chiseled_stone_bricks",
            (AbstractBlock.Settings settings) -> new InfestedBlock(CHISELED_STONE_BRICKS, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIGHT_BLUE_GRAY)
    );
    public static final Block BROWN_MUSHROOM_BLOCK = BlocksCover.register(
            "brown_mushroom_block",
            MushroomBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIRT_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block RED_MUSHROOM_BLOCK = BlocksCover.register(
            "red_mushroom_block",
            MushroomBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.RED)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block MUSHROOM_STEM = BlocksCover.register(
            "mushroom_stem",
            MushroomBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WHITE_GRAY)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(1.2f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block IRON_BARS = BlocksCover.register(
            "iron_bars",
            PaneBlock::new,
            AbstractBlock.Settings.create()
                    .requiresTool()
                    .strength(50.0f, 35.0f)
                    .sounds(BlockSoundGroup.IRON)
                    .nonOpaque()
    );
    public static final Block COPPER_BARS = register(
            "copper_bars",
            PaneBlock::new,
            AbstractBlock.Settings.create()
                    .requiresTool()
                    .strength(40.0f, 25.0f)
                    .sounds(BlockSoundGroup.COPPER)
                    .nonOpaque()
    );
    public static final Block IRON_CHAIN = BlocksCover.register(
            "iron_chain",
            ChainBlock::new,
            AbstractBlock.Settings.create()
                    .solid()
                    .requiresTool()
                    .strength(12.0f, 6.0f)
                    .sounds(BlockSoundGroup.CHAIN)
                    .nonOpaque()
    );
    public static final Block COPPER_CHAINS = register(
            "copper_chain",
            ChainBlock::new,
            AbstractBlock.Settings.create()
                    .solid()
                    .requiresTool()
                    .strength(10.0f, 6.0f)
                    .sounds(BlockSoundGroup.CHAIN)
                    .nonOpaque()
    );
    public static final Block GLASS_PANE = BlocksCover.register(
            "glass_pane",
            PaneBlock::new,
            AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
    );
    public static final Block PUMPKIN = BlocksCover.register(
            BlockKeys.PUMPKIN,
            PumpkinBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .instrument(NoteBlockInstrument.DIDGERIDOO)
                    .strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block MELON = BlocksCover.register(
            BlockKeys.MELON,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIME)
                    .strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block ATTACHED_PUMPKIN_STEM = BlocksCover.register(
            BlockKeys.ATTACHED_PUMPKIN_STEM,
            (AbstractBlock.Settings settings) -> new AttachedStemBlock(BlockKeys.PUMPKIN_STEM, BlockKeys.PUMPKIN, ItemKeys.PUMPKIN_SEEDS, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .strength(1.5f)
                    .sounds(BlockSoundGroup.WOOD)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block ATTACHED_MELON_STEM = BlocksCover.register(
            BlockKeys.ATTACHED_MELON_STEM,
            (AbstractBlock.Settings settings) -> new AttachedStemBlock(BlockKeys.MELON_STEM, BlockKeys.MELON, ItemKeys.MELON_SEEDS, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .strength(1.5f)
                    .sounds(BlockSoundGroup.WOOD)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block PUMPKIN_STEM = BlocksCover.register(
            BlockKeys.PUMPKIN_STEM,
            (AbstractBlock.Settings settings) -> new StemBlock(BlockKeys.PUMPKIN, BlockKeys.ATTACHED_PUMPKIN_STEM, ItemKeys.PUMPKIN_SEEDS, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .ticksRandomly()
                    .strength(1.5f)
                    .sounds(BlockSoundGroup.STEM)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block MELON_STEM = BlocksCover.register(
            BlockKeys.MELON_STEM,
            (AbstractBlock.Settings settings) -> new StemBlock(BlockKeys.MELON, BlockKeys.ATTACHED_MELON_STEM, ItemKeys.MELON_SEEDS, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .ticksRandomly()
                    .strength(1.5f)
                    .sounds(BlockSoundGroup.STEM)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block VINE = BlocksCover.register(
            "vine",
            VineBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .replaceable()
                    .noCollision()
                    .ticksRandomly()
                    .strength(0.5f)
                    .sounds(BlockSoundGroup.VINE)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block GLOW_LICHEN = BlocksCover.register(
            "glow_lichen",
            GlowLichenBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LICHEN_GREEN)
                    .replaceable()
                    .noCollision()
                    .strength(0.2f)
                    .sounds(BlockSoundGroup.GLOW_LICHEN)
                    .luminance(GlowLichenBlock.getLuminanceSupplier(7))
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block RESIN_CLUMP = BlocksCover.register(
            "resin_clump",
            MultifaceBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_ORANGE)
                    .replaceable()
                    .noCollision()
                    .strength(1.2f)
                    .sounds(BlockSoundGroup.RESIN)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block OAK_FENCE_GATE = BlocksCover.register(
            "oak_fence_gate",
            (AbstractBlock.Settings settings) -> new FenceGateBlock(WoodType.OAK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(OAK_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(10.0F,5.0F)
                    .burnable()
    );
    public static final Block BRICK_STAIRS = BlocksCover.registerStairsBlock("brick_stairs", BRICKS);
    public static final Block STONE_BRICK_STAIRS = BlocksCover.registerStairsBlock("stone_brick_stairs", STONE_BRICKS);
    public static final Block MUD_BRICK_STAIRS = BlocksCover.registerStairsBlock("mud_brick_stairs", MUD_BRICKS);
    public static final Block MYCELIUM = BlocksCover.register(
            "mycelium",
            MyceliumBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PURPLE)
                    .ticksRandomly()
                    .strength(7.0f , 5.0f)
                    .sounds(BlockSoundGroup.GRASS)
    );
    public static final Block LILY_PAD = BlocksCover.register(
            "lily_pad",
            LilyPadBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .strength(0.2f)
                    .sounds(BlockSoundGroup.LILY_PAD)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block RESIN_BLOCK = BlocksCover.register(
            "resin_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_ORANGE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(5.0f , 3.0f)
                    .sounds(BlockSoundGroup.RESIN)
    );
    public static final Block RESIN_BRICKS = BlocksCover.register(
            "resin_bricks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_ORANGE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .sounds(BlockSoundGroup.RESIN_BRICKS)
                    .strength(12.0f, 6.0f)
    );
    public static final Block RESIN_BRICK_STAIRS = BlocksCover.registerStairsBlock("resin_brick_stairs", RESIN_BRICKS);
    public static final Block RESIN_BRICK_SLAB = BlocksCover.register(
            "resin_brick_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_ORANGE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .sounds(BlockSoundGroup.RESIN_BRICKS)
                    .strength(10.0f, 6.0f)
    );
    public static final Block RESIN_BRICK_WALL = BlocksCover.register(
            "resin_brick_wall",
            WallBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_ORANGE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .sounds(BlockSoundGroup.RESIN_BRICKS)
                    .strength(15.0f, 10.0f)
    );
    public static final Block CHISELED_RESIN_BRICKS = BlocksCover.register(
            "chiseled_resin_bricks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_ORANGE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .sounds(BlockSoundGroup.RESIN_BRICKS)
                    .strength(12.0f, 7.0f)
    );
    public static final Block NETHER_BRICKS = BlocksCover.register(
            "nether_bricks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(35.0F, 30.0F)
                    .sounds(BlockSoundGroup.NETHER_BRICKS)
    );
    public static final Block NETHER_BRICK_FENCE = BlocksCover.register(
            "nether_brick_fence",
            FenceBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(30.0f, 25.0f)
                    .sounds(BlockSoundGroup.NETHER_BRICKS)
    );
    public static final Block NETHER_BRICK_STAIRS = BlocksCover.registerStairsBlock("nether_brick_stairs", NETHER_BRICKS);
    public static final Block NETHER_WART = BlocksCover.register(
            "nether_wart",
            NetherWartBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.RED)
                    .noCollision()
                    .ticksRandomly()
                    .strength(5.0f , 3.0f)
                    .sounds(BlockSoundGroup.NETHER_WART)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block ENCHANTING_TABLE = BlocksCover.register(
            "enchanting_table",
            EnchantingTableBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .luminance(state -> 7)
                    .strength(5.0f, 1200.0f)
    );
    public static final Block BREWING_STAND = BlocksCover.register(
            "brewing_stand",
            BrewingStandBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.IRON_GRAY)
                    .strength(1.0f)
                    .luminance(state -> 1)
                    .nonOpaque()
    );
    public static final Block CAULDRON = BlocksCover.register(
            "cauldron",
            CauldronBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(5.0f)
                    .nonOpaque()
    );
    public static final Block WATER_CAULDRON = BlocksCover.register(
            "water_cauldron",
            (AbstractBlock.Settings settings) ->
                    new LeveledCauldronBlock(Biome.Precipitation.RAIN, CauldronBehavior.WATER_CAULDRON_BEHAVIOR, settings),
            AbstractBlock.Settings.copy(CAULDRON)
    );
    public static final Block LAVA_CAULDRON = BlocksCover.register(
            "lava_cauldron",
            LavaCauldronBlock::new,
            AbstractBlock.Settings.copy(CAULDRON).luminance(state -> 15)
    );
    public static final Block POWDER_SNOW_CAULDRON = BlocksCover.register(
            "powder_snow_cauldron",
            (AbstractBlock.Settings settings) -> new LeveledCauldronBlock(Biome.Precipitation.SNOW, CauldronBehavior.POWDER_SNOW_CAULDRON_BEHAVIOR, settings),
            AbstractBlock.Settings.copy(CAULDRON)
    );
    public static final Block END_PORTAL = BlocksCover.register(
            "end_portal",
            EndPortalBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLACK)
                    .noCollision()
                    .luminance(state -> 15)
                    .strength(-1.0f, 3600000.0f)
                    .dropsNothing()
                    .pistonBehavior(PistonBehavior.BLOCK)
    );
    public static final Block END_PORTAL_FRAME = BlocksCover.register(
            "end_portal_frame",
            EndPortalFrameBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GREEN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .sounds(BlockSoundGroup.GLASS)
                    .luminance(state -> 1)
                    .strength(-1.0f, 3600000.0f)
                    .dropsNothing()
    );
    public static final Block END_STONE = BlocksCover.register(
            "end_stone",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_YELLOW)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(30.0F, 40.0F)
    );
    public static final Block DRAGON_EGG = BlocksCover.register(
            "dragon_egg",
            DragonEggBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLACK)
                    .strength(3.0f, 9.0f)
                    .luminance(state -> 1)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block REDSTONE_LAMP = BlocksCover.register(
            "redstone_lamp",
            RedstoneLampBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_ORANGE)
                    .luminance(BlocksCover.createLightLevelFromLitBlockCovertate(15))
                    .strength(2.0f)
                    .sounds(BlockSoundGroup.GLASS)
                    .allowsSpawning(BlocksCover::always)
    );
    public static final Block COCOA = BlocksCover.register(
            "cocoa",
            CocoaBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .ticksRandomly()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block SANDSTONE_STAIRS = BlocksCover.registerStairsBlock("sandstone_stairs", SANDSTONE);
    public static final Block EMERALD_ORE = BlocksCover.register(
            "emerald_ore",
            (AbstractBlock.Settings settings) -> new ExperienceDroppingBlock(UniformIntProvider.create(3, 7), settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(12.0f, 10.0f)
    );
    public static final Block DEEPSLATE_EMERALD_ORE = BlocksCover.register(
            "deepslate_emerald_ore",
            (AbstractBlock.Settings settings) -> new ExperienceDroppingBlock(UniformIntProvider.create(3, 7), settings),
            AbstractBlock.Settings.copy(EMERALD_ORE)
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .strength(14.5f, 13.0f)
                    .sounds(BlockSoundGroup.DEEPSLATE)
    );
    public static final Block ENDER_CHEST = BlocksCover.register(
            "ender_chest",
            EnderChestBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(22.5f, 600.0f)
                    .luminance(state -> 7)
    );
    public static final Block TRIPWIRE_HOOK = BlocksCover.register(
            "tripwire_hook",
            TripwireHookBlock::new,
            AbstractBlock.Settings.create()
                    .noCollision()
                    .sounds(BlockSoundGroup.WOOD)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block TRIPWIRE = BlocksCover.register(
            "tripwire",
            (AbstractBlock.Settings settings) -> new TripwireBlock(TRIPWIRE_HOOK, settings),
            AbstractBlock.Settings.create()
                    .noCollision()
                    .breakInstantly()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block EMERALD_BLOCK = BlocksCover.register(
            "emerald_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.EMERALD_GREEN)
                    .instrument(NoteBlockInstrument.BIT)
                    .requiresTool()
                    .strength(12.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
    );
    public static final Block SPRUCE_STAIRS = BlocksCover.registerStairsBlock("spruce_stairs", SPRUCE_PLANKS);
    public static final Block BIRCH_STAIRS = BlocksCover.registerStairsBlock("birch_stairs", BIRCH_PLANKS);
    public static final Block JUNGLE_STAIRS = BlocksCover.registerStairsBlock("jungle_stairs", JUNGLE_PLANKS);
    public static final Block COMMAND_BLOCK = BlocksCover.register(
            "command_block",
            (AbstractBlock.Settings settings) -> new CommandBlock(false, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BROWN)
                    .requiresTool()
                    .strength(-1.0f, 3600000.0f)
                    .dropsNothing()
    );
    public static final Block BEACON = BlocksCover.register(
            "beacon",
            BeaconBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIAMOND_BLUE)
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(3.0f)
                    .luminance(state -> 15)
                    .nonOpaque()
                    .solidBlock(BlocksCover::never)
    );
    public static final Block COBBLESTONE_WALL = BlocksCover.register(
            "cobblestone_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(COBBLESTONE).solid()
    );
    public static final Block MOSSY_COBBLESTONE_WALL = BlocksCover.register(
            "mossy_cobblestone_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(COBBLESTONE).solid()
    );
    public static final Block FLOWER_POT = BlocksCover.register(
            "flower_pot",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(AIR, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_TORCHFLOWER = BlocksCover.register(
            "potted_torchflower",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(TORCHFLOWER, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_OAK_SAPLING = BlocksCover.register(
            "potted_oak_sapling",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(OAK_SAPLING, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_SPRUCE_SAPLING = BlocksCover.register(
            "potted_spruce_sapling",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(SPRUCE_SAPLING, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_BIRCH_SAPLING = BlocksCover.register(
            "potted_birch_sapling",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(BIRCH_SAPLING, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_JUNGLE_SAPLING = BlocksCover.register(
            "potted_jungle_sapling",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(JUNGLE_SAPLING, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_ACACIA_SAPLING = BlocksCover.register(
            "potted_acacia_sapling",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(ACACIA_SAPLING, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_CHERRY_SAPLING = BlocksCover.register(
            "potted_cherry_sapling",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(CHERRY_SAPLING, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_DARK_OAK_SAPLING = BlocksCover.register(
            "potted_dark_oak_sapling",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(DARK_OAK_SAPLING, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_PALE_OAK_SAPLING = BlocksCover.register(
            "potted_pale_oak_sapling",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(PALE_OAK_SAPLING, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_MANGROVE_PROPAGULE = BlocksCover.register(
            "potted_mangrove_propagule",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(MANGROVE_PROPAGULE, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_FERN = BlocksCover.register(
            "potted_fern",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(FERN, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_DANDELION = BlocksCover.register(
            "potted_dandelion",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(DANDELION, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_POPPY = BlocksCover.register(
            "potted_poppy",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(POPPY, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_BLUE_ORCHID = BlocksCover.register(
            "potted_blue_orchid",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(BLUE_ORCHID, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_ALLIUM = BlocksCover.register(
            "potted_allium",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(ALLIUM, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_AZURE_BLUET = BlocksCover.register(
            "potted_azure_bluet",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(AZURE_BLUET, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_RED_TULIP = BlocksCover.register(
            "potted_red_tulip",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(RED_TULIP, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_ORANGE_TULIP = BlocksCover.register(
            "potted_orange_tulip",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(ORANGE_TULIP, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_WHITE_TULIP = BlocksCover.register(
            "potted_white_tulip",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(WHITE_TULIP, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_PINK_TULIP = BlocksCover.register(
            "potted_pink_tulip",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(PINK_TULIP, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_OXEYE_DAISY = BlocksCover.register(
            "potted_oxeye_daisy",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(OXEYE_DAISY, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_CORNFLOWER = BlocksCover.register(
            "potted_cornflower",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(CORNFLOWER, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_LILY_OF_THE_VALLEY = BlocksCover.register(
            "potted_lily_of_the_valley",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(LILY_OF_THE_VALLEY, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_WITHER_ROSE = BlocksCover.register(
            "potted_wither_rose",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(WITHER_ROSE, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_RED_MUSHROOM = BlocksCover.register(
            "potted_red_mushroom",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(RED_MUSHROOM, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_BROWN_MUSHROOM = BlocksCover.register(
            "potted_brown_mushroom",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(BROWN_MUSHROOM, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_DEAD_BUSH = BlocksCover.register(
            "potted_dead_bush",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(DEAD_BUSH, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_CACTUS = BlocksCover.register(
            "potted_cactus",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(CACTUS, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block CARROTS = BlocksCover.register(
            "carrots",
            CarrotsBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .ticksRandomly()
                    .strength(2.0f)
                    .sounds(BlockSoundGroup.CROP)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block POTATOES = BlocksCover.register(
            "potatoes",
            PotatoesBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .ticksRandomly()
                    .strength(2.0f)
                    .sounds(BlockSoundGroup.CROP)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block OAK_BUTTON = BlocksCover.register(
            "oak_button",
            (AbstractBlock.Settings settings) -> new ButtonBlock(BlockSetType.OAK, 30, settings),
            BlocksCover.createButtonSettings()
    );
    public static final Block SPRUCE_BUTTON = BlocksCover.register(
            "spruce_button",
            (AbstractBlock.Settings settings) -> new ButtonBlock(BlockSetType.SPRUCE, 30, settings),
            BlocksCover.createButtonSettings()
    );
    public static final Block BIRCH_BUTTON = BlocksCover.register(
            "birch_button",
            (AbstractBlock.Settings settings) -> new ButtonBlock(BlockSetType.BIRCH, 30, settings),
            BlocksCover.createButtonSettings()
    );
    public static final Block JUNGLE_BUTTON = BlocksCover.register(
            "jungle_button",
            (AbstractBlock.Settings settings) -> new ButtonBlock(BlockSetType.JUNGLE, 30, settings),
            BlocksCover.createButtonSettings()
    );
    public static final Block ACACIA_BUTTON = BlocksCover.register(
            "acacia_button",
            (AbstractBlock.Settings settings) -> new ButtonBlock(BlockSetType.ACACIA, 30, settings),
            BlocksCover.createButtonSettings()
    );
    public static final Block CHERRY_BUTTON = BlocksCover.register(
            "cherry_button",
            (AbstractBlock.Settings settings) -> new ButtonBlock(BlockSetType.CHERRY, 30, settings),
            BlocksCover.createButtonSettings()
    );
    public static final Block DARK_OAK_BUTTON = BlocksCover.register(
            "dark_oak_button",
            (AbstractBlock.Settings settings) -> new ButtonBlock(BlockSetType.DARK_OAK, 30, settings),
            BlocksCover.createButtonSettings()
    );
    public static final Block PALE_OAK_BUTTON = BlocksCover.register(
            "pale_oak_button",
            (AbstractBlock.Settings settings) -> new ButtonBlock(BlockSetType.PALE_OAK, 30, settings),
            BlocksCover.createButtonSettings()
    );
    public static final Block MANGROVE_BUTTON = BlocksCover.register(
            "mangrove_button",
            (AbstractBlock.Settings settings) -> new ButtonBlock(BlockSetType.MANGROVE, 30, settings),
            BlocksCover.createButtonSettings()
    );
    public static final Block BAMBOO_BUTTON = BlocksCover.register(
            "bamboo_button",
            (AbstractBlock.Settings settings) -> new ButtonBlock(BlockSetType.BAMBOO, 30, settings),
            BlocksCover.createButtonSettings()
    );
    public static final Block SKELETON_SKULL = BlocksCover.register(
            "skeleton_skull",
            (AbstractBlock.Settings settings) -> new SkullBlock(SkullBlock.Type.SKELETON, settings),
            AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.SKELETON)
                    .strength(1.0f)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block SKELETON_WALL_SKULL = BlocksCover.register(
            "skeleton_wall_skull",
            (AbstractBlock.Settings settings) -> new WallSkullBlock(SkullBlock.Type.SKELETON, settings),
            BlocksCover.copyLootTable(SKELETON_SKULL, true).strength(1.0f).pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block WITHER_SKELETON_SKULL = BlocksCover.register(
            "wither_skeleton_skull",
            WitherSkullBlock::new,
            AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.WITHER_SKELETON)
                    .strength(1.0f)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block WITHER_SKELETON_WALL_SKULL = BlocksCover.register(
            "wither_skeleton_wall_skull",
            WallWitherSkullBlock::new,
            BlocksCover.copyLootTable(WITHER_SKELETON_SKULL, true).strength(1.0f).pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block ZOMBIE_HEAD = BlocksCover.register(
            "zombie_head",
            (AbstractBlock.Settings settings) -> new SkullBlock(SkullBlock.Type.ZOMBIE, settings),
            AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.ZOMBIE)
                    .strength(1.0f)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block ZOMBIE_WALL_HEAD = BlocksCover.register(
            "zombie_wall_head",
            (AbstractBlock.Settings settings) -> new WallSkullBlock(SkullBlock.Type.ZOMBIE, settings),
            BlocksCover.copyLootTable(ZOMBIE_HEAD, true).strength(1.0f).pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block PLAYER_HEAD = BlocksCover.register(
            "player_head",
            PlayerSkullBlock::new,
            AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.CUSTOM_HEAD)
                    .strength(1.0f)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block PLAYER_WALL_HEAD = BlocksCover.register(
            "player_wall_head",
            WallPlayerSkullBlock::new,
            BlocksCover.copyLootTable(PLAYER_HEAD, true).strength(1.0f).pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block CREEPER_HEAD = BlocksCover.register(
            "creeper_head",
            (AbstractBlock.Settings settings) -> new SkullBlock(SkullBlock.Type.CREEPER, settings),
            AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.CREEPER)
                    .strength(1.0f)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block CREEPER_WALL_HEAD = BlocksCover.register(
            "creeper_wall_head",
            (AbstractBlock.Settings settings) -> new WallSkullBlock(SkullBlock.Type.CREEPER, settings),
            BlocksCover.copyLootTable(CREEPER_HEAD, true).strength(1.0f).pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block DRAGON_HEAD = BlocksCover.register(
            "dragon_head",
            (AbstractBlock.Settings settings) -> new SkullBlock(SkullBlock.Type.DRAGON, settings),
            AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.DRAGON)
                    .strength(1.0f)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block DRAGON_WALL_HEAD = BlocksCover.register(
            "dragon_wall_head",
            (AbstractBlock.Settings settings) -> new WallSkullBlock(SkullBlock.Type.DRAGON, settings),
            BlocksCover.copyLootTable(DRAGON_HEAD, true).strength(1.0f).pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block PIGLIN_HEAD = BlocksCover.register(
            "piglin_head",
            (AbstractBlock.Settings settings) -> new SkullBlock(SkullBlock.Type.PIGLIN, settings),
            AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.PIGLIN)
                    .strength(1.0f)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block PIGLIN_WALL_HEAD = BlocksCover.register(
            "piglin_wall_head",
            WallPiglinHeadBlock::new,
            BlocksCover.copyLootTable(PIGLIN_HEAD, true).strength(1.0f).pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block ANVIL = BlocksCover.register(
            "anvil",
            AnvilBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.IRON_GRAY)
                    .requiresTool()
                    .strength(5.0f, 1200.0f)
                    .sounds(BlockSoundGroup.ANVIL)
                    .pistonBehavior(PistonBehavior.BLOCK)
    );
    public static final Block CHIPPED_ANVIL = BlocksCover.register(
            "chipped_anvil",
            AnvilBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.IRON_GRAY)
                    .requiresTool()
                    .strength(5.0f, 1200.0f)
                    .sounds(BlockSoundGroup.ANVIL)
                    .pistonBehavior(PistonBehavior.BLOCK)
    );
    public static final Block DAMAGED_ANVIL = BlocksCover.register(
            "damaged_anvil",
            AnvilBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.IRON_GRAY)
                    .requiresTool()
                    .strength(5.0f, 1200.0f)
                    .sounds(BlockSoundGroup.ANVIL)
                    .pistonBehavior(PistonBehavior.BLOCK)
    );
    public static final Block TRAPPED_CHEST = BlocksCover.register(
            "trapped_chest",
            TrappedChestBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.5f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block LIGHT_WEIGHTED_PRESSURE_PLATE = BlocksCover.register(
            "light_weighted_pressure_plate",
            (AbstractBlock.Settings settings) -> new WeightedPressurePlateBlock(15, BlockSetType.GOLD, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GOLD)
                    .solid()
                    .noCollision()
                    .strength(2.0f)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block HEAVY_WEIGHTED_PRESSURE_PLATE = BlocksCover.register(
            "heavy_weighted_pressure_plate",
            (AbstractBlock.Settings settings) -> new WeightedPressurePlateBlock(150, BlockSetType.IRON, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.IRON_GRAY)
                    .solid()
                    .noCollision()
                    .strength(2.0f)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block COMPARATOR = BlocksCover.register(
            "comparator",
            ComparatorBlock::new,
            AbstractBlock.Settings.create()
                    .strength(0.5f)
                    .sounds(BlockSoundGroup.STONE)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block DAYLIGHT_DETECTOR = BlocksCover.register(
            "daylight_detector",
            DaylightDetectorBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block REDSTONE_BLOCK = BlocksCover.register(
            "redstone_block",
            RedstoneBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BRIGHT_RED)
                    .requiresTool()
                    .strength(10.0f, 6.0f)
                    .sounds(BlockSoundGroup.METAL)
                    .solidBlock(BlocksCover::never)
    );
    public static final Block NETHER_QUARTZ_ORE = BlocksCover.register(
            "nether_quartz_ore",
            (AbstractBlock.Settings settings) -> new ExperienceDroppingBlock(UniformIntProvider.create(2, 5), settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(15.0f, 20.0f)
                    .sounds(BlockSoundGroup.NETHER_ORE)
    );
    public static final Block HOPPER = BlocksCover.register(
            "hopper",
            HopperBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(3.0f, 4.8f)
                    .sounds(BlockSoundGroup.METAL)
                    .nonOpaque()
    );
    public static final Block QUARTZ_BLOCK = BlocksCover.register(
            "quartz_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OFF_WHITE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(20.0F, 10.0F)
    );
    public static final Block CHISELED_QUARTZ_BLOCK = BlocksCover.register(
            "chiseled_quartz_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OFF_WHITE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(20F, 10F)
    );
    public static final Block QUARTZ_PILLAR = BlocksCover.register(
            "quartz_pillar",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OFF_WHITE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(20F, 10F)
    );
    public static final Block QUARTZ_STAIRS = BlocksCover.registerStairsBlock("quartz_stairs", QUARTZ_BLOCK);
    public static final Block ACTIVATOR_RAIL = BlocksCover.register(
            "activator_rail",
            PoweredRailBlock::new,
            AbstractBlock.Settings.create()
                    .noCollision()
                    .strength(5.0f , 2.0f)
                    .sounds(BlockSoundGroup.METAL)
    );
    public static final Block DROPPER = BlocksCover.register(
            "dropper",
            DropperBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(3.5f)
    );
    public static final Block WHITE_TERRACOTTA = BlocksCover.register(
            "white_terracotta",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_WHITE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
    );
    public static final Block ORANGE_TERRACOTTA = BlocksCover.register(
            "orange_terracotta",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_ORANGE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
    );
    public static final Block MAGENTA_TERRACOTTA = BlocksCover.register(
            "magenta_terracotta",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_MAGENTA)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
    );
    public static final Block LIGHT_BLUE_TERRACOTTA = BlocksCover.register(
            "light_blue_terracotta",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
    );
    public static final Block YELLOW_TERRACOTTA = BlocksCover.register(
            "yellow_terracotta",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_YELLOW)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
    );
    public static final Block LIME_TERRACOTTA = BlocksCover.register(
            "lime_terracotta",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_LIME)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
    );
    public static final Block PINK_TERRACOTTA = BlocksCover.register(
            "pink_terracotta",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_PINK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
    );
    public static final Block GRAY_TERRACOTTA = BlocksCover.register(
            "gray_terracotta",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(12.5f, 14.2f)
    );
    public static final Block LIGHT_GRAY_TERRACOTTA = BlocksCover.register(
            "light_gray_terracotta",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
    );
    public static final Block CYAN_TERRACOTTA = BlocksCover.register(
            "cyan_terracotta",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_CYAN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
    );
    public static final Block PURPLE_TERRACOTTA = BlocksCover.register(
            "purple_terracotta",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_PURPLE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
    );
    public static final Block BLUE_TERRACOTTA = BlocksCover.register(
            "blue_terracotta",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_BLUE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
    );
    public static final Block BROWN_TERRACOTTA = BlocksCover.register(
            "brown_terracotta",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_BROWN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
    );
    public static final Block GREEN_TERRACOTTA = BlocksCover.register(
            "green_terracotta",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_GREEN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(12.5f, 14.2f)
    );
    public static final Block RED_TERRACOTTA = BlocksCover.register(
            "red_terracotta",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
    );
    public static final Block BLACK_TERRACOTTA = BlocksCover.register(
            "black_terracotta",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
    );
    public static final Block WHITE_STAINED_GLASS_PANE = BlocksCover.register(
            "white_stained_glass_pane",
            (AbstractBlock.Settings settings) -> new StainedGlassPaneBlock(DyeColor.WHITE, settings),
            AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
    );
    public static final Block ORANGE_STAINED_GLASS_PANE = BlocksCover.register(
            "orange_stained_glass_pane",
            (AbstractBlock.Settings settings) -> new StainedGlassPaneBlock(DyeColor.ORANGE, settings),
            AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
    );
    public static final Block MAGENTA_STAINED_GLASS_PANE = BlocksCover.register(
            "magenta_stained_glass_pane",
            (AbstractBlock.Settings settings) -> new StainedGlassPaneBlock(DyeColor.MAGENTA, settings),
            AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
    );
    public static final Block LIGHT_BLUE_STAINED_GLASS_PANE = BlocksCover.register(
            "light_blue_stained_glass_pane",
            (AbstractBlock.Settings settings) -> new StainedGlassPaneBlock(DyeColor.LIGHT_BLUE, settings),
            AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
    );
    public static final Block YELLOW_STAINED_GLASS_PANE = BlocksCover.register(
            "yellow_stained_glass_pane",
            (AbstractBlock.Settings settings) -> new StainedGlassPaneBlock(DyeColor.YELLOW, settings),
            AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
    );
    public static final Block LIME_STAINED_GLASS_PANE = BlocksCover.register(
            "lime_stained_glass_pane",
            (AbstractBlock.Settings settings) -> new StainedGlassPaneBlock(DyeColor.LIME, settings),
            AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
    );
    public static final Block PINK_STAINED_GLASS_PANE = BlocksCover.register(
            "pink_stained_glass_pane",
            (AbstractBlock.Settings settings) -> new StainedGlassPaneBlock(DyeColor.PINK, settings),
            AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
    );
    public static final Block GRAY_STAINED_GLASS_PANE = BlocksCover.register(
            "gray_stained_glass_pane",
            (AbstractBlock.Settings settings) -> new StainedGlassPaneBlock(DyeColor.GRAY, settings),
            AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
    );
    public static final Block LIGHT_GRAY_STAINED_GLASS_PANE = BlocksCover.register(
            "light_gray_stained_glass_pane",
            (AbstractBlock.Settings settings) -> new StainedGlassPaneBlock(DyeColor.LIGHT_GRAY, settings),
            AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
    );
    public static final Block CYAN_STAINED_GLASS_PANE = BlocksCover.register(
            "cyan_stained_glass_pane",
            (AbstractBlock.Settings settings) -> new StainedGlassPaneBlock(DyeColor.CYAN, settings),
            AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
    );
    public static final Block PURPLE_STAINED_GLASS_PANE = BlocksCover.register(
            "purple_stained_glass_pane",
            (AbstractBlock.Settings settings) -> new StainedGlassPaneBlock(DyeColor.PURPLE, settings),
            AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
    );
    public static final Block BLUE_STAINED_GLASS_PANE = BlocksCover.register(
            "blue_stained_glass_pane",
            (AbstractBlock.Settings settings) -> new StainedGlassPaneBlock(DyeColor.BLUE, settings),
            AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
    );
    public static final Block BROWN_STAINED_GLASS_PANE = BlocksCover.register(
            "brown_stained_glass_pane",
            (AbstractBlock.Settings settings) -> new StainedGlassPaneBlock(DyeColor.BROWN, settings),
            AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
    );
    public static final Block GREEN_STAINED_GLASS_PANE = BlocksCover.register(
            "green_stained_glass_pane",
            (AbstractBlock.Settings settings) -> new StainedGlassPaneBlock(DyeColor.GREEN, settings),
            AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
    );
    public static final Block RED_STAINED_GLASS_PANE = BlocksCover.register(
            "red_stained_glass_pane",
            (AbstractBlock.Settings settings) -> new StainedGlassPaneBlock(DyeColor.RED, settings),
            AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
    );
    public static final Block BLACK_STAINED_GLASS_PANE = BlocksCover.register(
            "black_stained_glass_pane",
            (AbstractBlock.Settings settings) -> new StainedGlassPaneBlock(DyeColor.BLACK, settings),
            AbstractBlock.Settings.create()
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
    );
    public static final Block ACACIA_STAIRS = BlocksCover.registerStairsBlock("acacia_stairs", ACACIA_PLANKS);
    public static final Block CHERRY_STAIRS = BlocksCover.registerStairsBlock("cherry_stairs", CHERRY_PLANKS);
    public static final Block DARK_OAK_STAIRS = BlocksCover.registerStairsBlock("dark_oak_stairs", DARK_OAK_PLANKS);
    public static final Block PALE_OAK_STAIRS = BlocksCover.registerStairsBlock("pale_oak_stairs", PALE_OAK_PLANKS);
    public static final Block MANGROVE_STAIRS = BlocksCover.registerStairsBlock("mangrove_stairs", MANGROVE_PLANKS);
    public static final Block BAMBOO_STAIRS = BlocksCover.registerStairsBlock("bamboo_stairs", BAMBOO_PLANKS);
    public static final Block BAMBOO_MOSAIC_STAIRS = BlocksCover.registerStairsBlock("bamboo_mosaic_stairs", BAMBOO_MOSAIC);
    public static final Block SLIME_BLOCK = BlocksCover.register(
            "slime_block",
            SlimeBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_GREEN)
                    .slipperiness(0.8f)
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.SLIME)
                    .nonOpaque()
    );
    public static final Block BARRIER = BlocksCover.register(
            "barrier",
            BarrierBlock::new,
            AbstractBlock.Settings.create()
                    .strength(-1.0f, 3600000.8f)
                    .mapColor(BlocksCover.createMapColorFromWaterloggedBlockCovertate(MapColor.CLEAR))
                    .dropsNothing()
                    .nonOpaque()
                    .allowsSpawning(BlocksCover::never)
                    .noBlockBreakParticles()
                    .pistonBehavior(PistonBehavior.BLOCK)
    );
    public static final Block LIGHT = BlocksCover.register(
            "light",
            LightBlock::new,
            AbstractBlock.Settings.create()
                    .replaceable()
                    .strength(-1.0f, 3600000.8f)
                    .mapColor(BlocksCover.createMapColorFromWaterloggedBlockCovertate(MapColor.CLEAR))
                    .dropsNothing()
                    .nonOpaque()
                    .luminance(LightBlock.STATE_TO_LUMINANCE)
    );
    public static final Block IRON_TRAPDOOR = BlocksCover.register(
            "iron_trapdoor",
            (AbstractBlock.Settings settings) -> new TrapdoorBlock(BlockSetType.IRON, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.IRON_GRAY)
                    .requiresTool()
                    .strength(50.0f,45.0f)
                    .nonOpaque()
                    .allowsSpawning(BlocksCover::never)
    );
    public static final Block PRISMARINE = BlocksCover.register(
            "prismarine",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.CYAN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(15.0f, 6.0f)
    );
    public static final Block PRISMARINE_BRICKS = BlocksCover.register(
            "prismarine_bricks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIAMOND_BLUE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(15.0f, 6.0f)
    );
    public static final Block DARK_PRISMARINE = BlocksCover.register(
            "dark_prismarine",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIAMOND_BLUE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(16.0f, 9.0f)
    );
    public static final Block PRISMARINE_STAIRS = BlocksCover.registerStairsBlock("prismarine_stairs", PRISMARINE);
    public static final Block PRISMARINE_BRICK_STAIRS = BlocksCover.registerStairsBlock("prismarine_brick_stairs", PRISMARINE_BRICKS);
    public static final Block DARK_PRISMARINE_STAIRS = BlocksCover.registerStairsBlock("dark_prismarine_stairs", DARK_PRISMARINE);
    public static final Block PRISMARINE_SLAB = BlocksCover.register(
            "prismarine_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.CYAN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(15.0f, 6.0f)
    );
    public static final Block PRISMARINE_BRICK_SLAB = BlocksCover.register(
            "prismarine_brick_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIAMOND_BLUE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(15.0f, 6.0f)
    );
    public static final Block DARK_PRISMARINE_SLAB = BlocksCover.register(
            "dark_prismarine_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIAMOND_BLUE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(16.0f, 9.0f)
    );
    public static final Block SEA_LANTERN = BlocksCover.register(
            "sea_lantern",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OFF_WHITE)
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(3.0F)
                    .sounds(BlockSoundGroup.GLASS)
                    .luminance(state -> 15)
                    .solidBlock(BlocksCover::never)
    );
    public static final Block HAY_BLOCK = BlocksCover.register(
            "hay_block",
            HayBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.YELLOW)
                    .instrument(NoteBlockInstrument.BANJO)
                    .strength(2.0f)
                    .sounds(BlockSoundGroup.GRASS)
    );
    public static final Block WHITE_CARPET = BlocksCover.register(
            "white_carpet",
            (AbstractBlock.Settings settings) -> new DyedCarpetBlock(DyeColor.WHITE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WHITE)
                    .strength(0.1f)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block ORANGE_CARPET = BlocksCover.register(
            "orange_carpet",
            (AbstractBlock.Settings settings) -> new DyedCarpetBlock(DyeColor.ORANGE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .strength(0.1f)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block MAGENTA_CARPET = BlocksCover.register(
            "magenta_carpet",
            (AbstractBlock.Settings settings) -> new DyedCarpetBlock(DyeColor.MAGENTA, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.MAGENTA)
                    .strength(0.1f)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block LIGHT_BLUE_CARPET = BlocksCover.register(
            "light_blue_carpet",
            (AbstractBlock.Settings settings) -> new DyedCarpetBlock(DyeColor.LIGHT_BLUE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIGHT_BLUE)
                    .strength(0.1f)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block YELLOW_CARPET = BlocksCover.register(
            "yellow_carpet",
            (AbstractBlock.Settings settings) -> new DyedCarpetBlock(DyeColor.YELLOW, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.YELLOW)
                    .strength(0.1f)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block LIME_CARPET = BlocksCover.register(
            "lime_carpet",
            (AbstractBlock.Settings settings) -> new DyedCarpetBlock(DyeColor.LIME, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIME)
                    .strength(0.1f)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block PINK_CARPET = BlocksCover.register(
            "pink_carpet",
            (AbstractBlock.Settings settings) -> new DyedCarpetBlock(DyeColor.PINK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PINK)
                    .strength(0.1f)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block GRAY_CARPET = BlocksCover.register(
            "gray_carpet",
            (AbstractBlock.Settings settings) -> new DyedCarpetBlock(DyeColor.GRAY, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .strength(0.1f)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block LIGHT_GRAY_CARPET = BlocksCover.register(
            "light_gray_carpet",
            (AbstractBlock.Settings settings) -> new DyedCarpetBlock(DyeColor.LIGHT_GRAY, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIGHT_GRAY)
                    .strength(0.1f)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block CYAN_CARPET = BlocksCover.register(
            "cyan_carpet",
            (AbstractBlock.Settings settings) -> new DyedCarpetBlock(DyeColor.CYAN, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.CYAN)
                    .strength(0.1f)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block PURPLE_CARPET = BlocksCover.register(
            "purple_carpet",
            (AbstractBlock.Settings settings) -> new DyedCarpetBlock(DyeColor.PURPLE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PURPLE)
                    .strength(0.1f)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block BLUE_CARPET = BlocksCover.register(
            "blue_carpet",
            (AbstractBlock.Settings settings) -> new DyedCarpetBlock(DyeColor.BLUE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLUE)
                    .strength(0.1f)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block BROWN_CARPET = BlocksCover.register(
            "brown_carpet",
            (AbstractBlock.Settings settings) -> new DyedCarpetBlock(DyeColor.BROWN, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BROWN)
                    .strength(0.1f)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block GREEN_CARPET = BlocksCover.register(
            "green_carpet",
            (AbstractBlock.Settings settings) -> new DyedCarpetBlock(DyeColor.GREEN, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GREEN)
                    .strength(0.1f)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block RED_CARPET = BlocksCover.register(
            "red_carpet",
            (AbstractBlock.Settings settings) -> new DyedCarpetBlock(DyeColor.RED, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.RED)
                    .strength(0.1f)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block BLACK_CARPET = BlocksCover.register(
            "black_carpet",
            (AbstractBlock.Settings settings) -> new DyedCarpetBlock(DyeColor.BLACK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLACK)
                    .strength(0.1f)
                    .sounds(BlockSoundGroup.WOOL)
                    .burnable()
    );
    public static final Block TERRACOTTA = BlocksCover.register(
            "terracotta",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
    );
    public static final Block COAL_BLOCK = BlocksCover.register(
            "coal_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(20.0F, 12.0F)
    );
    public static final Block PACKED_ICE = BlocksCover.register(
            "packed_ice",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_PURPLE)
                    .instrument(NoteBlockInstrument.CHIME)
                    .slipperiness(0.8F)
                    .strength(7F, 5F)
                    .sounds(BlockSoundGroup.GLASS)
    );
    public static final Block SUNFLOWER = BlocksCover.register(
            "sunflower",
            TallFlowerBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block LILAC = BlocksCover.register(
            "lilac",
            TallFlowerBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block ROSE_BUSH = BlocksCover.register(
            "rose_bush",
            TallFlowerBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block PEONY = BlocksCover.register(
            "peony",
            TallFlowerBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block TALL_GRASS = BlocksCover.register(
            "tall_grass",
            TallPlantBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .replaceable()
                    .noCollision()
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block LARGE_FERN = BlocksCover.register(
            "large_fern",
            TallPlantBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .replaceable()
                    .noCollision()
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block WHITE_BANNER = BlocksCover.register(
            "white_banner",
            (AbstractBlock.Settings settings) -> new BannerBlock(DyeColor.WHITE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block ORANGE_BANNER = BlocksCover.register(
            "orange_banner",
            (AbstractBlock.Settings settings) -> new BannerBlock(DyeColor.ORANGE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block MAGENTA_BANNER = BlocksCover.register(
            "magenta_banner",
            (AbstractBlock.Settings settings) -> new BannerBlock(DyeColor.MAGENTA, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block LIGHT_BLUE_BANNER = BlocksCover.register(
            "light_blue_banner",
            (AbstractBlock.Settings settings) -> new BannerBlock(DyeColor.LIGHT_BLUE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block YELLOW_BANNER = BlocksCover.register(
            "yellow_banner",
            (AbstractBlock.Settings settings) -> new BannerBlock(DyeColor.YELLOW, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block LIME_BANNER = BlocksCover.register(
            "lime_banner",
            (AbstractBlock.Settings settings) -> new BannerBlock(DyeColor.LIME, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block PINK_BANNER = BlocksCover.register(
            "pink_banner",
            (AbstractBlock.Settings settings) -> new BannerBlock(DyeColor.PINK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block GRAY_BANNER = BlocksCover.register(
            "gray_banner",
            (AbstractBlock.Settings settings) -> new BannerBlock(DyeColor.GRAY, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block LIGHT_GRAY_BANNER = BlocksCover.register(
            "light_gray_banner",
            (AbstractBlock.Settings settings) -> new BannerBlock(DyeColor.LIGHT_GRAY, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block CYAN_BANNER = BlocksCover.register(
            "cyan_banner",
            (AbstractBlock.Settings settings) -> new BannerBlock(DyeColor.CYAN, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block PURPLE_BANNER = BlocksCover.register(
            "purple_banner",
            (AbstractBlock.Settings settings) -> new BannerBlock(DyeColor.PURPLE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block BLUE_BANNER = BlocksCover.register(
            "blue_banner",
            (AbstractBlock.Settings settings) -> new BannerBlock(DyeColor.BLUE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block BROWN_BANNER = BlocksCover.register(
            "brown_banner",
            (AbstractBlock.Settings settings) -> new BannerBlock(DyeColor.BROWN, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block GREEN_BANNER = BlocksCover.register(
            "green_banner",
            (AbstractBlock.Settings settings) -> new BannerBlock(DyeColor.GREEN, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block RED_BANNER = BlocksCover.register(
            "red_banner",
            (AbstractBlock.Settings settings) -> new BannerBlock(DyeColor.RED, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block BLACK_BANNER = BlocksCover.register(
            "black_banner",
            (AbstractBlock.Settings settings) -> new BannerBlock(DyeColor.BLACK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block WHITE_WALL_BANNER = BlocksCover.register(
            "white_wall_banner",
            (AbstractBlock.Settings settings) -> new WallBannerBlock(DyeColor.WHITE, settings),
            BlocksCover.copyLootTable(WHITE_BANNER, true).mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f).sounds(BlockSoundGroup.WOOD).burnable()
    );
    public static final Block ORANGE_WALL_BANNER = BlocksCover.register(
            "orange_wall_banner",
            (AbstractBlock.Settings settings) -> new WallBannerBlock(DyeColor.ORANGE, settings),
            BlocksCover.copyLootTable(ORANGE_BANNER, true).mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f).sounds(BlockSoundGroup.WOOD).burnable()
    );
    public static final Block MAGENTA_WALL_BANNER = BlocksCover.register(
            "magenta_wall_banner",
            (AbstractBlock.Settings settings) -> new WallBannerBlock(DyeColor.MAGENTA, settings),
            BlocksCover.copyLootTable(MAGENTA_BANNER, true).mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f).sounds(BlockSoundGroup.WOOD).burnable()
    );
    public static final Block LIGHT_BLUE_WALL_BANNER = BlocksCover.register(
            "light_blue_wall_banner",
            (AbstractBlock.Settings settings) -> new WallBannerBlock(DyeColor.LIGHT_BLUE, settings),
            BlocksCover.copyLootTable(LIGHT_BLUE_BANNER, true).mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f).sounds(BlockSoundGroup.WOOD).burnable()
    );
    public static final Block YELLOW_WALL_BANNER = BlocksCover.register(
            "yellow_wall_banner",
            (AbstractBlock.Settings settings) -> new WallBannerBlock(DyeColor.YELLOW, settings),
            BlocksCover.copyLootTable(YELLOW_BANNER, true).mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f).sounds(BlockSoundGroup.WOOD).burnable()
    );
    public static final Block LIME_WALL_BANNER = BlocksCover.register(
            "lime_wall_banner",
            (AbstractBlock.Settings settings) -> new WallBannerBlock(DyeColor.LIME, settings),
            BlocksCover.copyLootTable(LIME_BANNER, true).mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f).sounds(BlockSoundGroup.WOOD).burnable()
    );
    public static final Block PINK_WALL_BANNER = BlocksCover.register(
            "pink_wall_banner",
            (AbstractBlock.Settings settings) -> new WallBannerBlock(DyeColor.PINK, settings),
            BlocksCover.copyLootTable(PINK_BANNER, true).mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f).sounds(BlockSoundGroup.WOOD).burnable()
    );
    public static final Block GRAY_WALL_BANNER = BlocksCover.register(
            "gray_wall_banner",
            (AbstractBlock.Settings settings) -> new WallBannerBlock(DyeColor.GRAY, settings),
            BlocksCover.copyLootTable(GRAY_BANNER, true).mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f).sounds(BlockSoundGroup.WOOD).burnable()
    );
    public static final Block LIGHT_GRAY_WALL_BANNER = BlocksCover.register(
            "light_gray_wall_banner",
            (AbstractBlock.Settings settings) -> new WallBannerBlock(DyeColor.LIGHT_GRAY, settings),
            BlocksCover.copyLootTable(LIGHT_GRAY_BANNER, true).mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f).sounds(BlockSoundGroup.WOOD).burnable()
    );
    public static final Block CYAN_WALL_BANNER = BlocksCover.register(
            "cyan_wall_banner",
            (AbstractBlock.Settings settings) -> new WallBannerBlock(DyeColor.CYAN, settings),
            BlocksCover.copyLootTable(CYAN_BANNER, true).mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f).sounds(BlockSoundGroup.WOOD).burnable()
    );
    public static final Block PURPLE_WALL_BANNER = BlocksCover.register(
            "purple_wall_banner",
            (AbstractBlock.Settings settings) -> new WallBannerBlock(DyeColor.PURPLE, settings),
            BlocksCover.copyLootTable(PURPLE_BANNER, true).mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f).sounds(BlockSoundGroup.WOOD).burnable()
    );
    public static final Block BLUE_WALL_BANNER = BlocksCover.register(
            "blue_wall_banner",
            (AbstractBlock.Settings settings) -> new WallBannerBlock(DyeColor.BLUE, settings),
            BlocksCover.copyLootTable(BLUE_BANNER, true).mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f).sounds(BlockSoundGroup.WOOD).burnable()
    );
    public static final Block BROWN_WALL_BANNER = BlocksCover.register(
            "brown_wall_banner",
            (AbstractBlock.Settings settings) -> new WallBannerBlock(DyeColor.BROWN, settings),
            BlocksCover.copyLootTable(BROWN_BANNER, true).mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f).sounds(BlockSoundGroup.WOOD).burnable()
    );
    public static final Block GREEN_WALL_BANNER = BlocksCover.register(
            "green_wall_banner",
            (AbstractBlock.Settings settings) -> new WallBannerBlock(DyeColor.GREEN, settings),
            BlocksCover.copyLootTable(GREEN_BANNER, true).mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f).sounds(BlockSoundGroup.WOOD).burnable()
    );
    public static final Block RED_WALL_BANNER = BlocksCover.register(
            "red_wall_banner",
            (AbstractBlock.Settings settings) -> new WallBannerBlock(DyeColor.RED, settings),
            BlocksCover.copyLootTable(RED_BANNER, true).mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f).sounds(BlockSoundGroup.WOOD).burnable()
    );
    public static final Block BLACK_WALL_BANNER = BlocksCover.register(
            "black_wall_banner",
            (AbstractBlock.Settings settings) -> new WallBannerBlock(DyeColor.BLACK, settings),
            BlocksCover.copyLootTable(BLACK_BANNER, true).mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0f).sounds(BlockSoundGroup.WOOD).burnable()
    );
    public static final Block RED_SANDSTONE = BlocksCover.register(
            "red_sandstone",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(30F, 25F)
    );
    public static final Block CHISELED_RED_SANDSTONE = BlocksCover.register(
            "chiseled_red_sandstone",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(30F, 25F)
    );
    public static final Block CUT_RED_SANDSTONE = BlocksCover.register(
            "cut_red_sandstone",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(30F, 25F)
    );
    public static final Block RED_SANDSTONE_STAIRS = BlocksCover.registerStairsBlock("red_sandstone_stairs", RED_SANDSTONE);
    public static final Block OAK_SLAB = BlocksCover.register(
            "oak_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(10.0f, 3.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block SPRUCE_SLAB = BlocksCover.register(
            "spruce_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(10.0f, 3.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block BIRCH_SLAB = BlocksCover.register(
            "birch_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_YELLOW)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(10.0f, 3.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block JUNGLE_SLAB = BlocksCover.register(
            "jungle_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIRT_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(10.0f, 3.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block ACACIA_SLAB = BlocksCover.register(
            "acacia_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(10.0f, 3.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block CHERRY_SLAB = BlocksCover.register(
            "cherry_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_WHITE)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(10.0f, 3.0f)
                    .sounds(BlockSoundGroup.CHERRY_WOOD)
                    .burnable()
    );
    public static final Block DARK_OAK_SLAB = BlocksCover.register(
            "dark_oak_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(10.0f, 3.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block PALE_OAK_SLAB = BlocksCover.register(
            "pale_oak_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(PALE_OAK_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(10.0f, 3.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block MANGROVE_SLAB = BlocksCover.register(
            "mangrove_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.RED)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(10.0f, 3.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block BAMBOO_SLAB = BlocksCover.register(
            "bamboo_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.YELLOW)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(10.0f, 3.0f)
                    .sounds(BlockSoundGroup.BAMBOO_WOOD)
                    .burnable()
    );
    public static final Block BAMBOO_MOSAIC_SLAB = BlocksCover.register(
            "bamboo_mosaic_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.YELLOW)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(10.0f, 3.0f)
                    .sounds(BlockSoundGroup.BAMBOO_WOOD)
                    .burnable()
    );
    public static final Block STONE_SLAB = BlocksCover.register(
            "stone_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(25.0f, 16.0f)
    );
    public static final Block SMOOTH_STONE_SLAB = BlocksCover.register(
            "smooth_stone_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(25.0f, 16.0f)
    );
    public static final Block SANDSTONE_SLAB = BlocksCover.register(
            "sandstone_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_YELLOW)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(25.0f, 16.0f)
    );
    public static final Block CUT_SANDSTONE_SLAB = BlocksCover.register(
            "cut_sandstone_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_YELLOW)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(25.0f, 16.0f)
    );
    public static final Block PETRIFIED_OAK_SLAB = BlocksCover.register(
            "petrified_oak_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(25.0f, 16.0f)
    );
    public static final Block COBBLESTONE_SLAB = BlocksCover.register(
            "cobblestone_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(25.0f, 16.0f)
    );
    public static final Block BRICK_SLAB = BlocksCover.register(
            "brick_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(25.0f, 16.0f)
    );
    public static final Block STONE_BRICK_SLAB = BlocksCover.register(
            "stone_brick_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(25.0f, 16.0f)
    );
    public static final Block MUD_BRICK_SLAB = BlocksCover.register(
            "mud_brick_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(8.0f, 6.0f)
                    .sounds(BlockSoundGroup.MUD_BRICKS)
    );
    public static final Block NETHER_BRICK_SLAB = BlocksCover.register(
            "nether_brick_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(35.0f, 25.0f)
                    .sounds(BlockSoundGroup.NETHER_BRICKS)
    );
    public static final Block QUARTZ_SLAB = BlocksCover.register(
            "quartz_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OFF_WHITE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(20.0F, 10.0F)
    );
    public static final Block RED_SANDSTONE_SLAB = BlocksCover.register(
            "red_sandstone_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(30F, 25F)
    );
    public static final Block CUT_RED_SANDSTONE_SLAB = BlocksCover.register(
            "cut_red_sandstone_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(30F, 25F)
    );
    public static final Block PURPUR_SLAB = BlocksCover.register(
            "purpur_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.MAGENTA)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(22.0f, 16.0f)
    );
    public static final Block SMOOTH_STONE = BlocksCover.register(
            "smooth_stone",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(30F, 25F)
    );
    public static final Block SMOOTH_SANDSTONE = BlocksCover.register(
            "smooth_sandstone",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_YELLOW)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(30F, 25F)
    );
    public static final Block SMOOTH_QUARTZ = BlocksCover.register(
            "smooth_quartz",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OFF_WHITE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(30F, 25F)
    );
    public static final Block SMOOTH_RED_SANDSTONE = BlocksCover.register(
            "smooth_red_sandstone",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(30F, 25F)
    );
    public static final Block SPRUCE_FENCE_GATE = BlocksCover.register(
            "spruce_fence_gate",
            (AbstractBlock.Settings settings) -> new FenceGateBlock(WoodType.SPRUCE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(SPRUCE_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(8.0F,5.0F)
                    .burnable()
    );
    public static final Block BIRCH_FENCE_GATE = BlocksCover.register(
            "birch_fence_gate",
            (AbstractBlock.Settings settings) -> new FenceGateBlock(WoodType.BIRCH, settings),
            AbstractBlock.Settings.create()
                    .mapColor(BIRCH_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(8.0F,5.0F)
                    .burnable()
    );
    public static final Block JUNGLE_FENCE_GATE = BlocksCover.register(
            "jungle_fence_gate",
            (AbstractBlock.Settings settings) -> new FenceGateBlock(WoodType.JUNGLE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(JUNGLE_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(8.0F,5.0F)
                    .burnable()
    );
    public static final Block ACACIA_FENCE_GATE = BlocksCover.register(
            "acacia_fence_gate",
            (AbstractBlock.Settings settings) -> new FenceGateBlock(WoodType.ACACIA, settings),
            AbstractBlock.Settings.create()
                    .mapColor(ACACIA_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(8.0F,5.0F)
                    .burnable()
    );
    public static final Block CHERRY_FENCE_GATE = BlocksCover.register(
            "cherry_fence_gate",
            (AbstractBlock.Settings settings) -> new FenceGateBlock(WoodType.CHERRY, settings),
            AbstractBlock.Settings.create()
                    .mapColor(CHERRY_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(8.0F,5.0F)
                    .burnable()
    );
    public static final Block DARK_OAK_FENCE_GATE = BlocksCover.register(
            "dark_oak_fence_gate",
            (AbstractBlock.Settings settings) -> new FenceGateBlock(WoodType.DARK_OAK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(DARK_OAK_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(8.0F,5.0F)
                    .burnable()
    );
    public static final Block PALE_OAK_FENCE_GATE = BlocksCover.register(
            "pale_oak_fence_gate",
            (AbstractBlock.Settings settings) -> new FenceGateBlock(WoodType.PALE_OAK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(PALE_OAK_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(8.0F,5.0F)
                    .burnable()
    );
    public static final Block MANGROVE_FENCE_GATE = BlocksCover.register(
            "mangrove_fence_gate",
            (AbstractBlock.Settings settings) -> new FenceGateBlock(WoodType.MANGROVE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MANGROVE_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(8.0F,5.0F)
                    .burnable()
    );
    public static final Block BAMBOO_FENCE_GATE = BlocksCover.register(
            "bamboo_fence_gate",
            (AbstractBlock.Settings settings) -> new FenceGateBlock(WoodType.BAMBOO, settings),
            AbstractBlock.Settings.create()
                    .mapColor(BAMBOO_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(8.0F,5.0F)
                    .burnable()
    );
    public static final Block SPRUCE_FENCE = BlocksCover.register(
            "spruce_fence",
            FenceBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(SPRUCE_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(12.0F,5.0F)
                    .burnable()
                    .sounds(BlockSoundGroup.WOOD)
    );
    public static final Block BIRCH_FENCE = BlocksCover.register(
            "birch_fence",
            FenceBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(BIRCH_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(12.0F,5.0F)
                    .burnable()
                    .sounds(BlockSoundGroup.WOOD)
    );
    public static final Block JUNGLE_FENCE = BlocksCover.register(
            "jungle_fence",
            FenceBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(JUNGLE_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(12.0F,5.0F)
                    .burnable()
                    .sounds(BlockSoundGroup.WOOD)
    );
    public static final Block ACACIA_FENCE = BlocksCover.register(
            "acacia_fence",
            FenceBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(ACACIA_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(12.0F,5.0F)
                    .burnable()
                    .sounds(BlockSoundGroup.WOOD)
    );
    public static final Block CHERRY_FENCE = BlocksCover.register(
            "cherry_fence",
            FenceBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(CHERRY_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(12.0F,5.0F)
                    .burnable()
                    .sounds(BlockSoundGroup.CHERRY_WOOD)
    );
    public static final Block DARK_OAK_FENCE = BlocksCover.register(
            "dark_oak_fence",
            FenceBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(DARK_OAK_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(12.0F,5.0F)
                    .burnable()
                    .sounds(BlockSoundGroup.WOOD)
    );
    public static final Block PALE_OAK_FENCE = BlocksCover.register(
            "pale_oak_fence",
            FenceBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(PALE_OAK_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(12.0F,5.0F)
                    .burnable()
                    .sounds(BlockSoundGroup.WOOD)
    );
    public static final Block MANGROVE_FENCE = BlocksCover.register(
            "mangrove_fence",
            FenceBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MANGROVE_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(12.0F,5.0F)
                    .burnable()
                    .sounds(BlockSoundGroup.WOOD)
    );
    public static final Block BAMBOO_FENCE = BlocksCover.register(
            "bamboo_fence",
            FenceBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(BAMBOO_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(12.0F,5.0F)
                    .sounds(BlockSoundGroup.BAMBOO_WOOD)
                    .burnable()
    );
    public static final Block SPRUCE_DOOR = BlocksCover.register(
            "spruce_door",
            (AbstractBlock.Settings settings) -> new DoorBlock(BlockSetType.SPRUCE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(SPRUCE_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(13.0f , 5.0f)
                    .nonOpaque()
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block BIRCH_DOOR = BlocksCover.register(
            "birch_door",
            (AbstractBlock.Settings settings) -> new DoorBlock(BlockSetType.BIRCH, settings),
            AbstractBlock.Settings.create()
                    .mapColor(BIRCH_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(13.0f , 5.0f)
                    .nonOpaque()
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block JUNGLE_DOOR = BlocksCover.register(
            "jungle_door",
            (AbstractBlock.Settings settings) -> new DoorBlock(BlockSetType.JUNGLE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(JUNGLE_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(13.0f , 5.0f)
                    .nonOpaque()
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block ACACIA_DOOR = BlocksCover.register(
            "acacia_door",
            (AbstractBlock.Settings settings) -> new DoorBlock(BlockSetType.ACACIA, settings),
            AbstractBlock.Settings.create()
                    .mapColor(ACACIA_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(13.0f , 5.0f)
                    .nonOpaque()
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block CHERRY_DOOR = BlocksCover.register(
            "cherry_door",
            (AbstractBlock.Settings settings) -> new DoorBlock(BlockSetType.CHERRY, settings),
            AbstractBlock.Settings.create()
                    .mapColor(CHERRY_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(13.0f , 5.0f)
                    .nonOpaque()
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block DARK_OAK_DOOR = BlocksCover.register(
            "dark_oak_door",
            (AbstractBlock.Settings settings) -> new DoorBlock(BlockSetType.DARK_OAK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(DARK_OAK_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(13.0f , 5.0f)
                    .nonOpaque()
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block PALE_OAK_DOOR = BlocksCover.register(
            "pale_oak_door",
            (AbstractBlock.Settings settings) -> new DoorBlock(BlockSetType.PALE_OAK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(PALE_OAK_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(13.0f , 5.0f)
                    .nonOpaque()
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block MANGROVE_DOOR = BlocksCover.register(
            "mangrove_door",
            (AbstractBlock.Settings settings) -> new DoorBlock(BlockSetType.MANGROVE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MANGROVE_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(13.0f , 5.0f)
                    .nonOpaque()
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block BAMBOO_DOOR = BlocksCover.register(
            "bamboo_door",
            (AbstractBlock.Settings settings) -> new DoorBlock(BlockSetType.BAMBOO, settings),
            AbstractBlock.Settings.create()
                    .mapColor(BAMBOO_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(13.0f , 5.0f)
                    .nonOpaque()
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block END_ROD = BlocksCover.register(
            "end_rod",
            EndRodBlock::new,
            AbstractBlock.Settings.create()
                    .nonOpaque()
                    .strength(1.0f )
                    .luminance(state -> 14)
                    .sounds(BlockSoundGroup.WOOD)
                    .nonOpaque()
    );
    public static final Block CHORUS_PLANT = BlocksCover.register(
            "chorus_plant",
            ChorusPlantBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PURPLE)
                    .strength(5.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block CHORUS_FLOWER = BlocksCover.register(
            "chorus_flower",
            (AbstractBlock.Settings settings) -> new ChorusFlowerBlock(CHORUS_PLANT, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PURPLE)
                    .ticksRandomly()
                    .strength(3.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .nonOpaque()
                    .allowsSpawning(BlocksCover::never)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .solidBlock(BlocksCover::never)
    );
    public static final Block PURPUR_BLOCK = BlocksCover.register(
            "purpur_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.MAGENTA)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(15.0F, 12.0F)
    );
    public static final Block PURPUR_PILLAR = BlocksCover.register(
            "purpur_pillar",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.MAGENTA)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(15.0f, 12.0f)
    );
    public static final Block PURPUR_STAIRS = BlocksCover.registerStairsBlock("purpur_stairs", PURPUR_BLOCK);
    public static final Block END_STONE_BRICKS = BlocksCover.register(
            "end_stone_bricks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_YELLOW)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(45F, 30F)
    );
    public static final Block TORCHFLOWER_CROP = BlocksCover.register(
            "torchflower_crop",
            TorchflowerBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .ticksRandomly()
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.CROP)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block PITCHER_CROP = BlocksCover.register(
            "pitcher_crop",
            PitcherCropBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .ticksRandomly()
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.CROP)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block PITCHER_PLANT = BlocksCover.register(
            "pitcher_plant",
            TallPlantBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.CROP)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block BEETROOTS = BlocksCover.register(
            "beetroots",
            BeetrootsBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .ticksRandomly()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.CROP)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block DIRT_PATH = BlocksCover.register(
            "dirt_path",
            DirtPathBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIRT_BROWN)
                    .strength(12.0f , 6.0f)
                    .sounds(BlockSoundGroup.GRASS)
                    .blockVision(BlocksCover::always)
                    .suffocates(BlocksCover::always)
    );
    public static final Block END_GATEWAY = BlocksCover.register(
            "end_gateway",
            EndGatewayBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLACK)
                    .noCollision()
                    .luminance(state -> 15)
                    .strength(-1.0f, 3600000.0f)
                    .dropsNothing()
                    .pistonBehavior(PistonBehavior.BLOCK)
    );
    public static final Block REPEATING_COMMAND_BLOCK = BlocksCover.register(
            "repeating_command_block",
            (AbstractBlock.Settings settings) -> new CommandBlock(false, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PURPLE)
                    .requiresTool()
                    .strength(-1.0f, 3600000.0f)
                    .dropsNothing()
    );
    public static final Block CHAIN_COMMAND_BLOCK = BlocksCover.register(
            "chain_command_block",
            (AbstractBlock.Settings settings) -> new CommandBlock(true, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GREEN)
                    .requiresTool()
                    .strength(-1.0f, 3600000.0f)
                    .dropsNothing()
    );
    public static final Block FROSTED_ICE = BlocksCover.register(
            "frosted_ice",
            FrostedIceBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_PURPLE)
                    .slipperiness(0.98f)
                    .strength(6.0f , 1.0f)
                    .sounds(BlockSoundGroup.GLASS)
                    .nonOpaque()
                    .allowsSpawning((state, world, pos, entityType) -> entityType == EntityType.POLAR_BEAR)
                    .solidBlock(BlocksCover::never)
    );
    public static final Block MAGMA_BLOCK = BlocksCover.register(
            "magma_block",
            MagmaBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .luminance(state -> 3)
                    .strength(12.0f , 1.0f)
                    .allowsSpawning((state, world, pos, entityType) -> entityType.isFireImmune())
                    .postProcess(BlocksCover::always)
                    .emissiveLighting(BlocksCover::always)
    );
    public static final Block NETHER_WART_BLOCK = BlocksCover.register(
            "nether_wart_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.RED)
                    .strength(3.0F)
                    .sounds(BlockSoundGroup.WART_BLOCK)
    );
    public static final Block RED_NETHER_BRICKS = BlocksCover.register(
            "red_nether_bricks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(45F, 30F)
                    .sounds(BlockSoundGroup.NETHER_BRICKS)
    );
    public static final Block BONE_BLOCK = BlocksCover.register(
            "bone_block",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_YELLOW)
                    .instrument(NoteBlockInstrument.XYLOPHONE)
                    .requiresTool()
                    .strength(12.0f , 2.0f)
                    .sounds(BlockSoundGroup.BONE)
    );
    public static final Block STRUCTURE_VOID = BlocksCover.register(
            "structure_void",
            StructureVoidBlock::new,
            AbstractBlock.Settings.create()
                    .replaceable()
                    .noCollision()
                    .dropsNothing()
                    .noBlockBreakParticles()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block OBSERVER = BlocksCover.register(
            "observer",
            ObserverBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(3.0f)
                    .solidBlock(BlocksCover::never)
    );
    public static final Block WHITE_GLAZED_TERRACOTTA = BlocksCover.register(
            "white_glazed_terracotta",
            GlazedTerracottaBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.WHITE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
                    .pistonBehavior(PistonBehavior.PUSH_ONLY)
    );
    public static final Block ORANGE_GLAZED_TERRACOTTA = BlocksCover.register(
            "orange_glazed_terracotta",
            GlazedTerracottaBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.ORANGE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
                    .pistonBehavior(PistonBehavior.PUSH_ONLY)
    );
    public static final Block MAGENTA_GLAZED_TERRACOTTA = BlocksCover.register(
            "magenta_glazed_terracotta",
            GlazedTerracottaBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.MAGENTA)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
                    .pistonBehavior(PistonBehavior.PUSH_ONLY)
    );
    public static final Block LIGHT_BLUE_GLAZED_TERRACOTTA = BlocksCover.register(
            "light_blue_glazed_terracotta",
            GlazedTerracottaBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.LIGHT_BLUE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
                    .pistonBehavior(PistonBehavior.PUSH_ONLY)
    );
    public static final Block YELLOW_GLAZED_TERRACOTTA = BlocksCover.register(
            "yellow_glazed_terracotta",
            GlazedTerracottaBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.YELLOW)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
                    .pistonBehavior(PistonBehavior.PUSH_ONLY)
    );
    public static final Block LIME_GLAZED_TERRACOTTA = BlocksCover.register(
            "lime_glazed_terracotta",
            GlazedTerracottaBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.LIME)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
                    .pistonBehavior(PistonBehavior.PUSH_ONLY)
    );
    public static final Block PINK_GLAZED_TERRACOTTA = BlocksCover.register(
            "pink_glazed_terracotta",
            GlazedTerracottaBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.PINK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
                    .pistonBehavior(PistonBehavior.PUSH_ONLY)
    );
    public static final Block GRAY_GLAZED_TERRACOTTA = BlocksCover.register(
            "gray_glazed_terracotta",
            GlazedTerracottaBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
                    .pistonBehavior(PistonBehavior.PUSH_ONLY)
    );
    public static final Block LIGHT_GRAY_GLAZED_TERRACOTTA = BlocksCover.register(
            "light_gray_glazed_terracotta",
            GlazedTerracottaBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.LIGHT_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
                    .pistonBehavior(PistonBehavior.PUSH_ONLY)
    );
    public static final Block CYAN_GLAZED_TERRACOTTA = BlocksCover.register(
            "cyan_glazed_terracotta",
            GlazedTerracottaBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.CYAN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
                    .pistonBehavior(PistonBehavior.PUSH_ONLY)
    );
    public static final Block PURPLE_GLAZED_TERRACOTTA = BlocksCover.register(
            "purple_glazed_terracotta",
            GlazedTerracottaBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.PURPLE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
                    .pistonBehavior(PistonBehavior.PUSH_ONLY)
    );
    public static final Block BLUE_GLAZED_TERRACOTTA = BlocksCover.register(
            "blue_glazed_terracotta",
            GlazedTerracottaBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.BLUE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
                    .pistonBehavior(PistonBehavior.PUSH_ONLY)
    );
    public static final Block BROWN_GLAZED_TERRACOTTA = BlocksCover.register(
            "brown_glazed_terracotta",
            GlazedTerracottaBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.BROWN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
                    .pistonBehavior(PistonBehavior.PUSH_ONLY)
    );
    public static final Block GREEN_GLAZED_TERRACOTTA = BlocksCover.register(
            "green_glazed_terracotta",
            GlazedTerracottaBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.GREEN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
                    .pistonBehavior(PistonBehavior.PUSH_ONLY)
    );
    public static final Block RED_GLAZED_TERRACOTTA = BlocksCover.register(
            "red_glazed_terracotta",
            GlazedTerracottaBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
                    .pistonBehavior(PistonBehavior.PUSH_ONLY)
    );
    public static final Block BLACK_GLAZED_TERRACOTTA = BlocksCover.register(
            "black_glazed_terracotta",
            GlazedTerracottaBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(32.5f, 24.2f)
                    .pistonBehavior(PistonBehavior.PUSH_ONLY)
    );
    public static final Block WHITE_CONCRETE = BlocksCover.register(
            "white_concrete",
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.WHITE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(27.5f,  17.0f)
    );
    public static final Block ORANGE_CONCRETE = BlocksCover.register(
            "orange_concrete",
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.ORANGE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(27.5f,  17.0f)
    );
    public static final Block MAGENTA_CONCRETE = BlocksCover.register(
            "magenta_concrete",
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.MAGENTA)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(27.5f,  17.0f)
    );
    public static final Block LIGHT_BLUE_CONCRETE = BlocksCover.register(
            "light_blue_concrete",
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.LIGHT_BLUE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(27.5f,  17.0f)
    );
    public static final Block YELLOW_CONCRETE = BlocksCover.register(
            "yellow_concrete",
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.YELLOW)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(27.5f,  17.0f)
    );
    public static final Block LIME_CONCRETE = BlocksCover.register(
            "lime_concrete",
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.LIME)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(27.5f,  17.0f)
    );
    public static final Block PINK_CONCRETE = BlocksCover.register(
            "pink_concrete",
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.PINK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(27.5f,  17.0f)
    );
    public static final Block GRAY_CONCRETE = BlocksCover.register(
            "gray_concrete",
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(27.5f,  17.0f)
    );
    public static final Block LIGHT_GRAY_CONCRETE = BlocksCover.register(
            "light_gray_concrete",
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.LIGHT_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(27.5f,  17.0f)
    );
    public static final Block CYAN_CONCRETE = BlocksCover.register(
            "cyan_concrete",
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.CYAN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(27.5f,  17.0f)
    );
    public static final Block PURPLE_CONCRETE = BlocksCover.register(
            "purple_concrete",
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.PURPLE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(27.5f,  17.0f)
    );
    public static final Block BLUE_CONCRETE = BlocksCover.register(
            "blue_concrete",
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.BLUE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(27.5f,  17.0f)
    );
    public static final Block BROWN_CONCRETE = BlocksCover.register(
            "brown_concrete",
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.BROWN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(27.5f,  17.0f)
    );
    public static final Block GREEN_CONCRETE = BlocksCover.register(
            "green_concrete",
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.GREEN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(27.5f,  17.0f)
    );
    public static final Block RED_CONCRETE = BlocksCover.register(
            "red_concrete",
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(27.5f,  17.0f)
    );
    public static final Block BLACK_CONCRETE = BlocksCover.register(
            "black_concrete",
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(27.5f,  17.0f)
    );
    public static final Block WHITE_CONCRETE_POWDER = BlocksCover.register(
            "white_concrete_powder",
            (AbstractBlock.Settings settings) -> new ConcretePowderBlock(WHITE_CONCRETE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.WHITE)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(6.0f)
                    .sounds(BlockSoundGroup.SAND)
    );
    public static final Block ORANGE_CONCRETE_POWDER = BlocksCover.register(
            "orange_concrete_powder",
            (AbstractBlock.Settings settings) -> new ConcretePowderBlock(ORANGE_CONCRETE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.ORANGE)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(6.0f)
                    .sounds(BlockSoundGroup.SAND)
    );
    public static final Block MAGENTA_CONCRETE_POWDER = BlocksCover.register(
            "magenta_concrete_powder",
            (AbstractBlock.Settings settings) -> new ConcretePowderBlock(MAGENTA_CONCRETE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.MAGENTA)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(6.0f)
                    .sounds(BlockSoundGroup.SAND)
    );
    public static final Block LIGHT_BLUE_CONCRETE_POWDER = BlocksCover.register(
            "light_blue_concrete_powder",
            (AbstractBlock.Settings settings) -> new ConcretePowderBlock(LIGHT_BLUE_CONCRETE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.LIGHT_BLUE)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(6.0f)
                    .sounds(BlockSoundGroup.SAND)
    );
    public static final Block YELLOW_CONCRETE_POWDER = BlocksCover.register(
            "yellow_concrete_powder",
            (AbstractBlock.Settings settings) -> new ConcretePowderBlock(YELLOW_CONCRETE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.YELLOW)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(6.0f)
                    .sounds(BlockSoundGroup.SAND)
    );
    public static final Block LIME_CONCRETE_POWDER = BlocksCover.register(
            "lime_concrete_powder",
            (AbstractBlock.Settings settings) -> new ConcretePowderBlock(LIME_CONCRETE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.LIME)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(6.0f)
                    .sounds(BlockSoundGroup.SAND)
    );
    public static final Block PINK_CONCRETE_POWDER = BlocksCover.register(
            "pink_concrete_powder",
            (AbstractBlock.Settings settings) -> new ConcretePowderBlock(PINK_CONCRETE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.PINK)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(6.0f)
                    .sounds(BlockSoundGroup.SAND)
    );
    public static final Block GRAY_CONCRETE_POWDER = BlocksCover.register(
            "gray_concrete_powder",
            (AbstractBlock.Settings settings) -> new ConcretePowderBlock(GRAY_CONCRETE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.GRAY)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(6.0f)
                    .sounds(BlockSoundGroup.SAND)
    );
    public static final Block LIGHT_GRAY_CONCRETE_POWDER = BlocksCover.register(
            "light_gray_concrete_powder",
            (AbstractBlock.Settings settings) -> new ConcretePowderBlock(LIGHT_GRAY_CONCRETE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.LIGHT_GRAY)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(6.0f)
                    .sounds(BlockSoundGroup.SAND)
    );
    public static final Block CYAN_CONCRETE_POWDER = BlocksCover.register(
            "cyan_concrete_powder",
            (AbstractBlock.Settings settings) -> new ConcretePowderBlock(CYAN_CONCRETE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.CYAN)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(6.0f)
                    .sounds(BlockSoundGroup.SAND)
    );
    public static final Block PURPLE_CONCRETE_POWDER = BlocksCover.register(
            "purple_concrete_powder",
            (AbstractBlock.Settings settings) -> new ConcretePowderBlock(PURPLE_CONCRETE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.PURPLE)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(6.0f)
                    .sounds(BlockSoundGroup.SAND)
    );
    public static final Block BLUE_CONCRETE_POWDER = BlocksCover.register(
            "blue_concrete_powder",
            (AbstractBlock.Settings settings) -> new ConcretePowderBlock(BLUE_CONCRETE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.BLUE)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(6.0f)
                    .sounds(BlockSoundGroup.SAND)
    );
    public static final Block BROWN_CONCRETE_POWDER = BlocksCover.register(
            "brown_concrete_powder",
            (AbstractBlock.Settings settings) -> new ConcretePowderBlock(BROWN_CONCRETE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.BROWN)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(6.0f)
                    .sounds(BlockSoundGroup.SAND)
    );
    public static final Block GREEN_CONCRETE_POWDER = BlocksCover.register(
            "green_concrete_powder",
            (AbstractBlock.Settings settings) -> new ConcretePowderBlock(GREEN_CONCRETE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.GREEN)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(6.0f)
                    .sounds(BlockSoundGroup.SAND)
    );
    public static final Block RED_CONCRETE_POWDER = BlocksCover.register(
            "red_concrete_powder",
            (AbstractBlock.Settings settings) -> new ConcretePowderBlock(RED_CONCRETE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.RED)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(6.0f)
                    .sounds(BlockSoundGroup.SAND)
    );
    public static final Block BLACK_CONCRETE_POWDER = BlocksCover.register(
            "black_concrete_powder",
            (AbstractBlock.Settings settings) -> new ConcretePowderBlock(BLACK_CONCRETE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(DyeColor.BLACK)
                    .instrument(NoteBlockInstrument.SNARE)
                    .strength(6.0f)
                    .sounds(BlockSoundGroup.SAND)
    );
    public static final Block KELP = BlocksCover.register(
            "kelp",
            KelpBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WATER_BLUE)
                    .noCollision()
                    .ticksRandomly()
                    .strength(1.2f)
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block KELP_PLANT = BlocksCover.register(
            "kelp_plant",
            KelpPlantBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WATER_BLUE)
                    .noCollision()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block DRIED_KELP_BLOCK = BlocksCover.register(
            "dried_kelp_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GREEN)
                    .strength(4.5F, 2.5F)
                    .sounds(BlockSoundGroup.GRASS)
    );
    public static final Block TURTLE_EGG = BlocksCover.register(
            "turtle_egg",
            TurtleEggBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_YELLOW)
                    .solid()
                    .strength(0.5f)
                    .sounds(BlockSoundGroup.METAL)
                    .ticksRandomly()
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block SNIFFER_EGG = BlocksCover.register(
            "sniffer_egg",
            SnifferEggBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.RED)
                    .strength(0.5f)
                    .sounds(BlockSoundGroup.METAL)
                    .nonOpaque()
    );
    public static final Block DRIED_GHAST = BlocksCover.register(
            "dried_ghast",
            DriedGhastBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .solid()
                    .strength(0.5f)
                    .sounds(BlockSoundGroup.DRIED_GHAST)
                    .nonOpaque()
                    .ticksRandomly()
    );
    public static final Block DEAD_TUBE_CORAL_BLOCK = BlocksCover.register(
            "dead_tube_coral_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .solid()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(15F, 6.0F)
    );
    public static final Block DEAD_BRAIN_CORAL_BLOCK = BlocksCover.register(
            "dead_brain_coral_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .solid()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(15F, 6.0F)
    );
    public static final Block DEAD_BUBBLE_CORAL_BLOCK = BlocksCover.register(
            "dead_bubble_coral_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .solid()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(15F, 6.0F)
    );
    public static final Block DEAD_FIRE_CORAL_BLOCK = BlocksCover.register(
            "dead_fire_coral_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .solid()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(15F, 6.0F)
    );
    public static final Block DEAD_HORN_CORAL_BLOCK = BlocksCover.register(
            "dead_horn_coral_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .solid()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(15F, 6.0F)
    );
    public static final Block TUBE_CORAL_BLOCK = BlocksCover.register(
            "tube_coral_block",
            (AbstractBlock.Settings settings) -> new CoralBlockBlock(DEAD_TUBE_CORAL_BLOCK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLUE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(14.0F, 6.0F)
                    .sounds(BlockSoundGroup.CORAL)
    );
    public static final Block BRAIN_CORAL_BLOCK = BlocksCover.register(
            "brain_coral_block",
            (AbstractBlock.Settings settings) -> new CoralBlockBlock(DEAD_BRAIN_CORAL_BLOCK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PINK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(14.0F, 6.0F)
                    .sounds(BlockSoundGroup.CORAL)
    );
    public static final Block BUBBLE_CORAL_BLOCK = BlocksCover.register(
            "bubble_coral_block",
            (AbstractBlock.Settings settings) -> new CoralBlockBlock(DEAD_BUBBLE_CORAL_BLOCK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PURPLE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(14.0F, 6.0F)
                    .sounds(BlockSoundGroup.CORAL)
    );
    public static final Block FIRE_CORAL_BLOCK = BlocksCover.register(
            "fire_coral_block",
            (AbstractBlock.Settings settings) -> new CoralBlockBlock(DEAD_FIRE_CORAL_BLOCK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(14.0F, 6.0F)
                    .sounds(BlockSoundGroup.CORAL)
    );
    public static final Block HORN_CORAL_BLOCK = BlocksCover.register(
            "horn_coral_block",
            (AbstractBlock.Settings settings) -> new CoralBlockBlock(DEAD_HORN_CORAL_BLOCK, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.YELLOW)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(14.0F, 6.0F)
                    .sounds(BlockSoundGroup.CORAL)
    );
    public static final Block DEAD_TUBE_CORAL = BlocksCover.register(
            "dead_tube_coral",
            DeadCoralBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .solid()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .noCollision()
                    .strength(7.0f , 3.0f)
    );
    public static final Block DEAD_BRAIN_CORAL = BlocksCover.register(
            "dead_brain_coral",
            DeadCoralBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .solid()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .noCollision()
                    .strength(7.0f , 3.0f)
    );
    public static final Block DEAD_BUBBLE_CORAL = BlocksCover.register(
            "dead_bubble_coral",
            DeadCoralBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .solid()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .noCollision()
                    .strength(7.0f , 3.0f)
    );
    public static final Block DEAD_FIRE_CORAL = BlocksCover.register(
            "dead_fire_coral",
            DeadCoralBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .solid()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .noCollision()
                    .strength(7.0f , 3.0f)
    );
    public static final Block DEAD_HORN_CORAL = BlocksCover.register(
            "dead_horn_coral",
            DeadCoralBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .solid()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .noCollision()
                    .strength(7.0f , 3.0f)
    );
    public static final Block TUBE_CORAL = BlocksCover.register(
            "tube_coral",
            (AbstractBlock.Settings settings) -> new CoralBlock(DEAD_TUBE_CORAL, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLUE)
                    .noCollision()
                    .strength(6.0f , 3.0f)
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block BRAIN_CORAL = BlocksCover.register(
            "brain_coral",
            (AbstractBlock.Settings settings) -> new CoralBlock(DEAD_BRAIN_CORAL, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PINK)
                    .noCollision()
                    .strength(6.0f , 3.0f)
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block BUBBLE_CORAL = BlocksCover.register(
            "bubble_coral",
            (AbstractBlock.Settings settings) -> new CoralBlock(DEAD_BUBBLE_CORAL, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PURPLE)
                    .noCollision()
                    .strength(6.0f , 3.0f)
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block FIRE_CORAL = BlocksCover.register(
            "fire_coral",
            (AbstractBlock.Settings settings) -> new CoralBlock(DEAD_FIRE_CORAL, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.RED)
                    .noCollision()
                    .strength(6.0f , 3.0f)
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block HORN_CORAL = BlocksCover.register(
            "horn_coral",
            (AbstractBlock.Settings settings) -> new CoralBlock(DEAD_HORN_CORAL, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.YELLOW)
                    .noCollision()
                    .strength(6.0f , 3.0f)
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block DEAD_TUBE_CORAL_FAN = BlocksCover.register(
            "dead_tube_coral_fan",
            DeadCoralFanBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .solid()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .noCollision()
                    .strength(7.0f , 3.0f)
    );
    public static final Block DEAD_BRAIN_CORAL_FAN = BlocksCover.register(
            "dead_brain_coral_fan",
            DeadCoralFanBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .solid()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .noCollision()
                    .strength(7.0f , 3.0f)
    );
    public static final Block DEAD_BUBBLE_CORAL_FAN = BlocksCover.register(
            "dead_bubble_coral_fan",
            DeadCoralFanBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .solid()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .noCollision()
                    .strength(7.0f , 3.0f)
    );
    public static final Block DEAD_FIRE_CORAL_FAN = BlocksCover.register(
            "dead_fire_coral_fan",
            DeadCoralFanBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .solid()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .noCollision()
                    .strength(7.0f , 3.0f)
    );
    public static final Block DEAD_HORN_CORAL_FAN = BlocksCover.register(
            "dead_horn_coral_fan",
            DeadCoralFanBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .solid()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .noCollision()
                    .strength(7.0f , 3.0f)
    );
    public static final Block TUBE_CORAL_FAN = BlocksCover.register(
            "tube_coral_fan",
            (AbstractBlock.Settings settings) -> new CoralFanBlock(DEAD_TUBE_CORAL_FAN, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLUE)
                    .noCollision()
                    .strength(6.0f , 3.0f)
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block BRAIN_CORAL_FAN = BlocksCover.register(
            "brain_coral_fan",
            (AbstractBlock.Settings settings) -> new CoralFanBlock(DEAD_BRAIN_CORAL_FAN, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PINK)
                    .noCollision()
                    .strength(6.0f , 3.0f)
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block BUBBLE_CORAL_FAN = BlocksCover.register(
            "bubble_coral_fan",
            (AbstractBlock.Settings settings) -> new CoralFanBlock(DEAD_BUBBLE_CORAL_FAN, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PURPLE)
                    .noCollision()
                    .strength(6.0f , 3.0f)
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block FIRE_CORAL_FAN = BlocksCover.register(
            "fire_coral_fan",
            (AbstractBlock.Settings settings) -> new CoralFanBlock(DEAD_FIRE_CORAL_FAN, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.RED)
                    .noCollision()
                    .strength(6.0f , 3.0f)
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block HORN_CORAL_FAN = BlocksCover.register(
            "horn_coral_fan",
            (AbstractBlock.Settings settings) -> new CoralFanBlock(DEAD_HORN_CORAL_FAN, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.YELLOW)
                    .noCollision()
                    .strength(6.0f , 3.0f)
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block DEAD_TUBE_CORAL_WALL_FAN = BlocksCover.register(
            "dead_tube_coral_wall_fan",
            DeadCoralWallFanBlock::new,
            BlocksCover.copyLootTable(DEAD_TUBE_CORAL_FAN, false)
                    .mapColor(MapColor.GRAY)
                    .solid()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .noCollision()
                    .strength(7.0f , 3.0f)
    );
    public static final Block TUBE_CORAL_WALL_FAN = BlocksCover.register(
            "tube_coral_wall_fan",
            (AbstractBlock.Settings settings) -> new CoralWallFanBlock(DEAD_TUBE_CORAL_WALL_FAN, settings),
            BlocksCover.copyLootTable(TUBE_CORAL_FAN, false)
                    .mapColor(MapColor.BLUE)
                    .noCollision()
                    .strength(7.0f , 3.0f)
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block DEAD_BRAIN_CORAL_WALL_FAN = BlocksCover.register(
            "dead_brain_coral_wall_fan",
            DeadCoralWallFanBlock::new,
            BlocksCover.copyLootTable(DEAD_BRAIN_CORAL_FAN, false)
                    .mapColor(MapColor.GRAY)
                    .solid()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .noCollision()
                    .strength(7.0f , 3.0f)
    );
    public static final Block BRAIN_CORAL_WALL_FAN = BlocksCover.register(
            "brain_coral_wall_fan",
            (AbstractBlock.Settings settings) -> new CoralWallFanBlock(DEAD_BRAIN_CORAL_WALL_FAN, settings),
            BlocksCover.copyLootTable(BRAIN_CORAL_FAN, false)
                    .mapColor(MapColor.PINK)
                    .noCollision()
                    .strength(7.0f , 3.0f)
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block DEAD_BUBBLE_CORAL_WALL_FAN = BlocksCover.register(
            "dead_bubble_coral_wall_fan",
            DeadCoralWallFanBlock::new,
            BlocksCover.copyLootTable(DEAD_BUBBLE_CORAL_FAN, false)
                    .mapColor(MapColor.GRAY)
                    .solid()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .noCollision()
                    .strength(7.0f , 3.0f)
    );
    public static final Block BUBBLE_CORAL_WALL_FAN = BlocksCover.register(
            "bubble_coral_wall_fan",
            (AbstractBlock.Settings settings) -> new CoralWallFanBlock(DEAD_BUBBLE_CORAL_WALL_FAN, settings),
            BlocksCover.copyLootTable(BUBBLE_CORAL_FAN, false)
                    .mapColor(MapColor.PURPLE)
                    .noCollision()
                    .strength(7.0f , 3.0f)
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block DEAD_FIRE_CORAL_WALL_FAN = BlocksCover.register(
            "dead_fire_coral_wall_fan",
            DeadCoralWallFanBlock::new,
            BlocksCover.copyLootTable(DEAD_FIRE_CORAL_FAN, false)
                    .mapColor(MapColor.GRAY)
                    .solid()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .noCollision()
                    .strength(7.0f , 3.0f)
    );
    public static final Block FIRE_CORAL_WALL_FAN = BlocksCover.register(
            "fire_coral_wall_fan",
            (AbstractBlock.Settings settings) -> new CoralWallFanBlock(DEAD_FIRE_CORAL_WALL_FAN, settings),
            BlocksCover.copyLootTable(FIRE_CORAL_FAN, false)
                    .mapColor(MapColor.RED)
                    .noCollision()
                    .strength(7.0f , 3.0f)
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block DEAD_HORN_CORAL_WALL_FAN = BlocksCover.register(
            "dead_horn_coral_wall_fan",
            DeadCoralWallFanBlock::new,
            BlocksCover.copyLootTable(DEAD_HORN_CORAL_FAN, false)
                    .mapColor(MapColor.GRAY)
                    .solid()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .noCollision()
                    .strength(7.0f , 3.0f)
    );
    public static final Block HORN_CORAL_WALL_FAN = BlocksCover.register(
            "horn_coral_wall_fan",
            (AbstractBlock.Settings settings) -> new CoralWallFanBlock(DEAD_HORN_CORAL_WALL_FAN, settings),
            BlocksCover.copyLootTable(HORN_CORAL_FAN, false)
                    .mapColor(MapColor.YELLOW)
                    .noCollision()
                    .strength(7.0f , 3.0f)
                    .sounds(BlockSoundGroup.WET_GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block SEA_PICKLE = BlocksCover.register(
            "sea_pickle",
            SeaPickleBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GREEN)
                    .luminance(state -> SeaPickleBlock.isDry(state) ? 0 : 3 + 3 * state.get(SeaPickleBlock.PICKLES))
                    .sounds(BlockSoundGroup.SLIME)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .strength(2.0f )
    );
    public static final Block BLUE_ICE = BlocksCover.register(
            "blue_ice",
            TranslucentBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_PURPLE)
                    .strength(5.0f , 3.0f)
                    .slipperiness(0.989f)
                    .sounds(BlockSoundGroup.GLASS)
    );
    public static final Block CONDUIT = BlocksCover.register(
            "conduit",
            ConduitBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIAMOND_BLUE)
                    .solid()
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(25.0f , 30.0f)
                    .luminance(state -> 15)
                    .nonOpaque()
    );
    public static final Block BAMBOO_SAPLING = BlocksCover.register(
            "bamboo_sapling",
            BambooShootBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .solid()
                    .ticksRandomly()
                    .noCollision()
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.BAMBOO_SAPLING)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block BAMBOO = BlocksCover.register(
            "bamboo",
            BambooBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .solid()
                    .ticksRandomly()
                    .strength(1.5f)
                    .sounds(BlockSoundGroup.BAMBOO)
                    .nonOpaque()
                    .dynamicBounds()
                    .offset(AbstractBlock.OffsetType.XZ)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .solidBlock(BlocksCover::never)
    );
    public static final Block POTTED_BAMBOO = BlocksCover.register(
            "potted_bamboo",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(BAMBOO, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block VOID_AIR = BlocksCover.register(
            "void_air",
            AirBlock::new,
            AbstractBlock.Settings.create()
                    .replaceable()
                    .noCollision()
                    .dropsNothing()
                    .air()
    );
    public static final Block CAVE_AIR = BlocksCover.register(
            "cave_air",
            AirBlock::new,
            AbstractBlock.Settings.create()
                    .replaceable()
                    .noCollision()
                    .dropsNothing()
                    .air()
    );
    public static final Block BUBBLE_COLUMN = BlocksCover.register(
            "bubble_column",
            BubbleColumnBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WATER_BLUE)
                    .replaceable()
                    .noCollision()
                    .dropsNothing()
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .liquid()
                    .sounds(BlockSoundGroup.INTENTIONALLY_EMPTY)
    );
    public static final Block POLISHED_GRANITE_STAIRS = BlocksCover.registerStairsBlock("polished_granite_stairs", POLISHED_GRANITE);
    public static final Block SMOOTH_RED_SANDSTONE_STAIRS = BlocksCover.registerStairsBlock("smooth_red_sandstone_stairs", SMOOTH_RED_SANDSTONE);
    public static final Block MOSSY_STONE_BRICK_STAIRS = BlocksCover.registerStairsBlock("mossy_stone_brick_stairs", MOSSY_STONE_BRICKS);
    public static final Block POLISHED_DIORITE_STAIRS = BlocksCover.registerStairsBlock("polished_diorite_stairs", POLISHED_DIORITE);
    public static final Block MOSSY_COBBLESTONE_STAIRS = BlocksCover.registerStairsBlock("mossy_cobblestone_stairs", MOSSY_COBBLESTONE);
    public static final Block END_STONE_BRICK_STAIRS = BlocksCover.registerStairsBlock("end_stone_brick_stairs", END_STONE_BRICKS);
    public static final Block STONE_STAIRS = BlocksCover.registerStairsBlock("stone_stairs", STONE);
    public static final Block SMOOTH_SANDSTONE_STAIRS = BlocksCover.registerStairsBlock("smooth_sandstone_stairs", SMOOTH_SANDSTONE);
    public static final Block SMOOTH_QUARTZ_STAIRS = BlocksCover.registerStairsBlock("smooth_quartz_stairs", SMOOTH_QUARTZ);
    public static final Block GRANITE_STAIRS = BlocksCover.registerStairsBlock("granite_stairs", GRANITE);
    public static final Block ANDESITE_STAIRS = BlocksCover.registerStairsBlock("andesite_stairs", ANDESITE);
    public static final Block RED_NETHER_BRICK_STAIRS = BlocksCover.registerStairsBlock("red_nether_brick_stairs", RED_NETHER_BRICKS);
    public static final Block POLISHED_ANDESITE_STAIRS = BlocksCover.registerStairsBlock("polished_andesite_stairs", POLISHED_ANDESITE);
    public static final Block DIORITE_STAIRS = BlocksCover.registerStairsBlock("diorite_stairs", DIORITE);
    public static final Block POLISHED_GRANITE_SLAB = BlocksCover.register(
            "polished_granite_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(POLISHED_GRANITE)
    );
    public static final Block SMOOTH_RED_SANDSTONE_SLAB = BlocksCover.register(
            "smooth_red_sandstone_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(SMOOTH_RED_SANDSTONE)
    );
    public static final Block MOSSY_STONE_BRICK_SLAB = BlocksCover.register(
            "mossy_stone_brick_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(MOSSY_STONE_BRICKS)
    );
    public static final Block POLISHED_DIORITE_SLAB = BlocksCover.register(
            "polished_diorite_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(POLISHED_DIORITE)
    );
    public static final Block MOSSY_COBBLESTONE_SLAB = BlocksCover.register(
            "mossy_cobblestone_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(MOSSY_COBBLESTONE)
    );
    public static final Block END_STONE_BRICK_SLAB = BlocksCover.register(
            "end_stone_brick_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(END_STONE_BRICKS)
    );
    public static final Block SMOOTH_SANDSTONE_SLAB = BlocksCover.register(
            "smooth_sandstone_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(SMOOTH_SANDSTONE)
    );
    public static final Block SMOOTH_QUARTZ_SLAB = BlocksCover.register(
            "smooth_quartz_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(SMOOTH_QUARTZ)
    );
    public static final Block GRANITE_SLAB = BlocksCover.register(
            "granite_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(GRANITE)
    );
    public static final Block ANDESITE_SLAB = BlocksCover.register(
            "andesite_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(ANDESITE)
    );
    public static final Block RED_NETHER_BRICK_SLAB = BlocksCover.register(
            "red_nether_brick_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(RED_NETHER_BRICKS)
    );
    public static final Block POLISHED_ANDESITE_SLAB = BlocksCover.register(
            "polished_andesite_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(POLISHED_ANDESITE)
    );
    public static final Block DIORITE_SLAB = BlocksCover.register(
            "diorite_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(DIORITE)
    );
    public static final Block BRICK_WALL = BlocksCover.register(
            "brick_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(BRICKS).solid()
    );
    public static final Block PRISMARINE_WALL = BlocksCover.register(
            "prismarine_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(PRISMARINE).solid()
    );
    public static final Block RED_SANDSTONE_WALL = BlocksCover.register(
            "red_sandstone_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(RED_SANDSTONE).solid()
    );
    public static final Block MOSSY_STONE_BRICK_WALL = BlocksCover.register(
            "mossy_stone_brick_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(MOSSY_STONE_BRICKS).solid()
    );
    public static final Block GRANITE_WALL = BlocksCover.register(
            "granite_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(GRANITE).solid()
    );
    public static final Block STONE_BRICK_WALL = BlocksCover.register(
            "stone_brick_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(STONE_BRICKS).solid()
    );
    public static final Block MUD_BRICK_WALL = BlocksCover.register(
            "mud_brick_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(MUD_BRICKS).solid()
    );
    public static final Block NETHER_BRICK_WALL = BlocksCover.register(
            "nether_brick_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(NETHER_BRICKS).solid()
    );
    public static final Block ANDESITE_WALL = BlocksCover.register(
            "andesite_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(ANDESITE).solid()
    );
    public static final Block RED_NETHER_BRICK_WALL = BlocksCover.register(
            "red_nether_brick_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(RED_NETHER_BRICKS).solid()
    );
    public static final Block SANDSTONE_WALL = BlocksCover.register(
            "sandstone_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(SANDSTONE).solid()
    );
    public static final Block END_STONE_BRICK_WALL = BlocksCover.register(
            "end_stone_brick_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(END_STONE_BRICKS).solid()
    );
    public static final Block DIORITE_WALL = BlocksCover.register(
            "diorite_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(DIORITE).solid()
    );
    public static final Block SCAFFOLDING = BlocksCover.register(
            "scaffolding",
            ScaffoldingBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_YELLOW)
                    .noCollision()
                    .sounds(BlockSoundGroup.SCAFFOLDING)
                    .dynamicBounds()
                    .strength(1.0f)
                    .allowsSpawning(BlocksCover::never)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .solidBlock(BlocksCover::never)
    );
    public static final Block LOOM = BlocksCover.register(
            "loom",
            LoomBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.5f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block BARREL = BlocksCover.register(
            "barrel",
            BarrelBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.5f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block SMOKER = BlocksCover.register(
            "smoker",
            SmokerBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(3.5f)
                    .luminance(BlocksCover.createLightLevelFromLitBlockCovertate(13))
    );
    public static final Block BLAST_FURNACE = BlocksCover.register(
            "blast_furnace",
            BlastFurnaceBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(3.5f)
                    .luminance(BlocksCover.createLightLevelFromLitBlockCovertate(13))
    );
    public static final Block CARTOGRAPHY_TABLE = BlocksCover.register(
            "cartography_table",
            CartographyTableBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.5f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block FLETCHING_TABLE = BlocksCover.register(
            "fletching_table",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.5F)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block GRINDSTONE = BlocksCover.register(
            "grindstone",
            GrindstoneBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.IRON_GRAY)
                    .strength(2.0f, 6.0f)
                    .sounds(BlockSoundGroup.STONE)
                    .pistonBehavior(PistonBehavior.BLOCK)
    );
    public static final Block LECTERN = BlocksCover.register(
            "lectern",
            LecternBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.5f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block SMITHING_TABLE = BlocksCover.register(
            "smithing_table",
            SmithingTableBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.5f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block STONECUTTER = BlocksCover.register(
            "stonecutter",
            StonecutterBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(3.5f)
    );
    public static final Block BELL = BlocksCover.register(
            "bell",
            BellBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GOLD)
                    .solid()
                    .strength(5.0f)
                    .sounds(BlockSoundGroup.ANVIL)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block LANTERN = BlocksCover.register(
            "lantern",
            LanternBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.IRON_GRAY)
                    .solid()
                    .strength(3.5f)
                    .sounds(BlockSoundGroup.LANTERN)
                    .luminance(state -> 15)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block SOUL_LANTERN = BlocksCover.register(
            "soul_lantern",
            LanternBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.IRON_GRAY)
                    .solid()
                    .strength(3.5f)
                    .sounds(BlockSoundGroup.LANTERN)
                    .luminance(state -> 10)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block COPPER_LANTERNS = register(
            "copper_lantern",
            LanternBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.IRON_GRAY)
                    .solid()
                    .strength(3.5f)
                    .sounds(BlockSoundGroup.LANTERN)
                    .luminance(state -> 15)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block CAMPFIRE = BlocksCover.register(
            "campfire",
            (AbstractBlock.Settings settings) -> new CampfireBlock(true, 1, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(3.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .luminance(BlocksCover.createLightLevelFromLitBlockCovertate(15))
                    .nonOpaque()
                    .burnable()
    );
    public static final Block SOUL_CAMPFIRE = BlocksCover.register(
            "soul_campfire",
            (AbstractBlock.Settings settings) -> new CampfireBlock(false, 2, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.SPRUCE_BROWN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(3.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .luminance(BlocksCover.createLightLevelFromLitBlockCovertate(10))
                    .nonOpaque()
                    .burnable()
    );
    public static final Block SWEET_BERRY_BUSH = BlocksCover.register(
            "sweet_berry_bush",
            SingleBerryBushBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .ticksRandomly()
                    .noCollision()
                    .sounds(BlockSoundGroup.SWEET_BERRY_BUSH)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .strength(1.0f)
    );
    public static final Block WARPED_STEM = BlocksCover.register(
            "warped_stem",
            PillarBlock::new,
            BlocksCover.createNetherStemSettings(MapColor.DARK_AQUA)
    );
    public static final Block STRIPPED_WARPED_STEM = BlocksCover.register(
            "stripped_warped_stem",
            PillarBlock::new,
            BlocksCover.createNetherStemSettings(MapColor.DARK_AQUA)
    );
    public static final Block WARPED_HYPHAE = BlocksCover.register(
            "warped_hyphae",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_DULL_PINK)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(25.0F , 35.0F)
                    .sounds(BlockSoundGroup.NETHER_STEM)
    );
    public static final Block STRIPPED_WARPED_HYPHAE = BlocksCover.register(
            "stripped_warped_hyphae",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_DULL_PINK)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(25.0F , 35.0F)
                    .sounds(BlockSoundGroup.NETHER_STEM)
    );
    public static final Block WARPED_NYLIUM = BlocksCover.register(
            "warped_nylium",
            NyliumBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TEAL)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(27.0f , 15.0f)
                    .sounds(BlockSoundGroup.NYLIUM)
                    .ticksRandomly()
    );
    public static final Block WARPED_FUNGUS = BlocksCover.register(
            "warped_fungus",
            (AbstractBlock.Settings settings) -> new FungusBlock(TreeConfiguredFeatures.WARPED_FUNGUS_PLANTED, WARPED_NYLIUM, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.CYAN)
                    .strength(0.7f)
                    .noCollision()
                    .sounds(BlockSoundGroup.FUNGUS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block POTTED_WARPED_FUNGUS = BlocksCover.register(
            "potted_warped_fungus",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(WARPED_FUNGUS, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block WARPED_WART_BLOCK = BlocksCover.register(
            "warped_wart_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BRIGHT_TEAL)
                    .strength(5.0F)
                    .sounds(BlockSoundGroup.WART_BLOCK)
    );
    public static final Block WARPED_ROOTS = BlocksCover.register(
            "warped_roots",
            RootsBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.CYAN)
                    .replaceable()
                    .noCollision()
                    .strength(28.0f , 15.0f)
                    .sounds(BlockSoundGroup.ROOTS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block NETHER_SPROUTS = BlocksCover.register(
            "nether_sprouts",
            SproutsBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.CYAN)
                    .replaceable()
                    .noCollision()
                    .strength(2.0f)
                    .sounds(BlockSoundGroup.NETHER_SPROUTS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block CRIMSON_STEM = BlocksCover.register(
            "crimson_stem",
            PillarBlock::new,
            BlocksCover.createNetherStemSettings(MapColor.DULL_PINK)
    );
    public static final Block STRIPPED_CRIMSON_STEM = BlocksCover.register(
            "stripped_crimson_stem",
            PillarBlock::new,
            BlocksCover.createNetherStemSettings(MapColor.DULL_PINK)
    );
    public static final Block CRIMSON_HYPHAE = BlocksCover.register(
            "crimson_hyphae",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_CRIMSON)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(25.0F , 35.0F)
                    .sounds(BlockSoundGroup.NETHER_STEM)
    );
    public static final Block STRIPPED_CRIMSON_HYPHAE = BlocksCover.register(
            "stripped_crimson_hyphae",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_CRIMSON)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(25.0F , 35.0F)
                    .sounds(BlockSoundGroup.NETHER_STEM)
    );
    public static final Block CRIMSON_NYLIUM = BlocksCover.register(
            "crimson_nylium",
            NyliumBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DULL_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(27.0f , 15.0f)
                    .sounds(BlockSoundGroup.NYLIUM)
                    .ticksRandomly()
    );
    public static final Block CRIMSON_FUNGUS = BlocksCover.register(
            "crimson_fungus",
            (AbstractBlock.Settings settings) -> new FungusBlock(TreeConfiguredFeatures.CRIMSON_FUNGUS_PLANTED, CRIMSON_NYLIUM, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_RED)
                    .strength(2.0F)
                    .noCollision()
                    .sounds(BlockSoundGroup.FUNGUS)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block POTTED_CRIMSON_FUNGUS = BlocksCover.register(
            "potted_crimson_fungus",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(CRIMSON_FUNGUS, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block SHROOMLIGHT = BlocksCover.register(
            "shroomlight",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.RED)
                    .strength(4.0f)
                    .sounds(BlockSoundGroup.SHROOMLIGHT)
                    .luminance(state -> 15)
    );
    public static final Block WEEPING_VINES = BlocksCover.register(
            "weeping_vines",
            WeepingVinesBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_RED)
                    .ticksRandomly()
                    .noCollision()
                    .strength(1.2f)
                    .sounds(BlockSoundGroup.WEEPING_VINES)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block WEEPING_VINES_PLANT = BlocksCover.register(
            "weeping_vines_plant",
            WeepingVinesPlantBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_RED)
                    .noCollision()
                    .strength(0.7f)
                    .sounds(BlockSoundGroup.WEEPING_VINES)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block TWISTING_VINES = BlocksCover.register(
            "twisting_vines",
            TwistingVinesBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.CYAN)
                    .ticksRandomly()
                    .noCollision()
                    .strength(1.2f)
                    .sounds(BlockSoundGroup.WEEPING_VINES)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block TWISTING_VINES_PLANT = BlocksCover.register(
            "twisting_vines_plant",
            TwistingVinesPlantBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.CYAN)
                    .noCollision()
                    .strength(1.2f)
                    .sounds(BlockSoundGroup.WEEPING_VINES)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block CRIMSON_ROOTS = BlocksCover.register(
            "crimson_roots",
            RootsBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_RED)
                    .replaceable()
                    .noCollision()
                    .strength(27.0f , 35.0f)
                    .sounds(BlockSoundGroup.ROOTS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block CRIMSON_PLANKS = BlocksCover.register(
            "crimson_planks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DULL_PINK)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(12.0f, 7.0f)
                    .sounds(BlockSoundGroup.NETHER_WOOD)
    );
    public static final Block WARPED_PLANKS = BlocksCover.register(
            "warped_planks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_AQUA)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(12.0f, 7.0f)
                    .sounds(BlockSoundGroup.NETHER_WOOD)
    );
    public static final Block CRIMSON_SLAB = BlocksCover.register(
            "crimson_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(CRIMSON_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(12.0f, 7.0f)
                    .sounds(BlockSoundGroup.NETHER_WOOD)
    );
    public static final Block WARPED_SLAB = BlocksCover.register(
            "warped_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(WARPED_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(12.0f, 7.0f)
                    .sounds(BlockSoundGroup.NETHER_WOOD)
    );
    public static final Block CRIMSON_PRESSURE_PLATE = BlocksCover.register(
            "crimson_pressure_plate",
            (AbstractBlock.Settings settings) -> new PressurePlateBlock(BlockSetType.CRIMSON, settings),
            AbstractBlock.Settings.create()
                    .mapColor(CRIMSON_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(0.5f)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block WARPED_PRESSURE_PLATE = BlocksCover.register(
            "warped_pressure_plate",
            (AbstractBlock.Settings settings) -> new PressurePlateBlock(BlockSetType.WARPED, settings),
            AbstractBlock.Settings.create()
                    .mapColor(WARPED_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .noCollision()
                    .strength(0.5f)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block CRIMSON_FENCE = BlocksCover.register(
            "crimson_fence",
            FenceBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(CRIMSON_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(15.0f, 7.0f)
                    .sounds(BlockSoundGroup.NETHER_WOOD)
    );
    public static final Block WARPED_FENCE = BlocksCover.register(
            "warped_fence",
            FenceBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(WARPED_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(15.0f, 7.0f)
                    .sounds(BlockSoundGroup.NETHER_WOOD)
    );
    public static final Block CRIMSON_TRAPDOOR = BlocksCover.register(
            "crimson_trapdoor",
            (AbstractBlock.Settings settings) -> new TrapdoorBlock(BlockSetType.CRIMSON, settings),
            AbstractBlock.Settings.create()
                    .mapColor(CRIMSON_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(10.0f, 7.0f)
                    .nonOpaque()
                    .allowsSpawning(BlocksCover::never)
    );
    public static final Block WARPED_TRAPDOOR = BlocksCover.register(
            "warped_trapdoor",
            (AbstractBlock.Settings settings) -> new TrapdoorBlock(BlockSetType.WARPED, settings),
            AbstractBlock.Settings.create()
                    .mapColor(WARPED_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(10.0f, 7.0f)
                    .nonOpaque()
                    .allowsSpawning(BlocksCover::never)
    );
    public static final Block CRIMSON_FENCE_GATE = BlocksCover.register(
            "crimson_fence_gate",
            (AbstractBlock.Settings settings) -> new FenceGateBlock(WoodType.CRIMSON, settings),
            AbstractBlock.Settings.create()
                    .mapColor(CRIMSON_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(12.0f, 7.0f)
    );
    public static final Block WARPED_FENCE_GATE = BlocksCover.register(
            "warped_fence_gate",
            (AbstractBlock.Settings settings) -> new FenceGateBlock(WoodType.WARPED, settings),
            AbstractBlock.Settings.create()
                    .mapColor(WARPED_PLANKS.getDefaultMapColor())
                    .solid()
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(12.0f, 7.0f)
    );
    public static final Block CRIMSON_STAIRS = BlocksCover.registerStairsBlock("crimson_stairs", CRIMSON_PLANKS);
    public static final Block WARPED_STAIRS = BlocksCover.registerStairsBlock("warped_stairs", WARPED_PLANKS);
    public static final Block CRIMSON_BUTTON = BlocksCover.register(
            "crimson_button",
            (AbstractBlock.Settings settings) -> new ButtonBlock(BlockSetType.CRIMSON, 30, settings),
            BlocksCover.createButtonSettings()
    );
    public static final Block WARPED_BUTTON = BlocksCover.register(
            "warped_button",
            (AbstractBlock.Settings settings) -> new ButtonBlock(BlockSetType.WARPED, 30, settings),
            BlocksCover.createButtonSettings()
    );
    public static final Block CRIMSON_DOOR = BlocksCover.register(
            "crimson_door",
            (AbstractBlock.Settings settings) -> new DoorBlock(BlockSetType.CRIMSON, settings),
            AbstractBlock.Settings.create()
                    .mapColor(CRIMSON_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(15.0f, 7.0f)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block WARPED_DOOR = BlocksCover.register(
            "warped_door",
            (AbstractBlock.Settings settings) -> new DoorBlock(BlockSetType.WARPED, settings),
            AbstractBlock.Settings.create()
                    .mapColor(WARPED_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(15.0f, 7.0f)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block CRIMSON_SIGN = BlocksCover.register(
            "crimson_sign",
            (AbstractBlock.Settings settings) -> new SignBlock(WoodType.CRIMSON, settings),
            AbstractBlock.Settings.create()
                    .mapColor(CRIMSON_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .solid()
                    .noCollision()
                    .strength(1.0f)
    );
    public static final Block CRIMSON_WALL_SIGN = BlocksCover.register(
            "crimson_wall_sign",
            (AbstractBlock.Settings settings) -> new WallSignBlock(WoodType.CRIMSON, settings),
            BlocksCover.copyLootTable(CRIMSON_SIGN, true).mapColor(CRIMSON_PLANKS.getDefaultMapColor()).instrument(NoteBlockInstrument.BASS).solid().noCollision().strength(1.0f)
    );
    public static final Block WARPED_SIGN = BlocksCover.register(
            "warped_sign",
            (AbstractBlock.Settings settings) -> new SignBlock(WoodType.WARPED, settings),
            AbstractBlock.Settings.create()
                    .mapColor(WARPED_PLANKS.getDefaultMapColor())
                    .instrument(NoteBlockInstrument.BASS)
                    .solid()
                    .noCollision()
                    .strength(1.0f)
    );
    public static final Block WARPED_WALL_SIGN = BlocksCover.register(
            "warped_wall_sign",
            (AbstractBlock.Settings settings) -> new WallSignBlock(WoodType.WARPED, settings),
            BlocksCover.copyLootTable(WARPED_SIGN, true).mapColor(WARPED_PLANKS.getDefaultMapColor()).instrument(NoteBlockInstrument.BASS).solid().noCollision().strength(1.0f)
    );
    public static final Block STRUCTURE_BLOCK = BlocksCover.register(
            "structure_block",
            StructureBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIGHT_GRAY)
                    .requiresTool()
                    .strength(-1.0f, 3600000.0f)
                    .dropsNothing()
    );
    public static final Block JIGSAW = BlocksCover.register(
            "jigsaw",
            JigsawBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIGHT_GRAY)
                    .requiresTool()
                    .strength(-1.0f, 3600000.0f)
                    .dropsNothing()
    );
    public static final Block TEST_BLOCK = BlocksCover.register(
            "test_block",
            TestBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIGHT_GRAY)
                    .strength(-1.0f, 3600000.0f)
                    .dropsNothing()
    );
    public static final Block TEST_INSTANCE_BLOCK = BlocksCover.register(
            "test_instance_block",
            TestInstanceBlock::new,
            AbstractBlock.Settings.create()
                    .nonOpaque()
                    .strength(-1.0f, 3600000.0f)
                    .dropsNothing()
                    .blockVision(BlocksCover::never)
    );
    public static final Block COMPOSTER = BlocksCover.register(
            "composter",
            ComposterBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block TARGET = BlocksCover.register(
            "target",
            TargetBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OFF_WHITE)
                    .strength(1.2f)
                    .sounds(BlockSoundGroup.GRASS)
    );
    public static final Block BEE_NEST = BlocksCover.register(
            "bee_nest",
            BeehiveBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.YELLOW)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(4.5f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block BEEHIVE = BlocksCover.register(
            "beehive",
            BeehiveBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.OAK_TAN)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0f)
                    .sounds(BlockSoundGroup.WOOD)
                    .burnable()
    );
    public static final Block HONEY_BLOCK = BlocksCover.register(
            "honey_block",
            HoneyBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .velocityMultiplier(0.4f)
                    .jumpVelocityMultiplier(0.5f)
                    .nonOpaque()
                    .strength(2.0f)
                    .sounds(BlockSoundGroup.HONEY)
    );
    public static final Block HONEYCOMB_BLOCK = BlocksCover.register(
            "honeycomb_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .strength(3F)
                    .sounds(BlockSoundGroup.CORAL)
    );
    public static final Block NETHERITE_BLOCK = BlocksCover.register(
            "netherite_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLACK)
                    .requiresTool()
                    .strength(150.0F, 1200.0F)
                    .sounds(BlockSoundGroup.NETHERITE)
    );
    public static final Block ANCIENT_DEBRIS = BlocksCover.register(
            "ancient_debris",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLACK)
                    .requiresTool()
                    .strength(115.0F, 1200.0F)
                    .sounds(BlockSoundGroup.ANCIENT_DEBRIS)
    );
    public static final Block CRYING_OBSIDIAN = BlocksCover.register(
            "crying_obsidian",
            CryingObsidianBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(50.0f, 1200.0f)
                    .luminance(state -> 10)
    );
    public static final Block RESPAWN_ANCHOR = BlocksCover.register(
            "respawn_anchor",
            RespawnAnchorBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(50.0f, 1200.0f)
                    .luminance(state -> RespawnAnchorBlock.getLightLevel(state, 15))
    );
    public static final Block POTTED_CRIMSON_ROOTS = BlocksCover.register(
            "potted_crimson_roots",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(CRIMSON_ROOTS, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_WARPED_ROOTS = BlocksCover.register(
            "potted_warped_roots",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(WARPED_ROOTS, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block LODESTONE = BlocksCover.register(
            "lodestone",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.IRON_GRAY)
                    .requiresTool()
                    .strength(35F, 30F)
                    .sounds(BlockSoundGroup.LODESTONE)
                    .pistonBehavior(PistonBehavior.BLOCK)
    );
    public static final Block BLACKSTONE = BlocksCover.register(
            "blackstone",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLACK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(35F, 30F)
    );
    public static final Block BLACKSTONE_STAIRS = BlocksCover.registerStairsBlock("blackstone_stairs", BLACKSTONE);
    public static final Block BLACKSTONE_WALL = BlocksCover.register(
            "blackstone_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(BLACKSTONE).solid()
    );
    public static final Block BLACKSTONE_SLAB = BlocksCover.register(
            "blackstone_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(BLACKSTONE)
    );
    public static final Block POLISHED_BLACKSTONE = BlocksCover.register(
            "polished_blackstone",
            AbstractBlock.Settings.copy(BLACKSTONE)
    );
    public static final Block POLISHED_BLACKSTONE_BRICKS = BlocksCover.register(
            "polished_blackstone_bricks",
            AbstractBlock.Settings.copy(POLISHED_BLACKSTONE)
    );
    public static final Block CRACKED_POLISHED_BLACKSTONE_BRICKS = BlocksCover.register(
            "cracked_polished_blackstone_bricks",
            AbstractBlock.Settings.copy(POLISHED_BLACKSTONE_BRICKS)
    );
    public static final Block POLISHED_BLACKSTONE_BRICK_SLAB = BlocksCover.register(
            "polished_blackstone_brick_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(POLISHED_BLACKSTONE_BRICKS)
    );
    public static final Block POLISHED_BLACKSTONE_BRICK_STAIRS = BlocksCover.registerStairsBlock("polished_blackstone_brick_stairs", POLISHED_BLACKSTONE_BRICKS);
    public static final Block POLISHED_BLACKSTONE_BRICK_WALL = BlocksCover.register(
            "polished_blackstone_brick_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(POLISHED_BLACKSTONE_BRICKS).solid()
    );
    public static final Block CHISELED_POLISHED_BLACKSTONE = BlocksCover.register(
            "chiseled_polished_blackstone",
            AbstractBlock.Settings.copy(POLISHED_BLACKSTONE)
    );
    public static final Block POLISHED_BLACKSTONE_STAIRS = BlocksCover.registerStairsBlock("polished_blackstone_stairs", POLISHED_BLACKSTONE);
    public static final Block POLISHED_BLACKSTONE_SLAB = BlocksCover.register(
            "polished_blackstone_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(POLISHED_BLACKSTONE)
    );
    public static final Block POLISHED_BLACKSTONE_WALL = BlocksCover.register(
            "polished_blackstone_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(POLISHED_BLACKSTONE).solid()
    );
    public static final Block GILDED_BLACKSTONE = BlocksCover.register(
            "gilded_blackstone",
            AbstractBlock.Settings.copy(BLACKSTONE).sounds(BlockSoundGroup.GILDED_BLACKSTONE)
    );
    public static final Block POLISHED_BLACKSTONE_PRESSURE_PLATE = BlocksCover.register(
            "polished_blackstone_pressure_plate",
            (AbstractBlock.Settings settings) -> new PressurePlateBlock(BlockSetType.POLISHED_BLACKSTONE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLACK)
                    .solid()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .noCollision()
                    .strength(0.5f)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block POLISHED_BLACKSTONE_BUTTON = BlocksCover.register(
            "polished_blackstone_button",
            (AbstractBlock.Settings settings) -> new ButtonBlock(BlockSetType.STONE, 20, settings),
            BlocksCover.createButtonSettings()
    );
    public static final Block CHISELED_NETHER_BRICKS = BlocksCover.register(
            "chiseled_nether_bricks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(40F, 25F)
                    .sounds(BlockSoundGroup.NETHER_BRICKS)
    );
    public static final Block CRACKED_NETHER_BRICKS = BlocksCover.register(
            "cracked_nether_bricks",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(40F, 25F)
                    .sounds(BlockSoundGroup.NETHER_BRICKS)
    );
    public static final Block QUARTZ_BRICKS = BlocksCover.register(
            "quartz_bricks",
            AbstractBlock.Settings.copy(QUARTZ_BLOCK)
    );
    public static final Block CANDLE = BlocksCover.register(
            "candle",
            CandleBlock::new,
            BlocksCover.createCandleSettings(MapColor.PALE_YELLOW)
    );
    public static final Block WHITE_CANDLE = BlocksCover.register(
            "white_candle",
            CandleBlock::new,
            BlocksCover.createCandleSettings(MapColor.WHITE_GRAY)
    );
    public static final Block ORANGE_CANDLE = BlocksCover.register(
            "orange_candle",
            CandleBlock::new,
            BlocksCover.createCandleSettings(MapColor.ORANGE)
    );
    public static final Block MAGENTA_CANDLE = BlocksCover.register(
            "magenta_candle",
            CandleBlock::new,
            BlocksCover.createCandleSettings(MapColor.MAGENTA)
    );
    public static final Block LIGHT_BLUE_CANDLE = BlocksCover.register(
            "light_blue_candle",
            CandleBlock::new,
            BlocksCover.createCandleSettings(MapColor.LIGHT_BLUE)
    );
    public static final Block YELLOW_CANDLE = BlocksCover.register(
            "yellow_candle",
            CandleBlock::new,
            BlocksCover.createCandleSettings(MapColor.YELLOW)
    );
    public static final Block LIME_CANDLE = BlocksCover.register(
            "lime_candle",
            CandleBlock::new,
            BlocksCover.createCandleSettings(MapColor.LIME)
    );
    public static final Block PINK_CANDLE = BlocksCover.register(
            "pink_candle",
            CandleBlock::new,
            BlocksCover.createCandleSettings(MapColor.PINK)
    );
    public static final Block GRAY_CANDLE = BlocksCover.register(
            "gray_candle",
            CandleBlock::new,
            BlocksCover.createCandleSettings(MapColor.GRAY)
    );
    public static final Block LIGHT_GRAY_CANDLE = BlocksCover.register(
            "light_gray_candle",
            CandleBlock::new,
            BlocksCover.createCandleSettings(MapColor.LIGHT_GRAY)
    );
    public static final Block CYAN_CANDLE = BlocksCover.register(
            "cyan_candle",
            CandleBlock::new,
            BlocksCover.createCandleSettings(MapColor.CYAN)
    );
    public static final Block PURPLE_CANDLE = BlocksCover.register(
            "purple_candle",
            CandleBlock::new,
            BlocksCover.createCandleSettings(MapColor.PURPLE)
    );
    public static final Block BLUE_CANDLE = BlocksCover.register(
            "blue_candle",
            CandleBlock::new,
            BlocksCover.createCandleSettings(MapColor.BLUE)
    );
    public static final Block BROWN_CANDLE = BlocksCover.register(
            "brown_candle",
            CandleBlock::new,
            BlocksCover.createCandleSettings(MapColor.BROWN)
    );
    public static final Block GREEN_CANDLE = BlocksCover.register(
            "green_candle",
            CandleBlock::new,
            BlocksCover.createCandleSettings(MapColor.GREEN)
    );
    public static final Block RED_CANDLE = BlocksCover.register(
            "red_candle",
            CandleBlock::new,
            BlocksCover.createCandleSettings(MapColor.RED)
    );
    public static final Block BLACK_CANDLE = BlocksCover.register(
            "black_candle",
            CandleBlock::new,
            BlocksCover.createCandleSettings(MapColor.BLACK)
    );
    public static final Block CANDLE_CAKE = BlocksCover.register(
            "candle_cake",
            (AbstractBlock.Settings settings) -> new CandleCakeBlock(CANDLE, settings),
            AbstractBlock.Settings.copy(CAKE).luminance(BlocksCover.createLightLevelFromLitBlockCovertate(3))
    );
    public static final Block WHITE_CANDLE_CAKE = BlocksCover.register(
            "white_candle_cake",
            (AbstractBlock.Settings settings) -> new CandleCakeBlock(WHITE_CANDLE, settings),
            AbstractBlock.Settings.copy(CANDLE_CAKE)
    );
    public static final Block ORANGE_CANDLE_CAKE = BlocksCover.register(
            "orange_candle_cake",
            (AbstractBlock.Settings settings) -> new CandleCakeBlock(ORANGE_CANDLE, settings),
            AbstractBlock.Settings.copy(CANDLE_CAKE)
    );
    public static final Block MAGENTA_CANDLE_CAKE = BlocksCover.register(
            "magenta_candle_cake",
            (AbstractBlock.Settings settings) -> new CandleCakeBlock(MAGENTA_CANDLE, settings),
            AbstractBlock.Settings.copy(CANDLE_CAKE)
    );
    public static final Block LIGHT_BLUE_CANDLE_CAKE = BlocksCover.register(
            "light_blue_candle_cake",
            (AbstractBlock.Settings settings) -> new CandleCakeBlock(LIGHT_BLUE_CANDLE, settings),
            AbstractBlock.Settings.copy(CANDLE_CAKE)
    );
    public static final Block YELLOW_CANDLE_CAKE = BlocksCover.register(
            "yellow_candle_cake",
            (AbstractBlock.Settings settings) -> new CandleCakeBlock(YELLOW_CANDLE, settings),
            AbstractBlock.Settings.copy(CANDLE_CAKE)
    );
    public static final Block LIME_CANDLE_CAKE = BlocksCover.register(
            "lime_candle_cake",
            (AbstractBlock.Settings settings) -> new CandleCakeBlock(LIME_CANDLE, settings),
            AbstractBlock.Settings.copy(CANDLE_CAKE)
    );
    public static final Block PINK_CANDLE_CAKE = BlocksCover.register(
            "pink_candle_cake",
            (AbstractBlock.Settings settings) -> new CandleCakeBlock(PINK_CANDLE, settings),
            AbstractBlock.Settings.copy(CANDLE_CAKE)
    );
    public static final Block GRAY_CANDLE_CAKE = BlocksCover.register(
            "gray_candle_cake",
            (AbstractBlock.Settings settings) -> new CandleCakeBlock(GRAY_CANDLE, settings),
            AbstractBlock.Settings.copy(CANDLE_CAKE)
    );
    public static final Block LIGHT_GRAY_CANDLE_CAKE = BlocksCover.register(
            "light_gray_candle_cake",
            (AbstractBlock.Settings settings) -> new CandleCakeBlock(LIGHT_GRAY_CANDLE, settings),
            AbstractBlock.Settings.copy(CANDLE_CAKE)
    );
    public static final Block CYAN_CANDLE_CAKE = BlocksCover.register(
            "cyan_candle_cake",
            (AbstractBlock.Settings settings) -> new CandleCakeBlock(CYAN_CANDLE, settings),
            AbstractBlock.Settings.copy(CANDLE_CAKE)
    );
    public static final Block PURPLE_CANDLE_CAKE = BlocksCover.register(
            "purple_candle_cake",
            (AbstractBlock.Settings settings) -> new CandleCakeBlock(PURPLE_CANDLE, settings),
            AbstractBlock.Settings.copy(CANDLE_CAKE)
    );
    public static final Block BLUE_CANDLE_CAKE = BlocksCover.register(
            "blue_candle_cake",
            (AbstractBlock.Settings settings) -> new CandleCakeBlock(BLUE_CANDLE, settings),
            AbstractBlock.Settings.copy(CANDLE_CAKE)
    );
    public static final Block BROWN_CANDLE_CAKE = BlocksCover.register(
            "brown_candle_cake",
            (AbstractBlock.Settings settings) -> new CandleCakeBlock(BROWN_CANDLE, settings),
            AbstractBlock.Settings.copy(CANDLE_CAKE)
    );
    public static final Block GREEN_CANDLE_CAKE = BlocksCover.register(
            "green_candle_cake",
            (AbstractBlock.Settings settings) -> new CandleCakeBlock(GREEN_CANDLE, settings),
            AbstractBlock.Settings.copy(CANDLE_CAKE)
    );
    public static final Block RED_CANDLE_CAKE = BlocksCover.register(
            "red_candle_cake",
            (AbstractBlock.Settings settings) -> new CandleCakeBlock(RED_CANDLE, settings),
            AbstractBlock.Settings.copy(CANDLE_CAKE)
    );
    public static final Block BLACK_CANDLE_CAKE = BlocksCover.register(
            "black_candle_cake",
            (AbstractBlock.Settings settings) -> new CandleCakeBlock(BLACK_CANDLE, settings),
            AbstractBlock.Settings.copy(CANDLE_CAKE)
    );
    public static final Block AMETHYST_BLOCK = BlocksCover.register(
            "amethyst_block",
            AmethystBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PURPLE)
                    .strength(45.0f , 35.0f)
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)
                    .requiresTool()
    );
    public static final Block BUDDING_AMETHYST = BlocksCover.register(
            "budding_amethyst",
            BuddingAmethystBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PURPLE)
                    .ticksRandomly()
                    .strength(17.0f , 5.0f)
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)
                    .requiresTool()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block AMETHYST_CLUSTER = BlocksCover.register(
            "amethyst_cluster",
            (AbstractBlock.Settings settings) -> new AmethystClusterBlock(7.0f, 10.0f, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PURPLE)
                    .solid()
                    .nonOpaque()
                    .sounds(BlockSoundGroup.AMETHYST_CLUSTER)
                    .strength(45.0f , 35.0f)
                    .luminance(state -> 5)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block LARGE_AMETHYST_BUD = BlocksCover.register(
            "large_amethyst_bud",
            (AbstractBlock.Settings settings) -> new AmethystClusterBlock(5.0f, 10.0f, settings),
            AbstractBlock.Settings.copy(AMETHYST_CLUSTER)
                    .sounds(BlockSoundGroup.MEDIUM_AMETHYST_BUD)
                    .luminance(state -> 4)
    );
    public static final Block MEDIUM_AMETHYST_BUD = BlocksCover.register(
            "medium_amethyst_bud",
            (AbstractBlock.Settings settings) -> new AmethystClusterBlock(4.0f, 10.0f, settings),
            AbstractBlock.Settings.copy(AMETHYST_CLUSTER)
                    .sounds(BlockSoundGroup.LARGE_AMETHYST_BUD)
                    .luminance(state -> 2)
    );
    public static final Block SMALL_AMETHYST_BUD = BlocksCover.register(
            "small_amethyst_bud",
            (AbstractBlock.Settings settings) -> new AmethystClusterBlock(3.0f, 8.0f, settings),
            AbstractBlock.Settings.copy(AMETHYST_CLUSTER)
                    .sounds(BlockSoundGroup.SMALL_AMETHYST_BUD)
                    .luminance(state -> 1)
    );
    public static final Block TUFF = BlocksCover.register(
            "tuff",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .sounds(BlockSoundGroup.TUFF)
                    .requiresTool()
                    .strength(15F, 6.0F)
    );
    public static final Block TUFF_SLAB = BlocksCover.register(
            "tuff_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(TUFF)
    );
    public static final Block TUFF_STAIRS = BlocksCover.register(
            "tuff_stairs",
            (AbstractBlock.Settings settings) -> new StairsBlock(TUFF.getDefaultState(), settings),
            AbstractBlock.Settings.copy(TUFF)
    );
    public static final Block TUFF_WALL = BlocksCover.register(
            "tuff_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(TUFF).solid()
    );
    public static final Block POLISHED_TUFF = BlocksCover.register(
            "polished_tuff",
            AbstractBlock.Settings.copy(TUFF).sounds(BlockSoundGroup.POLISHED_TUFF)
    );
    public static final Block POLISHED_TUFF_SLAB = BlocksCover.register(
            "polished_tuff_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(POLISHED_TUFF)
    );
    public static final Block POLISHED_TUFF_STAIRS = BlocksCover.register(
            "polished_tuff_stairs",
            (AbstractBlock.Settings settings) -> new StairsBlock(POLISHED_TUFF.getDefaultState(), settings),
            AbstractBlock.Settings.copy(POLISHED_TUFF)
    );
    public static final Block POLISHED_TUFF_WALL = BlocksCover.register(
            "polished_tuff_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(POLISHED_TUFF).solid()
    );
    public static final Block CHISELED_TUFF = BlocksCover.register(
            "chiseled_tuff",
            AbstractBlock.Settings.copy(TUFF)
    );
    public static final Block TUFF_BRICKS = BlocksCover.register(
            "tuff_bricks",
            AbstractBlock.Settings.copy(TUFF).sounds(BlockSoundGroup.TUFF_BRICKS)
    );
    public static final Block TUFF_BRICK_SLAB = BlocksCover.register(
            "tuff_brick_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(TUFF_BRICKS)
    );
    public static final Block TUFF_BRICK_STAIRS = BlocksCover.register(
            "tuff_brick_stairs",
            (AbstractBlock.Settings settings) -> new StairsBlock(TUFF_BRICKS.getDefaultState(), settings),
            AbstractBlock.Settings.copy(TUFF_BRICKS)
    );
    public static final Block TUFF_BRICK_WALL = BlocksCover.register(
            "tuff_brick_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(TUFF_BRICKS).solid()
    );
    public static final Block CHISELED_TUFF_BRICKS = BlocksCover.register(
            "chiseled_tuff_bricks",
            AbstractBlock.Settings.copy(TUFF_BRICKS)
    );
    public static final Block CALCITE = BlocksCover.register(
            "calcite",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_WHITE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .sounds(BlockSoundGroup.CALCITE)
                    .requiresTool()
                    .strength(27.5F)
    );
    public static final Block TINTED_GLASS = BlocksCover.register(
            "tinted_glass",
            TintedGlassBlock::new,
            AbstractBlock.Settings.copy(GLASS)
                    .mapColor(MapColor.GRAY)
                    .nonOpaque()
                    .allowsSpawning(BlocksCover::never)
                    .solidBlock(BlocksCover::never)
                    .suffocates(BlocksCover::never)
                    .blockVision(BlocksCover::never)
    );
    public static final Block POWDER_SNOW = BlocksCover.register(
            "powder_snow",
            PowderSnowBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WHITE)
                    .strength(1.25f)
                    .sounds(BlockSoundGroup.POWDER_SNOW)
                    .dynamicBounds()
                    .nonOpaque()
                    .solidBlock(BlocksCover::never)
    );
    public static final Block SCULK_SENSOR = BlocksCover.register(
            "sculk_sensor",
            SculkSensorBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.CYAN)
                    .strength(15.0f , 1000.0f)
                    .sounds(BlockSoundGroup.SCULK_SENSOR)
                    .luminance(state -> 1)
                    .emissiveLighting((state, world, pos) -> SculkSensorBlock.getPhase(state) == SculkSensorPhase.ACTIVE)
    );
    public static final Block CALIBRATED_SCULK_SENSOR = BlocksCover.register(
            "calibrated_sculk_sensor",
            CalibratedSculkSensorBlock::new,
            AbstractBlock.Settings.copy(SCULK_SENSOR)
    );
    public static final Block SCULK = BlocksCover.register(
            "sculk",
            SculkBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLACK)
                    .strength(25.0f , 15.0f)
                    .sounds(BlockSoundGroup.SCULK)
    );
    public static final Block SCULK_VEIN = BlocksCover.register(
            "sculk_vein",
            SculkVeinBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLACK)
                    .solid()
                    .noCollision()
                    .strength(2.0f)
                    .sounds(BlockSoundGroup.SCULK_VEIN)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block SCULK_CATALYST = BlocksCover.register(
            "sculk_catalyst",
            SculkCatalystBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLACK)
                    .strength(23.0f, 15.0f)
                    .sounds(BlockSoundGroup.SCULK_CATALYST)
                    .luminance(state -> 6)
    );
    public static final Block SCULK_SHRIEKER = BlocksCover.register(
            "sculk_shrieker",
            SculkShriekerBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLACK)
                    .strength(33.0f, 33.0f)
                    .sounds(BlockSoundGroup.SCULK_SHRIEKER)
    );
    public static final Block COPPER_BLOCK = BlocksCover.register(
            "copper_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .requiresTool()
                    .strength(13.0f, 6.0f)
                    .sounds(BlockSoundGroup.COPPER)
    );
    public static final Block EXPOSED_COPPER = BlocksCover.register(
            "exposed_copper",
            AbstractBlock.Settings.copy(COPPER_BLOCK).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
    );
    public static final Block EXPOSED_CUT_COPPER = BlocksCover.register(
            "exposed_cut_copper",
            AbstractBlock.Settings.copy(EXPOSED_COPPER)
    );
    public static final Block EXPOSED_CUT_COPPER_STAIRS = BlocksCover.register(
            "exposed_cut_copper_stairs",
            settings -> new StairsBlock(EXPOSED_CUT_COPPER.getDefaultState(), settings),
            AbstractBlock.Settings.copy(EXPOSED_COPPER)
    );
    public static final Block EXPOSED_CUT_COPPER_SLAB = BlocksCover.register(
            "exposed_cut_copper_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(EXPOSED_CUT_COPPER)
    );
    public static final Block EXPOSED_CHISELED_COPPER = BlocksCover.register(
            "exposed_chiseled_copper",
            AbstractBlock.Settings.copy(EXPOSED_COPPER)
    );
    public static final Block WAXED_EXPOSED_CHISELED_COPPER = BlocksCover.register(
            "waxed_exposed_chiseled_copper",
            AbstractBlock.Settings.copy(EXPOSED_CHISELED_COPPER)
    );
    public static final Block WAXED_EXPOSED_COPPER = BlocksCover.register(
            "waxed_exposed_copper",
            AbstractBlock.Settings.copy(EXPOSED_COPPER)
    );
    public static final Block WAXED_EXPOSED_CUT_COPPER = BlocksCover.register(
            "waxed_exposed_cut_copper",
            AbstractBlock.Settings.copy(EXPOSED_COPPER)
    );
    public static final Block WAXED_EXPOSED_CUT_COPPER_STAIRS = BlocksCover.registerStairsBlock("waxed_exposed_cut_copper_stairs", WAXED_EXPOSED_CUT_COPPER);
    public static final Block WAXED_EXPOSED_CUT_COPPER_SLAB = BlocksCover.register(
            "waxed_exposed_cut_copper_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(WAXED_EXPOSED_CUT_COPPER).requiresTool()
    );
    public static final Block WEATHERED_COPPER = BlocksCover.register(
            "weathered_copper",
             AbstractBlock.Settings.copy(COPPER_BLOCK).mapColor(MapColor.DARK_AQUA)
    );
    public static final Block WEATHERED_CUT_COPPER = BlocksCover.register(
            "weathered_cut_copper",
               AbstractBlock.Settings.copy(WEATHERED_COPPER)
    );
    public static final Block WEATHERED_CUT_COPPER_STAIRS = BlocksCover.register(
            "weathered_cut_copper_stairs",
              settings -> new StairsBlock(WEATHERED_COPPER.getDefaultState(), settings),
              AbstractBlock.Settings.copy(WEATHERED_COPPER)
    );
    public static final Block WEATHERED_CUT_COPPER_SLAB = BlocksCover.register(
            "weathered_cut_copper_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(WEATHERED_CUT_COPPER)
    );
    public static final Block WEATHERED_CHISELED_COPPER = BlocksCover.register(
            "weathered_chiseled_copper",
               AbstractBlock.Settings.copy(WEATHERED_COPPER)
    );
    public static final Block WAXED_WEATHERED_CHISELED_COPPER = BlocksCover.register(
            "waxed_weathered_chiseled_copper",
            AbstractBlock.Settings.copy(WEATHERED_CHISELED_COPPER)
    );
    public static final Block WAXED_WEATHERED_COPPER = BlocksCover.register(
            "waxed_weathered_copper",
            AbstractBlock.Settings.copy(WEATHERED_COPPER)
    );
    public static final Block WAXED_WEATHERED_CUT_COPPER = BlocksCover.register(
            "waxed_weathered_cut_copper",
            AbstractBlock.Settings.copy(WEATHERED_COPPER)
    );
    public static final Block WAXED_WEATHERED_CUT_COPPER_STAIRS = BlocksCover.registerStairsBlock("waxed_weathered_cut_copper_stairs", WAXED_WEATHERED_CUT_COPPER);
    public static final Block WAXED_WEATHERED_CUT_COPPER_SLAB = BlocksCover.register(
            "waxed_weathered_cut_copper_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(WAXED_WEATHERED_CUT_COPPER).requiresTool()
    );
    public static final Block OXIDIZED_COPPER = BlocksCover.register(
            "oxidized_copper",
             AbstractBlock.Settings.copy(COPPER_BLOCK).mapColor(MapColor.TEAL)
    );
    public static final Block OXIDIZED_CUT_COPPER = BlocksCover.register(
            "oxidized_cut_copper",
               AbstractBlock.Settings.copy(OXIDIZED_COPPER)
    );
    public static final Block OXIDIZED_CUT_COPPER_STAIRS = BlocksCover.register(
            "oxidized_cut_copper_stairs",
                settings -> new StairsBlock(BlocksCover.OXIDIZED_COPPER.getDefaultState(), settings),
                AbstractBlock.Settings.copy(OXIDIZED_CUT_COPPER)
    );
    public static final Block OXIDIZED_CUT_COPPER_SLAB = BlocksCover.register(
            "oxidized_cut_copper_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(OXIDIZED_CUT_COPPER)
    );
    public static final Block OXIDIZED_CHISELED_COPPER = BlocksCover.register(
            "oxidized_chiseled_copper",
                  AbstractBlock.Settings.copy(OXIDIZED_COPPER)
    );
    public static final Block WAXED_OXIDIZED_CHISELED_COPPER = BlocksCover.register(
            "waxed_oxidized_chiseled_copper",
            AbstractBlock.Settings.copy(OXIDIZED_CHISELED_COPPER)
    );
    public static final Block WAXED_OXIDIZED_COPPER = BlocksCover.register(
            "waxed_oxidized_copper",
            AbstractBlock.Settings.copy(OXIDIZED_COPPER)
    );
    public static final Block WAXED_OXIDIZED_CUT_COPPER = BlocksCover.register(
            "waxed_oxidized_cut_copper",
            AbstractBlock.Settings.copy(OXIDIZED_COPPER)
    );
    public static final Block WAXED_OXIDIZED_CUT_COPPER_STAIRS = BlocksCover.registerStairsBlock("waxed_oxidized_cut_copper_stairs", WAXED_OXIDIZED_CUT_COPPER);
    public static final Block WAXED_OXIDIZED_CUT_COPPER_SLAB = BlocksCover.register(
            "waxed_oxidized_cut_copper_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(WAXED_OXIDIZED_CUT_COPPER).requiresTool()
    );
    public static final Block COPPER_ORE = BlocksCover.register(
            "copper_ore",
            (AbstractBlock.Settings settings) -> new ExperienceDroppingBlock(ConstantIntProvider.create(0), settings),
            AbstractBlock.Settings.copy(IRON_ORE)
    );
    public static final Block DEEPSLATE_COPPER_ORE = BlocksCover.register(
            "deepslate_copper_ore",
            (AbstractBlock.Settings settings) -> new ExperienceDroppingBlock(ConstantIntProvider.create(0), settings),
            AbstractBlock.Settings.copy(COPPER_ORE).mapColor(MapColor.DEEPSLATE_GRAY).strength(4.5f, 3.0f).sounds(BlockSoundGroup.DEEPSLATE)
    );
    public static final Block CUT_COPPER = BlocksCover.register(
            "cut_copper",
            AbstractBlock.Settings.copy(COPPER_BLOCK)
    );
    public static final Block CUT_COPPER_STAIRS = BlocksCover.register(
            "cut_copper_stairs",
            (AbstractBlock.Settings settings) -> new StairsBlock(BlocksCover.CUT_COPPER.getDefaultState(), settings),
            AbstractBlock.Settings.copy(COPPER_BLOCK)
    );
    public static final Block CUT_COPPER_SLAB = BlocksCover.register(
            "cut_copper_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(CUT_COPPER)
    );
    public static final Block CHISELED_COPPER = BlocksCover.register(
            "chiseled_copper",
               AbstractBlock.Settings.copy(COPPER_BLOCK)
    );
    public static final Block WAXED_CHISELED_COPPER = BlocksCover.register(
            "waxed_chiseled_copper",
            AbstractBlock.Settings.copy(CHISELED_COPPER)
    );
    public static final Block WAXED_COPPER_BLOCK = BlocksCover.register(
            "waxed_copper_block",
            AbstractBlock.Settings.copy(COPPER_BLOCK)
    );
    public static final Block WAXED_CUT_COPPER = BlocksCover.register(
            "waxed_cut_copper",
            AbstractBlock.Settings.copy(COPPER_BLOCK)
    );
    public static final Block WAXED_CUT_COPPER_STAIRS = BlocksCover.registerStairsBlock("waxed_cut_copper_stairs", WAXED_CUT_COPPER);
    public static final Block WAXED_CUT_COPPER_SLAB = BlocksCover.register(
            "waxed_cut_copper_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(WAXED_CUT_COPPER).requiresTool()
    );
    public static final Block COPPER_DOOR = BlocksCover.register(
            "copper_door",
            (AbstractBlock.Settings settings) -> new DoorBlock(BlockSetType.COPPER, settings),
            AbstractBlock.Settings.create()
                    .mapColor(COPPER_BLOCK.getDefaultMapColor())
                    .strength(3.0f, 6.0f)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block EXPOSED_COPPER_DOOR = BlocksCover.register(
            "exposed_copper_door",
            (AbstractBlock.Settings settings) -> new DoorBlock(BlockSetType.COPPER, settings),
            AbstractBlock.Settings.copy(COPPER_DOOR).mapColor(EXPOSED_COPPER.getDefaultMapColor())
    );
    public static final Block WAXED_EXPOSED_COPPER_DOOR = BlocksCover.register(
            "waxed_exposed_copper_door",
            (AbstractBlock.Settings settings) -> new DoorBlock(BlockSetType.COPPER, settings),
            AbstractBlock.Settings.copy(EXPOSED_COPPER_DOOR)
    );
    public static final Block OXIDIZED_COPPER_DOOR = BlocksCover.register(
            "oxidized_copper_door",
            (AbstractBlock.Settings settings) -> new DoorBlock(BlockSetType.COPPER, settings),
            AbstractBlock.Settings.copy(COPPER_DOOR).mapColor(OXIDIZED_COPPER.getDefaultMapColor())
    );
    public static final Block WAXED_OXIDIZED_COPPER_DOOR = BlocksCover.register(
            "waxed_oxidized_copper_door",
            (AbstractBlock.Settings settings) -> new DoorBlock(BlockSetType.COPPER, settings),
            AbstractBlock.Settings.copy(OXIDIZED_COPPER_DOOR)
    );
    public static final Block WEATHERED_COPPER_DOOR = BlocksCover.register(
            "weathered_copper_door",
            (AbstractBlock.Settings settings) -> new DoorBlock(BlockSetType.COPPER, settings),
            AbstractBlock.Settings.copy(COPPER_DOOR).mapColor(WEATHERED_COPPER.getDefaultMapColor())
    );
    public static final Block WAXED_WEATHERED_COPPER_DOOR = BlocksCover.register(
            "waxed_weathered_copper_door",
            (AbstractBlock.Settings settings) -> new DoorBlock(BlockSetType.COPPER, settings),
            AbstractBlock.Settings.copy(WEATHERED_COPPER_DOOR)
    );
    public static final Block WAXED_COPPER_DOOR = BlocksCover.register(
            "waxed_copper_door",
            (AbstractBlock.Settings settings) -> new DoorBlock(BlockSetType.COPPER, settings),
            AbstractBlock.Settings.copy(COPPER_DOOR)
    );
    public static final Block COPPER_TRAPDOOR = BlocksCover.register(
            "copper_trapdoor",
            (AbstractBlock.Settings settings) -> new TrapdoorBlock(BlockSetType.COPPER, settings),
            AbstractBlock.Settings.create()
                    .mapColor(COPPER_BLOCK.getDefaultMapColor())
                    .strength(3.0f, 6.0f)
                    .requiresTool()
                    .nonOpaque()
                    .allowsSpawning(BlocksCover::never)
    );
    public static final Block EXPOSED_COPPER_TRAPDOOR = BlocksCover.register(
            "exposed_copper_trapdoor",
            (AbstractBlock.Settings settings) -> new TrapdoorBlock(BlockSetType.COPPER,  settings),
            AbstractBlock.Settings.copy(COPPER_TRAPDOOR).mapColor(EXPOSED_COPPER.getDefaultMapColor())
    );
    public static final Block WAXED_EXPOSED_COPPER_TRAPDOOR = BlocksCover.register(
            "waxed_exposed_copper_trapdoor",
            (AbstractBlock.Settings settings) -> new TrapdoorBlock(BlockSetType.COPPER, settings),
            AbstractBlock.Settings.copy(EXPOSED_COPPER_TRAPDOOR)
    );
    public static final Block OXIDIZED_COPPER_TRAPDOOR = BlocksCover.register(
            "oxidized_copper_trapdoor",
            (AbstractBlock.Settings settings) -> new TrapdoorBlock(BlockSetType.COPPER, settings),
            AbstractBlock.Settings.copy(COPPER_TRAPDOOR).mapColor(OXIDIZED_COPPER.getDefaultMapColor())
    );
    public static final Block WAXED_OXIDIZED_COPPER_TRAPDOOR = BlocksCover.register(
            "waxed_oxidized_copper_trapdoor",
            (AbstractBlock.Settings settings) -> new TrapdoorBlock(BlockSetType.COPPER, settings),
            AbstractBlock.Settings.copy(OXIDIZED_COPPER_TRAPDOOR)
    );
    public static final Block WEATHERED_COPPER_TRAPDOOR = BlocksCover.register(
            "weathered_copper_trapdoor",
            (AbstractBlock.Settings settings) -> new TrapdoorBlock(BlockSetType.COPPER, settings),
            AbstractBlock.Settings.copy(COPPER_TRAPDOOR).mapColor(WEATHERED_COPPER.getDefaultMapColor())
    );
    public static final Block WAXED_WEATHERED_COPPER_TRAPDOOR = BlocksCover.register(
            "waxed_weathered_copper_trapdoor",
            (AbstractBlock.Settings settings) -> new TrapdoorBlock(BlockSetType.COPPER, settings),
            AbstractBlock.Settings.copy(WEATHERED_COPPER_TRAPDOOR)
    );
    public static final Block WAXED_COPPER_TRAPDOOR = BlocksCover.register(
            "waxed_copper_trapdoor",
            (AbstractBlock.Settings settings) -> new TrapdoorBlock(BlockSetType.COPPER, settings),
            AbstractBlock.Settings.copy(COPPER_TRAPDOOR)
    );
    public static final Block COPPER_GRATE = BlocksCover.register(
            "copper_grate",
            GrateBlock::new,
            AbstractBlock.Settings.create()
                    .strength(3.0f, 6.0f)
                    .sounds(BlockSoundGroup.COPPER_GRATE)
                    .mapColor(MapColor.ORANGE)
                    .nonOpaque()
                    .requiresTool()
                    .allowsSpawning(BlocksCover::never)
                    .solidBlock(BlocksCover::never)
                    .suffocates(BlocksCover::never)
                    .blockVision(BlocksCover::never)
    );
    public static final Block EXPOSED_COPPER_GRATE = BlocksCover.register(
            "exposed_copper_grate",
            GrateBlock::new,
            AbstractBlock.Settings.copy(COPPER_GRATE).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
    );
    public static final Block WAXED_EXPOSED_COPPER_GRATE = BlocksCover.register(
            "waxed_exposed_copper_grate",
            GrateBlock::new,
            AbstractBlock.Settings.copy(EXPOSED_COPPER_GRATE)
    );
    public static final Block WEATHERED_COPPER_GRATE = BlocksCover.register(
            "weathered_copper_grate",
            GrateBlock::new,
            AbstractBlock.Settings.copy(COPPER_GRATE).mapColor(MapColor.DARK_AQUA)
    );
    public static final Block WAXED_WEATHERED_COPPER_GRATE = BlocksCover.register(
            "waxed_weathered_copper_grate",
            GrateBlock::new,
            AbstractBlock.Settings.copy(WEATHERED_COPPER_GRATE)
    );
    public static final Block OXIDIZED_COPPER_GRATE = BlocksCover.register(
            "oxidized_copper_grate",
            GrateBlock::new,
            AbstractBlock.Settings.copy(COPPER_GRATE).mapColor(MapColor.TEAL)
    );
    public static final Block WAXED_OXIDIZED_COPPER_GRATE = BlocksCover.register(
            "waxed_oxidized_copper_grate",
            GrateBlock::new,
            AbstractBlock.Settings.copy(OXIDIZED_COPPER_GRATE)
    );
    public static final Block WAXED_COPPER_GRATE = BlocksCover.register(
            "waxed_copper_grate",
            GrateBlock::new,
            AbstractBlock.Settings.copy(COPPER_GRATE)
    );
    public static final Block COPPER_BULB = BlocksCover.register(
            "copper_bulb",
            BulbBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(COPPER_BLOCK.getDefaultMapColor())
                    .strength(3.0f, 6.0f)
                    .sounds(BlockSoundGroup.COPPER_BULB)
                    .requiresTool()
                    .solidBlock(BlocksCover::never)
                    .luminance(BlocksCover.createLightLevelFromLitBlockCovertate(15))
    );
    public static final Block EXPOSED_COPPER_BULB = BlocksCover.register(
            "exposed_copper_bulb",
            BulbBlock::new,
            AbstractBlock.Settings.copy(COPPER_BULB).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).luminance(BlocksCover.createLightLevelFromLitBlockCovertate(12))
    );
    public static final Block WAXED_EXPOSED_COPPER_BULB = BlocksCover.register(
            "waxed_exposed_copper_bulb",
            BulbBlock::new,
            AbstractBlock.Settings.copy(EXPOSED_COPPER_BULB)
    );
    public static final Block WEATHERED_COPPER_BULB = BlocksCover.register(
            "weathered_copper_bulb",
            BulbBlock::new,
            AbstractBlock.Settings.copy(COPPER_BULB).mapColor(MapColor.DARK_AQUA).luminance(BlocksCover.createLightLevelFromLitBlockCovertate(8))
    );
    public static final Block WAXED_WEATHERED_COPPER_BULB = BlocksCover.register(
            "waxed_weathered_copper_bulb",
            BulbBlock::new,
            AbstractBlock.Settings.copy(WEATHERED_COPPER_BULB)
    );
    public static final Block OXIDIZED_COPPER_BULB = BlocksCover.register(
            "oxidized_copper_bulb",
            BulbBlock::new,
            AbstractBlock.Settings.copy(COPPER_BULB).mapColor(MapColor.TEAL).luminance(BlocksCover.createLightLevelFromLitBlockCovertate(4))
    );
    public static final Block WAXED_OXIDIZED_COPPER_BULB = BlocksCover.register(
            "waxed_oxidized_copper_bulb",
            BulbBlock::new,
            AbstractBlock.Settings.copy(OXIDIZED_COPPER_BULB)
    );
    public static final Block WAXED_COPPER_BULB = BlocksCover.register(
            "waxed_copper_bulb",
            BulbBlock::new,
            AbstractBlock.Settings.copy(COPPER_BULB)
    );
    public static final Block COPPER_CHEST = BlocksCover.register(
            "copper_chest",
            (AbstractBlock.Settings settings) -> new ChestBlock(() -> BlockEntityType.CHEST, SoundEvents.BLOCK_COPPER_CHEST_OPEN, SoundEvents.BLOCK_COPPER_CHEST_CLOSE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(COPPER_BLOCK.getDefaultMapColor())
                    .strength(3.0f, 6.0f)
                    .sounds(BlockSoundGroup.COPPER)
                    .requiresTool()
    );
    public static final Block EXPOSED_COPPER_CHEST = BlocksCover.register(
            "exposed_copper_chest",
            (AbstractBlock.Settings settings) -> new ChestBlock(() -> BlockEntityType.CHEST, SoundEvents.BLOCK_COPPER_CHEST_OPEN, SoundEvents.BLOCK_COPPER_CHEST_CLOSE, settings),
            AbstractBlock.Settings.copy(COPPER_CHEST).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
    );
    public static final Block WAXED_EXPOSED_COPPER_CHEST = BlocksCover.register(
            "waxed_exposed_copper_chest",
            (AbstractBlock.Settings settings) -> new ChestBlock(() -> BlockEntityType.CHEST, SoundEvents.BLOCK_COPPER_CHEST_OPEN, SoundEvents.BLOCK_COPPER_CHEST_CLOSE, settings),
            AbstractBlock.Settings.copy(EXPOSED_COPPER_CHEST)
    );
    public static final Block WEATHERED_COPPER_CHEST = BlocksCover.register(
            "weathered_copper_chest",
            (AbstractBlock.Settings settings) -> new ChestBlock(() -> BlockEntityType.CHEST, SoundEvents.BLOCK_COPPER_CHEST_WEATHERED_OPEN, SoundEvents.BLOCK_COPPER_CHEST_WEATHERED_CLOSE, settings),
            AbstractBlock.Settings.copy(COPPER_CHEST).mapColor(MapColor.DARK_AQUA)
    );
    public static final Block WAXED_WEATHERED_COPPER_CHEST = BlocksCover.register(
            "waxed_weathered_copper_chest",
            (AbstractBlock.Settings settings) -> new ChestBlock(() -> BlockEntityType.CHEST, SoundEvents.BLOCK_COPPER_CHEST_WEATHERED_OPEN, SoundEvents.BLOCK_COPPER_CHEST_WEATHERED_CLOSE, settings),
            AbstractBlock.Settings.copy(WEATHERED_COPPER_CHEST)
    );
    public static final Block OXIDIZED_COPPER_CHEST = BlocksCover.register(
            "oxidized_copper_chest",
            (AbstractBlock.Settings settings) -> new ChestBlock(() -> BlockEntityType.CHEST, SoundEvents.BLOCK_COPPER_CHEST_OXIDIZED_OPEN, SoundEvents.BLOCK_COPPER_CHEST_OXIDIZED_CLOSE, settings),
            AbstractBlock.Settings.copy(COPPER_CHEST).mapColor(MapColor.TEAL)
    );
    public static final Block WAXED_OXIDIZED_COPPER_CHEST = BlocksCover.register(
            "waxed_oxidized_copper_chest",
            (AbstractBlock.Settings settings) -> new ChestBlock(() -> BlockEntityType.CHEST, SoundEvents.BLOCK_COPPER_CHEST_OXIDIZED_OPEN, SoundEvents.BLOCK_COPPER_CHEST_OXIDIZED_CLOSE, settings),
            AbstractBlock.Settings.copy(OXIDIZED_COPPER_CHEST)
    );
    public static final Block WAXED_COPPER_CHEST = BlocksCover.register(
            "waxed_copper_chest",
            (AbstractBlock.Settings settings) -> new ChestBlock(() -> BlockEntityType.CHEST, SoundEvents.BLOCK_COPPER_CHEST_OPEN, SoundEvents.BLOCK_COPPER_CHEST_CLOSE, settings),
            AbstractBlock.Settings.copy(COPPER_CHEST)
    );
    public static final Block COPPER_GOLEM_STATUE = BlocksCover.register(
            "copper_golem_statue",
            Block::new,
            AbstractBlock.Settings.create()
                    .mapColor(COPPER_BLOCK.getDefaultMapColor())
                    .strength(3.0f, 6.0f)
                    .sounds(BlockSoundGroup.COPPER_GOLEM_STATUE)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block EXPOSED_COPPER_GOLEM_STATUE = BlocksCover.register(
            "exposed_copper_golem_statue",
            Block::new,
            AbstractBlock.Settings.copy(COPPER_GOLEM_STATUE).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
    );
    public static final Block WAXED_EXPOSED_COPPER_GOLEM_STATUE = BlocksCover.register(
            "waxed_exposed_copper_golem_statue",
            Block::new,
            AbstractBlock.Settings.copy(EXPOSED_COPPER_GOLEM_STATUE)
    );
    public static final Block WEATHERED_COPPER_GOLEM_STATUE = BlocksCover.register(
            "weathered_copper_golem_statue",
            Block::new,
            AbstractBlock.Settings.copy(COPPER_GOLEM_STATUE).mapColor(MapColor.DARK_AQUA)
    );
    public static final Block WAXED_WEATHERED_COPPER_GOLEM_STATUE = BlocksCover.register(
            "waxed_weathered_copper_golem_statue",
            Block::new,
            AbstractBlock.Settings.copy(WEATHERED_COPPER_GOLEM_STATUE)
    );
    public static final Block OXIDIZED_COPPER_GOLEM_STATUE = BlocksCover.register(
            "oxidized_copper_golem_statue",
            Block::new,
            AbstractBlock.Settings.copy(COPPER_GOLEM_STATUE).mapColor(MapColor.TEAL)
    );
    public static final Block WAXED_OXIDIZED_COPPER_GOLEM_STATUE = BlocksCover.register(
            "waxed_oxidized_copper_golem_statue",
            Block::new,
            AbstractBlock.Settings.copy(OXIDIZED_COPPER_GOLEM_STATUE)
    );
    public static final Block WAXED_COPPER_GOLEM_STATUE = BlocksCover.register(
            "waxed_copper_golem_statue",
            Block::new,
            AbstractBlock.Settings.copy(COPPER_GOLEM_STATUE)
    );
    public static final Block LIGHTNING_ROD = BlocksCover.register(
            "lightning_rod",
            LightningRodBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .solid()
                    .strength(3.0f, 6.0f)
                    .sounds(BlockSoundGroup.COPPER)
                    .nonOpaque()
    );
    public static final Block EXPOSED_LIGHTNING_ROD = BlocksCover.register(
            "exposed_lightning_rod",
            LightningRodBlock::new,
            AbstractBlock.Settings.copy(LIGHTNING_ROD).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
    );
    public static final Block WAXED_EXPOSED_LIGHTNING_ROD = BlocksCover.register(
            "waxed_exposed_lightning_rod",
            LightningRodBlock::new,
            AbstractBlock.Settings.copy(EXPOSED_LIGHTNING_ROD)
    );
    public static final Block WEATHERED_LIGHTNING_ROD = BlocksCover.register(
            "weathered_lightning_rod",
            LightningRodBlock::new,
            AbstractBlock.Settings.copy(LIGHTNING_ROD).mapColor(MapColor.DARK_AQUA)
    );
    public static final Block WAXED_WEATHERED_LIGHTNING_ROD = BlocksCover.register(
            "waxed_weathered_lightning_rod",
            LightningRodBlock::new,
            AbstractBlock.Settings.copy(WEATHERED_LIGHTNING_ROD)
    );
    public static final Block OXIDIZED_LIGHTNING_ROD = BlocksCover.register(
            "oxidized_lightning_rod",
            LightningRodBlock::new,
            AbstractBlock.Settings.copy(LIGHTNING_ROD).mapColor(MapColor.TEAL)
    );
    public static final Block WAXED_OXIDIZED_LIGHTNING_ROD = BlocksCover.register(
            "waxed_oxidized_lightning_rod",
            LightningRodBlock::new,
            AbstractBlock.Settings.copy(OXIDIZED_LIGHTNING_ROD)
    );
    public static final Block WAXED_LIGHTNING_ROD = BlocksCover.register(
            "waxed_lightning_rod",
            LightningRodBlock::new,
            AbstractBlock.Settings.copy(LIGHTNING_ROD)
    );
    public static final Block POINTED_DRIPSTONE = BlocksCover.register(
            "pointed_dripstone",
            PointedDripstoneBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_BROWN)
                    .solid()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .nonOpaque()
                    .sounds(BlockSoundGroup.POINTED_DRIPSTONE)
                    .ticksRandomly()
                    .strength(5.5f, 3.0f)
                    .dynamicBounds()
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .solidBlock(BlocksCover::never)
    );
    public static final Block DRIPSTONE_BLOCK = BlocksCover.register(
            "dripstone_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_BROWN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .sounds(BlockSoundGroup.DRIPSTONE_BLOCK)
                    .requiresTool()
                    .strength(26.5F, 15.0F)
    );
    public static final Block CAVE_VINES = BlocksCover.register(
            "cave_vines",
            CaveVinesHeadBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .ticksRandomly()
                    .noCollision()
                    .luminance(CaveVines.getLuminanceSupplier(14))
                    .strength(0.5f)
                    .sounds(BlockSoundGroup.CAVE_VINES)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block CAVE_VINES_PLANT = BlocksCover.register(
            "cave_vines_plant",
            CaveVinesBodyBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .luminance(CaveVines.getLuminanceSupplier(14))
                    .strength(0.5f)
                    .sounds(BlockSoundGroup.CAVE_VINES)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block SPORE_BLOSSOM = BlocksCover.register(
            "spore_blossom",
            SporeBlossomBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .strength(0.5f)
                    .noCollision()
                    .sounds(BlockSoundGroup.SPORE_BLOSSOM)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block AZALEA = BlocksCover.register(
            "azalea",
            AzaleaBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .strength(1.2f)
                    .sounds(BlockSoundGroup.AZALEA)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block FLOWERING_AZALEA = BlocksCover.register(
            "flowering_azalea",
            AzaleaBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .strength(1.2f)
                    .sounds(BlockSoundGroup.FLOWERING_AZALEA)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block MOSS_CARPET = BlocksCover.register(
            "moss_carpet",
            CarpetBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GREEN)
                    .strength(0.1f)
                    .sounds(BlockSoundGroup.MOSS_CARPET)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block PINK_PETALS = BlocksCover.register(
            "pink_petals",
            FlowerbedBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .sounds(BlockSoundGroup.FLOWERBED)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block WILDFLOWERS = BlocksCover.register(
            "wildflowers",
            FlowerbedBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .sounds(BlockSoundGroup.FLOWERBED)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block LEAF_LITTER = BlocksCover.register(
            "leaf_litter",
            LeafLitterBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.BROWN)
                    .replaceable()
                    .noCollision()
                    .sounds(BlockSoundGroup.LEAF_LITTER)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block MOSS_BLOCK = BlocksCover.register(
            "moss_block",
            (AbstractBlock.Settings settings) -> new MossBlock(UndergroundConfiguredFeatures.MOSS_PATCH_BONEMEAL, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GREEN)
                    .strength(0.8f)
                    .sounds(BlockSoundGroup.MOSS_BLOCK)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block BIG_DRIPLEAF = BlocksCover.register(
            "big_dripleaf",
            BigDripleafBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .strength(0.5f)
                    .sounds(BlockSoundGroup.BIG_DRIPLEAF)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block BIG_DRIPLEAF_STEM = BlocksCover.register(
            "big_dripleaf_stem",
            BigDripleafStemBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .strength(0.8f)
                    .sounds(BlockSoundGroup.BIG_DRIPLEAF)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block SMALL_DRIPLEAF = BlocksCover.register(
            "small_dripleaf",
            SmallDripleafBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .noCollision()
                    .strength(0.3f)
                    .sounds(BlockSoundGroup.SMALL_DRIPLEAF)
                    .offset(AbstractBlock.OffsetType.XYZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block HANGING_ROOTS = BlocksCover.register(
            "hanging_roots",
            HangingRootsBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIRT_BROWN)
                    .replaceable()
                    .noCollision()
                    .strength(5.0f)
                    .sounds(BlockSoundGroup.HANGING_ROOTS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .burnable()
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block ROOTED_DIRT = BlocksCover.register(
            "rooted_dirt",
            RootedDirtBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DIRT_BROWN)
                    .strength(12.0f , 6.0f)
                    .sounds(BlockSoundGroup.ROOTED_DIRT)
    );
    public static final Block MUD = BlocksCover.register(
            "mud",
            MudBlock::new,
            AbstractBlock.Settings.copy(DIRT)
                    .mapColor(MapColor.TERRACOTTA_CYAN)
                    .allowsSpawning(BlocksCover::always)
                    .solidBlock(BlocksCover::always)
                    .blockVision(BlocksCover::always)
                    .suffocates(BlocksCover::always)
                    .sounds(BlockSoundGroup.MUD)
    );
    public static final Block DEEPSLATE = BlocksCover.register(
            "deepslate",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(40.0f, 26.0f)
                    .sounds(BlockSoundGroup.DEEPSLATE)
    );
    public static final Block COBBLED_DEEPSLATE = BlocksCover.register(
            "cobbled_deepslate",
            AbstractBlock.Settings.copy(DEEPSLATE).strength(35.0f, 16.0f)
    );
    public static final Block COBBLED_DEEPSLATE_STAIRS = BlocksCover.registerStairsBlock("cobbled_deepslate_stairs", COBBLED_DEEPSLATE);
    public static final Block COBBLED_DEEPSLATE_SLAB = BlocksCover.register(
            "cobbled_deepslate_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(COBBLED_DEEPSLATE)
    );
    public static final Block COBBLED_DEEPSLATE_WALL = BlocksCover.register(
            "cobbled_deepslate_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(COBBLED_DEEPSLATE).solid()
    );
    public static final Block POLISHED_DEEPSLATE = BlocksCover.register(
            "polished_deepslate",
            AbstractBlock.Settings.copy(COBBLED_DEEPSLATE).sounds(BlockSoundGroup.POLISHED_DEEPSLATE)
    );
    public static final Block POLISHED_DEEPSLATE_STAIRS = BlocksCover.registerStairsBlock("polished_deepslate_stairs", POLISHED_DEEPSLATE);
    public static final Block POLISHED_DEEPSLATE_SLAB = BlocksCover.register(
            "polished_deepslate_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(POLISHED_DEEPSLATE)
    );
    public static final Block POLISHED_DEEPSLATE_WALL = BlocksCover.register(
            "polished_deepslate_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(POLISHED_DEEPSLATE).solid()
    );
    public static final Block DEEPSLATE_TILES = BlocksCover.register(
            "deepslate_tiles",
            AbstractBlock.Settings.copy(COBBLED_DEEPSLATE).sounds(BlockSoundGroup.DEEPSLATE_TILES)
    );
    public static final Block DEEPSLATE_TILE_STAIRS = BlocksCover.registerStairsBlock("deepslate_tile_stairs", DEEPSLATE_TILES);
    public static final Block DEEPSLATE_TILE_SLAB = BlocksCover.register(
            "deepslate_tile_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(DEEPSLATE_TILES)
    );
    public static final Block DEEPSLATE_TILE_WALL = BlocksCover.register(
            "deepslate_tile_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(DEEPSLATE_TILES).solid()
    );
    public static final Block CRACKED_DEEPSLATE_TILES = BlocksCover.register(
            "cracked_deepslate_tiles",
            AbstractBlock.Settings.copy(DEEPSLATE_TILES)
    );
    public static final Block DEEPSLATE_BRICKS = BlocksCover.register(
            "deepslate_bricks",
            AbstractBlock.Settings.copy(COBBLED_DEEPSLATE).sounds(BlockSoundGroup.DEEPSLATE_BRICKS)
    );
    public static final Block DEEPSLATE_BRICK_STAIRS = BlocksCover.registerStairsBlock("deepslate_brick_stairs", DEEPSLATE_BRICKS);
    public static final Block DEEPSLATE_BRICK_SLAB = BlocksCover.register(
            "deepslate_brick_slab",
            SlabBlock::new,
            AbstractBlock.Settings.copy(DEEPSLATE_BRICKS)
    );
    public static final Block DEEPSLATE_BRICK_WALL = BlocksCover.register(
            "deepslate_brick_wall",
            WallBlock::new,
            AbstractBlock.Settings.copy(DEEPSLATE_BRICKS).solid()
    );
    public static final Block CRACKED_DEEPSLATE_BRICKS = BlocksCover.register(
            "cracked_deepslate_bricks",
            AbstractBlock.Settings.copy(DEEPSLATE_BRICKS)
    );
    public static final Block CHISELED_DEEPSLATE = BlocksCover.register(
            "chiseled_deepslate",
            AbstractBlock.Settings.copy(COBBLED_DEEPSLATE).sounds(BlockSoundGroup.DEEPSLATE_BRICKS)
    );
    public static final Block INFESTED_DEEPSLATE = BlocksCover.register(
            "infested_deepslate",
            (AbstractBlock.Settings settings) -> new RotatedInfestedBlock(DEEPSLATE, settings),
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .sounds(BlockSoundGroup.DEEPSLATE)
    );
    public static final Block SMOOTH_BASALT = BlocksCover.register(
            "smooth_basalt",
            AbstractBlock.Settings.copy(BASALT)
    );
    public static final Block RAW_IRON_BLOCK = BlocksCover.register(
            "raw_iron_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.RAW_IRON_PINK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(55.0F, 26.0F)
    );
    public static final Block RAW_COPPER_BLOCK = BlocksCover.register(
            "raw_copper_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.ORANGE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(35.0F, 16.0F)
    );
    public static final Block RAW_GOLD_BLOCK = BlocksCover.register(
            "raw_gold_block",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.GOLD)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresTool()
                    .strength(15.0F, 6.0F)
    );
    public static final Block POTTED_AZALEA_BUSH = BlocksCover.register(
            "potted_azalea_bush",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(AZALEA, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block POTTED_FLOWERING_AZALEA_BUSH = BlocksCover.register(
            "potted_flowering_azalea_bush",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(FLOWERING_AZALEA, settings),
            BlocksCover.createFlowerPotSettings()
    );
    public static final Block OCHRE_FROGLIGHT = BlocksCover.register(
            "ochre_froglight",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PALE_YELLOW)
                    .strength(0.3f)
                    .luminance(state -> 15)
                    .sounds(BlockSoundGroup.FROGLIGHT)
    );
    public static final Block VERDANT_FROGLIGHT = BlocksCover.register(
            "verdant_froglight",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LICHEN_GREEN)
                    .strength(0.3f)
                    .luminance(state -> 15)
                    .sounds(BlockSoundGroup.FROGLIGHT)
    );
    public static final Block PEARLESCENT_FROGLIGHT = BlocksCover.register(
            "pearlescent_froglight",
            PillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.PINK)
                    .strength(0.3f)
                    .luminance(state -> 15)
                    .sounds(BlockSoundGroup.FROGLIGHT)
    );
    public static final Block FROGSPAWN = BlocksCover.register(
            "frogspawn",
            FrogspawnBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.WATER_BLUE)
                    .breakInstantly()
                    .nonOpaque()
                    .noCollision()
                    .sounds(BlockSoundGroup.FROGSPAWN)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block REINFORCED_DEEPSLATE = BlocksCover.register(
            "reinforced_deepslate",
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DEEPSLATE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .sounds(BlockSoundGroup.DEEPSLATE)
                    .strength(55.0F, 1200.0F)
    );
    public static final Block DECORATED_POT = BlocksCover.register(
            "decorated_pot",
            DecoratedPotBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .strength(1.0f)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .nonOpaque()
    );
    public static final Block CRAFTER = BlocksCover.register(
            "crafter",
            CrafterBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .strength(1.5f, 3.5f)
    );
    public static final Block TRIAL_SPAWNER = BlocksCover.register(
            "trial_spawner",
            TrialSpawnerBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .luminance(state -> state.get(TrialSpawnerBlock.TRIAL_SPAWNER_STATE).getLuminance())
                    .strength(50.0f)
                    .sounds(BlockSoundGroup.TRIAL_SPAWNER)
                    .blockVision(BlocksCover::never)
                    .nonOpaque()
    );
    public static final Block VAULT = BlocksCover.register(
            "vault",
            VaultBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .nonOpaque()
                    .sounds(BlockSoundGroup.VAULT)
                    .luminance(state -> state.get(VaultBlock.VAULT_STATE).getLuminance())
                    .strength(50.0f)
                    .blockVision(BlocksCover::never)
    );
    public static final Block HEAVY_CORE = BlocksCover.register(
            "heavy_core",
            HeavyCoreBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.IRON_GRAY)
                    .instrument(NoteBlockInstrument.SNARE)
                    .sounds(BlockSoundGroup.HEAVY_CORE)
                    .strength(10.0f)
                    .pistonBehavior(PistonBehavior.NORMAL)
                    .resistance(1200.0f)
    );
    public static final Block PALE_MOSS_BLOCK = BlocksCover.register(
            "pale_moss_block",
            (AbstractBlock.Settings settings) -> new MossBlock(VegetationConfiguredFeatures.PALE_MOSS_PATCH_BONEMEAL, settings),
            AbstractBlock.Settings.create()
                    .burnable()
                    .mapColor(MapColor.LIGHT_GRAY)
                    .strength(0.8f)
                    .sounds(BlockSoundGroup.MOSS_BLOCK)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block PALE_MOSS_CARPET = BlocksCover.register(
            "pale_moss_carpet",
            PaleMossCarpetBlock::new,
            AbstractBlock.Settings.create()
                    .burnable()
                    .mapColor(PALE_MOSS_BLOCK.getDefaultMapColor())
                    .strength(0.2f)
                    .sounds(BlockSoundGroup.MOSS_CARPET)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block PALE_HANGING_MOSS = BlocksCover.register(
            "pale_hanging_moss",
            HangingMossBlock::new,
            AbstractBlock.Settings.create()
                    .burnable()
                    .mapColor(PALE_MOSS_BLOCK.getDefaultMapColor())
                    .noCollision()
                    .sounds(BlockSoundGroup.MOSS_CARPET)
                    .strength(0.8f)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block OPEN_EYEBLOSSOM = BlocksCover.register(
            "open_eyeblossom",
            (AbstractBlock.Settings settings) -> new EyeblossomBlock(EyeblossomBlock.EyeblossomState.OPEN, settings),
            AbstractBlock.Settings.create()
                    .mapColor(CREAKING_HEART.getDefaultMapColor())
                    .noCollision()
                    .strength(0.3f)
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .ticksRandomly()
    );
    public static final Block POTTED_OPEN_EYEBLOSSOM = BlocksCover.register(
            "potted_open_eyeblossom",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(OPEN_EYEBLOSSOM, settings),
            BlocksCover.createFlowerPotSettings().ticksRandomly()
    );
    public static final Block CLOSED_EYEBLOSSOM = BlocksCover.register(
            "closed_eyeblossom",
            (AbstractBlock.Settings settings) -> new EyeblossomBlock(EyeblossomBlock.EyeblossomState.CLOSED, settings),
            AbstractBlock.Settings.create()
                    .mapColor(PALE_OAK_LEAVES.getDefaultMapColor())
                    .noCollision()
                    .strength(0.3f)
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .ticksRandomly()
    );
    public static final Block POTTED_CLOSED_EYEBLOSSOM = BlocksCover.register(
            "potted_closed_eyeblossom",
            (AbstractBlock.Settings settings) -> new FlowerPotBlock(CLOSED_EYEBLOSSOM, settings),
            BlocksCover.createFlowerPotSettings().ticksRandomly()
    );
    public static final Block FIREFLY_BUSH = BlocksCover.register(
            "firefly_bush",
            FireflyBushBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_GREEN)
                    .burnable()
                    .luminance(state -> 2)
                    .noCollision()
                    .strength(1.2f)
                    .sounds(BlockSoundGroup.SWEET_BERRY_BUSH)
                    .pistonBehavior(PistonBehavior.DESTROY)
    );

    public static final Block SHULKER_BOX = BlocksCover.register(
            "shulker_box",
            (AbstractBlock.Settings settings) -> new ShulkerBoxBlock(null, settings),
            BlocksCover.createShulkerBoxSettings(MapColor.PURPLE)
    );
    public static final Block WHITE_SHULKER_BOX = BlocksCover.register(
            "white_shulker_box",
            (AbstractBlock.Settings settings) -> new ShulkerBoxBlock(DyeColor.WHITE, settings),
            BlocksCover.createShulkerBoxSettings(MapColor.WHITE)
    );
    public static final Block ORANGE_SHULKER_BOX = BlocksCover.register(
            "orange_shulker_box",
            (AbstractBlock.Settings settings) -> new ShulkerBoxBlock(DyeColor.ORANGE, settings),
            BlocksCover.createShulkerBoxSettings(MapColor.ORANGE)
    );
    public static final Block MAGENTA_SHULKER_BOX = BlocksCover.register(
            "magenta_shulker_box",
            (AbstractBlock.Settings settings) -> new ShulkerBoxBlock(DyeColor.MAGENTA, settings),
            BlocksCover.createShulkerBoxSettings(MapColor.MAGENTA)
    );
    public static final Block LIGHT_BLUE_SHULKER_BOX = BlocksCover.register(
            "light_blue_shulker_box",
            (AbstractBlock.Settings settings) -> new ShulkerBoxBlock(DyeColor.LIGHT_BLUE, settings),
            BlocksCover.createShulkerBoxSettings(MapColor.LIGHT_BLUE)
    );
    public static final Block YELLOW_SHULKER_BOX = BlocksCover.register(
            "yellow_shulker_box",
            (AbstractBlock.Settings settings) -> new ShulkerBoxBlock(DyeColor.YELLOW, settings),
            BlocksCover.createShulkerBoxSettings(MapColor.YELLOW)
    );
    public static final Block LIME_SHULKER_BOX = BlocksCover.register(
            "lime_shulker_box",
            (AbstractBlock.Settings settings) -> new ShulkerBoxBlock(DyeColor.LIME, settings),
            BlocksCover.createShulkerBoxSettings(MapColor.LIME)
    );
    public static final Block PINK_SHULKER_BOX = BlocksCover.register(
            "pink_shulker_box",
            (AbstractBlock.Settings settings) -> new ShulkerBoxBlock(DyeColor.PINK, settings),
            BlocksCover.createShulkerBoxSettings(MapColor.PINK)
    );
    public static final Block GRAY_SHULKER_BOX = BlocksCover.register(
            "gray_shulker_box",
            (AbstractBlock.Settings settings) -> new ShulkerBoxBlock(DyeColor.GRAY, settings),
            BlocksCover.createShulkerBoxSettings(MapColor.GRAY)
    );
    public static final Block LIGHT_GRAY_SHULKER_BOX = BlocksCover.register(
            "light_gray_shulker_box",
            (AbstractBlock.Settings settings) -> new ShulkerBoxBlock(DyeColor.LIGHT_GRAY, settings),
            BlocksCover.createShulkerBoxSettings(MapColor.LIGHT_GRAY)
    );
    public static final Block CYAN_SHULKER_BOX = BlocksCover.register(
            "cyan_shulker_box",
            (AbstractBlock.Settings settings) -> new ShulkerBoxBlock(DyeColor.CYAN, settings),
            BlocksCover.createShulkerBoxSettings(MapColor.CYAN)
    );
    public static final Block PURPLE_SHULKER_BOX = BlocksCover.register(
            "purple_shulker_box",
            (AbstractBlock.Settings settings) -> new ShulkerBoxBlock(DyeColor.PURPLE, settings),
            BlocksCover.createShulkerBoxSettings(MapColor.TERRACOTTA_PURPLE)
    );
    public static final Block BLUE_SHULKER_BOX = BlocksCover.register(
            "blue_shulker_box",
            (AbstractBlock.Settings settings) -> new ShulkerBoxBlock(DyeColor.BLUE, settings),
            BlocksCover.createShulkerBoxSettings(MapColor.BLUE)
    );
    public static final Block BROWN_SHULKER_BOX = BlocksCover.register(
            "brown_shulker_box",
            (AbstractBlock.Settings settings) -> new ShulkerBoxBlock(DyeColor.BROWN, settings),
            BlocksCover.createShulkerBoxSettings(MapColor.BROWN)
    );
    public static final Block GREEN_SHULKER_BOX = BlocksCover.register(
            "green_shulker_box",
            (AbstractBlock.Settings settings) -> new ShulkerBoxBlock(DyeColor.GREEN, settings),
            BlocksCover.createShulkerBoxSettings(MapColor.GREEN)
    );
    public static final Block RED_SHULKER_BOX = BlocksCover.register(
            "red_shulker_box",
            (AbstractBlock.Settings settings) -> new ShulkerBoxBlock(DyeColor.RED, settings),
            BlocksCover.createShulkerBoxSettings(MapColor.RED)
    );
    public static final Block BLACK_SHULKER_BOX = BlocksCover.register(
            "black_shulker_box",
            (AbstractBlock.Settings settings) -> new ShulkerBoxBlock(DyeColor.BLACK, settings),
            BlocksCover.createShulkerBoxSettings(MapColor.BLACK)
    );
    public static final Block STICKY_PISTON = BlocksCover.register(
            "sticky_piston",
            (AbstractBlock.Settings settings) -> new PistonBlock(true, settings),
            BlocksCover.createPistonSettings()
    );
    public static final Block PISTON = BlocksCover.register(
            "piston",
            (AbstractBlock.Settings settings) -> new PistonBlock(false, settings),
            BlocksCover.createPistonSettings()
    );



    public static ToIntFunction<BlockState> createLightLevelFromLitBlockCovertate(int litLevel) {
        return state -> state.get(Properties.LIT) != false ? litLevel : 0;
    }

    private static Function<BlockState, MapColor> createMapColorFromWaterloggedBlockCovertate(MapColor mapColor) {
        return state -> state.get(Properties.WATERLOGGED) != false ? MapColor.WATER_BLUE : mapColor;
    }

    /**
     * A shortcut to always return {@code false} in a typed context predicate with an
     * {@link EntityType}, used like {@code settings.allowSpawning(BlocksCover::never)}.
     */
    public static Boolean never(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return false;
    }

    /**
     * A shortcut to always return {@code true} in a typed context predicate with an
     * {@link EntityType}, used like {@code settings.allowSpawning(BlocksCover::always)}.
     */
    public static Boolean always(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return true;
    }

    public static Boolean canSpawnOnLeaves(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return type == EntityType.OCELOT || type == EntityType.PARROT;
    }

    private static Block registerBedBlock(String id, DyeColor color) {
        return BlocksCover.register(id, (AbstractBlock.Settings settings)
                -> new BedBlock(color, settings)
                , AbstractBlock.Settings.create().mapColor(state -> state.get(BedBlock.PART) == BedPart.FOOT ? color.getMapColor() : MapColor.WHITE_GRAY)
                        .sounds(BlockSoundGroup.WOOD)
                        .strength(5.0f)
                        .nonOpaque()
                        .burnable()
                        .pistonBehavior(PistonBehavior.DESTROY)
        );
    }

    public static AbstractBlock.Settings createLogSettings(MapColor topMapColor, MapColor sideMapColor, BlockSoundGroup sounds) {
        return AbstractBlock.Settings.create()
                .mapColor(state -> state.get(PillarBlock.AXIS) == Direction.Axis.Y ? topMapColor : sideMapColor)
                .instrument(NoteBlockInstrument.BASS)
                .strength(20.0F , 15.0F)
                .burnable()
                .requiresTool().sounds(sounds).burnable();
    }

    public static AbstractBlock.Settings createNetherStemSettings(MapColor mapColor) {
        return AbstractBlock.Settings.create()
                .mapColor(state -> mapColor)
                .instrument(NoteBlockInstrument.BASS)
                .strength(25.0F , 35.0F)
                .sounds(BlockSoundGroup.NETHER_STEM);
    }

    /**
     * A shortcut to always return {@code true} a context predicate, used as
     * {@code settings.solidBlock(BlocksCover::always)}.
     */
    public static boolean always(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    /**
     * A shortcut to always return {@code false} a context predicate, used as
     * {@code settings.solidBlock(BlocksCover::never)}.
     */
    public static boolean never(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    private static Block registerStainedGlassBlock(String id, DyeColor color) {
        return BlocksCover.register(id,
                (AbstractBlock.Settings settings) ->
                        new StainedGlassBlock(color, settings),
                AbstractBlock.Settings.create().mapColor(color)
                        .instrument(NoteBlockInstrument.HAT)
                        .strength(3.0f , 1.5f)
                        .sounds(BlockSoundGroup.GLASS)
                        .nonOpaque()
                        .allowsSpawning(BlocksCover::never)
                        .solidBlock(BlocksCover::never)
                        .suffocates(BlocksCover::never)
                        .blockVision(BlocksCover::never)
        );
    }

    public static AbstractBlock.Settings createLeavesSettings(BlockSoundGroup sounds) {
        return AbstractBlock.Settings.create()
                .mapColor(MapColor.DARK_GREEN)
                .strength(3.0F , 0.5F)
                .ticksRandomly()
                .sounds(sounds)
                .noCollision()
                .allowsSpawning(Blocks::never)
                .suffocates(Blocks::never)
                .blockVision(Blocks::never)
                .burnable()
                .pistonBehavior(PistonBehavior.DESTROY)
                .solidBlock(Blocks::never);
    }

    private static AbstractBlock.Settings createShulkerBoxSettings(MapColor mapColor) {
        return AbstractBlock.Settings.create()
                .mapColor(mapColor)
                .solid()
                .strength(2.0f)
                .dynamicBounds()
                .nonOpaque()
                .suffocates(SHULKER_BOX_SUFFOCATES_PREDICATE)
                .blockVision(SHULKER_BOX_SUFFOCATES_PREDICATE)
                .pistonBehavior(PistonBehavior.DESTROY);
    }

    private static AbstractBlock.Settings createPistonSettings() {
        return AbstractBlock.Settings.create()
                .mapColor(MapColor.STONE_GRAY)
                .strength(2.5f)
                .solidBlock(BlocksCover::never)
                .suffocates(PISTON_SUFFOCATES_PREDICATE)
                .blockVision(PISTON_SUFFOCATES_PREDICATE)
                .pistonBehavior(PistonBehavior.BLOCK);
    }

    public static AbstractBlock.Settings createButtonSettings() {
        return AbstractBlock.Settings.create()
                .noCollision()
                .strength(0.5f)
                .pistonBehavior(PistonBehavior.DESTROY);
    }

    public static AbstractBlock.Settings createFlowerPotSettings() {
        return AbstractBlock.Settings.create()
                .strength(0.5f)
                .nonOpaque()
                .pistonBehavior(PistonBehavior.DESTROY);
    }

    private static AbstractBlock.Settings createCandleSettings(MapColor mapColor) {
        return AbstractBlock.Settings.create().mapColor(mapColor).nonOpaque().strength(0.1f).sounds(BlockSoundGroup.CANDLE).luminance(CandleBlock.STATE_TO_LUMINANCE).pistonBehavior(PistonBehavior.DESTROY);
    }

    private static Block registerStairsBlock(String id, Block base) {
        return BlocksCover.register(id,
                (AbstractBlock.Settings settings) -> new StairsBlock(base.getDefaultState(), settings),
                AbstractBlock.Settings.copy(base)
        );
    }

    private static AbstractBlock.Settings copyLootTable(Block block, boolean copyTranslationKey) {
        AbstractBlock.Settings settings2 = AbstractBlock.Settings.create().lootTable(block.getLootTableKey());
        if (copyTranslationKey) {
            settings2 = settings2.overrideTranslationKey(block.getTranslationKey());
        }
        return settings2;
    }

    public static Block register(RegistryKey<Block> key, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        Block block = factory.apply(settings.registryKey(key));
        Block registered = Registry.register(Registries.BLOCK, key, block);
        BlockCoverMap.put(key, registered);
        return registered;
    }

    public static Block register(RegistryKey<Block> key, AbstractBlock.Settings settings) {
        return BlocksCover.register(key, Block::new, settings);
    }

    private static RegistryKey<Block> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.ofVanilla(id));
    }

    private static Block register(String id, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        return BlocksCover.register(BlocksCover.keyOf(id), factory, settings);
    }

    private static Block register(String id, AbstractBlock.Settings settings) {
        return BlocksCover.register(id, Block::new, settings);
    }
    public static void registerBlocksCover() {
    }
}

