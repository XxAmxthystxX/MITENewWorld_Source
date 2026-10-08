package com.mitenewworld.recipe;
import com.mitenewworld.MITENewWorld;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.ShapedCraftingRecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.resource.featuretoggle.FeatureSet;

import java.util.List;

public record ModCraftingRecipeDisplay (int width, int height, int craftLevel , List<SlotDisplay> ingredients, SlotDisplay result, SlotDisplay craftingStation) implements RecipeDisplay {
    public static final MapCodec<ModCraftingRecipeDisplay> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Codec.INT.fieldOf("width").forGetter(ModCraftingRecipeDisplay::width),
                            Codec.INT.fieldOf("height").forGetter(ModCraftingRecipeDisplay::height),
                            Codec.INT.fieldOf("craftLevel").forGetter(ModCraftingRecipeDisplay::craftLevel),
                            SlotDisplay.CODEC.listOf().fieldOf("ingredients").forGetter(ModCraftingRecipeDisplay::ingredients),
                            SlotDisplay.CODEC.fieldOf("result").forGetter(ModCraftingRecipeDisplay::result),
                            SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(ModCraftingRecipeDisplay::craftingStation)
                    )
                    .apply(instance, ModCraftingRecipeDisplay::new)
    );
    public static final PacketCodec<RegistryByteBuf, ModCraftingRecipeDisplay> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            ModCraftingRecipeDisplay::width,
            PacketCodecs.VAR_INT,
            ModCraftingRecipeDisplay::height,
            PacketCodecs.VAR_INT,
            ModCraftingRecipeDisplay::craftLevel,
            SlotDisplay.PACKET_CODEC.collect(PacketCodecs.toList()),
            ModCraftingRecipeDisplay::ingredients,
            SlotDisplay.PACKET_CODEC,
            ModCraftingRecipeDisplay::result,
            SlotDisplay.PACKET_CODEC,
            ModCraftingRecipeDisplay::craftingStation,
            ModCraftingRecipeDisplay::new
    );
    public static final RecipeDisplay.Serializer<ModCraftingRecipeDisplay> SERIALIZER = new RecipeDisplay.Serializer<>(CODEC, PACKET_CODEC);

    public ModCraftingRecipeDisplay(int width, int height, int craftLevel ,List<SlotDisplay> ingredients, SlotDisplay result, SlotDisplay craftingStation) {
        if (ingredients.size() != width * height) {
            throw new IllegalArgumentException("Invalid shaped recipe display contents");
        } else {
            this.width = width;
            this.height = height;
            this.craftLevel = craftLevel;
            this.ingredients = ingredients;
            this.result = result;
            this.craftingStation = craftingStation;
        }
    }

    @Override
    public RecipeDisplay.Serializer<ModCraftingRecipeDisplay> serializer() {
        return SERIALIZER;
    }

    @Override
    public boolean isEnabled(FeatureSet features) {
        return this.ingredients.stream().allMatch(ingredient -> ingredient.isEnabled(features)) && RecipeDisplay.super.isEnabled(features);
    }
}
