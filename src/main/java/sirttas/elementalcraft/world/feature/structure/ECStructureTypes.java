package sirttas.elementalcraft.world.feature.structure;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECStructureTypes {

	private static final DeferredRegister<StructureType<?>> STRUCTURE_TYPE_DEFERRED_REGISTER = DeferredRegister.create(Registry.STRUCTURE_TYPE_REGISTRY, ElementalCraftApi.MODID);
	private static final DeferredRegister<StructurePieceType> PIECE_TYPE_DEFERRED_REGISTER = DeferredRegister.create(Registry.STRUCTURE_PIECE_REGISTRY, ElementalCraftApi.MODID);



	public static final RegistryObject<StructureType<SourceAltarStructure>> SOURCE_ALTAR = register(SourceAltarStructure.CODEC, SourceAltarStructure.NAME);
	public static final RegistryObject<StructurePieceType> SOURCE_ALTAR_PIECE_TYPE = register(get(SourceAltarStructure.Piece::new), SourceAltarStructure.NAME);

	private ECStructureTypes() {}

	private static StructurePieceType get(StructurePieceType.StructureTemplateType type) {
		return type;
	}

	private static <T extends Structure> RegistryObject<StructureType<T>> register(Codec<T> codec, String name) {
		return STRUCTURE_TYPE_DEFERRED_REGISTER.register(name, () -> () -> codec);
	}

	private static RegistryObject<StructurePieceType> register(StructurePieceType type, String name) {
		return PIECE_TYPE_DEFERRED_REGISTER.register(name, () -> type);
	}

	public static void register(IEventBus bus) {
		STRUCTURE_TYPE_DEFERRED_REGISTER.register(bus);
		PIECE_TYPE_DEFERRED_REGISTER.register(bus);
	}
}
