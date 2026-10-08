package com.mitenewworld.block.castingtable;

import com.mitenewworld.entity.blockentity.castingtable.ModCastingTableEntity;
import com.mitenewworld.item.component.CastingTableComponent;
import com.mitenewworld.registry.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import net.minecraft.block.AbstractBlock;

/**
 * 燧石铸造台：一级铸造台，燧石 + 木板搭出来的最原始台子。
 *
 * <p>与金属铸造台的区别：
 * <ul>
 *   <li>等级固定 1，耐久固定 {@value #MAX_DURABILITY}，不参与金属配比 / 染色</li>
 *   <li>使用独立的模型与贴图（不走金属台那套 tint 白模）</li>
 * </ul>
 */
public class ModFlintCastingTableBlock extends AbstractCastingTableBlock {

    public static final MapCodec<ModFlintCastingTableBlock> CODEC = createCodec(ModFlintCastingTableBlock::new);

    /** 固定等级：一级 */
    public static final int TABLE_LEVEL = 1;
    /** 固定耐久上限 */
    public static final float MAX_DURABILITY = 200f;
    /** 完全损坏时掉落废金属所用的配比 */
    public static final Map<String, Float> METALS = Map.of("flint", 1.0f);

    public ModFlintCastingTableBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ModCastingTableEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlockEntities.MOD_CASTING_TABLE_ENTITY,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }

    /** 放下时注入固定属性：一级 / 满耐久 */
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (world.getBlockEntity(pos) instanceof ModCastingTableEntity table) {
            table.applyFromComponent(CastingTableComponent.fresh(METALS, TABLE_LEVEL, MAX_DURABILITY));
        }
    }
}
