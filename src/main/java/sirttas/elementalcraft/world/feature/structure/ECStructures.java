package sirttas.elementalcraft.world.feature.structure;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
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

		register(r, SOURCE_ALTAR, SOURCE_ALTAR_PIECE_TYPE, SourceAltarStructure.NAME);
	}

	private static void register(IForgeRegistry<StructureFeature<?>> reg, StructureFeature<?> thing, StructurePieceType.StructureTemplateType pieceType, String name) {
		register(reg, thing, name);
		registerPiece(pieceType, name);
	}

	private static void register(IForgeRegistry<StructureFeature<?>> reg, StructureFeature<?> thing, String name) {
		RegistryHelper.register(reg, thing, ElementalCraft.createRL(name));
	}

	private static void registerPiece(StructurePieceType.StructureTemplateType pieceType, String name) {
		registerPiece(pieceType, ElementalCraft.createRL(name));
	}

	private static void registerPiece(StructurePieceType.StructureTemplateType pieceType, ResourceLocation location) {
		Registry.register(Registry.STRUCTURE_PIECE, location, pieceType);
	}

	private static StructurePieceType.StructureTemplateType get(StructurePieceType.StructureTemplateType type) {
		return type;
	}

}
