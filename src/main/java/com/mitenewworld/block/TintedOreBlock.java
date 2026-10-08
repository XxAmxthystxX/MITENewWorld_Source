package com.mitenewworld.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class TintedOreBlock extends ModOreBlock {

    public TintedOreBlock(String metalId, OreRichness richness, Item dropItem, float baseHardness, Settings settings) {
        super(metalId, richness, dropItem, baseHardness, settings);
    }

    public float hardnessAt(int y) {
        return baseHardness() + DepthLayer.levelAt(y);
    }

    @Override
    protected float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
        return DepthLayer.breakingDelta(state, player, world, pos, hardnessAt(pos.getY()));
    }
}
