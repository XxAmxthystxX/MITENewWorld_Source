package com.mitenewworld.recipe;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.recipe.ModFurnaceRecipeDisplay;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategories;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

import java.util.List;

public class ModFurnaceRecipe implements Recipe<SingleStackRecipeInput> {
    private final int BurnTick;
    private final ItemStack result;
    private final Ingredient input;
    private final int Fuellevel;
    private final float experience;

    public ModFurnaceRecipe(Ingredient input , ItemStack result, int burnTick , int fuellevel, float experience) {
        this.input = input;
        this.Fuellevel = fuellevel;
        this.BurnTick = burnTick;
        this.result = result;
        this.experience = experience;
    }


    public static class Type implements RecipeType<ModFurnaceRecipe> {
        public static final ModFurnaceRecipe.Type INSTANCE = new ModFurnaceRecipe.Type();
        public static final String ID = "modfurnace";
    }

    public static class Serializer implements RecipeSerializer<ModFurnaceRecipe> {
        public static final ModFurnaceRecipe.Serializer INSTANCE = new ModFurnaceRecipe.Serializer();
        public static final String ID = "modfurnace";


        public MapCodec<ModFurnaceRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ModFurnaceRecipe> packetCodec() {
            return PACKET_CODEC;
        }

        public static final MapCodec<ModFurnaceRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                        (Ingredient.CODEC.fieldOf("input")).forGetter(r -> r.input),
                        (ItemStack.VALIDATED_CODEC.fieldOf("result")).forGetter(r -> r.result),
                        (Codec.INT.fieldOf("burntick").forGetter(r -> r.BurnTick)),
                        (Codec.INT.fieldOf("furnacelevel").forGetter(r -> r.Fuellevel)),
                        (Codec.FLOAT.optionalFieldOf("experience" , 0.0f).forGetter(r -> r.experience)))
                .apply(instance, ModFurnaceRecipe::new));


        private static ModFurnaceRecipe read(RegistryByteBuf buf) {
            Ingredient input = Ingredient.PACKET_CODEC.decode(buf);
            ItemStack result = ItemStack.PACKET_CODEC.decode(buf);
            int Burntime = buf.readVarInt();
            int Furnacelevel = buf.readVarInt();
            float experience = buf.readFloat();
            return new ModFurnaceRecipe( input ,result , Burntime , Furnacelevel, experience);
        }

        private static void write(RegistryByteBuf buf, ModFurnaceRecipe recipe) {
            Ingredient.PACKET_CODEC.encode(buf, recipe.input);
            ItemStack.PACKET_CODEC.encode(buf, recipe.result);
            buf.writeVarInt(recipe.BurnTick);
            buf.writeVarInt(recipe.Fuellevel);
            buf.writeFloat(recipe.experience);
        }
    }

    public static final PacketCodec<RegistryByteBuf, ModFurnaceRecipe> PACKET_CODEC = PacketCodec.ofStatic(ModFurnaceRecipe.Serializer::write, ModFurnaceRecipe.Serializer::read);

    @Override
    public boolean matches(SingleStackRecipeInput input, World world) {
        return this.input.test(input.getStackInSlot(0));
    }

    @Override
    public ItemStack craft(SingleStackRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return result.copy();
    }


    @Override
    public RecipeSerializer<ModFurnaceRecipe> getSerializer() {
        return ModFurnaceRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<ModFurnaceRecipe> getType() {
        return ModFurnaceRecipe.Type.INSTANCE;
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        return IngredientPlacement.forSingleSlot(input);
    }

    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return RecipeBookCategories.FURNACE_MISC;
    }
    public float getExperience() {
        return this.experience;
    }
    public RecipeCategory getCategory() {
        return RecipeCategory.MISC;
    }

    public int getFuellevel() {
        return this.Fuellevel;
    }
    public int getBurnTick() {
        return this.BurnTick;
    }

    @Override
    public List<RecipeDisplay> getDisplays() {
        return List.of(new ModFurnaceRecipeDisplay( this.Fuellevel, this.BurnTick, this.input.toDisplay(), SlotDisplay.AnyFuelSlotDisplay.INSTANCE, new SlotDisplay.StackSlotDisplay(this.result), new SlotDisplay.ItemSlotDisplay(this.result.getItem()) ));
    }

}


