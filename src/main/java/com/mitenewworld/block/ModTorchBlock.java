package com.mitenewworld.block;
import com.mitenewworld.MITENewWorld;

import com.google.common.collect.Maps;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.K1;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;


public class ModTorchBlock extends AbstractTorchBlock {
     public static final BooleanProperty LIT = Properties.LIT;
     protected static final MapCodec<SimpleParticleType> PARTICLE_TYPE_CODEC = Registries.PARTICLE_TYPE.getCodec().comapFlatMap(particleType -> {
        DataResult<SimpleParticleType> dataResult;
        if (particleType instanceof SimpleParticleType simpleParticleType) {
            dataResult = DataResult.success(simpleParticleType);
        } else {
            dataResult = DataResult.error(() -> "Not a SimpleParticleType: " + particleType);
        }
        return dataResult;
    }, simpleParticleType ->simpleParticleType).fieldOf("particle_options");
    public static final MapCodec<ModTorchBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    PARTICLE_TYPE_CODEC.forGetter(block -> block.particle),
                    TorchBlock.createSettingsCodec())
                    .apply(instance, (particle, settings) -> new ModTorchBlock(settings, particle)));
    protected final SimpleParticleType particle;


    public ModTorchBlock(Settings settings, SimpleParticleType particle) {
        super(settings);
        this.particle = particle;
        this.setDefaultState(this.stateManager.getDefaultState().with(LIT, false));
    }

    @Override
    protected MapCodec<? extends AbstractTorchBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }
        if (stack.getItem() instanceof FlintAndSteelItem) {
            setLit((ServerWorld) world, pos, !state.get(LIT));
            stack.damage(1, player);
            return ActionResult.SUCCESS;
        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    public void setLit(ServerWorld world, BlockPos pos, boolean lit) {
        world.setBlockState(pos, this.getDefaultState().with(LIT, lit),Block.NOTIFY_LISTENERS);
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(LIT)) {
            double d = (double)pos.getX() + 0.5;
            double e = (double)pos.getY() + 0.7;
            double f = (double)pos.getZ() + 0.5;
            world.addParticleClient(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
            world.addParticleClient(this.particle, d, e, f, 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }


}
