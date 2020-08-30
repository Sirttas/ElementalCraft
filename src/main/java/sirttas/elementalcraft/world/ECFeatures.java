package sirttas.elementalcraft.world;

import java.util.ArrayList;
import java.util.List;
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
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.mixin.AccessorBiomeGenerationSettings;
import sirttas.elementalcraft.registry.RegistryHelper;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECFeatures {

	public static final Feature<NoFeatureConfig> SOURCE = new SourceFeature(NoFeatureConfig.field_236558_a_);

	@SubscribeEvent
	public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
		IForgeRegistry<Feature<?>> r = event.getRegistry();

		RegistryHelper.register(r, SOURCE, "source");
	}

	public static void addToWorldgen() {
		ConfiguredFeature<?, ?> crystalOre = register(new ResourceLocation(ElementalCraft.MODID, "crystal_ore"),
				Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.field_241882_a, ECBlocks.crystalOre.getDefaultState(), 9)).func_242733_d(64).func_242728_a()
						.func_242731_b(20));
		ConfiguredFeature<?, ?> source = register(new ResourceLocation(ElementalCraft.MODID, "source"),
				SOURCE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.field_244001_l).func_242729_a(ECConfig.CONFIG.sourceSpawnChance.get()));

		for (Biome biome : ForgeRegistries.BIOMES) { // TODO exclude biomes
			addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, biome, crystalOre);
			addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, biome, source);
		}
	}

	public static void addFeature(GenerationStage.Decoration decoration, Biome biome, ConfiguredFeature<?, ?> feature) {
		List<List<Supplier<ConfiguredFeature<?, ?>>>> features = biome.func_242440_e().func_242498_c();

		if (features instanceof ImmutableList) {
			features = features.stream().map(l -> l.stream().collect(Collectors.toList())).collect(Collectors.toList());
			((AccessorBiomeGenerationSettings) biome.func_242440_e()).setFeatures(features);
		}

		while (features.size() <= decoration.ordinal()) {
			features.add(new ArrayList<>());
		}
		features.get(decoration.ordinal()).add(() -> feature);
	}

	private static <C extends IFeatureConfig> ConfiguredFeature<C, ?> register(ResourceLocation loc, ConfiguredFeature<C, ?> feature) {
		return Registry.register(WorldGenRegistries.field_243653_e, loc, feature);
	}
}
