package com.mitenewworld.entity.blockentity.craftingtable;
import com.mitenewworld.block.craftingtable.ModCraftingTableBlock;
import com.mitenewworld.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

/** 单体合成台方块实体：等级 / 名称取自方块参数 */
public class ModCraftingTableEntity extends AbstractModCraftingTableEntity {

    public ModCraftingTableEntity(BlockPos pos, BlockState state) {
        this(pos, state, (ModCraftingTableBlock) state.getBlock());
    }

    private ModCraftingTableEntity(BlockPos pos, BlockState state, ModCraftingTableBlock block) {
        super(ModBlockEntities.MOD_CRAFTING_TABLE_ENTITY, pos, state, block.getCraftLevel());
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("container.mitenewworld."
                + ((ModCraftingTableBlock) getCachedState().getBlock()).tableName());
    }
}
