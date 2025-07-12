package sirttas.elementalcraft.interaction.jei.category.element.synthesis;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.interaction.jei.ingredient.source.IngredientSource;

import javax.annotation.Nonnull;
import java.util.List;

public class AirMillSynthesisRecipeCategory extends AbstractECRecipeCategory<IngredientElementType> {

	public static final String NAME = "air_mill_synthesis";

	private static final ItemStack AIR_MILL_SYNTHESIZER = new ItemStack(ECBlocks.AIR_MILL_SYNTHESIZER.get());
	private static final List<ItemStack> CONTAINERS = List.of(
			new ItemStack(ECBlocks.CONTAINER.get()),
			new ItemStack(ECBlocks.AIR_RESERVOIR.get()));

	public AirMillSynthesisRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.air_mill_synthesis", createDrawableStack(guiHelper, AIR_MILL_SYNTHESIZER), guiHelper.createBlankDrawable(64, 48));
		addOverlay(guiHelper.createDrawable(ElementalCraftApi.createRL("textures/gui/overlay/extraction.png"), 0, 0, 24, 9), 21, 35);
	}

	@Nonnull
	@Override
	public RecipeType<IngredientElementType> getRecipeType() {
		return ECJEIRecipeTypes.AIR_MILL_SYNTHESIS;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull IngredientElementType recipe, @Nonnull IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.CATALYST, 0, 16).addItemStack(AIR_MILL_SYNTHESIZER);
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 0, 32).addItemStacks(CONTAINERS);
		builder.addSlot(RecipeIngredientRole.OUTPUT, 47, 32).addIngredient(ECIngredientTypes.ELEMENT, recipe);
	}
}
