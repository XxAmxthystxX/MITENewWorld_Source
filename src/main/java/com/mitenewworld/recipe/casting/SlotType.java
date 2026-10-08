package com.mitenewworld.recipe.casting;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.registry.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

/**
 * 铸造槽类型。
 *
 * 每个类型自带两样"性质"：
 * <ul>
 *   <li>{@link #texture()} — 该槽位在 GUI 里绘制的槽框贴图
 *       （textures/gui/widget/slot_*.png，18x18）；</li>
 *   <li>{@link #accepts(ItemStack)} — 该槽位接受的物品。</li>
 * </ul>
 * 槽位本身由 {@code ModCastingSlot} 管理，性质全部委托到这里。
 */
public enum SlotType {
    /** 合金锭 */
    INGOT("slot_ingot"),
    /** 合金粒 */
    NUGGET("slot_nugget"),
    /** 合金块（暂用通用槽框） */
    BLOCK("slot"),
    /** 合金链条 */
    CHAIN("slot_chain"),
    /** 手柄（木棍） */
    HANDLE("slot_stick"),
    /** 皮革绳（筋腱） */
    SINEW("slot_sinew");

    private final Identifier texture;

    SlotType(String textureName) {
        this.texture = Identifier.of(MITENewWorld.MOD_ID, "textures/gui/widget/" + textureName + ".png");
    }

    /** 该槽位的槽框贴图（18x18） */
    public Identifier texture() {
        return texture;
    }

    /** 该槽位是否接受该物品 */
    public boolean accepts(ItemStack stack) {
        return switch (this) {
            case INGOT -> stack.isOf(ModItems.ALLOY_INGOT);
            case NUGGET -> stack.isOf(ModItems.ALLOY_NUGGET);
            case BLOCK -> stack.isOf(ModItems.ALLOY_BLOCK);
            case CHAIN -> stack.isOf(ModItems.ALLOY_CHAIN);
            case HANDLE -> stack.isOf(Items.STICK);
            case SINEW -> stack.isOf(ModItems.SINEW);
        };
    }

    /** 该槽位的物品是否参与合金属性计算（木棍 / 皮革绳这类辅材不算） */
    public boolean countsAsMetal() {
        return this == INGOT || this == NUGGET || this == BLOCK || this == CHAIN;
    }
}
