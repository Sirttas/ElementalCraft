package sirttas.elementalcraft.world.feature.structure;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECStructureTypes {

	private static final DeferredRegister<StructureType<?>> STRUCTURE_TYPE_DEFERRED_REGISTER = DeferredRegister.create(BuiltInRegistries.STRUCTURE_TYPE.key(), ElementalCraftApi.MODID);
	private static final DeferredRegister<StructurePieceType> PIECE_TYPE_DEFERRED_REGISTER = DeferredRegister.create(BuiltInRegistries.STRUCTURE_PIECE.key(), ElementalCraftApi.MODID);



	public static final DeferredHolder<StructureType<?>, StructureType<SourceAltarStructure>> SOURCE_ALTAR = register(SourceAltarStructure.CODEC, SourceAltarStructure.NAME);
	public static final DeferredHolder<StructurePieceType, StructurePieceType> SOURCE_ALTAR_PIECE_TYPE = register(get(SourceAltarStructure.Piece::new), SourceAltarStructure.NAME);

	private ECStructureTypes() {}

	private static StructurePieceType get(StructurePieceType.StructureTemplateType type) {
		return type;
	}

	private static <T extends Structure> DeferredHolder<StructureType<?>, StructureType<T>> register(Codec<T> codec, String name) {
		return STRUCTURE_TYPE_DEFERRED_REGISTER.register(name, () -> () -> codec);
	}

	private static DeferredHolder<StructurePieceType, StructurePieceType> register(StructurePieceType type, String name) {
		return PIECE_TYPE_DEFERRED_REGISTER.register(name, () -> type);
	}

	public static void register(IEventBus bus) {
		STRUCTURE_TYPE_DEFERRED_REGISTER.register(bus);
		PIECE_TYPE_DEFERRED_REGISTER.register(bus);
	}
}
