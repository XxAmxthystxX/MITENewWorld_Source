package com.mitenewworld.block.alloyfurnace;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.core.AbstractMultiBlockStructure;
import com.mitenewworld.registry.ModBlocks;
import com.mitenewworld.registry.ModBlockEntities;
import com.mitenewworld.entity.blockentity.alloyfurnace.AbstractModBlastFurnaceEntity;
import com.mitenewworld.entity.blockentity.alloyfurnace.ModBlastFurnaceEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/** 合金炉熔炼核心（平滑石砖）。 */
public class ModBlastFurnace1Block extends AbstractModBlastFurnaceBlock {

    public static final MapCodec<ModBlastFurnace1Block> CODEC = createCodec(ModBlastFurnace1Block::new);

    public ModBlastFurnace1Block(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<ModBlastFurnace1Block> getCodec() { return CODEC; }

    @Override public int tier() { return 1; }
    @Override public int tierColor() { return 0xFF8A5A; }

    @Override
    protected boolean isValidShell(BlockState state) {
        return state.isOf(ModBlocks.SMOOTH_STONE_BRICK);
    }

    @Override
    protected List<AbstractMultiBlockStructure.SpecialBlock> specialBlocks() {
        return List.of(
                new AbstractMultiBlockStructure.SpecialBlock(
                        s -> s.isOf(ModBlocks.SMOOTH_STONE_BRICK_INPUT_PORT),
                        AbstractMultiBlockStructure.SpecialKind.HOPPER_LINK),
                new AbstractMultiBlockStructure.SpecialBlock(
                        s -> s.isOf(ModBlocks.SMOOTH_STONE_BRICK_OUTPUT_PORT),
                        AbstractMultiBlockStructure.SpecialKind.OUTPUT)
        );
    }

    @Override
    protected AbstractModBlastFurnaceEntity createFurnaceEntity(BlockPos pos, BlockState state, BlockPos hopperPos, BlockPos outputPos) {
        return new ModBlastFurnaceEntity(pos, state, hopperPos, outputPos);
    }

    @Override
    protected BlockEntityType<? extends AbstractModBlastFurnaceEntity> blockEntityType() {
        return ModBlockEntities.MOD_BLAST_FURNACE_ENTITY;
    }
}
