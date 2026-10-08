package com.mitenewworld.mixin.worldmixin;


import com.mitenewworld.MITENewWorld;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.block.FluidBlock.LEVEL;
import static net.minecraft.fluid.FlowableFluid.FALLING;

@Mixin(FlowableFluid.class)

public abstract class FlowableFluidMixin extends Fluid{

    @Inject(method = "getUpdatedState", at = @At("HEAD"), cancellable = true)
    // 优化版本 - 复用 mutablePos
    protected void getUpdatedState(ServerWorld world, BlockPos pos, BlockState state, CallbackInfoReturnable<FluidState> cir) {
        cir.cancel();
        FluidState currentFluidState = state.getFluidState();
        int currentLevel = currentFluidState.isEmpty() ? 0 : currentFluidState.getLevel();
        int stillSourceCount = 0;
        int maxLevel = currentLevel;
        BlockPos.Mutable mutablePos = new BlockPos.Mutable();

        for (Direction direction : Direction.Type.HORIZONTAL) {
            BlockPos.Mutable neighborPos = mutablePos.set(pos, direction);
            BlockState neighborState = world.getBlockState(neighborPos);
            FluidState neighborFluid = neighborState.getFluidState();
            if (!neighborFluid.getFluid().matchesType(this) || !receivesFlow(direction, world, pos, state, neighborPos, neighborState)) {
                continue;
            }
            if (neighborFluid.isStill()) {
                ++stillSourceCount;
            }
            maxLevel = Math.max(maxLevel, neighborFluid.getLevel());
        }

        // 无限水源检查
        if (stillSourceCount >= 2 && this.isInfinite(world)) {
            BlockState belowState = world.getBlockState(mutablePos.set(pos, Direction.DOWN));
            FluidState belowFluid = belowState.getFluidState();

            if (belowState.isSolidBlock(world , pos) || this.isMatchingAndStill(belowFluid)) {
                cir.setReturnValue(this.getStill(false));
            }
        }

        BlockState aboveState = world.getBlockState(mutablePos.set(pos, Direction.UP));
        FluidState aboveFluid = aboveState.getFluidState();

        // 检查上方流体
        if (!aboveFluid.isEmpty() && aboveFluid.getFluid().matchesType(this) &&
                receivesFlow(Direction.UP, world, pos, state, mutablePos, aboveState)) {
            cir.setReturnValue( this.getFlowing(8, true));
            return;
        }

        // 获取下方状态（复用 mutablePos）
        BlockState belowState2 = world.getBlockState(mutablePos.set(pos, Direction.DOWN));
        FluidState belowFluid2 = belowState2.getFluidState();

        // 新增特性：流体下落的额外消耗
        if ((belowFluid2.getFluid().matchesType(this) || belowState2.isAir()) &&
                aboveFluid.isEmpty()) {
            maxLevel -= 3;
        }
        int finalLevel = maxLevel - this.getLevelDecreasePerBlock(world);
        if (finalLevel <= 0) {
            cir.setReturnValue(Fluids.EMPTY.getDefaultState());
            return;
        }
        cir.setReturnValue(this.getFlowing(finalLevel, false));
    }
    @Shadow
    protected int getLevelDecreasePerBlock(WorldView world){
        return 0;
    }

    @Shadow
    public Fluid getFlowing(){
        return null;
    }
    @Shadow
    public FluidState getFlowing(int level, boolean falling) {
        return this.getFlowing().getDefaultState().with(LEVEL, level).with(FALLING, falling);
    }

    @Shadow
    private static boolean receivesFlow(Direction face, BlockView world, BlockPos pos, BlockState state, BlockPos fromPos, BlockState fromState) {
        return false;
    }

    @Shadow
    protected boolean isInfinite(ServerWorld world) {
        return false;
    }

    @Shadow
    public Fluid getStill() {
        return null;
    }

    @Shadow
    public FluidState getStill(boolean falling) {
        return this.getStill().getDefaultState().with(FALLING, falling);
    }

    @Shadow
    private boolean isMatchingAndStill(FluidState fluidState2) {
        return false;
    }
}
