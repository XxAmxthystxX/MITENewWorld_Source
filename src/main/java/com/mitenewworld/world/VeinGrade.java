package com.mitenewworld.world;

import com.mitenewworld.block.OreRichness;
import net.minecraft.util.math.random.Random;

public enum VeinGrade {

    GRADE_1(1, new OreRichness[]{OreRichness.POOR}, new float[]{1.0f}),
    GRADE_2(2, new OreRichness[]{OreRichness.POOR, OreRichness.MEDIUM}, new float[]{0.7f, 0.3f}),
    GRADE_3(3, new OreRichness[]{OreRichness.POOR, OreRichness.MEDIUM}, new float[]{0.3f, 0.7f}),
    GRADE_4(4, new OreRichness[]{OreRichness.MEDIUM, OreRichness.RICH}, new float[]{0.6f, 0.4f}),
    GRADE_5(5, new OreRichness[]{OreRichness.MEDIUM, OreRichness.RICH}, new float[]{0.3f, 0.7f});

    public final int level;

    private final OreRichness[] options;
    private final float[] weights;

    VeinGrade(int level, OreRichness[] options, float[] weights) {
        this.level = level;
        this.options = options;
        this.weights = weights;
    }

    public OreRichness pick(Random random) {
        float roll = random.nextFloat();
        float cumulative = 0.0f;
        for (int i = 0; i < options.length; i++) {
            cumulative += weights[i];
            if (roll < cumulative) {
                return options[i];
            }
        }
        return options[options.length - 1];
    }

    public static VeinGrade byLevel(int level) {
        for (VeinGrade grade : values()) {
            if (grade.level == level) {
                return grade;
            }
        }
        throw new IllegalArgumentException("Unknown vein grade: " + level);
    }
}
