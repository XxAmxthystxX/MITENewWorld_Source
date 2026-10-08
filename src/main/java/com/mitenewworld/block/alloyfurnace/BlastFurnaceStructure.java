package com.mitenewworld.block.alloyfurnace;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.core.AbstractMultiBlockStructure;
import net.fabricmc.loader.impl.game.patch.GameTransformer;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import java.util.ArrayList;
import java.util.List;

/**
 * 高炉多方块结构（5×5×5）。
 *
 * - 棱（非顶点）不计入
 * - 顶点计入
 * - 内部 3×3×3 必须为空
 * - 主方块位于正面 3×3 的第 8 格，局部坐标 (2, 1, 0)
 */
public final class BlastFurnaceStructure extends AbstractMultiBlockStructure {

    public static final BlastFurnaceStructure INSTANCE = new BlastFurnaceStructure();

    private static final int SIZE = 5;
    private static final int INTERIOR_MIN = 1;
    private static final int INTERIOR_MAX = 3;
    private static final BlockPos MASTER_LOCAL = new BlockPos(2, 1, 0);

    private BlastFurnaceStructure() {}

    @Override protected int sizeX() { return SIZE; }
    @Override protected int sizeY() { return SIZE; }
    @Override protected int sizeZ() { return SIZE; }

    @Override protected BlockPos masterLocal() { return MASTER_LOCAL; }

    @Override protected @Nullable Direction getFacing(BlockState state) {
        return state.contains(Properties.HORIZONTAL_FACING)
                ? state.get(Properties.HORIZONTAL_FACING)
                : null;
    }

    @Override protected Cell classify(int sx, int sy, int sz) {
        if (isInterior(sx, sy, sz)) {
            return Cell.INTERIOR;
        }
        if (isOnEdge(sx, sy, sz) && !isVertex(sx, sy, sz)) {
            return Cell.SKIP;
        }
        if (sx == MASTER_LOCAL.getX()
                && sy == MASTER_LOCAL.getY()
                && sz == MASTER_LOCAL.getZ()) {
            return Cell.MASTER;
        }
        return Cell.SHELL;
    }

    private static boolean isOnEdge(int sx, int sy, int sz) {
        int c = 0;
        if (sx == 0 || sx == SIZE - 1) {
            c++;
        }
        if (sy == 0 || sy == SIZE - 1) {
            c++;
        }
        if (sz == 0 || sz == SIZE - 1) {
            c++;
        }
        return c == 2;
    }

    private static boolean isVertex(int sx, int sy, int sz) {
        return (sx == 0 || sx == SIZE - 1)
                && (sy == 0 || sy == SIZE - 1)
                && (sz == 0 || sz == SIZE - 1);
    }

    private static boolean isInterior(int sx, int sy, int sz) {
        return sx >= INTERIOR_MIN && sx <= INTERIOR_MAX
                && sy >= INTERIOR_MIN && sy <= INTERIOR_MAX
                && sz >= INTERIOR_MIN && sz <= INTERIOR_MAX;
    }

    /**
     * 枚举整个结构所占的方块坐标（核心 + 外壳 + 特殊方块），不含内部空腔。
     * 用于熔化时把整座炉子都变成岩浆。
     */
    public List<BlockPos> enumerateBlocks(BlockPos masterPos, BlockState masterState) {
        Direction facing = getFacing(masterState);
        if (facing == null) {
            return List.of();
        }
        Direction right = facing.rotateYClockwise();
        Direction back  = facing.getOpposite();
        Direction up    = Direction.UP;

        BlockPos ml = masterLocal();
        BlockPos origin = masterPos
                .offset(right, -ml.getX())
                .offset(up,    -ml.getY())
                .offset(back,  -ml.getZ());

        List<BlockPos> result = new ArrayList<>();
        BlockPos.Mutable cursor = new BlockPos.Mutable();
        for (int sz = 0; sz < sizeZ(); sz++) {
            for (int sy = 0; sy < sizeY(); sy++) {
                for (int sx = 0; sx < sizeX(); sx++) {
                    Cell cell = classify(sx, sy, sz);
                    if (cell == Cell.SKIP || cell == Cell.INTERIOR) {
                        continue;
                    }
                    cursor.set(origin).move(right, sx).move(up, sy).move(back, sz);
                    result.add(cursor.toImmutable());
                }
            }
        }
        return result;
    }
}
