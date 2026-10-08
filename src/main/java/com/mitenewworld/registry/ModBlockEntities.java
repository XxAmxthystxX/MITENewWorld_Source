package com.mitenewworld.registry;
import com.mitenewworld.entity.blockentity.AlloyBlockEntity;
import com.mitenewworld.entity.blockentity.portal.WorldBreakerBlockEntity;
import com.mitenewworld.entity.blockentity.portal.WorldBreakerBlockEntity;
import com.mitenewworld.entity.blockentity.craftingtable.ModCraftingTableEntity;
import com.mitenewworld.entity.blockentity.furnace.ModFurnaceEntity;

import com.mitenewworld.MITENewWorld;
import com.mitenewworld.registry.ModBlocks;
import com.mitenewworld.entity.blockentity.alloyfurnace.ModBlastFurnaceEntity;
import com.mitenewworld.entity.blockentity.castingtable.ModCastingTableEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.Optional;
import java.util.function.Supplier;


public class ModBlockEntities {

    public static final BlockEntityType<ModCraftingTableEntity> MOD_CRAFTING_TABLE_ENTITY = createBlockEntityType("mod_crafting_table_entity", FabricBlockEntityTypeBuilder.create(ModCraftingTableEntity::new, ModBlocks.MOD_FLINT_CRAFTING_TABLE_BLOCK, ModBlocks.MOD_COPPER_CRAFTING_TABLE_BLOCK, ModBlocks.MOD_SILVER_CRAFTING_TABLE_BLOCK, ModBlocks.MOD_GOLD_CRAFTING_TABLE_BLOCK, ModBlocks.MOD_TIN_CRAFTING_TABLE_BLOCK, ModBlocks.MOD_ALUMINIUM_CRAFTING_TABLE_BLOCK, ModBlocks.MOD_IRON_CRAFTING_TABLE_BLOCK, ModBlocks.MOD_TITANIUM_CRAFTING_TABLE_BLOCK, ModBlocks.MOD_MITHRIL_CRAFTING_TABLE_BLOCK, ModBlocks.MOD_PLATINUM_CRAFTING_TABLE_BLOCK, ModBlocks.MOD_IRIDIUM_CRAFTING_TABLE_BLOCK, ModBlocks.MOD_STARLIGHT_CRAFTING_TABLE_BLOCK, ModBlocks.MOD_ANCIENT_METAL_CRAFTING_TABLE_BLOCK, ModBlocks.MOD_ADAMANTIUM_CRAFTING_TABLE_BLOCK).build());

    public static final BlockEntityType<ModFurnaceEntity> MOD_FURNACE_ENTITY = createBlockEntityType("mod_furnace_entity", FabricBlockEntityTypeBuilder.create(ModFurnaceEntity::new, ModBlocks.MOD_CLAY_FURNACE_BLOCK, ModBlocks.MOD_LARGE_CLAY_FURNACE_BLOCK, ModBlocks.MOD_SANDSTONE_FURNACE_BLOCK, ModBlocks.MOD_STONE_FURNACE_BLOCK, ModBlocks.MOD_OBSIDIAN_FURNACE_BLOCK, ModBlocks.MOD_NETHERRACK_FURNACE_BLOCK).build());


    public static final BlockEntityType<WorldBreakerBlockEntity> WORLD_BREAKER_BLOCK_ENTITY = createBlockEntityType("world_breaker_block_entity", FabricBlockEntityTypeBuilder.create(WorldBreakerBlockEntity::new, ModBlocks.WORLD_BREAKER).build());

    public static final BlockEntityType<ModBlastFurnaceEntity> MOD_BLAST_FURNACE_ENTITY = createBlockEntityType("mod_alloy_furnace_entity", FabricBlockEntityTypeBuilder.create(ModBlastFurnaceEntity::new, ModBlocks.SMOOTH_STONE_BRICK_SMELTING_CORE, ModBlocks.OBSIDIAN_BRICK_SMELTING_CORE, ModBlocks.BLACKSTONE_BRICK_SMELTING_CORE, ModBlocks.NETHERITE_BRICK_SMELTING_CORE, ModBlocks.QUARTZ_BRICK_SMELTING_CORE).build());

    public static final BlockEntityType<ModCastingTableEntity> MOD_CASTING_TABLE_ENTITY = createBlockEntityType("mod_casting_table_entity", FabricBlockEntityTypeBuilder.create(ModCastingTableEntity::new, ModBlocks.allCastingTables()).build());

    public static final BlockEntityType<AlloyBlockEntity> ALLOY_BLOCK_ENTITY = createBlockEntityType("alloy_block_entity", FabricBlockEntityTypeBuilder.create(AlloyBlockEntity::new, ModBlocks.ALLOY_BLOCK).build());








    public static <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(String id, BlockEntityType<T> builder) {
        Util.getChoiceType(TypeReferences.BLOCK_ENTITY, id);
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(MITENewWorld.MOD_ID, id), builder);
    }



    public static void registerBlockEntities() {
    }
}
