package com.mitenewworld.item.component;

import com.mitenewworld.MITENewWorld;
import com.mitenewworld.item.metal.AlloyComponent;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {

    public static final ComponentType<AlloyComponent> ALLOY_COMPONENT = register(

                  "alloy_component",
                    builder -> builder
                            .codec(AlloyComponent.CODEC)
                            .packetCodec(AlloyComponent.PACKET_CODEC)
                            .cache()
    );

    public static final ComponentType<OreComponent> ORE_COMPONENT = register(
            "ore_component",
            builder -> builder
                    .codec(OreComponent.CODEC)
                    .packetCodec(OreComponent.PACKET_CODEC)
                    .cache()
    );

    public static final ComponentType<CastingTableComponent> CASTING_TABLE_COMPONENT = register(
            "casting_table",
            builder -> builder
                    .codec(CastingTableComponent.CODEC)
                    .packetCodec(CastingTableComponent.PACKET_CODEC)
                    .cache()
    );

    /**
     * 打造品质分：产物落盘后仍可回读（品质档位本身由 {@code QualityLevel} 从分数映射得到）。
     */
    public static final ComponentType<Integer> QUALITY_SCORE = register(
            "quality_score",
            builder -> builder
                    .codec(Codec.INT)
                    .packetCodec(PacketCodecs.VAR_INT)
    );

    private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(MITENewWorld.MOD_ID, id), (builderOperator.apply(ComponentType.builder())).build());
    }

    public static void registerModDataComponentTypes() {

    }
}
