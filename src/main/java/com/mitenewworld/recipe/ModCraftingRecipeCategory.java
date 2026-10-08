package com.mitenewworld.recipe;
import com.mitenewworld.MITENewWorld;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.ValueLists;

import java.util.function.IntFunction;

public enum ModCraftingRecipeCategory implements StringIdentifiable {
    BUILDING_BLOCKS("building_blocks" , 0),
    FUNCTION_BLOCKS("function_blocks" , 1),
    EQUIPMENT("equipment" , 2),
    FOOD("food" , 3),
    RED_STONE("redstone" , 4),
    MISC("misc" , 5),
    ;
    public static final Codec<ModCraftingRecipeCategory> CODEC = StringIdentifiable.createCodec(ModCraftingRecipeCategory::values);
    public static final IntFunction<ModCraftingRecipeCategory> INDEX_TO_VALUE = ValueLists.createIndexToValueFunction(ModCraftingRecipeCategory::getIndex, values(), ValueLists.OutOfBoundsHandling.ZERO);
    public static final PacketCodec<ByteBuf, ModCraftingRecipeCategory> PACKET_CODEC = PacketCodecs.indexed(INDEX_TO_VALUE, ModCraftingRecipeCategory::getIndex);
    private final String id;
    private final int index;

    ModCraftingRecipeCategory(final String id, final int index) {
        this.id = id;
        this.index = index;
    }

    @Override
    public String asString() {
        return this.id;
    }

    public int getIndex() {
        return this.index;
    }
}
