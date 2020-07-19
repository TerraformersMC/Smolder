package com.terraformers.smolder;

import com.terraformers.smolder.api.SmolderBiomeRegistry;
import com.terraformers.smolder.biome.SmolderBiome;
import com.terraformers.smolder.world.SmolderBiomeSource;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;

public class Smolder implements ModInitializer {

	public static final String MOD_ID = "smolder";
	
	@Override
	public void onInitialize() {
		SmolderBiomeRegistry.init();
		Registry.register(Registry.BIOME_SOURCE, new Identifier(MOD_ID, "smolder_biome_source"), SmolderBiomeSource.CODEC);
		RegistryEntryAddedCallback.event(Registry.BIOME).register((i, id, biome) -> addBiome(biome));
		Registry.BIOME.forEach((biome) -> addBiome(biome));
	}
	
	private void addBiome(Biome biome)
	{
		if (biome.getCategory() == Category.NETHER && !(biome instanceof SmolderBiome)) {
			SmolderBiomeRegistry.registerBiome(biome);
		}
	}
}