package sirttas.elementalcraft.interaction.mekanism;

import mekanism.api.inventory.IgnoredIInventory;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.basic.BasicCrushingRecipe;
import mekanism.api.recipes.basic.BasicEnrichingRecipe;
import mekanism.api.recipes.basic.BasicInjectingRecipe;
import mekanism.api.recipes.basic.BasicPurifyingRecipe;
import mekanism.client.jei.MekanismJEI;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.RegisterEvent;
import sirttas.elementalcraft.api.pureore.factory.IPureOreRecipeFactory;
import sirttas.elementalcraft.api.pureore.factory.IPureOreRecipeFactoryType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.AbstractMillGrindstoneBlockEntity;
import sirttas.elementalcraft.interaction.mekanism.injector.ChemicalDissolutionPureOreRecipeFactory;
import sirttas.elementalcraft.interaction.mekanism.injector.ItemStackGasToItemStackPureOreRecipeFactory;
import sirttas.elementalcraft.interaction.mekanism.injector.ItemStackToItemStackPureOreRecipeFactory;
import sirttas.elementalcraft.interaction.mekanism.recipe.MekanismCrusherRecipeWrapper;
import sirttas.elementalcraft.pureore.injector.PureOreRecipeFactoryTypes;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

import java.util.function.BiFunction;

public class MekanismInteraction {

	private MekanismInteraction() {}
	
	public static void registerPureOreRecipeInjectors(RegisterEvent.RegisterHelper<IPureOreRecipeFactoryType<?, ? extends Recipe<?>>> registry) {
		register(registry, MekanismRecipeType.DISSOLUTION, ChemicalDissolutionPureOreRecipeFactory::new);
		register(registry, MekanismRecipeType.INJECTING, (m, t) -> new ItemStackGasToItemStackPureOreRecipeFactory<>(m, t, BasicInjectingRecipe::new));
		register(registry, MekanismRecipeType.PURIFYING, (m, t) -> new ItemStackGasToItemStackPureOreRecipeFactory<>(m, t, BasicPurifyingRecipe::new));
		register(registry, MekanismRecipeType.ENRICHING, (m, t) -> new ItemStackToItemStackPureOreRecipeFactory<>(m, t, BasicEnrichingRecipe::new));
		register(registry, MekanismRecipeType.CRUSHING, (m, t) -> new ItemStackToItemStackPureOreRecipeFactory<>(m, t, BasicCrushingRecipe::new));
	}

	private static <T extends MekanismRecipe, C extends IInputRecipeCache> void register(RegisterEvent.RegisterHelper<IPureOreRecipeFactoryType<?, ? extends Recipe<?>>> registry, RecipeTypeRegistryObject<T, C> type, BiFunction<RecipeManager,  RecipeTypeRegistryObject<T, C>, IPureOreRecipeFactory<IgnoredIInventory, T>> factory) {
		PureOreRecipeFactoryTypes.register(registry, type.getRegistryName(), m -> factory.apply(m, type));
	}

	public static void addAirMillToCrushing(IRecipeCatalystRegistration registry) {
		registry.addRecipeCatalyst(new ItemStack(ECBlocks.AIR_MILL_GRINDSTONE.get()), MekanismJEI.recipeType(MekanismJEIRecipeType.CRUSHING));
	}

	public static IGrindingRecipe lookupCrusherRecipe(Level level, AbstractMillGrindstoneBlockEntity millGrindstone) {
		var stack = millGrindstone.getInventory().getItem(0);
		var crusherRecipe = MekanismRecipeType.CRUSHING.findFirst(level, recipe -> recipe.test(stack));
		var wrapper = crusherRecipe != null ? new MekanismCrusherRecipeWrapper(crusherRecipe) : null;

		return wrapper != null && wrapper.matches(millGrindstone, level) ? wrapper : null;
	}
}
