package sirttas.elementalcraft.interaction.jei.category.instrument;

import java.util.List;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.binder.IBinder;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;

public class BindingRecipeCategory extends AbstractInstrumentRecipeCategory<IBinder, AbstractBindingRecipe> {

	public static final ResourceLocation UID = ElementalCraft.createRL(AbstractBindingRecipe.NAME);
	
	private static final ItemStack BINDER = new ItemStack(ECItems.BINDER);
	private static final int RADIUS = 42;


	public BindingRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.binding", guiHelper.createDrawableIngredient(BINDER), guiHelper.createBlankDrawable(RADIUS * 2 + 48, RADIUS * 2 + 16));
		setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/overlay/binding.png"), 0, 0, 124, 83), 10, 10);
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<AbstractBindingRecipe> getRecipeClass() {
		return AbstractBindingRecipe.class;
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, AbstractBindingRecipe recipe, IIngredients ingredients) {
		int i = 0;
		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);

		for (List<ItemStack> input : inputs) {
			double a = Math.toRadians((double) i / (double) inputs.size() * 360D + 180);

			recipeLayout.getItemStacks().init(i, true, RADIUS + (int) (-RADIUS * Math.sin(a)), RADIUS + (int) (RADIUS * Math.cos(a)));
			recipeLayout.getItemStacks().set(i, input);
			i++;
		}
		recipeLayout.getItemStacks().init(i, false, RADIUS, RADIUS);
		recipeLayout.getItemStacks().set(i, tank);
		recipeLayout.getItemStacks().init(i + 1, false, RADIUS, RADIUS - 16);
		recipeLayout.getItemStacks().set(i + 1, BINDER);

		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).init(i + 2, true, RADIUS + 1, RADIUS + 18);
		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).set(i + 2, ingredients.getInputs(ECIngredientTypes.ELEMENT).get(0));

		recipeLayout.getItemStacks().init(i + 3, false, RADIUS * 2 + 32, RADIUS);
		recipeLayout.getItemStacks().set(i + 3, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
	}

}
