package com.terraformersmc.smolder;

import com.terraformersmc.smolder.api.SmolderBiomeRegistry;
import com.terraformersmc.smolder.world.SmolderBiomeSource;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome.Category;

public class Smolder implements ModInitializer {
	public static final String MOD_ID = "smolder";
	
	@Override
	public void onInitialize() {
		SmolderBiomeRegistry.init();
		Registry.register(Registry.BIOME_SOURCE, new Identifier(MOD_ID, "smolder_biome_source"), SmolderBiomeSource.CODEC);
		RegistryEntryAddedCallback.event(BuiltinRegistries.BIOME).register((i, id, biome) -> {
			if (biome.getCategory() == Category.NETHER && (id != null && !SmolderBiomeRegistry.lastID.equals(id))) SmolderBiomeRegistry.registerBiome(biome);
		});
		registerExistingBiomes();
	}
	
	/**
	 * Register existing biomes on library startup. Must be called once.
	 */
	private void registerExistingBiomes() {
		BuiltinRegistries.BIOME.forEach((biome) -> {
			if (biome.getCategory() == Category.NETHER && !(SmolderBiomeRegistry.getFromBiome(biome) != null)) {
				SmolderBiomeRegistry.registerBiome(biome);
			}
		});
	}
}