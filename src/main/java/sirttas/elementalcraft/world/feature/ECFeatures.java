package sirttas.elementalcraft.world.feature;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.registry.RegistryHelper;
import sirttas.elementalcraft.world.feature.config.ElementTypeFeatureConfig;
import sirttas.elementalcraft.world.feature.config.IElementTypeFeatureConfig;
import sirttas.elementalcraft.world.feature.config.RandomElementTypeFeatureConfig;
import sirttas.elementalcraft.world.feature.placement.SourcePlacement;
import sirttas.elementalcraft.world.feature.structure.ECStructures;
import sirttas.elementalcraft.world.feature.structure.SourceAltarStructure;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECFeatures {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + SourceFeature.NAME) public static final Feature<IElementTypeFeatureConfig> SOURCE = null;

	private static PlacedFeature crystalOrePlaced;
	private static PlacedFeature sourcePlaced;
	private static PlacedFeature icySourcePlaced;
	private static PlacedFeature jungleSourcePlaced;
	private static PlacedFeature mushroomSourcePlaced;
	private static PlacedFeature netherSourcePlaced;
	private static PlacedFeature wetSourcePlaced;
	private static PlacedFeature drySourcePlaced;
	private static PlacedFeature endSourcePlaced;
	private static PlacedFeature forestSourcePlaced;
	private static PlacedFeature hillSourcePlaced;
	private static PlacedFeature plainSourcePlaced;
	private static PlacedFeature oceanSourcePlaced;
	private static PlacedFeature spawnFireSourcePlaced;
	private static PlacedFeature spawnWaterSourcePlaced;
	private static PlacedFeature spawnEarthSourcePlaced;
	private static PlacedFeature spawnAirSourcePlaced;
	private static ConfiguredStructureFeature<?, ?> sourceAltar;

	private ECFeatures() {}
	
	@SubscribeEvent
	public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
		IForgeRegistry<Feature<?>> r = event.getRegistry();
		Feature<IElementTypeFeatureConfig> source = new SourceFeature();
		var oreInertCrystalTargetList = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ECBlocks.CRYSTAL_ORE.defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ECBlocks.CRYSTAL_ORE.defaultBlockState()));
		var sourcePlacement = sourcePlacement();
		var chanceSourcePlacement = sourcePlacement(RarityFilter.onAverageOnceEvery(ECConfig.COMMON.sourceSpawnChance.get()));
		
		RegistryHelper.register(r, source, SourceFeature.NAME);

		ConfiguredFeature<?, ?> crystalOreConfig = register("crystal_ore", Feature.ORE.configured(new OreConfiguration(oreInertCrystalTargetList, ECConfig.COMMON.inertCrystalSize.get())));
		crystalOrePlaced =  PlacementUtils.register("crystal_ore_middle", crystalOreConfig.placed(OrePlacements.commonOrePlacement(ECConfig.COMMON.inertCrystalCount.get(), HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(ECConfig.COMMON.inertCrystalYMax.get())))));

		ConfiguredFeature<?, ?> sourceConfig = register(SourceFeature.NAME, source.configured(RandomElementTypeFeatureConfig.ALL));
		sourcePlaced = PlacementUtils.register(SourceFeature.NAME, sourceConfig.placed(sourcePlacement(RarityFilter.onAverageOnceEvery(ECConfig.COMMON.randomSourceSpawnChance.get()))));

		ConfiguredFeature<?, ?> icySourceConfig = register(SourceFeature.NAME_ICY, source.configured(RandomElementTypeFeatureConfig.ICY));
		icySourcePlaced = PlacementUtils.register(SourceFeature.NAME_ICY, icySourceConfig.placed(chanceSourcePlacement));
		ConfiguredFeature<?, ?> jungleSourceConfig = register(SourceFeature.NAME_JUNGLE, source.configured(RandomElementTypeFeatureConfig.JUNGLE));
		jungleSourcePlaced = PlacementUtils.register(SourceFeature.NAME_JUNGLE, jungleSourceConfig.placed(chanceSourcePlacement));
		ConfiguredFeature<?, ?> mushroomSourceConfig = register(SourceFeature.NAME_MUSHROOM, source.configured(RandomElementTypeFeatureConfig.ALL));
		mushroomSourcePlaced = PlacementUtils.register(SourceFeature.NAME_MUSHROOM, mushroomSourceConfig.placed(chanceSourcePlacement));
		ConfiguredFeature<?, ?> wetSourceConfig = register(SourceFeature.NAME_WET, source.configured(RandomElementTypeFeatureConfig.WET));
		wetSourcePlaced = PlacementUtils.register(SourceFeature.NAME_WET, wetSourceConfig.placed(chanceSourcePlacement));
		ConfiguredFeature<?, ?> drySourceConfig = register(SourceFeature.NAME_DRY, source.configured(RandomElementTypeFeatureConfig.DRY));
		drySourcePlaced = PlacementUtils.register(SourceFeature.NAME_DRY, drySourceConfig.placed(chanceSourcePlacement));
		ConfiguredFeature<?, ?> endSourceConfig = register(SourceFeature.NAME_END, source.configured(RandomElementTypeFeatureConfig.END));
		endSourcePlaced = PlacementUtils.register(SourceFeature.NAME_END, endSourceConfig.placed(chanceSourcePlacement));
		ConfiguredFeature<?, ?> forestSourceConfig = register(SourceFeature.NAME_FOREST, source.configured(RandomElementTypeFeatureConfig.FOREST));
		forestSourcePlaced = PlacementUtils.register(SourceFeature.NAME_FOREST, forestSourceConfig.placed(chanceSourcePlacement));
		ConfiguredFeature<?, ?> hillSourceConfig = register(SourceFeature.NAME_HILL, source.configured(RandomElementTypeFeatureConfig.HILL));
		hillSourcePlaced = PlacementUtils.register(SourceFeature.NAME_HILL, hillSourceConfig.placed(chanceSourcePlacement));
		ConfiguredFeature<?, ?> plainSourceConfig = register(SourceFeature.NAME_PLAIN, source.configured(RandomElementTypeFeatureConfig.PLAIN));
		plainSourcePlaced = PlacementUtils.register(SourceFeature.NAME_PLAIN, plainSourceConfig.placed(chanceSourcePlacement));
		ConfiguredFeature<?, ?> netherSourceConfig = register(SourceFeature.NAME_NETHER, source.configured(RandomElementTypeFeatureConfig.NETHER));
		netherSourcePlaced = PlacementUtils.register(SourceFeature.NAME_NETHER, netherSourceConfig.placed(chanceSourcePlacement));
		ConfiguredFeature<?, ?> oceanSourceConfig = register(SourceFeature.NAME_OCEAN, source.configured(ElementTypeFeatureConfig.WATER));
		oceanSourcePlaced = PlacementUtils.register(SourceFeature.NAME_OCEAN, oceanSourceConfig.placed(sourcePlacement(RarityFilter.onAverageOnceEvery(ECConfig.COMMON.oceanSourceSpawnChance.get()))));
		ConfiguredFeature<?, ?> spawnFireSourceConfig = register(SourceFeature.NAME_FIRE_SPAWN, source.configured(ElementTypeFeatureConfig.FIRE));
		spawnFireSourcePlaced = PlacementUtils.register(SourceFeature.NAME_FIRE_SPAWN, spawnFireSourceConfig.placed(sourcePlacement));
		ConfiguredFeature<?, ?> spawnWaterSourceConfig = register(SourceFeature.NAME_WATER_SPAWN, source.configured(ElementTypeFeatureConfig.WATER));
		spawnWaterSourcePlaced = PlacementUtils.register(SourceFeature.NAME_WATER_SPAWN, spawnWaterSourceConfig.placed(sourcePlacement));
		ConfiguredFeature<?, ?> spawnEarthSourceConfig = register(SourceFeature.NAME_EARTH_SPAWN, source.configured(ElementTypeFeatureConfig.EARTH));
		spawnEarthSourcePlaced = PlacementUtils.register(SourceFeature.NAME_EARTH_SPAWN, spawnEarthSourceConfig.placed(sourcePlacement));
		ConfiguredFeature<?, ?> spawnAirSourceConfig = register(SourceFeature.NAME_AIR_SPAWN, source.configured(ElementTypeFeatureConfig.AIR));
		spawnAirSourcePlaced = PlacementUtils.register(SourceFeature.NAME_AIR_SPAWN, spawnAirSourceConfig.placed(sourcePlacement));

		sourceAltar = registerStructure(SourceAltarStructure.NAME, ECStructures.SOURCE_ALTAR, RandomElementTypeFeatureConfig.ALL, ECStructures.SOURCE_ALTAR_PIECE_TYPE);
	}

	private static List<PlacementModifier> sourcePlacement(PlacementModifier ... modifiers) {
		 var list = Lists.newArrayList(new SourcePlacement(), InSquarePlacement.spread(), BiomeFilter.biome());

		 if (modifiers != null && modifiers.length > 0) {
			 list.addAll(Arrays.asList(modifiers));
		 }
		 return List.copyOf(list);
	}

	public static void onBiomeLoad(BiomeLoadingEvent event) {
		if (Boolean.FALSE.equals(ECConfig.COMMON.disableWorldGen.get())) {
			var generation = event.getGeneration();
			Biome.BiomeCategory category = event.getCategory();
			boolean sourceSpawn = Boolean.FALSE.equals(ECConfig.COMMON.disableSourceSpawn.get());

			if (Boolean.FALSE.equals(ECConfig.COMMON.disableInertCrystal.get()) && category != Biome.BiomeCategory.THEEND && category != Biome.BiomeCategory.NETHER) {
				generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, crystalOrePlaced);
			}
			if (sourceSpawn) {
				generation.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, sourcePlaced);
				switch (category) {
					case ICY -> generation.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, icySourcePlaced);
					case JUNGLE -> generation.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, jungleSourcePlaced);
					case MUSHROOM -> generation.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, mushroomSourcePlaced);
					case NETHER -> generation.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, netherSourcePlaced);
					case OCEAN -> generation.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, oceanSourcePlaced);
					case EXTREME_HILLS -> generation.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, hillSourcePlaced);
					case PLAINS -> generation.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, plainSourcePlaced);
					case BEACH, RIVER, SWAMP -> generation.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, wetSourcePlaced);
					case TAIGA, FOREST -> generation.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, forestSourcePlaced);
					case MESA, DESERT, SAVANNA -> generation.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, drySourcePlaced);
					case THEEND -> generation.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, endSourcePlaced);
				}
			}
		}
	}

	public static void registerStructures(BiConsumer<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> consumer) {
		BiConsumer<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> consumerWrapper = (feature, key) -> {
			if (feature != null) {
				consumer.accept(feature, key);
			}
		};

		for (var entry : BuiltinRegistries.BIOME.entrySet()) {
			var key = entry.getKey();
			var category = entry.getValue().getBiomeCategory();

			if (category != Biome.BiomeCategory.THEEND
					&& category != Biome.BiomeCategory.NETHER
					&& category != Biome.BiomeCategory.BEACH
					&& category != Biome.BiomeCategory.OCEAN
					&& category != Biome.BiomeCategory.RIVER
					&& category != Biome.BiomeCategory.SWAMP) {
				consumerWrapper.accept(sourceAltar, key);
			}
		}
	}

	public static void addSpawnSources(ServerLevel world) {
		if (Boolean.FALSE.equals(ECConfig.COMMON.disableSourceSpawn.get())) {
			Random rand = new Random(world.getSeed());
			BlockPos pos = world.getSharedSpawnPos().offset(-100, 0, -100);
	
			addSpawnSource(world, rand, pos.offset(rand.nextInt(100), 0, rand.nextInt(100)), spawnFireSourcePlaced);
			addSpawnSource(world, rand, pos.offset(rand.nextInt(100), 0, rand.nextInt(100)), spawnWaterSourcePlaced);
			addSpawnSource(world, rand, pos.offset(rand.nextInt(100), 0, rand.nextInt(100)), spawnEarthSourcePlaced);
			addSpawnSource(world, rand, pos.offset(rand.nextInt(100), 0, rand.nextInt(100)), spawnAirSourcePlaced);
		}
	}

	private static void addSpawnSource(ServerLevel world, Random rand, BlockPos pos, PlacedFeature source) {
		ChunkPos chunkPos = new ChunkPos(pos);
		ServerChunkCache chunkProvider = world.getChunkSource();
		
		chunkProvider.getChunk(chunkPos.x, chunkPos.z, true);
		source.place(world, chunkProvider.getGenerator(), rand, pos);
	}

	private static <C extends FeatureConfiguration> ConfiguredFeature<C, ?> register(String name, ConfiguredFeature<C, ?> feature) {
		return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, ElementalCraft.createRL(name), feature);
	}

	private static <C extends FeatureConfiguration> ConfiguredStructureFeature<C, ?> registerStructure(String name, StructureFeature<C> structure, C config, StructurePieceType structurePieceType) {
		ResourceLocation location = ElementalCraft.createRL(name);
		ConfiguredStructureFeature<C, ?> structureFeature = Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, location, new ConfiguredStructureFeature<>(structure, config));
		
		Registry.register(Registry.STRUCTURE_PIECE, location, structurePieceType);
		StructureFeature.STRUCTURES_REGISTRY.put(structure.getFeatureName(), structure);
		return structureFeature;
	}
}
