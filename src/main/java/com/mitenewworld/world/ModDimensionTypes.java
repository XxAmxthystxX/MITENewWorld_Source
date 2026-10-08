package com.mitenewworld.world;

import com.mitenewworld.MITENewWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;


public class ModDimensionTypes {
    public static final RegistryKey<DimensionType> UNDERWORLD_DIMENSION_TYPE = ModDimensionTypes.ofType("underworld_dimension_type");
    public static final RegistryKey<World> UNDERWORLD_WORLD_KEY = ModDimensionTypes.ofWorld("underworld");
    public static final RegistryKey<DimensionOptions> UNDERWORLD_DIMENSION_OPTIONS = ModDimensionTypes.ofOptions("underworld");

    private static RegistryKey<DimensionOptions> ofOptions(String id) {
        return RegistryKey.of(RegistryKeys.DIMENSION, Identifier.of(MITENewWorld.MOD_ID , id));
    }
    private static RegistryKey<DimensionType> ofType(String id) {
        return RegistryKey.of(RegistryKeys.DIMENSION_TYPE, Identifier.of(MITENewWorld.MOD_ID , id));
    }
    private static RegistryKey<World> ofWorld(String id) {
        return RegistryKey.of(RegistryKeys.WORLD , Identifier.of(MITENewWorld.MOD_ID , id));
    }

    public static void registerModDimensionTypes() {

    }
}
