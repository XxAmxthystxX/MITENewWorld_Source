package com.mitenewworld.mixin.clientmixin;


import com.mitenewworld.MITENewWorld;
import com.mitenewworld.screen.recipebook.ModInventoryRecipeBook;
import com.mitenewworld.core.shadow.ShadowScreenHandler;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.RecipeBookScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Deprecated
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends RecipeBookScreen<PlayerScreenHandler> {

    @Unique
    private static final Identifier REALLYCRAFT_TEXTURE = Identifier.of(MITENewWorld.MOD_ID, "textures/gui/widget/invreadycraft.png");
    @Unique
    private static final Identifier LOCKED_TEXTRUE = Identifier.of(MITENewWorld.MOD_ID,"textures/gui/widget/locked.png");
    @Unique
    private static final Identifier PROGRESS_TEXTRUE = Identifier.of(MITENewWorld.MOD_ID, "textures/gui/widget/invprogress.png");

    public InventoryScreenMixin(PlayerScreenHandler handler, RecipeBookWidget<?> recipeBook, PlayerInventory inventory, Text title) {
        super(handler, recipeBook, inventory, title);
    }


    @Inject(
            method = "<init>(Lnet/minecraft/entity/player/PlayerEntity;)V",
            at = @At("TAIL")
    )
    private void onInitTail(PlayerEntity player, CallbackInfo ci) {
        // 通过反射替换 recipeBookWidget 字段
        try {
            Field field = RecipeBookScreen.class.getDeclaredField("recipeBook");
            field.setAccessible(true);

            ModInventoryRecipeBook customWidget = new ModInventoryRecipeBook(player.playerScreenHandler);
            field.set(this, customWidget);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        PropertyDelegate handlerPropertiesDelegate = ((ShadowScreenHandler) this.handler).getPropertyDelegate();
        if (handlerPropertiesDelegate.get(3) == 1) {
            int a = getScaledProgress(handlerPropertiesDelegate.get(1), handlerPropertiesDelegate.get(0));
            context.drawTexture(RenderPipelines.GUI_TEXTURED, PROGRESS_TEXTRUE,x + 135,y + 29,0,0, a , 14, 32, 32);
        } else {
            switch (handlerPropertiesDelegate.get(2)) {
                case 1 ->
                        context.drawTexture(RenderPipelines.GUI_TEXTURED,REALLYCRAFT_TEXTURE,x + 135,y + 29,0,0,16 , 14, 32, 32);
                case 2 ->
                        context.drawTexture(RenderPipelines.GUI_TEXTURED,LOCKED_TEXTRUE, x + 135,y + 29,0,0,16 , 14, 32, 32);

            }
        }


    }
    @Unique
    private int getScaledProgress(int i , int j) {
        return i != 0 && j != 0 ? j * 16 / i : 0;
    }

    @Shadow
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {

    }
    @Override
    public boolean mouseClicked(Click click, boolean doubled) {

        double d = click.x() - (double) (x + 135);
        double e = click.y() - (double) (y + 29);
        if (d >= 0.0 && e >= 0.0 && d < 21.0 && e < 12.0 && this.handler.onButtonClick(this.client.player, 0)) {
            this.client.interactionManager.clickButton(this.handler.syncId, 0);
            return true;
        }
        return super.mouseClicked(click, doubled);

    }
}
