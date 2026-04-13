package sirttas.elementalcraft.interaction.jei.category.instrument.io;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.types.IRecipeType;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.infusion.InfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.ToolInfusionRecipe;

import javax.annotation.Nonnull;


public class ToolInfusionRecipeCategory extends InfusionRecipeCategory {

	public ToolInfusionRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, "elementalcraft.jei.tool_infusion");
	}

	@Nonnull
	@Override
	public IRecipeType<@NotNull InfusionRecipe> getRecipeType() {
		return ECJEIRecipeTypes.TOOL_INFUSION;
	}

    protected void createFocusLinks(@Nonnull IRecipeLayoutBuilder builder, @Nonnull InfusionRecipe recipe, Slots slots) {
        if (recipe instanceof ToolInfusionRecipe) {
            builder.createFocusLink(slots.input(), slots.output());
        }
    }
}
