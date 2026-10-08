package com.mitenewworld.item.metal;
import com.mitenewworld.MITENewWorld;

import com.google.common.collect.ImmutableMap;
import com.mitenewworld.cover.BlocksCover;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

/**
 * 工具的特殊交互行为。所有实现以匿名类形式集中于此。
 *
 * <p>使用方式：{@code ToolType.PICKAXE.behavior().useOnBlock(ctx, type)}
 */
public interface ToolBehavior {

    ActionResult useOnBlock(ItemUsageContext context, ModToolItem.ToolType type);

    // ============================================================
    //  种类枚举
    // ============================================================

    enum Kind { NONE, STRIP, FLATTEN, TILL, SHEAR, PROJECTILE }

    static ToolBehavior of(Kind kind) {
        return switch (kind) {
            case NONE       -> NONE;
            case STRIP      -> STRIP;
            case FLATTEN    -> FLATTEN;
            case TILL       -> TILL;
            case SHEAR      -> SHEAR;
            case PROJECTILE -> PROJECTILE;
        };
    }

    // ============================================================
    //  无特殊行为
    // ============================================================

    ToolBehavior NONE = (context, type) -> ActionResult.PASS;

    // ============================================================
    //  斧系：剥皮 / 刮锈 / 去蜡
    // ============================================================

    ToolBehavior STRIP = new ToolBehavior() {

        private static final Map<Block, Block> STRIPPED =
                new ImmutableMap.Builder<Block, Block>()
                        .put(BlocksCover.OAK_WOOD,        BlocksCover.STRIPPED_OAK_WOOD)
                        .put(BlocksCover.OAK_LOG,         BlocksCover.STRIPPED_OAK_LOG)
                        .put(BlocksCover.DARK_OAK_WOOD,   BlocksCover.STRIPPED_DARK_OAK_WOOD)
                        .put(BlocksCover.DARK_OAK_LOG,    BlocksCover.STRIPPED_DARK_OAK_LOG)
                        .put(BlocksCover.ACACIA_WOOD,     BlocksCover.STRIPPED_ACACIA_WOOD)
                        .put(BlocksCover.ACACIA_LOG,      BlocksCover.STRIPPED_ACACIA_LOG)
                        .put(BlocksCover.CHERRY_WOOD,     BlocksCover.STRIPPED_CHERRY_WOOD)
                        .put(BlocksCover.CHERRY_LOG,      BlocksCover.STRIPPED_CHERRY_LOG)
                        .put(BlocksCover.BIRCH_WOOD,      BlocksCover.STRIPPED_BIRCH_WOOD)
                        .put(BlocksCover.BIRCH_LOG,       BlocksCover.STRIPPED_BIRCH_LOG)
                        .put(BlocksCover.JUNGLE_WOOD,     BlocksCover.STRIPPED_JUNGLE_WOOD)
                        .put(BlocksCover.JUNGLE_LOG,      BlocksCover.STRIPPED_JUNGLE_LOG)
                        .put(BlocksCover.SPRUCE_WOOD,     BlocksCover.STRIPPED_SPRUCE_WOOD)
                        .put(BlocksCover.SPRUCE_LOG,      BlocksCover.STRIPPED_SPRUCE_LOG)
                        .put(BlocksCover.WARPED_STEM,     BlocksCover.STRIPPED_WARPED_STEM)
                        .put(BlocksCover.WARPED_HYPHAE,   BlocksCover.STRIPPED_WARPED_HYPHAE)
                        .put(BlocksCover.CRIMSON_STEM,    BlocksCover.STRIPPED_CRIMSON_STEM)
                        .put(BlocksCover.CRIMSON_HYPHAE,  BlocksCover.STRIPPED_CRIMSON_HYPHAE)
                        .put(BlocksCover.MANGROVE_WOOD,   BlocksCover.STRIPPED_MANGROVE_WOOD)
                        .put(BlocksCover.MANGROVE_LOG,    BlocksCover.STRIPPED_MANGROVE_LOG)
                        .put(BlocksCover.BAMBOO_BLOCK,    BlocksCover.STRIPPED_BAMBOO_BLOCK)
                        .put(BlocksCover.PALE_OAK_WOOD,   BlocksCover.STRIPPED_PALE_OAK_WOOD)
                        .put(BlocksCover.PALE_OAK_LOG,    BlocksCover.STRIPPED_PALE_OAK_LOG)
                        .build();

        @Override
        public ActionResult useOnBlock(ItemUsageContext context, ModToolItem.ToolType type) {
            World world = context.getWorld();
            BlockPos pos = context.getBlockPos();
            PlayerEntity player = context.getPlayer();
            if (shouldCancelStripAttempt(context)) {
                return ActionResult.PASS;
            }

            Optional<BlockState> result = tryStrip(world, pos, player, world.getBlockState(pos));
            if (result.isEmpty()) {
                return ActionResult.PASS;
            }

            ItemStack stack = context.getStack();
            if (player instanceof ServerPlayerEntity sp) {
                Criteria.ITEM_USED_ON_BLOCK.trigger(sp, pos, stack);
            }
            world.setBlockState(pos, result.get(), Block.NOTIFY_ALL_AND_REDRAW);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, result.get()));
            if (player != null) {
                // 磨损由 ToolItem 统一处理，这里不直接 damage
            }
            return ActionResult.SUCCESS;
        }

        private boolean shouldCancelStripAttempt(ItemUsageContext context) {
            PlayerEntity player = context.getPlayer();
            if (player == null) {
                return false;
            }
            return context.getHand().equals(net.minecraft.util.Hand.MAIN_HAND)
                    && player.getOffHandStack().isOf(net.minecraft.item.Items.SHIELD)
                    && !player.shouldCancelInteraction();
        }

        private Optional<BlockState> tryStrip(World world, BlockPos pos, @Nullable PlayerEntity player, BlockState state) {
            Optional<BlockState> opt = getStrippedState(state);
            if (opt.isPresent()) {
                world.playSound(player, pos, SoundEvents.ITEM_AXE_STRIP,
                        SoundCategory.BLOCKS, 1f, 1f);
                return opt;
            }
            Optional<BlockState> ox = Oxidizable.getDecreasedOxidationState(state);
            if (ox.isPresent()) {
                playStrip(world, pos, player, SoundEvents.ITEM_AXE_SCRAPE, 3005);
                return ox;
            }

            return Optional.empty();
        }

        private Optional<BlockState> getStrippedState(BlockState state) {
            return Optional.ofNullable(STRIPPED.get(state.getBlock()))
                    .map(b -> b.getDefaultState()
                            .with(PillarBlock.AXIS, state.get(PillarBlock.AXIS)));
        }

        private void playStrip(World world, BlockPos pos,
                               @Nullable PlayerEntity player,
                               net.minecraft.sound.SoundEvent sound, int event) {
            world.playSound(player, pos, sound, SoundCategory.BLOCKS, 1f, 1f);
            world.syncWorldEvent(player, event, pos, 0);
        }
    };

    // ============================================================
    //  铲：铲平草方块 / 熄灭营火
    // ============================================================

    ToolBehavior FLATTEN = new ToolBehavior() {

        private static final Map<Block, BlockState> PATH_STATES =
                new ImmutableMap.Builder<Block, BlockState>()
                        .put(BlocksCover.GRASS_BLOCK, BlocksCover.DIRT_PATH.getDefaultState())
                        .put(BlocksCover.DIRT,        BlocksCover.DIRT_PATH.getDefaultState())
                        .put(BlocksCover.PODZOL,      BlocksCover.DIRT_PATH.getDefaultState())
                        .put(BlocksCover.COARSE_DIRT, BlocksCover.DIRT_PATH.getDefaultState())
                        .put(BlocksCover.MYCELIUM,    BlocksCover.DIRT_PATH.getDefaultState())
                        .put(BlocksCover.ROOTED_DIRT, BlocksCover.DIRT_PATH.getDefaultState())
                        .build();

        @Override
        public ActionResult useOnBlock(ItemUsageContext context, ModToolItem.ToolType type) {
            World world = context.getWorld();
            BlockPos pos = context.getBlockPos();
            BlockState state = world.getBlockState(pos);
            if (context.getSide() == Direction.DOWN) {
                return ActionResult.PASS;
            }

            PlayerEntity player = context.getPlayer();
            BlockState target = PATH_STATES.get(state.getBlock());
            BlockState result = null;

            if (target != null && world.getBlockState(pos.up()).isAir()) {
                world.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN,
                        SoundCategory.BLOCKS, 1f, 1f);
                result = target;
            } else if (state.getBlock() instanceof CampfireBlock
                    && state.get(CampfireBlock.LIT)) {
                if (!world.isClient()) {
                    world.syncWorldEvent(null, WorldEvents.FIRE_EXTINGUISHED, pos, 0);
                }
                CampfireBlock.extinguish(player, world, pos, state);
                result = state.with(CampfireBlock.LIT, false);
            }

            if (result != null) {
                if (!world.isClient()) {
                    world.setBlockState(pos, result, Block.NOTIFY_ALL_AND_REDRAW);
                    world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, result));
                }
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        }
    };

    // ============================================================
    //  锄 / 鹤嘴锄：耕地
    // ============================================================

    ToolBehavior TILL = new ToolBehavior() {

        private static final Map<Block, java.util.function.Predicate<ItemUsageContext>> PREDICATES =
                Map.of(
                        BlocksCover.GRASS_BLOCK, HoeItem::canTillFarmland,
                        BlocksCover.DIRT_PATH,   HoeItem::canTillFarmland,
                        BlocksCover.DIRT,        HoeItem::canTillFarmland,
                        BlocksCover.COARSE_DIRT, HoeItem::canTillFarmland
                );

        private static final Map<Block, BlockState> RESULTS = Map.of(
                BlocksCover.GRASS_BLOCK, net.minecraft.block.Blocks.FARMLAND.getDefaultState(),
                BlocksCover.DIRT_PATH,   net.minecraft.block.Blocks.FARMLAND.getDefaultState(),
                BlocksCover.DIRT,        net.minecraft.block.Blocks.FARMLAND.getDefaultState(),
                BlocksCover.COARSE_DIRT, net.minecraft.block.Blocks.DIRT.getDefaultState()
        );

        @Override
        public ActionResult useOnBlock(ItemUsageContext context, ModToolItem.ToolType type) {
            World world = context.getWorld();
            BlockPos pos = context.getBlockPos();
            Block block = world.getBlockState(pos).getBlock();

            // 根土：耕地 + 掉垂根
            if (block == BlocksCover.ROOTED_DIRT) {
                PlayerEntity player = context.getPlayer();
                world.playSound(player, pos, SoundEvents.ITEM_HOE_TILL,
                        SoundCategory.BLOCKS, 1f, 1f);
                if (!world.isClient()) {
                    world.setBlockState(pos, net.minecraft.block.Blocks.DIRT.getDefaultState(), Block.NOTIFY_ALL_AND_REDRAW);
                    world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, net.minecraft.block.Blocks.DIRT.getDefaultState()));
                    Block.dropStack(world, pos, context.getSide(), new ItemStack(net.minecraft.item.Items.HANGING_ROOTS));
                }
                return ActionResult.SUCCESS;
            }

            var predicate = PREDICATES.get(block);
            BlockState result = RESULTS.get(block);
            if (predicate == null || result == null) {
                return ActionResult.PASS;
            }
            if (!predicate.test(context)) {
                return ActionResult.PASS;
            }

            PlayerEntity player = context.getPlayer();
            world.playSound(player, pos, SoundEvents.ITEM_HOE_TILL,
                    SoundCategory.BLOCKS, 1f, 1f);
            if (!world.isClient()) {
                world.setBlockState(pos, result, Block.NOTIFY_ALL_AND_REDRAW);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, result));
            }
            return ActionResult.SUCCESS;
        }
    };

    // ============================================================
    //  剪刀：剪植物
    // ============================================================

    ToolBehavior SHEAR = (context, type) -> {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof AbstractPlantStemBlock stem
                && !stem.hasMaxAge(state)) {
            PlayerEntity player = context.getPlayer();
            ItemStack stack = context.getStack();
            if (player instanceof ServerPlayerEntity sp) {
                Criteria.ITEM_USED_ON_BLOCK.trigger(sp, pos, stack);
            }
            world.playSound(player, pos, SoundEvents.BLOCK_GROWING_PLANT_CROP,
                    SoundCategory.BLOCKS, 1f, 1f);
            BlockState result = stem.withMaxAge(state);
            world.setBlockState(pos, result);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, result));
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    };

    // ============================================================
    //  匕首：可投掷（仅作为标记，实际投掷逻辑在 ModToolItem）
    // ============================================================

    ToolBehavior PROJECTILE = (context, type) -> ActionResult.PASS;
}
