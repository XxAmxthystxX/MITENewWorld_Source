package com.mitenewworld.block.castingtable;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.entity.blockentity.castingtable.ModCastingTableEntity;
import com.mitenewworld.item.component.CastingTableComponent;
import com.mitenewworld.item.component.ModDataComponentTypes;
import com.mitenewworld.item.component.OreComponent;
import com.mitenewworld.registry.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * 铸造台基础方块：朝向 + 4 级损坏状态（沿用铁砧的损坏思路）。
 *
 * <p>损坏掉落：完全损坏（DAMAGE=3）→ 7~15 个废金属（主金属=该铸造台金属）；
 * 未完全损坏 → 掉落带组件的铸造台（保留等级/耐久/金属）。
 */
public abstract class AbstractCastingTableBlock extends BlockWithEntity {

    public static final IntProperty DAMAGE = IntProperty.of("damage", 0, 3);
    public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;

    protected AbstractCastingTableBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(DAMAGE, 0));
    }

    // ============================================================
    //  碰撞箱：与铁砧造型的四个 element 一致（沿用原版 AnvilBlock 的取值）
    // ============================================================

    private static final VoxelShape SHAPE_BASE = createCuboidShape(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);
    private static final VoxelShape SHAPE_STEP_Z = createCuboidShape(4.0, 4.0, 3.0, 12.0, 5.0, 13.0);
    private static final VoxelShape SHAPE_STEM_Z = createCuboidShape(6.0, 5.0, 4.0, 10.0, 10.0, 12.0);
    private static final VoxelShape SHAPE_TOP_Z = createCuboidShape(3.0, 10.0, 0.0, 13.0, 16.0, 16.0);
    /** 南北向：砧面沿 Z 轴 */
    private static final VoxelShape SHAPE_NS = VoxelShapes.union(SHAPE_BASE, SHAPE_STEP_Z, SHAPE_STEM_Z, SHAPE_TOP_Z);

    private static final VoxelShape SHAPE_STEP_X = createCuboidShape(3.0, 4.0, 4.0, 13.0, 5.0, 12.0);
    private static final VoxelShape SHAPE_STEM_X = createCuboidShape(4.0, 5.0, 6.0, 12.0, 10.0, 10.0);
    private static final VoxelShape SHAPE_TOP_X = createCuboidShape(0.0, 10.0, 3.0, 16.0, 16.0, 13.0);
    /** 东西向：砧面沿 X 轴 */
    private static final VoxelShape SHAPE_EW = VoxelShapes.union(SHAPE_BASE, SHAPE_STEP_X, SHAPE_STEM_X, SHAPE_TOP_X);

    /** 由耐久比例计算损坏档位：0 正常，1 轻微，2 严重，3 完全损坏 */
    public static int damageFor(float ratio) {
        if (ratio <= 0f) {
            return 3;
        }
        if (ratio < 0.33f) {
            return 2;
        }
        if (ratio < 0.66f) {
            return 1;
        }
        return 0;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = state.get(FACING);
        return (facing == Direction.NORTH || facing == Direction.SOUTH) ? SHAPE_NS : SHAPE_EW;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getOutlineShape(state, world, pos, context);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ModCastingTableEntity table) {
                // 多人占用锁：打造中被占用则拒绝打开（仅 FORGING 锁定，owner 掉线后自动释放）
                if (table.isOccupiedByOther(player)) {
                    player.sendMessage(Text.translatable("message.mitenewworld.casting_table_occupied"), true);
                    return ActionResult.FAIL;
                }
                player.openHandledScreen(table);
            } else {
                return ActionResult.FAIL;
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    protected void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ModCastingTableEntity table) {
            ItemScatterer.spawn(world, pos, table);
        }
        super.onStateReplaced(state, world, pos, moved);
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, LootWorldContext.Builder builder) {
        List<ItemStack> drops = new ArrayList<>();
        BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);

        if (blockEntity instanceof ModCastingTableEntity table) {
            if (state.get(DAMAGE) >= 3 || table.isBroken()) {
                // 完全损坏 → 掉废金属
                Random random = table.getWorld() instanceof ServerWorld sw
                        ? sw.getRandom() : Random.create();
                int count = 7 + random.nextInt(9); // 7~15
                for (int i = 0; i < count; i++) {
                    ItemStack scrap = new ItemStack(ModItems.SCRAP_METAL);
                    scrap.set(ModDataComponentTypes.ORE_COMPONENT, OreComponent.scrap(table.metals()));
                    drops.add(scrap);
                }
            } else {
                ItemStack stack = new ItemStack(this);
                stack.set(ModDataComponentTypes.CASTING_TABLE_COMPONENT, new CastingTableComponent(table.metals(), table.tableLevel(), table.maxDurability(), table.currentDurability()));
                drops.add(stack);
            }
            return drops;
        }

        drops.add(new ItemStack(this));
        return drops;
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, DAMAGE);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing().rotateYClockwise());
    }
}
