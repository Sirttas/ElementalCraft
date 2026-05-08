package sirttas.elementalcraft.recipe.instrument.infusion;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.input.SingleItemSingleElementRecipeInput;
import sirttas.elementalcraft.recipe.instrument.SingleElementInstrumentRecipe;

public interface InfusionRecipe extends SingleElementInstrumentRecipe<SingleItemSingleElementRecipeInput> {

	String NAME = "infusion";

	@Override
	default boolean matches(SingleItemSingleElementRecipeInput input, Level level) {
		var stack = input.getItem(0);
		
		return !stack.isEmpty() && input.getElementType() == getElementType() && getInput().test(stack);
	}

	@Override
	default RecipeType<InfusionRecipe> getType() {
		return ECRecipeTypes.INFUSION.get();
	}
	
	Ingredient getInput();

    @Override
    default PlacementInfo placementInfo() {
        return PlacementInfo.create(getInput());
    }
}
