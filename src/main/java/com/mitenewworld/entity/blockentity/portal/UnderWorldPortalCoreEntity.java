package com.mitenewworld.entity.blockentity.portal;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.cover.BlocksCover;
import com.mitenewworld.registry.ModBlocks;
import com.mitenewworld.registry.ModEntities;
import com.mitenewworld.world.ModDimensionTypes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class UnderWorldPortalCoreEntity extends Entity {

    private BlockPos corePos;
    private int[] portalPos = new int[68];
    private int size = 0;
    private final BlockPos.Mutable mutable = new BlockPos.Mutable();
    private int lifetime = 0;
    /** 门的总寿命：20 刻/秒 × 600 秒 = 10 分钟 */
    private static final int MAX_LIFETIME = 20 * 600;
    /**
     * 侵蚀间隔：每 1000 刻（50 秒）从边缘向内蚀掉 1 个门方块。
     * 10 分钟寿命内约侵蚀 12 次，门自然衰减但不会提前塌光。
     */
    private static final int DECAY_INTERVAL = 1000;

    public UnderWorldPortalCoreEntity(EntityType<?> type, World world) {
        super(type, world);
        this.noClip = true;
        this.setNoGravity(true);
    }

    // =========================
    @Override
    public void tick() {
        World world = this.getEntityWorld();
        if (world.isClient()) {
            return;
        }
        ServerWorld targetWorld = getTargetWorld(world);
        lifetime++;
        if (lifetime > MAX_LIFETIME || corePos == null) {
            this.cleanRemainingPortals((ServerWorld) world);
            this.discard();
            return;
        }

        // 每 DECAY_INTERVAL 刻从边缘向内侵蚀 1 个门方块
        if (lifetime % DECAY_INTERVAL == 0) {
            if (targetWorld != null) {
                decay((ServerWorld) world, targetWorld);
            }
        }
    }

    // =========================
    // 辅助：获取目标世界
    private ServerWorld getTargetWorld(World world) {
        if (world.getRegistryKey() == World.OVERWORLD) {
            return world.getServer().getWorld(ModDimensionTypes.UNDERWORLD_WORLD_KEY);
        }
        if (world.getRegistryKey() == ModDimensionTypes.UNDERWORLD_WORLD_KEY) {
            return world.getServer().getWorld(World.OVERWORLD);
        }
        return null;
    }

    /**
     * 根据当前世界类型，返回传送门方块相对于核心的 Y 偏移。
     * 主世界：传送门在核心下方一层 (-1)
     * 地下世界：传送门在核心上方一层 (+1)
     */
    private int getPortalYOffset(World world) {
        if (world.getRegistryKey() == World.OVERWORLD) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * 侵蚀：每次调用从**当前最外层**蚀掉 1 个门方块，把它的偏移量从记录里移除，形成"边缘向内"的收缩。
     *
     * <p>距离度量用切比雪夫距离 {@code max(|dx|, |dz|)}：3×3 核心是距离 1，
     * 随机游走长出来的方块在最外层，所以总是先啃外围、最后才动核心。
     * 同一圈上若仍有存活方块，会优先挑**角落**（|dx| 与 |dz| 同时最大的那几个）。
     *
     * <p>若选中的方块已被外部移除（例如玩家自己挖掉、或另一侧先侵蚀过），
     * 就直接把它从记录里摘掉、跳过切换方块，下个周期继续——避免"卡死"在已消失的方块上。
     */
    private void decay(ServerWorld world, ServerWorld targetWorld) {
        if (size <= 0) {
            return;
        }

        int maxDist = 0;
        for (int i = 0; i < size; i++) {
            int d = chebyshev(portalPos[i * 2], portalPos[i * 2 + 1]);
            if (d > maxDist) {
                maxDist = d;
            }
        }

        int sourceYOffset = getPortalYOffset(world);

        // 收集最外层且在源世界仍是虚空之门的方块
        List<Integer> edge = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (chebyshev(portalPos[i * 2], portalPos[i * 2 + 1]) != maxDist) {
                continue;
            }
            BlockPos p = corePos.add(portalPos[i * 2], sourceYOffset, portalPos[i * 2 + 1]);
            if (world.getBlockState(p).isOf(ModBlocks.VOID_GATE)) {
                edge.add(i);
            }
        }

        // 最外层一个都不剩（都被挖了）：摘掉记录里所有已消失的，下个周期再算
        if (edge.isEmpty()) {
            for (int i = size - 1; i >= 0; i--) {
                BlockPos p = corePos.add(portalPos[i * 2], sourceYOffset, portalPos[i * 2 + 1]);
                if (!world.getBlockState(p).isOf(ModBlocks.VOID_GATE)) {
                    removeAt(i);
                }
            }
            return;
        }

        // 优先挑角落：|dx| 与 |dz| 同时最大的那几个
        int cornerScore = -1;
        for (int idx : edge) {
            int s = Math.abs(portalPos[idx * 2]) + Math.abs(portalPos[idx * 2 + 1]);
            if (s > cornerScore) {
                cornerScore = s;
            }
        }
        List<Integer> corners = new ArrayList<>();
        for (int idx : edge) {
            if (Math.abs(portalPos[idx * 2]) + Math.abs(portalPos[idx * 2 + 1]) == cornerScore) {
                corners.add(idx);
            }
        }
        List<Integer> pick = corners.isEmpty() ? edge : corners;

        int chosen = pick.get(world.random.nextInt(pick.size()));
        int offsetX = portalPos[chosen * 2];
        int offsetZ = portalPos[chosen * 2 + 1];

        int targetYOffset = getPortalYOffset(targetWorld);

        // 源世界侧
        BlockPos sourcePortalPos = corePos.add(offsetX, sourceYOffset, offsetZ);
        if (world.getBlockState(sourcePortalPos).isOf(ModBlocks.VOID_GATE)) {
            world.setBlockState(sourcePortalPos, BlocksCover.BEDROCK.getDefaultState(), 3);
        }

        // 目标世界侧（X/Z 相同，Y 按世界类型固定转换）
        BlockPos targetCorePos = (world.getRegistryKey() == World.OVERWORLD)
                ? new BlockPos(corePos.getX(), 124, corePos.getZ())
                : new BlockPos(corePos.getX(), 0, corePos.getZ());
        BlockPos targetPortalPos = targetCorePos.add(offsetX, targetYOffset, offsetZ);
        if (targetWorld.getBlockState(targetPortalPos).isOf(ModBlocks.VOID_GATE)) {
            targetWorld.setBlockState(targetPortalPos, BlocksCover.BEDROCK.getDefaultState(), 3);
        }

        removeAt(chosen);
    }

    /** 切比雪夫距离：门是平面上的方块簇，只用水平偏移衡量圈层 */
    private static int chebyshev(int offsetX, int offsetZ) {
        return Math.max(Math.abs(offsetX), Math.abs(offsetZ));
    }

    /** 把第 index 条记录与末尾交换后 size--（顺序无关，故不保持原序） */
    private void removeAt(int index) {
        int last = (size - 1) * 2;
        portalPos[index * 2] = portalPos[last];
        portalPos[index * 2 + 1] = portalPos[last + 1];
        size--;
    }

    // =========================
    public void buildFromWorld(ServerWorld world, BlockPos core) {
        this.corePos = core;
        int portalYOffset = getPortalYOffset(world);
        mutable.set(core.add(0, portalYOffset, 0)); // 参考点设为传送门层
        Random random = world.random;
        final int SIZE = 7;
        final int CENTER = 3;
        boolean[][] portalMap = new boolean[SIZE][SIZE];
        int portalIndex = 0;
        final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        // 中心 3x3 方块
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                portalMap[i + CENTER][j + CENTER] = true;
                world.setBlockState(mutable.add(i, 0, j), ModBlocks.VOID_GATE.getDefaultState(), 3);
                size++;
                portalPos[portalIndex++] = i;
                portalPos[portalIndex++] = j;
            }
        }

        // 扩散外围方块
        int[][] frontier = new int[SIZE * SIZE][2];
        int frontierSize = 0;
        for (int i = 2; i <= 4; i++) {
            for (int j = 2; j <= 4; j++) {
                frontier[frontierSize][0] = i;
                frontier[frontierSize][1] = j;
                frontierSize++;
            }
        }

        int attempts = 25;
        while (attempts > 0 && frontierSize > 0) {
            int idx = random.nextInt(frontierSize);
            int x = frontier[idx][0];
            int z = frontier[idx][1];
            int[] dir = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
            int nx = x + dir[0];
            int nz = z + dir[1];

            if (nx >= 0 && nx < SIZE && nz >= 0 && nz < SIZE && !portalMap[nx][nz]) {
                portalMap[nx][nz] = true;
                BlockPos worldPos = mutable.add(nx - CENTER, 0, nz - CENTER);
                world.setBlockState(worldPos, ModBlocks.VOID_GATE.getDefaultState(), 3);
                size++;
                portalPos[portalIndex++] = nx - CENTER;
                portalPos[portalIndex++] = nz - CENTER;
                frontier[frontierSize][0] = nx;
                frontier[frontierSize][1] = nz;
                frontierSize++;
                attempts--;
            }
        }

        // 清空周围空间：7x3x7（X: -3~3, Z: -3~3, Y: 根据世界类型决定向上或向下3格）
        int yStart, yEnd;
        if (portalYOffset == -1) { // 主世界：向上清除（传送门在下方，清除上方空间）
            yStart = 0;
            yEnd = 4;
        } else { // 地下世界：向下清除（传送门在上方，清除下方空间）
            yStart = -3;
            yEnd = 0;
        }
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                for (int dy = yStart; dy <= yEnd; dy++) {
                    mutable.set(core.add(dx, dy, dz));
                    world.setBlockState(mutable, BlocksCover.AIR.getDefaultState(), 3);
                }
            }
        }
    }

    // =========================
    public void copyFromWorld(ServerWorld world, BlockPos core, int[] portalPos, int size) {
        this.corePos = core;
        this.size = size;
        System.arraycopy(portalPos, 0, this.portalPos, 0, size * 2);

        int portalYOffset = getPortalYOffset(world);

        for (int i = 0; i < size * 2; i += 2) {
            BlockPos pos = core.add(portalPos[i], portalYOffset, portalPos[i + 1]);
            world.setBlockState(pos, ModBlocks.VOID_GATE.getDefaultState(), 3);
        }

        // 清空周围空间（与 buildFromWorld 一致）
        int yStart, yEnd;
        if (portalYOffset == -1) {
            yStart = 0;
            yEnd = 4;
        } else {
            yStart = -3;
            yEnd = 0;
        }
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                for (int dy = yStart; dy <= yEnd; dy++) {
                    mutable.set(core.add(dx, dy, dz));
                    world.setBlockState(mutable, BlocksCover.AIR.getDefaultState(), 3);
                }
            }
        }
    }

    // =========================
    public static void create(ServerWorld world, BlockPos pos) {
        UnderWorldPortalCoreEntity entity1 = ModEntities.UNDER_WORLD_PORTAL_CORE_ENTITY.create(
                world, null, pos, SpawnReason.COMMAND, false, false
        );
        if (entity1 == null) {
            return;
        }

        entity1.buildFromWorld(world, pos);
        entity1.refreshPositionAndAngles(pos, 0, 0);
        world.spawnEntity(entity1);

        // 目标世界信息
        ServerWorld targetWorld;
        BlockPos targetPos;
        if (world.getRegistryKey() == World.OVERWORLD) {
            targetWorld = world.getServer().getWorld(ModDimensionTypes.UNDERWORLD_WORLD_KEY);
            if (targetWorld == null) {
                return;
            }
            targetPos = new BlockPos(pos.getX(), 124, pos.getZ());
        } else if (world.getRegistryKey() == ModDimensionTypes.UNDERWORLD_WORLD_KEY) {
            targetWorld = world.getServer().getWorld(World.OVERWORLD);
            if (targetWorld == null) {
                return;
            }
            targetPos = new BlockPos(pos.getX(), -62, pos.getZ());
        } else {
            return;
        }

        // 在目标世界创建实体
        UnderWorldPortalCoreEntity entity2 = ModEntities.UNDER_WORLD_PORTAL_CORE_ENTITY.create(
                targetWorld, null, targetPos, SpawnReason.COMMAND, false, false
        );
        if (entity2 == null) {
            return;
        }

        entity2.copyFromWorld(targetWorld, targetPos, entity1.portalPos, entity1.size);
        entity2.refreshPositionAndAngles(targetPos, 0, 0);
        targetWorld.spawnEntity(entity2);
    }

    // =========================
    @Override
    protected void writeCustomData(WriteView view) {
        if (corePos != null) {
            view.putInt("cx", corePos.getX());
            view.putInt("cy", corePos.getY());
            view.putInt("cz", corePos.getZ());
        }
        view.putIntArray("portalpos", portalPos);
        view.putInt("life", lifetime);
        view.putInt("size", size);
    }

    @Override
    protected void readCustomData(ReadView view) {
        corePos = new BlockPos(
                view.getInt("cx", 0),
                view.getInt("cy", 0),
                view.getInt("cz", 0)
        );
        lifetime = view.getInt("life", 0);
        portalPos = view.getOptionalIntArray("portalpos").orElse(new int[0]);
        size = view.getInt("size", 0);
    }

    // =========================
    private void cleanRemainingPortals(ServerWorld world) {
        ServerWorld targetWorld = getTargetWorld(world);
        if (targetWorld == null) {
            return;
        }

        int sourceYOffset = getPortalYOffset(world);
        int targetYOffset = getPortalYOffset(targetWorld);

        BlockPos targetCorePos;
        if (world.getRegistryKey() == World.OVERWORLD) {
            targetCorePos = new BlockPos(corePos.getX(), 124, corePos.getZ());
        } else {
            targetCorePos = new BlockPos(corePos.getX(), -62, corePos.getZ());
        }

        for (int i = 0; i < size * 2; i += 2) {
            int offsetX = portalPos[i];
            int offsetZ = portalPos[i + 1];

            // 源世界传送门
            BlockPos sourcePos = corePos.add(offsetX, sourceYOffset, offsetZ);
            if (world.getBlockState(sourcePos).isOf(ModBlocks.VOID_GATE)) {
                world.setBlockState(sourcePos, BlocksCover.BEDROCK.getDefaultState(), 3);
            }

            // 目标世界传送门
            BlockPos targetPos = targetCorePos.add(offsetX, targetYOffset, offsetZ);
            if (targetWorld.getBlockState(targetPos).isOf(ModBlocks.VOID_GATE)) {
                targetWorld.setBlockState(targetPos, BlocksCover.BEDROCK.getDefaultState(), 3);
            }
        }
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
    }
}
