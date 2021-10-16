package sirttas.elementalcraft.block.source.trait.value;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.source.trait.value.SourceTraitValueProviderType;
import sirttas.elementalcraft.registry.RegistryHelper;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SourceTraitValueProviders {

	private SourceTraitValueProviders() {}
	
	@SubscribeEvent
	public static void registerBlockPosPredicateSerializers(RegistryEvent.Register<SourceTraitValueProviderType<?>> event) {
		IForgeRegistry<SourceTraitValueProviderType<?>> registry = event.getRegistry();

		RegistryHelper.register(registry, new SourceTraitValueProviderType<>(FixedSourceTraitValueProvider.CODEC), FixedSourceTraitValueProvider.NAME);
		RegistryHelper.register(registry, new SourceTraitValueProviderType<>(LinearSourceTraitValueProvider.CODEC), LinearSourceTraitValueProvider.NAME);
		RegistryHelper.register(registry, new SourceTraitValueProviderType<>(RangeBasedSourceTraitValueProvider.CODEC), RangeBasedSourceTraitValueProvider.NAME);
		RegistryHelper.register(registry, new SourceTraitValueProviderType<>(StepsSourceTraitValueProvider.CODEC), StepsSourceTraitValueProvider.NAME);
	}
}
