package com.mitenewworld.item.metal;

import com.mitenewworld.MITENewWorld;
import com.mitenewworld.block.alloyfurnace.AbstractModBlastFurnaceBlock;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.Map;

public final class ModAlloyMetals {


    public static final RegistryKey<Registry<MetalType>> METAL_TYPE_KEY =
            RegistryKey.ofRegistry(Identifier.of(MITENewWorld.MOD_ID, "metal_type"));

    public static final Registry<MetalType> METAL_TYPE =
            FabricRegistryBuilder.createSimple(METAL_TYPE_KEY)
                    .attribute(RegistryAttribute.SYNCED)
                    .buildAndRegister();

    // ============================================================
    //  基底金属
    // ============================================================

    public static final MetalType COPPER = register(
            "copper", 0xE77C56,
            600f, 3f, 12f, 5f, 32f,
            AbstractModBlastFurnaceBlock.HeatLevel.CAMPFIRE, 80f,
            1.0f, 1.0f, Map.of(
                    25,  new MetalBonus(Stat.TOUGHNESS, 0.3f),
                    50,  new MetalBonus(Stat.TOUGHNESS, 0.7f),
                    75,  new MetalBonus(Stat.TOUGHNESS, 1.2f),
                    100, new MetalBonus(Stat.TOUGHNESS, 2.0f)
            ));

    public static final MetalType SILVER = register(
            "silver", 0xE8E8F0,
            400f, 2f, 15f, 12f, 33f,
            AbstractModBlastFurnaceBlock.HeatLevel.FURNACE, 180f,
            0.9f, 0.9f, Map.of(
                    25,  new MetalBonus(Stat.ENCHANT_RARITY, 0.5f),
                    50,  new MetalBonus(Stat.ENCHANT_RARITY, 1.0f),
                    75,  new MetalBonus(Stat.ENCHANT_RARITY, 1.8f),
                    100, new MetalBonus(Stat.ENCHANT_RARITY, 3.0f)
            ));

    public static final MetalType IRON = register(
            "iron", 0xD8D8D8,
            1000f, 2f, 25f, 8f, 35f,
            AbstractModBlastFurnaceBlock.HeatLevel.FURNACE, 200f,
            1.4f, 1.4f, Map.of(
                    25,  new MetalBonus(Stat.DURABILITY, 50f),
                    50,  new MetalBonus(Stat.DURABILITY, 100f),
                    75,  new MetalBonus(Stat.DURABILITY, 180f),
                    100, new MetalBonus(Stat.DURABILITY, 300f)
            ));

    public static final MetalType TITANIUM = register(
            "titanium", 0x98A8C0,
            3000f, 8f, 40f, 10f, 22f,
            AbstractModBlastFurnaceBlock.HeatLevel.FURNACE, 350f,
            0.3f, 2.0f, Map.of(
                    25,  new MetalBonus(Stat.TOUGHNESS, 0.4f),
                    50,  new MetalBonus(Stat.TOUGHNESS, 0.8f),
                    75,  new MetalBonus(Stat.TOUGHNESS, 1.3f),
                    100, new MetalBonus(Stat.TOUGHNESS, 2.0f)
            ));

    public static final MetalType MITHRIL = register(
            "mithril", 0xB8D8F0,
            8000f, 5f, 55f, 18f, 18f,
            AbstractModBlastFurnaceBlock.HeatLevel.BLAST_FURNACE, 800f,
            1.5f, 1.5f, Map.of(
                    25,  new MetalBonus(Stat.DURABILITY, 400f),
                    50,  new MetalBonus(Stat.DURABILITY, 1000f),
                    75,  new MetalBonus(Stat.DURABILITY, 2000f),
                    100, new MetalBonus(Stat.DURABILITY, 4000f)
            ));

    public static final MetalType ADAMANTIUM = register(
            "adamantium", 0x6A4A8A,
            60000f, 6f, 85f, 12f, 42f,
            AbstractModBlastFurnaceBlock.HeatLevel.ARCANE, 7200f,
            1.8f, 1.8f, Map.of(
                    25,  new MetalBonus(Stat.DURABILITY, 3000f),
                    50,  new MetalBonus(Stat.DURABILITY, 8000f),
                    75,  new MetalBonus(Stat.HARDNESS, 5f),
                    100, new MetalBonus(Stat.DURABILITY, 30000f)
            ));

    public static final MetalType ANCIENT_METAL = register(
            "ancient_metal", 0x2A9088,
            100000f, 7f, 70f, 15f, 56f,
            AbstractModBlastFurnaceBlock.HeatLevel.CRUCIBLE, 2400f,
            2.0f, 2.0f, Map.of(
                    25,  new MetalBonus(Stat.DURABILITY, 5000f),
                    50,  new MetalBonus(Stat.DURABILITY, 12000f),
                    75,  new MetalBonus(Stat.DURABILITY, 25000f),
                    100, new MetalBonus(Stat.DURABILITY, 50000f)
            ));

// ============================================================
//  调和金属（偏科）
// ============================================================

    public static final MetalType TIN = register(
            "tin", 0xA8B8B8,
            180f, 4f, 5f, 3f, 25f,
            AbstractModBlastFurnaceBlock.HeatLevel.CAMPFIRE, 50f,
            0.7f, 0.7f, Map.of(
                    25,  new MetalBonus(Stat.HARDNESS, 1f),
                    50,  new MetalBonus(Stat.HARDNESS, 2f),
                    75,  new MetalBonus(Stat.HARDNESS, 4f),
                    100, new MetalBonus(Stat.HARDNESS, 6f)
            ));

    public static final MetalType GOLD = register(
            "gold", 0xFFD966,
            150f, 1f, 8f, 25f, 62f,
            AbstractModBlastFurnaceBlock.HeatLevel.CAMPFIRE, 120f,
            0.6f, 0.8f, Map.of(
                    25,  new MetalBonus(Stat.ENCHANT_RARITY, 1.0f),
                    50,  new MetalBonus(Stat.ENCHANT_RARITY, 2.0f),
                    75,  new MetalBonus(Stat.ENCHANT_RARITY, 4.0f),
                    100, new MetalBonus(Stat.ENCHANT_RARITY, 6.0f)
            ));

    public static final MetalType ALUMINIUM = register(
            "aluminium", 0xC8D0D8,
            350f, 7f, 18f, 8f, 10f,
            AbstractModBlastFurnaceBlock.HeatLevel.FURNACE, 150f,
            0.6f, 0.9f, Map.of(
                    25,  new MetalBonus(Stat.TOUGHNESS, 0.3f),
                    50,  new MetalBonus(Stat.TOUGHNESS, 0.7f),
                    75,  new MetalBonus(Stat.TOUGHNESS, 1.2f),
                    100, new MetalBonus(Stat.TOUGHNESS, 2.0f)
            ));

    public static final MetalType PLATINUM = register(
            "platinum", 0xE0D8C8,
            600f, 10f, 30f, 15f, 48f,
            AbstractModBlastFurnaceBlock.HeatLevel.BLAST_FURNACE, 550f,
            0.7f, 0.9f, Map.of(
                    25,  new MetalBonus(Stat.TOUGHNESS, 0.4f),
                    50,  new MetalBonus(Stat.TOUGHNESS, 0.9f),
                    75,  new MetalBonus(Stat.TOUGHNESS, 1.5f),
                    100, new MetalBonus(Stat.TOUGHNESS, 2.5f)
            ));

    public static final MetalType IRIDIUM = register(
            "iridium", 0xA8C0D8,
            8000f, 3f, 100f, 8f, 70f,
            AbstractModBlastFurnaceBlock.HeatLevel.ARCANE, 3200f,
            0.7f, 0.9f, Map.of(
                    25,  new MetalBonus(Stat.HARDNESS, 2f),
                    50,  new MetalBonus(Stat.HARDNESS, 4f),
                    75,  new MetalBonus(Stat.HARDNESS, 7f),
                    100, new MetalBonus(Stat.HARDNESS, 12f)
            ));

    public static final MetalType STARLIGHT = register(
            "starlight", 0x9080E8,
            4000f, 5f, 45f, 22f, 15f,
            AbstractModBlastFurnaceBlock.HeatLevel.CRUCIBLE, 2200f,
            0.5f, 0.8f, Map.of(
                    25,  new MetalBonus(Stat.ENCHANT_RARITY, 0.8f),
                    50,  new MetalBonus(Stat.ENCHANT_RARITY, 1.5f),
                    75,  new MetalBonus(Stat.ENCHANT_RARITY, 2.5f),
                    100, new MetalBonus(Stat.ENCHANT_RARITY, 4.0f)
            ));
    private static MetalType register(String id, int color,
                                      float dur, float tough, float hard,
                                      float ench, float weight,
                                      AbstractModBlastFurnaceBlock.HeatLevel heat, float totalHeat,
                                      float rAdd, float rSub,
                                      Map<Integer, MetalBonus> bonuses) {
        return Registry.register(METAL_TYPE, Identifier.of(MITENewWorld.MOD_ID, id), new MetalType(id, color, dur, tough, hard, ench, weight, heat, totalHeat, rAdd, rSub, bonuses));
    }

    public static MetalType byId(String id) {
        return METAL_TYPE.get(Identifier.of(MITENewWorld.MOD_ID, id));
    }

    public static void registerModAlloyMetals() {}


    public record MetalType(
            String id,
            int color,
            float durability,
            float toughness,
            float hardness,
            float enchantRarity,
            /** 单锭绝对重量，铁锭 = 35 */
            float weight,
            AbstractModBlastFurnaceBlock.HeatLevel requiredHeat,
            float totalHeat,
            float resistanceAdd,
            float resistanceSub,
            Map<Integer, MetalBonus> bonuses
    ) {

    }
    public enum Stat { DURABILITY, TOUGHNESS, HARDNESS, ENCHANT_RARITY }

    public record MetalBonus(Stat stat, float value) {}
}
