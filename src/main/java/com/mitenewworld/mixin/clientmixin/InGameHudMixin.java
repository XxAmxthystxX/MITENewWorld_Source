package com.mitenewworld.mixin.clientmixin;


import com.mitenewworld.MITENewWorld;
import com.mitenewworld.core.shadow.ShadowHungerManager;
import com.mitenewworld.core.shadow.ShadowMinecraftClient;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Unique
    private static final Identifier IN_BREAK_HAIR = Identifier.of(MITENewWorld.MOD_ID, "textures/gui/hud/inbreakhair.png");
    @Shadow
    private static final Identifier FOOD_EMPTY_HUNGER_TEXTURE = Identifier.ofVanilla("hud/food_empty_hunger");
    @Shadow
    private static final Identifier FOOD_HALF_HUNGER_TEXTURE = Identifier.ofVanilla("hud/food_half_hunger");
    @Shadow
    private static final Identifier FOOD_FULL_HUNGER_TEXTURE = Identifier.ofVanilla("hud/food_full_hunger");
    @Shadow
    private static final Identifier FOOD_EMPTY_TEXTURE = Identifier.ofVanilla("hud/food_empty");
    @Shadow
    private static final Identifier FOOD_HALF_TEXTURE = Identifier.ofVanilla("hud/food_half");
    @Shadow
    private static final Identifier FOOD_FULL_TEXTURE = Identifier.ofVanilla("hud/food_full");
    @Shadow
    private final Random random = Random.create();
    @Mutable
    @Final
    @Shadow
    private final MinecraftClient client;
    @Shadow
    private int ticks;

    public InGameHudMixin(MinecraftClient client) {
        this.client = client;
    }

    @Redirect(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIII)V"))
    private void renderCrosshair(DrawContext instance, RenderPipeline pipeline, Identifier sprite, int x, int y, int width, int height) {
        boolean isBreak = ((ShadowMinecraftClient)client).getBreakState();
        if (isBreak) {
            instance.drawTexture(pipeline,IN_BREAK_HAIR, x, y, 0, 0, 16, 16 , 16, 16);
        } else {
            instance.drawGuiTexture(pipeline,sprite, x, y, width, height);
        }
    }

    @Inject(method = "renderFood", at = @At(value = "HEAD"), cancellable = true)
    private void renderFood(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo ci) {
        ci.cancel();
        HungerManager hungerManager = player.getHungerManager();
        int i = hungerManager.getFoodLevel();
        int u = ((ShadowHungerManager)hungerManager).getMaxFoodLevel() / 2;
        for (int j = 0; j < u; j++) {
            int k = top;
            Identifier identifier;
            Identifier identifier2;
            Identifier identifier3;
            if (player.hasStatusEffect(StatusEffects.HUNGER)) {
                identifier = FOOD_EMPTY_HUNGER_TEXTURE;
                identifier2 = FOOD_HALF_HUNGER_TEXTURE;
                identifier3 = FOOD_FULL_HUNGER_TEXTURE;
            } else {
                identifier = FOOD_EMPTY_TEXTURE;
                identifier2 = FOOD_HALF_TEXTURE;
                identifier3 = FOOD_FULL_TEXTURE;
            }

            if (player.getHungerManager().getSaturationLevel() <= 0.0F && this.ticks % (i * 3 + 1) == 0) {
                k = top + (this.random.nextInt(3) - 1);
            }

            int l = right - j * 8 - 9;
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED,identifier, l, k, 9, 9);
            if (j * 2 + 1 < i) {
                context.drawGuiTexture(RenderPipelines.GUI_TEXTURED,identifier3, l, k, 9, 9);
            }

            if (j * 2 + 1 == i) {
                context.drawGuiTexture(RenderPipelines.GUI_TEXTURED,identifier2, l, k, 9, 9);
            }
        }

    }

    @Shadow
    public TextRenderer getTextRenderer() {
        return this.client.textRenderer;
    }

}
