package sirttas.elementalcraft.world.feature.structure;

import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.registry.RegistryHelper;
import sirttas.elementalcraft.world.feature.config.IElementTypeFeatureConfig;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECStructures {

	public static final Structure<IElementTypeFeatureConfig> SOURCE_ALTAR = new SourceAltarStructure();
	public static final IStructurePieceType SOURCE_ALTAR_PIECE_TYPE = SourceAltarStructure.Piece::new;

	private ECStructures() {}
	
	@SubscribeEvent
	public static void registerStructures(RegistryEvent.Register<Structure<?>> event) {
		IForgeRegistry<Structure<?>> r = event.getRegistry();

		RegistryHelper.register(r, SOURCE_ALTAR, SourceAltarStructure.NAME);
	}

}
