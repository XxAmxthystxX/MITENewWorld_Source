package com.mitenewworld.mixin.worldmixin;


import com.mitenewworld.MITENewWorld;
import com.mitenewworld.cover.BlocksCover;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(Blocks.class)
public class BlocksMixin {
    @Inject(method = "<clinit>", at = @At("HEAD"))
    private static void registerBlocksPropertyCover(CallbackInfo ci) {
        //BlocksCover.registerBlocksCover();
    }
    @Inject(method = "register(Lnet/minecraft/registry/RegistryKey;Ljava/util/function/Function;Lnet/minecraft/block/AbstractBlock$Settings;)Lnet/minecraft/block/Block;", at = @At("HEAD"), cancellable = true)

    private static void BlocksCoverById(RegistryKey<Block> key, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings, CallbackInfoReturnable<Block> cir) {
        cir.cancel();
        Block block1 = BlocksCover.BlockCoverMap.getOrDefault(key, null);
        if (block1 != null) {
            cir.setReturnValue(block1);
        } else {
            MITENewWorld.LOGGER.warn("has block not found in BlocksCover.BlockCoverMap: {}", key.getValue().getPath());
            cir.setReturnValue(BlocksCover.register(key, factory, settings));
        }
    }




}

