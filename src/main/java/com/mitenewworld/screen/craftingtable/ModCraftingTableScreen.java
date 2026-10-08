package com.mitenewworld.screen.craftingtable;
import com.mitenewworld.screen.ModRecipeBookScreen;

import com.mitenewworld.MITENewWorld;
import com.mitenewworld.screen.recipebook.ModCraftingRecipeBook;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenPos;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModCraftingTableScreen extends ModRecipeBookScreen<ModCraftingTableScreenHandler> implements RecipeBookProvider {
    private static final Identifier CANTCRAFT_TEXTURE = Identifier.of(MITENewWorld.MOD_ID,"textures/gui/widget/cantcraft.png");
    private static final Identifier REALLYCRAFT_TEXTURE = Identifier.of(MITENewWorld.MOD_ID,"textures/gui/widget/readycraft.png");
    private static final Identifier LOCKED_TEXTURE = Identifier.of(MITENewWorld.MOD_ID,"textures/gui/widget/locked.png");
    private static final Identifier PROGRESS_TEXTURE = Identifier.of(MITENewWorld.MOD_ID,"textures/gui/widget/progress.png");
    private static final Identifier TEXTRUE = Identifier.of(MITENewWorld.MOD_ID,"textures/gui/container/modcrafting_table.png");

    public ModCraftingTableScreen(ModCraftingTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, new ModCraftingRecipeBook(handler), inventory, title);
    }
    @Override
    protected void init() {
        super.init();

    }

    @Override
    protected ScreenPos getRecipeBookButtonPos() {
        return new ScreenPos(this.x + 5, this.height / 2 - 49);
    }


    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED ,TEXTRUE, x, y, 0f ,0f ,backgroundWidth ,backgroundHeight, 256, 256);
        renderProgressArrow(context, x, y);
    }
    private void renderProgressArrow(DrawContext context, int x, int y) {
        int craftState = handler.getPropertyDelegate().get(2);
        int craftingState = handler.getPropertyDelegate().get(3);
        if (craftingState == 1 ) {
            int progress = getScaledProgress();
            context.drawTexture(
                    RenderPipelines.GUI_TEXTURED,
                    PROGRESS_TEXTURE,
                    x + 90, y + 35,
                    0, 0,
                    progress, 16,
                    32 ,  32
            );
            return;
        }
        switch (craftState) {
            case 2 ->
                    context.drawTexture(RenderPipelines.GUI_TEXTURED,LOCKED_TEXTURE, x + 90, y + 35, 0, 0, 22, 16, 32, 32);
            case 3 ->
                    context.drawTexture(RenderPipelines.GUI_TEXTURED,REALLYCRAFT_TEXTURE, x + 90, y + 35, 0, 0, 22, 16, 32, 32);
            case 1 ->
                    context.drawTexture(RenderPipelines.GUI_TEXTURED,CANTCRAFT_TEXTURE, x + 90, y + 35, 0, 0, 22, 16, 32, 32);
        }

    }


    public int getScaledProgress() {
        int progress = handler.getPropertyDelegate().get(0);
        int maxprogress = handler.getPropertyDelegate().get(1);
        int progressArrowSize = 22 ;
        return maxprogress != 0 && progress != 0 ? progress * progressArrowSize / maxprogress : 0;

    }

    @Override
    public boolean mouseClicked(Click click, boolean doubleClick) {
        double d = click.x() - (double)(x + 90);
        double e = click.y() - (double)(y + 35);
        if (d >= 0.0 && e >= 0.0 && d < 21.0 && e < 12.0 && this.handler.onButtonClick(this.client.player, 0)) {
            this.client.interactionManager.clickButton(this.handler.syncId , 0);
            return true;
        }
        return super.mouseClicked(click , doubleClick);
    }


}
