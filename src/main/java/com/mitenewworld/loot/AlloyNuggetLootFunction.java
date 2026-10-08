package com.mitenewworld.loot;

import com.mitenewworld.MITENewWorld;
import com.mitenewworld.registry.ModItems;
import com.mitenewworld.item.metal.AlloyItem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Map;

/**
 * 掉落合金粒：按 metal 参数生成纯金属的粒（含配比、形态共鸣与命名）。
 *
 * <p>用运行时计算而不是把数值写死在 JSON 里，保证掉落物与合成产物完全一致、可堆叠。
 */
public record AlloyNuggetLootFunction(String metal) implements LootFunction {

    public static final MapCodec<AlloyNuggetLootFunction> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.STRING.fieldOf("metal").forGetter(AlloyNuggetLootFunction::metal)
    ).apply(i, AlloyNuggetLootFunction::new));

    public static final LootFunctionType<AlloyNuggetLootFunction> TYPE = new LootFunctionType<>(CODEC);

    @Override
    public LootFunctionType<? extends LootFunction> getType() {
        return TYPE;
    }

    @Override
    public ItemStack apply(ItemStack stack, LootContext context) {
        return AlloyItem.create(ModItems.ALLOY_NUGGET, AlloyItem.Form.NUGGET, Map.of(metal, 1.0f));
    }

    public static void register() {
        Registry.register(Registries.LOOT_FUNCTION_TYPE,
                Identifier.of(MITENewWorld.MOD_ID, "alloy_nugget"), TYPE);
    }
}
