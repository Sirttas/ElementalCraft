package sirttas.elementalcraft.interaction.jei.category.instrument.io;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.block.instrument.infuser.IInfuser;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.ToolInfusionRecipe;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class InfusionRecipeCategory extends AbstractIOInstrumentRecipeCategory<IInfuser, IInfusionRecipe> {

	public InfusionRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, "elementalcraft.jei.infusion", ECItems.INFUSER);
	}

	@Nonnull
	@Override
	public RecipeType<IInfusionRecipe> getRecipeType() {
		return ECJEIRecipeTypes.INFUSION;
	}

	@Override
	protected List<ItemStack> getTanks() {
		return List.of(tank, new ItemStack(ECItems.TANK_SMALL));
	}

	@Override
	@Nonnull
	protected List<ItemStack> getOutputs(@Nonnull IInfusionRecipe recipe) {
		if (recipe instanceof ToolInfusionRecipe toolInfusionRecipe) {
			ToolInfusion infusion = toolInfusionRecipe.getToolInfusion();

			return recipe.getIngredients().stream()
					.flatMap(i -> Arrays.stream(i.getItems())
							.map(stack -> {
								ItemStack copy = stack.copy();

								ToolInfusionHelper.setInfusion(copy, infusion);
								return copy;
							}))
					.toList();
		}
		return super.getOutputs(recipe);
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull IInfusionRecipe recipe, @Nonnull IFocusGroup focuses) {
		var ingredients = recipe.getIngredients();
		var output = getOutputs(recipe);

		var inputSlot = builder.addSlot(RecipeIngredientRole.INPUT, 0, 0)
				.addIngredients(ingredients.get(0));

		builder.addSlot(RecipeIngredientRole.CATALYST, 30, 24)
				.addItemStack(instrument);
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 30, 40)
				.addItemStacks(getTanks());
		builder.addSlot(RecipeIngredientRole.INPUT, 30, 58)
				.addIngredient(ECIngredientTypes.ELEMENT, getElementTypeIngredient(recipe));

		var outputSlot = builder.addSlot(RecipeIngredientRole.OUTPUT, 59, 0)
				.addItemStacks(output);

		if (recipe instanceof ToolInfusionRecipe && ingredients.get(0).getItems().length == output.size()) {
			builder.createFocusLink(inputSlot, outputSlot);
		}
	}
}
