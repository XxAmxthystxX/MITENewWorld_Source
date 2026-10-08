package com.mitenewworld.mixin.worldmixin;
import com.mitenewworld.MITENewWorld;

import com.mitenewworld.cover.BlocksCover;
import com.mitenewworld.registry.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.world.gen.feature.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static net.minecraft.world.gen.feature.OreConfiguredFeatures.*;

/**
 * 已废弃：矿脉的 configured feature 由 datagen 生成的 JSON 决定，运行时不走 bootstrap，
 * 所以这个类改什么都不生效，而且它本来就没注册进 mitenewworld.mixins.json。
 * 保留仅是留档，实际生效的是 OrePlacedFeaturesMixin + 生成的 worldgen JSON。
 */
@Deprecated
@Mixin(OreConfiguredFeatures.class)
public class OreConfiguredFeaturesMixin {
    @Inject(method = "bootstrap", at = @At("HEAD"), cancellable = true)
    private static void FixedBootstrap(Registerable<ConfiguredFeature<?, ?>> featureRegisterable, CallbackInfo ci) {
        ci.cancel();
        RuleTest ruleTest = new TagMatchRuleTest(BlockTags.BASE_STONE_OVERWORLD);
        RuleTest ruleTest2 = new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest ruleTest3 = new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest ruleTest4 = new BlockMatchRuleTest(BlocksCover.NETHERRACK);
        RuleTest ruleTest5 = new TagMatchRuleTest(BlockTags.BASE_STONE_NETHER);
        List<OreFeatureConfig.Target> list = List.of(OreFeatureConfig.createTarget(ruleTest2, ModBlocks.IRON_ORE.getDefaultState()), OreFeatureConfig.createTarget(ruleTest3, ModBlocks.DEEPSLATE_IRON_ORE.getDefaultState()));
        List<OreFeatureConfig.Target> list2 = List.of(OreFeatureConfig.createTarget(ruleTest2, ModBlocks.GOLD_ORE.getDefaultState()), OreFeatureConfig.createTarget(ruleTest3, ModBlocks.DEEPSLATE_GOLD_ORE.getDefaultState()));
        List<OreFeatureConfig.Target> list3 = List.of(OreFeatureConfig.createTarget(ruleTest2, Blocks.DIAMOND_ORE.getDefaultState()), OreFeatureConfig.createTarget(ruleTest3, Blocks.DEEPSLATE_DIAMOND_ORE.getDefaultState()));
        List<OreFeatureConfig.Target> list4 = List.of(OreFeatureConfig.createTarget(ruleTest2, Blocks.LAPIS_ORE.getDefaultState()), OreFeatureConfig.createTarget(ruleTest3, Blocks.DEEPSLATE_LAPIS_ORE.getDefaultState()));
        List<OreFeatureConfig.Target> list5 = List.of(OreFeatureConfig.createTarget(ruleTest2, ModBlocks.COPPER_ORE.getDefaultState()), OreFeatureConfig.createTarget(ruleTest3, ModBlocks.DEEPSLATE_COPPER_ORE.getDefaultState()));
        List<OreFeatureConfig.Target> list6 = List.of(OreFeatureConfig.createTarget(ruleTest2, ModBlocks.COAL_ORE.getDefaultState()), OreFeatureConfig.createTarget(ruleTest3, ModBlocks.DEEPSLATE_COAL_ORE.getDefaultState()));


        ConfiguredFeatures.register(featureRegisterable, ORE_MAGMA, Feature.ORE, new OreFeatureConfig(ruleTest4, BlocksCover.MAGMA_BLOCK.getDefaultState(), 33));
        ConfiguredFeatures.register(featureRegisterable, ORE_SOUL_SAND, Feature.ORE, new OreFeatureConfig(ruleTest4, BlocksCover.SOUL_SAND.getDefaultState(), 12));
        ConfiguredFeatures.register(featureRegisterable, ORE_NETHER_GOLD, Feature.ORE, new OreFeatureConfig(ruleTest4, Blocks.NETHER_GOLD_ORE.getDefaultState(), 10));
        ConfiguredFeatures.register(featureRegisterable, ORE_QUARTZ, Feature.ORE, new OreFeatureConfig(ruleTest4, Blocks.NETHER_QUARTZ_ORE.getDefaultState(), 14));
        ConfiguredFeatures.register(featureRegisterable, ORE_GRAVEL_NETHER, Feature.ORE, new OreFeatureConfig(ruleTest4, BlocksCover.GRAVEL.getDefaultState(), 33));
        ConfiguredFeatures.register(featureRegisterable, ORE_BLACKSTONE, Feature.ORE, new OreFeatureConfig(ruleTest4, BlocksCover.BLACKSTONE.getDefaultState(), 33));
        ConfiguredFeatures.register(featureRegisterable, ORE_DIRT, Feature.ORE, new OreFeatureConfig(ruleTest, BlocksCover.DIRT.getDefaultState(), 33));
        ConfiguredFeatures.register(featureRegisterable, ORE_GRAVEL, Feature.ORE, new OreFeatureConfig(ruleTest, BlocksCover.GRAVEL.getDefaultState(), 15));
        ConfiguredFeatures.register(featureRegisterable, ORE_GRANITE, Feature.ORE, new OreFeatureConfig(ruleTest, BlocksCover.GRANITE.getDefaultState(), 64));
        ConfiguredFeatures.register(featureRegisterable, ORE_DIORITE, Feature.ORE, new OreFeatureConfig(ruleTest, BlocksCover.DIORITE.getDefaultState(), 64));
        ConfiguredFeatures.register(featureRegisterable, ORE_ANDESITE, Feature.ORE, new OreFeatureConfig(ruleTest, BlocksCover.ANDESITE.getDefaultState(), 64));
        ConfiguredFeatures.register(featureRegisterable, ORE_TUFF, Feature.ORE, new OreFeatureConfig(ruleTest, BlocksCover.TUFF.getDefaultState(), 64));
        ConfiguredFeatures.register(featureRegisterable, ORE_COAL, Feature.ORE, new OreFeatureConfig(list6, 10));
        ConfiguredFeatures.register(featureRegisterable, ORE_COAL_BURIED, Feature.ORE, new OreFeatureConfig(list6, 17, 0.5F));
        ConfiguredFeatures.register(featureRegisterable, ORE_IRON, Feature.ORE, new OreFeatureConfig(list, 7));
        ConfiguredFeatures.register(featureRegisterable, ORE_IRON_SMALL, Feature.ORE, new OreFeatureConfig(list, 4));
        ConfiguredFeatures.register(featureRegisterable, ORE_GOLD, Feature.ORE, new OreFeatureConfig(list2, 3));
        ConfiguredFeatures.register(featureRegisterable, ORE_GOLD_BURIED, Feature.ORE, new OreFeatureConfig(list2, 9, 0.5F));
        ConfiguredFeatures.register(featureRegisterable, ORE_REDSTONE, Feature.ORE, new OreFeatureConfig( List.of( OreFeatureConfig.createTarget(ruleTest2, Blocks.REDSTONE_ORE.getDefaultState()), OreFeatureConfig.createTarget(ruleTest3, Blocks.DEEPSLATE_REDSTONE_ORE.getDefaultState()) ), 8 ));
        ConfiguredFeatures.register(featureRegisterable, ORE_DIAMOND_SMALL, Feature.ORE, new OreFeatureConfig(list3, 4, 0.5F));
        ConfiguredFeatures.register(featureRegisterable, ORE_DIAMOND_LARGE, Feature.ORE, new OreFeatureConfig(list3, 12, 0.7F));
        ConfiguredFeatures.register(featureRegisterable, ORE_DIAMOND_BURIED, Feature.ORE, new OreFeatureConfig(list3, 8, 1.0F));
        ConfiguredFeatures.register(featureRegisterable, ORE_DIAMOND_MEDIUM, Feature.ORE, new OreFeatureConfig(list3, 8, 0.5F));
        ConfiguredFeatures.register(featureRegisterable, ORE_LAPIS, Feature.ORE, new OreFeatureConfig(list4, 7));
        ConfiguredFeatures.register(featureRegisterable, ORE_LAPIS_BURIED, Feature.ORE, new OreFeatureConfig(list4, 7, 1.0F));
        ConfiguredFeatures.register(featureRegisterable, ORE_INFESTED, Feature.ORE, new OreFeatureConfig( List.of( OreFeatureConfig.createTarget(ruleTest2, BlocksCover.INFESTED_STONE.getDefaultState()), OreFeatureConfig.createTarget(ruleTest3, BlocksCover.INFESTED_DEEPSLATE.getDefaultState()) ), 6 ));
        ConfiguredFeatures.register(featureRegisterable, ORE_EMERALD, Feature.ORE, new OreFeatureConfig( List.of( OreFeatureConfig.createTarget(ruleTest2, Blocks.EMERALD_ORE.getDefaultState()), OreFeatureConfig.createTarget(ruleTest3, Blocks.DEEPSLATE_EMERALD_ORE.getDefaultState()) ), 3 ));
        ConfiguredFeatures.register(featureRegisterable, ORE_ANCIENT_DEBRIS_LARGE, Feature.SCATTERED_ORE, new OreFeatureConfig(ruleTest5, Blocks.ANCIENT_DEBRIS.getDefaultState(), 3, 1.0F));
        ConfiguredFeatures.register(featureRegisterable, ORE_ANCIENT_DEBRIS_SMALL, Feature.SCATTERED_ORE, new OreFeatureConfig(ruleTest5, Blocks.ANCIENT_DEBRIS.getDefaultState(), 2, 1.0F));
        ConfiguredFeatures.register(featureRegisterable, ORE_COPPER_SMALL, Feature.ORE, new OreFeatureConfig(list5, 5));
        ConfiguredFeatures.register(featureRegisterable, ORE_COPPER_LARGE, Feature.ORE, new OreFeatureConfig(list5, 8));
        ConfiguredFeatures.register(featureRegisterable, ORE_CLAY, Feature.ORE, new OreFeatureConfig(ruleTest, BlocksCover.CLAY.getDefaultState(), 16));


    }
}
