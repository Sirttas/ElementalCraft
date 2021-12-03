package sirttas.elementalcraft.world.feature.structure;

import com.google.common.collect.ImmutableMap;

import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.registry.RegistryHelper;
import sirttas.elementalcraft.world.feature.config.IElementTypeFeatureConfig;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECStructures {

	public static final StructureFeature<IElementTypeFeatureConfig> SOURCE_ALTAR = new SourceAltarStructure();
	public static final StructurePieceType SOURCE_ALTAR_PIECE_TYPE = StructurePieceType.setTemplatePieceId(SourceAltarStructure.Piece::new, ElementalCraft.createRL("source_altar").toString());

	private ECStructures() {}
	
	@SubscribeEvent
	public static void registerStructures(RegistryEvent.Register<StructureFeature<?>> event) {
		IForgeRegistry<StructureFeature<?>> r = event.getRegistry();

		register(r, SOURCE_ALTAR, new StructureFeatureConfiguration(ECConfig.COMMON.sourceAltarDistance.get(), 8, 4847339), SourceAltarStructure.NAME);
	}
	
	private static void register(IForgeRegistry<StructureFeature<?>> reg, StructureFeature<?> thing, StructureFeatureConfiguration config, String name) {
		RegistryHelper.register(reg, thing, name);
		
		NoiseGeneratorSettings.bootstrap().structureSettings().structureConfig().put(thing, config);
		StructureSettings.DEFAULTS = ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder().putAll(StructureSettings.DEFAULTS).put(thing, config).build();
	}
	
}
