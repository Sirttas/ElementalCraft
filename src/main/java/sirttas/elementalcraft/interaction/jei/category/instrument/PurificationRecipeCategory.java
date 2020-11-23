package sirttas.elementalcraft.interaction.jei.category.instrument;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.purifier.TilePurifier;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.instrument.PurifierRecipe;

public class PurificationRecipeCategory extends AbstractInstrumentRecipeCategory<TilePurifier, PurifierRecipe> {

	public static final ResourceLocation UID = ElementalCraft.createRL("purification");

	private final IDrawable icon;
	private final IDrawable background;
	private ItemStack purifier = new ItemStack(ECItems.purifier);


	public PurificationRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createBlankDrawable(75, 75);
		icon = guiHelper.createDrawableIngredient(purifier);
		setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/infusion_overlay.png"), 0, 0, 65, 16), 8, 20);
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends PurifierRecipe> getRecipeClass() {
		return PurifierRecipe.class;
	}

	@Override
	public String getTitle() {
		return I18n.format("elementalcraft.jei.purification");
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}


	@Override
	public void setRecipe(IRecipeLayout recipeLayout, PurifierRecipe recipe, IIngredients ingredients) {
		recipeLayout.getItemStacks().init(0, true, 0, 0);
		recipeLayout.getItemStacks().set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));

		recipeLayout.getItemStacks().init(1, false, 30, 40);
		recipeLayout.getItemStacks().set(1, tank);
		recipeLayout.getItemStacks().init(2, false, 30, 24);
		recipeLayout.getItemStacks().set(2, purifier);

		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).init(3, true, 31, 58);
		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).set(3, ingredients.getInputs(ECIngredientTypes.ELEMENT).get(0));

		recipeLayout.getItemStacks().init(4, false, 59, 0);
		recipeLayout.getItemStacks().set(4, ingredients.getOutputs(VanillaTypes.ITEM).get(0));

	}

}
