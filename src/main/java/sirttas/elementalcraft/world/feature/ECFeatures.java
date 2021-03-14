package sirttas.elementalcraft.world.feature;

import java.util.Random;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.registry.RegistryHelper;
import sirttas.elementalcraft.world.feature.config.ElementTypeFeatureConfig;
import sirttas.elementalcraft.world.feature.config.IElementTypeFeatureConfig;
import sirttas.elementalcraft.world.feature.config.RandomElementTypeFeatureConfig;
import sirttas.elementalcraft.world.feature.placement.ECPlacements;
import sirttas.elementalcraft.world.feature.structure.ECStructures;
import sirttas.elementalcraft.world.feature.structure.SourceAltarStructure;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECFeatures {

	@ObjectHolder(ElementalCraft.MODID + ":" + SourceFeature.NAME) public static final Feature<IElementTypeFeatureConfig> SOURCE = null;

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
	private static StructureFeature<?, ?> sourceAltar;

	private ECFeatures() {}
	
	@SubscribeEvent
	public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
		IForgeRegistry<Feature<?>> r = event.getRegistry();
		Feature<IElementTypeFeatureConfig> source = new SourceFeature();
		ConfiguredPlacement<?> sourcePlacement = ECPlacements.SOURCE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG).square();
		ConfiguredPlacement<?> chanceSourcePlacement = sourcePlacement.chance(ECConfig.COMMON.sourceSpawnChance.get());
		
		RegistryHelper.register(r, source, SourceFeature.NAME);

		crystalOreConfig = register("crystal_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, ECBlocks.CRYSTAL_ORE.getDefaultState(), 
				ECConfig.COMMON.inertCrystalSize.get())).range(ECConfig.COMMON.inertCrystalYMax.get()).square().count(ECConfig.COMMON.inertCrystalCount.get()));
		sourceConfig = register(SourceFeature.NAME, source.withConfiguration(RandomElementTypeFeatureConfig.ALL).withPlacement(sourcePlacement.chance(ECConfig.COMMON.randomSourceSpawnChance.get())));
		icySourceConfig = register(SourceFeature.NAME_ICY, source.withConfiguration(RandomElementTypeFeatureConfig.ICY).withPlacement(chanceSourcePlacement));
		jungleSourceConfig = register(SourceFeature.NAME_JUNGLE, source.withConfiguration(RandomElementTypeFeatureConfig.JUNGLE).withPlacement(chanceSourcePlacement));
		mushroomSourceConfig = register(SourceFeature.NAME_MUSHROOM, source.withConfiguration(RandomElementTypeFeatureConfig.ALL).withPlacement(chanceSourcePlacement));
		wetSourceConfig = register(SourceFeature.NAME_WET, source.withConfiguration(RandomElementTypeFeatureConfig.WET).withPlacement(chanceSourcePlacement));
		drySourceConfig = register(SourceFeature.NAME_DRY, source.withConfiguration(RandomElementTypeFeatureConfig.DRY).withPlacement(chanceSourcePlacement));
		endSourceConfig = register(SourceFeature.NAME_END, source.withConfiguration(RandomElementTypeFeatureConfig.END).withPlacement(chanceSourcePlacement));
		forestSourceConfig = register(SourceFeature.NAME_FOREST, source.withConfiguration(RandomElementTypeFeatureConfig.FOREST).withPlacement(chanceSourcePlacement));
		hillSourceConfig = register(SourceFeature.NAME_HILL, source.withConfiguration(RandomElementTypeFeatureConfig.HILL).withPlacement(chanceSourcePlacement));
		plainSourceConfig = register(SourceFeature.NAME_PLAIN, source.withConfiguration(RandomElementTypeFeatureConfig.PLAIN).withPlacement(chanceSourcePlacement));
		netherSourceConfig = register(SourceFeature.NAME_NETHER, source.withConfiguration(RandomElementTypeFeatureConfig.NETHER).withPlacement(chanceSourcePlacement));
		oceanSourceConfig = register(SourceFeature.NAME_OCEAN, source.withConfiguration(ElementTypeFeatureConfig.WATER).withPlacement(sourcePlacement.chance(ECConfig.COMMON.oceanSourceSpawnChance.get())));
		spawnFireSourceConfig = register(SourceFeature.NAME_FIRE_SPAWN, source.withConfiguration(ElementTypeFeatureConfig.FIRE).withPlacement(sourcePlacement));
		spawnWaterSourceConfig = register(SourceFeature.NAME_WATER_SPAWN, source.withConfiguration(ElementTypeFeatureConfig.WATER).withPlacement(sourcePlacement));
		spawnEarthSourceConfig = register(SourceFeature.NAME_EARTH_SPAWN, source.withConfiguration(ElementTypeFeatureConfig.EARTH).withPlacement(sourcePlacement));
		spawnAirSourceConfig = register(SourceFeature.NAME_AIR_SPAWN, source.withConfiguration(ElementTypeFeatureConfig.AIR).withPlacement(sourcePlacement));

		sourceAltar = registerStructure(SourceAltarStructure.NAME, ECStructures.SOURCE_ALTAR, RandomElementTypeFeatureConfig.ALL, ECStructures.SOURCE_ALTAR_PIECE_TYPE);

	}

	public static void onBiomeLoad(BiomeLoadingEvent event) {
		Biome.Category category = event.getCategory();
		
		if (Boolean.FALSE.equals(ECConfig.COMMON.disableWorldGen.get())) {
			event.getGeneration().withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, sourceConfig);

			if (category != Biome.Category.THEEND && category != Biome.Category.NETHER) {
				if (Boolean.FALSE.equals(ECConfig.COMMON.disableInertCrystal.get())) {
					event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, crystalOreConfig);
				}

				if (category != Biome.Category.BEACH && category != Biome.Category.OCEAN && category != Biome.Category.RIVER && category != Biome.Category.SWAMP) {
					event.getGeneration().withStructure(sourceAltar);
				}
			}
			switch (category) {
			case ICY:
				event.getGeneration().withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, icySourceConfig);
				break;
			case JUNGLE:
				event.getGeneration().withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, jungleSourceConfig);
				break;
			case MUSHROOM:
				event.getGeneration().withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, mushroomSourceConfig);
				break;
			case NETHER:
				event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, netherSourceConfig);
				break;
			case OCEAN:
				event.getGeneration().withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, oceanSourceConfig);
				break;
			case EXTREME_HILLS:
				event.getGeneration().withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, hillSourceConfig);
				break;
			case PLAINS:
				event.getGeneration().withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, plainSourceConfig);
				break;
			case BEACH:
			case RIVER:
			case SWAMP:
				event.getGeneration().withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, wetSourceConfig);
				break;
			case TAIGA:
			case FOREST:
				event.getGeneration().withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, forestSourceConfig);
				break;
			case MESA:
			case DESERT:
			case SAVANNA:
				event.getGeneration().withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, drySourceConfig);
				break;
			case THEEND:
				event.getGeneration().withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, endSourceConfig);
				break;
			default:
				break;

			}
		}
	}

	public static void addSpawnSources(ServerWorld world) {
		Random rand = new Random(world.getSeed());
		BlockPos pos = world.getSpawnPoint().add(-100, 0, -100);

		addSpawnSource(world, rand, pos.add(rand.nextInt(100), 0, rand.nextInt(100)), spawnFireSourceConfig);
		addSpawnSource(world, rand, pos.add(rand.nextInt(100), 0, rand.nextInt(100)), spawnWaterSourceConfig);
		addSpawnSource(world, rand, pos.add(rand.nextInt(100), 0, rand.nextInt(100)), spawnEarthSourceConfig);
		addSpawnSource(world, rand, pos.add(rand.nextInt(100), 0, rand.nextInt(100)), spawnAirSourceConfig);
	}

	private static void addSpawnSource(ServerWorld world, Random rand, BlockPos pos, ConfiguredFeature<?, ?> source) {
		ChunkPos chunkPos = new ChunkPos(pos);
		ServerChunkProvider chunkProvider = world.getChunkProvider();
		
		chunkProvider.getChunk(chunkPos.x, chunkPos.z, true);
		source.generate(world, chunkProvider.getChunkGenerator(), rand, pos);
	}

	private static <C extends IFeatureConfig> ConfiguredFeature<C, ?> register(String name, ConfiguredFeature<C, ?> feature) {
		return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, ElementalCraft.createRL(name), feature);
	}

	private static <C extends IFeatureConfig> StructureFeature<C, ?> registerStructure(String name, Structure<C> structure, C config, IStructurePieceType structurePieceType) {
		ResourceLocation location = ElementalCraft.createRL(name);
		StructureFeature<C, ?> structureFeature = Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, location, new StructureFeature<>(structure, config));
		
		Registry.register(Registry.STRUCTURE_PIECE, location, structurePieceType);
		Structure.NAME_STRUCTURE_BIMAP.put(structure.getStructureName(), structure);
		return structureFeature;
	}
}
