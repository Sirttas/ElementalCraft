package sirttas.elementalcraft.item.pureore.injector;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.pureore.PureOreException;
import sirttas.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.interaction.ECinteractions;
import sirttas.elementalcraft.registry.RegistryHelper;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PureOreRecipeInjectors {

	private PureOreRecipeInjectors() {}
	
	@SubscribeEvent
	public static void registerPureOreRecipeInjectors(RegistryEvent.Register<AbstractPureOreRecipeInjector<?, ? extends Recipe<?>>> event) {
		IForgeRegistry<AbstractPureOreRecipeInjector<?, ? extends Recipe<?>>> registry = event.getRegistry();


		if (Boolean.TRUE.equals(ECConfig.COMMON.pureOreSmeltingRecipe.get())) {
			register(registry, new PureOreCookingRecipeInjector<>(RecipeType.SMELTING, SmeltingRecipe::new));
		}
		if (Boolean.TRUE.equals(ECConfig.COMMON.pureOreBlastingRecipe.get())) {
			register(registry, new PureOreCookingRecipeInjector<>(RecipeType.BLASTING, BlastingRecipe::new));
		}
		if (Boolean.TRUE.equals(ECConfig.COMMON.pureOreCampFireRecipe.get())) {
			register(registry, new PureOreCookingRecipeInjector<>(RecipeType.CAMPFIRE_COOKING, CampfireCookingRecipe::new));
		}
		if (ECinteractions.isMekanismActive()) {
			// TODO MekanismInteraction.registerPureOreRecipeInjectors(registry);
		}
	}

	public static <C extends Container, T extends Recipe<C>> void register(IForgeRegistry<AbstractPureOreRecipeInjector<?, ? extends Recipe<?>>> registry, AbstractPureOreRecipeInjector<C, T> injector) {
		ResourceLocation id = injector.getRecipeTypeRegistryName();

		if (id == null) {
			throw new PureOreException("Cannot register injector as its RecipeType is absant in registry.");
		}
		RegistryHelper.register(registry, injector, ElementalCraft.createRL(id.getNamespace() + '_' + id.getPath()));
	}

}
