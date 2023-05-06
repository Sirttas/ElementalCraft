package sirttas.elementalcraft.interaction.jei.category.instrument.io;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.instrument.io.mill.saw.WaterMillWoodSawBlockEntity;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.io.sawing.SawingRecipe;

import javax.annotation.Nonnull;

public class SawingRecipeCategory extends AbstractIOInstrumentRecipeCategory<WaterMillWoodSawBlockEntity, SawingRecipe> {

	public SawingRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, "elementalcraft.jei.sawing", ECBlocks.WATER_MILL_WOOD_SAW.get());
	}

	@Nonnull
	@Override
	public RecipeType<SawingRecipe> getRecipeType() {
		return ECJEIRecipeTypes.SAWING;
	}
}
