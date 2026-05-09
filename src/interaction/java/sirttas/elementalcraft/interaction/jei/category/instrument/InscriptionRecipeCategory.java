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
import sirttas.elementalcraft.recipe.instrument.inscription.InscriptionRecipe;
import sirttas.elementalcraft.recipe.instrument.inscription.InscriptionRecipeDisplay;

import javax.annotation.Nonnull;

public class InscriptionRecipeCategory extends AbstractInstrumentRecipeCategory<MultipleItemsSingleElementRecipeInput, InscriptionRecipe> {

	private static final ItemStack INSCRIBER = new ItemStack(ECBlocks.INSCRIBER.get());

	private final ItemStack container = new ItemStack(ECBlocks.CONTAINER.get());

	public InscriptionRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.inscription", createDrawableStack(guiHelper, INSCRIBER),100, 100);
		addOverlay(guiHelper.createDrawable(ElementalCraftApi.identifier("textures/gui/overlay/inscription.png"), 0, 0, 25, 12), 60, 20);
	}

	@Nonnull
	@Override
	public IRecipeType<@NotNull InscriptionRecipe> getRecipeType() {
		return ECJEIRecipeTypes.INSCRIPTION;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull InscriptionRecipe recipe, @Nonnull IFocusGroup focuses) {
        if (!(recipe.display().getFirst() instanceof InscriptionRecipeDisplay display)) {
            return;
        }

		var ingredients = display.ingredients();

		builder.addSlot(RecipeIngredientRole.INPUT, 22, 4)
				.add(ingredients.get(0));
		builder.addSlot(RecipeIngredientRole.INPUT, 6, 22)
				.add(ingredients.get(1));
		builder.addSlot(RecipeIngredientRole.INPUT, 22, 22)
				.add(ingredients.get(2));
		builder.addSlot(RecipeIngredientRole.INPUT, 38, 22)
				.add(ingredients.get(3));

		builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, 22, 42)
				.add(display.craftingStation());
		builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, 22, 58)
				.add(container);

		builder.addSlot(RecipeIngredientRole.INPUT, 23, 76)
				.addIngredients(ECIngredientTypes.ELEMENT, getElementTypeIngredients(recipe));

		builder.addSlot(RecipeIngredientRole.OUTPUT, 72, 34)
				.add(display.result());
	}
}
