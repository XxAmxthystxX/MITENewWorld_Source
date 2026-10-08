package com.mitenewworld.entity.blockentity.portal;
import com.mitenewworld.MITENewWorld;
import com.mitenewworld.registry.ModBlockEntities;

import com.mitenewworld.block.portal.WorldBreakerBlock;
import com.mitenewworld.world.ModDimensionTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;


public class WorldBreakerBlockEntity extends BlockEntity  {


    private int phaseTick = 0;       // 阶段计时
    private int currentRadius = 0;   // 门生成半径

    public WorldBreakerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WORLD_BREAKER_BLOCK_ENTITY, pos, state);
    }


    public void tick(World world, BlockPos pos, BlockState state) {
        if ( world.isClient()) {
            return;
        }

        if (!state.get(WorldBreakerBlock.ACTIVE)) {
            return;
        }


        int phase = state.get(WorldBreakerBlock.PHASE);

        phaseTick++;
        // 每 2 tick 执行一次逻辑
        if ((phaseTick & 1) != 0) return;

        ServerWorld serverWorld = (ServerWorld) world;
        Random random = serverWorld.getRandom();

        // 粒子和门生成
        switch (phase) {
            case 1 -> generatePhase1Particles(serverWorld, pos, random);
            case 2 -> generatePhase2Particles(serverWorld, pos, random);
            case 3 -> generatePhase3Particles(serverWorld, pos, random);
            case 4 -> generatePhase4Particles(serverWorld, pos, random);
        }

        // 阶段持续时间
        int phaseDuration = switch (phase) {
            case 1 -> 200;
            case 2 -> 300;
            case 3 -> 100;
            case 4 -> 50;
            default -> 0;
        };

        // 阶段切换
        if (phaseTick >= phaseDuration) {
            phaseTick = 0;
            if (phase < 4) {
                world.setBlockState(pos, state.with(WorldBreakerBlock.PHASE, phase + 1), Block.NOTIFY_ALL);
            } else {
                if (world.getRegistryKey() == World.OVERWORLD && pos.getY() == 1) {
                    UnderWorldPortalCoreEntity.create(serverWorld, pos);
                } else if (world.getRegistryKey() == ModDimensionTypes.UNDERWORLD_WORLD_KEY && pos.getY() == 124) {
                    UnderWorldPortalCoreEntity.create(serverWorld, pos);
                }
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
                serverWorld.createExplosion(null, pos.getX(), pos.getY() - 1, pos.getZ(), 256.0f, World.ExplosionSourceType.BLOCK);

            }
        }
    }


    @Override
    public void writeData(WriteView view) {
        super.writeData(view);
        view.putInt("phaseTick", phaseTick);
        view.putInt("currentRadius", currentRadius);
    }

    @Override
    public void readData(ReadView view) {
        super.readData(view);
        phaseTick = view.getInt("phaseTick" , 0);
        currentRadius = view.getInt("currentRadius" , 0);
    }


    public static void generatePhase1Particles(ServerWorld world, BlockPos pos, Random random) {
        for (int i = 0; i < 10; i++) {
            double startX = pos.getX() + random.nextDouble() * 14 - 7;
            double startY = pos.getY() + random.nextDouble() * 5;
            double startZ = pos.getZ() + random.nextDouble() * 14 - 7;

            double dx = (pos.getX() + 0.5 - startX) * 0.1;
            double dy = (pos.getY() + 0.5 - startY) * 0.1;
            double dz = (pos.getZ() + 0.5 - startZ) * 0.1;

            world.spawnParticles(
                    ParticleTypes.END_ROD,
                    startX, startY, startZ,
                    1, dx, dy, dz, 0.1
            );
        }
    }

    public static void generatePhase2Particles(ServerWorld world, BlockPos pos, Random random) {
        for (int i = 0; i < 40; i++) {
            double startX = pos.getX() + random.nextDouble() * 16 - 8;
            double startY = pos.getY() + random.nextDouble() * 6;
            double startZ = pos.getZ() + random.nextDouble() * 16 - 8;

            double dx = (pos.getX() + 0.5 - startX) * 0.15;
            double dy = (pos.getY() + 0.5 - startY) * 0.15;
            double dz = (pos.getZ() + 0.5 - startZ) * 0.15;

            world.spawnParticles(
                    ParticleTypes.PORTAL,
                    startX, startY, startZ,
                    1, dx, dy, dz, 0.1
            );
        }
    }

    public static void generatePhase3Particles(ServerWorld world, BlockPos pos, Random random) {
        for (int i = 0; i < 20; i++) {
            double x = pos.getX() + random.nextDouble() * 1 - 0.5;
            double z = pos.getZ() + random.nextDouble() * 1 - 0.5;
            double y = pos.getY();
            world.spawnParticles(ParticleTypes.SOUL, x, y, z, 2, 0.1, 0.5, 0.1, 1.0); // 红色粉尘
        }
    }

    public static void generatePhase4Particles(ServerWorld world, BlockPos pos, Random random) {
        for (int i = 0; i < 20; i++) {
            double x = pos.getX() + random.nextDouble() * 1 - 0.5;
            double z = pos.getZ() + random.nextDouble() * 1 - 0.5;
            double y = pos.getY();

            world.spawnParticles(
                    ParticleTypes.END_ROD,
                    x, y, z,
                    1, 0, 0.5 + random.nextDouble(), 0, 0.1
            );

        }
    }





}
