package sirttas.elementalcraft.interaction.mekanism;

import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.client.jei.MekanismJEI;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.impl.CrushingIRecipe;
import mekanism.common.recipe.impl.EnrichingIRecipe;
import mekanism.common.recipe.impl.InjectingIRecipe;
import mekanism.common.recipe.impl.PurifyingIRecipe;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.IForgeRegistry;
import sirttas.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.mekanism.injector.ChemicalDissolutionPureOreRecipeInjector;
import sirttas.elementalcraft.interaction.mekanism.injector.ItemStackGasToItemStackPureOreRecipeInjector;
import sirttas.elementalcraft.interaction.mekanism.injector.ItemStackToItemStackPureOreRecipeInjector;
import sirttas.elementalcraft.interaction.mekanism.recipe.MekanismCrusherRecipeWrapper;
import sirttas.elementalcraft.pureore.injector.PureOreRecipeInjectors;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

public class MekanismInteraction {

	private MekanismInteraction() {}
	
	public static void registerPureOreRecipeInjectors(IForgeRegistry<AbstractPureOreRecipeInjector<?, ? extends Recipe<?>>> registry) {
		PureOreRecipeInjectors.register(registry, new ChemicalDissolutionPureOreRecipeInjector(MekanismRecipeType.DISSOLUTION));
		PureOreRecipeInjectors.register(registry, new ItemStackGasToItemStackPureOreRecipeInjector<>(MekanismRecipeType.INJECTING, InjectingIRecipe::new));
		PureOreRecipeInjectors.register(registry, new ItemStackGasToItemStackPureOreRecipeInjector<>(MekanismRecipeType.PURIFYING, PurifyingIRecipe::new));
		PureOreRecipeInjectors.register(registry, new ItemStackToItemStackPureOreRecipeInjector<>(MekanismRecipeType.ENRICHING, EnrichingIRecipe::new));
		PureOreRecipeInjectors.register(registry, new ItemStackToItemStackPureOreRecipeInjector<>(MekanismRecipeType.CRUSHING, CrushingIRecipe::new));
	}

	public static void addAirMillToCrushing(IRecipeCatalystRegistration registry) {
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.AIR_MILL.get()), MekanismJEI.recipeType(MekanismJEIRecipeType.CRUSHING));
	}

	public static IGrindingRecipe lookupCrusherRecipe(Level world, Container inv) {
		ItemStackToItemStackRecipe crusherRecipe = MekanismRecipeType.CRUSHING.findFirst(world, recipe -> recipe.test(inv.getItem(0)));
		
		return crusherRecipe != null ? new MekanismCrusherRecipeWrapper(crusherRecipe) : null;
	}
}
