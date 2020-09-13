package sirttas.elementalcraft.world.feature;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
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
import sirttas.elementalcraft.world.feature.structure.SourceAltarStructure;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECFeatures {

	@ObjectHolder(ElementalCraft.MODID + ":" + SourceFeature.NAME) public static Feature<NoFeatureConfig> source;
	@ObjectHolder(ElementalCraft.MODID + ":" + SourceAltarStructure.NAME) public static Structure<NoFeatureConfig> sourceAltar;

	@SubscribeEvent
	public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
		IForgeRegistry<Feature<?>> r = event.getRegistry();

		RegistryHelper.register(r, new SourceFeature(), SourceFeature.NAME);
		RegistryHelper.register(r, new SourceAltarStructure(), SourceAltarStructure.NAME);
	}

	public static void addToWorldgen() {
		for (Biome biome : ForgeRegistries.BIOMES) { // TODO exclude biomes
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
					Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ECBlocks.crystalOre.getDefaultState(), 9))
							.withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(20, 0, 0, 64)))); // TODO config

			biome.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION,
					source.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(ECConfig.CONFIG.sourceSpawnChance.get()))));
			biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES,
					sourceAltar.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
			if (ECBiomes.LAND.contains(biome)) {
				biome.addStructure(sourceAltar.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
			}
		}
	}
}
