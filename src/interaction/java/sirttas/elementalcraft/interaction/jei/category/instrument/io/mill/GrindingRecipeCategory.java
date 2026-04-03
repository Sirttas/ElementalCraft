package sirttas.elementalcraft.interaction.jei.category.instrument.io.mill;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;
import sirttas.elementalcraft.recipe.instrument.io.grinding.GrindingRecipe;

import javax.annotation.Nonnull;

public class GrindingRecipeCategory extends AbstractMillRecipeCategory<SimpleIOInstrumentRecipeInput, GrindingRecipe> {

	public GrindingRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, "elementalcraft.jei.grinding", ECBlocks.WATER_MILL_GRINDSTONE.get(), ECBlocks.AIR_MILL_GRINDSTONE.get());
	}

	@Nonnull
	@Override
	public RecipeType<GrindingRecipe> getRecipeType() {
		return ECJEIRecipeTypes.GRINDING;
	}
}
