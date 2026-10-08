package com.mitenewworld.item;
import com.mitenewworld.cover.ItemsCover;
import com.mitenewworld.MITENewWorld;
import com.mitenewworld.registry.ModItems;

import com.mitenewworld.registry.ModBlocks;
import com.mitenewworld.item.component.ModConsumableComponents;
import com.mitenewworld.item.component.ModFoodComponents;
import net.minecraft.item.Item;

public class ModFoodItems {

    public static final Item SALAD = ModItems.register("salad", ModFoodItem::new ,new Item.Settings().food(ModFoodComponents.SALAD, ModConsumableComponents.SALAD).maxCount(4).recipeRemainder(ItemsCover.BOWL));
    public static final Item PORRIDGE = ModItems.register("porridge", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.PORRIDGE, ModConsumableComponents.PORRIDGE).maxCount(4).recipeRemainder(ItemsCover.BOWL));
    public static final Item BOWL_OF_MILK = ModItems.register("bowl_of_milk", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.BOWL_OF_MILK, ModConsumableComponents.BOWL_OF_MILK).maxCount(4).recipeRemainder(ItemsCover.BOWL));
    public static final Item CEREAL = ModItems.register("cereal", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.CEREAL, ModConsumableComponents.CEREAL).maxCount(4).recipeRemainder(ItemsCover.BOWL));
    public static final Item PUMPKIN_SOUP = ModItems.register("pumpkin_soup", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.PUMPKIN_SOUP, ModConsumableComponents.PUMPKIN_SOUP).maxCount(4).recipeRemainder(ItemsCover.BOWL));
    public static final Item CREAM_OF_MUSHROOM_SOUP = ModItems.register("cream_of_mushroom_soup", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.CREAM_OF_MUSHROOM_SOUP, ModConsumableComponents.CREAM_OF_MUSHROOM_SOUP).maxCount(4).recipeRemainder(ItemsCover.BOWL));
    public static final Item VEGETABLE_SOUP = ModItems.register("vegetable_soup", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.VEGETABLE_SOUP, ModConsumableComponents.VEGETABLE_SOUP).maxCount(4).recipeRemainder(ItemsCover.BOWL));
    public static final Item CHICKEN_SOUP = ModItems.register("chicken_soup", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.CHICKEN_SOUP, ModConsumableComponents.CHICKEN_SOUP).maxCount(4).recipeRemainder(ItemsCover.BOWL));
    public static final Item BEEF_STEW = ModItems.register("beef_stew", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.BEEF_STEW, ModConsumableComponents.BEEF_STEW).maxCount(4).recipeRemainder(ItemsCover.BOWL));
    public static final Item SORBET = ModItems.register("sorbet", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.SORBET, ModConsumableComponents.SORBET).maxCount(4).recipeRemainder(ItemsCover.BOWL));
    public static final Item MASHED_POTATO = ModItems.register("mashed_potato", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.MASHED_POTATO, ModConsumableComponents.MASHED_POTATO).maxCount(4).recipeRemainder(ItemsCover.BOWL));
    public static final Item ICE_CREAM = ModItems.register("ice_cream", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.ICE_CREAM, ModConsumableComponents.ICE_CREAM).maxCount(4).recipeRemainder(ItemsCover.BOWL));
    public static final Item BLUE_BERRIES = ModItems.register("blue_berries", (settings) -> new ModFoodItem(ModBlocks.BLUEBERRY_BUSH, settings), new Item.Settings().food(ModFoodComponents.BLUEBERRIES, ModConsumableComponents.BLUEBERRIES).maxCount(16));
    public static final Item CHOCOLATE = ModItems.register("chocolate", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.CHOCOLATE, ModConsumableComponents.CHOCOLATE).maxCount(8));
    public static final Item ONION = ModItems.register("onion", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.ONION, ModConsumableComponents.ONION).maxCount(16));
    public static final Item ORANGE = ModItems.register("orange", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.ORANGE, ModConsumableComponents.ORANGE).maxCount(16));
    public static final Item CHEESE = ModItems.register("cheese", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.CHEESE, ModConsumableComponents.CHEESE).maxCount(8));
    public static final Item DOUGH = ModItems.register("dough", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.DOUGH, ModConsumableComponents.DOUGH).maxCount(16));
    public static final Item BANANA = ModItems.register("banana", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.BANANA, ModConsumableComponents.BANANA).maxCount(16));
    public static final Item MILK_COPPER_BUCKET = ModItems.register("milk_copper_bucket", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.COPPER_BUCKET_OF_MILK, ModConsumableComponents.COPPER_BUCKET_OF_MILK).maxCount(1).recipeRemainder(ModItems.COPPER_BUCKET).useRemainder(ModItems.COPPER_BUCKET));
    public static final Item MILK_SILVER_BUCKET = ModItems.register("milk_silver_bucket", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.SILVER_BUCKET_OF_MILK, ModConsumableComponents.SILVER_BUCKET_OF_MILK).maxCount(1).recipeRemainder(ModItems.SILVER_BUCKET).useRemainder(ModItems.SILVER_BUCKET));
    public static final Item MILK_GOLD_BUCKET = ModItems.register("milk_gold_bucket", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.GOLD_BUCKET_OF_MILK, ModConsumableComponents.GOLD_BUCKET_OF_MILK).maxCount(1).recipeRemainder(ModItems.GOLD_BUCKET).useRemainder(ModItems.GOLD_BUCKET));
    public static final Item MILK_IRON_BUCKET = ModItems.register("milk_iron_bucket", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.IRON_BUCKET_OF_MILK, ModConsumableComponents.IRON_BUCKET_OF_MILK).maxCount(1).recipeRemainder(ModItems.IRON_BUCKET).useRemainder(ModItems.IRON_BUCKET));
    public static final Item MILK_MITHRIL_BUCKET = ModItems.register("milk_mithril_bucket", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.MITHRIL_BUCKET_OF_MILK, ModConsumableComponents.MITHRIL_BUCKET_OF_MILK).maxCount(1).recipeRemainder(ModItems.MITHRIL_BUCKET).useRemainder(ModItems.MITHRIL_BUCKET));
    public static final Item MILK_ANCIENT_METAL_BUCKET = ModItems.register("milk_ancient_metal_bucket", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.ANCIENT_BUCKET_OF_MILK, ModConsumableComponents.ANCIENT_BUCKET_OF_MILK).maxCount(1).recipeRemainder(ModItems.ANCIENT_METAL_BUCKET).useRemainder(ModItems.ANCIENT_METAL_BUCKET));
    public static final Item MILK_ADAMANTIUM_BUCKET = ModItems.register("milk_adamantium_bucket", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.ADAMANTIUM_BUCKET_OF_MILK, ModConsumableComponents.ADAMANTIUM_BUCKET_OF_MILK).maxCount(1).recipeRemainder(ModItems.ADAMANTIUM_BUCKET).useRemainder(ModItems.ADAMANTIUM_BUCKET));
    public static final Item WATER_BOWL = ModItems.register("water_bowl", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.WATER_BOWL, ModConsumableComponents.WATER_BOWL).maxCount(4).recipeRemainder(ItemsCover.BOWL).useRemainder(ItemsCover.BOWL));
    public static final Item HORSE_MEAT = ModItems.register("horse_meat", ModFoodItem::new, new Item.Settings().food(ModFoodComponents.HORSE_MEAT, ModConsumableComponents.HORSE_MEAT).maxCount(16));
    public static void registerModFoodItems() {

    }

}
