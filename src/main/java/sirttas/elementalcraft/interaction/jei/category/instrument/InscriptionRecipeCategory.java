package sirttas.elementalcraft.interaction.jei.category.instrument;

import java.util.List;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.inscriber.TileInscriber;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.instrument.InscriptionRecipe;

public class InscriptionRecipeCategory extends AbstractInstrumentRecipeCategory<TileInscriber, InscriptionRecipe> {

	public static final ResourceLocation UID = ElementalCraft.createRL(InscriptionRecipe.NAME);

	private final IDrawable icon;
	private final IDrawable background;
	private ItemStack inscriber = new ItemStack(ECItems.INSCRIBER);

	public InscriptionRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createBlankDrawable(100, 100);
		icon = guiHelper.createDrawableIngredient(inscriber);
		setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/inscription_overlay.png"), 0, 0, 25, 12), 60, 20);
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends InscriptionRecipe> getRecipeClass() {
		return InscriptionRecipe.class;
	}

	@Override
	public String getTitle() {
		return I18n.format("elementalcraft.jei.inscription");
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
	public void setRecipe(IRecipeLayout recipeLayout, InscriptionRecipe recipe, IIngredients ingredients) {
		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);
		
		recipeLayout.getItemStacks().init(0, true, 22, 4);
		recipeLayout.getItemStacks().set(0, inputs.get(0));
		recipeLayout.getItemStacks().init(1, true, 6, 22);
		recipeLayout.getItemStacks().set(1, inputs.get(1));
		recipeLayout.getItemStacks().init(2, true, 22, 22);
		recipeLayout.getItemStacks().set(2, inputs.get(2));
		recipeLayout.getItemStacks().init(3, true, 38, 22);
		recipeLayout.getItemStacks().set(3, inputs.get(3));

		recipeLayout.getItemStacks().init(4, false, 22, 58);
		recipeLayout.getItemStacks().set(4, tank);
		recipeLayout.getItemStacks().init(5, false, 22, 42);
		recipeLayout.getItemStacks().set(5, inscriber);

		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).init(6, true, 23, 76);
		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).set(6, ingredients.getInputs(ECIngredientTypes.ELEMENT).get(0));

		if (!outputs.isEmpty()) {
			recipeLayout.getItemStacks().init(7, false, 72, 34);
			recipeLayout.getItemStacks().set(7, outputs.get(0));
		}

	}

}
