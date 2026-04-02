package sirttas.elementalcraft.recipe.instrument.infusion;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.input.SingleItemSingleElementRecipeInput;
import sirttas.elementalcraft.recipe.instrument.SingleElementInstrumentRecipe;

import javax.annotation.Nonnull;

public interface IInfusionRecipe extends SingleElementInstrumentRecipe<SingleItemSingleElementRecipeInput> {

	String NAME = "infusion";

	@Override
	default boolean matches(@Nonnull SingleItemSingleElementRecipeInput input, @Nonnull Level level) {
		var stack = input.getItem(0);
		
		return !stack.isEmpty() && input.getElementType() == getElementType() && getInput().test(stack);
	}

	@Nonnull
	@Override
	default RecipeType<@NotNull IInfusionRecipe> getType() {
		return ECRecipeTypes.INFUSION.get();
	}
	
	Ingredient getInput();

    @Override
    default @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.create(getInput());
    }
}
