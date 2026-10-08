package com.mitenewworld.block;

import com.mitenewworld.registry.ModBlocks;
import com.mitenewworld.registry.ModItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class ModOres {

    public static final Block MOD_STONE = ModBlocks.register(
            "mod_stone",
            ModStoneBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.STONE_GRAY)
                    .requiresTool()
                    .strength(ModStoneBlock.BASE_HARDNESS, 15.0f)
                    .sounds(BlockSoundGroup.STONE)
    );

    private static final List<String> OVERWORLD_ORES = List.of(
            "copper", "tin", "aluminium", "gold", "silver", "iron", "titanium",
            "platinum", "mithril", "adamantium", "ancient_metal", "starlight", "iridium", "coal");

    private static final List<String> UNDERGROUND_ORES = List.of(
            "copper", "tin", "aluminium", "gold", "silver", "iron", "titanium", "platinum", "coal");

    private static final List<String> UNDERGROUND_RARE_ORES = List.of("mithril", "adamantium");

    private static final List<String> NETHER_ORES = List.of("titanium", "platinum", "iridium");

    public static final List<Block> ALL_ORES = new ArrayList<>();
    public static final List<Block> TINTED_ORES = new ArrayList<>();

    private static final Map<String, Block> ORES = new HashMap<>();

    static {
        for (String metal : OVERWORLD_ORES) {
            for (OreRichness richness : OreRichness.values()) {
                registerOre(OreBackground.OVERWORLD, metal, richness);
            }
        }
        for (String metal : UNDERGROUND_ORES) {
            for (OreRichness richness : OreRichness.values()) {
                registerOre(OreBackground.UNDERGROUND, metal, richness);
            }
        }
        for (String metal : UNDERGROUND_RARE_ORES) {
            registerOre(OreBackground.UNDERGROUND, metal, OreRichness.POOR);
            registerOre(OreBackground.UNDERGROUND, metal, OreRichness.MEDIUM);
        }
        for (OreRichness richness : OreRichness.values()) {
            registerOre(OreBackground.END, "starlight", richness);
        }
        for (String metal : NETHER_ORES) {
            for (OreRichness richness : OreRichness.values()) {
                registerOre(OreBackground.NETHER, metal, richness);
            }
        }
        registerVanillaUndergroundOre("diamond");
        registerVanillaUndergroundOre("emerald");
        registerVanillaUndergroundOre("lapis");
    }

    private ModOres() {
    }

    private static void registerOre(OreBackground background, String metal, OreRichness richness) {
        OreTraits traits = OreTraits.of(metal);
        Item drop = rawOreOf(metal);
        String id = background.idPrefix() + metal + "_ore_" + richness.id;
        Block block = ModBlocks.register(id, settings -> switch (background) {
            case OVERWORLD -> new TintedOreBlock(metal, richness, drop, traits.hardness(), settings);
            case UNDERGROUND -> new UndergroundOreBlock(metal, richness, drop, traits.hardness(), settings);
            case END -> new EndOreBlock(metal, richness, drop, traits.hardness(), settings);
            case NETHER -> new NetherOreBlock(metal, richness, drop, traits.hardness(), settings);
        }, oreSettings(traits));
        ALL_ORES.add(block);
        if (background == OreBackground.OVERWORLD) {
            TINTED_ORES.add(block);
        }
        ORES.put(key(background, metal, richness), block);
    }

    private static void registerVanillaUndergroundOre(String metal) {
        OreTraits traits = OreTraits.of(metal);
        Block block = ModBlocks.register(
                "underground_" + metal + "_ore",
                settings -> new UndergroundOreBlock(metal, OreRichness.MEDIUM, null, traits.hardness(), settings),
                oreSettings(traits));
        ALL_ORES.add(block);
        ORES.put(key(OreBackground.UNDERGROUND, metal, OreRichness.MEDIUM), block);
    }

    private static AbstractBlock.Settings oreSettings(OreTraits traits) {
        return AbstractBlock.Settings.create()
                .mapColor(MapColor.STONE_GRAY)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresTool()
                .strength(traits.hardness(), traits.resistance())
                .sounds(BlockSoundGroup.STONE);
    }

    private static Item rawOreOf(String metal) {
        return switch (metal) {
            case "copper" -> ModItems.RAW_COPPER;
            case "silver" -> ModItems.RAW_SILVER;
            case "gold" -> ModItems.RAW_GOLD;
            case "iron" -> ModItems.RAW_IRON;
            case "titanium" -> ModItems.RAW_TITANIUM;
            case "mithril" -> ModItems.RAW_MITHRIL;
            case "ancient_metal" -> ModItems.RAW_ANCIENT_METAL;
            case "adamantium" -> ModItems.RAW_ADAMANTIUM;
            case "tin" -> ModItems.RAW_TIN;
            case "aluminium" -> ModItems.RAW_ALUMINIUM;
            case "platinum" -> ModItems.RAW_PLATINUM;
            case "iridium" -> ModItems.RAW_IRIDIUM;
            case "starlight" -> ModItems.RAW_STARLIGHT;
            case "coal" -> Items.COAL;
            default -> null;
        };
    }

    private static String key(OreBackground background, String metal, OreRichness richness) {
        return background.name().toLowerCase(Locale.ROOT) + "/" + metal + "/" + richness.id;
    }

    public static BlockState oreState(OreBackground background, String metal, OreRichness richness) {
        Block block = ORES.get(key(background, metal, richness));
        return block == null ? null : block.getDefaultState();
    }

    public static void init() {
    }
}
