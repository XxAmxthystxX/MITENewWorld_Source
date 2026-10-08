package com.mitenewworld.registry;
import com.mitenewworld.item.ModFoodItems;
import com.mitenewworld.item.metal.ModDefaultAlloys;

import com.mitenewworld.MITENewWorld;
import com.mitenewworld.block.ModOres;
import com.mitenewworld.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup toolsgroup = Registry.register(Registries.ITEM_GROUP, Identifier.of(MITENewWorld.MOD_ID,"toolsgroup"),
            ItemGroup.create(null, 7).displayName(Text.translatable("itemGroup.buildingBlocks"))
                    .icon(() -> new ItemStack(Items.WOODEN_AXE))
                    .entries((displayContext, entries) -> {
                       // entries.add(ModItems.ADAMANTIUM_LONG_SWORD);
                        entries.add(ModItems.WOODEN_CULB);
                        entries.add(ModItems.WOODEN_CUDGEL);

                        entries.add(ModItems.ALLOY_AXE);
                        entries.add(ModItems.ALLOY_BATTLEAXE);
                        entries.add(ModItems.ALLOY_PICKAXE);
                        entries.add(ModItems.ALLOY_SHOVEL);
                        entries.add(ModItems.ALLOY_HOE);
                        entries.add(ModItems.ALLOY_HATCHET);
                        entries.add(ModItems.ALLOY_SWORD);
                        entries.add(ModItems.ALLOY_DAGGER);
                        entries.add(ModItems.ALLOY_MATTOCK);
                        entries.add(ModItems.ALLOY_WARHAMMER);
                        entries.add(ModItems.ALLOY_SCYTHE);
                        entries.add(ModItems.ALLOY_SHEARS);
                        entries.add(ModItems.ALLOY_CASTING_HAMMER);

                        // 各金属 100% 的默认合金工具（13 金属 × 13 件）
                        for (ItemStack stack : ModDefaultAlloys.tools()) {
                            entries.add(stack);
                        }

                        entries.add(ModItems.RUSTED_IRON_WARHAMMER);
                        entries.add(ModItems.RUSTED_IRON_SWORD);
                        entries.add(ModItems.RUSTED_IRON_SHOVEL);
                        entries.add(ModItems.RUSTED_IRON_SHEARS);
                        entries.add(ModItems.RUSTED_IRON_SCYTHE);
                        entries.add(ModItems.RUSTED_IRON_DAGGER);
                        entries.add(ModItems.RUSTED_IRON_MATTOCK);
                        entries.add(ModItems.RUSTED_IRON_PICKAXE);
                        entries.add(ModItems.RUSTED_IRON_AXE);
                        entries.add(ModItems.RUSTED_IRON_BATTLEAXE);
                        entries.add(ModItems.RUSTED_IRON_HOE);
                        entries.add(ModItems.RUSTED_IRON_HATCHET);

                        entries.add(ModItems.WATER_ADAMANTIUM_BUCKET);
                        entries.add(ModItems.WATER_ANCIENT_METAL_BUCKET);
                        entries.add(ModItems.WATER_MITHRIL_BUCKET);
                        entries.add(ModItems.WATER_GOLD_BUCKET);
                        entries.add(ModItems.WATER_IRON_BUCKET);
                        entries.add(ModItems.WATER_SILVER_BUCKET);
                        entries.add(ModItems.WATER_COPPER_BUCKET);
                        entries.add(ModItems.ADAMANTIUM_BUCKET);
                        entries.add(ModItems.ANCIENT_METAL_BUCKET);
                        entries.add(ModItems.MITHRIL_BUCKET);
                        entries.add(ModItems.GOLD_BUCKET);
                        entries.add(ModItems.IRON_BUCKET);
                        entries.add(ModItems.SILVER_BUCKET);
                        entries.add(ModItems.COPPER_BUCKET);
                        entries.add(ModItems.LAVA_ADAMANTIUM_BUCKET);
                        entries.add(ModItems.LAVA_ANCIENT_METAL_BUCKET);
                        entries.add(ModItems.LAVA_MITHRIL_BUCKET);
                        entries.add(ModItems.LAVA_GOLD_BUCKET);
                        entries.add(ModItems.LAVA_IRON_BUCKET);
                        entries.add(ModItems.LAVA_SILVER_BUCKET);
                        entries.add(ModItems.LAVA_COPPER_BUCKET);
                        entries.add(ModItems.STONE_COPPER_BUCKET);
                        entries.add(ModItems.STONE_SILVER_BUCKET);
                        entries.add(ModItems.STONE_GOLD_BUCKET);
                        entries.add(ModItems.STONE_IRON_BUCKET);
                        entries.add(ModItems.STONE_MITHRIL_BUCKET);
                        entries.add(ModItems.STONE_ANCIENT_METAL_BUCKET);
                        entries.add(ModItems.STONE_ADAMANTIUM_BUCKET);




                    }).build());

    public static final ItemGroup blockgroup = Registry.register(Registries.ITEM_GROUP, Identifier.of(MITENewWorld.MOD_ID,"blockgroup"),
            ItemGroup.create(null, 8).displayName(Text.translatable("itemGroup.buildingBlocks"))
                    .icon(() -> new ItemStack(ModBlocks.SILVER_ORE))
                    .entries((displayContext, entries) -> {
                        entries.add(ModBlocks.COAL_ORE);
                        entries.add(ModBlocks.DEEPSLATE_COAL_ORE);
                        entries.add(ModBlocks.COPPER_ORE);
                        entries.add(ModBlocks.DEEPSLATE_COPPER_ORE);
                        entries.add(ModBlocks.GOLD_ORE);
                        entries.add(ModBlocks.DEEPSLATE_GOLD_ORE);
                        entries.add(ModBlocks.SILVER_ORE);
                        entries.add(ModBlocks.DEEPSLATE_SILVER_ORE);
                        entries.add(ModBlocks.IRON_ORE);
                        entries.add(ModBlocks.DEEPSLATE_IRON_ORE);
                        entries.add(ModBlocks.MITHRIL_ORE);
                        entries.add(ModBlocks.ADAMANTIUM_ORE);
                        entries.add(ModBlocks.IRON_BLOCK);
                        entries.add(ModBlocks.SILVER_BLOCK);
                        entries.add(ModBlocks.GOLD_BLOCK);
                        entries.add(ModBlocks.COPPER_BLOCK);
                        entries.add(ModBlocks.ADAMANTIUM_BLOCK);
                        entries.add(ModBlocks.ANCIENT_METAL_BLOCK);
                        entries.add(ModBlocks.MITHRIL_BLOCK);
                        entries.add(ModBlocks.GOLD_ORE_NETHERRACK);
        entries.add(ModBlocks.MOD_FLINT_CRAFTING_TABLE_BLOCK);
        entries.add(ModBlocks.MOD_COPPER_CRAFTING_TABLE_BLOCK);
        entries.add(ModBlocks.MOD_TIN_CRAFTING_TABLE_BLOCK);
        entries.add(ModBlocks.MOD_GOLD_CRAFTING_TABLE_BLOCK);
        entries.add(ModBlocks.MOD_SILVER_CRAFTING_TABLE_BLOCK);
        entries.add(ModBlocks.MOD_ALUMINIUM_CRAFTING_TABLE_BLOCK);
        entries.add(ModBlocks.MOD_IRON_CRAFTING_TABLE_BLOCK);
        entries.add(ModBlocks.MOD_TITANIUM_CRAFTING_TABLE_BLOCK);
        entries.add(ModBlocks.MOD_MITHRIL_CRAFTING_TABLE_BLOCK);
        entries.add(ModBlocks.MOD_PLATINUM_CRAFTING_TABLE_BLOCK);
        entries.add(ModBlocks.MOD_IRIDIUM_CRAFTING_TABLE_BLOCK);
        entries.add(ModBlocks.MOD_STARLIGHT_CRAFTING_TABLE_BLOCK);
        entries.add(ModBlocks.MOD_ANCIENT_METAL_CRAFTING_TABLE_BLOCK);
        entries.add(ModBlocks.MOD_ADAMANTIUM_CRAFTING_TABLE_BLOCK);
                        entries.add(ModBlocks.MOD_CLAY_FURNACE_BLOCK);
                        entries.add(ModBlocks.MOD_LARGE_CLAY_FURNACE_BLOCK);
                        entries.add(ModBlocks.MOD_SANDSTONE_FURNACE_BLOCK);
                        entries.add(ModBlocks.MOD_STONE_FURNACE_BLOCK);
                        entries.add(ModBlocks.MOD_OBSIDIAN_FURNACE_BLOCK);
                        entries.add(ModBlocks.MOD_NETHERRACK_FURNACE_BLOCK);
                        entries.add(ModBlocks.RAW_COPPER_BLOCK);
                        entries.add(ModBlocks.RAW_SILVER_BLOCK);
                        entries.add(ModBlocks.RAW_GOLD_BLOCK);
                        entries.add(ModBlocks.RAW_IRON_BLOCK);
                        entries.add(ModBlocks.RAW_MITHRIL_BLOCK);
                        entries.add(ModBlocks.RAW_ADAMANTIUM_BLOCK);
                        entries.add(ModBlocks.VOID_GATE);
                        entries.add(ModBlocks.WORLD_BREAKER);
                        entries.add(ModBlocks.UNDERWORLD_DIRT);
                        entries.add(ModBlocks.UNDERWORLD_GRASS);
                        entries.add(ModBlocks.UNDERWORLD_COBBLESTONE);
                        entries.add(ModBlocks.UNDERWORLD_STONE);
                        entries.add(ModBlocks.EROSION_LEAVES);
                        entries.add(ModBlocks.EROSION_LOG);
                        // 合金炉（5 档：外壳 / 原料口 / 产物口 / 熔炼核心）与铸造台
                        entries.add(ModBlocks.SMOOTH_STONE_BRICK);
                        entries.add(ModBlocks.SMOOTH_STONE_BRICK_INPUT_PORT);
                        entries.add(ModBlocks.SMOOTH_STONE_BRICK_OUTPUT_PORT);
                        entries.add(ModBlocks.SMOOTH_STONE_BRICK_SMELTING_CORE);
                        entries.add(ModBlocks.OBSIDIAN_BRICK);
                        entries.add(ModBlocks.OBSIDIAN_BRICK_INPUT_PORT);
                        entries.add(ModBlocks.OBSIDIAN_BRICK_OUTPUT_PORT);
                        entries.add(ModBlocks.OBSIDIAN_BRICK_SMELTING_CORE);
                        entries.add(ModBlocks.BLACKSTONE_BRICK);
                        entries.add(ModBlocks.BLACKSTONE_BRICK_INPUT_PORT);
                        entries.add(ModBlocks.BLACKSTONE_BRICK_OUTPUT_PORT);
                        entries.add(ModBlocks.BLACKSTONE_BRICK_SMELTING_CORE);
                        entries.add(ModBlocks.QUARTZ_BRICK);
                        entries.add(ModBlocks.QUARTZ_BRICK_INPUT_PORT);
                        entries.add(ModBlocks.QUARTZ_BRICK_OUTPUT_PORT);
                        entries.add(ModBlocks.QUARTZ_BRICK_SMELTING_CORE);
                        entries.add(ModBlocks.NETHERITE_BRICK);
                        entries.add(ModBlocks.NETHERITE_BRICK_INPUT_PORT);
                        entries.add(ModBlocks.NETHERITE_BRICK_OUTPUT_PORT);
                        entries.add(ModBlocks.NETHERITE_BRICK_SMELTING_CORE);
                        entries.add(ModBlocks.MOD_FLINT_CASTING_TABLE);
                        entries.add(ModBlocks.MOD_CASTING_TABLE);
                        // 13 种金属的铸造台
                        for (Block castingTable : ModBlocks.metalCastingTables().values()) {
                            entries.add(castingTable);
                        }
                        entries.add(ModOres.MOD_STONE);
                        for (var ore : ModOres.ALL_ORES) {
                            entries.add(ore);
                        }




                    }).build());
    public static final ItemGroup foodsgroup = Registry.register(Registries.ITEM_GROUP, Identifier.of(MITENewWorld.MOD_ID,"foodsgroup"),
            ItemGroup.create(null, 9).displayName(Text.translatable("itemGroup.buildingBlocks"))
                    .icon(() -> new ItemStack(ModFoodItems.SALAD))
                    .entries((displayContext, entries) -> {
                        entries.add(ModFoodItems.SALAD);
                        entries.add(ModFoodItems.BLUE_BERRIES);
                        entries.add(ModFoodItems.PORRIDGE);
                        entries.add(ModFoodItems.BOWL_OF_MILK);
                        entries.add(ModFoodItems.CEREAL);
                        entries.add(ModFoodItems.CHOCOLATE);
                        entries.add(ModFoodItems.PUMPKIN_SOUP);
                        entries.add(ModFoodItems.CREAM_OF_MUSHROOM_SOUP);
                        entries.add(ModFoodItems.VEGETABLE_SOUP);
                        entries.add(ModFoodItems.CHICKEN_SOUP);
                        entries.add(ModFoodItems.BEEF_STEW);
                        entries.add(ModFoodItems.ORANGE);
                        entries.add(ModFoodItems.SORBET);
                        entries.add(ModFoodItems.CHEESE);
                        entries.add(ModFoodItems.MASHED_POTATO);
                        entries.add(ModFoodItems.ICE_CREAM);
                        entries.add(ModFoodItems.DOUGH);
                        entries.add(ModFoodItems.BANANA);
                        entries.add(ModFoodItems.ONION);
                        entries.add(ModFoodItems.MILK_COPPER_BUCKET);
                        entries.add(ModFoodItems.MILK_SILVER_BUCKET);
                        entries.add(ModFoodItems.MILK_GOLD_BUCKET);
                        entries.add(ModFoodItems.MILK_IRON_BUCKET);
                        entries.add(ModFoodItems.MILK_MITHRIL_BUCKET);
                        entries.add(ModFoodItems.MILK_ANCIENT_METAL_BUCKET);
                        entries.add(ModFoodItems.MILK_ADAMANTIUM_BUCKET);
                        entries.add(ModFoodItems.WATER_BOWL);
                        entries.add(ModFoodItems.HORSE_MEAT);


                    }).build());
    public static final ItemGroup IngredientsGroup = Registry.register(Registries.ITEM_GROUP, Identifier.of(MITENewWorld.MOD_ID,"ingredientsgroup"),
            ItemGroup.create(null, 9).displayName(Text.translatable("itemGroup.buildingBlocks"))
                    .icon(() -> new ItemStack(ModItems.ALLOY_INGOT))
                    .entries((displayContext, entries) -> {
                        // 粗矿（13 种）
                        entries.add(ModItems.RAW_COPPER);
                        entries.add(ModItems.RAW_SILVER);
                        entries.add(ModItems.RAW_GOLD);
                        entries.add(ModItems.RAW_IRON);
                        entries.add(ModItems.RAW_TITANIUM);
                        entries.add(ModItems.RAW_MITHRIL);
                        entries.add(ModItems.RAW_ANCIENT_METAL);
                        entries.add(ModItems.RAW_ADAMANTIUM);
                        entries.add(ModItems.RAW_TIN);
                        entries.add(ModItems.RAW_ALUMINIUM);
                        entries.add(ModItems.RAW_PLATINUM);
                        entries.add(ModItems.RAW_IRIDIUM);
                        entries.add(ModItems.RAW_STARLIGHT);
                        // 合金形态（4 种）
                        entries.add(ModItems.ALLOY_INGOT);
                        entries.add(ModItems.ALLOY_NUGGET);
                        entries.add(ModItems.ALLOY_BLOCK);
                        entries.add(ModItems.ALLOY_CHAIN);
                        // 各金属 100% 的默认材料（13 金属 × 4 形态）
                        for (ItemStack stack : ModDefaultAlloys.materials()) {
                            entries.add(stack);
                        }
                        // 废金属与析金粉
                        entries.add(ModItems.SCRAP_METAL);
                        entries.add(ModItems.REFINING_POWDER);
                        entries.add(ModItems.CALCITE_FRAGMENT);
                        // 材料
                        entries.add(ModItems.FLINT_FRAGMENT);
                        entries.add(ModItems.EMERALD_FRAGMENT);
                        entries.add(ModItems.GLASS_FRAGMENT);
                        entries.add(ModItems.DIAMOND_FRAGMENT);
                        entries.add(ModItems.NETHER_QUARTZ_FRAGMENT);
                        entries.add(ModItems.OBSIDIAN_FRAGMENT);
                        entries.add(ModItems.SINEW);
                        // 工具箱（13 金属各一）
                        entries.add(ModItems.COPPER_TOOLBOX);
                        entries.add(ModItems.SILVER_TOOLBOX);
                        entries.add(ModItems.IRON_TOOLBOX);
                        entries.add(ModItems.TITANIUM_TOOLBOX);
                        entries.add(ModItems.MITHRIL_TOOLBOX);
                        entries.add(ModItems.ADAMANTIUM_TOOLBOX);
                        entries.add(ModItems.ANCIENT_METAL_TOOLBOX);
                        entries.add(ModItems.TIN_TOOLBOX);
                        entries.add(ModItems.GOLD_TOOLBOX);
                        entries.add(ModItems.ALUMINIUM_TOOLBOX);
                        entries.add(ModItems.PLATINUM_TOOLBOX);
                        entries.add(ModItems.IRIDIUM_TOOLBOX);
                        entries.add(ModItems.STARLIGHT_TOOLBOX);
                    }).build());
    public static final ItemGroup ArmorsGroup = Registry.register(Registries.ITEM_GROUP, Identifier.of(MITENewWorld.MOD_ID,"armorsgroup"),
            ItemGroup.create(null, 9).displayName(Text.translatable("itemGroup.buildingBlocks"))
                    .icon(() -> new ItemStack(Items.IRON_HELMET))
                    .entries((displayContext, entries) -> {
                        // 板甲
                        entries.add(ModItems.ALLOY_HELMET);
                        entries.add(ModItems.ALLOY_CHESTPLATE);
                        entries.add(ModItems.ALLOY_LEGGINGS);
                        entries.add(ModItems.ALLOY_BOOTS);
                        entries.add(ModItems.ALLOY_BODY);
                        // 锁链甲
                        entries.add(ModItems.ALLOY_CHAIN_HELMET);
                        entries.add(ModItems.ALLOY_CHAIN_CHESTPLATE);
                        entries.add(ModItems.ALLOY_CHAIN_LEGGINGS);
                        entries.add(ModItems.ALLOY_CHAIN_BOOTS);
                        entries.add(ModItems.ALLOY_CHAIN_BODY);
                        // 各金属 100% 的默认护甲（13 金属 × 10 件）
                        for (ItemStack stack : ModDefaultAlloys.armors()) {
                            entries.add(stack);
                        }
                    }).build());



    public static void registerModItemGroup() {
        MITENewWorld.LOGGER.info("Registering Item Groups");
    }
}


