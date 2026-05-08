package sirttas.elementalcraft.interaction.mekanism.injector;

import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import sirttas.elementalcraft.api.pureore.factory.AbstractPureOreRecipeFactory;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;

public abstract class AbstractMekanismPureOreRecipeFactory<I extends RecipeInput, T extends MekanismRecipe<I>> extends AbstractPureOreRecipeFactory<I, T> {


	protected AbstractMekanismPureOreRecipeFactory(@Nonnull RecipeManager recipeManager, @Nonnull RecipeTypeRegistryObject<I, T, ?> recipeType) {
		super(recipeManager, recipeType.getRecipeType());
	}

	@Nonnull
	protected static ItemStackIngredient getInput(Ingredient ingredient, ItemStackIngredient old) {
		var representations = old.getRepresentations();

		if (!representations.isEmpty()) {
			return getInput(ingredient, (int) old.getNeededAmount(representations.getFirst()));
		}
		return getInput(ingredient);
	}

	@Nonnull
	protected static ItemStackIngredient getInput(Ingredient ingredient) {
		return IngredientCreatorAccess.item().from(ingredient, getInputMultiplier(1));
	}

	@Nonnull
	protected static ItemStackIngredient getInput(Ingredient ingredient, int size) {
		return IngredientCreatorAccess.item().from(ingredient, getInputMultiplier(size));
	}

	protected static ChemicalStackIngredient tweakOutput(ChemicalStackIngredient chemicalInput) {
		return IngredientCreatorAccess.chemicalStack().from(chemicalInput.ingredient(), getOutputMultiplier(chemicalInput.amount()));
	}

	protected static ItemStack tweakOutput(ItemStack stack) {
		var copy = stack.copy();

		copy.setCount(getOutputMultiplier(stack.getCount()));
		return copy;
	}

	protected static ChemicalStack tweakOutput(ChemicalStack stack) {
		var copy = stack.copy();

		copy.setAmount(getOutputMultiplier(stack.getAmount()));
		return copy;
	}

	protected static int getInputMultiplier(long count) {
		return (int) Math.max(2, count * ECConfig.SERVER.mekanismPureOreInputMultiplier.get());
	}

	protected static int getOutputMultiplier(long count) {
		return (int) Math.max(2, count * ECConfig.SERVER.mekanismPureOreOutputMultiplier.get());
	}

	@Override
	public boolean filter(RecipeHolder<T> recipe, ItemStack stack) {
		return stack.is(ECTags.Items.PURE_ORES_SOURCES_ORES); // TODO use a specific tag
	}
}
