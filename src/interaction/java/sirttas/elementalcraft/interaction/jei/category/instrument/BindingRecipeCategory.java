package sirttas.elementalcraft.interaction.jei.category.instrument;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.recipe.input.MultipleItemsSingleElementRecipeInput;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;
import sirttas.elementalcraft.recipe.instrument.binding.BinderRecipeDisplay;

import javax.annotation.Nonnull;

public class BindingRecipeCategory extends AbstractInstrumentRecipeCategory<MultipleItemsSingleElementRecipeInput, AbstractBindingRecipe> {

	private final ItemStack container = new ItemStack(ECBlocks.CONTAINER.get());
	private static final ItemStack BINDER = new ItemStack(ECBlocks.BINDER.get());
	private static final int RADIUS = 42;

	public BindingRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.binding", createDrawableStack(guiHelper, BINDER), RADIUS * 2 + 48, RADIUS * 2 + 16);
		addOverlay(guiHelper.createDrawable(ElementalCraftApi.createRL("textures/gui/overlay/binding.png"), 0, 0, 124, 83), 10, 10);
	}

	@Nonnull
	@Override
	public IRecipeType<@NotNull AbstractBindingRecipe> getRecipeType() {
		return ECJEIRecipeTypes.BINDING;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull AbstractBindingRecipe recipe, @Nonnull IFocusGroup focuses) {
        if (!(recipe.display().getFirst() instanceof BinderRecipeDisplay display)) {
            return;
        }

        int i = 0;
        int size = display.ingredients().size();

		for (var ingredient : display.ingredients()) {
			double angle = Math.toRadians(i / (double) size * 360D + 180);

			builder.addSlot(RecipeIngredientRole.INPUT, RADIUS + (int) (-RADIUS * Math.sin(angle)), RADIUS + (int) (RADIUS * Math.cos(angle)))
					.add(ingredient);
			i++;
		}

		builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, RADIUS, RADIUS - 16)
				.add(display.craftingStation());
		builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, RADIUS, RADIUS)
				.add(container);

		builder.addSlot(RecipeIngredientRole.INPUT, RADIUS, RADIUS + 18)
				.addIngredients(ECIngredientTypes.ELEMENT, getElementTypeIngredients(recipe));

		builder.addSlot(RecipeIngredientRole.OUTPUT, RADIUS * 2 + 32, RADIUS)
				.add(display.result());
	}
}
