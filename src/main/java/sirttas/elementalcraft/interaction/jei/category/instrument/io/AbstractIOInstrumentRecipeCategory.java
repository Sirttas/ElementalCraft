package sirttas.elementalcraft.interaction.jei.category.instrument.io;

import java.util.List;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.IInstrument;
import sirttas.elementalcraft.interaction.jei.category.instrument.AbstractInstrumentRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

public abstract class AbstractIOInstrumentRecipeCategory<K extends IInstrument, T extends IInstrumentRecipe<K>> extends AbstractInstrumentRecipeCategory<K, T> {
	
	private final IDrawable icon;
	private final IDrawable background;
	private final ItemStack instrument;

	protected AbstractIOInstrumentRecipeCategory(IGuiHelper guiHelper, ItemStack instrument) {
		this.instrument = instrument;
		background = guiHelper.createBlankDrawable(75, 75);
		icon = guiHelper.createDrawableIngredient(instrument);
		setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/infusion_overlay.png"), 0, 0, 65, 16), 8, 20);
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	protected List<ItemStack> getTanks() {
		return ImmutableList.of(tank);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, T recipe, IIngredients ingredients) {
		recipeLayout.getItemStacks().init(0, true, 0, 0);
		recipeLayout.getItemStacks().set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));

		recipeLayout.getItemStacks().init(1, false, 30, 40);
		recipeLayout.getItemStacks().set(1, getTanks());
		recipeLayout.getItemStacks().init(2, false, 30, 24);
		recipeLayout.getItemStacks().set(2, instrument);

		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).init(3, true, 31, 58);
		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).set(3, ingredients.getInputs(ECIngredientTypes.ELEMENT).get(0));

		recipeLayout.getItemStacks().init(4, false, 59, 0);
		recipeLayout.getItemStacks().set(4, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
	}
}
