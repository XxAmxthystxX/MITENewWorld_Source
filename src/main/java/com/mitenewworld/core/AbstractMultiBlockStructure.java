package com.mitenewworld.core;
import com.mitenewworld.MITENewWorld;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 通用多方块结构验证基类。
 *
 * 子类只负责描述“形状”：
 * - 结构尺寸
 * - 主方块在局部坐标中的位置
 * - 每个局部坐标的格子类型
 *
 * 具体方块识别（外壳、特殊方块）由调用方在 {@link #validate} 时通过回调传入，
 * 避免基类耦合具体方块。
 *
 * 坐标系：
 * - facing：主方块朝向（从外部看向主方块的方向）
 * - 结构在 facing.getOpposite() 方向延伸
 * - right = facing.rotateYClockwise()
 * - up = Direction.UP
 * - 局部坐标 (sx, sy, sz)：sx 沿 right，sy 沿 up，sz 沿 back
 */
public abstract class AbstractMultiBlockStructure {

    // ============================================================
    //  格子类型
    // ============================================================

    protected enum Cell {
        /** 不检查，允许任意方块 */
        SKIP,
        /** 内部空腔：必须为空，非空则加入 toClear */
        INTERIOR,
        /** 普通外壳：必须满足 isValidShell */
        SHELL,
        /** 主方块位置 */
        MASTER
    }

    // ============================================================
    //  特殊方块
    // ============================================================

    /** 特殊方块的种类 */
    public enum SpecialKind {
        /**
         * 输出方块：第一个被找到的该类型方块，
         * 其沿 facing 方向向外一格作为 outputPos。
         */
        OUTPUT,
        /**
         * 连接方块：第一个被找到的该类型方块，
         * 其沿 facing 方向向外一格必须为漏斗，位置作为 hopperLinkPos。
         */
        HOPPER_LINK
    }

    /** 特殊方块定义：状态识别 + 种类 */
    public record SpecialBlock(Predicate<BlockState> predicate, SpecialKind kind) {}

    // ============================================================
    //  验证结果
    // ============================================================

    public static final class Result {
        public final boolean valid;
        /** 失败原因（i18n 后缀，成功时为 null） */
        public final @Nullable String reason;
        /** 失败位置（可空） */
        public final @Nullable BlockPos failedPos;
        /** 失败位置处实际方块（可空） */
        public final @Nullable Block failedBlock;
        /** 每种特殊方块第一个被找到的位置（已向外一格） */
        public final Map<SpecialKind, BlockPos> specials;
        /** 内部空腔中需要清空的非空气方块 */
        public final List<BlockPos> toClear;

        Result(boolean valid, @Nullable String reason, @Nullable BlockPos failedPos, @Nullable Block failedBlock,
               Map<SpecialKind, BlockPos> specials, List<BlockPos> toClear) {
            this.valid = valid;
            this.reason = reason;
            this.failedPos = failedPos;
            this.failedBlock = failedBlock;
            this.specials = specials;
            this.toClear = toClear;
        }

        public @Nullable BlockPos get(SpecialKind kind) {
            return specials.get(kind);
        }

        static Result invalid(String reason) {
            return new Result(false, reason, null, null, Map.of(), List.of());
        }

        static Result invalidAt(String reason, BlockPos pos, BlockState state) {
            return new Result(false, reason, pos, state.getBlock(), Map.of(), List.of());
        }

        static final Result INVALID = invalid("invalid_structure");
    }

    // ============================================================
    //  缓存
    // ============================================================

    private final Map<BlockPos, Result> cache = new HashMap<>();

    public Result validate(World world, BlockPos masterPos, BlockState masterState,
                           Predicate<BlockState> isValidShell,
                           List<SpecialBlock> specials) {
        Result cached = cache.get(masterPos);
        if (cached != null) {
            return cached;
        }
        Result r = doValidate(world, masterPos, masterState, isValidShell, specials);
        // 只缓存成功结果：失败不缓存，方便玩家补齐结构后再次验证
        if (r.valid) cache.put(masterPos, r);
        return r;
    }

    public void invalidate(BlockPos masterPos) {
        cache.remove(masterPos);
    }

    public void clearCache() {
        cache.clear();
    }

    // ============================================================
    //  子类实现：形状描述
    // ============================================================

    /** 结构在 right 方向的尺寸 */
    protected abstract int sizeX();
    /** 结构在 up 方向的尺寸 */
    protected abstract int sizeY();
    /** 结构在 back 方向的尺寸 */
    protected abstract int sizeZ();

    /** 主方块在局部坐标中的位置 */
    protected abstract BlockPos masterLocal();

    /** 从主方块状态取得朝向，返回 null 表示朝向不可用 */
    protected abstract @Nullable Direction getFacing(BlockState masterState);

    /** 分类一个局部坐标的格子 */
    protected abstract Cell classify(int sx, int sy, int sz);


    /**
     * 检查验证结果是否有效。
     * 默认要求所有 {@link SpecialKind} 都已找到。
     * 如果某些结构不需要某个特殊方块，可覆盖。
     */
    protected boolean isResultValid(Map<SpecialKind, BlockPos> specials) {
        for (SpecialKind k : SpecialKind.values()) {
            if (!specials.containsKey(k)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 输出方块向外一格是否必须落在结构外。
     * 默认 true。
     */
    protected boolean outputMustBeOutside() {
        return true;
    }

    /**
     * 连接方块向外一格是否必须是漏斗。
     * 默认 true。
     */
    protected boolean hopperLinkMustBeHopper() {
        return true;
    }

    // ============================================================
    //  核心算法
    // ============================================================

    private Result doValidate(World world, BlockPos masterPos, BlockState masterState,
                              Predicate<BlockState> isValidShell,
                              List<SpecialBlock> specials) {
        Direction facing = getFacing(masterState);
        if (facing == null) {
            return Result.invalid("facing");
        }

        Direction right = facing.rotateYClockwise();
        Direction back  = facing.getOpposite();
        Direction up    = Direction.UP;

        BlockPos ml = masterLocal();
        BlockPos origin = masterPos
                .offset(right, -ml.getX())
                .offset(up,    -ml.getY())
                .offset(back,  -ml.getZ());

        int xMax = sizeX();
        int yMax = sizeY();
        int zMax = sizeZ();

        Map<SpecialKind, BlockPos> found = new HashMap<>();
        List<BlockPos> toClear = new ArrayList<>();
        BlockPos.Mutable cursor = new BlockPos.Mutable();

        for (int sz = 0; sz < zMax; sz++) {
            for (int sy = 0; sy < yMax; sy++) {
                for (int sx = 0; sx < xMax; sx++) {
                    Cell cell = classify(sx, sy, sz);
                    if (cell == Cell.SKIP) {
                        continue;
                    }

                    cursor.set(origin).move(right, sx).move(up, sy).move(back, sz);
                    BlockPos p = cursor.toImmutable();

                    // 内部空腔
                    if (cell == Cell.INTERIOR) {
                        if (!world.getBlockState(p).isAir()) {
                            toClear.add(p);
                        }
                        continue;
                    }

                    BlockState s = world.getBlockState(p);

                    // 主方块
                    if (cell == Cell.MASTER) {
                        if (!p.equals(masterPos)) {
                            return Result.invalidAt("master", p, s);
                        }
                        continue;
                    }

                    // 外壳：先匹配特殊方块
                    boolean handled = false;
                    for (SpecialBlock sp : specials) {
                        if (!sp.predicate().test(s)) {
                            continue;
                        }
                        handled = true;
                        if (found.containsKey(sp.kind())) {
                            break;
                        }

                        switch (sp.kind()) {
                            case OUTPUT -> {
                                BlockPos out = p.offset(outwardOf(p, origin, right, up, back, xMax, yMax, zMax, facing));
                                if (outputMustBeOutside()
                                        && isInside(out, origin, right, up, back, xMax, yMax, zMax)) {
                                    return Result.invalidAt("output", p, s);
                                }
                                found.put(SpecialKind.OUTPUT, out);
                            }
                            case HOPPER_LINK -> {
                                BlockPos h = p.offset(outwardOf(p, origin, right, up, back, xMax, yMax, zMax, facing));
                                if (hopperLinkMustBeHopper()
                                        && !world.getBlockState(h).isOf(Blocks.HOPPER)) {
                                    return Result.invalidAt("hopper", h, world.getBlockState(h));
                                }
                                found.put(SpecialKind.HOPPER_LINK, h);
                            }
                        }
                        break;
                    }
                    if (handled) {
                        continue;
                    }

                    // 普通外壳
                    if (!isValidShell.test(s)) return Result.invalidAt("shell", p, s);
                }
            }
        }

        if (!isResultValid(found)) {
            for (SpecialKind k : SpecialKind.values()) {
                if (!found.containsKey(k)) {
                    return Result.invalid(k == SpecialKind.HOPPER_LINK ? "input_missing" : "output_missing");
                }
            }
            return Result.invalid("invalid_structure");
        }
        return new Result(true, null, null, null, found, toClear);
    }

    // ============================================================
    //  工具
    // ============================================================

    protected static boolean isInside(BlockPos p, BlockPos origin,
                                      Direction right, Direction up, Direction back,
                                      int xMax, int yMax, int zMax) {
        BlockPos d = p.subtract(origin);
        int sx = dot(d, right);
        int sy = dot(d, up);
        int sz = dot(d, back);
        return sx >= 0 && sx < xMax
                && sy >= 0 && sy < yMax
                && sz >= 0 && sz < zMax;
    }

    protected static int dot(BlockPos v, Direction d) {
        return v.getX() * d.getOffsetX()
                + v.getY() * d.getOffsetY()
                + v.getZ() * d.getOffsetZ();
    }

    /**
     * 特殊方块（输入口/产物口）朝结构外的方向：由该方块所在的边界决定（从结构中心向外），
     * 而不是固定用主方块朝向，避免漏斗等偏移到结构内部与外壳重叠。
     */
    protected static Direction outwardOf(BlockPos p, BlockPos origin, Direction right, Direction up, Direction back,
                                         int xMax, int yMax, int zMax, Direction fallback) {
        BlockPos d = p.subtract(origin);
        int sx = dot(d, right);
        int sy = dot(d, up);
        int sz = dot(d, back);
        if (sz == 0) {
            return back.getOpposite();
        }
        if (sz == zMax - 1) {
            return back;
        }
        if (sx == 0) {
            return right.getOpposite();
        }
        if (sx == xMax - 1) {
            return right;
        }
        if (sy == 0) {
            return up.getOpposite();
        }
        if (sy == yMax - 1) {
            return up;
        }
        return fallback;
    }
}
