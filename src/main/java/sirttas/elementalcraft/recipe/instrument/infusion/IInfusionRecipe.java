package sirttas.elementalcraft.recipe.instrument.infusion;

import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.infuser.IInfuser;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

import javax.annotation.Nonnull;

public interface IInfusionRecipe extends IInstrumentRecipe<IInfuser> {

	String NAME = "infusion";
	RecipeType<IInfusionRecipe> TYPE = Registry.register(Registry.RECIPE_TYPE, ElementalCraft.createRL(NAME), new RecipeType<IInfusionRecipe>() {
		@Override
		public String toString() {
			return NAME;
		}
	});

	@Override
	default boolean matches(IInfuser infuser) {
		ItemStack stack = infuser.getItem();
		
		return !stack.isEmpty() && infuser.getContainerElementType() == getElementType() && getInput().test(stack);
	}

	@Nonnull
	@Override
	default RecipeType<?> getType() {
		return TYPE;
	}
	
	Ingredient getInput();
	
	@Nonnull
	@Override
	default NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(Ingredient.EMPTY, getInput());
	}
}
