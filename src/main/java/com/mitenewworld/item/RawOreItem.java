package com.mitenewworld.item;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.item.component.ModDataComponentTypes;
import com.mitenewworld.item.component.OreComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class RawOreItem extends Item {

    /** 该物品对应的金属 id，由注册时传入 */
    private final String metalId;

    public RawOreItem(String metalId, Settings settings) {
        super(settings);
        this.metalId = metalId;
    }

    public String metalId() { return metalId; }

    /** 掉落时构造 */
    public static ItemStack create(Item item, float percent) {
        ItemStack stack = new ItemStack(item);
        String metal = ((RawOreItem) item).metalId();
        stack.set(ModDataComponentTypes.ORE_COMPONENT, OreComponent.ore(metal, percent));
        return stack;
    }

    @Override
    public Text getName(ItemStack stack) {
        OreComponent ore = stack.get(ModDataComponentTypes.ORE_COMPONENT);
        if (ore == null) {
            return super.getName(stack);
        }

        float avg = ore.average(stack.getCount());
        return Text.empty()
                .append(super.getName(stack))
                .append(Text.literal(String.format(" %.0f%%", avg * 100))
                        .formatted(Formatting.GRAY));
    }
}
