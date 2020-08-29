package sirttas.elementalcraft.recipe;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.recipe.instrument.BinderRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.InfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.purification.PurifierRecipe;
import sirttas.elementalcraft.registry.RegistryHelper;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECRecipes {

	@SubscribeEvent
	public static void register(RegistryEvent.Register<IRecipeSerializer<?>> event) {
		IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();

		RegistryHelper.register(registry, new InfusionRecipe.Serializer(InfusionRecipe::new), InfusionRecipe.NAME);
		RegistryHelper.register(registry, new BinderRecipe.Serializer(BinderRecipe::new), BinderRecipe.NAME);
		RegistryHelper.register(registry, new PurifierRecipe.Serializer(PurifierRecipe::new), PurifierRecipe.NAME);
		RegistryHelper.register(registry, new PureInfusionRecipe.Serializer(PureInfusionRecipe::new), PureInfusionRecipe.NAME);
	}
}
