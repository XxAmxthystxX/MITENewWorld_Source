package com.mitenewworld.mixin.recipebookmixin;
import com.mitenewworld.MITENewWorld;



import com.mitenewworld.recipe.ModCraftingRecipe;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeGridAligner;
import net.minecraft.recipe.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(RecipeGridAligner.class)
public interface RecipeGridAlignerMixin  {

    @Inject(method = "alignRecipeToGrid(IILnet/minecraft/recipe/Recipe;Ljava/lang/Iterable;Lnet/minecraft/recipe/RecipeGridAligner$Filler;)V" , at = @At("HEAD") ,cancellable = true)
    private static <T> void alignRecipeToGrid(int width, int height, Recipe<?> recipe, Iterable<T> slots, RecipeGridAligner.Filler<T> filler, CallbackInfo ci) {
        if (recipe instanceof ModCraftingRecipe modCraftingRecipe) {
            RecipeGridAligner.alignRecipeToGrid(width, height, modCraftingRecipe.getWidth(), modCraftingRecipe.getHeight(), slots, filler);
            ci.cancel();
        }
    }


}
