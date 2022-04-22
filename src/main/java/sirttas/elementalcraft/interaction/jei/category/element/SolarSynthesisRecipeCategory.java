package sirttas.elementalcraft.interaction.jei.category.element;

import com.google.common.collect.Lists;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.item.ECItems;

import javax.annotation.Nonnull;
import java.util.List;

public class SolarSynthesisRecipeCategory extends AbstractElementFromItemRecipeCategory {

	public static final String NAME = "solar_synthesis";

	private static final ItemStack SOLAR_SYNTHESIZER = new ItemStack(ECBlocks.SOLAR_SYNTHESIZER);

	protected final ItemStack tank = new ItemStack(ECItems.TANK);

	public SolarSynthesisRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.solar_synthesis", createDrawableStack(guiHelper, SOLAR_SYNTHESIZER), guiHelper.createBlankDrawable(84, 66));
		setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/overlay/solar_synthesis.png"), 0, 0, 49, 54), 8, 4);
	}

	@Nonnull
	@Override
	public RecipeType<Ingredient> getRecipeType() {
		return ECJEIRecipeTypes.SOLAR_SYNTHESIS;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull Ingredient ingredient, @Nonnull IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 15, 0)
				.addIngredients(ingredient);

		builder.addSlot(RecipeIngredientRole.CATALYST, 15, 31)
				.addItemStack(SOLAR_SYNTHESIZER);
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 15, 47)
				.addItemStack(tank);
		builder.addSlot(RecipeIngredientRole.OUTPUT, 60, 44)
				.addIngredient(ECIngredientTypes.ELEMENT, getOutput(ingredient));
	}

	public static List<Ingredient> getLenses() {
		return Lists.newArrayList(Ingredient.of(ECItems.FIRE_LENSE), Ingredient.of(ECItems.WATER_LENSE), Ingredient.of(ECItems.EARTH_LENSE), Ingredient.of(ECItems.AIR_LENSE));
	}

}
