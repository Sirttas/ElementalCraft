package sirttas.elementalcraft.recipe.instrument.infusion;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.block.instrument.infuser.IInfuser;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

public interface IInfusionRecipe extends IInstrumentRecipe<IInfuser> {

	public static final String NAME = "infusion";
	public static final RecipeType<IInfusionRecipe> TYPE = Registry.register(Registry.RECIPE_TYPE, ElementalCraft.createRL(NAME), new RecipeType<IInfusionRecipe>() {
		@Override
		public String toString() {
			return NAME;
		}
	});

	@Override
	default boolean matches(IInfuser inv) {
		ItemStack stack = inv.getItem();
		ISingleElementStorage tank = inv.getTank();
		
		return !stack.isEmpty() && tank != null && inv.getTankElementType() == getElementType() && getInput().test(stack);
	}

	@Override
	default RecipeType<?> getType() {
		return TYPE;
	}
	
	Ingredient getInput();
	
	@Override
	default NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(Ingredient.EMPTY, getInput());
	}
}