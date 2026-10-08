package com.mitenewworld.world;

import com.mitenewworld.block.OreBackground;

import java.util.List;
import java.util.Map;

public final class OreGradeBands {

    public record Band(int minY, int maxY, int minGrade, int maxGrade) {

        public int rollGrade(net.minecraft.util.math.random.Random random) {
            if (maxGrade <= minGrade) {
                return minGrade;
            }
            return minGrade + random.nextInt(maxGrade - minGrade + 1);
        }
    }

    private static final Map<OreBackground, List<Band>> BANDS = Map.of(
            OreBackground.OVERWORLD, List.of(
                    new Band(180, 200, 1, 1),
                    new Band(140, 179, 1, 2),
                    new Band(100, 139, 2, 3),
                    new Band(60, 99, 3, 4),
                    new Band(0, 59, 4, 5)),
            OreBackground.UNDERGROUND, List.of(
                    new Band(140, 256, 1, 2),
                    new Band(100, 139, 2, 3),
                    new Band(60, 99, 3, 4),
                    new Band(0, 59, 4, 5)),
            OreBackground.END, List.of(
                    new Band(0, 512, 3, 5)),
            OreBackground.NETHER, List.of(
                    new Band(0, 256, 3, 5))
    );

    private OreGradeBands() {
    }

    public static Band at(OreBackground background, int y) {
        List<Band> bands = BANDS.get(background);
        for (Band band : bands) {
            if (y >= band.minY() && y <= band.maxY()) {
                return band;
            }
        }
        return bands.get(bands.size() - 1);
    }
}
