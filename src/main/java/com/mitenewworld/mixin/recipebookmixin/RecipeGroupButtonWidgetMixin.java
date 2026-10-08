package com.mitenewworld.mixin.recipebookmixin;
import com.mitenewworld.MITENewWorld;


import com.mitenewworld.screen.recipebook.ModRecipeResultCollection;
import com.mitenewworld.core.shadow.ShadowClientRecipeBook;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.screen.recipebook.RecipeGroupButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.recipe.RecipeDisplayEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(RecipeGroupButtonWidget.class)
public abstract class RecipeGroupButtonWidgetMixin extends ClickableWidget {
    @Mutable
    @Final
    @Shadow
    private final RecipeBookWidget.Tab tab ;
    @Shadow
    private float bounce;

    public RecipeGroupButtonWidgetMixin(int x, int y, int width, int height, Text message, RecipeBookWidget.Tab tab) {
        super(x, y, width, height, message);
        this.tab = tab;
    }
    @Inject(method = "checkForNewRecipes", at = @At("HEAD"), cancellable = true)
    public void checkForNewRecipes(ClientRecipeBook recipeBook, boolean filteringCraftable, CallbackInfo ci) {
        ModRecipeResultCollection.ModRecipeFilterMode recipeFilterMode = filteringCraftable ? ModRecipeResultCollection.ModRecipeFilterMode.CRAFTABLE : ModRecipeResultCollection.ModRecipeFilterMode.ANY;
        List<ModRecipeResultCollection> list = ((ShadowClientRecipeBook)recipeBook).getFixedResultsByCategory(this.tab.category());
        for (ModRecipeResultCollection recipeResultCollection : list) {
            for (RecipeDisplayEntry recipeDisplayEntry : recipeResultCollection.filter(recipeFilterMode)) {
                if (!recipeBook.isHighlighted(recipeDisplayEntry.id())) {
                    continue;
                }
                this.bounce = 15.0f;
                return;
            }
        }
    }

    @Inject(method = "hasKnownRecipes", at = @At("HEAD"), cancellable = true)
    public void hasKnownRecipes(ClientRecipeBook recipeBook, CallbackInfoReturnable<Boolean> cir) {
        cir.cancel();
        List<ModRecipeResultCollection> list = ((ShadowClientRecipeBook)recipeBook).getFixedResultsByCategory(this.tab.category());
        this.visible = false;
        for (ModRecipeResultCollection recipeResultCollection : list) {
            if (!recipeResultCollection.hasDisplayableRecipes()) {
                continue;
            }
            this.visible = true;
            break;
        }
        cir.setReturnValue(this.visible);
    }
}
