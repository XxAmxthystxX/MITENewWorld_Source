package com.mitenewworld.mixin.servermixin;
import com.mitenewworld.MITENewWorld;


import com.mitenewworld.network.StateUpdateS2CPacket;
import com.mitenewworld.core.shadow.ShadowHungerManager;
import com.mitenewworld.core.shadow.ShadowPlayerEntity;
import com.mitenewworld.recipe.ModCraftingRecipe;
import com.mitenewworld.recipe.ModFurnaceRecipe;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerRecipeBook;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    @Shadow public abstract ServerWorld getEntityWorld();

    @Shadow @Final private ServerRecipeBook recipeBook;

    /** 上次已同步给客户端的状态值，用于做变化检测（见 sendStateIfChanged） */
    @Unique private float lastSyncedHealth = -1.0f;
    @Unique private int lastSyncedFood = -1;
    @Unique private float lastSyncedSaturation = -1.0f;
    @Unique private int lastSyncedMaxFood = -1;
    @Unique private int lastSyncedWater = -1;
    @Unique private int lastSyncedMaxWater = -1;

    public ServerPlayerEntityMixin(World world, GameProfile profile) {
        super(world, profile);
    }


    @Inject(method = "onSpawn", at = @At("HEAD"))
    public void onSpawn(CallbackInfo ci) {
        int i = this.experienceLevel / 5;
        ((ShadowPlayerEntity) this).setPlayerMaxHealth(i);
        // 只解锁本模组的配方：原版工作台/熔炉已被禁用，把原版配方也塞进配方书没有意义，
        // 而且每次重生都会重新遍历一次全表
        List<RecipeEntry<?>> modRecipes = new ArrayList<>();
        for (RecipeEntry<?> entry : getEntityWorld().getRecipeManager().values()) {
            Recipe<?> recipe = entry.value();
            if (recipe instanceof ModCraftingRecipe || recipe instanceof ModFurnaceRecipe) {
                modRecipes.add(entry);
            }
        }
        recipeBook.unlockRecipes(modRecipes, (ServerPlayerEntity) (Object) this);
    }


    @Redirect(method = "playerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V"))
    public void sendPacket(ServerPlayNetworkHandler serverPlayNetworkHandler, Packet<?> packet) {
        if (packet instanceof HealthUpdateS2CPacket) {
            sendStateIfChanged();
        } else {
            serverPlayNetworkHandler.sendPacket(packet);
        }
    }
    @Inject(
            method = "playerTick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/server/network/ServerPlayerEntity;lastLevelScore:I",
                    ordinal = 0,
                    shift = At.Shift.AFTER,
                    opcode = Opcodes.GETFIELD)
    )
    private void updataMaxHealth(CallbackInfo ci) {
        sendStateIfChanged();
    }

    /**
     * 只在数值真正变化时才发状态包。
     * 原来两个注入点都无条件每 tick 发一次，静止的玩家也在 20 包/秒地刷。
     */
    @Unique
    private void sendStateIfChanged() {
        float health = this.getHealth();
        int foodLevel = hungerManager.getFoodLevel();
        float saturationLevel = hungerManager.getSaturationLevel();
        int maxFoodLevel = ((ShadowHungerManager) hungerManager).getMaxFoodLevel();
        int waterLevel = ((ShadowHungerManager) hungerManager).getWaterLevel();
        int maxWaterLevel = ((ShadowHungerManager) hungerManager).getMaxWaterLevel();

        if (Float.compare(health, lastSyncedHealth) == 0
                && foodLevel == lastSyncedFood
                && Float.compare(saturationLevel, lastSyncedSaturation) == 0
                && maxFoodLevel == lastSyncedMaxFood
                && waterLevel == lastSyncedWater
                && maxWaterLevel == lastSyncedMaxWater) {
            return;
        }

        lastSyncedHealth = health;
        lastSyncedFood = foodLevel;
        lastSyncedSaturation = saturationLevel;
        lastSyncedMaxFood = maxFoodLevel;
        lastSyncedWater = waterLevel;
        lastSyncedMaxWater = maxWaterLevel;

        ServerPlayNetworking.send((ServerPlayerEntity) (Object) this, new StateUpdateS2CPacket(health, saturationLevel, foodLevel, maxFoodLevel, waterLevel, maxWaterLevel));
    }

    @Inject(method = "trySleep", at = @At(value = "HEAD"), cancellable = true)
    private void FixedTrySleep(BlockPos pos, CallbackInfoReturnable<Either<SleepFailureReason, Unit>> cir) {
        cir.cancel();
        Direction direction = this.getEntityWorld().getBlockState(pos).get(HorizontalFacingBlock.FACING);
        if (this.isSleeping() || !this.isAlive()) {
            cir.setReturnValue(Either.left(PlayerEntity.SleepFailureReason.OTHER_PROBLEM));
        } else if (!this.getEntityWorld().getDimension().natural()) {
            cir.setReturnValue(Either.left(PlayerEntity.SleepFailureReason.NOT_POSSIBLE_HERE));
        } else if (!this.isBedWithinRange(pos, direction)) {
            cir.setReturnValue(Either.left(PlayerEntity.SleepFailureReason.TOO_FAR_AWAY));
        } else if (this.isBedObstructed(pos, direction)) {
            cir.setReturnValue(Either.left(PlayerEntity.SleepFailureReason.OBSTRUCTED));
        } else {
            this.setSpawnPoint(new ServerPlayerEntity.Respawn( WorldProperties.SpawnPoint.create( this.getEntityWorld().getRegistryKey(), pos, this.getYaw(), 1.0f), true ) , true);
            if (!this.isCreative()) {
                Vec3d vec3d = Vec3d.ofBottomCenter(pos);
                List<HostileEntity> list = this.getEntityWorld()
                        .getEntitiesByClass(
                                HostileEntity.class,
                                new Box(vec3d.getX() - 8.0, vec3d.getY() - 5.0, vec3d.getZ() - 8.0, vec3d.getX() + 8.0, vec3d.getY() + 5.0, vec3d.getZ() + 8.0),
                                entity -> entity.isAngryAt(getEntityWorld(),(ServerPlayerEntity)(Object)this)
                        );
                if (!list.isEmpty()) {
                    cir.setReturnValue(Either.left(PlayerEntity.SleepFailureReason.NOT_SAFE));
                    return;
                }
            }

            Either<PlayerEntity.SleepFailureReason, Unit> either = super.trySleep(pos).ifRight(unit -> {
                this.incrementStat(Stats.SLEEP_IN_BED);
                Criteria.SLEPT_IN_BED.trigger((ServerPlayerEntity) (Object) this);
            });
            if (!this.getEntityWorld().isSleepingEnabled()) {
                this.sendMessage(Text.translatable("sleep.not_possible"), true);
            }

            this.getEntityWorld().updateSleepingPlayers();
            cir.setReturnValue(either);

        }

    }

    @Shadow
    private boolean isBedWithinRange(BlockPos pos, Direction direction) {
        return false;
    }

    @Shadow
    private boolean isBedObstructed(BlockPos pos, Direction direction) {
        return false;
    }

    @Shadow
    public void setSpawnPoint(@Nullable ServerPlayerEntity.Respawn respawn, boolean sendMessage) {
    }

}

