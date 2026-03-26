package sirttas.elementalcraft.interaction.jei.category.instrument;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.library.util.RecipeUtil;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.recipe.input.MultipleItemsSingleElementRecipeInput;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe;

import javax.annotation.Nonnull;

public class CrystallizationRecipeCategory extends AbstractInstrumentRecipeCategory<MultipleItemsSingleElementRecipeInput, CrystallizationRecipe> {

	private static final ItemStack CRYSTALLIZER = new ItemStack(ECBlocks.CRYSTALLIZER.get());

	private final ItemStack container = new ItemStack(ECBlocks.CONTAINER.get());

	public CrystallizationRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.crystallization", createDrawableStack(guiHelper, CRYSTALLIZER), 132, 110);
		addOverlay(guiHelper.createDrawable(ElementalCraftApi.createRL("textures/gui/overlay/crystallization.png"), 0, 0, 124, 52), 10, 10);
	}

	@Nonnull
	@Override
	public RecipeType<CrystallizationRecipe> getRecipeType() {
		return ECJEIRecipeTypes.CRYSTALLIZATION;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull CrystallizationRecipe recipe, @Nonnull IFocusGroup focuses) {
		var ingredients = recipe.getIngredients();

		builder.addSlot(RecipeIngredientRole.INPUT, 42, 32)
				.addIngredients(ingredients.get(0));
		builder.addSlot(RecipeIngredientRole.INPUT, 42, 14)
				.addIngredients(ingredients.get(1));

		builder.addSlot(RecipeIngredientRole.CATALYST, 42, 52)
				.addItemStack(CRYSTALLIZER);
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 42, 68)
				.addItemStack(container);

		builder.addSlot(RecipeIngredientRole.INPUT, 42, 86)
				.addIngredients(ECIngredientTypes.ELEMENT, getElementTypeIngredients(recipe));

		builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 42)
				.addItemStack(RecipeUtil.getResultItem(recipe));
	}

}
