package com.mitenewworld.screen.recipebook;
import com.mitenewworld.MITENewWorld;


import com.google.common.collect.Lists;
import com.mitenewworld.core.shadow.ShadowClientRecipeBook;
import com.mitenewworld.core.shadow.ShadowSearchManager;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.navigation.NavigationAxis;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.recipebook.*;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.ToggleButtonWidget;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.resource.language.LanguageDefinition;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.network.packet.c2s.play.RecipeCategoryOptionsC2SPacket;
import net.minecraft.recipe.NetworkRecipeId;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.book.RecipeBookGroup;
import net.minecraft.recipe.book.RecipeBookType;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplayContexts;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextParameterMap;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Environment(EnvType.CLIENT)
public abstract class ModRecipeBook<T extends AbstractRecipeScreenHandler> implements Drawable, Element, Selectable {
    public static final ButtonTextures BUTTON_TEXTURES = new ButtonTextures(Identifier.ofVanilla("recipe_book/button"), Identifier.ofVanilla("recipe_book/button_highlighted"));
    protected static final Identifier TEXTURE = Identifier.ofVanilla("textures/gui/recipe_book.png");
    private static final Text SEARCH_HINT_TEXT = Text.translatable("gui.recipebook.search_hint").fillStyle(TextFieldWidget.SEARCH_STYLE);
    private static final Text TOGGLE_ALL_RECIPES_TEXT = Text.translatable("gui.recipebook.toggleRecipes.all");
    private int leftOffset;
    private int parentWidth;
    private int parentHeight;
    private float displayTime;
    @Nullable
    private NetworkRecipeId selectedRecipeId;
    private final GhostRecipe ghostRecipe;
    private final List<RecipeGroupButtonWidget> tabButtons = Lists.newArrayList();
    @Nullable
    private RecipeGroupButtonWidget currentTab;
    protected ToggleButtonWidget toggleCraftableButton;
    protected final T craftingScreenHandler;
    protected MinecraftClient client;
    @Nullable
    private TextFieldWidget searchField;
    private String searchText = "";
    private final List<RecipeBookWidget.Tab> tabs;
    private ClientRecipeBook recipeBook;
    private final ModRecipeBookResults recipesArea;
    @Nullable
    private NetworkRecipeId selectedRecipe;
    @Nullable
    private ModRecipeResultCollection selectedRecipeResults;
    private final RecipeFinder recipeFinder = new RecipeFinder();
    private int cachedInvChangeCount;
    private boolean searching;
    private boolean open;
    private boolean narrow;
    @Nullable
    private ScreenRect searchFieldRect;

    public ModRecipeBook(T craftingScreenHandler, List<RecipeBookWidget.Tab> tabs) {
        this.craftingScreenHandler = craftingScreenHandler;
        this.tabs = tabs;
        CurrentIndexProvider currentIndexProvider = () -> MathHelper.floor(this.displayTime / 30.0f);
        this.ghostRecipe = new GhostRecipe(currentIndexProvider);
        this.recipesArea = new ModRecipeBookResults(this, currentIndexProvider, craftingScreenHandler instanceof AbstractFurnaceScreenHandler);
    }

    public void initialize(int parentWidth, int parentHeight, MinecraftClient client, boolean narrow) {
        this.client = client;
        this.parentWidth = parentWidth;
        this.parentHeight = parentHeight;
        this.narrow = narrow;
        this.recipeBook = client.player.getRecipeBook();
        this.cachedInvChangeCount = client.player.getInventory().getChangeCount();
        this.open = this.isGuiOpen();
        this.reset();
    }

    private void reset() {
        boolean bl = this.isFilteringCraftable();
        this.leftOffset = this.narrow ? 0 : 86;
        int i = this.getLeft();
        int j = this.getTop();
        this.recipeFinder.clear();
        this.client.player.getInventory().populateRecipeFinder(this.recipeFinder);
        this.craftingScreenHandler.populateRecipeFinder(this.recipeFinder);
        String string = this.searchField != null ? this.searchField.getText() : "";
        this.searchField = new TextFieldWidget(this.client.textRenderer, i + 25, j + 13, 81, this.client.textRenderer.fontHeight + 5, Text.translatable("itemGroup.search"));
        this.searchField.setMaxLength(50);
        this.searchField.setVisible(true);
        this.searchField.setEditableColor(-1);
        this.searchField.setText(string);
        this.searchField.setPlaceholder(SEARCH_HINT_TEXT);
        this.searchFieldRect = ScreenRect.of(NavigationAxis.HORIZONTAL, i + 8, this.searchField.getY(), this.searchField.getX() - this.getLeft(), this.searchField.getHeight());
        this.recipesArea.initialize(this.client, i, j);
        this.toggleCraftableButton = new ToggleButtonWidget(i + 110, j + 12, 26, 16, bl);
        this.updateTooltip();
        this.setBookButtonTexture();
        this.tabButtons.clear();
        for (net.minecraft.client.gui.screen.recipebook.RecipeBookWidget.Tab tab : this.tabs) {
            this.tabButtons.add(new RecipeGroupButtonWidget(tab));
        }
        if (this.currentTab != null) {
            this.currentTab = this.tabButtons.stream().filter(button -> button.getCategory().equals(this.currentTab.getCategory())).findFirst().orElse(null);
        }
        if (this.currentTab == null) {
            this.currentTab = this.tabButtons.getFirst();
        }
        this.currentTab.setToggled(true);
        this.populateAllRecipes();
        this.refreshTabButtons(bl);
        this.refreshResults(false, bl);
    }

    private int getTop() {
        return (this.parentHeight - 166) / 2;
    }

    private int getLeft() {
        return (this.parentWidth - 147) / 2 - this.leftOffset;
    }

    private void updateTooltip() {
        this.toggleCraftableButton.setTooltip(this.toggleCraftableButton.isToggled() ? Tooltip.of(this.getToggleCraftableButtonText()) : Tooltip.of(TOGGLE_ALL_RECIPES_TEXT));
    }

    protected abstract void setBookButtonTexture();

    public int findLeftEdge(int width, int backgroundWidth) {
        return this.isOpen() && !this.narrow ? 177 + (width - backgroundWidth - 200) / 2 : (width - backgroundWidth) / 2;
    }

    public void toggleOpen() {
        this.setOpen(!this.isOpen());
    }

    public boolean isOpen() {
        return this.open;
    }

    private boolean isGuiOpen() {
        return this.recipeBook.isGuiOpen(this.craftingScreenHandler.getCategory());
    }

    protected void setOpen(boolean opened) {
        if (opened) {
            this.reset();
        }
        this.open = opened;
        this.recipeBook.setGuiOpen(this.craftingScreenHandler.getCategory(), opened);
        if (!opened) {
            this.recipesArea.hideAlternates();
        }
        this.sendBookDataPacket();
    }

    protected abstract boolean isCraftingSlot(Slot var1);

    public void onMouseClick(@Nullable Slot slot) {
        if (slot != null && this.isCraftingSlot(slot)) {
            this.selectedRecipeId = null;
            this.ghostRecipe.clear();
            if (this.isOpen()) {
                this.refreshInputs();
            }
        }
    }

    private void populateAllRecipes() {
        for (RecipeBookWidget.Tab tab : this.tabs) {
            for (ModRecipeResultCollection recipeResultCollection : ((ShadowClientRecipeBook)this.recipeBook).getFixedResultsByCategory(tab.category())) {
                this.populateRecipes(recipeResultCollection, this.recipeFinder);
            }
        }
    }

    protected abstract void populateRecipes(ModRecipeResultCollection var1, RecipeFinder var2);

    private void refreshResults(boolean resetCurrentPage, boolean filteringCraftable) {
        ClientPlayNetworkHandler clientPlayNetworkHandler;
        List<ModRecipeResultCollection> list = new ArrayList<>(((ShadowClientRecipeBook) this.recipeBook).getFixedResultsByCategory(this.currentTab.getCategory()));
        ArrayList<ModRecipeResultCollection> list2 = Lists.newArrayList(list);
        list2.removeIf(resultCollection -> !resultCollection.hasDisplayableRecipes());

        String string = null;
        if (this.searchField != null) {
            string = this.searchField.getText();
        }
        if (string != null && !string.isEmpty() && (clientPlayNetworkHandler = this.client.getNetworkHandler()) != null) {
            ObjectLinkedOpenHashSet<ModRecipeResultCollection> objectSet = new ObjectLinkedOpenHashSet<>();
            ((ShadowSearchManager) clientPlayNetworkHandler.getSearchManager()).getFixedRecipeOutputReloadFuture().findAll(string.toLowerCase(Locale.ROOT)).forEach(recipeResultCollection -> objectSet.add(recipeResultCollection));
            list2.removeIf(resultCollection -> !objectSet.contains(resultCollection));
        }
        if (filteringCraftable) {
            list2.removeIf(resultCollection -> !resultCollection.hasCraftableRecipes());
        }
        this.recipesArea.setResults(list2, resetCurrentPage, filteringCraftable);
    }

    private void refreshTabButtons(boolean filteringCraftable) {
        int i = (this.parentWidth - 147) / 2 - this.leftOffset - 30;
        int j = (this.parentHeight - 166) / 2 + 3;
        int k = 27;
        int l = 0;
        for (RecipeGroupButtonWidget recipeGroupButtonWidget : this.tabButtons) {
            RecipeBookGroup recipeBookGroup = recipeGroupButtonWidget.getCategory();
            if (recipeBookGroup instanceof net.minecraft.client.recipebook.RecipeBookType) {
                recipeGroupButtonWidget.visible = true;
                recipeGroupButtonWidget.setPosition(i, j + k * l++);
                continue;
            }
            if (!recipeGroupButtonWidget.hasKnownRecipes(this.recipeBook)) {
                continue;
            }
            recipeGroupButtonWidget.setPosition(i, j + 27 * l++);
            recipeGroupButtonWidget.checkForNewRecipes(this.recipeBook, filteringCraftable);
        }
    }

    public void update() {
        boolean bl = this.isGuiOpen();
        if (this.isOpen() != bl) {
            this.setOpen(bl);
        }
        if (!this.isOpen()) {
            return;
        }
        if (this.cachedInvChangeCount != this.client.player.getInventory().getChangeCount()) {
            this.refreshInputs();
            this.cachedInvChangeCount = this.client.player.getInventory().getChangeCount();
        }
    }

    private void refreshInputs() {
        this.recipeFinder.clear();
        this.client.player.getInventory().populateRecipeFinder(this.recipeFinder);
        this.craftingScreenHandler.populateRecipeFinder(this.recipeFinder);
        this.populateAllRecipes();
        this.refreshResults(false, this.isFilteringCraftable());
    }

    private boolean isFilteringCraftable() {
        return this.recipeBook.isFilteringCraftable(this.craftingScreenHandler.getCategory());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        if (!this.isOpen()) {
            return;
        }
        if (!this.client.isCtrlPressed()) {
            this.displayTime += deltaTicks;
        }
        int i = this.getLeft();
        int j = this.getTop();
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, i, j, 1.0f, 1.0f, 147, 166, 256, 256);
        this.searchField.render(context, mouseX, mouseY, deltaTicks);
        for (RecipeGroupButtonWidget recipeGroupButtonWidget : this.tabButtons) {
            recipeGroupButtonWidget.render(context, mouseX, mouseY, deltaTicks);
        }
        this.toggleCraftableButton.render(context, mouseX, mouseY, deltaTicks);
        this.recipesArea.draw(context, i, j, mouseX, mouseY, deltaTicks);
    }

    public void drawTooltip(DrawContext context, int x, int y, @Nullable Slot slot) {
        if (!this.isOpen()) {
            return;
        }
        this.recipesArea.drawTooltip(context, x, y);
        this.ghostRecipe.drawTooltip(context, this.client, x, y, slot);
    }

    protected abstract Text getToggleCraftableButtonText();

    public void drawGhostSlots(DrawContext context, boolean resultHasPadding) {
        this.ghostRecipe.draw(context, this.client, resultHasPadding);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        boolean bl;
        if (!this.isOpen() || this.client.player.isSpectator()) {
            return false;
        }
        if (this.recipesArea.mouseClicked(click, this.getLeft(), this.getTop(), 147, 166, doubled)) {
            NetworkRecipeId networkRecipeId = this.recipesArea.getLastClickedRecipe();
            ModRecipeResultCollection recipeResultCollection = this.recipesArea.getLastClickedResults();
            if (networkRecipeId != null && recipeResultCollection != null) {
                if (!this.select(recipeResultCollection, networkRecipeId, click.hasShift())) {
                    return false;
                }
                this.selectedRecipeResults = recipeResultCollection;
                this.selectedRecipe = networkRecipeId;
                if (!this.isWide()) {
                    this.setOpen(false);
                }
            }
            return true;
        }
        if (this.searchField != null) {
            bl = this.searchFieldRect != null && this.searchFieldRect.contains(MathHelper.floor(click.x()), MathHelper.floor(click.y()));
            if (bl || this.searchField.mouseClicked(click, doubled)) {
                this.searchField.setFocused(true);
                return true;
            }
            this.searchField.setFocused(false);
        }
        if (this.toggleCraftableButton.mouseClicked(click, doubled)) {
            bl = this.toggleFilteringCraftable();
            this.toggleCraftableButton.setToggled(bl);
            this.updateTooltip();
            this.sendBookDataPacket();
            this.refreshResults(false, bl);
            return true;
        }
        for (RecipeGroupButtonWidget recipeGroupButtonWidget : this.tabButtons) {
            if (!recipeGroupButtonWidget.mouseClicked(click, doubled)) {
                continue;
            }
            if (this.currentTab != recipeGroupButtonWidget) {
                if (this.currentTab != null) {
                    this.currentTab.setToggled(false);
                }
                this.currentTab = recipeGroupButtonWidget;
                this.currentTab.setToggled(true);
                this.refreshResults(true, this.isFilteringCraftable());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        if (this.searchField != null && this.searchField.isFocused()) {
            return this.searchField.mouseDragged(click, offsetX, offsetY);
        }
        return false;
    }

    private boolean select(ModRecipeResultCollection results, NetworkRecipeId recipeId, boolean bl) {
        if (!results.isCraftable(recipeId) && recipeId.equals(this.selectedRecipeId)) {
            return false;
        }
        this.selectedRecipeId = recipeId;
        this.ghostRecipe.clear();
        this.client.interactionManager.clickRecipe(this.client.player.currentScreenHandler.syncId, recipeId, bl);
        return true;
    }

    private boolean toggleFilteringCraftable() {
        RecipeBookType recipeBookType = this.craftingScreenHandler.getCategory();
        boolean bl = !this.recipeBook.isFilteringCraftable(recipeBookType);
        this.recipeBook.setFilteringCraftable(recipeBookType, bl);
        return bl;
    }

    public boolean isClickOutsideBounds(double mouseX, double mouseY, int x, int y, int backgroundWidth, int backgroundHeight) {
        if (!this.isOpen()) {
            return true;
        }
        boolean bl = mouseX < (double)x || mouseY < (double)y || mouseX >= (double)(x + backgroundWidth) || mouseY >= (double)(y + backgroundHeight);
        boolean bl2 = (double)(x - 147) < mouseX && mouseX < (double)x && (double)y < mouseY && mouseY < (double)(y + backgroundHeight);
        return bl && !bl2 && !this.currentTab.isSelected();
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        this.searching = false;
        if (!this.isOpen() || this.client.player.isSpectator()) {
            return false;
        }
        if (input.isEscape() && !this.isWide()) {
            this.setOpen(false);
            return true;
        }
        if (this.searchField.keyPressed(input)) {
            this.refreshSearchResults();
            return true;
        }
        if (this.searchField.isFocused() && this.searchField.isVisible() && !input.isEscape()) {
            return true;
        }
        if (this.client.options.chatKey.matchesKey(input) && !this.searchField.isFocused()) {
            this.searching = true;
            this.searchField.setFocused(true);
            return true;
        }
        if (input.isEnterOrSpace() && this.selectedRecipeResults != null && this.selectedRecipe != null) {
            ClickableWidget.playClickSound(MinecraftClient.getInstance().getSoundManager());
            return this.select(this.selectedRecipeResults, this.selectedRecipe, input.hasShift());
        }
        return false;
    }

    @Override
    public boolean keyReleased(KeyInput input) {
        this.searching = false;
        return Element.super.keyReleased(input);
    }

    @Override
    public boolean charTyped(CharInput input) {
        if (this.searching) {
            return false;
        }
        if (!this.isOpen() || this.client.player.isSpectator()) {
            return false;
        }
        if (this.searchField.charTyped(input)) {
            this.refreshSearchResults();
            return true;
        }
        return Element.super.charTyped(input);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return false;
    }

    @Override
    public void setFocused(boolean focused) {
    }

    @Override
    public boolean isFocused() {
        return false;
    }

    private void refreshSearchResults() {
        String string = this.searchField.getText().toLowerCase(Locale.ROOT);
        this.triggerPirateSpeakEasterEgg(string);
        if (!string.equals(this.searchText)) {
            this.refreshResults(false, this.isFilteringCraftable());
            this.searchText = string;
        }
    }

    private void triggerPirateSpeakEasterEgg(String search) {
        if ("excitedze".equals(search)) {
            LanguageManager languageManager = this.client.getLanguageManager();
            LanguageDefinition languageDefinition = languageManager.getLanguage("en_pt");
            if (languageDefinition == null || languageManager.getLanguage().equals("en_pt")) {
                return;
            }
            languageManager.setLanguage("en_pt");
            this.client.options.language = "en_pt";
            this.client.reloadResources();
            this.client.options.write();
        }
    }

    private boolean isWide() {
        return this.leftOffset == 86;
    }

    public void refresh() {
        this.populateAllRecipes();
        this.refreshTabButtons(this.isFilteringCraftable());
        if (this.isOpen()) {
            this.refreshResults(false, this.isFilteringCraftable());
        }
    }

    public void onRecipeDisplayed(NetworkRecipeId recipeId) {
        this.client.player.onRecipeDisplayed(recipeId);
    }

    public void onCraftFailed(RecipeDisplay display) {;
        this.ghostRecipe.clear();
        ContextParameterMap contextParameterMap = SlotDisplayContexts.createParameters(Objects.requireNonNull(this.client.world));
        this.showGhostRecipe(this.ghostRecipe, display, contextParameterMap);
    }

    protected abstract void showGhostRecipe(GhostRecipe var1, RecipeDisplay var2, ContextParameterMap var3);

    protected void sendBookDataPacket() {
        if (this.client.getNetworkHandler() != null) {
            RecipeBookType recipeBookType = this.craftingScreenHandler.getCategory();
            boolean bl = this.recipeBook.getOptions().isGuiOpen(recipeBookType);
            boolean bl2 = this.recipeBook.getOptions().isFilteringCraftable(recipeBookType);
            this.client.getNetworkHandler().sendPacket(new RecipeCategoryOptionsC2SPacket(recipeBookType, bl, bl2));
        }
    }

    @Override
    public Selectable.SelectionType getType() {
        return this.open ? Selectable.SelectionType.HOVERED : Selectable.SelectionType.NONE;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
        ArrayList<ClickableWidget> list = Lists.newArrayList();
        this.recipesArea.forEachButton(button -> {
            if (button.isInteractable()) {
                list.add(button);
            }
        });
        list.add(this.searchField);
        list.add(this.toggleCraftableButton);
        list.addAll(this.tabButtons);
        Screen.SelectedElementNarrationData selectedElementNarrationData = Screen.findSelectedElementData(list, null);
        if (selectedElementNarrationData != null) {
            selectedElementNarrationData.selectable().appendNarrations(builder.nextMessage());
        }
    }

}



