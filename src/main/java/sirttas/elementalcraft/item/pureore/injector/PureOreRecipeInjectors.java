package sirttas.elementalcraft.item.pureore.injector;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.BlastingRecipe;
import net.minecraft.item.crafting.CampfireCookingRecipe;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.pureore.PureOreException;
import sirttas.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.interaction.ECinteractions;
import sirttas.elementalcraft.interaction.mekanism.MekanismInteraction;
import sirttas.elementalcraft.registry.RegistryHelper;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PureOreRecipeInjectors {

	private PureOreRecipeInjectors() {}
	
	@SubscribeEvent
	public static void registerPureOreRecipeInjectors(RegistryEvent.Register<AbstractPureOreRecipeInjector<?, ? extends IRecipe<?>>> event) {
		IForgeRegistry<AbstractPureOreRecipeInjector<?, ? extends IRecipe<?>>> registry = event.getRegistry();


		if (Boolean.TRUE.equals(ECConfig.COMMON.pureOreSmeltingRecipe.get())) {
			register(registry, new PureOreCookingRecipeInjector<>(IRecipeType.SMELTING, FurnaceRecipe::new));
		}
		if (Boolean.TRUE.equals(ECConfig.COMMON.pureOreBlastingRecipe.get())) {
			register(registry, new PureOreCookingRecipeInjector<>(IRecipeType.BLASTING, BlastingRecipe::new));
		}
		if (Boolean.TRUE.equals(ECConfig.COMMON.pureOreCampFireRecipe.get())) {
			register(registry, new PureOreCookingRecipeInjector<>(IRecipeType.CAMPFIRE_COOKING, CampfireCookingRecipe::new));
		}
		if (ECinteractions.isMekanismActive()) {
			MekanismInteraction.registerPureOreRecipeInjectors(registry);
		}
	}

	public static <C extends IInventory, T extends IRecipe<C>> void register(IForgeRegistry<AbstractPureOreRecipeInjector<?, ? extends IRecipe<?>>> registry, AbstractPureOreRecipeInjector<C, T> injector) {
		ResourceLocation id = injector.getRecipeTypeRegistryName();

		if (id == null) {
			throw new PureOreException("Cannot register injector as its RecipeType is absant in registry.");
		}
		RegistryHelper.register(registry, injector, ElementalCraft.createRL(id.getNamespace() + '_' + id.getPath()));
	}

}
