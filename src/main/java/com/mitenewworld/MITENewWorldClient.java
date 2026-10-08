package com.mitenewworld;
import com.mitenewworld.registry.ModBlocks;
import com.mitenewworld.block.DepthLayer;
import com.mitenewworld.block.ModOres;

import com.mitenewworld.item.metal.AlloyProperties;
import com.mitenewworld.item.metal.AlloyTintSource;
import com.mitenewworld.render.CastingTableTintSource;
import com.mitenewworld.entity.blockentity.AlloyBlockEntity;
import com.mitenewworld.entity.blockentity.castingtable.ModCastingTableEntity;
import com.mitenewworld.network.StateUpdateS2CPacket;
import com.mitenewworld.render.ModEntityRenderers;
import com.mitenewworld.screen.alloyfurnace.ModBlastFurnaceScreen;
import com.mitenewworld.screen.castingtable.ModCastingTableScreen;
import com.mitenewworld.screen.craftingtable.ModCraftingTableScreen;
import com.mitenewworld.screen.furnace.ModFurnaceScreen;
import com.mitenewworld.screen.ModScreenHandlers;
import com.mitenewworld.core.shadow.ShadowHungerManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.loader.impl.lib.mappingio.format.tiny.Tiny1FileReader;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.item.tint.TintSourceTypes;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.util.Identifier;


public class MITENewWorldClient implements ClientModInitializer {



        @Override
        public void onInitializeClient() {

            HandledScreens.register(ModScreenHandlers.MOD_CRAFTING_TABLE_SCREEN_HANDLER , ModCraftingTableScreen::new);
            HandledScreens.register(ModScreenHandlers.MOD_FURNACE_SCREEN_HANDLER , ModFurnaceScreen::new);
            HandledScreens.register(ModScreenHandlers.MOD_BAST_FURNACE_SCREEN_HANDLER , ModBlastFurnaceScreen::new);
            HandledScreens.register(ModScreenHandlers.CASTING_TABLE_SCREEN_HANDLER , ModCastingTableScreen::new);

            ClientPlayNetworking.registerGlobalReceiver(StateUpdateS2CPacket.ID, (packet, context) -> {
                context.client().execute(() -> {
                    context.client().player.updateHealth(packet.getHealth());
                    HungerManager hungerManager = context.client().player.getHungerManager();
                    hungerManager.setFoodLevel(packet.getFood());
                    hungerManager.setSaturationLevel(packet.getSaturation());
                    ((ShadowHungerManager)hungerManager).setMaxFoodLevel(packet.getMaxFoodLevel());
                    ((ShadowHungerManager)hungerManager).setMaxWaterLevel(packet.getMaxWaterLevel());
                    ((ShadowHungerManager)hungerManager).setWaterLevel(packet.getWater());
                });
            });

            ModEntityRenderers.registerRenderers();

            // 矿石双层模型：矿点层含透明像素，必须走 cutout 渲染层，否则透明处会渲染成黑色
            BlockRenderLayerMap.putBlocks(BlockRenderLayer.CUTOUT, ModOres.ALL_ORES.toArray(new Block[0]));

            TintSourceTypes.ID_MAPPER.put(Identifier.of(MITENewWorld.MOD_ID, "alloy"), AlloyTintSource.CODEC);
            TintSourceTypes.ID_MAPPER.put(Identifier.of(MITENewWorld.MOD_ID, "casting_table"), CastingTableTintSource.CODEC);

            // 铸造台方块调色：金属色（通用台 + 13 种金属台共用一套渲染）
            ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
                if (world != null && pos != null
                        && world.getBlockEntity(pos) instanceof ModCastingTableEntity table) {
                    return 0xFF000000 | table.metalColor();
                }
                return 0xFFFFFFFF;
            }, ModBlocks.allCastingTables());

            // 深度调色：mod_stone 与主世界矿石背景
            ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
                if (tintIndex != 0 || pos == null) {
                    return 0xFFFFFFFF;
                }
                return 0xFF000000 | DepthLayer.colorAt(pos.getY());
            }, ModOres.MOD_STONE);
            for (Block ore : ModOres.TINTED_ORES) {
                ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
                    if (tintIndex != 0 || pos == null) {
                        return 0xFFFFFFFF;
                    }
                    return 0xFF000000 | DepthLayer.colorAt(pos.getY());
                }, ore);
            }

            // 合金块调色：读取方块实体里的合金配比
            ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
                if (tintIndex != 0 || world == null || pos == null) {
                    return 0xFFFFFFFF;
                }
                if (world.getBlockEntity(pos) instanceof AlloyBlockEntity be && be.alloy() != null) {
                    return AlloyTintSource.colorOf(be.alloy());
                }
                return 0xFFFFFFFF;
            }, ModBlocks.ALLOY_BLOCK);
        }



}

