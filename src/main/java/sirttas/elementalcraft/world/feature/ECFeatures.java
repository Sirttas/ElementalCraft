package sirttas.elementalcraft.world.feature;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
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
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.ConfiguredDecorator;
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
import sirttas.elementalcraft.world.feature.placement.ECPlacements;
import sirttas.elementalcraft.world.feature.structure.ECStructures;
import sirttas.elementalcraft.world.feature.structure.SourceAltarStructure;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECFeatures {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + SourceFeature.NAME) public static final Feature<IElementTypeFeatureConfig> SOURCE = null;

	private static ConfiguredFeature<?, ?> crystalOreConfig;
	private static ConfiguredFeature<?, ?> sourceConfig;
	private static ConfiguredFeature<?, ?> icySourceConfig;
	private static ConfiguredFeature<?, ?> jungleSourceConfig;
	private static ConfiguredFeature<?, ?> mushroomSourceConfig;
	private static ConfiguredFeature<?, ?> netherSourceConfig;
	private static ConfiguredFeature<?, ?> wetSourceConfig;
	private static ConfiguredFeature<?, ?> drySourceConfig;
	private static ConfiguredFeature<?, ?> endSourceConfig;
	private static ConfiguredFeature<?, ?> forestSourceConfig;
	private static ConfiguredFeature<?, ?> hillSourceConfig;
	private static ConfiguredFeature<?, ?> plainSourceConfig;
	private static ConfiguredFeature<?, ?> oceanSourceConfig;
	private static ConfiguredFeature<?, ?> spawnFireSourceConfig;
	private static ConfiguredFeature<?, ?> spawnWaterSourceConfig;
	private static ConfiguredFeature<?, ?> spawnEarthSourceConfig;
	private static ConfiguredFeature<?, ?> spawnAirSourceConfig;
	private static ConfiguredStructureFeature<?, ?> sourceAltar;

	private ECFeatures() {}
	
	@SubscribeEvent
	public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
		IForgeRegistry<Feature<?>> r = event.getRegistry();
		Feature<IElementTypeFeatureConfig> source = new SourceFeature();
		ConfiguredDecorator<?> sourcePlacement = ECPlacements.SOURCE.configured(DecoratorConfiguration.NONE).squared();
		ConfiguredDecorator<?> chanceSourcePlacement = sourcePlacement.rarity(ECConfig.COMMON.sourceSpawnChance.get());
		
		RegistryHelper.register(r, source, SourceFeature.NAME);

		crystalOreConfig = register("crystal_ore", Feature.ORE.configured(new OreConfiguration(OreConfiguration.Predicates.NATURAL_STONE, ECBlocks.CRYSTAL_ORE.defaultBlockState(), 
				ECConfig.COMMON.inertCrystalSize.get()))
				.rangeTriangle(VerticalAnchor.aboveBottom(2), VerticalAnchor.absolute(ECConfig.COMMON.inertCrystalYMax.get()))
				.squared().count(ECConfig.COMMON.inertCrystalCount.get()));
		sourceConfig = register(SourceFeature.NAME, source.configured(RandomElementTypeFeatureConfig.ALL).decorated(sourcePlacement.rarity(ECConfig.COMMON.randomSourceSpawnChance.get())));
		icySourceConfig = register(SourceFeature.NAME_ICY, source.configured(RandomElementTypeFeatureConfig.ICY).decorated(chanceSourcePlacement));
		jungleSourceConfig = register(SourceFeature.NAME_JUNGLE, source.configured(RandomElementTypeFeatureConfig.JUNGLE).decorated(chanceSourcePlacement));
		mushroomSourceConfig = register(SourceFeature.NAME_MUSHROOM, source.configured(RandomElementTypeFeatureConfig.ALL).decorated(chanceSourcePlacement));
		wetSourceConfig = register(SourceFeature.NAME_WET, source.configured(RandomElementTypeFeatureConfig.WET).decorated(chanceSourcePlacement));
		drySourceConfig = register(SourceFeature.NAME_DRY, source.configured(RandomElementTypeFeatureConfig.DRY).decorated(chanceSourcePlacement));
		endSourceConfig = register(SourceFeature.NAME_END, source.configured(RandomElementTypeFeatureConfig.END).decorated(chanceSourcePlacement));
		forestSourceConfig = register(SourceFeature.NAME_FOREST, source.configured(RandomElementTypeFeatureConfig.FOREST).decorated(chanceSourcePlacement));
		hillSourceConfig = register(SourceFeature.NAME_HILL, source.configured(RandomElementTypeFeatureConfig.HILL).decorated(chanceSourcePlacement));
		plainSourceConfig = register(SourceFeature.NAME_PLAIN, source.configured(RandomElementTypeFeatureConfig.PLAIN).decorated(chanceSourcePlacement));
		netherSourceConfig = register(SourceFeature.NAME_NETHER, source.configured(RandomElementTypeFeatureConfig.NETHER).decorated(chanceSourcePlacement));
		oceanSourceConfig = register(SourceFeature.NAME_OCEAN, source.configured(ElementTypeFeatureConfig.WATER).decorated(sourcePlacement.rarity(ECConfig.COMMON.oceanSourceSpawnChance.get())));
		spawnFireSourceConfig = register(SourceFeature.NAME_FIRE_SPAWN, source.configured(ElementTypeFeatureConfig.FIRE).decorated(sourcePlacement));
		spawnWaterSourceConfig = register(SourceFeature.NAME_WATER_SPAWN, source.configured(ElementTypeFeatureConfig.WATER).decorated(sourcePlacement));
		spawnEarthSourceConfig = register(SourceFeature.NAME_EARTH_SPAWN, source.configured(ElementTypeFeatureConfig.EARTH).decorated(sourcePlacement));
		spawnAirSourceConfig = register(SourceFeature.NAME_AIR_SPAWN, source.configured(ElementTypeFeatureConfig.AIR).decorated(sourcePlacement));

		sourceAltar = registerStructure(SourceAltarStructure.NAME, ECStructures.SOURCE_ALTAR, RandomElementTypeFeatureConfig.ALL, ECStructures.SOURCE_ALTAR_PIECE_TYPE);

	}

	public static void onBiomeLoad(BiomeLoadingEvent event) {
		Biome.BiomeCategory category = event.getCategory();
		boolean sourceSpawn = Boolean.FALSE.equals(ECConfig.COMMON.disableSourceSpawn.get());
		
		if (Boolean.FALSE.equals(ECConfig.COMMON.disableWorldGen.get())) {
			if (sourceSpawn) {
				event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, sourceConfig);
			}
			if (category != Biome.BiomeCategory.THEEND && category != Biome.BiomeCategory.NETHER) {
				if (Boolean.FALSE.equals(ECConfig.COMMON.disableInertCrystal.get())) {
					event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, crystalOreConfig);
				}

				if (category != Biome.BiomeCategory.BEACH && category != Biome.BiomeCategory.OCEAN && category != Biome.BiomeCategory.RIVER && category != Biome.BiomeCategory.SWAMP) {
					event.getGeneration().addStructureStart(sourceAltar);
				}
			}
			if (sourceSpawn) {
				switch (category) {
				case ICY:
					event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, icySourceConfig);
					break;
				case JUNGLE:
					event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, jungleSourceConfig);
					break;
				case MUSHROOM:
					event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, mushroomSourceConfig);
					break;
				case NETHER:
					event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, netherSourceConfig);
					break;
				case OCEAN:
					event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, oceanSourceConfig);
					break;
				case EXTREME_HILLS:
					event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, hillSourceConfig);
					break;
				case PLAINS:
					event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, plainSourceConfig);
					break;
				case BEACH, RIVER, SWAMP:
					event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, wetSourceConfig);
					break;
				case TAIGA, FOREST:
					event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, forestSourceConfig);
					break;
				case MESA, DESERT, SAVANNA:
					event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, drySourceConfig);
					break;
				case THEEND:
					event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, endSourceConfig);
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
