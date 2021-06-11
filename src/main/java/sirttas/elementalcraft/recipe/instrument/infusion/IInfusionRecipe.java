package sirttas.elementalcraft.recipe.instrument.infusion;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.registry.Registry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.block.instrument.infuser.IInfuser;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

public interface IInfusionRecipe extends IInstrumentRecipe<IInfuser> {

	public static final String NAME = "infusion";
	public static final IRecipeType<IInfusionRecipe> TYPE = Registry.register(Registry.RECIPE_TYPE, ElementalCraft.createRL(NAME), new IRecipeType<IInfusionRecipe>() {
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
	default IRecipeType<?> getType() {
		return TYPE;
	}
	
	Ingredient getInput();
	
	@Override
	default NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(Ingredient.EMPTY, getInput());
	}
}