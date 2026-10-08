package com.mitenewworld.core.shadow;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.screen.recipebook.ModRecipeResultCollection;
import net.minecraft.recipe.book.RecipeBookGroup;

import java.util.List;

public interface ShadowClientRecipeBook {
    List<ModRecipeResultCollection> getFixedResultsByCategory(RecipeBookGroup category) ;

    List<ModRecipeResultCollection> getFixedOrderedResults() ;

}
