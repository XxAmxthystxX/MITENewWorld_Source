package com.mitenewworld.network;

import com.mitenewworld.MITENewWorld;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

/**
 * 客户端 → 服务器：
 *  - READY 状态点击 = 请求开始打造（grade/slider/cycleTick 无意义，填 0）
 *  - FORGING 状态点击 = 小游戏判定结果
 *
 * 服务器只做范围校验，不严格反作弊。
 */
public record MinigameClickC2SPacket(BlockPos pos, int grade, int sliderPosition, int cycleTick) implements CustomPayload {

    public static final Id<MinigameClickC2SPacket> ID =
            new Id<>(Identifier.of(MITENewWorld.MOD_ID, "casting_minigame_click"));

    public static final PacketCodec<RegistryByteBuf, MinigameClickC2SPacket> CODEC =
            PacketCodec.tuple(
                    BlockPos.PACKET_CODEC, MinigameClickC2SPacket::pos,
                    PacketCodecs.VAR_INT, MinigameClickC2SPacket::grade,
                    PacketCodecs.VAR_INT, MinigameClickC2SPacket::sliderPosition,
                    PacketCodecs.VAR_INT, MinigameClickC2SPacket::cycleTick,
                    MinigameClickC2SPacket::new
            );

    @Override
    public Id<? extends CustomPayload> getId() { return ID; }
}