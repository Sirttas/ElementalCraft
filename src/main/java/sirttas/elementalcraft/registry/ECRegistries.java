package sirttas.elementalcraft.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;
import sirttas.elementalcraft.spell.Spell;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECRegistries {

	private static final int MIN_ID = 0;
	private static final int MAX_ID = Short.MAX_VALUE - 1;

	private ECRegistries() {}
	
	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public static void createECRegistry(RegistryEvent.NewRegistry event) {
		makeRegistry(ElementalCraft.createRL("spell"), Spell.class).setDefaultKey(ElementalCraft.createRL("none")).create();
		makeRegistry(ElementalCraft.createRL("pure_ore_recipe_injector"), AbstractPureOreRecipeInjector.class).create();
	}

	private static <T extends IForgeRegistryEntry<T>> RegistryBuilder<T> makeRegistry(ResourceLocation name, Class<T> clazz) {
		return new RegistryBuilder<T>().setName(name).setIDRange(MIN_ID, MAX_ID).setType(clazz);
	}
}
