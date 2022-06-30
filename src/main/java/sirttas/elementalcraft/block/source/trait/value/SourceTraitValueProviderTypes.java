package sirttas.elementalcraft.block.source.trait.value;

import com.mojang.serialization.Codec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValueProvider;
import sirttas.elementalcraft.api.source.trait.value.SourceTraitValueProviderType;

import java.util.function.Supplier;

public class SourceTraitValueProviderTypes {

	private static final DeferredRegister<SourceTraitValueProviderType<?>> DEFERRED_REGISTER = DeferredRegister.create(ElementalCraftApi.SOURCE_TRAIT_VALUE_PROVIDER_TYPE_REGISTRY_KEY, ElementalCraftApi.MODID);
	public static final Supplier<IForgeRegistry<SourceTraitValueProviderType<?>>> REGISTRY = DEFERRED_REGISTER.makeRegistry(RegistryBuilder::new);

	public static final RegistryObject<SourceTraitValueProviderType<FixedSourceTraitValueProvider>> FIXED = register(FixedSourceTraitValueProvider.CODEC, FixedSourceTraitValueProvider.NAME);
	public static final RegistryObject<SourceTraitValueProviderType<LinearSourceTraitValueProvider>> LINEAR = register(LinearSourceTraitValueProvider.CODEC, LinearSourceTraitValueProvider.NAME);
	public static final RegistryObject<SourceTraitValueProviderType<RangeBasedSourceTraitValueProvider>> RANGE_BASED = register(RangeBasedSourceTraitValueProvider.CODEC, RangeBasedSourceTraitValueProvider.NAME);
	public static final RegistryObject<SourceTraitValueProviderType<StepsSourceTraitValueProvider>> STEPS = register(StepsSourceTraitValueProvider.CODEC, StepsSourceTraitValueProvider.NAME);
	public static final RegistryObject<SourceTraitValueProviderType<ChanceSourceTraitValueProvider>> CHANCE = register(ChanceSourceTraitValueProvider.CODEC, ChanceSourceTraitValueProvider.NAME);
	public static final RegistryObject<SourceTraitValueProviderType<PredicateSourceTraitValueProvider>> PREDICATE = register(PredicateSourceTraitValueProvider.CODEC, PredicateSourceTraitValueProvider.NAME);


	private SourceTraitValueProviderTypes() {}

	private static <T extends ISourceTraitValueProvider> RegistryObject<SourceTraitValueProviderType<T>> register(Codec<T> codec, String name) {
		return DEFERRED_REGISTER.register(name, () -> new SourceTraitValueProviderType<>(codec));
	}

	public static void register(IEventBus bus) {
		DEFERRED_REGISTER.register(bus);
	}
}
