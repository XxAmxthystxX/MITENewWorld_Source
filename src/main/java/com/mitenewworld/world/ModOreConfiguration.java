package com.mitenewworld.world;

import com.mitenewworld.block.OreBackground;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.FeatureConfig;

import java.util.Locale;

public record ModOreConfiguration(OreBackground background, String metal, int size) implements FeatureConfig {

    public static final Codec<ModOreConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.xmap(
                    id -> OreBackground.valueOf(id.toUpperCase(Locale.ROOT)),
                    background -> background.name().toLowerCase(Locale.ROOT)
            ).fieldOf("background").forGetter(ModOreConfiguration::background),
            Codec.STRING.fieldOf("metal").forGetter(ModOreConfiguration::metal),
            Codec.INT.fieldOf("size").forGetter(ModOreConfiguration::size)
    ).apply(instance, ModOreConfiguration::new));
}
