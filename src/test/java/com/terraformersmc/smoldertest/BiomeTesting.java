package com.terraformersmc.smoldertest;

import com.terraformersmc.smolder.api.SmolderBiomeRegistry;
import com.terraformersmc.smolder.biome.BiomeDefinition;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class BiomeTesting implements ModInitializer {
	@Override
	public void onInitialize() {
		System.out.println("Test smolder biomes");
		SmolderBiomeRegistry.registerBiome(new Identifier("smoldertest", "test_biome"), new BiomeDefinition().setFogColor(0, 255, 0));
	}
}
