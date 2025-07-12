package sirttas.elementalcraft.interaction.jei.category.element.synthesis;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import mezz.jei.common.Constants;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;

import javax.annotation.Nonnull;
import java.util.List;

public class CombustionRecipeCategory extends AbstractECRecipeCategory<IJeiFuelingRecipe> {

	public static final String NAME = "combustion";

	private static final ItemStack COMBUSTION_SYNTHESIZER = new ItemStack(ECBlocks.COMBUSTION_SYNTHESIZER.get());
	private static final List<ItemStack> CONTAINERS = List.of(
			new ItemStack(ECBlocks.SMALL_CONTAINER.get()),
			new ItemStack(ECBlocks.CONTAINER.get()),
			new ItemStack(ECBlocks.FIRE_RESERVOIR.get()));

	public CombustionRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.combustion", createDrawableStack(guiHelper, COMBUSTION_SYNTHESIZER), guiHelper.createBlankDrawable(55, 63));
		addOverlay(guiHelper.createDrawable(ElementalCraftApi.createRL("textures/gui/overlay/combustion.png"), 0, 0, 38, 13), 0, 17);
		addOverlay(guiHelper.drawableBuilder(Constants.RECIPE_GUI_VANILLA, 82, 114, 14, 14).buildAnimated(100, IDrawableAnimated.StartDirection.TOP, true), 0, 17);
	}

	@Nonnull
	@Override
	public RecipeType<IJeiFuelingRecipe> getRecipeType() {
		return ECJEIRecipeTypes.COMBUSTION;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull IJeiFuelingRecipe recipe, @Nonnull IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 0, 0)
				.addItemStacks(recipe.getInputs());

		builder.addSlot(RecipeIngredientRole.CATALYST, 0, 31)
				.addItemStack(COMBUSTION_SYNTHESIZER);
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 0, 47)
				.addItemStacks(CONTAINERS);
		builder.addSlot(RecipeIngredientRole.OUTPUT, 39, 16)
				.addIngredient(ECIngredientTypes.ELEMENT, new IngredientElementType(ElementType.FIRE, IngredientElementType.getGaugeValue(recipe.getBurnTime())));
	}
}
