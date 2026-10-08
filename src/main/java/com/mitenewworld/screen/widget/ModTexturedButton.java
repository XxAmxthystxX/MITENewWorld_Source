package com.mitenewworld.screen.widget;
import com.mitenewworld.MITENewWorld;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModTexturedButton extends ButtonWidget {

    private final ModButtonTextures textures;
    private long clickTime = -1;
    private static final long CLICK_DURATION = 200;

    public ModTexturedButton(int x, int y, int width, int height,
                             ModButtonTextures textures, PressAction pressAction) {
        this(x, y, width, height, textures, pressAction, ScreenTexts.EMPTY);
    }

    public ModTexturedButton(int x, int y, int width, int height,
                             ModButtonTextures textures, PressAction pressAction, Text text) {
        super(x, y, width, height, text, pressAction, DEFAULT_NARRATION_SUPPLIER);
        this.textures = textures;
    }

    public ModTexturedButton(int width, int height,
                             ModButtonTextures textures, PressAction pressAction, Text text) {
        this(0, 0, width, height, textures, pressAction, text);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        long now = System.currentTimeMillis();
        if (this.clickTime > 0 && now - this.clickTime >= CLICK_DURATION) {
            this.clickTime = -1;
        }

        Identifier tex;
        if (!this.active || this.clickTime > 0) {
            // 禁用与按下共用同一张纹理
            tex = this.textures.get(3);
        } else if (this.isHovered()) {
            tex = this.textures.get(2);
        } else {
            tex = this.textures.get(1);
        }

        context.drawTexture(RenderPipelines.GUI_TEXTURED, tex,
                this.getX(), this.getY(), 0, 0,
                this.getWidth(), this.getHeight(),
                this.getWidth(), this.getHeight());

        int textColor = this.active ? 0xFFFFFF : 0xA0A0A0;
        this.drawMessage(context, MinecraftClient.getInstance().textRenderer, textColor);
    }

    @Override
    public void onClick(Click click, boolean doubled) {
        if (!this.active) {
            return;
        }
        this.clickTime = System.currentTimeMillis();
        super.onClick(click, doubled);
    }

    /** 供外部触发按下视觉，例如判定按钮由键盘触发 */
    public void flashPress() {
        this.clickTime = System.currentTimeMillis();
    }
}
