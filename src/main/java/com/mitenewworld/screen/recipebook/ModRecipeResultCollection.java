package com.mitenewworld.screen.recipebook;


import com.mitenewworld.MITENewWorld;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.recipe.NetworkRecipeId;
import net.minecraft.recipe.RecipeDisplayEntry;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.display.RecipeDisplay;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

@Environment(EnvType.CLIENT)
public class ModRecipeResultCollection {
    public static final ModRecipeResultCollection EMPTY = new ModRecipeResultCollection(List.of());
    private final List<RecipeDisplayEntry> entries;
    private final Set<NetworkRecipeId> displayableRecipes = new HashSet<>();
    private final Set<NetworkRecipeId> canCraftRecipe = new HashSet<>();
    private final Set<NetworkRecipeId> cantCraftRecipeOfLevel = new HashSet<>();
    private final Set<NetworkRecipeId> cantCraftRecipeOfIngredients = new HashSet<>();

    public ModRecipeResultCollection(List<RecipeDisplayEntry> entries) {
        this.entries = entries;
    }

    public void populateRecipes(RecipeFinder finder, ToIntFunction<RecipeDisplay> displayFunction) {
        displayableRecipes.clear();
        canCraftRecipe.clear();
        cantCraftRecipeOfLevel.clear();
        cantCraftRecipeOfIngredients.clear();
        for (RecipeDisplayEntry recipeDisplayEntry : this.entries) {
            NetworkRecipeId recipeId = recipeDisplayEntry.id();
            int state = displayFunction.applyAsInt(recipeDisplayEntry.display());
            boolean isCraftable = recipeDisplayEntry.isCraftable(finder);
            if (state == 3) {
                return;
            }
            displayableRecipes.add(recipeId);
            if (isCraftable) {
                switch (state) {
                    case 0 -> {
                        canCraftRecipe.add(recipeId);
                    }
                    case 1 -> {
                        cantCraftRecipeOfLevel.add(recipeId);
                    }
                    case 2 -> {
                        cantCraftRecipeOfIngredients.add(recipeId);
                    }
                    default  -> {
                        cantCraftRecipeOfIngredients.add(recipeId);
                    }
                }
            } else {
                cantCraftRecipeOfIngredients.add(recipeId);
            }
        }

    }

    public int getRecipeState(){
        if (!this.canCraftRecipe.isEmpty()) {
            return 0;
        }
        if (!this.cantCraftRecipeOfLevel.isEmpty()) {
            return 1;
        }
        return 2;
    }

        public boolean isCraftable(NetworkRecipeId recipeId) {
            return this.canCraftRecipe.contains(recipeId);
        }
        public boolean hasCraftableRecipes() {
            return !this.canCraftRecipe.isEmpty();
        }
        public boolean hasDisplayableRecipes() {
            return !displayableRecipes.isEmpty();
        }
        public List<RecipeDisplayEntry> getAllRecipes() {
            return this.entries;
        }


    public List<RecipeDisplayEntry> filter(ModRecipeFilterMode filterMode) {
        if (displayableRecipes.isEmpty() && !entries.isEmpty()) {
            return new ArrayList<>();
        }

        Predicate<NetworkRecipeId> predicate = switch (filterMode) {
            case ANY -> displayableRecipes::contains;
            case CRAFTABLE -> canCraftRecipe::contains;
            case NOT_CRAFTABLE_BY_INGREDIENTS -> cantCraftRecipeOfIngredients::contains;
        };

        ArrayList<RecipeDisplayEntry> list = new ArrayList<>();

        for (RecipeDisplayEntry recipeDisplayEntry : this.entries) {
            NetworkRecipeId recipeId = recipeDisplayEntry.id();
            if (predicate.test(recipeId)) {
                list.add(recipeDisplayEntry);
            }
        }
        return list;
    }

    @Environment(EnvType.CLIENT)
        public enum ModRecipeFilterMode {
            ANY,
            CRAFTABLE,
            NOT_CRAFTABLE_BY_INGREDIENTS;
        }
    }


