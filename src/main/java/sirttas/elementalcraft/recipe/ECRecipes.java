package sirttas.elementalcraft.recipe;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe;
import sirttas.elementalcraft.recipe.instrument.InscriptionRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.BindingRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.AbstractInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.InfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.AbstractGrindingRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.AirMillGrindingRecipe;
import sirttas.elementalcraft.registry.RegistryHelper;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECRecipes {

	private ECRecipes() {}
	
	@SubscribeEvent
	public static void register(RegistryEvent.Register<IRecipeSerializer<?>> event) {
		IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();

		RegistryHelper.register(registry, new InfusionRecipe.Serializer(), AbstractInfusionRecipe.NAME);
		RegistryHelper.register(registry, new BindingRecipe.Serializer(), AbstractBindingRecipe.NAME);
		RegistryHelper.register(registry, new CrystallizationRecipe.Serializer(), CrystallizationRecipe.NAME);
		RegistryHelper.register(registry, new InscriptionRecipe.Serializer(), InscriptionRecipe.NAME);
		RegistryHelper.register(registry, new AirMillGrindingRecipe.Serializer(), AbstractGrindingRecipe.NAME);
		RegistryHelper.register(registry, new PureInfusionRecipe.Serializer(), PureInfusionRecipe.NAME);
	}
}
