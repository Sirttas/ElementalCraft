package sirttas.elementalcraft.interaction.jei.category.instrument.io;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;

import javax.annotation.Nonnull;


public class ToolInfusionRecipeCategory extends InfusionRecipeCategory {

	public ToolInfusionRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, "elementalcraft.jei.tool_infusion");
	}

	@Nonnull
	@Override
	public RecipeType<IInfusionRecipe> getRecipeType() {
		return ECJEIRecipeTypes.TOOL_INFUSION;
	}
}
