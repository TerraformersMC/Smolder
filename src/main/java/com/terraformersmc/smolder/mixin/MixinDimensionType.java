package com.terraformersmc.smolder.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.terraformersmc.smolder.world.SmolderBiomeSource;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;

@Mixin(DimensionType.class)
public class MixinDimensionType {
	/**
	 * @reason Use Smolder's biome source
	 * @author SuperCoder79 + paulevs
	 */
	@Overwrite
	private static ChunkGenerator createNetherGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
		return new NoiseChunkGenerator(new SmolderBiomeSource(biomeRegistry, seed), seed, () -> {
			return (ChunkGeneratorSettings) chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.NETHER);
		});
	}
}
