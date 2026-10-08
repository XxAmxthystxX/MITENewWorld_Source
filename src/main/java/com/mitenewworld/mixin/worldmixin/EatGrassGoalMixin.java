package com.mitenewworld.mixin.worldmixin;
import com.mitenewworld.MITENewWorld;


import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.EatGrassGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(EatGrassGoal.class)
public abstract class EatGrassGoalMixin extends Goal {
    @Shadow
    private static final int MAX_TIMER = 40;
    @Shadow
    private static final Predicate<BlockState> EDIBLE_PREDICATE = state -> state.isIn(BlockTags.EDIBLE_FOR_SHEEP);
    @Mutable
    @Final
    @Shadow
    private final MobEntity mob;
    @Mutable
    @Final
    @Shadow
    private final World world;
    @Shadow
    private int timer;

    protected EatGrassGoalMixin(MobEntity mob, World world) {
        this.mob = mob;
        this.world = world;
    }

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {
        ci.cancel();
        if (!world.isClient()) {
            this.timer = Math.max(0, this.timer - 1);
            if (this.timer == this.getTickCount(MAX_TIMER)) {
                BlockPos blockPos = this.mob.getBlockPos();
                if (EDIBLE_PREDICATE.test(this.world.getBlockState(blockPos))) {
                    if (((ServerWorld)world).getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                        world.breakBlock(blockPos, false);
                    }
                    this.mob.onEatingGrass();
                }
            }
        }
    }
}
