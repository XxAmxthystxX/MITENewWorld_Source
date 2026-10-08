package com.mitenewworld.item.metal;
import com.mitenewworld.MITENewWorld;
import com.mitenewworld.screen.tooltip.AlloyIngotTipComponent;
import com.mitenewworld.screen.tooltip.AlloyTooltipHelper;
import com.mitenewworld.item.component.ModDataComponentTypes;
import com.mitenewworld.item.component.ModTooltipData;

import com.mitenewworld.item.component.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 合金材料物品：粒 / 锭 / 块 / 链条。
 *
 * <p>同一类，由 {@link Form} 区分形态。形态决定：
 * <ul>
 *   <li>应用哪个形态共鸣（nugget / block / chain）</li>
 *   <li>命名后缀</li>
 * </ul>
 */
public class AlloyItem extends Item {

    private final Form form;

    public AlloyItem(Form form, Settings settings) {
        super(settings);
        this.form = form;
    }

    public Form form() { return form; }

    // ============================================================
    //  工厂
    // ============================================================

    /** 从原始配比构造一个对应形态的物品 */
    public static ItemStack create(Item item, Form form, Map<String, Float> raw) {
        AlloyComponent comp = AlloyComponent.compute(raw, Set.of());
        if (form.synergy != null) {
            comp = comp.withFormSynergy(form.synergy);
        }
        return buildStack(item, form, comp);
    }

    /** 单一金属 */
    public static ItemStack pure(Item item, Form form, ModAlloyMetals.MetalType metal) {
        return create(item, form, Map.of(metal.id(), 1.0f));
    }

    /** 从已有合金组件派生新形态 */
    public static ItemStack derive(Item item, Form form, AlloyComponent source) {
        AlloyComponent comp = source;
        if (form.synergy != null) {
            comp = comp.withFormSynergy(form.synergy);
        }
        return buildStack(item, form, comp);
    }

    private static ItemStack buildStack(Item item, Form form, AlloyComponent comp) {
        ItemStack stack = new ItemStack(item);
        stack.set(ModDataComponentTypes.ALLOY_COMPONENT, comp);
        stack.set(DataComponentTypes.CUSTOM_NAME, plainStyle(nameFor(comp, form)));
        return stack;
    }

    // ============================================================
    //  命名
    // ============================================================

    private static Text nameFor(AlloyComponent comp, Form form) {
        List<String> display = comp.display();

        if (display.isEmpty()) {
            return Text.translatable(form.baseKey);
        }
        if (display.size() == 1) {
            return Text.translatable(form.singleKey, metalName(display.get(0)));
        }

        MutableText name = Text.empty();
        for (int i = 0; i < display.size(); i++) {
            if (i > 0) {
                name.append(Text.literal("-"));
            }
            name.append(metalName(display.get(i)));
        }
        name.append(Text.translatable(form.suffixKey));
        return name;
    }
    /** 从已有 AlloyComponent 直接构造物品（不重新计算属性） */
    public static ItemStack createFromComponent(Item item, Form form, AlloyComponent comp) {
        ItemStack stack = new ItemStack(item);
        stack.set(ModDataComponentTypes.ALLOY_COMPONENT, comp);
        stack.set(DataComponentTypes.CUSTOM_NAME, plainStyle(nameFor(comp, form)));
        return stack;
    }
    private static Text metalName(String id) {
        return Text.translatable("metal.mitenewworld." + id);
    }
    /** 合金类物品名不加斜体（自定义名默认会被渲染成斜体） */
    private static Text plainStyle(Text name) {
        MutableText copy = name.copy();
        copy.setStyle(copy.getStyle().withItalic(false));
        return copy;
    }
    @Override
    @Environment(EnvType.CLIENT)
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        return tooltipFor(stack);
    }

    @Environment(EnvType.CLIENT)
    public static Optional<TooltipData> tooltipFor(ItemStack stack) {
        AlloyComponent comp = stack.get(ModDataComponentTypes.ALLOY_COMPONENT);
        if (comp == null) {
            return Optional.empty();
        }
        // 配比始终显示；手持玻璃片时追加「当合成为工具时：」属性块
        AlloyIngotTipComponent tip = AlloyIngotTipComponent.of(comp, AlloyTooltipHelper.isHoldingMagnifier());
        return Optional.of(new ModTooltipData(tip));
    }
    // ============================================================
    //  形态枚举
    // ============================================================

    public enum Form {
        NUGGET("nugget",
                "item.mitenewworld.alloy_nugget",
                "item.mitenewworld.alloy_nugget.single",
                "item.mitenewworld.alloy_nugget.suffix"),

        INGOT("ingot",
                "item.mitenewworld.alloy_ingot",
                "item.mitenewworld.alloy_ingot.single",
                "item.mitenewworld.alloy_ingot.suffix"),

        BLOCK("block",
                "item.mitenewworld.alloy_block",
                "item.mitenewworld.alloy_block.single",
                "item.mitenewworld.alloy_block.suffix"),

        CHAIN("chain",
                "item.mitenewworld.alloy_chain",
                "item.mitenewworld.alloy_chain.single",
                "item.mitenewworld.alloy_chain.suffix");

        /** 形态共鸣 id；null 表示基准形态（锭） */
        public final String synergy;
        public final String baseKey;
        public final String singleKey;
        public final String suffixKey;

        Form(String synergy, String baseKey, String singleKey, String suffixKey) {
            this.synergy = synergy;
            this.baseKey = baseKey;
            this.singleKey = singleKey;
            this.suffixKey = suffixKey;
        }
    }
}
