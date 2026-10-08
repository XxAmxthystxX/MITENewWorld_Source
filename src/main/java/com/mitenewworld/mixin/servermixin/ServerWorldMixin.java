package com.mitenewworld.mixin.servermixin;
import com.mitenewworld.MITENewWorld;


import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.debug.SubscriptionTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerEntityManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.SleepManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.village.raid.RaidManager;
import net.minecraft.world.EntityList;
import net.minecraft.world.GameRules;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.debug.DebugSubscriptionTypes;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.tick.TickManager;
import net.minecraft.world.tick.WorldTickScheduler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
    @Shadow @Final private MinecraftServer server;
    @Shadow
    private final SleepManager sleepManager = new SleepManager();
    @Shadow
    private boolean inBlockTick;
    @Shadow
    private int idleTimeout;
    @Mutable
    @Final
    @Shadow
    private final ServerChunkManager chunkManager;
    @Mutable
    @Final
    @Shadow
    private final ServerEntityManager<Entity> entityManager;
    @Shadow
    final EntityList entityList = new EntityList();
    @Shadow
    @Nullable
    private EnderDragonFight enderDragonFight;
    @Shadow
    final List<ServerPlayerEntity> players = Lists.newArrayList();
    @Mutable
    @Final
    @Shadow
    protected final RaidManager raidManager;
    @Shadow
    private final WorldTickScheduler<Block> blockTickScheduler = new WorldTickScheduler<>(this::isTickingFutureReady);
    @Shadow
    private final WorldTickScheduler<Fluid> fluidTickScheduler = new WorldTickScheduler<>(this::isTickingFutureReady);
    @Mutable
    @Final
    @Shadow
    final SubscriptionTracker subscriptionTracker ;
    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates, ServerChunkManager chunkManager, ServerEntityManager<Entity> entityManager, RaidManager raidManager, SubscriptionTracker subscriptionTracker) {
        super(properties, registryRef, registryManager, dimensionEntry, isClient, debugWorld, seed, maxChainedNeighborUpdates);
        this.chunkManager = chunkManager;
        this.entityManager = entityManager;
        this.raidManager = raidManager;
        this.subscriptionTracker = subscriptionTracker;
    }


    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        ci.cancel();
        long l;
        int i;
        Profiler profiler = Profilers.get();
        this.inBlockTick = true;
        TickManager tickManager = this.getTickManager();
        boolean bl = tickManager.shouldTick();
        if (bl) {
            profiler.push("world border");
            this.getWorldBorder().tick();
            profiler.swap("weather");
            this.tickWeather();
            profiler.pop();
        }
        if (this.sleepManager.canSkipNight(i = this.getGameRules().getInt(GameRules.PLAYERS_SLEEPING_PERCENTAGE)) && this.sleepManager.canResetTime(i, this.players)) {
            if (this.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
                l = this.properties.getTimeOfDay() + 19L;
                this.setTimeOfDay(l - l % 24000L);
            }
            if (random.nextBetween(0 , 100) > 80 && this.getGameRules().getBoolean(GameRules.DO_WEATHER_CYCLE) && this.isRaining()) {
                this.resetWeather();
            }
        }
        this.calculateAmbientDarkness();
        if (bl) {
            this.tickTime();
        }
        profiler.push("tickPending");
        if (!this.isDebugWorld() && bl) {
            l = this.getTime();
            profiler.push("blockTicks");
            this.blockTickScheduler.tick(l, 65536, this::tickBlock);
            profiler.swap("fluidTicks");
            this.fluidTickScheduler.tick(l, 65536, this::tickFluid);
            profiler.pop();
        }
        profiler.swap("raid");
        if (bl) {
            this.raidManager.tick((ServerWorld)(Object)this);
        }
        profiler.swap("chunkSource");
        this.getChunkManager().tick(shouldKeepTicking, true);
        profiler.swap("blockEvents");
        if (bl) {
            this.processSyncedBlockEvents();
        }
        this.inBlockTick = false;
        profiler.pop();
        boolean bl2 = this.chunkManager.shouldResetIdleTimeout();
        if (bl2) {
            this.resetIdleTimeout();
        }
        if (bl) {
            ++this.idleTimeout;
        }
        if (this.idleTimeout < 300) {
            profiler.push("entities");
            if (this.enderDragonFight != null && bl) {
                profiler.push("dragonFight");
                this.enderDragonFight.tick();
                profiler.pop();
            }
            this.entityList.forEach(entity -> {
                if (entity.isRemoved()) {
                    return;
                }
                if (tickManager.shouldSkipTick((Entity)entity)) {
                    return;
                }
                profiler.push("checkDespawn");
                entity.checkDespawn();
                profiler.pop();
                if (!(entity instanceof ServerPlayerEntity) && !this.chunkManager.chunkLoadingManager.getLevelManager().shouldTickEntities(entity.getChunkPos().toLong())) {
                    return;
                }
                Entity entity2 = entity.getVehicle();
                if (entity2 != null) {
                    if (entity2.isRemoved() || !entity2.hasPassenger((Entity)entity)) {
                        entity.stopRiding();
                    } else {
                        return;
                    }
                }
                profiler.push("tick");
                this.tickEntity(this::tickEntity, entity);
                profiler.pop();
            });
            profiler.swap("blockEntities");
            this.tickBlockEntities();
            profiler.pop();
        }
        profiler.push("entityManagement");
        this.entityManager.tick();
        profiler.pop();
        profiler.push("debugSynchronizers");
        if (this.subscriptionTracker.isSubscribed(DebugSubscriptionTypes.NEIGHBOR_UPDATES)) {
            this.neighborUpdater.setNeighborUpdateCallback(pos -> this.subscriptionTracker.sendEventDebugData((BlockPos)pos, DebugSubscriptionTypes.NEIGHBOR_UPDATES, pos));
        } else {
            this.neighborUpdater.setNeighborUpdateCallback(null);
        }
        this.subscriptionTracker.tick(this.server.getSubscriberTracker());
        profiler.pop();
    }

    @Shadow
    public GameRules getGameRules() {
        return null;
    }

    @Shadow
    public void tickEntity(Entity entity) {
    }

    @Shadow
    protected void tickTime() {
    }

    @Shadow
    private void tickWeather() {
    }

    @Shadow
    public void setTimeOfDay(long timeOfDay) {
    }

    @Shadow
    public void resetWeather() {
    }

    @Shadow
    private boolean isTickingFutureReady(long chunkPos) {
        return false;
    }

    @Shadow
    private void tickFluid(BlockPos pos, Fluid fluid) {
    }

    @Shadow
    private void tickBlock(BlockPos pos, Block block) {
    }

    @Shadow
    private void processSyncedBlockEvents() {
    }

    @Shadow
    public LongSet getForcedChunks() {
        return null;
    }

    @Shadow
    public void resetIdleTimeout() {
    }



}
