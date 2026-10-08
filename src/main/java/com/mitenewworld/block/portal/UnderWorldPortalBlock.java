package com.mitenewworld.block.portal;

import com.mitenewworld.MITENewWorld;
import com.mitenewworld.world.ModDimensionTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.*;
import net.minecraft.world.border.WorldBorder;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class UnderWorldPortalBlock extends Block implements Portal {

    public static final MapCodec<UnderWorldPortalBlock> CODEC =
            NetherPortalBlock.createCodec(UnderWorldPortalBlock::new);

    private static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 9.0, 0.0, 16.0, 11.0, 16.0);

    public UnderWorldPortalBlock(Settings settings) {
        super(settings);
    }

    @Override
    public MapCodec<UnderWorldPortalBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }


    // =========================
    // 传送逻辑（核心）
    // =========================
    @Override
    public @Nullable TeleportTarget createTeleportTarget(ServerWorld world, Entity entity, BlockPos pos) {

        RegistryKey<World> targetKey;

        if (world.getRegistryKey() == World.OVERWORLD) {
            targetKey = ModDimensionTypes.UNDERWORLD_WORLD_KEY;
        } else if (world.getRegistryKey() == ModDimensionTypes.UNDERWORLD_WORLD_KEY) {
            targetKey = World.OVERWORLD;
        } else {
            return null;
        }

        ServerWorld targetWorld = world.getServer().getWorld(targetKey);
        if (targetWorld == null) {
            MITENewWorld.LOGGER.error("目标维度未加载: {}", targetKey);
            return null;
        }

        double x = entity.getX();
        double z = entity.getZ();

        WorldBorder border = targetWorld.getWorldBorder();
        x = MathHelper.clamp(x, border.getBoundWest(), border.getBoundEast());
        z = MathHelper.clamp(z, border.getBoundNorth(), border.getBoundSouth());
        int y = 0;
        // 安全附加效果
        if (entity instanceof LivingEntity living) {
            if (targetWorld.getRegistryKey() == World.OVERWORLD) {
                y = -61;
                living.addStatusEffect(new StatusEffectInstance( StatusEffects.LEVITATION, 60, 1, false, false, false ));
            } else if (targetWorld.getRegistryKey() == ModDimensionTypes.UNDERWORLD_WORLD_KEY) {
                y = 122;
                living.addStatusEffect(new StatusEffectInstance( StatusEffects.SLOW_FALLING, 60, 0, false, false, false ));
            }
        }


        return new TeleportTarget(
                targetWorld,
                new Vec3d(x, y, z),
                entity.getVelocity(),
                entity.getYaw(),
                entity.getPitch(),
                PositionFlag.combine(PositionFlag.DELTA, PositionFlag.ROT),
                TeleportTarget.SEND_TRAVEL_THROUGH_PORTAL_PACKET
        );
    }

    // =========================
    // 触发传送
    // =========================
    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler, boolean bl) {
        entity.tryUsePortal(this, pos);
    }





}
