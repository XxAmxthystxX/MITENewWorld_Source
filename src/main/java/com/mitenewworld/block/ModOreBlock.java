package com.mitenewworld.block;

import com.mitenewworld.item.RawOreItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class ModOreBlock extends Block {

    private final String metalId;
    private final OreRichness richness;
    private final Item dropItem;
    private final float baseHardness;

    protected ModOreBlock(String metalId, OreRichness richness, Item dropItem, float baseHardness, Settings settings) {
        super(settings);
        this.metalId = metalId;
        this.richness = richness;
        this.dropItem = dropItem;
        this.baseHardness = baseHardness;
    }

    public String metalId() {
        return metalId;
    }

    public OreRichness richness() {
        return richness;
    }

    public Item dropItem() {
        return dropItem;
    }

    public float baseHardness() {
        return baseHardness;
    }

    @Override
    protected void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack tool, boolean dropExperience) {
        super.onStacksDropped(state, world, pos, tool, dropExperience);
        if (dropItem == null) {
            return;
        }

        int fortune = fortuneLevel(world, tool);
        if (dropItem instanceof RawOreItem) {
            float percent = richness.rollPercent(world.getRandom());
            percent += DepthLayer.levelAt(pos.getY()) * 0.01f;
            percent += fortune * 0.10f;
            percent = Math.clamp(percent, 0.001f, 1.0f);
            percent = Math.round(percent * 1000.0f) / 1000.0f;
            Block.dropStack(world, pos, RawOreItem.create(dropItem, percent));
        } else {
            Block.dropStack(world, pos, new ItemStack(dropItem, richness.baseCount + fortune));
        }
    }

    private static int fortuneLevel(World world, ItemStack tool) {
        if (tool.isEmpty()) {
            return 0;
        }
        Registry<Enchantment> registry = world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
        RegistryEntry<Enchantment> fortune = registry.getOptional(Enchantments.FORTUNE).orElse(null);
        if (fortune == null) {
            return 0;
        }
        return EnchantmentHelper.getLevel(fortune, tool);
    }
}
