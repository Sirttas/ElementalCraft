package sirttas.elementalcraft.world;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.registry.RegistryHelper;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECFeatures {

	public static final Feature<NoFeatureConfig> SOURCE = new SourceFeature(NoFeatureConfig::deserialize);

	@SubscribeEvent
	public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
		IForgeRegistry<Feature<?>> r = event.getRegistry();

		RegistryHelper.register(r, SOURCE, "source");
	}

	public static void addToWorldgen() {
		for (Biome biome : ForgeRegistries.BIOMES) { // TODO exclude biomes
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
					Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ECBlocks.crystalOre.getDefaultState(), 9))
							.withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(20, 0, 0, 64)))); // TODO config

			biome.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION,
					SOURCE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(ECConfig.CONFIG.sourceSpawnChance.get()))));
		}
	}
}
