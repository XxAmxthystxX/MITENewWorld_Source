package com.mitenewworld.screen.recipebook;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.recipe.ModCraftingRecipeDisplay;
import com.mitenewworld.screen.recipebook.ModRecipeBook;
import com.mitenewworld.screen.recipebook.ModRecipeResultCollection;
import com.mitenewworld.core.shadow.ShadowGhostRecipe;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.recipebook.GhostRecipe;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.recipebook.RecipeBookType;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeGridAligner;
import net.minecraft.recipe.book.RecipeBookCategories;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.screen.AbstractCraftingScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextParameterMap;

import java.util.List;

public class ModInventoryRecipeBook extends ModRecipeBook<AbstractCraftingScreenHandler> {
    private static final ButtonTextures FILTER_BUTTON_TEXTURES = new ButtonTextures(
            Identifier.ofVanilla("recipe_book/filter_enabled"),
            Identifier.ofVanilla("recipe_book/filter_disabled"),
            Identifier.ofVanilla("recipe_book/filter_enabled_highlighted"),
            Identifier.ofVanilla("recipe_book/filter_disabled_highlighted")
    );
    private static final Text TOGGLE_CRAFTABLE_TEXT = Text.translatable("gui.recipebook.toggleRecipes.craftable");
    private static final List<RecipeBookWidget.Tab> TABS = List.of(
            new RecipeBookWidget.Tab(RecipeBookType.CRAFTING),
            new RecipeBookWidget.Tab(Items.IRON_AXE, Items.GOLDEN_SWORD, RecipeBookCategories.CRAFTING_EQUIPMENT),
            new RecipeBookWidget.Tab(Items.BRICKS, RecipeBookCategories.CRAFTING_BUILDING_BLOCKS),
            new RecipeBookWidget.Tab(Items.LAVA_BUCKET, Items.APPLE, RecipeBookCategories.CRAFTING_MISC),
            new RecipeBookWidget.Tab(Items.REDSTONE, RecipeBookCategories.CRAFTING_REDSTONE)
    );

    public ModInventoryRecipeBook(AbstractCraftingScreenHandler craftingScreenHandler) {
        super(craftingScreenHandler, TABS);
    }


    private int canDisplay(RecipeDisplay display) {
        if (display instanceof ModCraftingRecipeDisplay craftingRecipeDisplay) {
            if (this.craftingScreenHandler.getOutputSlot().hasStack()){
                return 2;
            }
            if (craftingRecipeDisplay.height() > 2 || craftingRecipeDisplay.width() > 2) {
                return 3;
            }
            if (craftingRecipeDisplay.craftLevel() > 0){
                return 1;
            }
        }
        return 0;
    }
    @Override
    protected void showGhostRecipe(GhostRecipe ghostRecipe, RecipeDisplay display, ContextParameterMap context) {
        ((ShadowGhostRecipe)ghostRecipe).pubAddResult(this.craftingScreenHandler.getOutputSlot(), context, display.result());
        if (display instanceof ModCraftingRecipeDisplay recipeDisplay) {
            List<Slot> list = this.craftingScreenHandler.getInputSlots();
            RecipeGridAligner.alignRecipeToGrid(
                    this.craftingScreenHandler.getWidth(),
                    this.craftingScreenHandler.getHeight(),
                    recipeDisplay.width(),
                    recipeDisplay.height(),
                    recipeDisplay.ingredients(),
                    (slot, index, x, y) -> {
                        Slot slot2 = list.get(index);
                        ((ShadowGhostRecipe)ghostRecipe).pubAddInputs(slot2, context, slot);
                    }
            );

        }
    }

    @Override
    protected void setBookButtonTexture() {
        this.toggleCraftableButton.setTextures(FILTER_BUTTON_TEXTURES);
    }

    @Override
    protected boolean isCraftingSlot(Slot slot) {
        return this.craftingScreenHandler.getOutputSlot() == slot || this.craftingScreenHandler.getInputSlots().contains(slot);
    }


    @Override
    protected Text getToggleCraftableButtonText() {
        return TOGGLE_CRAFTABLE_TEXT;
    }


    protected void populateRecipes(ModRecipeResultCollection recipeResultCollection, RecipeFinder recipeFinder) {
        recipeResultCollection.populateRecipes(recipeFinder, this::canDisplay);
    }
}
