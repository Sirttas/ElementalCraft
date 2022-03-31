package sirttas.elementalcraft.world.feature.structure;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.datafix.fixes.WorldGenSettingsFix;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
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
	public static final StructurePieceType.StructureTemplateType SOURCE_ALTAR_PIECE_TYPE = get(SourceAltarStructure.Piece::new);

	private ECStructures() {}
	
	@SubscribeEvent
	public static void registerStructures(RegistryEvent.Register<StructureFeature<?>> event) {
		IForgeRegistry<StructureFeature<?>> r = event.getRegistry();

		register(r, SOURCE_ALTAR, new WorldGenSettingsFix.StructureFeatureConfiguration(ECConfig.COMMON.sourceAltarDistance.get(), 8, 4847339), SourceAltarStructure.NAME);
	}
	
	private static void register(IForgeRegistry<StructureFeature<?>> reg, StructureFeature<?> thing, WorldGenSettingsFix.StructureFeatureConfiguration config, String name) {
		RegistryHelper.register(reg, thing, name);

		WorldGenSettingsFix.DEFAULTS = ImmutableMap.<String, WorldGenSettingsFix.StructureFeatureConfiguration>builder().putAll(WorldGenSettingsFix.DEFAULTS).put(ElementalCraft.createRL(name).toString(), config).build();
		StructurePieceType.setTemplatePieceId(SOURCE_ALTAR_PIECE_TYPE, ElementalCraft.createRL("source_altar").toString());
		StructureFeature.STEP.put(thing, GenerationStep.Decoration.SURFACE_STRUCTURES);
	}

	private static StructurePieceType.StructureTemplateType get(StructurePieceType.StructureTemplateType type) {
		return type;
	}

}
