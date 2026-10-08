package com.mitenewworld.loot;

import com.mitenewworld.MITENewWorld;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ModLootTables {
    private static final Set<RegistryKey<LootTable>> LOOT_TABLES = new HashSet<>();
    private static final Set<RegistryKey<LootTable>> LOOT_TABLES_READ_ONLY = Collections.unmodifiableSet(LOOT_TABLES);

    public static final RegistryKey<LootTable> BLUEBERRY_BUSH = register("blueberry_bush/interactive_block");

    private static Map<DyeColor, RegistryKey<LootTable>> registerAllDyeColors(String prefix) {
        return Util.mapEnum(DyeColor.class, color -> register(prefix + "/" + color.getId()));
    }

    private static RegistryKey<LootTable> register(String id) {
        return registerLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(MITENewWorld.MOD_ID , id)));
    }

    private static RegistryKey<LootTable> registerLootTable(RegistryKey<LootTable> key) {
        if (LOOT_TABLES.add(key)) {
            return key;
        } else {
            throw new IllegalArgumentException(key.getValue() + " is already a registered built-in loot table");
        }
    }

    public static Set<RegistryKey<LootTable>> getAll() {
        return LOOT_TABLES_READ_ONLY;
    }

    public static void registerLootTables() {
        MITENewWorld.LOGGER.info("Registering loot tables");
    }
}
