package com.mitenewworld.tags;

import com.mitenewworld.MITENewWorld;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModItemTags {
    public static final TagKey<Item> ADAMANTIUM_TOOL = of("adamantium_tool");
    public static final TagKey<Item> COPPER = of("copper");
    public static final TagKey<Item> SILVER = of("silver");
    public static final TagKey<Item> GOLD = of("gold");
    public static final TagKey<Item> IRON = of("iron");
    public static final TagKey<Item> ANCIENT_METAL = of("ancient_metal");
    public static final TagKey<Item> MITHRIL = of("mithril");
    public static final TagKey<Item> ADAMANTIUM = of("adamantium");

    public static final TagKey<Item> REPAIRS_NONE = of("repairs_none");
    public static final TagKey<Item> REPAIRS_COPPER = of("repairs_copper");
    public static final TagKey<Item> REPAIRS_COPPER_CHAIN = of("repairs_copper_chain");
    public static final TagKey<Item> REPAIRS_SILVER = of("repairs_silver");
    public static final TagKey<Item> REPAIRS_SILVER_CHAIN = of("repairs_silver_chain");
    public static final TagKey<Item> REPAIRS_GOLD = of("repairs_gold");
    public static final TagKey<Item> REPAIRS_GOLD_CHAIN = of("repairs_gold_chain");
    public static final TagKey<Item> REPAIRS_IRON = of("repairs_iron");
    public static final TagKey<Item> REPAIRS_IRON_CHAIN = of("repairs_iron_chain");
    public static final TagKey<Item> REPAIRS_MITHRIL = of("repairs_mithril");
    public static final TagKey<Item> REPAIRS_MITHRIL_CHAIN = of("repairs_mithril_chain");
    public static final TagKey<Item> REPAIRS_ANCIENT_METAL = of("repairs_ancient_metal");
    public static final TagKey<Item> REPAIRS_ANCIENT_METAL_CHAIN = of("repairs_ancient_metal_chain");
    public static final TagKey<Item> REPAIRS_ADAMANTIUM = of("repairs_adamantium");
    public static final TagKey<Item> REPAIRS_ADAMANTIUM_CHAIN = of("repairs_adamantium_chain");

    public static final TagKey<Item> MILK_OF_BUCKET = of("milk_of_bucket");

    private static TagKey<Item> of(String id) {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of(MITENewWorld.MOD_ID,id));
    }
    public static void registerModItemTags(){
        MITENewWorld.LOGGER.info("Register Item Tags");
    }
}


