package sirttas.elementalcraft.interaction.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.pureinfuser.PureInfuserBlockEntity;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.PureInfusionRecipe;

import javax.annotation.Nonnull;

public class PureInfusionRecipeCategory extends AbstractBlockEntityRecipeCategory<PureInfuserBlockEntity, PureInfusionRecipe> {

	public PureInfusionRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.pureinfusion", createDrawableStack(guiHelper, new ItemStack(ECItems.PURE_INFUSER)), guiHelper.createBlankDrawable(177, 134));
		setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/overlay/pureinfusion.png"), 0, 0, 142, 83), 27, 27);
	}

	@Nonnull
	@Override
	public RecipeType<PureInfusionRecipe> getRecipeType() {
		return ECJEIRecipeTypes.PURE_INFUSION;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull PureInfusionRecipe recipe, @Nonnull IFocusGroup focuses) {
		var ingredients = recipe.getIngredients();
		var elementIngredients = IngredientElementType.all(getGaugeValue(recipe.getElementAmount()));

		builder.addSlot(RecipeIngredientRole.INPUT, 60, 61)
				.addIngredients(ingredients.get(0));

		// Left
		builder.addSlot(RecipeIngredientRole.INPUT, 26, 61)
				.addIngredients(ingredients.get(1));
		builder.addSlot(RecipeIngredientRole.INPUT, 9, 61)
				.addIngredient(ECIngredientTypes.ELEMENT, elementIngredients.get(0));

		// Top
		builder.addSlot(RecipeIngredientRole.INPUT, 60, 27)
				.addIngredients(ingredients.get(2));
		builder.addSlot(RecipeIngredientRole.INPUT, 60, 10)
				.addIngredient(ECIngredientTypes.ELEMENT, elementIngredients.get(1));

		// Bottom
		builder.addSlot(RecipeIngredientRole.INPUT, 60, 95)
				.addIngredients(ingredients.get(3));
		builder.addSlot(RecipeIngredientRole.INPUT, 60, 112)
				.addIngredient(ECIngredientTypes.ELEMENT, elementIngredients.get(2));

		// Right
		builder.addSlot(RecipeIngredientRole.INPUT, 94, 61)
				.addIngredients(ingredients.get(4));
		builder.addSlot(RecipeIngredientRole.INPUT, 111, 61)
				.addIngredient(ECIngredientTypes.ELEMENT, elementIngredients.get(3));

		builder.addSlot(RecipeIngredientRole.OUTPUT, 154, 61)
				.addItemStack(recipe.getResultItem());
	}
}
