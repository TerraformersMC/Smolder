package com.terraformersmc.smolder.world;

import java.util.Collections;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.terraformersmc.smolder.api.SmolderBiomeRegistry;
import com.terraformersmc.smolder.biome.SmolderBiome;
import com.terraformersmc.smolder.generator.BiomeMap;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

public class SmolderBiomeSource extends BiomeSource {

	public static final Codec<SmolderBiomeSource> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter((theEndBiomeSource) -> {
			return theEndBiomeSource.biomeRegistry;
		}), Codec.LONG.fieldOf("seed").stable().forGetter((theEndBiomeSource) -> {
			return theEndBiomeSource.seed;
		})).apply(instance, instance.stable(SmolderBiomeSource::new));
	});
	private BiomeMap map;
	private final long seed;
	private final Registry<Biome> biomeRegistry;

	public SmolderBiomeSource(Registry<Biome> biomeRegistry, long seed) {
		super(Collections.emptyList());
		this.seed = seed;
		this.map = new BiomeMap(seed);
		this.biomeRegistry = biomeRegistry;
	}

	@Override
	public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
		SmolderBiome biome = map.getBiome(biomeX << 2, biomeZ << 2);
		if (biomeX == 0 && biomeZ == 0) {
			map.clearCache();
		}
		return biomeRegistry.getOrThrow(SmolderBiomeRegistry.getBiomeKey(biome));
	}
	
	@Override
	public BiomeSource withSeed(long seed) {
		return new SmolderBiomeSource(biomeRegistry, seed);
	}

	@Override
	protected Codec<? extends BiomeSource> getCodec() {
		return CODEC;
	}
}
