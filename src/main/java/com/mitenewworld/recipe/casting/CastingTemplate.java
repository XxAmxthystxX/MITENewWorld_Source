package com.mitenewworld.recipe.casting;

import com.mitenewworld.item.metal.AlloyComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;


public final class CastingTemplate {

    /**
     * @param index 铸造槽下标（0~15）
     * @param x     GUI 内 x（相对背景左上角）
     * @param y     GUI 内 y
     * @param type  该槽接受的物品类型
     */
    public record ActiveSlot(int index, int x, int y, SlotType type) {}

    /** 属性计算方法：把输入合金合并为该模版的最终属性（不含品质）。 */
    @FunctionalInterface
    public interface PropertyCalculator {
        AlloyComponent calculate(List<AlloyComponent> inputs);
    }

    /** 物品合成方法：用属性 + 品质分产出产物。 */
    @FunctionalInterface
    public interface CraftFunction {
        ItemStack craft(AlloyComponent properties, List<AlloyComponent> inputs, int qualityScore);
    }

    private final String id;
    private final Item output;
    private final int outputCount;
    private final List<ActiveSlot> activeSlots;
    private final PropertyCalculator propertyCalculator;
    private final CraftFunction craftFunction;

    public CastingTemplate(String id,
                           Item output,
                           int outputCount,
                           List<ActiveSlot> activeSlots,
                           PropertyCalculator propertyCalculator,
                           CraftFunction craftFunction) {
        this.id = id;
        this.output = output;
        this.outputCount = outputCount;
        this.activeSlots = List.copyOf(activeSlots);
        this.propertyCalculator = propertyCalculator;
        this.craftFunction = craftFunction;
    }

    public String id() { return id; }
    public Item output() { return output; }
    public int outputCount() { return outputCount; }
    public List<ActiveSlot> activeSlots() { return activeSlots; }
    public int slotCount() { return activeSlots.size(); }

    /** 调用模版自身的属性计算。 */
    public AlloyComponent calculateProperties(List<AlloyComponent> inputs) {
        return propertyCalculator.calculate(inputs);
    }

    /** 调用模版自身的合成：先算属性，再产出物品。 */
    public ItemStack craft(List<AlloyComponent> inputs, int qualityScore) {
        AlloyComponent properties = calculateProperties(inputs);
        return craftFunction.craft(properties, inputs, qualityScore);
    }
}