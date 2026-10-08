package com.mitenewworld.item.component;

import com.mitenewworld.MITENewWorld;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

@Deprecated
public interface ModEquipmentAssetKeys {

    RegistryKey<EquipmentAsset> COPPER = ModEquipmentAssetKeys.register("copper");
    RegistryKey<EquipmentAsset> COPPER_CHAIN = ModEquipmentAssetKeys.register("copper_chain");
    RegistryKey<EquipmentAsset> SILVER = ModEquipmentAssetKeys.register("silver");
    RegistryKey<EquipmentAsset> SILVER_CHAIN = ModEquipmentAssetKeys.register("silver_chain");
    RegistryKey<EquipmentAsset> GOLD = ModEquipmentAssetKeys.register("gold");
    RegistryKey<EquipmentAsset> GOLD_CHAIN = ModEquipmentAssetKeys.register("gold_chain");
    RegistryKey<EquipmentAsset> RUSTED_IRON = ModEquipmentAssetKeys.register("rusted_iron");
    RegistryKey<EquipmentAsset> RUSTED_IRON_CHAIN = ModEquipmentAssetKeys.register("rusted_iron_chain");
    RegistryKey<EquipmentAsset> IRON = ModEquipmentAssetKeys.register("iron");
    RegistryKey<EquipmentAsset> IRON_CHAIN = ModEquipmentAssetKeys.register("iron_chain");
    RegistryKey<EquipmentAsset> MITHRIL = ModEquipmentAssetKeys.register("mithril");
    RegistryKey<EquipmentAsset> MITHRIL_CHAIN = ModEquipmentAssetKeys.register("mithril_chain");
    RegistryKey<EquipmentAsset> ANCIENT_METAL = ModEquipmentAssetKeys.register("ancient_metal");
    RegistryKey<EquipmentAsset> ANCIENT_METAL_CHAIN = ModEquipmentAssetKeys.register("ancient_metal_chain");
    RegistryKey<EquipmentAsset> ADAMANTIUM = ModEquipmentAssetKeys.register("adamantium");
    RegistryKey<EquipmentAsset> ADAMANTIUM_CHAIN = ModEquipmentAssetKeys.register("adamantium_chain");
    RegistryKey<EquipmentAsset> ALLOY_ARMOR = ModEquipmentAssetKeys.register("alloy_armor");
    RegistryKey<EquipmentAsset> ALLOY_CHAIN = ModEquipmentAssetKeys.register("alloy_chain");
    static RegistryKey<EquipmentAsset> register(String name) {
        return RegistryKey.of(EquipmentAssetKeys.REGISTRY_KEY, Identifier.of(MITENewWorld.MOD_ID ,name));
    }


}
