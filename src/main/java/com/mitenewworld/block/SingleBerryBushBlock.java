package com.mitenewworld.block;
import com.mitenewworld.MITENewWorld;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;

/**
 * 通用浆果丛基类（支持任意年龄范围、成熟自动重置、采摘掉落）
 */
public class SingleBerryBushBlock extends PlantBlock implements Fertilizable {


    public static final MapCodec<SingleBerryBushBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Block.createSettingsCodec(),
                    RegistryKey.createCodec(RegistryKeys.LOOT_TABLE).fieldOf("loot_table").forGetter(b -> b.lootTable),
                    Codecs.rangedInt(0, 32).fieldOf("max_age").forGetter(b -> b.maxAge),
                    Codecs.rangedInt(0, 32).fieldOf("reset_age").forGetter(b -> b.resetAge)
            ).apply(instance, SingleBerryBushBlock::new)
    );

    public static final int DEFAULT_MAX_AGE = 3;
    public static final int DEFAULT_RESET_AGE = 1;

    protected static final IntProperty ageProperty = SweetBerryBushBlock.AGE;
    protected final int maxAge;
    protected final int resetAge;
    protected final RegistryKey<LootTable> lootTable;

    // 形状
    protected VoxelShape smallShape = Block.createColumnShape(10.0, 0.0, 8.0);
    protected VoxelShape largeShape = Block.createColumnShape(14.0, 0.0, 16.0);
    protected VoxelShape fullShape = VoxelShapes.fullCube();

    public SingleBerryBushBlock(Settings settings, RegistryKey<LootTable> lootTable, int maxAge, int resetAge) {
        super(settings);
        this.lootTable = lootTable;
        this.maxAge = maxAge;
        this.resetAge = resetAge;

        this.setDefaultState(this.stateManager.getDefaultState().with(ageProperty, 0));
    }

    public SingleBerryBushBlock(Settings settings) {
        this(settings, LootTables.SWEET_BERRY_BUSH_HARVEST, DEFAULT_MAX_AGE, DEFAULT_RESET_AGE);
    }

    protected double getGrowthProbability() { return 1.0 / 17.0; }
    protected int getMinLightLevelForGrowth() { return 9; }
    protected double getResetProbability() { return 1.0 / 17.0; }

    protected SoundEvent getHarvestSound() { return SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES; }
    protected RegistryKey<LootTable> getHarvestLootTableId() { return lootTable; }

    @Override
    protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
        return ItemStack.EMPTY;
    }

    protected VoxelShape getShapeForAge(int age) {
        if (age == 0) {
            return smallShape;
        }
        if (age == maxAge) {
            return fullShape;
        }
        return largeShape;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShapeForAge(state.get(ageProperty));
    }

    @Override
    protected boolean hasRandomTicks(BlockState state) {
        int age = state.get(ageProperty);
        return age < maxAge || (age == maxAge && resetAge < maxAge);
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int age = state.get(ageProperty);
        if (age < maxAge) {
            if (random.nextDouble() < getGrowthProbability()
                    && world.getBaseLightLevel(pos.up(), 0) >= getMinLightLevelForGrowth()) {
                world.setBlockState(pos, state.with(ageProperty, age + 1), Block.NOTIFY_LISTENERS);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(state.with(ageProperty, age + 1)));
            }
        } else if (age == maxAge && resetAge < maxAge) {
            if (random.nextDouble() < getResetProbability()) {
                world.setBlockState(pos, state.with(ageProperty, resetAge), Block.NOTIFY_LISTENERS);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(state.with(ageProperty, resetAge)));
            }
        }
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler, boolean bl) {
        if (entity instanceof LivingEntity && entity.getType() != EntityType.FOX && entity.getType() != EntityType.BEE) {
            entity.slowMovement(state, new Vec3d(0.8F, 0.75, 0.8F));
            if (world instanceof ServerWorld serverWorld && state.get(ageProperty) != 0) {
                Vec3d vec3d = entity.isControlledByPlayer() ? entity.getMovement() : entity.getLastRenderPos().subtract(entity.getEntityPos());
                if (vec3d.horizontalLengthSquared() > 0.0) {
                    double d = Math.abs(vec3d.getX());
                    double e = Math.abs(vec3d.getZ());
                    if (d >= 0.003F || e >= 0.003F) {
                        entity.damage(serverWorld, world.getDamageSources().sweetBerryBush(), 1.0F);
                    }
                }
            }
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        int age = state.get(ageProperty);
        if (age > 1) {
            if (world instanceof ServerWorld serverWorld) {
                Block.generateBlockInteractLoot(
                        serverWorld,
                        getHarvestLootTableId(),
                        state,
                        world.getBlockEntity(pos),
                        null,
                        player,
                        (w, stack) -> Block.dropStack(w, pos, stack)
                );
                serverWorld.playSound(null, pos, getHarvestSound(), SoundCategory.BLOCKS, 1.0F,
                        0.8F + serverWorld.random.nextFloat() * 0.4F);
                serverWorld.setBlockState(pos, state.with(ageProperty, resetAge), Block.NOTIFY_LISTENERS);
                serverWorld.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, state.with(ageProperty, resetAge)));
            }
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hit);
    }


    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(ageProperty);
    }


    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        int newAge = Math.min(maxAge, state.get(ageProperty) + 1);
        world.setBlockState(pos, state.with(ageProperty, newAge), Block.NOTIFY_LISTENERS);
    }


    @Override
    protected MapCodec<? extends PlantBlock> getCodec() {
        return CODEC;
    }
}
