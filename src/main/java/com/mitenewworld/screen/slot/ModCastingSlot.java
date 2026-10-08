package com.mitenewworld.screen.slot;
import com.mitenewworld.recipe.casting.SlotType;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

/**
 * 铸造台的槽位。
 *
 * 一个槽位有三种"性质"，全部集中在这里管理：
 * <ol>
 *   <li><b>是否激活</b> — 未激活槽位被移出屏幕（不渲染、不可交互），
 *       模板切换时由 ScreenHandler 重建；</li>
 *   <li><b>槽位类型</b> — {@link SlotType}，决定接受什么物品；</li>
 *   <li><b>槽框贴图</b> — 由 {@link SlotType#texture()} 提供，
 *       屏幕侧直接取用，不再统一画一张 slot.png。</li>
 * </ol>
 */
public class ModCastingSlot extends Slot {

    /** 未激活铸造槽的屏幕外占位坐标 */
    public static final int OFF_SCREEN_X = -1000;
    public static final int OFF_SCREEN_Y = -1000;

    private final boolean active;
    private final SlotType type;

    public ModCastingSlot(Inventory inventory, int index, int x, int y, SlotType type, boolean active) {
        super(inventory, index, x, y);
        this.type = type;
        this.active = active;
    }

    /** 未激活占位槽：不渲染、不可交互 */
    public static ModCastingSlot inactive(Inventory inventory, int index) {
        return new ModCastingSlot(inventory, index, OFF_SCREEN_X, OFF_SCREEN_Y, null, false);
    }

    public boolean isActive() {
        return active;
    }

    public SlotType type() {
        return type;
    }

    /** 该槽位的槽框贴图；未激活（或无类型）返回 null，屏幕侧跳过绘制 */
    public Identifier backgroundTexture() {
        return active && type != null ? type.texture() : null;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return active && type != null && type.accepts(stack);
    }
}
