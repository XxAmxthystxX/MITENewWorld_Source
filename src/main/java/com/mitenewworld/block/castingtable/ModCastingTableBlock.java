package com.mitenewworld.block.castingtable;

import com.mitenewworld.entity.blockentity.castingtable.ModCastingTableEntity;
import com.mitenewworld.item.component.CastingTableComponent;
import com.mitenewworld.item.component.ModDataComponentTypes;
import com.mitenewworld.registry.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * 铸造台方块。
 *
 * <p>方块本身只有一份（ID 按金属区分），金属 / 等级 / 耐久全部由
 * {@link CastingTableComponent} 承载，渲染靠乘法 tint 上金属色：
 * <ul>
 *   <li>方块：{@code ColorProviderRegistry.BLOCK} → BlockEntity 里的金属配比</li>
 *   <li>物品：{@code mitenewworld:casting_table} tint → 物品上的组件</li>
 * </ul>
 * 所以纹理必须画成"接近白的明暗图"，详见 CastingTableDefaults / 生成脚本里的说明。
 */
public class ModCastingTableBlock extends AbstractCastingTableBlock {

    public static final MapCodec<ModCastingTableBlock> CODEC = createCodec(ModCastingTableBlock::new);

    /** 该方块默认对应的金属 id（物品未带组件时的兜底） */
    private final String metalId;

    public ModCastingTableBlock(Settings settings) {
        this(CastingTableDefaults.FALLBACK_METAL, settings);
    }

    public ModCastingTableBlock(String metalId, Settings settings) {
        super(settings);
        this.metalId = metalId == null ? CastingTableDefaults.FALLBACK_METAL : metalId;
    }

    public String metalId() {
        return metalId;
    }

    @Override
    protected MapCodec<ModCastingTableBlock> getCodec() {
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

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        CastingTableComponent comp = itemStack.get(ModDataComponentTypes.CASTING_TABLE_COMPONENT);
        if (comp != null && world.getBlockEntity(pos) instanceof ModCastingTableEntity table) {
            table.applyFromComponent(comp);
        }
    }
}
