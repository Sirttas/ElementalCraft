package sirttas.elementalcraft.interaction.jei.category.instrument.io;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.types.IRecipeType;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;
import sirttas.elementalcraft.recipe.instrument.io.purification.OrePurificationRecipe;

import javax.annotation.Nonnull;

public class PurificationRecipeCategory extends AbstractIOInstrumentRecipeCategory<SimpleIOInstrumentRecipeInput, OrePurificationRecipe> {

	public static final String NAME = "purification";

	public PurificationRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, "elementalcraft.jei.purification", ECBlocks.PURIFIER.get());
	}

	@Nonnull
	@Override
	public IRecipeType<@NotNull OrePurificationRecipe> getRecipeType() {
		return ECJEIRecipeTypes.ORE_PURIFICATION;
	}
}
