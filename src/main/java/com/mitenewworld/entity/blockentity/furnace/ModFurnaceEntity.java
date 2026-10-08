package com.mitenewworld.entity.blockentity.furnace;
import com.mitenewworld.block.furnace.ModFurnaceBlock;
import com.mitenewworld.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

/** 单体熔炉方块实体：等级 / 燃料容量 / 名称取自方块参数 */
public class ModFurnaceEntity extends AbstractModFurnaceEntity {

    public ModFurnaceEntity(BlockPos pos, BlockState state) {
        this(pos, state, (ModFurnaceBlock) state.getBlock());
    }

    private ModFurnaceEntity(BlockPos pos, BlockState state, ModFurnaceBlock block) {
        super(ModBlockEntities.MOD_FURNACE_ENTITY, pos, state, block.getMaxFuelTime(), block.getLevel());
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("container.mitenewworld." + ((ModFurnaceBlock) getCachedState().getBlock()).furnaceName());
    }
}
