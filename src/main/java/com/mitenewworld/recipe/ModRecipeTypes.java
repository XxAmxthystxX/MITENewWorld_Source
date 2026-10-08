package com.mitenewworld.recipe;

import com.mitenewworld.MITENewWorld;
import com.mitenewworld.recipe.ModCraftingRecipeDisplay;
import com.mitenewworld.recipe.ModFurnaceRecipeDisplay;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipeTypes {
    public static final RecipeType<ModCraftingRecipe> MOD_CRAFTING = RecipeType.register(ModCraftingRecipe.Type.ID);
    public static final RecipeType<ModFurnaceRecipe> MOD_FURNACE = RecipeType.register(ModFurnaceRecipe.Type.ID);
    public static void registerRecipes() {
        Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(MITENewWorld.MOD_ID, ModCraftingRecipe.Serializer.ID), ModCraftingRecipe.Serializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE, Identifier.of(MITENewWorld.MOD_ID, ModCraftingRecipe.Type.ID), ModCraftingRecipe.Type.INSTANCE);
        Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(MITENewWorld.MOD_ID, ModFurnaceRecipe.Serializer.ID), ModFurnaceRecipe.Serializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE, Identifier.of(MITENewWorld.MOD_ID, ModFurnaceRecipe.Type.ID), ModFurnaceRecipe.Type.INSTANCE);
        Registry.register(Registries.RECIPE_DISPLAY, Identifier.of(MITENewWorld.MOD_ID, ModCraftingRecipe.Serializer.ID), ModCraftingRecipeDisplay.SERIALIZER);
        Registry.register(Registries.RECIPE_DISPLAY, Identifier.of(MITENewWorld.MOD_ID, ModFurnaceRecipe.Serializer.ID), ModFurnaceRecipeDisplay.SERIALIZER);
    }
    public static <T extends Recipe<?>> RecipeType<T> register(final String id) {
        return Registry.register(Registries.RECIPE_TYPE, Identifier.of(MITENewWorld.MOD_ID,id), new RecipeType<T>(){
            public String toString() {
                return id;
            }
        });
    }
    }

