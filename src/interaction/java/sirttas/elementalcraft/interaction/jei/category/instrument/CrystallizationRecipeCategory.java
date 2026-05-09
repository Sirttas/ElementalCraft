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
import sirttas.elementalcraft.recipe.instrument.crystallization.CrystallizationRecipe;
import sirttas.elementalcraft.recipe.instrument.crystallization.CrystallizationRecipeDisplay;

import javax.annotation.Nonnull;

public class CrystallizationRecipeCategory extends AbstractInstrumentRecipeCategory<MultipleItemsSingleElementRecipeInput, CrystallizationRecipe> {

	private static final ItemStack CRYSTALLIZER = new ItemStack(ECBlocks.CRYSTALLIZER.get());

	private final ItemStack container = new ItemStack(ECBlocks.CONTAINER.get());

	public CrystallizationRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.crystallization", createDrawableStack(guiHelper, CRYSTALLIZER), 132, 110);
		addOverlay(guiHelper.createDrawable(ElementalCraftApi.identifier("textures/gui/overlay/crystallization.png"), 0, 0, 124, 52), 10, 10);
	}

	@Nonnull
	@Override
	public IRecipeType<@NotNull CrystallizationRecipe> getRecipeType() {
		return ECJEIRecipeTypes.CRYSTALLIZATION;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull CrystallizationRecipe recipe, @Nonnull IFocusGroup focuses) {
        if (!(recipe.display().getFirst() instanceof CrystallizationRecipeDisplay display)) {
            return;
        }

		builder.addSlot(RecipeIngredientRole.INPUT, 42, 32)
				.add(display.gem());
		builder.addSlot(RecipeIngredientRole.INPUT, 42, 14)
				.add(display.crystal());

		builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, 42, 52)
				.add(display.craftingStation());
		builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, 42, 68)
				.add(container);

		builder.addSlot(RecipeIngredientRole.INPUT, 42, 86)
				.addIngredients(ECIngredientTypes.ELEMENT, getElementTypeIngredients(recipe));

		builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 42)
				.add(display.result());
	}

}
