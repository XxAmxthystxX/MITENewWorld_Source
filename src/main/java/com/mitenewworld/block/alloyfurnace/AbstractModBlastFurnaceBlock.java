package com.mitenewworld.block.alloyfurnace;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.core.AbstractMultiBlockStructure;
import com.mitenewworld.entity.blockentity.alloyfurnace.AbstractModBlastFurnaceEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 多方块高炉基类。
 *
 * 结构验证在 {@link #onUse} 时执行：
 * - 已有 BlockEntity → 直接打开 GUI
 * - 无 BlockEntity → 验证结构，成功则创建实体，失败则提示玩家
 *
 * 结构被破坏（方块被替换）时，掉落库存并清空结构缓存。
 */
public abstract class AbstractModBlastFurnaceBlock extends BlockWithEntity {

    public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty LIT = Properties.LIT;

    protected AbstractModBlastFurnaceBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(LIT, Boolean.FALSE));
    }

    /** 该核心的等级 1~5 与 GUI 着色 */
    public abstract int tier();
    public abstract int tierColor();

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
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
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(LIT)) {
            double d = (double) pos.getX() + 0.5;
            double e = (double) pos.getY();
            double f = (double) pos.getZ() + 0.5;
            if (random.nextDouble() < 0.1) {
                world.playSoundClient(d, e, f, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE,
                        SoundCategory.BLOCKS, 1.0f, 1.0f, false);
            }
            Direction direction = state.get(FACING);
            Direction.Axis axis = direction.getAxis();
            double h = random.nextDouble() * 0.6 - 0.3;
            double i = axis == Direction.Axis.X ? (double) direction.getOffsetX() * 0.52 : h;
            double j = random.nextDouble() * 6.0 / 16.0;
            double k = axis == Direction.Axis.Z ? (double) direction.getOffsetZ() * 0.52 : h;
            world.addParticleClient(ParticleTypes.SMOKE, d + i, e + j, f + k, 0.0, 0.0, 0.0);
            world.addParticleClient(ParticleTypes.FLAME, d + i, e + j, f + k, 0.0, 0.0, 0.0);
        }
    }

    // ============================================================
    //  子类必须实现
    // ============================================================

    /** 普通外壳方块判定（例如地狱岩及衍生完整方块） */
    protected abstract boolean isValidShell(BlockState state);

    /** 特殊方块列表（OUTPUT / HOPPER_LINK 等） */
    protected abstract List<AbstractMultiBlockStructure.SpecialBlock> specialBlocks();

    /** 创建对应 tier 的方块实体 */
    protected abstract AbstractModBlastFurnaceEntity createFurnaceEntity(BlockPos pos, BlockState state, BlockPos hopperPos, BlockPos outputPos);

    /** 该方块对应的 BlockEntityType，用于 ticker 校验 */
    protected abstract BlockEntityType<? extends AbstractModBlastFurnaceEntity> blockEntityType();

    // ============================================================
    //  交互
    // ============================================================

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, BlockHitResult hit) {
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }

        BlockEntity existing = world.getBlockEntity(pos);
        if (existing instanceof AbstractModBlastFurnaceEntity furnace) {
            // 打火石：无热值时点一次火（点亮炉子），不打开 GUI
            if (player.getStackInHand(Hand.MAIN_HAND).getItem() == Items.FLINT_AND_STEEL) {
                furnace.ignite(world, pos);
                return ActionResult.SUCCESS;
            }
            player.openHandledScreen(furnace);
            return ActionResult.SUCCESS;
        }

        // 无实体：验证结构
        AbstractMultiBlockStructure.Result result = BlastFurnaceStructure.INSTANCE.validate(
                world, pos, state,
                this::isValidShell,
                specialBlocks()
        );

        if (!result.valid) {
            String reason = result.reason == null ? "invalid_structure" : result.reason;
            player.sendMessage(
                    Text.translatable("message.mitenewworld.blast_furnace." + reason),
                    false);
            if (result.failedPos != null && result.failedBlock != null) {
                BlockPos fp = result.failedPos;
                player.sendMessage(
                        Text.translatable("message.mitenewworld.blast_furnace.at",
                                fp.getX(), fp.getY(), fp.getZ(), result.failedBlock.getName()),
                        false);
            }
            return ActionResult.FAIL;
        }

        // 清空内部 3×3×3 非空气方块
        for (BlockPos p : result.toClear) {
            world.setBlockState(p, Blocks.AIR.getDefaultState());
        }

        // 创建实体
        AbstractModBlastFurnaceEntity furnace = createFurnaceEntity(pos, state, result.get(AbstractMultiBlockStructure.SpecialKind.HOPPER_LINK), result.get(AbstractMultiBlockStructure.SpecialKind.OUTPUT));
        world.addBlockEntity(furnace);
        player.openHandledScreen(furnace);
        return ActionResult.SUCCESS;
    }

    // ============================================================
    //  销毁
    // ============================================================

    @Override
    public void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof AbstractModBlastFurnaceEntity furnace) {
            furnace.dropContents(world, pos);
        }
        BlastFurnaceStructure.INSTANCE.invalidate(pos);


        super.onStateReplaced(state, world, pos, moved);
    }

    // ============================================================
    //  方块实体
    // ============================================================

    /** 实体只在结构校验通过后由 {@link #onUse} 手动创建，这里返回 null */
    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    // ============================================================
    //  渲染与 Ticker
    // ============================================================

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        // BlockWithEntity 默认 INVISIBLE，必须覆盖成 MODEL 才会渲染模型
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient()) {
            return null;
        }
        return validateTicker(type, blockEntityType(),
                (w, p, s, be) -> ((AbstractModBlastFurnaceEntity) be).tick(w, p, s));
    }

    // ============================================================
    //  热值等级（5 档）
    // ============================================================

    public enum HeatLevel {
        CAMPFIRE     ("campfire",      0,   5f),
        FURNACE      ("furnace",       1,  20f),
        BLAST_FURNACE("blast_furnace", 2,  60f),
        CRUCIBLE     ("crucible",      3, 180f),
        ARCANE       ("arcane",        4, 500f);

        public final String id;
        public final int tier;
        /** 该等级炉子每 tick 提供的热值 */
        public final float heatPerTick;

        HeatLevel(String id, int tier, float heatPerTick) {
            this.id = id;
            this.tier = tier;
            this.heatPerTick = heatPerTick;
        }

        private static final java.util.Map<String, HeatLevel> BY_ID = new java.util.HashMap<>();
        private static final HeatLevel[] VALUES = values();
        static { for (HeatLevel h : VALUES) {
            BY_ID.put(h.id, h);
        } }

        public static HeatLevel byId(String id) { return BY_ID.get(id); }
        public static HeatLevel byTier(int tier) {
            return VALUES[Math.clamp(tier, 0, VALUES.length - 1)];
        }

        /** 本炉子每 tick 热值是否达到金属的最低要求 */
        public boolean canMelt(HeatLevel required) {
            return this.heatPerTick >= required.heatPerTick - 1e-4f;
        }

        /** 熔炼 totalHeat 所需的游戏刻数 */
        public int smeltTicks(float totalHeat) {
            return Math.max(1, (int) Math.ceil(totalHeat / this.heatPerTick));
        }
    }
}
