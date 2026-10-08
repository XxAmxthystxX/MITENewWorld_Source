package com.mitenewworld.item.component;
import com.mitenewworld.MITENewWorld;

import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.item.consume.UseAction;
import net.minecraft.sound.SoundEvents;

import java.util.List;

public class ModConsumableComponents {
    public static final ConsumableComponent APPLE = ModConsumableComponents.food(2.2f).build();
    public static final ConsumableComponent BAKED_POTATO = ModConsumableComponents.food(2.6f).build();
    public static final ConsumableComponent BEEF = ModConsumableComponents.food(6.8f).build();
    public static final ConsumableComponent BEETROOT = ModConsumableComponents.food(3.2f).build();
    public static final ConsumableComponent BEETROOT_SOUP = ModConsumableComponents.drink(3.0f).build();
    public static final ConsumableComponent BREAD = ModConsumableComponents.food(2.6f).build();
    public static final ConsumableComponent CARROT = ModConsumableComponents.food(2.6f).build();
    public static final ConsumableComponent CHICKEN = ModConsumableComponents.food(6.0f).consumeEffect(new ApplyEffectsConsumeEffect( new StatusEffectInstance(StatusEffects.POISON, 600, 0), 0.8F)).build();
    public static final ConsumableComponent CHORUS_FRUIT = ModConsumableComponents.food(2.0f).build();
    public static final ConsumableComponent COD = ModConsumableComponents.food(4.0f).build();
    public static final ConsumableComponent COOKED_BEEF = ModConsumableComponents.food(4.0f).build();
    public static final ConsumableComponent COOKED_CHICKEN = ModConsumableComponents.food(3.0f).build();
    public static final ConsumableComponent COOKED_COD = ModConsumableComponents.food(2.7f).build();
    public static final ConsumableComponent COOKED_MUTTON = ModConsumableComponents.food(3.3f).build();
    public static final ConsumableComponent COOKED_PORKCHOP = ModConsumableComponents.food(3.7f).build();
    public static final ConsumableComponent COOKED_RABBIT = ModConsumableComponents.food(3.2f).build();
    public static final ConsumableComponent COOKED_SALMON = ModConsumableComponents.food(2.8f).build();
    public static final ConsumableComponent COOKIE = ModConsumableComponents.food(1.0f).build();
    public static final ConsumableComponent DRIED_KELP = ModConsumableComponents.food(0.7f).build();
    public static final ConsumableComponent ENCHANTED_GOLDEN_APPLE = ModConsumableComponents.food(2.0f).build();
    public static final ConsumableComponent GOLDEN_APPLE = ModConsumableComponents.food(2.0f).build();
    public static final ConsumableComponent HONEY_BOTTLE = ModConsumableComponents.drink(1.5f).build();
    public static final ConsumableComponent MELON_SLICE = ModConsumableComponents.food(1.2f).build();
    public static final ConsumableComponent MUSHROOM_STEW = ModConsumableComponents.food(3.0f).build();
    public static final ConsumableComponent MUTTON = ModConsumableComponents.food(5.2f).build();
    public static final ConsumableComponent POISONOUS_POTATO = ModConsumableComponents.food(1.0f).consumeEffect(new ApplyEffectsConsumeEffect(List.of( new StatusEffectInstance(StatusEffects.POISON, 60, 0), new StatusEffectInstance(StatusEffects.NAUSEA, 300, 0), new StatusEffectInstance(StatusEffects.SLOWNESS , 300 ,0), new StatusEffectInstance(StatusEffects.WEAKNESS, 300, 0) ) )).build();
    public static final ConsumableComponent PORKCHOP = ModConsumableComponents.food(5.6f).consumeEffect(new ApplyEffectsConsumeEffect( new StatusEffectInstance(StatusEffects.POISON , 300 , 0) , 0.3f)).build();
    public static final ConsumableComponent POTATO = ModConsumableComponents.food(3.3f).build();
    public static final ConsumableComponent PUFFERFISH = ModConsumableComponents.food(3.0f).consumeEffect(new ApplyEffectsConsumeEffect(List.of( new StatusEffectInstance(StatusEffects.POISON, 120, 10), new StatusEffectInstance(StatusEffects.NAUSEA, 300, 0), new StatusEffectInstance(StatusEffects.SLOWNESS, 30, 0), new StatusEffectInstance(StatusEffects.WEAKNESS, 1200, 0) ) )).build();
    public static final ConsumableComponent PUMPKIN_PIE = ModConsumableComponents.food(3.0f).build();
    public static final ConsumableComponent RABBIT = ModConsumableComponents.food(4.2f).build();
    public static final ConsumableComponent RABBIT_STEW = ModConsumableComponents.food(3.1f).build();
    public static final ConsumableComponent ROTTEN_FLESH = ModConsumableComponents.food(1.2f).consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.POISON, 600, 0) , 0.7f)).build();
    public static final ConsumableComponent SALMON = ModConsumableComponents.food(3.5f).build();
    public static final ConsumableComponent SPIDER_EYE = ModConsumableComponents.food(1.8f).consumeEffect(new ApplyEffectsConsumeEffect(List.of( new StatusEffectInstance(StatusEffects.POISON, 100, 0), new StatusEffectInstance(StatusEffects.NIGHT_VISION, 200, 0) ) )).build();
    @Deprecated
    public static final ConsumableComponent SUSPICIOUS_STEW = null;
    public static final ConsumableComponent SWEET_BERRIES = ModConsumableComponents.food(0.6f).build();
    public static final ConsumableComponent GLOW_BERRIES = ModConsumableComponents.food(0.6f).build();
    public static final ConsumableComponent TROPICAL_FISH = ModConsumableComponents.food(3.0f).build();
    @Deprecated
    public static final ConsumableComponent OMINOUS_BOTTLE = null;
    //ModFoods
    public static final ConsumableComponent WHEAT_SEEDS = ModConsumableComponents.food(0.6f).build();
    public static final ConsumableComponent MELON_SEEDS = ModConsumableComponents.food(0.6f).build();;
    public static final ConsumableComponent PUMPKIN_SEEDS = ModConsumableComponents.food(0.6f).build();;
    public static final ConsumableComponent BEETROOT_SEEDS = ModConsumableComponents.food(0.6f).build();
    public static final ConsumableComponent SUGAR = ModConsumableComponents.food(1.0f).build();
    public static final ConsumableComponent BROWN_MUSHROOM = ModConsumableComponents.food(1.6f).build();;
    public static final ConsumableComponent RED_MUSHROOM = ModConsumableComponents.food(1.6f).consumeEffect(new ApplyEffectsConsumeEffect(List.of( new StatusEffectInstance(StatusEffects.POISON, 120, 10), new StatusEffectInstance(StatusEffects.NAUSEA, 300, 0), new StatusEffectInstance(StatusEffects.SLOWNESS, 30, 0), new StatusEffectInstance(StatusEffects.WEAKNESS, 1200, 0) ) )).build();
    public static final ConsumableComponent HORSE_MEAT = ModConsumableComponents.food(4.2f).build();
    public static final ConsumableComponent SALAD = ModConsumableComponents.food(1.6f).consumeParticles(false).build();
    public static final ConsumableComponent BLUEBERRIES = ModConsumableComponents.food(0.6f).build();
    public static final ConsumableComponent BOWL_OF_MILK = ModConsumableComponents.drink(1.4f).build();
    public static final ConsumableComponent CEREAL = ModConsumableComponents.drink(1.2f).build();
    public static final ConsumableComponent CHOCOLATE = ModConsumableComponents.food(1.2f).build();
    public static final ConsumableComponent PUMPKIN_SOUP = ModConsumableComponents.drink(1.8f).build();
    public static final ConsumableComponent CREAM_OF_MUSHROOM_SOUP = ModConsumableComponents.food(2.0f).consumeParticles(false).build();
    public static final ConsumableComponent ONION = ModConsumableComponents.food(1.2f).build();
    public static final ConsumableComponent CREAM_OF_VEGETABLE_SOUP = ModConsumableComponents.food(2.2f).consumeParticles(false).build();
    public static final ConsumableComponent CHICKEN_SOUP = ModConsumableComponents.food(2.4f).consumeParticles(false).consumeEffect(new ApplyEffectsConsumeEffect( new StatusEffectInstance(StatusEffects.SATURATION , 20 , 0) )).build();
    public static final ConsumableComponent BEEF_STEW = ModConsumableComponents.food(2.8f).consumeParticles(false).consumeEffect(new ApplyEffectsConsumeEffect( new StatusEffectInstance(StatusEffects.SATURATION , 20 , 0) )).build();
    public static final ConsumableComponent ORANGE = ModConsumableComponents.food(1.0f).build();
    public static final ConsumableComponent BANANA = ModConsumableComponents.food(1.0f).build();
    public static final ConsumableComponent SORBET = ModConsumableComponents.food(1.2f).consumeParticles(false).build();
    public static final ConsumableComponent CHEESE = ModConsumableComponents.food(1.5f).build();
    public static final ConsumableComponent MASHED_POTATO = ModConsumableComponents.food(1.5f).consumeParticles(false).build();
    public static final ConsumableComponent ICE_CREAM = ModConsumableComponents.food(1.5f).consumeParticles(false).build();
    public static final ConsumableComponent DOUGH = ModConsumableComponents.food(1.0f).consumeEffect(new ApplyEffectsConsumeEffect( new StatusEffectInstance(StatusEffects.NAUSEA , 20 , 0) )).build();
    public static final ConsumableComponent PORRIDGE = ModConsumableComponents.food(1.8f).consumeParticles(false).build();
    public static final ConsumableComponent VEGETABLE_SOUP = ModConsumableComponents.food(2.2f).consumeParticles(false).build();
    public static final ConsumableComponent COPPER_BUCKET_OF_MILK = ModConsumableComponents.drink(3.0f).build();
    public static final ConsumableComponent SILVER_BUCKET_OF_MILK = ModConsumableComponents.drink(3.0f).build();
    public static final ConsumableComponent GOLD_BUCKET_OF_MILK = ModConsumableComponents.drink(3.0f).build();
    public static final ConsumableComponent IRON_BUCKET_OF_MILK = ModConsumableComponents.drink(3.0f).build();
    public static final ConsumableComponent MITHRIL_BUCKET_OF_MILK = ModConsumableComponents.drink(3.0f).build();
    public static final ConsumableComponent ANCIENT_BUCKET_OF_MILK = ModConsumableComponents.drink(3.0f).build();
    public static final ConsumableComponent ADAMANTIUM_BUCKET_OF_MILK = ModConsumableComponents.drink(3.0f).build();
    public static final ConsumableComponent WATER_BOWL = ModConsumableComponents.drink(3.0f).build();

    public static ConsumableComponent.Builder food(float usetime) {
        return ConsumableComponent.builder().consumeSeconds(usetime).useAction(UseAction.EAT).sound(SoundEvents.ENTITY_GENERIC_EAT).consumeParticles(true);
    }

    public static ConsumableComponent.Builder drink(float usetime) {
        return ConsumableComponent.builder().consumeSeconds(usetime).useAction(UseAction.DRINK).sound(SoundEvents.ENTITY_GENERIC_DRINK).consumeParticles(false);
    }
}
