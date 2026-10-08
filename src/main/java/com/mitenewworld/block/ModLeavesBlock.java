package com.mitenewworld.block;
import com.mitenewworld.MITENewWorld;

import net.minecraft.block.LeavesBlock;

@Deprecated
public abstract class ModLeavesBlock extends LeavesBlock {
    public ModLeavesBlock(Settings settings) {
        super(10.0f ,settings);
    }

   /* @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public int getOpacity(BlockState state, BlockView world, BlockPos pos) {
        return 7;
    }
    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        Vec3d vec3d = new Vec3d(1.0F, 1.0F, 1.0F);
        entity.slowMovement(state, vec3d);
    }
    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }*/


}
