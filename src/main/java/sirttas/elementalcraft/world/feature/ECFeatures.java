package sirttas.elementalcraft.world.feature;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.data.worldgen.StructureSets;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.registry.RegistryHelper;
import sirttas.elementalcraft.world.feature.config.ElementTypeFeatureConfig;
import sirttas.elementalcraft.world.feature.config.IElementTypeFeatureConfig;
import sirttas.elementalcraft.world.feature.config.RandomElementTypeFeatureConfig;
import sirttas.elementalcraft.world.feature.placement.ECPlacements;
import sirttas.elementalcraft.world.feature.placement.SourcePlacement;
import sirttas.elementalcraft.world.feature.structure.ECStructures;
import sirttas.elementalcraft.world.feature.structure.SourceAltarStructure;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECFeatures {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + SourceFeature.NAME) public static final Feature<IElementTypeFeatureConfig> SOURCE = null;

	private static Holder<PlacedFeature> crystalOrePlaced;
	private static Holder<PlacedFeature> sourcePlaced;
	private static Holder<PlacedFeature> icySourcePlaced;
	private static Holder<PlacedFeature> jungleSourcePlaced;
	private static Holder<PlacedFeature> mushroomSourcePlaced;
	private static Holder<PlacedFeature> netherSourcePlaced;
	private static Holder<PlacedFeature> wetSourcePlaced;
	private static Holder<PlacedFeature> drySourcePlaced;
	private static Holder<PlacedFeature> endSourcePlaced;
	private static Holder<PlacedFeature> forestSourcePlaced;
	private static Holder<PlacedFeature> hillSourcePlaced;
	private static Holder<PlacedFeature> mountainSourcePlaced;
	private static Holder<PlacedFeature> plainSourcePlaced;
	private static Holder<PlacedFeature> oceanSourcePlaced;

	private ECFeatures() {}
	
	@SubscribeEvent
	public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
		registerPlacement("source", ECPlacements.SOURCE);

		IForgeRegistry<Feature<?>> r = event.getRegistry();
		Feature<IElementTypeFeatureConfig> source = new SourceFeature();
		var oreInertCrystalTargetList = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ECBlocks.CRYSTAL_ORE.defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ECBlocks.DEEPSLATE_CRYSTAL_ORE.get().defaultBlockState()));
		var chanceSourcePlacement = sourcePlacement(RarityFilter.onAverageOnceEvery(ECConfig.COMMON.sourceSpawnChance.get()));
		
		RegistryHelper.register(r, source, SourceFeature.NAME);

		Holder<ConfiguredFeature<?, ?>> crystalOreConfig = register("crystal_ore", Feature.ORE, new OreConfiguration(oreInertCrystalTargetList, ECConfig.COMMON.inertCrystalSize.get()));
		crystalOrePlaced = register("crystal_ore_middle", crystalOreConfig, OrePlacements.commonOrePlacement(ECConfig.COMMON.inertCrystalCount.get(), HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(ECConfig.COMMON.inertCrystalYMax.get()))));

		Holder<ConfiguredFeature<?, ?>> sourceConfig = register(SourceFeature.NAME, source, RandomElementTypeFeatureConfig.ALL);
		sourcePlaced = register(SourceFeature.NAME, sourceConfig, sourcePlacement(RarityFilter.onAverageOnceEvery(ECConfig.COMMON.randomSourceSpawnChance.get())));

		Holder<ConfiguredFeature<?, ?>> icySourceConfig = register(SourceFeature.NAME_ICY, source, RandomElementTypeFeatureConfig.ICY);
		icySourcePlaced =register(SourceFeature.NAME_ICY, icySourceConfig, chanceSourcePlacement);
		Holder<ConfiguredFeature<?, ?>> jungleSourceConfig = register(SourceFeature.NAME_JUNGLE, source, RandomElementTypeFeatureConfig.JUNGLE);
		jungleSourcePlaced = register(SourceFeature.NAME_JUNGLE, jungleSourceConfig, chanceSourcePlacement);
		Holder<ConfiguredFeature<?, ?>> mushroomSourceConfig = register(SourceFeature.NAME_MUSHROOM, source, RandomElementTypeFeatureConfig.ALL);
		mushroomSourcePlaced = register(SourceFeature.NAME_MUSHROOM, mushroomSourceConfig, chanceSourcePlacement);
		Holder<ConfiguredFeature<?, ?>> wetSourceConfig = register(SourceFeature.NAME_WET, source, RandomElementTypeFeatureConfig.WET);
		wetSourcePlaced = register(SourceFeature.NAME_WET, wetSourceConfig, chanceSourcePlacement);
		Holder<ConfiguredFeature<?, ?>> drySourceConfig = register(SourceFeature.NAME_DRY, source, RandomElementTypeFeatureConfig.DRY);
		drySourcePlaced = register(SourceFeature.NAME_DRY, drySourceConfig, chanceSourcePlacement);
		Holder<ConfiguredFeature<?, ?>> endSourceConfig = register(SourceFeature.NAME_END, source, RandomElementTypeFeatureConfig.END);
		endSourcePlaced = register(SourceFeature.NAME_END, endSourceConfig, chanceSourcePlacement);
		Holder<ConfiguredFeature<?, ?>> forestSourceConfig = register(SourceFeature.NAME_FOREST, source, RandomElementTypeFeatureConfig.FOREST);
		forestSourcePlaced = register(SourceFeature.NAME_FOREST, forestSourceConfig, chanceSourcePlacement);
		Holder<ConfiguredFeature<?, ?>> hillSourceConfig = register(SourceFeature.NAME_HILL, source, RandomElementTypeFeatureConfig.HILL);
		hillSourcePlaced = register(SourceFeature.NAME_HILL, hillSourceConfig, chanceSourcePlacement);
		Holder<ConfiguredFeature<?, ?>> mountainSourceConfig = register(SourceFeature.NAME_MOUNTAIN, source, RandomElementTypeFeatureConfig.MOUNTAIN);
		mountainSourcePlaced = register(SourceFeature.NAME_MOUNTAIN, mountainSourceConfig, chanceSourcePlacement);
		Holder<ConfiguredFeature<?, ?>> plainSourceConfig = register(SourceFeature.NAME_PLAIN, source, RandomElementTypeFeatureConfig.PLAIN);
		plainSourcePlaced = register(SourceFeature.NAME_PLAIN, plainSourceConfig, chanceSourcePlacement);
		Holder<ConfiguredFeature<?, ?>> netherSourceConfig = register(SourceFeature.NAME_NETHER, source, RandomElementTypeFeatureConfig.NETHER);
		netherSourcePlaced = register(SourceFeature.NAME_NETHER, netherSourceConfig, chanceSourcePlacement);
		Holder<ConfiguredFeature<?, ?>> oceanSourceConfig = register(SourceFeature.NAME_OCEAN, source, ElementTypeFeatureConfig.WATER);
		oceanSourcePlaced = register(SourceFeature.NAME_OCEAN, oceanSourceConfig, sourcePlacement(RarityFilter.onAverageOnceEvery(ECConfig.COMMON.oceanSourceSpawnChance.get())));

		registerStructure(SourceAltarStructure.NAME, ECStructures.SOURCE_ALTAR, RandomElementTypeFeatureConfig.ALL, ECStructures.SOURCE_ALTAR_PIECE_TYPE, new RandomSpreadStructurePlacement(ECConfig.COMMON.sourceAltarDistance.get(), 8, RandomSpreadType.LINEAR, 4847339));
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
					case MOUNTAIN -> generation.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, mountainSourcePlaced);
					case PLAINS -> generation.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, plainSourcePlaced);
					case BEACH, RIVER, SWAMP -> generation.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, wetSourcePlaced);
					case TAIGA, FOREST -> generation.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, forestSourcePlaced);
					case MESA, DESERT, SAVANNA -> generation.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, drySourcePlaced);
					case THEEND -> generation.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, endSourcePlaced);
				}
			}
		}
	}

	public static void addSpawnSources(ServerLevel world) {
		if (Boolean.FALSE.equals(ECConfig.COMMON.disableSourceSpawn.get())) {
			Random rand = new Random(world.getSeed());
			BlockPos pos = world.getSharedSpawnPos().offset(-100, 0, -100);

			for (var type : ElementType.ALL_VALID) {
				for (int i = 0; i < ECConfig.COMMON.sourceSpawnCount.get(); i++) {
					addSpawnSource(world, pos.offset(rand.nextInt(100), 0, rand.nextInt(100)), type);
				}
			}
		}
	}

	private static void addSpawnSource(ServerLevel level, BlockPos pos, ElementType type) {
		var x = pos.getX();
		var z = pos.getZ();
		var y = SourcePlacement.getHeight(level, x, z);
		ChunkPos chunkPos = new ChunkPos(pos);
		ServerChunkCache chunkProvider = level.getChunkSource();

		chunkProvider.getChunk(chunkPos.x, chunkPos.z, true);
		level.setBlock(new BlockPos(x, y, z), ECBlocks.SOURCE.defaultBlockState().setValue(ElementType.STATE_PROPERTY, type), 3);
	}

	public static <C extends FeatureConfiguration, F extends Feature<C>> Holder<ConfiguredFeature<?, ?>> register(String name, F feature, C config) {
		return BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_FEATURE, ElementalCraft.createRL(name), new ConfiguredFeature<>(feature, config));
	}

	public static Holder<PlacedFeature> register(String name, Holder<? extends ConfiguredFeature<?, ?>> config, List<PlacementModifier> placement) {
		return BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, ElementalCraft.createRL(name), new PlacedFeature(Holder.hackyErase(config), List.copyOf(placement)));
	}

	private static <C extends FeatureConfiguration> Holder<ConfiguredStructureFeature<?, ?>> registerStructure(String name, StructureFeature<C> structure, C config, StructurePieceType structurePieceType, StructurePlacement structurePlacement) {
		ResourceLocation location = ElementalCraft.createRL(name);
		var key = ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, location);
		var setKey = ResourceKey.create(Registry.STRUCTURE_SET_REGISTRY, location);

		var configuredStructureFeatureHolder = StructureFeatures.register(key, structure.configured(config, BiomeTags.IS_HILL)); //TODO: Add biome tags

		StructureSets.register(setKey, new StructureSet(configuredStructureFeatureHolder, structurePlacement));
		Registry.register(Registry.STRUCTURE_PIECE, location, structurePieceType);
		return configuredStructureFeatureHolder;
	}

	private static <P extends PlacementModifier> PlacementModifierType<P> registerPlacement(String name, PlacementModifierType<P> pPlacementModifierType) {
		return Registry.register(Registry.PLACEMENT_MODIFIERS, ElementalCraft.createRL(name), pPlacementModifierType);
	}

}
