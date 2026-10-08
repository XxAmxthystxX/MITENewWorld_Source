package com.mitenewworld.screen.recipebook;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.screen.recipebook.ModRecipeBook;
import com.mitenewworld.screen.recipebook.ModRecipeResultCollection;
import com.mitenewworld.screen.furnace.ModFurnaceScreenHandler;
import com.mitenewworld.core.shadow.ShadowGhostRecipe;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.recipebook.GhostRecipe;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.recipebook.RecipeBookType;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.book.RecipeBookCategories;
import net.minecraft.recipe.display.FurnaceRecipeDisplay;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextParameterMap;

import java.util.List;

public class ModFurnaceRecipeBook extends ModRecipeBook<ModFurnaceScreenHandler> {
    private static final ButtonTextures FILTER_BUTTON_TEXTURES = new ButtonTextures(
            Identifier.ofVanilla("recipe_book/furnace_filter_enabled"),
            Identifier.ofVanilla("recipe_book/furnace_filter_disabled"),
            Identifier.ofVanilla("recipe_book/furnace_filter_enabled_highlighted"),
            Identifier.ofVanilla("recipe_book/furnace_filter_disabled_highlighted")
    );
    private static final List<RecipeBookWidget.Tab> TABS = List.of(new RecipeBookWidget.Tab(RecipeBookType.FURNACE), new RecipeBookWidget.Tab(Items.PORKCHOP, RecipeBookCategories.FURNACE_FOOD), new RecipeBookWidget.Tab(Items.STONE, RecipeBookCategories.FURNACE_BLOCKS), new RecipeBookWidget.Tab(Items.LAVA_BUCKET, Items.EMERALD, RecipeBookCategories.FURNACE_MISC));
    private final Text toggleCraftableButtonText = Text.translatable("gui.recipebook.toggleRecipes.smeltable");

    public ModFurnaceRecipeBook(ModFurnaceScreenHandler screenHandler) {
        super(screenHandler, TABS);

    }

    @Override
    protected void setBookButtonTexture() {
        this.toggleCraftableButton.setTextures(FILTER_BUTTON_TEXTURES);
    }

    @Override
    protected boolean isCraftingSlot(Slot slot) {
        return switch (slot.id) {
            case 0, 1, 2 -> true;
            default -> false;
        };
    }

    @Override
    protected void showGhostRecipe(GhostRecipe ghostRecipe, RecipeDisplay display, ContextParameterMap context) {
        ((ShadowGhostRecipe)ghostRecipe).pubAddResult(this.craftingScreenHandler.getOutputSlot(), context, display.result());
        if (display instanceof FurnaceRecipeDisplay furnaceRecipeDisplay) {
            ((ShadowGhostRecipe)ghostRecipe).pubAddInputs(this.craftingScreenHandler.getInputSlot(), context, furnaceRecipeDisplay.ingredient());
            Slot slot = this.craftingScreenHandler.slots.get(1);
            if (slot.getStack().isEmpty()) {
                ((ShadowGhostRecipe)ghostRecipe).pubAddInputs(slot, context, furnaceRecipeDisplay.fuel());
            }
        }
    }

    @Override
    protected Text getToggleCraftableButtonText() {
        return this.toggleCraftableButtonText;
    }

    @Override
    protected void populateRecipes(ModRecipeResultCollection recipeResultCollection, RecipeFinder recipeFinder) {
        recipeResultCollection.populateRecipes(recipeFinder, this::canDisplay);
    }


    private int canDisplay(RecipeDisplay recipeDisplay) {
        return 1;
    }
}
