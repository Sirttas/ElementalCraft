package sirttas.elementalcraft.interaction.jei.category.instrument.io;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import sirttas.elementalcraft.block.instrument.io.mill.AirMillBlockEntity;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

import javax.annotation.Nonnull;

public class GrindingRecipeCategory extends AbstractIOInstrumentRecipeCategory<AirMillBlockEntity, IGrindingRecipe> {

	public GrindingRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, "elementalcraft.jei.grinding", ECItems.AIR_MILL);
	}

	@Nonnull
	@Override
	public RecipeType<IGrindingRecipe> getRecipeType() {
		return ECJEIRecipeTypes.GRINDING;
	}
}
