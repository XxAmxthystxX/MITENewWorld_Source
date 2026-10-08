package com.mitenewworld.render;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.item.component.CastingTableComponent;
import com.mitenewworld.item.component.ModDataComponentTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * 铸造台物品调色：读取 {@link CastingTableComponent} 的金属配比 → 加权平均色。
 */
public record CastingTableTintSource() implements TintSource {

    public static final MapCodec<CastingTableTintSource> CODEC = MapCodec.unit(CastingTableTintSource::new);

    @Override
    public int getTint(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity user) {
        CastingTableComponent comp = stack.get(ModDataComponentTypes.CASTING_TABLE_COMPONENT);
        if (comp == null) {
            return 0xFFFFFFFF;
        }
        return 0xFF000000 | comp.color();
    }

    @Override
    public MapCodec<? extends TintSource> getCodec() {
        return CODEC;
    }
}
