package com.mitenewworld.block.craftingtable;
import com.mitenewworld.registry.ModBlockEntities;
import com.mitenewworld.entity.blockentity.craftingtable.ModCraftingTableEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** 单体合成台：等级 / 名称由注册参数决定 */
public class ModCraftingTableBlock extends AbstractCraftingTableBlock<ModCraftingTableEntity> {

    public static final MapCodec<ModCraftingTableBlock> CODEC =
            createCodec(settings -> new ModCraftingTableBlock(1, "flint_crafting_table", settings));

    private final int craftLevel;
    private final String tableName;

    public ModCraftingTableBlock(int craftLevel, String tableName, Settings settings) {
        super(settings);
        this.craftLevel = craftLevel;
        this.tableName = tableName;
    }

    /** 合成等级 1~6 */
    public int getCraftLevel() {
        return craftLevel;
    }

    /** 容器名后缀，完整键为 container.mitenewworld.<name> */
    public String tableName() {
        return tableName;
    }

    @Override
    protected ModCraftingTableEntity createCraftingTableEntity(BlockPos pos, BlockState state) {
        return new ModCraftingTableEntity(pos, state);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlockEntities.MOD_CRAFTING_TABLE_ENTITY,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }
}
