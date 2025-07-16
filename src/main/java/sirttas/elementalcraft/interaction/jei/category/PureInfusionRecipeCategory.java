package sirttas.elementalcraft.interaction.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.library.util.RecipeUtil;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.recipe.pure.infusion.PureInfusionRecipe;

import javax.annotation.Nonnull;

public class PureInfusionRecipeCategory extends AbstractECRecipeCategory<PureInfusionRecipe> {

	public PureInfusionRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.pureinfusion", createDrawableStack(guiHelper, new ItemStack(ECBlocks.PURE_INFUSER.get())), 177, 134);
		addOverlay(guiHelper.createDrawable(ElementalCraftApi.createRL("textures/gui/overlay/pureinfusion.png"), 0, 0, 142, 83), 27, 27);
	}

	@Nonnull
	@Override
	public RecipeType<PureInfusionRecipe> getRecipeType() {
		return ECJEIRecipeTypes.PURE_INFUSION;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull PureInfusionRecipe recipe, @Nonnull IFocusGroup focuses) {
		var ingredients = recipe.getIngredientsMap();
		var elementAmount = IngredientElementType.getGaugeValue(recipe.getElementAmount());

		builder.addSlot(RecipeIngredientRole.INPUT, 60, 61)
				.addIngredients(ingredients.get(ElementType.NONE));

		// Left
		builder.addSlot(RecipeIngredientRole.INPUT, 26, 61)
				.addIngredients(ingredients.get(ElementType.FIRE));
		builder.addSlot(RecipeIngredientRole.INPUT, 9, 61)
				.addIngredient(ECIngredientTypes.ELEMENT, new IngredientElementType(ElementType.FIRE, elementAmount));

		// Top
		builder.addSlot(RecipeIngredientRole.INPUT, 60, 27)
				.addIngredients(ingredients.get(ElementType.WATER));
		builder.addSlot(RecipeIngredientRole.INPUT, 60, 10)
				.addIngredient(ECIngredientTypes.ELEMENT, new IngredientElementType(ElementType.WATER, elementAmount));

		// Bottom
		builder.addSlot(RecipeIngredientRole.INPUT, 60, 95)
				.addIngredients(ingredients.get(ElementType.EARTH));
		builder.addSlot(RecipeIngredientRole.INPUT, 60, 112)
				.addIngredient(ECIngredientTypes.ELEMENT, new IngredientElementType(ElementType.EARTH, elementAmount));

		// Right
		builder.addSlot(RecipeIngredientRole.INPUT, 94, 61)
				.addIngredients(ingredients.get(ElementType.AIR));
		builder.addSlot(RecipeIngredientRole.INPUT, 111, 61)
				.addIngredient(ECIngredientTypes.ELEMENT, new IngredientElementType(ElementType.AIR, elementAmount));

		builder.addSlot(RecipeIngredientRole.OUTPUT, 154, 61)
				.addItemStack(RecipeUtil.getResultItem(recipe));
	}
}
