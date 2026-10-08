package com.mitenewworld.screen.castingtable;
import com.mitenewworld.screen.ModScreenHandlers;

import com.mitenewworld.entity.blockentity.castingtable.ModCastingTableEntity;
import com.mitenewworld.registry.ModItems;
import com.mitenewworld.recipe.casting.CastingTemplate.ActiveSlot;
import com.mitenewworld.recipe.casting.ModCastingTemplates;
import com.mitenewworld.screen.slot.ModCastingSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class ModCastingTableScreenHandler extends ScreenHandler {

    public static final int BTN_START = 0;
    public static final int BTN_TEMPLATE_PREV = 1;
    public static final int BTN_TEMPLATE_NEXT = 2;

    /**
     * 布局常量是"槽位框的绘制坐标"，实际 {@link Slot} 的 x/y 要整体偏移 1 像素才与绘制对齐。
     */
    static final int SLOT_DRAW_OFFSET = 1;

    /** 未激活铸造槽的屏幕外占位坐标（不渲染、不可交互），见 {@link ModCastingSlot} */

    /** 产物槽（背景图上的大格） */
    static final int OUTPUT_SLOT_X = 138;
    static final int OUTPUT_SLOT_Y = 70;
    /** 铸造锤槽 */
    static final int HAMMER_SLOT_X = 137;
    static final int HAMMER_SLOT_Y = 114;
    /** 玩家背包与快捷栏（与背景图上的槽位框对齐） */
    static final int PLAYER_INV_X = 7;
    static final int PLAYER_INV_Y = 137;
    static final int HOTBAR_Y = 195;

    private final ModCastingTableEntity entity; // 客户端为 null
    private final PropertyDelegate delegate;
    private final BlockPos pos;
    private final Inventory inv;
    private final boolean clientSide;
    /** 已应用的模板下标，用于检测模板变化并重排槽位 */
    private int appliedTemplateIndex = Integer.MIN_VALUE;

    /** 客户端构造：由 ExtendedScreenHandlerType 调用 */
    public ModCastingTableScreenHandler(int syncId, PlayerInventory playerInv, BlockPos pos) {
        this(syncId, playerInv, null, new ArrayPropertyDelegate(ModCastingTableEntity.PROPERTY_COUNT), pos, true);
    }

    /** 服务端构造 */
    public ModCastingTableScreenHandler(int syncId, PlayerInventory playerInv,
                                        ModCastingTableEntity entity,
                                        PropertyDelegate delegate, BlockPos pos) {
        this(syncId, playerInv, entity, delegate, pos, false);
    }

    private ModCastingTableScreenHandler(int syncId, PlayerInventory playerInv,
                                         ModCastingTableEntity entity,
                                         PropertyDelegate delegate,
                                         BlockPos pos, boolean clientSide) {
        super(ModScreenHandlers.CASTING_TABLE_SCREEN_HANDLER, syncId);
        this.entity = entity;
        this.delegate = delegate;
        this.pos = pos;
        this.clientSide = clientSide;
        this.inv = entity != null ? entity : new SimpleInventory(ModCastingTableEntity.INVENTORY_SIZE);

        // 输入槽 0~15：坐标全部由当前模板的 activeSlots 决定，这里先占位，最后统一重排
        for (int i = 0; i < ModCastingTableEntity.CASTING_SLOTS; i++) {
            this.addSlot(ModCastingSlot.inactive(inv, i));
        }
        // 产物槽
        this.addSlot(new Slot(inv, ModCastingTableEntity.OUTPUT_SLOT,
                OUTPUT_SLOT_X + SLOT_DRAW_OFFSET, OUTPUT_SLOT_Y + SLOT_DRAW_OFFSET) {
            @Override public boolean canInsert(ItemStack stack) { return false; }
        });
        // 铸造锤槽：合金铸造锤 / 燧石锻造锤均可
        this.addSlot(new Slot(inv, ModCastingTableEntity.HAMMER_SLOT,
                HAMMER_SLOT_X + SLOT_DRAW_OFFSET, HAMMER_SLOT_Y + SLOT_DRAW_OFFSET) {
            @Override public boolean canInsert(ItemStack stack) {
                return stack.isOf(ModItems.ALLOY_CASTING_HAMMER)
                        || stack.isOf(ModItems.FLINT_FORGING_HAMMER);
            }
        });

        // 玩家背包 3 行 + 快捷栏 1 行（坐标与背景图上的槽位框对齐）
        for (int row = 0; row < 3; row++) {
            int rowY = PLAYER_INV_Y + SLOT_DRAW_OFFSET + row * 18;
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, PLAYER_INV_X + SLOT_DRAW_OFFSET + col * 18, rowY));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInv, col, PLAYER_INV_X + SLOT_DRAW_OFFSET + col * 18, HOTBAR_Y + SLOT_DRAW_OFFSET));
        }

        if (delegate != null) {
            this.addProperties(delegate);
        }

        // 初始布局：按当前模板摆放激活槽
        refreshTemplateLayout();
    }

    public BlockPos pos() { return pos; }
    public int currentTemplateIndex() { return delegate != null ? delegate.get(0) : 0; }

    /** 属性代理读取（屏幕侧用） */
    public int getProperty(int index) {
        return delegate != null ? delegate.get(index) : 0;
    }

    /**
     * 客户端收到 {@code templateIndex} 同步后立刻重排槽位。
     */
    @Override
    public void setProperty(int id, int value) {
        super.setProperty(id, value);
        if (id == 0) {
            refreshTemplateLayout();
        }
    }

    /**
     * 模板变化时重排 16 个铸造槽。
     * 哪些槽存在、放在哪，完全由模板的 {@link ActiveSlot} 决定；未激活槽移出屏幕并禁用。
     */
    public void refreshTemplateLayout() {
        int index = currentTemplateIndex();
        if (index == appliedTemplateIndex) {
            return;
        }
        appliedTemplateIndex = index;

        Map<Integer, ActiveSlot> byIndex = new HashMap<>();
        for (ActiveSlot slot : ModCastingTemplates.byIndex(index).activeSlots()) {
            byIndex.put(slot.index(), slot);
        }
        for (int i = 0; i < ModCastingTableEntity.CASTING_SLOTS; i++) {
            Slot old = this.slots.get(i);
            ActiveSlot active = byIndex.get(i);
            // 模板里存的是绘制坐标，Slot 需要整体偏移 1 像素
            ModCastingSlot fresh = active != null
                    ? new ModCastingSlot(inv, i, active.x() + SLOT_DRAW_OFFSET, active.y() + SLOT_DRAW_OFFSET, active.type(), true)
                    : ModCastingSlot.inactive(inv, i);
            // Slot.x/y 是 final，只能整体替换 Slot 对象，因此要沿用原来的网络 id
            fresh.id = old.id;
            this.slots.set(i, fresh);
        }
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        switch (id) {
            case BTN_START -> { /* 由 C2S 小游戏点击包处理 */ }
            case BTN_TEMPLATE_PREV -> requestTemplateSwitch(player, -1);
            case BTN_TEMPLATE_NEXT -> requestTemplateSwitch(player, +1);
        }
        return true;
    }

    private void requestTemplateSwitch(PlayerEntity player, int delta) {
        if (entity != null && !clientSide) {
            entity.setTemplate(player, entity.templateIndex() + delta);
            refreshTemplateLayout();
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        // 简易版：只处理输出槽快速取出
        Slot s = this.slots.get(slot);
        if (!s.hasStack()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = s.getStack();
        ItemStack copy = stack.copy();

        if (slot == ModCastingTableEntity.CASTING_SLOTS) {
            // 输出槽 → 玩家背包
            if (!this.insertItem(stack, ModCastingTableEntity.CASTING_SLOTS + 2, this.slots.size(), true))
                return ItemStack.EMPTY;
        } else if (slot == ModCastingTableEntity.CASTING_SLOTS + 1) {
            // 锤子槽 → 玩家背包
            if (!this.insertItem(stack, ModCastingTableEntity.CASTING_SLOTS + 2, this.slots.size(), true))
                return ItemStack.EMPTY;
        } else if (slot >= ModCastingTableEntity.CASTING_SLOTS + 2) {
            // 玩家背包 → 锤子槽优先
            if (!this.insertItem(stack, ModCastingTableEntity.CASTING_SLOTS + 1, ModCastingTableEntity.CASTING_SLOTS + 2, false)) {
                if (!this.insertItem(stack, 0, ModCastingTableEntity.CASTING_SLOTS, false)) {
                    return ItemStack.EMPTY;
                }
            }
        } else {
            // 输入槽 → 玩家背包
            if (!this.insertItem(stack, ModCastingTableEntity.CASTING_SLOTS + 2, this.slots.size(), true))
                return ItemStack.EMPTY;
        }

        s.onQuickTransfer(stack, copy);
        if (stack.isEmpty()) {
            s.setStack(ItemStack.EMPTY);
        } else {
            s.markDirty();
        }
        return copy;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        if (entity == null) {
            return true;
        }
        return !entity.getWorld().isClient()
                && player.getEntityWorld().getBlockEntity(pos) == entity;
    }
}
