package com.mitenewworld.screen.recipebook;
import com.mitenewworld.MITENewWorld;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mitenewworld.screen.widget.ModAnimatedResultButton;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.recipebook.*;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ToggleButtonWidget;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.util.InputUtil;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.NetworkRecipeId;
import net.minecraft.recipe.display.SlotDisplayContexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextParameterMap;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ModRecipeBookResults {
    private static final ButtonTextures PAGE_FORWARD_TEXTURES = new ButtonTextures(Identifier.ofVanilla("recipe_book/page_forward"), Identifier.ofVanilla("recipe_book/page_forward_highlighted"));
    private static final ButtonTextures PAGE_BACKWARD_TEXTURES = new ButtonTextures(Identifier.ofVanilla("recipe_book/page_backward"), Identifier.ofVanilla("recipe_book/page_backward_highlighted"));
    private final List<ModAnimatedResultButton> resultButtons = Lists.newArrayListWithCapacity(20);
    @Nullable
    private ModAnimatedResultButton hoveredResultButton;
    private final RecipeAlternativesWidget alternatesWidget;
    private MinecraftClient client;
    private final ModRecipeBook<?> recipeBookWidget;
    private List<ModRecipeResultCollection> resultCollections = ImmutableList.of();
    private ToggleButtonWidget nextPageButton;
    private ToggleButtonWidget prevPageButton;
    private int pageCount;
    private int currentPage;
    private ClientRecipeBook recipeBook;
    @Nullable
    private NetworkRecipeId lastClickedRecipe;
    @Nullable
    private ModRecipeResultCollection resultCollection;
    private boolean filteringCraftable;

    public ModRecipeBookResults(ModRecipeBook<?> recipeBookWidget, CurrentIndexProvider currentIndexProvider, boolean furnace) {
        this.recipeBookWidget = recipeBookWidget;
        this.alternatesWidget = new RecipeAlternativesWidget(currentIndexProvider, furnace);
        for (int i = 0; i < 20; ++i) {
            this.resultButtons.add(new ModAnimatedResultButton(currentIndexProvider));
        }
    }

    public void initialize(MinecraftClient client, int parentLeft, int parentTop) {
        this.client = client;
        this.recipeBook = client.player.getRecipeBook();
        for (int i = 0; i < this.resultButtons.size(); ++i) {
            this.resultButtons.get(i).setPosition(parentLeft + 11 + 25 * (i % 5), parentTop + 31 + 25 * (i / 5));
        }
        this.nextPageButton = new ToggleButtonWidget(parentLeft + 93, parentTop + 137, 12, 17, false);
        this.nextPageButton.setTextures(PAGE_FORWARD_TEXTURES);
        this.prevPageButton = new ToggleButtonWidget(parentLeft + 38, parentTop + 137, 12, 17, true);
        this.prevPageButton.setTextures(PAGE_BACKWARD_TEXTURES);
    }

    public void setResults(List<ModRecipeResultCollection> resultCollections, boolean resetCurrentPage, boolean filteringCraftable) {
        this.resultCollections = resultCollections;
        this.filteringCraftable = filteringCraftable;
        this.pageCount = (int)Math.ceil((double)resultCollections.size() / 20.0);
        if (this.pageCount <= this.currentPage || resetCurrentPage) {
            this.currentPage = 0;
        }
        this.refreshResultButtons();
    }

    private void refreshResultButtons() {
        int i = 20 * this.currentPage;
        ContextParameterMap contextParameterMap = SlotDisplayContexts.createParameters(this.client.world);
        for (int j = 0; j < this.resultButtons.size(); ++j) {
            ModAnimatedResultButton button = this.resultButtons.get(j);
            if (i + j < this.resultCollections.size()) {
                ModRecipeResultCollection recipeResultCollection = this.resultCollections.get(i + j);
                button.showResultCollection(recipeResultCollection, this.filteringCraftable, this, contextParameterMap);
                continue;
            }
            button.visible = false;
        }
        this.hideShowPageButtons();
    }


    private void hideShowPageButtons() {
        this.nextPageButton.visible = this.pageCount > 1 && this.currentPage < this.pageCount - 1;
        this.prevPageButton.visible = this.pageCount > 1 && this.currentPage > 0;
    }

    public void draw(DrawContext context, int x, int y, int mouseX, int mouseY, float deltaTicks) {
        if (this.pageCount > 1) {
            MutableText text = Text.translatable("gui.recipebook.page", this.currentPage + 1, this.pageCount);
            int i = this.client.textRenderer.getWidth(text);
            context.drawTextWithShadow(this.client.textRenderer, text, x - i / 2 + 73, y + 141, Colors.WHITE);
        }
        this.hoveredResultButton = null;
        for (ModAnimatedResultButton animatedResultButton : this.resultButtons) {
            animatedResultButton.render(context, mouseX, mouseY, deltaTicks);
            if (!animatedResultButton.visible || !animatedResultButton.isSelected()) {
                continue;
            }
            this.hoveredResultButton = animatedResultButton;
        }
        this.prevPageButton.render(context, mouseX, mouseY, deltaTicks);
        this.nextPageButton.render(context, mouseX, mouseY, deltaTicks);
        context.createNewRootLayer();
        this.alternatesWidget.render(context, mouseX, mouseY, deltaTicks);
    }

    public void drawTooltip(DrawContext context, int x, int y) {
        if (this.client.currentScreen != null && this.hoveredResultButton != null && !this.alternatesWidget.isVisible()) {
            ItemStack itemStack = this.hoveredResultButton.getDisplayStack();
            Identifier identifier = itemStack.get(DataComponentTypes.TOOLTIP_STYLE);
            context.drawTooltip(this.client.textRenderer, this.hoveredResultButton.getTooltip(itemStack), x, y, identifier);
        }
    }

    @Nullable
    public NetworkRecipeId getLastClickedRecipe() {
        return this.lastClickedRecipe;
    }

    @Nullable
    public ModRecipeResultCollection getLastClickedResults() {
        return this.resultCollection;
    }

    public void hideAlternates() {
        this.alternatesWidget.setVisible(false);
    }

    public boolean mouseClicked(Click click, int left, int top, int width, int height, boolean bl) {
        this.lastClickedRecipe = null;
        this.resultCollection = null;
        if (this.alternatesWidget.isVisible()) {
            if (this.alternatesWidget.mouseClicked(click, bl)) {
                this.lastClickedRecipe = this.alternatesWidget.getLastClickedRecipe();
                this.resultCollection = new ModRecipeResultCollection(this.alternatesWidget.getResults().getAllRecipes());
            } else {
                this.alternatesWidget.setVisible(false);
            }
            return true;
        }
        boolean nextClicked = this.nextPageButton.mouseClicked(click, bl);
        boolean prevClicked = this.prevPageButton.mouseClicked(click, bl);

        if (nextClicked) {
            ++this.currentPage;
            this.refreshResultButtons();
            return true;
        }
        if (prevClicked) {
            --this.currentPage;
            this.refreshResultButtons();
            return true;
        }
        ContextParameterMap contextParameterMap = SlotDisplayContexts.createParameters(this.client.world);
        for (ModAnimatedResultButton animatedResultButton : this.resultButtons) {
            boolean buttonClicked = animatedResultButton.mouseClicked(click, bl);

            if (!buttonClicked) {
                continue;
            }

            if (click.button() == 0) {
                this.lastClickedRecipe = animatedResultButton.getCurrentId();
                this.resultCollection = animatedResultButton.getResultCollection();
            } else if (click.button() == InputUtil.GLFW_MOUSE_BUTTON_RIGHT && !this.alternatesWidget.isVisible() && !animatedResultButton.hasSingleResult()) {
                this.alternatesWidget.showAlternativesForResult(new RecipeResultCollection(animatedResultButton.getResultCollection().getAllRecipes()), contextParameterMap, this.filteringCraftable, animatedResultButton.getX(), animatedResultButton.getY(), left + width / 2, top + 13 + height / 2, animatedResultButton.getWidth());
            }

            return true;
        }

        return false;
    }
    public void onRecipeDisplayed(NetworkRecipeId recipeId) {
        this.recipeBookWidget.onRecipeDisplayed(recipeId);
    }

    public ClientRecipeBook getRecipeBook() {
        return this.recipeBook;
    }

    public void forEachButton(Consumer<ClickableWidget> consumer) {
        consumer.accept(this.nextPageButton);
        consumer.accept(this.prevPageButton);
        this.resultButtons.forEach(consumer);
    }
}
