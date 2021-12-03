package sirttas.elementalcraft.world.feature;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.data.worldgen.placement.PlacementUtils;
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
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECFeatures {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + SourceFeature.NAME) public static final Feature<IElementTypeFeatureConfig> SOURCE = null;

	private static ConfiguredFeature<?, ?> crystalOreConfig;
	private static PlacedFeature crystalOrePlaced;
	private static ConfiguredFeature<?, ?> sourceConfig;
	private static PlacedFeature sourcePlaced;
	private static ConfiguredFeature<?, ?> icySourceConfig;
	private static PlacedFeature icySourcePlaced;
	private static ConfiguredFeature<?, ?> jungleSourceConfig;
	private static PlacedFeature jungleSourcePlaced;
	private static ConfiguredFeature<?, ?> mushroomSourceConfig;
	private static PlacedFeature mushroomSourcePlaced;
	private static ConfiguredFeature<?, ?> netherSourceConfig;
	private static PlacedFeature netherSourcePlaced;
	private static ConfiguredFeature<?, ?> wetSourceConfig;
	private static PlacedFeature wetSourcePlaced;
	private static ConfiguredFeature<?, ?> drySourceConfig;
	private static PlacedFeature drySourcePlaced;
	private static ConfiguredFeature<?, ?> endSourceConfig;
	private static PlacedFeature endSourcePlaced;
	private static ConfiguredFeature<?, ?> forestSourceConfig;
	private static PlacedFeature forestSourcePlaced;
	private static ConfiguredFeature<?, ?> hillSourceConfig;
	private static PlacedFeature hillSourcePlaced;
	private static ConfiguredFeature<?, ?> plainSourceConfig;
	private static PlacedFeature plainSourcePlaced;
	private static ConfiguredFeature<?, ?> oceanSourceConfig;
	private static PlacedFeature oceanSourcePlaced;
	private static ConfiguredFeature<?, ?> spawnFireSourceConfig;
	private static PlacedFeature spawnFireSourcePlaced;
	private static ConfiguredFeature<?, ?> spawnWaterSourceConfig;
	private static PlacedFeature spawnWaterSourcePlaced;
	private static ConfiguredFeature<?, ?> spawnEarthSourceConfig;
	private static PlacedFeature spawnEarthSourcePlaced;
	private static ConfiguredFeature<?, ?> spawnAirSourceConfig;
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

		crystalOreConfig = register("crystal_ore", Feature.ORE.configured(new OreConfiguration(oreInertCrystalTargetList, ECConfig.COMMON.inertCrystalSize.get())));
		crystalOrePlaced =  PlacementUtils.register("crystal_ore_middle", crystalOreConfig.placed(OrePlacements.commonOrePlacement(ECConfig.COMMON.inertCrystalCount.get(), HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(ECConfig.COMMON.inertCrystalYMax.get())))));

		sourceConfig = register(SourceFeature.NAME, source.configured(RandomElementTypeFeatureConfig.ALL));
		sourcePlaced = PlacementUtils.register(SourceFeature.NAME, sourceConfig.placed(sourcePlacement(RarityFilter.onAverageOnceEvery(ECConfig.COMMON.randomSourceSpawnChance.get()))));

		icySourceConfig = register(SourceFeature.NAME_ICY, source.configured(RandomElementTypeFeatureConfig.ICY));
		icySourcePlaced = PlacementUtils.register(SourceFeature.NAME_ICY, icySourceConfig.placed(chanceSourcePlacement));
		jungleSourceConfig = register(SourceFeature.NAME_JUNGLE, source.configured(RandomElementTypeFeatureConfig.JUNGLE));
		jungleSourcePlaced = PlacementUtils.register(SourceFeature.NAME_JUNGLE, jungleSourceConfig.placed(chanceSourcePlacement));
		mushroomSourceConfig = register(SourceFeature.NAME_MUSHROOM, source.configured(RandomElementTypeFeatureConfig.ALL));
		mushroomSourcePlaced = PlacementUtils.register(SourceFeature.NAME_MUSHROOM, mushroomSourceConfig.placed(chanceSourcePlacement));
		wetSourceConfig = register(SourceFeature.NAME_WET, source.configured(RandomElementTypeFeatureConfig.WET));
		wetSourcePlaced = PlacementUtils.register(SourceFeature.NAME_WET, wetSourceConfig.placed(chanceSourcePlacement));
		drySourceConfig = register(SourceFeature.NAME_DRY, source.configured(RandomElementTypeFeatureConfig.DRY));
		drySourcePlaced = PlacementUtils.register(SourceFeature.NAME_DRY, drySourceConfig.placed(chanceSourcePlacement));
		endSourceConfig = register(SourceFeature.NAME_END, source.configured(RandomElementTypeFeatureConfig.END));
		endSourcePlaced = PlacementUtils.register(SourceFeature.NAME_END, endSourceConfig.placed(chanceSourcePlacement));
		forestSourceConfig = register(SourceFeature.NAME_FOREST, source.configured(RandomElementTypeFeatureConfig.FOREST));
		forestSourcePlaced = PlacementUtils.register(SourceFeature.NAME_FOREST, forestSourceConfig.placed(chanceSourcePlacement));
		hillSourceConfig = register(SourceFeature.NAME_HILL, source.configured(RandomElementTypeFeatureConfig.HILL));
		hillSourcePlaced = PlacementUtils.register(SourceFeature.NAME_HILL, hillSourceConfig.placed(chanceSourcePlacement));
		plainSourceConfig = register(SourceFeature.NAME_PLAIN, source.configured(RandomElementTypeFeatureConfig.PLAIN));
		plainSourcePlaced = PlacementUtils.register(SourceFeature.NAME_PLAIN, plainSourceConfig.placed(chanceSourcePlacement));
		netherSourceConfig = register(SourceFeature.NAME_NETHER, source.configured(RandomElementTypeFeatureConfig.NETHER));
		netherSourcePlaced = PlacementUtils.register(SourceFeature.NAME_NETHER, netherSourceConfig.placed(chanceSourcePlacement));
		oceanSourceConfig = register(SourceFeature.NAME_OCEAN, source.configured(ElementTypeFeatureConfig.WATER));
		oceanSourcePlaced = PlacementUtils.register(SourceFeature.NAME_OCEAN, oceanSourceConfig.placed(sourcePlacement(RarityFilter.onAverageOnceEvery(ECConfig.COMMON.oceanSourceSpawnChance.get()))));
		spawnFireSourceConfig = register(SourceFeature.NAME_FIRE_SPAWN, source.configured(ElementTypeFeatureConfig.FIRE));
		spawnFireSourcePlaced = PlacementUtils.register(SourceFeature.NAME_FIRE_SPAWN, spawnFireSourceConfig.placed(sourcePlacement));
		spawnWaterSourceConfig = register(SourceFeature.NAME_WATER_SPAWN, source.configured(ElementTypeFeatureConfig.WATER));
		spawnWaterSourcePlaced = PlacementUtils.register(SourceFeature.NAME_WATER_SPAWN, spawnWaterSourceConfig.placed(sourcePlacement));
		spawnEarthSourceConfig = register(SourceFeature.NAME_EARTH_SPAWN, source.configured(ElementTypeFeatureConfig.EARTH));
		spawnEarthSourcePlaced = PlacementUtils.register(SourceFeature.NAME_EARTH_SPAWN, spawnEarthSourceConfig.placed(sourcePlacement));
		spawnAirSourceConfig = register(SourceFeature.NAME_AIR_SPAWN, source.configured(ElementTypeFeatureConfig.AIR));
		spawnAirSourcePlaced = PlacementUtils.register(SourceFeature.NAME_AIR_SPAWN, spawnAirSourceConfig.placed(sourcePlacement));

		sourceAltar = registerStructure(SourceAltarStructure.NAME, ECStructures.SOURCE_ALTAR, RandomElementTypeFeatureConfig.ALL, ECStructures.SOURCE_ALTAR_PIECE_TYPE);
	}

	private static List<PlacementModifier> sourcePlacement(PlacementModifier ... modifiers) {
		 var list = Lists.newArrayList(new SourcePlacement(), InSquarePlacement.spread(), BiomeFilter.biome());

		 if (modifiers != null && modifiers.length > 0) {
			 Stream.of(modifiers).forEach(list::add);
		 }
		 return List.copyOf(list);
	}

	public static void onBiomeLoad(BiomeLoadingEvent event) {
		Biome.BiomeCategory category = event.getCategory();
		boolean sourceSpawn = Boolean.FALSE.equals(ECConfig.COMMON.disableSourceSpawn.get());
		
		if (Boolean.FALSE.equals(ECConfig.COMMON.disableWorldGen.get())) {
			if (sourceSpawn) {
				event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, sourcePlaced);
			}
			if (category != Biome.BiomeCategory.THEEND && category != Biome.BiomeCategory.NETHER) {
				if (Boolean.FALSE.equals(ECConfig.COMMON.disableInertCrystal.get())) {
					event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, crystalOrePlaced);
				}

				if (category != Biome.BiomeCategory.BEACH && category != Biome.BiomeCategory.OCEAN && category != Biome.BiomeCategory.RIVER && category != Biome.BiomeCategory.SWAMP) {
					// TODO add source altars
				}
			}
			if (sourceSpawn) {
				switch (category) {
				case ICY:
					event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, icySourcePlaced);
					break;
				case JUNGLE:
					event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, jungleSourcePlaced);
					break;
				case MUSHROOM:
					event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, mushroomSourcePlaced);
					break;
				case NETHER:
					event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, netherSourcePlaced);
					break;
				case OCEAN:
					event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, oceanSourcePlaced);
					break;
				case EXTREME_HILLS:
					event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, hillSourcePlaced);
					break;
				case PLAINS:
					event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, plainSourcePlaced);
					break;
				case BEACH, RIVER, SWAMP:
					event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, wetSourcePlaced);
					break;
				case TAIGA, FOREST:
					event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, forestSourcePlaced);
					break;
				case MESA, DESERT, SAVANNA:
					event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, drySourcePlaced);
					break;
				case THEEND:
					event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, endSourcePlaced);
					break;
				default:
					break;
				}
			}
		}
	}

	public static void addSpawnSources(ServerLevel world) {
		if (Boolean.FALSE.equals(ECConfig.COMMON.disableSourceSpawn.get())) {
			Random rand = new Random(world.getSeed());
			BlockPos pos = world.getSharedSpawnPos().offset(-100, 0, -100);
	
			addSpawnSource(world, rand, pos.offset(rand.nextInt(100), 0, rand.nextInt(100)), spawnFireSourceConfig);
			addSpawnSource(world, rand, pos.offset(rand.nextInt(100), 0, rand.nextInt(100)), spawnWaterSourceConfig);
			addSpawnSource(world, rand, pos.offset(rand.nextInt(100), 0, rand.nextInt(100)), spawnEarthSourceConfig);
			addSpawnSource(world, rand, pos.offset(rand.nextInt(100), 0, rand.nextInt(100)), spawnAirSourceConfig);
		}
	}

	private static void addSpawnSource(ServerLevel world, Random rand, BlockPos pos, ConfiguredFeature<?, ?> source) {
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
