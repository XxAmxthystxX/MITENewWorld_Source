package com.mitenewworld.data;
import com.mitenewworld.MITENewWorld;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.BlockPos;

public record ModFurnaceBlockData(BlockPos pos) {
    public static final PacketCodec<RegistryByteBuf, ModFurnaceBlockData> CODEC =
            PacketCodec.tuple(BlockPos.PACKET_CODEC, ModFurnaceBlockData::pos, ModFurnaceBlockData::new);

}

