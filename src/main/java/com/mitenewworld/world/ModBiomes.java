package com.mitenewworld.world;

import com.mitenewworld.MITENewWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class ModBiomes {
    public static final RegistryKey<Biome> UNDERGROUND_NORMAL = ModBiomes.keyOf("underground_normal");


    private static RegistryKey<Biome> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.BIOME, Identifier.of(MITENewWorld.MOD_ID, id));
    }

    public static void registerBiomes() {

    }
}
