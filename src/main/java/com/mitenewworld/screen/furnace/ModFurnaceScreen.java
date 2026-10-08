package com.mitenewworld.screen.furnace;
import com.mitenewworld.screen.ModRecipeBookScreen;

import com.mitenewworld.MITENewWorld;
import com.mitenewworld.screen.recipebook.ModFurnaceRecipeBook;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenPos;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModFurnaceScreen extends ModRecipeBookScreen<ModFurnaceScreenHandler> implements RecipeBookProvider {


    public static final Identifier TEXTURE = Identifier.of( MITENewWorld.MOD_ID ,"textures/gui/container/furnace.png");
    public static final Identifier BURN_PROGRESS = Identifier.of( MITENewWorld.MOD_ID ,"textures/gui/widget/progress.png");
    public static final Identifier FUEL_PROGRESS = Identifier.of( MITENewWorld.MOD_ID ,"textures/gui/widget/fuelprogress.png");
    public static final Identifier LOCKED_TEXTURE = Identifier.of(MITENewWorld.MOD_ID,"textures/gui/widget/locked.png");
    public ModFurnaceScreen(ModFurnaceScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, new ModFurnaceRecipeBook(handler), inventory, title);
    }
    @Override
    public void init() {
        super.init();
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
    }

    @Override
    protected ScreenPos getRecipeBookButtonPos() {
        return new ScreenPos(this.x + 20, this.height / 2 - 49);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED,TEXTURE, x, y,0 ,0 ,backgroundWidth ,backgroundHeight , 256 , 256);
        int i = this.handler.getPropertyDelegate().get(4);
        switch (i) {
            case 0  ->  context.drawTexture(RenderPipelines.GUI_TEXTURED,LOCKED_TEXTURE, x + 80 , y + 35 , 0, 0 , 16, 16, 32, 32);
            case 1  ->  this.renderProgressArrow(context , x , y);
        }
    }
    public int getBurnProgress() {
        int i = this.handler.getPropertyDelegate().get(1);
        int j = this.handler.getPropertyDelegate().get(2);
        return  j != 0 && i != 0 ? i * 25 / j : 0;
    }
    public int getFuelProgress() {
        int i = this.handler.getPropertyDelegate().get(0);
        int j = this.handler.getPropertyDelegate().get(3);
        return  j != 0 && i != 0 ? i * 13 / j : 0;
    }

    private void renderProgressArrow(DrawContext context, int x, int y) {
        int fuelProgress = this.getFuelProgress();
        int burnProgress = this.getBurnProgress();
        context.drawTexture(RenderPipelines.GUI_TEXTURED,FUEL_PROGRESS, x + 57,   y + 37 + (13 - fuelProgress),  0, 14 - fuelProgress, 16, fuelProgress,32, 32);
        context.drawTexture(RenderPipelines.GUI_TEXTURED,BURN_PROGRESS, x + 80 , y + 35 , 0, 0 , burnProgress, 16, 32, 32);
    }


}
