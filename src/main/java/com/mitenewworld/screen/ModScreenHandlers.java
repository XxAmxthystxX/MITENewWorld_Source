package com.mitenewworld.screen;
import com.mitenewworld.screen.alloyfurnace.ModBlastFurnaceScreenHandler;
import com.mitenewworld.screen.castingtable.ModCastingTableScreenHandler;
import com.mitenewworld.screen.craftingtable.ModCraftingTableScreenHandler;
import com.mitenewworld.screen.furnace.ModFurnaceScreenHandler;

import com.mitenewworld.MITENewWorld;
import com.mitenewworld.data.ModBlastFurnaceBlockData;
import com.mitenewworld.data.ModCraftingTableBlockData;
import com.mitenewworld.data.ModFurnaceBlockData;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModScreenHandlers {
    public static final ScreenHandlerType<ModCraftingTableScreenHandler> MOD_CRAFTING_TABLE_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(MITENewWorld.MOD_ID,"mod_crafting_table_screen_handler"), new ExtendedScreenHandlerType<>(ModCraftingTableScreenHandler::new , ModCraftingTableBlockData.CODEC));
    public static final ScreenHandlerType<ModFurnaceScreenHandler> MOD_FURNACE_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(MITENewWorld.MOD_ID, "mod_furnace_screen_handler"), new ExtendedScreenHandlerType<>(ModFurnaceScreenHandler::new, ModFurnaceBlockData.CODEC));
    public static final ScreenHandlerType<ModBlastFurnaceScreenHandler> MOD_BAST_FURNACE_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(MITENewWorld.MOD_ID, "mod_blast_furnace_screen_handler"), new ExtendedScreenHandlerType<>(ModBlastFurnaceScreenHandler::new, ModBlastFurnaceBlockData.CODEC));

    public static final ScreenHandlerType<ModCastingTableScreenHandler> CASTING_TABLE_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(MITENewWorld.MOD_ID, "mod_casting_table_screen_handler"), new ExtendedScreenHandlerType<>(ModCastingTableScreenHandler::new, BlockPos.PACKET_CODEC));



    public static void registerScreenHandlers() {
    }
}
