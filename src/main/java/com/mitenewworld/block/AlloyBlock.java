package com.mitenewworld.block;

import com.mitenewworld.entity.blockentity.AlloyBlockEntity;
import com.mitenewworld.item.metal.AlloyBlockItem;
import com.mitenewworld.registry.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class AlloyBlock extends BlockWithEntity {

    public static final MapCodec<AlloyBlock> CODEC = createCodec(AlloyBlock::new);

    public AlloyBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AlloyBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack tool, boolean dropExperience) {
        super.onStacksDropped(state, world, pos, tool, dropExperience);
        if (world.getBlockEntity(pos) instanceof AlloyBlockEntity be && be.alloy() != null) {
            Block.dropStack(world, pos, AlloyBlockItem.fromComponent(ModItems.ALLOY_BLOCK, be.alloy()));
        }
    }
}
