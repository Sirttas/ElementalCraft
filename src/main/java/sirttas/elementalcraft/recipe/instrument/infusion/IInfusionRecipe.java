package sirttas.elementalcraft.recipe.instrument.infusion;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.input.SingleItemSingleElementRecipeInput;
import sirttas.elementalcraft.recipe.instrument.ISingleElementInstrumentRecipe;

import javax.annotation.Nonnull;

public interface IInfusionRecipe extends ISingleElementInstrumentRecipe<SingleItemSingleElementRecipeInput> {

	String NAME = "infusion";

	@Override
	default boolean matches(@Nonnull SingleItemSingleElementRecipeInput input, @Nonnull Level level) {
		var stack = input.getItem(0);
		
		return !stack.isEmpty() && input.getElementType() == getElementType() && getInput().test(stack);
	}

	@Nonnull
	@Override
	default RecipeType<?> getType() {
		return ECRecipeTypes.INFUSION.get();
	}
	
	Ingredient getInput();
	
	@Nonnull
	@Override
	default NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(Ingredient.EMPTY, getInput());
	}
}
