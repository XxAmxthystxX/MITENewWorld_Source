package com.mitenewworld;

import com.mitenewworld.recipe.casting.ModCastingTemplates;
import com.mitenewworld.registry.ModBlocks;
import com.mitenewworld.core.FuelMap;
import com.mitenewworld.registry.ModBlockEntities;
import com.mitenewworld.item.ModFoodItems;
import com.mitenewworld.registry.ModItemGroups;
import com.mitenewworld.registry.ModItems;
import com.mitenewworld.screen.tooltip.AbstractAlloyTooltipComponent;
import com.mitenewworld.item.component.ModDataComponentTypes;
import com.mitenewworld.item.component.ModTooltipData;
import com.mitenewworld.item.metal.ModAlloyMetals;
import com.mitenewworld.item.metal.ModSynergies;
import com.mitenewworld.registry.ModEntities;
import com.mitenewworld.loot.ModLootFunctions;
import com.mitenewworld.network.MinigameClickC2SPacket;
import com.mitenewworld.network.StateUpdateS2CPacket;
import com.mitenewworld.recipe.ModRecipeTypes;
import com.mitenewworld.screen.ModScreenHandlers;
import com.mitenewworld.tags.ModBlockTags;
import com.mitenewworld.tags.ModItemTags;
import com.mitenewworld.loot.ModLootTables;
import com.mitenewworld.world.ModBiomes;
import com.mitenewworld.world.ModOreFeatures;
import com.mitenewworld.world.ModDimensionTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MITENewWorld implements ModInitializer {
    public static final String MOD_ID = "mitenewworld";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {

        ModAlloyMetals.registerModAlloyMetals();
        ModSynergies.registerModSynergies();

        ModDataComponentTypes.registerModDataComponentTypes();

        ModBlocks.registerModBlocks();

        ModItems.registerModItem();
        ModFoodItems.registerModFoodItems();

        FuelMap.registerModFuels();

        ModItemGroups.registerModItemGroup();

        ModBlockTags.registerModBlockTags();
        ModItemTags.registerModItemTags();

        ModLootTables.registerLootTables();
        ModLootFunctions.register();

        ModRecipeTypes.registerRecipes();

        ModBlockEntities.registerBlockEntities();
        ModCastingTemplates.init();

        ModScreenHandlers.registerScreenHandlers();

        ModOreFeatures.init();
        ModBiomes.registerBiomes();
        ModDimensionTypes.registerModDimensionTypes();

        ModEntities.registerEntities();
        ModEntities.registerDefaultAttribute();
        ModEntities.registerSpawnRestriction();

        PayloadTypeRegistry.playS2C().register(StateUpdateS2CPacket.ID, StateUpdateS2CPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(MinigameClickC2SPacket.ID, MinigameClickC2SPacket.CODEC);

        ModOreFeatures.registerBiomeFeatures();

        TooltipComponentCallback.EVENT.register((tooltipData) -> {
            if (tooltipData instanceof ModTooltipData(AbstractAlloyTooltipComponent tooltipComponent)) {
                return tooltipComponent;
            }
            return null;
        });

        LOGGER.info("MITE New World has been initialized.");
    }




}
