package sirttas.elementalcraft.data.predicate.block;

import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.dpanvil.api.predicate.block.BlockPosPredicateType;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.data.predicate.block.pipe.HasPipeUpgrade;
import sirttas.elementalcraft.data.predicate.block.rune.HasRunePredicate;
import sirttas.elementalcraft.data.predicate.block.shrine.HasShrineUpgradePredicate;

public class ECBlockPosPredicateTypes {

	private static final DeferredRegister<BlockPosPredicateType<?>> DEFERRED_REGISTRY = DeferredRegister.create(BlockPosPredicateType.REGISTRY_KEY, ElementalCraftApi.MODID);

	public static final DeferredHolder<BlockPosPredicateType<?>, BlockPosPredicateType<HasShrineUpgradePredicate>> HAS_SHRINE_UPGRADE = register(HasShrineUpgradePredicate.NAME, HasShrineUpgradePredicate.CODEC);
	public static final DeferredHolder<BlockPosPredicateType<?>, BlockPosPredicateType<HasPipeUpgrade>> HAS_PIPE_UPGRADE = register(HasPipeUpgrade.NAME, HasPipeUpgrade.CODEC);
	public static final DeferredHolder<BlockPosPredicateType<?>, BlockPosPredicateType<HasRunePredicate>> HAS_RUNE = register(HasRunePredicate.NAME, HasRunePredicate.CODEC);
	public static final DeferredHolder<BlockPosPredicateType<?>, BlockPosPredicateType<RangeFromSpawnPredicate>> RANGE_FROM_SPAWN = register(RangeFromSpawnPredicate.NAME, RangeFromSpawnPredicate.CODEC);

	private ECBlockPosPredicateTypes() {}


	private static <T extends IBlockPosPredicate> DeferredHolder<BlockPosPredicateType<?>, BlockPosPredicateType<T>> register(String name, MapCodec<T> codec) {
		return DEFERRED_REGISTRY.register(name, () -> new BlockPosPredicateType<>(codec));
	}

	public static void register(IEventBus bus) {
		DEFERRED_REGISTRY.register(bus);
	}
}
