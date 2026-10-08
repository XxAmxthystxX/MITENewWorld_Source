package com.mitenewworld.screen.castingtable;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.entity.blockentity.castingtable.ModCastingTableEntity;
import com.mitenewworld.recipe.casting.CastingTemplate;
import com.mitenewworld.recipe.casting.ModCastingTemplates;
import com.mitenewworld.network.MinigameClickC2SPacket;
import com.mitenewworld.screen.slot.ModCastingSlot;
import com.mitenewworld.screen.widget.ModButtonTextures;
import com.mitenewworld.screen.widget.ModTexturedButton;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ModCastingTableScreen extends HandledScreen<ModCastingTableScreenHandler> {

    // ==================== 贴图 ====================

    private static final Identifier TEX_BG =
            Identifier.of("mitenewworld", "textures/gui/container/castingtable_gui.png");
    private static final Identifier TEX_PROGRESS =
            Identifier.of("mitenewworld", "textures/gui/widget/casting_progress.png");
    private static final Identifier TEX_FLAG =
            Identifier.of("mitenewworld", "textures/gui/widget/flag.png");
    /** 铸造槽的槽位框：贴图由每个槽位的 SlotType 决定（见 ModCastingSlot.backgroundTexture） */
    private static final int SLOT_SIZE = 18;

    /** 底图文件为 256x256，面板实际占左上 176x220 */
    private static final int BG_WIDTH = 176;
    private static final int BG_HEIGHT = 220;
    private static final int TEX_SIZE = 256;

    // ==================== 布局（均相对背景左上角） ====================

    /** 判定框 */
    private static final int JUDGE_X = 12;
    private static final int JUDGE_Y = 18;
    private static final int JUDGE_W = 100;
    private static final int JUDGE_H = 16;
    /** 进度条（箭头，随进度裁剪宽度） */
    private static final int PROGRESS_X = 105;
    private static final int PROGRESS_Y = 70;
    private static final int PROGRESS_W = 25;
    private static final int PROGRESS_H = 15;
    /** casting_progress.png 实际为 32x32，贴图采样尺寸必须按真实大小传 */
    private static final int PROGRESS_TEX_SIZE = 32;
    /** 模板展示位（18x18 槽框，物品画在 +1,+1） */
    private static final int TEMPLATE_X = 137;
    private static final int TEMPLATE_Y = 95;
    private static final int TEMPLATE_SIZE = 18;
    /** 翻页按钮（12x17） */
    private static final int PAGE_PREV_X = 123;
    private static final int PAGE_NEXT_X = 157;
    private static final int PAGE_Y = 95;
    private static final int PAGE_W = 12;
    private static final int PAGE_H = 17;
    /** 铸造按钮（20x20） */
    private static final int BUTTON_X = 137;
    private static final int BUTTON_Y = 17;
    private static final int BUTTON_SIZE = 20;
    /** 锻造次数文本（放右上角，避开原版标题位） */
    private static final int USES_X = 118;
    private static final int USES_Y = 7;
    /** 浮标（flag.png 内容实际为 5x11） */
    private static final int FLAG_W = 5;
    private static final int FLAG_H = 11;

    /** 模板显示名的语言键前缀 */
    private static final String TEMPLATE_LANG_PREFIX = "casting.mitenewworld.";

    // ==================== 屏幕字段 ====================

    private final MinigameState minigame = new MinigameState();
    private int lastTemplateIndex = -1;
    private int lastStateOrdinal = -1;

    private ModTexturedButton startButton;
    private ModTexturedButton prevButton;
    private ModTexturedButton nextButton;

    public ModCastingTableScreen(ModCastingTableScreenHandler handler, PlayerInventory inv, Text title) {
        super(handler, inv, title);
        this.backgroundWidth = BG_WIDTH;
        this.backgroundHeight = BG_HEIGHT;
    }

    @Override
    protected void init() {
        super.init();
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        // 翻页按钮（模板展示位两侧）
        prevButton = new ModTexturedButton(x + PAGE_PREV_X, y + PAGE_Y, PAGE_W, PAGE_H,
                ModButtonTextures.TEMPLATE_PREV,
                b -> client.interactionManager.clickButton(handler.syncId,
                        ModCastingTableScreenHandler.BTN_TEMPLATE_PREV));
        nextButton = new ModTexturedButton(x + PAGE_NEXT_X, y + PAGE_Y, PAGE_W, PAGE_H,
                ModButtonTextures.TEMPLATE_NEXT,
                b -> client.interactionManager.clickButton(handler.syncId,
                        ModCastingTableScreenHandler.BTN_TEMPLATE_NEXT));

        // 开始/判定按钮
        startButton = new ModTexturedButton(x + BUTTON_X, y + BUTTON_Y, BUTTON_SIZE, BUTTON_SIZE,
                ModButtonTextures.CASTING_START,
                b -> onStartOrJudge());

        addDrawableChild(prevButton);
        addDrawableChild(nextButton);
        addDrawableChild(startButton);

        // 标题与原版"物品栏"标题改成与自绘槽位对齐（原版默认 (8,6) / (8, height-94)）
        this.titleX = 8;
        this.titleY = 6;
        this.playerInventoryTitleX = ModCastingTableScreenHandler.PLAYER_INV_X + ModCastingTableScreenHandler.SLOT_DRAW_OFFSET;
        this.playerInventoryTitleY = ModCastingTableScreenHandler.PLAYER_INV_Y + ModCastingTableScreenHandler.SLOT_DRAW_OFFSET - 12;

        refreshButtonStates();
    }

    private void onStartOrJudge() {
        int stateOrd = getStateOrdinal();
        if (stateOrd == ModCastingTableEntity.CastingState.READY.ordinal()) {
            // READY：请求开始（发送一个 grade=0 的点击包，服务端走 startForging）
            sendMinigameClick(0, 0, 0);
        } else if (stateOrd == ModCastingTableEntity.CastingState.FORGING.ordinal()) {
            int grade = minigame.click();
            sendMinigameClick(grade, minigame.sliderPosition, minigame.cycleTick);
        }
    }

    private void sendMinigameClick(int grade, int sliderPosition, int cycleTick) {
        if (client != null && client.getNetworkHandler() != null) {
            ClientPlayNetworking.send(new MinigameClickC2SPacket(handler.pos(), grade, sliderPosition, cycleTick));
        }
    }

    private int getStateOrdinal() {
        return handler.getProperty(1);
    }

    private void refreshButtonStates() {
        int stateOrd = getStateOrdinal();
        ModCastingTableEntity.CastingState state =
                ModCastingTableEntity.CastingState.byOrdinal(stateOrd);

        // 锤子存在与否从客户端库存读
        boolean hasHammer = handler.slots.get(ModCastingTableEntity.CASTING_SLOTS + 1).hasStack();
        boolean canStart = state == ModCastingTableEntity.CastingState.READY && hasHammer;
        boolean canJudge = state == ModCastingTableEntity.CastingState.FORGING;

        startButton.active = canStart || canJudge;
        prevButton.active = state != ModCastingTableEntity.CastingState.FORGING;
        nextButton.active = state != ModCastingTableEntity.CastingState.FORGING;

        if (state == ModCastingTableEntity.CastingState.FORGING) {
            minigame.tick();
        }
    }

    @Override
    public void handledScreenTick() {
        int tIdx = handler.getProperty(0);
        if (tIdx != lastTemplateIndex) {
            lastTemplateIndex = tIdx;
            // 模板换了 → 由模板决定哪些槽存在、放在哪
            handler.refreshTemplateLayout();
        }

        int sOrd = getStateOrdinal();
        if (sOrd != lastStateOrdinal) {
            lastStateOrdinal = sOrd;
            if (sOrd == ModCastingTableEntity.CastingState.FORGING.ordinal()) {
                minigame.startNewCycle();
            }
        }

        refreshButtonStates();
        super.handledScreenTick();
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        this.renderBackground(ctx, mouseX, mouseY, delta);
        super.render(ctx, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(ctx, mouseX, mouseY);
        renderTemplateTooltip(ctx, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext ctx, float delta, int mouseX, int mouseY) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        ctx.drawTexture(RenderPipelines.GUI_TEXTURED, TEX_BG,
                x, y, 0, 0, BG_WIDTH, BG_HEIGHT, TEX_SIZE, TEX_SIZE);

        // 铸造槽位置随模板变化，槽位框在这里按模板坐标画出来
        renderCastingSlotFrames(ctx, x, y);
        renderTemplateDisplay(ctx, x, y);
        renderUses(ctx, x, y);
        renderProgress(ctx, x, y);

        // 小游戏
        int sOrd = getStateOrdinal();
        if (sOrd == ModCastingTableEntity.CastingState.FORGING.ordinal()) {
            renderMinigame(ctx, x, y);
        }
    }

    /**
     * 铸造槽的槽位框：从 ScreenHandler 的 16 个铸造槽取绘制坐标与贴图。
     * Slot 的 x/y 已含 1 像素偏移（SLOT_DRAW_OFFSET），绘制时减回去。
     */
    private void renderCastingSlotFrames(DrawContext ctx, int x, int y) {
        int offset = ModCastingTableScreenHandler.SLOT_DRAW_OFFSET;
        for (Slot slot : handler.slots.subList(0, ModCastingTableEntity.CASTING_SLOTS)) {
            if (!(slot instanceof ModCastingSlot castingSlot)) {
                continue;
            }
            Identifier texture = castingSlot.backgroundTexture();
            if (texture == null) {
                continue;
            }
            ctx.drawTexture(RenderPipelines.GUI_TEXTURED, texture,
                    x + slot.x - offset, y + slot.y - offset, 0, 0, SLOT_SIZE, SLOT_SIZE, SLOT_SIZE, SLOT_SIZE);
        }
    }

    /** 模板展示位：显示当前模板的产物 */
    private void renderTemplateDisplay(DrawContext ctx, int x, int y) {
        CastingTemplate tpl = ModCastingTemplates.byIndex(handler.getProperty(0));
        ItemStack icon = new ItemStack(tpl.output(), tpl.outputCount());
        ctx.drawItem(icon, x + TEMPLATE_X + 1, y + TEMPLATE_Y + 1);
    }

    /** 悬停模板展示位时显示模板名 */
    private void renderTemplateTooltip(DrawContext ctx, int mouseX, int mouseY) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        boolean hover = mouseX >= x + TEMPLATE_X && mouseX < x + TEMPLATE_X + TEMPLATE_SIZE
                && mouseY >= y + TEMPLATE_Y && mouseY < y + TEMPLATE_Y + TEMPLATE_SIZE;
        if (!hover) {
            return;
        }
        CastingTemplate tpl = ModCastingTemplates.byIndex(handler.getProperty(0));
        ctx.drawTooltip(textRenderer, Text.translatable(TEMPLATE_LANG_PREFIX + tpl.id()), mouseX, mouseY);
    }

    /** 锻造次数（耐久 / 100 向上取整） */
    private void renderUses(DrawContext ctx, int x, int y) {
        int cur = handler.getProperty(3);
        int max = handler.getProperty(4);
        if (max <= 0) {
            return;
        }
        int uses = (int) Math.ceil(cur / 100.0);
        int maxUses = (int) Math.ceil(max / 100.0);
        ctx.drawText(textRenderer, uses + " / " + maxUses, x + USES_X, y + USES_Y, 0xFFFFFF, true);
    }

    /** 进度箭头：按进度裁剪宽度 */
    private void renderProgress(DrawContext ctx, int x, int y) {
        int prog = handler.getProperty(5);
        int total = handler.getProperty(6);
        if (total <= 0) {
            return;
        }
        int w = Math.min(PROGRESS_W, prog * PROGRESS_W / total);
        if (w <= 0) {
            return;
        }
        ctx.drawTexture(RenderPipelines.GUI_TEXTURED, TEX_PROGRESS,
                x + PROGRESS_X, y + PROGRESS_Y, 0, 0, w, PROGRESS_H, PROGRESS_TEX_SIZE, PROGRESS_TEX_SIZE);
    }

    private void renderMinigame(DrawContext ctx, int x, int y) {
        int barX = x + JUDGE_X;
        int barY = y + JUDGE_Y;

        ctx.fill(barX, barY, barX + JUDGE_W, barY + JUDGE_H, 0xFF202020);

        int acc = 0;
        for (GradeRange r : minigame.ranges) {
            int left = barX + acc * JUDGE_W / 100;
            int right = barX + (acc + r.width) * JUDGE_W / 100;
            ctx.fill(left, barY, right, barY + JUDGE_H, colorFor(r.grade));
            acc += r.width;
        }

        // 浮标（限制在判定条范围内，避免越界）
        int fx = barX + minigame.sliderPosition * JUDGE_W / 100 - FLAG_W / 2;
        int fy = barY + (JUDGE_H - FLAG_H) / 2;
        if (fx < barX) {
            fx = barX;
        } else if (fx > barX + JUDGE_W - FLAG_W) {
            fx = barX + JUDGE_W - FLAG_W;
        }
        ctx.drawTexture(RenderPipelines.GUI_TEXTURED, TEX_FLAG, fx, fy, 0, 0, FLAG_W, FLAG_H, 16, 16);

        // 判定后的反馈：只高亮落点所在档（仅该档宽度、用该档颜色，不再整条满铺）
        if (minigame.effectTicks > 0) {
            int segL = barX + minigame.lastSegLo * JUDGE_W / 100;
            int segR = barX + minigame.lastSegHi * JUDGE_W / 100;
            int ey = barY + JUDGE_H - 2;
            ctx.fill(segL, ey, segR, barY + JUDGE_H, colorFor(minigame.lastGrade));
        }
    }

    private static int colorFor(int grade) {
        return switch (grade) {
            case  2 -> 0xFFFFAA00;
            case  1 -> 0xFF55FF55;
            case  0 -> 0xFF808080;
            case -1 -> 0xFFFF5555;
            case -2 -> 0xFFAA0000;
            default -> 0xFF000000;
        };
    }
    // TODO: 玩家技能影响判定
    // TODO: 切换玩家继续打造时，小游戏状态切换
    // TODO: 传说特殊附魔（applyQuality 内）
    // TODO: 品质阈值结合打造时长动态缩放
    private record GradeRange(int grade, int width) {
    }

    private static final class MinigameState {
        static final int CYCLE_TICKS = 100;
        static final int MAX_SLIDER = 100;
        // 判定区基础宽度（%），顺序对应 GRADES = {-2,-1,0,1,2}：
        // 完美(+2) 7%、优良(+1) 14% —— 窄判定区，避免随手一点就高中；
        // 其余 79% 留给普通/较差档。每轮顺序随机打乱，最佳档不会永远在最右侧。
        static final int[] BASE_WIDTH = {30, 27, 22, 14, 7};
        static final int[] GRADES = {-2, -1, 0, 1, 2};

        final Random random = new Random();
        int cycleTick = 0;
        int sliderPosition = 0;
        int direction = 1; // 1=左→右，-1=右→左（每轮随机，避免浮标永远同向）
        List<GradeRange> ranges = new ArrayList<>();
        int lastGrade = 0;
        /** 落点所在档的左右边界（单位：0~100，点击瞬间按当时布局记录，避免换轮重排后错位） */
        int lastSegLo = 0;
        int lastSegHi = 0;
        int effectTicks = 0;

        MinigameState() { startNewCycle(); }

        void startNewCycle() {
            cycleTick = 0;
            sliderPosition = 0;
            direction = random.nextBoolean() ? 1 : -1;
            ranges = generateRanges();
        }

        List<GradeRange> generateRanges() {
            int[] widths = new int[5];
            int total = 0;
            for (int i = 0; i < 5; i++) {
                int jitter = random.nextInt(7) - 3;
                widths[i] = Math.max(1, BASE_WIDTH[i] + jitter);
                total += widths[i];
            }
            // 随机打乱判定区顺序，最佳档(+2)不会永远固定在最右侧
            List<Integer> order = new ArrayList<>(List.of(0, 1, 2, 3, 4));
            Collections.shuffle(order, random);
            List<GradeRange> list = new ArrayList<>(5);
            int acc = 0;
            for (int k = 0; k < 5; k++) {
                int i = order.get(k);
                int w = (k == 4) ? (100 - acc) : Math.max(1, widths[i] * 100 / total);
                list.add(new GradeRange(GRADES[i], w));
                acc += w;
            }
            int sum = 0;
            for (GradeRange r : list) {
                sum += r.width;
            }
            if (sum != 100 && !list.isEmpty()) {
                GradeRange last = list.get(list.size() - 1);
                list.set(list.size() - 1, new GradeRange(last.grade, Math.max(1, last.width + (100 - sum))));
            }
            return list;
        }

        int judge(int pos) {
            int acc = 0;
            for (GradeRange r : ranges) {
                acc += r.width;
                if (pos < acc) {
                    return r.grade;
                }
            }
            return 0;
        }

        void tick() {
            if (effectTicks > 0) {
                effectTicks--;
            }
            cycleTick++;
            if (cycleTick >= CYCLE_TICKS) {
                startNewCycle();
                return;
            }
            sliderPosition = direction > 0
                    ? cycleTick * MAX_SLIDER / CYCLE_TICKS
                    : (CYCLE_TICKS - cycleTick) * MAX_SLIDER / CYCLE_TICKS;
        }

        int click() {
            int grade = judge(sliderPosition);
            lastGrade = grade;
            // 记录落点所在档的边界（按点击瞬间的布局），反馈条据此高亮
            int acc = 0;
            for (GradeRange r : ranges) {
                if (sliderPosition < acc + r.width) {
                    lastSegLo = acc;
                    lastSegHi = acc + r.width;
                    break;
                }
                acc += r.width;
            }
            effectTicks = 20;
            startNewCycle();
            return grade;
        }
    }

}
