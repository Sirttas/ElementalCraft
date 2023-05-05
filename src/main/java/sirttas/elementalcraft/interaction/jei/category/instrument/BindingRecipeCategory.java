package sirttas.elementalcraft.interaction.jei.category.instrument;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.instrument.binder.IBinder;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;

import javax.annotation.Nonnull;

public class BindingRecipeCategory extends AbstractInstrumentRecipeCategory<IBinder, AbstractBindingRecipe> {

	private static final ItemStack BINDER = new ItemStack(ECBlocks.BINDER.get());
	private static final int RADIUS = 42;

	public BindingRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.binding", createDrawableStack(guiHelper, BINDER), guiHelper.createBlankDrawable(RADIUS * 2 + 48, RADIUS * 2 + 16));
		setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/overlay/binding.png"), 0, 0, 124, 83), 10, 10);
	}

	@Nonnull
	@Override
	public RecipeType<AbstractBindingRecipe> getRecipeType() {
		return ECJEIRecipeTypes.BINDING;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull AbstractBindingRecipe recipe, @Nonnull IFocusGroup focuses) {
		int i = 0;
		var ingredients = recipe.getIngredients();

		for (var ingredient : ingredients) {
			double a = Math.toRadians(i / (double) ingredients.size() * 360D + 180);

			builder.addSlot(RecipeIngredientRole.INPUT, RADIUS + (int) (-RADIUS * Math.sin(a)), RADIUS + (int) (RADIUS * Math.cos(a)))
					.addIngredients(ingredient);
			i++;
		}

		builder.addSlot(RecipeIngredientRole.CATALYST, RADIUS, RADIUS - 16)
				.addItemStack(BINDER);
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, RADIUS, RADIUS)
				.addItemStack(container);

		builder.addSlot(RecipeIngredientRole.INPUT, RADIUS, RADIUS + 18)
				.addIngredients(ECIngredientTypes.ELEMENT, getElementTypeIngredients(recipe));

		builder.addSlot(RecipeIngredientRole.OUTPUT, RADIUS * 2 + 32, RADIUS)
				.addItemStack(recipe.getResultItem());
	}
}
