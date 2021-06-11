package sirttas.elementalcraft.interaction.jei.category;

import java.util.List;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.pureinfuser.PureInfuserBlockEntity;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.PureInfusionRecipe;

public class PureInfusionRecipeCategory extends AbstractBlockEntityRecipeCategory<PureInfuserBlockEntity, PureInfusionRecipe> {

	public static final ResourceLocation UID = ElementalCraft.createRL(PureInfusionRecipe.NAME);

	public PureInfusionRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.pureinfusion", guiHelper.createDrawableIngredient(new ItemStack(ECItems.PURE_INFUSER)), guiHelper.createBlankDrawable(177, 134));
		setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/overlay/pureinfusion.png"), 0, 0, 142, 83), 27, 27);
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends PureInfusionRecipe> getRecipeClass() {
		return PureInfusionRecipe.class;
	}


	@Override
	public void setRecipe(IRecipeLayout recipeLayout, PureInfusionRecipe recipe, IIngredients ingredients) {
		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<List<IngredientElementType>> elementInputs = ingredients.getInputs(ECIngredientTypes.ELEMENT);

		recipeLayout.getItemStacks().init(0, true, 59, 60);
		recipeLayout.getItemStacks().set(0, inputs.get(0));
		recipeLayout.getItemStacks().init(1, true, 25, 60);
		recipeLayout.getItemStacks().set(1, inputs.get(1));
		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).init(1, true, 10, 61);
		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).set(1, elementInputs.get(0));
		recipeLayout.getItemStacks().init(2, true, 59, 26);
		recipeLayout.getItemStacks().set(2, inputs.get(2));
		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).init(2, true, 61, 10);
		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).set(2, elementInputs.get(1));
		recipeLayout.getItemStacks().init(3, true, 59, 94);
		recipeLayout.getItemStacks().set(3, inputs.get(3));
		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).init(3, true, 60, 111);
		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).set(3, elementInputs.get(2));
		recipeLayout.getItemStacks().init(4, true, 93, 60);
		recipeLayout.getItemStacks().set(4, inputs.get(4));
		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).init(4, true, 111, 61);
		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).set(4, elementInputs.get(3));
		recipeLayout.getItemStacks().init(5, false, 153, 60);
		recipeLayout.getItemStacks().set(5, ingredients.getOutputs(VanillaTypes.ITEM).get(0));

	}

	@Override
	public void setIngredients(PureInfusionRecipe recipe, IIngredients ingredients) {
		super.setIngredients(recipe, ingredients);
		ingredients.setInputs(ECIngredientTypes.ELEMENT, IngredientElementType.all(getGaugeValue(recipe.getElementAmount())));
	}

}
