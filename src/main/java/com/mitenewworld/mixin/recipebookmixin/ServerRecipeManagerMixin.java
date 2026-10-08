package com.mitenewworld.mixin.recipebookmixin;


import com.mitenewworld.MITENewWorld;
import com.mitenewworld.recipe.ModCraftingRecipe;
import com.mitenewworld.recipe.ModFurnaceRecipe;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.recipe.*;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.resource.featuretoggle.FeatureSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Mixin(ServerRecipeManager.class)
public class ServerRecipeManagerMixin {

    @Inject(method = "collectServerRecipes", at = @At("HEAD"), cancellable = true)
    private static void collectServerRecipes(Iterable<RecipeEntry<?>> recipes, FeatureSet enabledFeatures, CallbackInfoReturnable<List<ServerRecipeManager.ServerRecipe>> cir) {
        List<ServerRecipeManager.ServerRecipe> list = new ArrayList<>();
        Object2IntMap<String> object2IntMap = new Object2IntOpenHashMap<>();

        for(RecipeEntry<?> recipeEntry : recipes) {
            Recipe<?> recipe = recipeEntry.value();
            if (!(recipe instanceof ModCraftingRecipe) && !(recipe instanceof ModFurnaceRecipe)) {
                continue;
            }

            OptionalInt optionalInt;
            if (recipe.getGroup().isEmpty()) {
                optionalInt = OptionalInt.empty();
            } else {
                optionalInt = OptionalInt.of(
                        object2IntMap.computeIfAbsent(
                                recipe.getGroup(),
                                (group) -> object2IntMap.size()
                        )
                );
            }

            Optional<List<Ingredient>> optional;
            if (recipe.isIgnoredInRecipeBook()) {
                optional = Optional.empty();
            } else {
                optional = Optional.of(recipe.getIngredientPlacement().getIngredients());
            }

            for(RecipeDisplay recipeDisplay : recipe.getDisplays()) {
                if (recipeDisplay.isEnabled(enabledFeatures)) {
                    int i = list.size();
                    NetworkRecipeId networkRecipeId = new NetworkRecipeId(i);
                    RecipeDisplayEntry recipeDisplayEntry = new RecipeDisplayEntry(
                            networkRecipeId, recipeDisplay, optionalInt,
                            recipe.getRecipeBookCategory(), optional
                    );
                    list.add(new ServerRecipeManager.ServerRecipe(recipeDisplayEntry, recipeEntry));
                }
            }


        }

        cir.setReturnValue(list);
    }
}

