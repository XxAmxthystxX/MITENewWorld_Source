package com.mitenewworld.block;

import net.minecraft.util.math.random.Random;

public enum OreRichness {

    POOR("poor", 0.20f, 0.40f, 1),
    MEDIUM("medium", 0.40f, 0.75f, 2),
    RICH("rich", 0.70f, 1.00f, 3);

    public final String id;
    public final float minPercent;
    public final float maxPercent;
    public final int baseCount;

    OreRichness(String id, float minPercent, float maxPercent, int baseCount) {
        this.id = id;
        this.minPercent = minPercent;
        this.maxPercent = maxPercent;
        this.baseCount = baseCount;
    }

    public float rollPercent(Random random) {
        return minPercent + random.nextFloat() * (maxPercent - minPercent);
    }

    public static OreRichness byId(String id) {
        for (OreRichness richness : values()) {
            if (richness.id.equals(id)) {
                return richness;
            }
        }
        throw new IllegalArgumentException("Unknown ore richness: " + id);
    }
}
