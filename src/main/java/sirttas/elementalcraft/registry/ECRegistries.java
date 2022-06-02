package sirttas.elementalcraft.registry;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.infusion.tool.effect.ToolInfusionEffectType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;
import sirttas.elementalcraft.api.source.trait.value.SourceTraitValueProviderType;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECRegistries {

	private ECRegistries() {}
	
	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public static void createECRegistry(NewRegistryEvent event) {
		event.create(makeRegistry(ECNames.PURE_ORE_RECIPE_INJECTOR, AbstractPureOreRecipeInjector.class));
		event.create(makeRegistry(ECNames.TOOL_INFUSION_TYPE, ToolInfusionEffectType.class));
		event.create(makeRegistry(ECNames.SOURCE_TRAIT_VALUE_PROVIDER_TYPE, SourceTraitValueProviderType.class));
	}

	private static <T extends IForgeRegistryEntry<T>> RegistryBuilder<T> makeRegistry(String name, Class<T> clazz) {
		return new RegistryBuilder<T>().setName(ElementalCraft.createRL(name)).setType(clazz);
	}

}
