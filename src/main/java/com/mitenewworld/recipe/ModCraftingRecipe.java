package com.mitenewworld.recipe;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.recipe.ModCraftingRecipeCategory;
import com.mitenewworld.recipe.ModCraftingRecipeDisplay;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.book.RecipeBookCategories;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ModCraftingRecipe implements Recipe<CraftingRecipeInput> {
    private final int CraftingTick;
    private final ItemStack result;
    private final RawShapedRecipe rawShapedRecipe;
    @Nullable
    private IngredientPlacement ingredientPlacement;
    private final int Craftlevel;
    private final String group;
    private final CraftingRecipeCategory category;


    public ModCraftingRecipe(RawShapedRecipe rawShapedRecipe, ItemStack result, int CraftingTick, int Craftlevel, String group, CraftingRecipeCategory category) {
        this.CraftingTick = CraftingTick;
        this.Craftlevel = Craftlevel;
        this.result = result;
        this.rawShapedRecipe = rawShapedRecipe;
        this.group = group;
        this.category = category;
    }


    public static class Type implements RecipeType<ModCraftingRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "mod_crafting_shaped";
    }

    public static class Serializer implements RecipeSerializer<ModCraftingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "mod_crafting_shaped";


        public MapCodec<ModCraftingRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ModCraftingRecipe> packetCodec() {
            return PACKET_CODEC;
        }

        public static final MapCodec<ModCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                        (RawShapedRecipe.CODEC.forGetter(r -> r.rawShapedRecipe)),
                        (ItemStack.VALIDATED_CODEC.fieldOf("result")).forGetter(r -> r.result),
                        (Codec.INT.fieldOf("craftingtick").forGetter(r -> r.CraftingTick)),
                        (Codec.INT.fieldOf("craftlevel").forGetter(r -> r.Craftlevel)),
                        (Codec.STRING.optionalFieldOf("group" , "").forGetter(r -> r.group)),
                        (CraftingRecipeCategory.CODEC.fieldOf("category").forGetter(r -> r.category)))
                .apply(instance, ModCraftingRecipe::new));


        private static ModCraftingRecipe read(RegistryByteBuf buf) {
            RawShapedRecipe rawShapedRecipe = RawShapedRecipe.PACKET_CODEC.decode(buf);
            ItemStack result = ItemStack.PACKET_CODEC.decode(buf);
            int craftingtick = buf.readVarInt();
            int craftlevel = buf.readVarInt();
            String string = buf.readString();
            CraftingRecipeCategory craftingRecipeCategory = buf.readEnumConstant(CraftingRecipeCategory.class);
            return new ModCraftingRecipe(rawShapedRecipe, result, craftingtick, craftlevel,string ,craftingRecipeCategory );
        }

        private static void write(RegistryByteBuf buf, ModCraftingRecipe recipe) {
            RawShapedRecipe.PACKET_CODEC.encode(buf, recipe.rawShapedRecipe);
            ItemStack.PACKET_CODEC.encode(buf, recipe.result);
            buf.writeVarInt(recipe.CraftingTick);
            buf.writeVarInt(recipe.Craftlevel);
            buf.writeString(recipe.group);
            buf.writeEnumConstant(recipe.category);
        }
    }

    public int getCraftLevel() {
        return this.Craftlevel;
    }

    public int getCraftTick() {
        return CraftingTick;
    }

    public static final PacketCodec<RegistryByteBuf, ModCraftingRecipe> PACKET_CODEC = PacketCodec.ofStatic(Serializer::write, Serializer::read);


    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        return this.rawShapedRecipe.matches(input);
        /*
        int height = rawShapedRecipe.getHeight();
        int width = rawShapedRecipe.getWidth();
        if (input.getWidth() == width && input.getHeight() == height) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    Optional<Ingredient> optional =  rawShapedRecipe.getIngredients().get(j + i * width);
                    if (Ingredient.matches(optional, input.getStackInSlot(j, i))){
                        continue;
                    }
                    return false;

                }
            }
            return true;
        }
        return false;*/
    }



    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return result.copy();
    }


    @Override
    public RecipeSerializer<ModCraftingRecipe> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<ModCraftingRecipe> getType() {
        return Type.INSTANCE;
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        if (this.ingredientPlacement == null) {
            this.ingredientPlacement = IngredientPlacement.forMultipleSlots(this.rawShapedRecipe.getIngredients());
        }
        return this.ingredientPlacement;
    }

    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return switch (this.category) {
            case MISC -> RecipeBookCategories.CRAFTING_MISC;
            case BUILDING -> RecipeBookCategories.CRAFTING_BUILDING_BLOCKS;
            case REDSTONE -> RecipeBookCategories.CRAFTING_REDSTONE;
            case EQUIPMENT -> RecipeBookCategories.CRAFTING_EQUIPMENT;
        };
    }


    public int getWidth() {
        return this.rawShapedRecipe.getWidth();
    }

    public int getHeight() {
        return this.rawShapedRecipe.getHeight();
    }

    @Override
    public List<RecipeDisplay> getDisplays() {
        return List.of(
                new ModCraftingRecipeDisplay(
                        this.getWidth(),
                        this.getHeight(),
                        this.Craftlevel,
                        this.rawShapedRecipe.getIngredients()
                                .stream()
                                .map(ingredient -> ingredient.map(Ingredient::toDisplay).orElse(SlotDisplay.EmptySlotDisplay.INSTANCE))
                                .toList(),
                        new SlotDisplay.StackSlotDisplay(this.result),
                        new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)
                )
        );
    }


}
