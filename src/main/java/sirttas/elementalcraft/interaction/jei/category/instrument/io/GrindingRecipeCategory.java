package sirttas.elementalcraft.interaction.jei.category.instrument.io;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.AbstractMillGrindstoneBlockEntity;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

import javax.annotation.Nonnull;

public class GrindingRecipeCategory extends AbstractIOInstrumentRecipeCategory<AbstractMillGrindstoneBlockEntity, IGrindingRecipe> {

	public GrindingRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, "elementalcraft.jei.grinding", ECBlocks.WATER_MILL_GRINDSTONE.get());
	}

	@Nonnull
	@Override
	public RecipeType<IGrindingRecipe> getRecipeType() {
		return ECJEIRecipeTypes.GRINDING;
	}
}
