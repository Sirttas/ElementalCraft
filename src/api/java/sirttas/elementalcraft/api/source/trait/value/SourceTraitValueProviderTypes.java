package sirttas.elementalcraft.api.source.trait.value;

import com.mojang.serialization.Codec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.registry.ElementalCraftRegistries;

public class SourceTraitValueProviderTypes {

	private static final DeferredRegister<SourceTraitValueProviderType<?>> DEFERRED_REGISTER = DeferredRegister.create(ElementalCraftRegistries.Keys.SOURCE_TRAIT_VALUE_PROVIDER_TYPE, ElementalCraftApi.MODID);

	public static final DeferredHolder<SourceTraitValueProviderType<?>, SourceTraitValueProviderType<FixedSourceTraitValueProvider>> FIXED = register(FixedSourceTraitValueProvider.CODEC, FixedSourceTraitValueProvider.NAME);
	public static final DeferredHolder<SourceTraitValueProviderType<?>, SourceTraitValueProviderType<LinearSourceTraitValueProvider>> LINEAR = register(LinearSourceTraitValueProvider.CODEC, LinearSourceTraitValueProvider.NAME);
	public static final DeferredHolder<SourceTraitValueProviderType<?>, SourceTraitValueProviderType<RangeBasedSourceTraitValueProvider>> RANGE_BASED = register(RangeBasedSourceTraitValueProvider.CODEC, RangeBasedSourceTraitValueProvider.NAME);
	public static final DeferredHolder<SourceTraitValueProviderType<?>, SourceTraitValueProviderType<StepsSourceTraitValueProvider>> STEPS = register(StepsSourceTraitValueProvider.CODEC, StepsSourceTraitValueProvider.NAME);
	public static final DeferredHolder<SourceTraitValueProviderType<?>, SourceTraitValueProviderType<ChanceSourceTraitValueProvider>> CHANCE = register(ChanceSourceTraitValueProvider.CODEC, ChanceSourceTraitValueProvider.NAME);
	public static final DeferredHolder<SourceTraitValueProviderType<?>, SourceTraitValueProviderType<PredicateSourceTraitValueProvider>> PREDICATE = register(PredicateSourceTraitValueProvider.CODEC, PredicateSourceTraitValueProvider.NAME);


	private SourceTraitValueProviderTypes() {}

	private static <T extends ISourceTraitValueProvider> DeferredHolder<SourceTraitValueProviderType<?>, SourceTraitValueProviderType<T>> register(Codec<T> codec, String name) {
		return DEFERRED_REGISTER.register(name, () -> new SourceTraitValueProviderType<>(codec));
	}

	public static void register(IEventBus bus) {
		DEFERRED_REGISTER.register(bus);
	}
}
