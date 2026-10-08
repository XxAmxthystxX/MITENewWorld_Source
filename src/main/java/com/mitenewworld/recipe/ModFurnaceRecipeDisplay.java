package com.mitenewworld.recipe;
import com.mitenewworld.MITENewWorld;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.display.FurnaceRecipeDisplay;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.resource.featuretoggle.FeatureSet;

public record ModFurnaceRecipeDisplay(int burnLevel , int burnTime ,SlotDisplay ingredient, SlotDisplay fuel, SlotDisplay result, SlotDisplay craftingStation)
        implements RecipeDisplay {
    public static final MapCodec<ModFurnaceRecipeDisplay> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Codec.INT.fieldOf("burn_level").forGetter(ModFurnaceRecipeDisplay::burnLevel),
                            Codec.INT.fieldOf("burn_time").forGetter(ModFurnaceRecipeDisplay::burnTime),
                            SlotDisplay.CODEC.fieldOf("ingredient").forGetter(ModFurnaceRecipeDisplay::ingredient),
                            SlotDisplay.CODEC.fieldOf("fuel").forGetter(ModFurnaceRecipeDisplay::fuel),
                            SlotDisplay.CODEC.fieldOf("result").forGetter(ModFurnaceRecipeDisplay::result),
                            SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(ModFurnaceRecipeDisplay::craftingStation)
                    )
                    .apply(instance, ModFurnaceRecipeDisplay::new)
    );
    public static final PacketCodec<RegistryByteBuf, ModFurnaceRecipeDisplay> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            ModFurnaceRecipeDisplay::burnLevel,
            PacketCodecs.VAR_INT,
            ModFurnaceRecipeDisplay::burnTime,
            SlotDisplay.PACKET_CODEC,
            ModFurnaceRecipeDisplay::ingredient,
            SlotDisplay.PACKET_CODEC,
            ModFurnaceRecipeDisplay::fuel,
            SlotDisplay.PACKET_CODEC,
            ModFurnaceRecipeDisplay::result,
            SlotDisplay.PACKET_CODEC,
            ModFurnaceRecipeDisplay::craftingStation,
            ModFurnaceRecipeDisplay::new
    );
    public static final RecipeDisplay.Serializer<ModFurnaceRecipeDisplay> SERIALIZER = new RecipeDisplay.Serializer<>(CODEC, PACKET_CODEC);

    @Override
    public RecipeDisplay.Serializer<ModFurnaceRecipeDisplay> serializer() {
        return SERIALIZER;
    }

    @Override
    public boolean isEnabled(FeatureSet features) {
        return this.ingredient.isEnabled(features) && this.fuel().isEnabled(features) && RecipeDisplay.super.isEnabled(features);
    }
}
