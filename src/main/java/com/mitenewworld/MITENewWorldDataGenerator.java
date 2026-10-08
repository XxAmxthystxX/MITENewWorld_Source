package com.mitenewworld;
import com.mitenewworld.datagen.ModBlockTagsProvider;
import com.mitenewworld.datagen.ModENUSLanProvider;
import com.mitenewworld.datagen.ModItemTagsProvider;
import com.mitenewworld.datagen.ModLootTableProvider;
import com.mitenewworld.datagen.ModModelsProvider;
import com.mitenewworld.datagen.ModRecipesProvider;

import com.mitenewworld.datagen.*;
import com.mitenewworld.world.ModDimensionTypes;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypeRegistrar;
import net.minecraft.world.dimension.DimensionTypes;

public class MITENewWorldDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		/*pack.addProvider(ModModelsProvider::new);
		pack.addProvider(ModLootTableProvider::new);
		pack.addProvider(ModRecipesProvider::new);
		pack.addProvider(ModBlockTagsProvider::new);
		pack.addProvider(ModItemTagsProvider::new);
		pack.addProvider(ModENUSLanProvider::new);*/

	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		DataGeneratorEntrypoint.super.buildRegistry(registryBuilder);
		registryBuilder.addRegistry(RegistryKeys.DIMENSION_TYPE, DimensionTypeRegistrar::bootstrap);
	}
}
