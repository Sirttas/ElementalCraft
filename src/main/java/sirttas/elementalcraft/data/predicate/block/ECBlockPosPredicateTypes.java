package sirttas.elementalcraft.data.predicate.block;

import com.mojang.serialization.Codec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import sirttas.dpanvil.api.predicate.block.BlockPosPredicateType;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.data.predicate.block.rune.HasRunePredicate;
import sirttas.elementalcraft.data.predicate.block.shrine.HasShrineUpgradePredicate;

public class ECBlockPosPredicateTypes {

	private static final DeferredRegister<BlockPosPredicateType<?>> DEFERRED_REGISTRY = DeferredRegister.create(BlockPosPredicateType.REGISTRY_KEY, ElementalCraftApi.MODID);

	public static final RegistryObject<BlockPosPredicateType<HasShrineUpgradePredicate>> HAS_SHRINE_UPGRADE = register(HasShrineUpgradePredicate.CODEC, HasShrineUpgradePredicate.NAME);
	public static final RegistryObject<BlockPosPredicateType<HasRunePredicate>> HAS_RUNE = register(HasRunePredicate.CODEC, HasRunePredicate.NAME);
	public static final RegistryObject<BlockPosPredicateType<RangeFromSpawnPredicate>> RANGE_FROM_SPAWN = register(RangeFromSpawnPredicate.CODEC, RangeFromSpawnPredicate.NAME);

	private ECBlockPosPredicateTypes() {}


	private static <T extends IBlockPosPredicate> RegistryObject<BlockPosPredicateType<T>> register(Codec<T> codec, String name) {
		return DEFERRED_REGISTRY.register(name, () -> new BlockPosPredicateType<>(codec));
	}

	public static void register(IEventBus bus) {
		DEFERRED_REGISTRY.register(bus);
	}
}
