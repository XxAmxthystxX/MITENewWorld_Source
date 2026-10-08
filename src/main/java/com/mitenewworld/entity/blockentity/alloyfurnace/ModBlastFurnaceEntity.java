package com.mitenewworld.entity.blockentity.alloyfurnace;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.block.alloyfurnace.AbstractModBlastFurnaceBlock;
import com.mitenewworld.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

/**
 * 合金炉方块实体：等级由熔炼核心方块决定（1~5）。
 */
public class ModBlastFurnaceEntity extends AbstractModBlastFurnaceEntity {

    /** NBT 加载用（结构位置从存档恢复） */
    public ModBlastFurnaceEntity(BlockPos pos, BlockState state) {
        this(pos, state, null, null);
    }

    /** 结构校验成功时由方块创建 */
    public ModBlastFurnaceEntity(BlockPos pos, BlockState state, BlockPos hopperPos, BlockPos outputPos) {
        super(ModBlockEntities.MOD_BLAST_FURNACE_ENTITY, pos, state,
                ((AbstractModBlastFurnaceBlock) state.getBlock()).tier(),
                hopperPos, outputPos);
    }

    @Override
    public int getTierColor() {
        return ((AbstractModBlastFurnaceBlock) this.getCachedState().getBlock()).tierColor();
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("container.mitenewworld.alloy_furnace." + this.tier);
    }
}
