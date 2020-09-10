package com.terraformersmc.smolder.biome;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.sound.MusicType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.sound.BiomeAdditionsSound;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.Biome.Precipitation;
import net.minecraft.world.biome.BiomeEffects.Builder;
import net.minecraft.world.biome.BiomeParticleConfig;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.carver.ConfiguredCarvers;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

public class BiomeDefinition {
	private static final int ALPHA = 255 << 24;
	
	private List<ConfiguredStructureFeature<?, ?>> structures = Lists.newArrayList();
	private List<FeatureInfo> features = Lists.newArrayList();
	private List<SpawnInfo> mobs = Lists.newArrayList();
	
	private ConfiguredSurfaceBuilder<?> surfaceBuilder = ConfiguredSurfaceBuilders.NETHER;
	
	private BiomeParticleConfig particleConfig;
	private BiomeAdditionsSound additions;
	private BiomeMoodSound mood;
	private SoundEvent music;
	private SoundEvent loop;
	
	private Precipitation precipitation = Precipitation.NONE;
	private float temperature = 2.0F;
	private float downfall = 0.0F;
	
	private Category category = Category.NETHER;
	private float size = SmolderBiomeData.DEF_SIZE;
	private float weight = SmolderBiomeData.DEF_WEIGHT;
	
	private int waterFogColor = 329011;
	private int waterColor = 4159204;
	private int fogColor = 3344392;
	
	private boolean defaultOres = true;
	private boolean defaultMobs = true;
	private boolean defaultFeatures = true;
	private boolean defaultStructureFeatures = true;
	
	public BiomeDefinition() {}
	
	/**
	 * Set biome surface builder
	 * @param surfaceBuilder - {@link ConfiguredSurfaceBuilder}
	 * @return this {@link BiomeDefinition}
	 */
	public BiomeDefinition setSurfaceBuilder(ConfiguredSurfaceBuilder<?> surfaceBuilder) {
		this.surfaceBuilder = surfaceBuilder;
		return this;
	}
	
	/**
	 * Set default ores generation
	 * @param value - if true (default) then default ores will be generated
	 * @return this {@link BiomeDefinition}
	 */
	public BiomeDefinition addDefaultOres(boolean value) {
		defaultOres = value;
		return this;
	}
	
	/**
	 * Set default nether structure features to be added
	 * @param value - if true (default) then default structure features (nether fortresses, caves, etc.) will be added into biome
	 * @return this {@link BiomeDefinition}
	 */
	public BiomeDefinition addDefaultStructureFeatures(boolean value) {
		defaultStructureFeatures = value;
		return this;
	}
	
	/**
	 * Set default nether features to be added
	 * @param value - if true (default) then default features (small structures) will be added into biome
	 * @return this {@link BiomeDefinition}
	 */
	public BiomeDefinition addDefaultFeatures(boolean value) {
		defaultFeatures = value;
		return this;
	}
	
	/**
	 * Set default Nether Wastes mobs to be added
	 * @param value - if true (default) then default mobs will be added into biome
	 * @return this {@link BiomeDefinition}
	 */
	public BiomeDefinition addDefaultMobs(boolean value) {
		defaultMobs = value;
		return this;
	}
	
	/**
	 * Adds mob into biome
	 * @param type - {@link EntityType}
	 * @param group - {@link SpawnGroup}
	 * @param weight - cumulative spawning weight
	 * @param minGroupSize - minimum count of mobs in the group
	 * @param maxGroupSize - maximum count of mobs in the group
	 * @return this {@link BiomeDefinition}
	 */
	public BiomeDefinition addMobSpawn(EntityType<?> type, SpawnGroup group, int weight, int minGroupSize, int maxGroupSize) {
		SpawnInfo info = new SpawnInfo();
		info.type = type;
		info.group = group;
		info.weight = weight;
		info.minGroupSize = minGroupSize;
		info.maxGroupSize = maxGroupSize;
		mobs.add(info);
		return this;
	}
	
	/**
	 * Adds feature (small structure) into biome - plants, ores, small buildings, etc.
	 * @param feature - {@link ConfiguredStructureFeature} to add
	 * @return this {@link BiomeDefinition}
	 */
	public BiomeDefinition addStructureFeature(ConfiguredStructureFeature<?, ?> feature) {
		structures.add(feature);
		return this;
	}
	
	public BiomeDefinition addFeature(Feature featureStep, ConfiguredFeature<?, ?> feature) {
		FeatureInfo info = new FeatureInfo();
		info.featureStep = featureStep;
		info.feature = feature;
		features.add(info);
		return this;
	}
	
	/**
	 * Sets biome fog color
	 * @param r - Red [0 - 255]
	 * @param g - Green [0 - 255]
	 * @param b - Blue [0 - 255]
	 * @return this {@link BiomeDefinition}
	 */
	public BiomeDefinition setFogColor(int r, int g, int b) {
		r = MathHelper.clamp(r, 0, 255);
		g = MathHelper.clamp(g, 0, 255);
		b = MathHelper.clamp(b, 0, 255);
		this.fogColor = color(r, g, b);
		return this;
	}
	
	/**
	 * Sets biome water color
	 * @param r - Red [0 - 255]
	 * @param g - Green [0 - 255]
	 * @param b - Blue [0 - 255]
	 * @return this {@link BiomeDefinition}
	 */
	public BiomeDefinition setWaterColor(int r, int g, int b) {
		r = MathHelper.clamp(r, 0, 255);
		g = MathHelper.clamp(g, 0, 255);
		b = MathHelper.clamp(b, 0, 255);
		this.waterColor = color(r, g, b);
		return this;
	}
	
	/**
	 * Sets biome underwater fog color
	 * @param r - Red [0 - 255]
	 * @param g - Green [0 - 255]
	 * @param b - Blue [0 - 255]
	 * @return this {@link BiomeDefinition}
	 */
	public BiomeDefinition setWaterFogColor(int r, int g, int b) {
		r = MathHelper.clamp(r, 0, 255);
		g = MathHelper.clamp(g, 0, 255);
		b = MathHelper.clamp(b, 0, 255);
		this.waterFogColor = color(r, g, b);
		return this;
	}
	
	/**
	 * Converts 3 color values [0-255] into one integer with 255 alpha
	 */
	private int color(int r, int g, int b) {
		return ALPHA | (r << 16) | (g << 8) | b;
	}
	
	/**
	 * Plays in never-ending loop for as long as player is in the biome
	 * @param loop - SoundEvent
	 * @return this {@link BiomeDefinition}
	 */
	public BiomeDefinition setLoopSound(SoundEvent loop) {
		this.loop = loop;
		return this;
	}
	
	/**
	 * Plays commonly while the player is in the biome
	 * @param mood - SoundEvent
	 * @return this {@link BiomeDefinition}
	 */
	public BiomeDefinition setMoodSound(SoundEvent mood) {
		this.mood = new BiomeMoodSound(mood, 6000, 8, 2.0D);
		return this;
	}
	
	/**
	 * Set additional sounds. They plays once every 6000-17999 ticks while the player is in the biome
	 * @param additions - SoundEvent
	 * @return this BiomeDefenition
	 */
	public BiomeDefinition setAdditionsSounds(SoundEvent additions) {
		this.additions = new BiomeAdditionsSound(additions, 0.0111);
		return this;
	}

	/**
	 * Set background music for biome
	 * @param music
	 * @return
	 */
	public BiomeDefinition setMusic(SoundEvent music) {
		this.music = music;
		return this;
	}

	public BiomeDefinition setPrecipitation(Precipitation precipitation) {
		this.precipitation = precipitation;
		return this;
	}
	
	public BiomeDefinition setTemperature(float temperature) {
		this.temperature = temperature;
		return this;
	}
	
	public BiomeDefinition setDownfall(float downfall) {
		this.downfall = downfall;
		return this;
	}
	
	public BiomeDefinition setCategory(Category category) {
		this.category = category;
		return this;
	}
	
	public BiomeDefinition setSize(float size) {
		this.size = size;
		return this;
	}
	
	public BiomeDefinition setWeight(float weight) {
		this.weight = weight;
		return this;
	}

	public float getSize() {
		return size;
	}

	public float getWeight() {
		return weight;
	}

	public Biome build()
	{
		SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
		GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();
		Builder effects = new Builder();
		
		if (defaultMobs) addDefaultMobs(spawnSettings);
		mobs.forEach((spawn) -> { spawnSettings.spawn(spawn.group, new SpawnSettings.SpawnEntry(spawn.type, spawn.weight, spawn.minGroupSize, spawn.maxGroupSize)); });
		
		generationSettings.surfaceBuilder(surfaceBuilder);
		structures.forEach((structure) -> generationSettings.structureFeature(structure));
		features.forEach((info) -> generationSettings.feature(info.featureStep, info.feature));
		if (defaultOres) DefaultBiomeFeatures.addNetherMineables(generationSettings);
		if (defaultStructureFeatures) addDefaultStructures(generationSettings);
		if (defaultFeatures) addDefaultFeatures(generationSettings);
		
		effects.skyColor(fogColor).waterColor(waterColor).waterFogColor(waterFogColor).fogColor(fogColor);
		if (loop != null) effects.loopSound(loop);
		if (mood != null) effects.moodSound(mood);
		if (additions != null) effects.additionsSound(additions);
		if (particleConfig != null) effects.particleConfig(particleConfig);
		effects.music(MusicType.createIngameMusic(music != null ? music : SoundEvents.MUSIC_NETHER_NETHER_WASTES));
		
		return new Biome.Builder()
				.precipitation(precipitation)
				.category(category)
				.depth(0.1F)
				.scale(0.2F)
				.temperature(temperature)
				.downfall(downfall)
				.effects(effects.build())
				.spawnSettings(spawnSettings.build())
				.generationSettings(generationSettings.build())
				.build();
	}
	
	private void addDefaultStructures(GenerationSettings.Builder generationSettings) {
		generationSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL_NETHER);
		generationSettings.structureFeature(ConfiguredStructureFeatures.FORTRESS);
		generationSettings.structureFeature(ConfiguredStructureFeatures.BASTION_REMNANT);
		generationSettings.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.NETHER_CAVE);
		generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, ConfiguredFeatures.SPRING_LAVA);
	}

	private void addDefaultFeatures(GenerationSettings.Builder generationSettings) {
		generationSettings.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, ConfiguredFeatures.SPRING_OPEN);
		generationSettings.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, ConfiguredFeatures.PATCH_FIRE);
		generationSettings.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, ConfiguredFeatures.PATCH_SOUL_FIRE);
		generationSettings.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, ConfiguredFeatures.GLOWSTONE_EXTRA);
		generationSettings.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, ConfiguredFeatures.GLOWSTONE);
		generationSettings.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, ConfiguredFeatures.BROWN_MUSHROOM_NETHER);
		generationSettings.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, ConfiguredFeatures.RED_MUSHROOM_NETHER);
		generationSettings.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, ConfiguredFeatures.ORE_MAGMA);
		generationSettings.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, ConfiguredFeatures.SPRING_CLOSED);
	}

	private void addDefaultMobs(SpawnSettings.Builder spawnSettings) {
		spawnSettings.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.GHAST, 50, 4, 4));
		spawnSettings.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.ZOMBIFIED_PIGLIN, 100, 4, 4));
		spawnSettings.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.MAGMA_CUBE, 2, 4, 4));
		spawnSettings.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.ENDERMAN, 1, 4, 4));
		spawnSettings.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.PIGLIN, 15, 4, 4));
		spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.STRIDER, 60, 1, 2));
	}
	
	private static final class SpawnInfo {
		SpawnGroup group;
		EntityType<?> type;
		int weight;
		int minGroupSize;
		int maxGroupSize;
	}
	
	private static final class FeatureInfo {
		Feature featureStep;
		ConfiguredFeature<?, ?> feature;
	}
}