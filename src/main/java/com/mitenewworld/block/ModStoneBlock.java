package com.mitenewworld.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class ModStoneBlock extends Block {

    public static final float BASE_HARDNESS = 10.0f;

    public ModStoneBlock(Settings settings) {
        super(settings);
    }

    public float hardnessAt(int y) {
        return BASE_HARDNESS + DepthLayer.levelAt(y);
    }

    @Override
    protected float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
        return DepthLayer.breakingDelta(state, player, world, pos, hardnessAt(pos.getY()));
    }
}
