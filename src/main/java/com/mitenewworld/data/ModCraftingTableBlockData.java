package com.mitenewworld.data;
import com.mitenewworld.MITENewWorld;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.BlockPos;

public record ModCraftingTableBlockData(BlockPos pos) {
    public static final PacketCodec<RegistryByteBuf, ModCraftingTableBlockData> CODEC =
            PacketCodec.tuple(BlockPos.PACKET_CODEC, ModCraftingTableBlockData::pos, ModCraftingTableBlockData::new);

}
