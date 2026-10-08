package com.mitenewworld.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public final class DepthLayer {

    public static final int SURFACE_Y = 200;
    public static final int DEPTH_STEP = 32;
    public static final int MAX_LEVEL = 6;

    private DepthLayer() {
    }

    public static int levelAt(int y) {
        return Math.clamp((SURFACE_Y - y) / DEPTH_STEP, 0, MAX_LEVEL);
    }

    public static float brightnessAt(int y) {
        return 1.0f - levelAt(y) * 0.08f;
    }

    public static int colorAt(int y) {
        int c = (int) (255 * brightnessAt(y));
        return (c << 16) | (c << 8) | c;
    }

    public static float breakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos, float hardness) {
        if (hardness == -1.0f) {
            return 0.0f;
        }
        int divisor = player.canHarvest(state) ? 30 : 100;
        return player.getBlockBreakingSpeed(state) / hardness / (float) divisor;
    }
}
