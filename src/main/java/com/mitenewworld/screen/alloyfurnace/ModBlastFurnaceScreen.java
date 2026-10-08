package com.mitenewworld.screen.alloyfurnace;

import com.mitenewworld.MITENewWorld;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModBlastFurnaceScreen extends HandledScreen<ModBlastFurnaceScreenHandler> {

    public static final Identifier TEXTURE = Identifier.of(MITENewWorld.MOD_ID, "textures/gui/container/blastfurnace_gui.png");
    public static final Identifier PROGRESS = Identifier.of(MITENewWorld.MOD_ID, "textures/gui/widget/blastfurnace_progress.png");
    public static final Identifier FUEL_PROGRESS = Identifier.of(MITENewWorld.MOD_ID, "textures/gui/widget/fuelprogress.png");
    public static final Identifier SLOT = Identifier.of(MITENewWorld.MOD_ID, "textures/gui/widget/slot.png");
    public static final Identifier INGOT = Identifier.of(MITENewWorld.MOD_ID, "textures/item/alloy/alloy_ingot.png");
    public static final Identifier WARNING = Identifier.of(MITENewWorld.MOD_ID, "textures/gui/widget/blastfurnace_warning.png");

    private static final int[] TIER_COLORS = {0xFF8A5A, 0xC0C0C0, 0x808080, 0xB060FF, 0x60FFFF};

    private static final int GUI_W = 176;
    private static final int GUI_H = 220;

    private static final int PROGRESS_X = 135;
    private static final int PROGRESS_Y = 61;
    private static final int PROGRESS_W = 32;
    private static final int PROGRESS_H = 14;

    private static final int FUEL_X = 123;
    private static final int FUEL_Y = 107;
    private static final int FUEL_SIZE = 13;

    private static final int INGOT_X = 141;
    private static final int INGOT_Y = 86;
    /** 警告图标相对锭位置的偏移（像素） */
    private static final int WARNING_DX = 0;
    private static final int WARNING_DY = 0;
    /** 警告阈值：超过融化温度的 90% 显示 */
    private static final float WARNING_THRESHOLD = 0.9f;

    public ModBlastFurnaceScreen(ModBlastFurnaceScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = GUI_W;
        this.backgroundHeight = GUI_H;
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (GUI_W - this.textRenderer.getWidth(this.title)) / 2;
        this.playerInventoryTitleX = 7;
        this.playerInventoryTitleY = GUI_H - 94;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, GUI_W, GUI_H, 256, 256);

        for (Slot slot : this.handler.slots) {
            if (slot == this.handler.getFuelSlot() || slot == this.handler.getFluxSlot()) {
                continue;
            }
            context.drawTexture(RenderPipelines.GUI_TEXTURED, SLOT,
                    x + slot.x - 1, y + slot.y - 1, 0, 0, 18, 18, 18, 18);
        }

        int temperature = this.handler.getPropertyDelegate().get(0);
        int maxTemperature = Math.max(1, this.handler.getPropertyDelegate().get(1));
        int cookProgress = this.handler.getPropertyDelegate().get(2);
        int maxCookProgress = Math.max(1, this.handler.getPropertyDelegate().get(3));
        int heatValue = this.handler.getPropertyDelegate().get(5);
        int maxHeatCapacity = Math.max(1, this.handler.getPropertyDelegate().get(6));

        int tier = Math.clamp(this.handler.getTier(), 1, TIER_COLORS.length);
        int tierColor = TIER_COLORS[tier - 1];

        // 锭：基础合金锭贴图 × 合金炉颜色
        context.drawTexture(RenderPipelines.GUI_TEXTURED, INGOT,
                x + INGOT_X, y + INGOT_Y, 0, 0, 16, 16, 16, 16, 0xFF000000 | tierColor);

        // 温度覆盖：红色，alpha 0% ~ 70%
        float tempRatio = Math.min(1.0f, temperature / (float) maxTemperature);
        int alpha = (int) (0.7f * tempRatio * 255.0f);
        if (alpha > 0) {
            context.drawTexture(RenderPipelines.GUI_TEXTURED, INGOT,
                    x + INGOT_X, y + INGOT_Y, 0, 0, 16, 16, 16, 16, ((alpha & 0xFF) << 24) | 0xFF0000);
        }

        // 过热警告（> 90% 融化温度）
        if (temperature * 10 > maxTemperature * 9) {
            context.drawTexture(RenderPipelines.GUI_TEXTURED, WARNING,
                    x + INGOT_X + WARNING_DX, y + INGOT_Y + WARNING_DY, 0, 0, 16, 16, 16, 16);
        }

        // 熔炼进度
        int burn = (int) (PROGRESS_W * (cookProgress / (float) maxCookProgress));
        if (burn > 0) {
            context.drawTexture(RenderPipelines.GUI_TEXTURED, PROGRESS,
                    x + PROGRESS_X, y + PROGRESS_Y, 0, 0, burn, PROGRESS_H, 32, 32);
        }

        // 燃料进度（自下而上）
        int fuel = (int) (FUEL_SIZE * (heatValue / (float) maxHeatCapacity));
        if (fuel > 0) {
            context.drawTexture(RenderPipelines.GUI_TEXTURED, FUEL_PROGRESS,
                    x + FUEL_X, y + FUEL_Y + (FUEL_SIZE - fuel), 0, FUEL_SIZE - fuel,
                    FUEL_SIZE, fuel, 32, 32);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
