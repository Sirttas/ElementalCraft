package sirttas.elementalcraft.world.feature;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
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
import sirttas.elementalcraft.world.feature.structure.ECStructures;
import sirttas.elementalcraft.world.feature.structure.SourceAltarStructure;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECFeatures {

	@ObjectHolder(ElementalCraft.MODID + ":" + SourceFeature.NAME) public static Feature<NoFeatureConfig> source;

	private static ConfiguredFeature<?, ?> crystalOreConfig;
	private static ConfiguredFeature<?, ?> sourceConfig;
	private static StructureFeature<?, ?> sourceAltar;

	@SubscribeEvent
	public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
		IForgeRegistry<Feature<?>> r = event.getRegistry();
		Feature<NoFeatureConfig> source = new SourceFeature();
		
		RegistryHelper.register(r, source, SourceFeature.NAME);

		crystalOreConfig = register("crystal_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, ECBlocks.crystalOre.getDefaultState(), 
				ECConfig.COMMON.inertCrystalSize.get())).range(ECConfig.COMMON.inertCrystalYMax.get()).square().func_242731_b(ECConfig.COMMON.inertCrystalCount.get()));
		sourceConfig = register(SourceFeature.NAME,
				source.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).chance(ECConfig.COMMON.sourceSpawnChance.get()));

		sourceAltar = registerStructure(SourceAltarStructure.NAME, ECStructures.SOURCE_ALTAR, NoFeatureConfig.field_236559_b_, ECStructures.SOURCE_ALTAR_PIECE_TYPE);

	}

	public static void onBiomeLoad(BiomeLoadingEvent event) {
		Biome.Category category = event.getCategory();
		
		if (Boolean.FALSE.equals(ECConfig.COMMON.disableWorldGen.get()) && category != Biome.Category.THEEND && category != Biome.Category.NETHER) {
			if (Boolean.FALSE.equals(ECConfig.COMMON.disableInertCrystal.get())) {
				event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, crystalOreConfig);
			}
			event.getGeneration().withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, sourceConfig);
	
			if (category != Biome.Category.BEACH && category != Biome.Category.OCEAN && category != Biome.Category.RIVER && category != Biome.Category.SWAMP) {
				event.getGeneration().withStructure(sourceAltar);
			}
		}
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
