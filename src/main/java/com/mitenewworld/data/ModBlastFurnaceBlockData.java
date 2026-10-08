package com.mitenewworld.data;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.math.BlockPos;

public record ModBlastFurnaceBlockData(BlockPos pos, int tier) {
    public static final PacketCodec<RegistryByteBuf, ModBlastFurnaceBlockData> CODEC =
            PacketCodec.tuple(BlockPos.PACKET_CODEC, ModBlastFurnaceBlockData::pos,
                    PacketCodecs.VAR_INT, ModBlastFurnaceBlockData::tier,
                    ModBlastFurnaceBlockData::new);
}
