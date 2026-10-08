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

/** 合金炉熔炼核心（黑石砖）。 */
public class ModBlastFurnace3Block extends AbstractModBlastFurnaceBlock {

    public static final MapCodec<ModBlastFurnace3Block> CODEC = createCodec(ModBlastFurnace3Block::new);

    public ModBlastFurnace3Block(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<ModBlastFurnace3Block> getCodec() { return CODEC; }

    @Override public int tier() { return 3; }
    @Override public int tierColor() { return 0x808080; }

    @Override
    protected boolean isValidShell(BlockState state) {
        return state.isOf(ModBlocks.BLACKSTONE_BRICK);
    }

    @Override
    protected List<AbstractMultiBlockStructure.SpecialBlock> specialBlocks() {
        return List.of(
                new AbstractMultiBlockStructure.SpecialBlock(
                        s -> s.isOf(ModBlocks.BLACKSTONE_BRICK_INPUT_PORT),
                        AbstractMultiBlockStructure.SpecialKind.HOPPER_LINK),
                new AbstractMultiBlockStructure.SpecialBlock(
                        s -> s.isOf(ModBlocks.BLACKSTONE_BRICK_OUTPUT_PORT),
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
