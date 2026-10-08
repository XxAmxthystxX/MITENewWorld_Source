package com.mitenewworld.tags;

import com.mitenewworld.MITENewWorld;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModBlockTags {
    public static final TagKey<Block> NEED_ADAMANTIUM_TOOL = of("need_adamantium_tool");
    public static final TagKey<Block> INCORRECT_FOR_ADAMANTIUM_TOOL = of("incorrect_for_adamantium_tool");
    public static final TagKey<Block> INCORRECT_FOR_COPPER_TOOL = of("incorrect_for_copper_tool");
    public static final TagKey<Block> INCORRECT_FOR_SILVER_TOOL = of("incorrect_for_silver_tool");
    public static final TagKey<Block> INCORRECT_FOR_IRON_TOOL = of("incorrect_for_iron_tool");
    public static final TagKey<Block> INCORRECT_FOR_MITHRIL_TOOL = of("incorrect_for_mithril_tool");
    public static final TagKey<Block> INCORRECT_FOR_FLINT_TOOL = of("incorrect_for_flint_tool");
    public static final TagKey<Block> INCORRECT_FOR_ANCIENT_METAL_TOOL = of("incorrect_for_ancient_metal_tool");
    public static final TagKey<Block> INCORRECT_FOR_GOLD_TOOL = of("incorrect_for_gold_tool");
    public static final TagKey<Block> INCORRECT_FOR_OBSIDIAN_TOOL = of("incorrect_for_obsidian_tool");
    public static final TagKey<Block> INCORRECT_FOR_RUSTED_IRON_TOOL = of("incorrect_for_rusted_iron_tool");
    //
    public static final TagKey<Block> AXE_MINEABLE = of("axe_mineable");
    public static final TagKey<Block> SHOVEL_MINEABLE = of("shovel_mineable");
    public static final TagKey<Block> SWORD_MINEABLE = of("sword_mineable");
    public static final TagKey<Block> PICKAXE_MINEABLE = of("pickaxe_mineable");
    public static final TagKey<Block> SCYTHE_MINEABLE = of("scythe_mineable");
    public static final TagKey<Block> SHEARS_MINEABLE = of("shears_mineable");
    /** 铸造锤用：空 tag，永远不会正确掉落（不能采矿） */
    public static final TagKey<Block> CASTING_HAMMER_MINEABLE = of("casting_hammer_mineable");

    public static final TagKey<Block> UNDERWORLD_ORE_REPLACE = of("underworld_ore_replace");


    private static TagKey<Block> of(String id) {
    return TagKey.of(RegistryKeys.BLOCK, Identifier.of(MITENewWorld.MOD_ID,id));
}
    public static void registerModBlockTags(){
        MITENewWorld.LOGGER.info("Register Block Tags");
    }
}

