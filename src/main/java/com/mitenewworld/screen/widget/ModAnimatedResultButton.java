package com.mitenewworld.screen.widget;

import com.mitenewworld.MITENewWorld;
import com.mitenewworld.screen.recipebook.ModRecipeBookResults;
import com.mitenewworld.screen.recipebook.ModRecipeResultCollection;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.screen.recipebook.*;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.input.MouseInput;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.NetworkRecipeId;
import net.minecraft.recipe.RecipeDisplayEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextParameterMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Environment( EnvType.CLIENT)
public class ModAnimatedResultButton extends ClickableWidget {
    private static final Identifier SLOT_CRAFTABLE = Identifier.of(MITENewWorld.MOD_ID , "textures/gui/recipe_book/slot_craftable.png");
    private static final Identifier SLOT_LOW_LEVEL = Identifier.of(MITENewWorld.MOD_ID , "textures/gui/recipe_book/slot_lowlevel.png");
    private static final Identifier SLOT_NO_INGREDIENT = Identifier.of(MITENewWorld.MOD_ID , "textures/gui/recipe_book/slot_noingredient.png");
    private static final Text MORE_RECIPES_TEXT = Text.translatable("gui.recipebook.moreRecipes");
    private ModRecipeResultCollection resultCollection = ModRecipeResultCollection.EMPTY;
    private List<ModAnimatedResultButton.Result> results = List.of();
    private boolean allResultsEqual;
    private final CurrentIndexProvider currentIndexProvider;
    private float bounce;

    public ModAnimatedResultButton(CurrentIndexProvider currentIndexProvider) {
        super(0, 0, 25, 25, ScreenTexts.EMPTY);
        this.currentIndexProvider = currentIndexProvider;
    }
    public void showResultCollection(ModRecipeResultCollection resultCollection, boolean filteringCraftable, ModRecipeBookResults results, ContextParameterMap context) {
        if (resultCollection == null || resultCollection == ModRecipeResultCollection.EMPTY) {
            this.results = List.of();
            this.visible = false;
            this.allResultsEqual = false;
            return;
        }
        this.resultCollection = resultCollection;
        List<RecipeDisplayEntry> list1 = resultCollection.filter(filteringCraftable ? ModRecipeResultCollection.ModRecipeFilterMode.CRAFTABLE : ModRecipeResultCollection.ModRecipeFilterMode.ANY);
        if (list1.isEmpty()) {
            this.results = List.of();
            this.visible = false;
            this.allResultsEqual = false;
            return;
        }
        this.visible = true;
        this.results = list1.stream().map(entry -> {
            List<ItemStack> stacks = entry.getStacks(context);
            if (stacks == null || stacks.isEmpty()) {
                stacks = List.of(new ItemStack(Items.BARRIER));
            }
            return new ModAnimatedResultButton.Result(entry.id(), stacks);
        }).toList();

        this.allResultsEqual = ModAnimatedResultButton.areAllResultsEqual(this.results);
        if (results != null) {
            ClientRecipeBook recipeBook = results.getRecipeBook();
            if (recipeBook != null) {
                List<NetworkRecipeId> list2 = list1.stream()
                        .map(RecipeDisplayEntry::id)
                        .filter(recipeBook::isHighlighted)
                        .toList();

                if (!list2.isEmpty()) {
                    list2.forEach(results::onRecipeDisplayed);
                    this.bounce = 15.0f;
                }
            }
        }
    }
    private static boolean areAllResultsEqual(List<ModAnimatedResultButton.Result> results) {
        Iterator<ItemStack> iterator = results.stream().flatMap(result -> result.displayItems().stream()).iterator();
        if (!iterator.hasNext()) {
            return true;
        }
        ItemStack itemStack = iterator.next();
        while (iterator.hasNext()) {
            ItemStack itemStack2 = iterator.next();
            if (ItemStack.areItemsAndComponentsEqual(itemStack, itemStack2)) {
                continue;
            }
            return false;
        }
        return true;
    }

    public ModRecipeResultCollection getResultCollection() {
        return this.resultCollection;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        boolean bl;
        Identifier identifier = switch (this.resultCollection.getRecipeState()) {
            case 0 -> SLOT_CRAFTABLE;
            case 1 -> SLOT_LOW_LEVEL;
            case 2 -> SLOT_NO_INGREDIENT;
            default -> SLOT_NO_INGREDIENT;
        };
        bl = this.bounce > 0.0f;
        if (bl) {
            float f = 1.0f + 0.1f * (float)Math.sin(this.bounce / 15.0f * (float)Math.PI);
            context.getMatrices().pushMatrix();
            context.getMatrices().translate(this.getX() + 8, this.getY() + 12);
            context.getMatrices().scale(f, f);
            context.getMatrices().translate(-(this.getX() + 8), -(this.getY() + 12));
            this.bounce -= deltaTicks;
        }
        context.drawTexture(RenderPipelines.GUI_TEXTURED, identifier, this.getX(), this.getY(), 0, 0 , 25 , 25 , 25 , 25);
        ItemStack itemStack = this.getDisplayStack();
        int i = 4;
        if (this.hasMultipleResults() && this.allResultsEqual) {
            context.drawItem(itemStack, this.getX() + i + 1, this.getY() + i + 1, 0);
            --i;
        }
        context.drawItemWithoutEntity(itemStack, this.getX() + i, this.getY() + i);
        if (bl) {
            context.getMatrices().popMatrix();
        }
    }

    private boolean hasMultipleResults() {
        return this.results.size() > 1;
    }

    public boolean hasSingleResult() {
        return this.results.size() == 1;
    }

    public NetworkRecipeId getCurrentId() {
        int j = this.results.size();
        if (j == 0) {
            return new NetworkRecipeId(0);
        }
        int i = this.currentIndexProvider.currentIndex() % j;
        return this.results.get(i).id;
    }

    public ItemStack getDisplayStack() {
        int i = this.currentIndexProvider.currentIndex();
        int j = this.results.size();
        if (j == 0) {
            return ItemStack.EMPTY;
        }
        int k = i / j;
        int l = i - j * k;
        return this.results.get(l).getDisplayStack(k);
    }

    public List<Text> getTooltip(ItemStack stack) {
        ArrayList<Text> list = new ArrayList<>(Screen.getTooltipFromItem(MinecraftClient.getInstance(), stack));
        if (this.hasMultipleResults()) {
            list.add(MORE_RECIPES_TEXT);
        }
        return list;
    }
    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        return super.mouseClicked(click, doubled);
    }
    @Override
    public void appendClickableNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, Text.translatable("narration.recipe", this.getDisplayStack().getName()));
        if (this.hasMultipleResults()) {
            builder.put(NarrationPart.USAGE, Text.translatable("narration.button.usage.hovered"), Text.translatable("narration.recipe.usage.more"));
        } else {
            builder.put(NarrationPart.USAGE, Text.translatable("narration.button.usage.hovered"));
        }
    }

    @Override
    public int getWidth() {
        return 25;
    }

    @Override
    protected boolean isValidClickButton(MouseInput input) {
        return input.button() == 0 || input.button() == InputUtil.GLFW_MOUSE_BUTTON_RIGHT;
    }

    @Environment(value=EnvType.CLIENT)
    record Result(NetworkRecipeId id, List<ItemStack> displayItems) {
        public ItemStack getDisplayStack(int currentIndex) {
            if (this.displayItems.isEmpty()) {
                return ItemStack.EMPTY;
            }
            int i = currentIndex % this.displayItems.size();
            return this.displayItems.get(i);
        }
    }
}

