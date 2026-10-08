package com.mitenewworld.block.portal;
import com.mitenewworld.entity.blockentity.portal.WorldBreakerBlockEntity;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.registry.ModBlockEntities;
import com.mitenewworld.entity.blockentity.portal.WorldBreakerBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class WorldBreakerBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final MapCodec<WorldBreakerBlock> CODEC = createCodec(WorldBreakerBlock::new);;
    public static final IntProperty PHASE = IntProperty.of("phase", 0, 4);
    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

    public WorldBreakerBlock(Settings settings) {
        super(settings);
        this.setDefaultState(getDefaultState()
                .with(PHASE, 0)
                .with(ACTIVE, false));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public ActionResult onUseWithItem(ItemStack stack, BlockState state,
                                      World world, BlockPos pos,
                                      PlayerEntity player, Hand hand,
                                      BlockHitResult hit) {
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }

        if (state.get(ACTIVE)) {
            player.sendMessage(Text.literal("装置已激活"), true);
            return ActionResult.SUCCESS;
        }
        if (!stack.isOf(Items.NETHER_STAR)) {
            player.sendMessage(Text.literal("装置需要强烈的能量"), true);
            return ActionResult.CONSUME;
        }

        if (!player.getAbilities().creativeMode) {
            stack.decrement(1);
        }

        world.setBlockState(pos, state.with(ACTIVE, true).with(PHASE, 1), Block.NOTIFY_ALL);

        player.sendMessage(Text.literal("能量注入成功，装置启动"), true);
        return ActionResult.CONSUME;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(PHASE, ACTIVE);
    }


    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WorldBreakerBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlockEntities.WORLD_BREAKER_BLOCK_ENTITY,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }
}
