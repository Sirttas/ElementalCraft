package sirttas.elementalcraft.registry;

import net.minecraft.resources.ResourceLocation;
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
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.spell.Spell;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECRegistries {

	private static final int MIN_ID = 0;
	private static final int MAX_ID = Short.MAX_VALUE - 1;

	private ECRegistries() {}
	
	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public static void createECRegistry(NewRegistryEvent event) {
		event.create(makeRegistry(ElementalCraft.createRL("spell"), Spell.class).setDefaultKey(ElementalCraft.createRL("none")));
		event.create(makeRegistry(ElementalCraft.createRL("pure_ore_recipe_injector"), AbstractPureOreRecipeInjector.class));
		event.create(makeRegistry(ElementalCraft.createRL("tool_infusion_type"), ToolInfusionEffectType.class));
		event.create(makeRegistry(ElementalCraft.createRL("source_trait_value_provider_type"), SourceTraitValueProviderType.class));
		event.create(makeRegistry(ElementalCraft.createRL(ECNames.JEWEL), Jewel.class));
	}

	private static <T extends IForgeRegistryEntry<T>> RegistryBuilder<T> makeRegistry(ResourceLocation name, Class<T> clazz) {
		return new RegistryBuilder<T>().setName(name).setIDRange(MIN_ID, MAX_ID).setType(clazz);
	}

}
