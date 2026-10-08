package com.mitenewworld.block.furnace;
import com.mitenewworld.registry.ModBlockEntities;
import com.mitenewworld.entity.blockentity.furnace.ModFurnaceEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** 单体熔炉：等级 / 燃料容量 / 名称由注册参数决定 */
public class ModFurnaceBlock extends AbstractModFurnaceBlock<ModFurnaceEntity> {

    public static final MapCodec<ModFurnaceBlock> CODEC =
            createCodec(settings -> new ModFurnaceBlock(1, 400, "clay_furnace", settings));

    private final int level;
    private final int maxFuelTime;
    private final String furnaceName;

    public ModFurnaceBlock(int level, int maxFuelTime, String furnaceName, Settings settings) {
        super(settings);
        this.level = level;
        this.maxFuelTime = maxFuelTime;
        this.furnaceName = furnaceName;
    }

    /** 机器等级 1~4 */
    public int getLevel() {
        return level;
    }

    /** 燃料值上限 */
    public int getMaxFuelTime() {
        return maxFuelTime;
    }

    /** 容器名后缀，完整键为 container.mitenewworld.<name> */
    public String furnaceName() {
        return furnaceName;
    }

    @Override
    protected MapCodec<ModFurnaceBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected ModFurnaceEntity createCraftingTableEntity(BlockPos pos, BlockState state) {
        return new ModFurnaceEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlockEntities.MOD_FURNACE_ENTITY,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }
}
