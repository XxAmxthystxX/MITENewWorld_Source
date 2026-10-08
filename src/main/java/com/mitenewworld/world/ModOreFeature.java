package com.mitenewworld.world;

import com.mitenewworld.block.ModOres;
import com.mitenewworld.block.OreBackground;
import com.mitenewworld.block.OreRichness;
import com.mitenewworld.block.OreTraits;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.ArrayList;
import java.util.List;

public class ModOreFeature extends Feature<ModOreConfiguration> {

    public ModOreFeature(Codec<ModOreConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<ModOreConfiguration> context) {
        WorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos origin = context.getOrigin();
        ModOreConfiguration config = context.getConfig();

        OreTraits traits = OreTraits.of(config.metal());
        OreGradeBands.Band band = OreGradeBands.at(config.background(), origin.getY());
        int gradeLevel = Math.clamp(band.rollGrade(random), traits.veinMinGrade(), traits.veinMaxGrade());
        OreRichness richness = VeinGrade.byLevel(gradeLevel).pick(random);

        BlockState ore = ModOres.oreState(config.background(), config.metal(), richness);
        if (ore == null) {
            return false;
        }

        Block replaceable = config.background().replaceable();
        boolean placed = false;
        for (BlockPos pos : veinShape(random, origin, config.size())) {
            if (world.getBlockState(pos).isOf(replaceable)) {
                world.setBlockState(pos, ore, Block.NOTIFY_ALL);
                placed = true;
            }
        }
        return placed;
    }

    private static List<BlockPos> veinShape(Random random, BlockPos origin, int size) {
        List<BlockPos> positions = new ArrayList<>();
        double angle = random.nextDouble() * Math.PI;
        double dirX = Math.cos(angle);
        double dirZ = Math.sin(angle);
        double length = size * 0.5;
        int steps = Math.max(2, size);
        int perStep = Math.max(1, MathHelper.ceil(size * 0.25));
        double centerX = origin.getX() + 0.5;
        double centerY = origin.getY() + 0.5;
        double centerZ = origin.getZ() + 0.5;

        for (int step = 0; step < steps; step++) {
            double t = (double) step / (steps - 1) - 0.5;
            double stepX = centerX + dirX * length * 2.0 * t;
            double stepZ = centerZ + dirZ * length * 2.0 * t;
            for (int i = 0; i < perStep; i++) {
                double offsetX = (random.nextDouble() - 0.5) * size * 0.5;
                double offsetY = (random.nextDouble() - 0.5) * size * 0.4;
                double offsetZ = (random.nextDouble() - 0.5) * size * 0.5;
                positions.add(BlockPos.ofFloored(stepX + offsetX, centerY + offsetY, stepZ + offsetZ));
            }
        }
        return positions;
    }
}
