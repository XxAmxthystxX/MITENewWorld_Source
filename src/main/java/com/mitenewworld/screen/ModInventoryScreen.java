package com.mitenewworld.screen;

import com.mitenewworld.MITENewWorld;
import com.mitenewworld.screen.recipebook.ModInventoryRecipeBook;
import com.mitenewworld.core.shadow.ShadowScreenHandler;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenPos;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.StatusEffectsDisplay;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;

public class ModInventoryScreen extends ModRecipeBookScreen<PlayerScreenHandler>{
    private static final Identifier REALLYCRAFT_TEXTURE = Identifier.of(MITENewWorld.MOD_ID, "textures/gui/widget/invreadycraft.png");
    private static final Identifier LOCKED_TEXTRUE = Identifier.of(MITENewWorld.MOD_ID,"textures/gui/widget/locked.png");
    private static final Identifier PROGRESS_TEXTRUE = Identifier.of(MITENewWorld.MOD_ID, "textures/gui/widget/invprogress.png");
    private float mouseX;
    private float mouseY;
    private boolean mouseDown;
    private final StatusEffectsDisplay statusEffectsDisplay;

    // ---- 合成进度：服务端只在"新一轮开始"时同步一次（craftRound），之后客户端本地按 tick 推算 ----
    private boolean clientCrafting = false;
    private int clientCraftTicks = 0;
    private int clientCraftMax = 1;
    private int clientCraftRound = -1;

    public ModInventoryScreen(PlayerEntity player) {
        super(player.playerScreenHandler, new ModInventoryRecipeBook(player.playerScreenHandler), player.getInventory(), Text.translatable("container.crafting"));
        this.titleX = 97;
        this.statusEffectsDisplay = new StatusEffectsDisplay(this);
    }

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();
        if (this.client.player.isInCreativeMode()) {
            this.client.setScreen(new CreativeInventoryScreen(this.client.player, this.client.player.networkHandler.getEnabledFeatures(), this.client.options.getOperatorItemsTab().getValue()));
            return;
        }
        PropertyDelegate delegate = ((ShadowScreenHandler) this.handler).getPropertyDelegate();
        boolean running = delegate.get(3) == 1;
        int round = delegate.get(4);
        if (running) {
            if (!this.clientCrafting || round != this.clientCraftRound) {
                // 新一轮开始：重置本地计数，长度取服务端下发的一次性 maxprogress
                this.clientCrafting = true;
                this.clientCraftRound = round;
                this.clientCraftMax = Math.max(1, delegate.get(1));
                this.clientCraftTicks = 0;
            } else {
                this.clientCraftTicks++;
            }
        } else {
            this.clientCrafting = false;
            this.clientCraftTicks = 0;
        }
    }

    @Override
    protected void init() {
        if (this.client.player.isInCreativeMode()) {
            this.client.setScreen(new CreativeInventoryScreen(this.client.player, this.client.player.networkHandler.getEnabledFeatures(), this.client.options.getOperatorItemsTab().getValue()));
            return;
        }
        super.init();
    }

    @Override
    protected ScreenPos getRecipeBookButtonPos() {

        return new ScreenPos(this.x + 104 , this.height / 2 - 22);
    }
    @Override
    protected void onRecipeBookToggled() {
        this.mouseDown = true;
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(this.textRenderer, this.title, this.titleX, this.titleY, Colors.DARK_GRAY, false);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        PropertyDelegate handlerPropertiesDelegate = ((ShadowScreenHandler) this.handler).getPropertyDelegate();
        if (this.clientCrafting) {
            // 本地推算，不依赖服务端逐 tick 下发
            int current = Math.min(this.clientCraftTicks, this.clientCraftMax);
            int a = getScaledProgress(this.clientCraftMax, current);
            context.drawTexture(RenderPipelines.GUI_TEXTURED, PROGRESS_TEXTRUE, x + 135, y + 29, 0, 0, a, 14, 32, 32);
        } else {
            switch (handlerPropertiesDelegate.get(2)) {
                case 1 ->
                        context.drawTexture(RenderPipelines.GUI_TEXTURED,REALLYCRAFT_TEXTURE,x + 135,y + 29,0,0,16 , 14, 32, 32);
                case 2 ->
                        context.drawTexture(RenderPipelines.GUI_TEXTURED,LOCKED_TEXTRUE, x + 135,y + 29,0,0,16 , 14, 32, 32);

            }
        }
        this.statusEffectsDisplay.drawStatusEffects(context, mouseX, mouseY);
        super.render(context, mouseX, mouseY, deltaTicks);
        this.statusEffectsDisplay.drawStatusEffectTooltip(context, mouseX, mouseY);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    private int getScaledProgress(int i , int j) {
        return i != 0 && j != 0 ? j * 16 / i : 0;
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
    @Override
    public boolean showsStatusEffects() {
        return this.statusEffectsDisplay.shouldHideStatusEffectHud();
    }

    @Override
    protected boolean shouldAddPaddingToGhostResult() {
        return false;
    }

    @Override
    protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {
        int i = this.x;
        int j = this.y;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, i, j, 0.0f, 0.0f, this.backgroundWidth, this.backgroundHeight, 256, 256);
        InventoryScreen.drawEntity(context, i + 26, j + 8, i + 75, j + 78, 30, 0.0625f, this.mouseX, this.mouseY, (LivingEntity)this.client.player);
    }

    @Override
    public boolean mouseReleased(Click click) {
        if (this.mouseDown) {
            this.mouseDown = false;
            return true;
        }
        return super.mouseReleased(click);
    }
}
