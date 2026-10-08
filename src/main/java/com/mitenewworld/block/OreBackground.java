package com.mitenewworld.block;

import com.mitenewworld.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public enum OreBackground {

    OVERWORLD("", 0),
    UNDERGROUND("underground_", 1),
    END("end_", 2),
    NETHER("nether_", 3);

    private final String idPrefix;
    private final int modelIndex;

    OreBackground(String idPrefix, int modelIndex) {
        this.idPrefix = idPrefix;
        this.modelIndex = modelIndex;
    }

    public String idPrefix() {
        return idPrefix;
    }

    public int modelIndex() {
        return modelIndex;
    }

    public Block replaceable() {
        return switch (this) {
            case OVERWORLD -> ModOres.MOD_STONE;
            case UNDERGROUND -> ModBlocks.UNDERWORLD_STONE;
            case END -> Blocks.END_STONE;
            case NETHER -> Blocks.BLACKSTONE;
        };
    }
}
