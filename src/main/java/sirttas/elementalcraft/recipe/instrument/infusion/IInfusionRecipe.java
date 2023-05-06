package sirttas.elementalcraft.recipe.instrument.infusion;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import sirttas.elementalcraft.block.instrument.infuser.IInfuser;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.ISingleElementInstrumentRecipe;

import javax.annotation.Nonnull;

public interface IInfusionRecipe extends ISingleElementInstrumentRecipe<IInfuser> {

	String NAME = "infusion";

	@Override
	default boolean matches(IInfuser infuser) {
		ItemStack stack = infuser.getItem();
		
		return !stack.isEmpty() && infuser.getContainerElementType() == getElementType() && getInput().test(stack);
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
