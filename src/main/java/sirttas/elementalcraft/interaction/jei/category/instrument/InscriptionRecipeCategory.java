package sirttas.elementalcraft.interaction.jei.category.instrument;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.instrument.inscriber.InscriberBlockEntity;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.recipe.instrument.InscriptionRecipe;

import javax.annotation.Nonnull;

public class InscriptionRecipeCategory extends AbstractInstrumentRecipeCategory<InscriberBlockEntity, InscriptionRecipe> {

	private static final ItemStack INSCRIBER = new ItemStack(ECBlocks.INSCRIBER.get());

	public InscriptionRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.inscription", createDrawableStack(guiHelper, INSCRIBER), guiHelper.createBlankDrawable(100, 100));
		setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/overlay/inscription.png"), 0, 0, 25, 12), 60, 20);
	}

	@Nonnull
	@Override
	public RecipeType<InscriptionRecipe> getRecipeType() {
		return ECJEIRecipeTypes.INSCRIPTION;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull InscriptionRecipe recipe, @Nonnull IFocusGroup focuses) {
		var ingredients = recipe.getIngredients();

		builder.addSlot(RecipeIngredientRole.INPUT, 22, 4)
				.addIngredients(ingredients.get(0));
		builder.addSlot(RecipeIngredientRole.INPUT, 6, 22)
				.addIngredients(ingredients.get(1));
		builder.addSlot(RecipeIngredientRole.INPUT, 22, 22)
				.addIngredients(ingredients.get(2));
		builder.addSlot(RecipeIngredientRole.INPUT, 38, 22)
				.addIngredients(ingredients.get(3));

		builder.addSlot(RecipeIngredientRole.CATALYST, 22, 42)
				.addItemStack(INSCRIBER);
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 22, 58)
				.addItemStack(container);

		builder.addSlot(RecipeIngredientRole.INPUT, 23, 76)
				.addIngredients(ECIngredientTypes.ELEMENT, getElementTypeIngredients(recipe));

		builder.addSlot(RecipeIngredientRole.OUTPUT, 72, 34)
				.addItemStack(recipe.getResultItem());
	}
}
