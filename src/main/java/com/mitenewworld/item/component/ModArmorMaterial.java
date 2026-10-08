package com.mitenewworld.item.component;
import com.mitenewworld.MITENewWorld;

import com.google.common.collect.Maps;
import com.mitenewworld.item.metal.ModArmorItem;
import com.mitenewworld.tags.ModItemTags;
import net.minecraft.item.Item;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.Map;

@Deprecated
public record ModArmorMaterial(int durability, Map<ModArmorItem.ArmorType, Integer> defense, int enchantmentValue, RegistryEntry<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> repairIngredient, RegistryKey<EquipmentAsset> assetId) {
    public static ModArmorMaterial LEATHER = new  ModArmorMaterial(
            100,
            ModArmorMaterial.createDefenseMap(1 , 1 , 1 , 1 , 3),
            5,
            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
            0.0f,
            0.0f,
            ItemTags.REPAIRS_LEATHER_ARMOR,
            EquipmentAssetKeys.LEATHER
    );
    public static ModArmorMaterial COPPER = new ModArmorMaterial(
            600,
            ModArmorMaterial.createDefenseMap(1 , 3 , 5 , 2 , 10),
            8, SoundEvents.ITEM_ARMOR_EQUIP_COPPER,
            0.8f,
            0.4f,
            ModItemTags.REPAIRS_COPPER,
            ModEquipmentAssetKeys.COPPER
    );
    public static ModArmorMaterial COPPER_CHAIN = new   ModArmorMaterial(
            250,
            ModArmorMaterial.createDefenseMap(1 , 2 , 2 , 1 , 4),
            8,
            SoundEvents.ITEM_ARMOR_EQUIP_COPPER,
            0.4f,
            0.0f,
            ModItemTags.REPAIRS_COPPER_CHAIN,
            ModEquipmentAssetKeys.COPPER_CHAIN
    );
    public static ModArmorMaterial SILVER = new   ModArmorMaterial(
            400,
            ModArmorMaterial.createDefenseMap(2 , 4 , 6 , 3 , 12),
            12,
            SoundEvents.ITEM_ARMOR_EQUIP_COPPER,
            0.4f,
            0.4f,
            ModItemTags.REPAIRS_SILVER,
            ModEquipmentAssetKeys.SILVER
    );
    public static ModArmorMaterial SILVER_CHAIN = new   ModArmorMaterial(
            200,
            ModArmorMaterial.createDefenseMap(2 , 3 , 3 , 2 , 6),
            10,
            SoundEvents.ITEM_ARMOR_EQUIP_COPPER,
            0.0f,
            0.0f,
            ModItemTags.REPAIRS_SILVER_CHAIN,
            ModEquipmentAssetKeys.SILVER_CHAIN
    );
    public static ModArmorMaterial GOLD = new   ModArmorMaterial(
            300,
            ModArmorMaterial.createDefenseMap(2 , 2 , 2 , 2 , 10),
            14,
            SoundEvents.ITEM_ARMOR_EQUIP_COPPER,
            0.0f,
            0.0f,
            ModItemTags.REPAIRS_GOLD,
            ModEquipmentAssetKeys.GOLD
    );
    public static ModArmorMaterial GOLD_CHAIN = new   ModArmorMaterial(
            150,
            ModArmorMaterial.createDefenseMap(1 , 1 , 1 , 1 , 2),
            12,
            SoundEvents.ITEM_ARMOR_EQUIP_COPPER,
            0.0f,
            0.0f,
            ModItemTags.REPAIRS_GOLD,
            ModEquipmentAssetKeys.GOLD_CHAIN
    );
    public static ModArmorMaterial RUSTED_IRON = new   ModArmorMaterial(
            300,
            ModArmorMaterial.createDefenseMap(1 , 1 , 2 , 1, 3),
            4,
            SoundEvents.ITEM_ARMOR_EQUIP_COPPER,
            0.4f,
            0.0f,
            null,
            ModEquipmentAssetKeys.RUSTED_IRON
    );
    public static ModArmorMaterial RUSTED_IRON_CHAIN = new   ModArmorMaterial(
            50,
            ModArmorMaterial.createDefenseMap(1 , 1 , 1 , 1 , 1),
            2,
            SoundEvents.ITEM_ARMOR_EQUIP_COPPER,
            0.0f,
            0.0f,
            null,
            ModEquipmentAssetKeys.RUSTED_IRON_CHAIN
    );
    public static ModArmorMaterial IRON = new   ModArmorMaterial(
            1000,
            ModArmorMaterial.createDefenseMap(4 , 7 , 10 , 5 , 20),
            15,
            SoundEvents.ITEM_ARMOR_EQUIP_COPPER,
            2.0f,
            1.2f,
            ModItemTags.REPAIRS_IRON,
            ModEquipmentAssetKeys.IRON
    );
    public static ModArmorMaterial IRON_CHAIN = new   ModArmorMaterial(
            650,
            ModArmorMaterial.createDefenseMap(3 , 5 , 8 , 4 , 12),
            12,
            SoundEvents.ITEM_ARMOR_EQUIP_COPPER,
            0.8f,
            0.4f,
            ModItemTags.REPAIRS_IRON_CHAIN,
            ModEquipmentAssetKeys.IRON_CHAIN
    );
    public static ModArmorMaterial MITHRIL = new   ModArmorMaterial(
            4800,
            ModArmorMaterial.createDefenseMap(5 , 8 , 11 , 7 , 27),
            18,
            SoundEvents.ITEM_ARMOR_EQUIP_COPPER,
            4.0f,
            1.2f,
            ModItemTags.REPAIRS_MITHRIL,
            ModEquipmentAssetKeys.MITHRIL
    );
    public static ModArmorMaterial MITHRIL_CHAIN = new   ModArmorMaterial(
            3000,
            ModArmorMaterial.createDefenseMap(4 , 6 ,  9 , 5 , 21),
            14,
            SoundEvents.ITEM_ARMOR_EQUIP_COPPER,
            2.8f,
            0.8f,
            ModItemTags.REPAIRS_MITHRIL_CHAIN,
            ModEquipmentAssetKeys.MITHRIL_CHAIN
    );
    public static ModArmorMaterial ANCIENT_METAL = new   ModArmorMaterial(
            16000, ModArmorMaterial.createDefenseMap(6 , 9 , 13 , 7 , 32),
            20,
            SoundEvents.ITEM_ARMOR_EQUIP_COPPER,
            4.8f,
            2.0f,
            ModItemTags.REPAIRS_ANCIENT_METAL,
            ModEquipmentAssetKeys.ANCIENT_METAL
    );
    public static ModArmorMaterial ANCIENT_METAL_CHAIN = new   ModArmorMaterial(
            13500,
            ModArmorMaterial.createDefenseMap(5 , 7 , 11 , 7 , 28),
            12,
            SoundEvents.ITEM_ARMOR_EQUIP_COPPER,
            4.0f,
            1.8f,
            ModItemTags.REPAIRS_ANCIENT_METAL_CHAIN,
            ModEquipmentAssetKeys.ANCIENT_METAL_CHAIN
    );
    public static ModArmorMaterial ADAMANTIUM = new   ModArmorMaterial(
            42000,
            ModArmorMaterial.createDefenseMap(7 , 11 , 14 ,8 , 40),
            22,
            SoundEvents.ITEM_ARMOR_EQUIP_COPPER,
            2.5f,
            4.0f,
            ModItemTags.REPAIRS_ADAMANTIUM,
            ModEquipmentAssetKeys.ADAMANTIUM
    );
    public static  ModArmorMaterial ADAMANTIUM_CHAIN = new ModArmorMaterial(
            30000,
            ModArmorMaterial.createDefenseMap(5 , 9 , 11 ,5 , 30),
            16,
            SoundEvents.ITEM_ARMOR_EQUIP_COPPER,
            2.0f,
            2.0f,
            ModItemTags.REPAIRS_ADAMANTIUM_CHAIN,
            ModEquipmentAssetKeys.ADAMANTIUM_CHAIN
    );

    private static Map<ModArmorItem.ArmorType, Integer> createDefenseMap(int boots, int leggings , int chestplate, int helmet ,int body) {
        return Maps.newEnumMap(Map.of(ModArmorItem.ArmorType.BOOTS, boots, ModArmorItem.ArmorType.LEGGINGS, leggings, ModArmorItem.ArmorType.CHESTPLATE, chestplate, ModArmorItem.ArmorType.HELMET, helmet, ModArmorItem.ArmorType.BODY, body));
    }
}
