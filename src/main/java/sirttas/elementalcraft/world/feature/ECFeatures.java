package sirttas.elementalcraft.world.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

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
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.registry.RegistryHelper;
import sirttas.elementalcraft.world.biome.ECBiomes;
import sirttas.elementalcraft.world.feature.structure.ECStructures;
import sirttas.elementalcraft.world.feature.structure.SourceAltarStructure;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECFeatures {

	@ObjectHolder(ElementalCraft.MODID + ":" + SourceFeature.NAME) public static Feature<NoFeatureConfig> source;

	@SubscribeEvent
	public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
		IForgeRegistry<Feature<?>> r = event.getRegistry();

		RegistryHelper.register(r, new SourceFeature(), SourceFeature.NAME);
	}

	public static void addToWorldgen() {
		ConfiguredFeature<?, ?> crystalOre = register("crystal_ore",
				Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241882_a, ECBlocks.crystalOre.getDefaultState(), 9)).func_242733_d(64).func_242728_a()
						.func_242731_b(20));
		ConfiguredFeature<?, ?> sourceConfig = register(SourceFeature.NAME,
				source.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.field_244001_l).func_242729_a(ECConfig.CONFIG.sourceSpawnChance.get()));

		StructureFeature<?, ?> sourceAltar = registerStructure(SourceAltarStructure.NAME, ECStructures.SOURCE_ALTAR, NoFeatureConfig.field_236559_b_, ECStructures.SOURCE_ALTAR_PIECE_TYPE);

		for (Biome biome : ForgeRegistries.BIOMES) { // TODO exclude biomes
			addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, biome, crystalOre);
			addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, biome, sourceConfig);

			if (ECBiomes.LAND.stream().anyMatch(k -> k.func_240901_a_().equals(biome.getRegistryName()))) {
				addStructure(biome, sourceAltar);
			}
		}
	}

	public static void addFeature(GenerationStage.Decoration decoration, Biome biome, ConfiguredFeature<?, ?> feature) {
		List<List<Supplier<ConfiguredFeature<?, ?>>>> features = biome.func_242440_e().func_242498_c();

		if (features instanceof ImmutableList) {
			features = features.stream().map(l -> l.stream().collect(Collectors.toList())).collect(Collectors.toList());
			biome.func_242440_e().field_242484_f = features;
		}

		while (features.size() <= decoration.ordinal()) {
			features.add(new ArrayList<>());
		}
		features.get(decoration.ordinal()).add(() -> feature);
	}

	public static void addStructure(Biome biome, StructureFeature<?, ?> structure) {
		List<Supplier<StructureFeature<?, ?>>> structures = biome.func_242440_e().field_242485_g;

		if (structures instanceof ImmutableList) {
			structures = structures.stream().collect(Collectors.toList());
			biome.func_242440_e().field_242485_g = structures;
		}

		structures.add(() -> structure);

		Map<Integer, List<Structure<?>>> structuresByStage = biome.field_242421_g;
		int step = structure.field_236268_b_.func_236396_f_().ordinal();

		if (!structuresByStage.containsKey(step)) {
			structuresByStage.put(step, new ArrayList<>());
		}
		structuresByStage.get(step).add(structure.field_236268_b_);
	}

	private static <C extends IFeatureConfig> ConfiguredFeature<C, ?> register(String name, ConfiguredFeature<C, ?> feature) {
		return Registry.register(WorldGenRegistries.field_243653_e, new ResourceLocation(ElementalCraft.MODID, name), feature);
	}

	private static <C extends IFeatureConfig> StructureFeature<C, ?> registerStructure(String name, Structure<C> structure, C config, IStructurePieceType structurePieceType) {
		ResourceLocation location = new ResourceLocation(ElementalCraft.MODID, name);
		StructureFeature<C, ?> structureFeature = Registry.register(WorldGenRegistries.field_243654_f, location, new StructureFeature<>(structure, config));
		
		Registry.register(Registry.STRUCTURE_PIECE, location, structurePieceType);
		Structure.field_236365_a_.put(structure.getStructureName(), structure);
		return structureFeature;
	}
}