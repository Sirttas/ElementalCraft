package sirttas.elementalcraft.interaction.jei.category.instrument.io;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.recipe.input.SingleItemSingleElementRecipeInput;
import sirttas.elementalcraft.recipe.instrument.infusion.InfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.ToolInfusionRecipe;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class InfusionRecipeCategory extends AbstractIOInstrumentRecipeCategory<SingleItemSingleElementRecipeInput, InfusionRecipe> {

	public InfusionRecipeCategory(IGuiHelper guiHelper) {
		this(guiHelper, "elementalcraft.jei.infusion");
	}

	protected InfusionRecipeCategory(IGuiHelper guiHelper, String translationKey) {
		super(guiHelper, translationKey, ECBlocks.INFUSER.get());
	}

	@Nonnull
	@Override
	public RecipeType<InfusionRecipe> getRecipeType() {
		return ECJEIRecipeTypes.INFUSION;
	}

	@Override
	protected List<ItemStack> getContainers() {
		return List.of(container, new ItemStack(ECBlocks.SMALL_CONTAINER.get()));
	}

	@Override
	@Nonnull
	protected List<ItemStack> getOutputs(@Nonnull InfusionRecipe recipe) {
		if (recipe instanceof ToolInfusionRecipe toolInfusionRecipe) {
			var infusion = toolInfusionRecipe.getToolInfusion();

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
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull InfusionRecipe recipe, @Nonnull IFocusGroup focuses) {
		var ingredients = recipe.getIngredients();
		var output = getOutputs(recipe);
		var input = ingredients.get(0);

		var inputSlot = builder.addSlot(RecipeIngredientRole.INPUT, 0, 0)
				.addIngredients(input);

		builder.addSlot(RecipeIngredientRole.CATALYST, 30, 24)
				.addItemStack(instrument);
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 30, 40)
				.addItemStacks(getContainers());
		builder.addSlot(RecipeIngredientRole.INPUT, 30, 58)
				.addIngredients(ECIngredientTypes.ELEMENT, getElementTypeIngredients(recipe));

		var outputSlot = builder.addSlot(RecipeIngredientRole.OUTPUT, 59, 0)
				.addItemStacks(output);

		if (recipe instanceof ToolInfusionRecipe && input.getItems().length == output.size()) {
			builder.createFocusLink(inputSlot, outputSlot);
		}
	}
}
