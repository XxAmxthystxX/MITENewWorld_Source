package com.mitenewworld.mixin.worldmixin;
import com.mitenewworld.MITENewWorld;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin extends Block {
    /** 复用的减速向量：onEntityCollision 每次碰撞都会调用，不能每次 new */
    private static final Vec3d LEAF_SLOWDOWN = new Vec3d(1.1F, 1.2F, 1.1F);

    public LeavesBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public int getOpacity(BlockState state) {
        return 7;
    }
    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler, boolean bl) {
        entity.slowMovement(state, LEAF_SLOWDOWN);
    }
}
