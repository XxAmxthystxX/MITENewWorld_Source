package com.mitenewworld.world;

import com.mitenewworld.MITENewWorld;
import com.mitenewworld.block.OreBackground;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.List;
import java.util.Locale;

public final class ModOreFeatures {

    public static final Feature<ModOreConfiguration> MOD_ORE = Registry.register(
            Registries.FEATURE,
            Identifier.of(MITENewWorld.MOD_ID, "mod_ore"),
            new ModOreFeature(ModOreConfiguration.CODEC));

    private static final List<String> OVERWORLD_METALS = List.of(
            "copper", "tin", "aluminium", "gold", "silver", "iron", "titanium", "platinum", "coal");

    private static final List<String> UNDERGROUND_METALS = List.of(
            "copper", "tin", "aluminium", "gold", "silver", "iron", "titanium", "platinum", "coal",
            "mithril", "adamantium");

    private static final List<String> NETHER_METALS = List.of("titanium", "platinum", "iridium");

    public static final RegistryKey<PlacedFeature> UNDERGROUND_DIAMOND_ORE = placedKey("underworld_diamond_ore");
    public static final RegistryKey<PlacedFeature> UNDERGROUND_EMERALD_ORE = placedKey("underworld_emerald_ore");
    public static final RegistryKey<PlacedFeature> UNDERGROUND_LAPIS_ORE = placedKey("underworld_lapis_ore");

    private ModOreFeatures() {
    }

    private static Identifier id(OreBackground background, String metal) {
        return Identifier.of(MITENewWorld.MOD_ID,
                "ore_" + background.name().toLowerCase(Locale.ROOT) + "_" + metal);
    }

    public static RegistryKey<ConfiguredFeature<?, ?>> configuredKey(OreBackground background, String metal) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, id(background, metal));
    }

    public static RegistryKey<PlacedFeature> placedKey(OreBackground background, String metal) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, id(background, metal));
    }

    private static RegistryKey<PlacedFeature> placedKey(String id) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MITENewWorld.MOD_ID, id));
    }

    public static void registerBiomeFeatures() {
        for (String metal : OVERWORLD_METALS) {
            BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, placedKey(OreBackground.OVERWORLD, metal));
        }
        for (String metal : UNDERGROUND_METALS) {
            BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.UNDERGROUND_NORMAL), GenerationStep.Feature.UNDERGROUND_ORES, placedKey(OreBackground.UNDERGROUND, metal));
        }
        BiomeModifications.addFeature(
                BiomeSelectors.foundInTheEnd(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                placedKey(OreBackground.END, "starlight"));
        for (String metal : NETHER_METALS) {
            BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.BASALT_DELTAS), GenerationStep.Feature.UNDERGROUND_ORES, placedKey(OreBackground.NETHER, metal));
        }
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.UNDERGROUND_NORMAL), GenerationStep.Feature.UNDERGROUND_ORES, UNDERGROUND_DIAMOND_ORE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.UNDERGROUND_NORMAL), GenerationStep.Feature.UNDERGROUND_ORES, UNDERGROUND_EMERALD_ORE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.UNDERGROUND_NORMAL), GenerationStep.Feature.UNDERGROUND_ORES, UNDERGROUND_LAPIS_ORE);
    }

    public static void init() {
    }
}
