package com.mitenewworld.network;

import com.mitenewworld.MITENewWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class StateUpdateS2CPacket implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, StateUpdateS2CPacket> CODEC = PacketCodec.of(StateUpdateS2CPacket::write, StateUpdateS2CPacket::new);
    public static final Id<StateUpdateS2CPacket> ID =
            new CustomPayload.Id<>(Identifier.of(MITENewWorld.MOD_ID, "state_update_s2c"));
    private final float health;
    private final int food;
    private final int water;
    private final float saturation;
    private final int maxFoodLevel;
    private final int maxWaterLevel;


    public StateUpdateS2CPacket(float health, float saturation, int food, int maxFoodLevel, int water, int maxWaterLevel) {
        this.health = health;
        this.saturation = saturation;
        this.food = food;
        this.maxFoodLevel = maxFoodLevel;
        this.water = water;
        this.maxWaterLevel = maxWaterLevel;
    }

    private StateUpdateS2CPacket(PacketByteBuf buf) {
        this.health = buf.readFloat();
        this.saturation = buf.readFloat();
        this.food = buf.readVarInt();
        this.maxFoodLevel = buf.readVarInt();
        this.water = buf.readVarInt();
        this.maxWaterLevel = buf.readVarInt();
    }

    private void write(PacketByteBuf buf) {
        buf.writeFloat(this.health);
        buf.writeFloat(this.saturation);
        buf.writeVarInt(this.food);
        buf.writeVarInt(this.maxFoodLevel);
        buf.writeVarInt(this.water);
        buf.writeVarInt(this.maxWaterLevel);
    }
    public float getHealth() {
        return this.health;
    }

    public int getFood() {
        return this.food;
    }

    public float getSaturation() {
        return this.saturation;
    }

    public int getMaxFoodLevel() {
        return this.maxFoodLevel;
    }
    public int getWater() {
        return this.water;
    }
    public int getMaxWaterLevel() {
        return this.maxWaterLevel;
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
