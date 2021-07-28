package sirttas.elementalcraft.world.feature.structure;

import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.registry.RegistryHelper;
import sirttas.elementalcraft.world.feature.config.IElementTypeFeatureConfig;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECStructures {

	public static final StructureFeature<IElementTypeFeatureConfig> SOURCE_ALTAR = new SourceAltarStructure();
	public static final StructurePieceType SOURCE_ALTAR_PIECE_TYPE = SourceAltarStructure.Piece::new;

	private ECStructures() {}
	
	@SubscribeEvent
	public static void registerStructures(RegistryEvent.Register<StructureFeature<?>> event) {
		IForgeRegistry<StructureFeature<?>> r = event.getRegistry();

		RegistryHelper.register(r, SOURCE_ALTAR, SourceAltarStructure.NAME);
	}

}
