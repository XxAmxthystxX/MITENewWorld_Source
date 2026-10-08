package com.mitenewworld;

import com.mitenewworld.entity.blockentity.castingtable.ModCastingTableEntity;
import com.mitenewworld.network.MinigameClickC2SPacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

/**
 * 服务端逻辑入口。
 *
 * <p>这里刻意实现 {@link ModInitializer} 而不是 {@code DedicatedServerModInitializer}：
 * 后者<b>只在专用服务器</b>触发，单人 / 局域网主机的集成服务器不会执行，
 * 导致小游戏点击包（{@link MinigameClickC2SPacket}）的接收器注册不上、铸造按钮毫无反应。
 * 用 {@code ModInitializer} 可保证专用服务器与集成服务器都会注册。
 */
public class MITENewWorldServer implements ModInitializer {

    @Override
    public void onInitialize() {
        registerCastingMinigameReceiver();
    }

    private void registerCastingMinigameReceiver() {
        ServerPlayNetworking.registerGlobalReceiver(MinigameClickC2SPacket.ID,
                (payload, context) -> {
                    ServerPlayerEntity player = context.player();
                    World world = player.getEntityWorld();
                    BlockEntity be = world.getBlockEntity(payload.pos());
                    if (!(be instanceof ModCastingTableEntity table)) {
                        return;
                    }

                    // 轻量范围校验
                    if (payload.grade() < -2 || payload.grade() > 2) {
                        return;
                    }
                    if (payload.sliderPosition() < 0 || payload.sliderPosition() > 100) {
                        return;
                    }
                    if (payload.cycleTick() < 0 || payload.cycleTick() > 200) {
                        return;
                    }

                    // 距离与视线都不过度校验，交给方块占用锁兜底
                    table.onMinigameClick(player, payload.grade());
                });
    }
}
