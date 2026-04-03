package sirttas.elementalcraft.api.source.trait.value;

import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.registry.ElementalCraftRegistries;

public class SourceTraitValueProviderTypes {

	private static final DeferredRegister<SourceTraitValueProviderType<?>> DEFERRED_REGISTER = DeferredRegister.create(ElementalCraftRegistries.Keys.SOURCE_TRAIT_VALUE_PROVIDER_TYPE, ElementalCraftApi.MODID);

	public static final DeferredHolder<SourceTraitValueProviderType<?>, SourceTraitValueProviderType<FixedSourceTraitValueProvider>> FIXED = register(FixedSourceTraitValueProvider.NAME, FixedSourceTraitValueProvider.CODEC);
	public static final DeferredHolder<SourceTraitValueProviderType<?>, SourceTraitValueProviderType<LinearSourceTraitValueProvider>> LINEAR = register(LinearSourceTraitValueProvider.NAME, LinearSourceTraitValueProvider.CODEC);
	public static final DeferredHolder<SourceTraitValueProviderType<?>, SourceTraitValueProviderType<RangeBasedSourceTraitValueProvider>> RANGE_BASED = register(RangeBasedSourceTraitValueProvider.NAME, RangeBasedSourceTraitValueProvider.CODEC);
	public static final DeferredHolder<SourceTraitValueProviderType<?>, SourceTraitValueProviderType<StepsSourceTraitValueProvider>> STEPS = register(StepsSourceTraitValueProvider.NAME, StepsSourceTraitValueProvider.CODEC);
	public static final DeferredHolder<SourceTraitValueProviderType<?>, SourceTraitValueProviderType<ChanceSourceTraitValueProvider>> CHANCE = register(ChanceSourceTraitValueProvider.NAME, ChanceSourceTraitValueProvider.CODEC);
	public static final DeferredHolder<SourceTraitValueProviderType<?>, SourceTraitValueProviderType<PredicateSourceTraitValueProvider>> PREDICATE = register(PredicateSourceTraitValueProvider.NAME, PredicateSourceTraitValueProvider.CODEC);

	private SourceTraitValueProviderTypes() {}

	private static <T extends ISourceTraitValueProvider> DeferredHolder<SourceTraitValueProviderType<?>, SourceTraitValueProviderType<T>> register(String name, MapCodec<T> codec) {
		return DEFERRED_REGISTER.register(name, () -> new SourceTraitValueProviderType<>(codec));
	}

    @ApiStatus.Internal
	public static void register(IEventBus bus) {
		DEFERRED_REGISTER.register(bus);
	}
}
