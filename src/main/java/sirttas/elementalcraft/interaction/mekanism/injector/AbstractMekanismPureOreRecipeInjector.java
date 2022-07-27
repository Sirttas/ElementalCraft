package sirttas.elementalcraft.interaction.mekanism.injector;

import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.inventory.IgnoredIInventory;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.registration.impl.RecipeTypeRegistryObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import sirttas.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;

public abstract class AbstractMekanismPureOreRecipeInjector<T extends MekanismRecipe> extends AbstractPureOreRecipeInjector<IgnoredIInventory, T> {

	private final RecipeTypeRegistryObject<T, ?> recipeTypeRegistryObject;

	protected AbstractMekanismPureOreRecipeInjector(RecipeTypeRegistryObject<T, ?> recipeType) {
		super(recipeType::getRecipeType);
		this.recipeTypeRegistryObject = recipeType;
	}

	@Nonnull
	protected static ItemStackIngredient getInput(Ingredient ingredient, ItemStackIngredient old) {
		var representations = old.getRepresentations();

		if (!representations.isEmpty()) {
			return getInput(ingredient, (int) old.getNeededAmount(representations.get(0)));
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

	protected static ChemicalStackIngredient.GasStackIngredient tweakOutput(ChemicalStackIngredient.GasStackIngredient chemicalInput) {
		var gas = IngredientCreatorAccess.gas();

		var ingredients = chemicalInput.getRepresentations().stream()
				.map(s -> {
					var copy = s.copy();

					copy.setAmount(getOutputMultiplier(chemicalInput.getNeededAmount(s)));
					return gas.from(copy);
				})
				.toList();

		if (ingredients.isEmpty()) {
			return chemicalInput;
		} else if (ingredients.size() == 1) {
			return ingredients.get(0);
		}
		return gas.createMulti(ingredients.toArray(ChemicalStackIngredient.GasStackIngredient[]::new));
	}

	protected static ItemStack tweakOutput(ItemStack stack) {
		stack.setCount(getOutputMultiplier(stack.getCount()));
		return stack;
	}

	protected static <T extends Chemical<T>> ChemicalStack<T> tweakOutput(ChemicalStack<T> stack) {
		stack.setAmount(getOutputMultiplier(stack.getAmount()));
		return stack;
	}

	protected static int getInputMultiplier(long count) {
		return (int) Math.max(2, count * ECConfig.COMMON.mekanismPureOreInputMultiplier.get());
	}

	protected static int getOutputMultiplier(long count) {
		return (int) Math.max(2, count * ECConfig.COMMON.mekanismPureOreOutputMultiplier.get());
	}


	@Override
	public ResourceLocation getRecipeTypeRegistryName() {
		return new ResourceLocation("mekanism", recipeTypeRegistryObject.getInternalRegistryName());
	}

	@Override
	public boolean filter(T recipe, ItemStack stack) {
		return stack.is(ECTags.Items.PURE_ORES_ORE_SOURCE);
	}
}
