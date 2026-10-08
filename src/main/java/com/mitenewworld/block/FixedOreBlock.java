package com.mitenewworld.block;

import net.minecraft.item.Item;

public class FixedOreBlock extends ModOreBlock {

    public FixedOreBlock(String metalId, OreRichness richness, Item dropItem, float baseHardness, Settings settings) {
        super(metalId, richness, dropItem, baseHardness, settings);
    }
}
