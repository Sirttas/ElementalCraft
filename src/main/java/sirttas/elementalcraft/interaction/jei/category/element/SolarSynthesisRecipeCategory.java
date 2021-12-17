package sirttas.elementalcraft.interaction.jei.category.element;

import java.util.List;

import com.google.common.collect.Lists;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.item.ECItems;

import javax.annotation.Nonnull;

public class SolarSynthesisRecipeCategory extends AbstractElementFromItemRecipeCategory {

	public static final ResourceLocation UID = ElementalCraft.createRL("solar_synthesis");

	private static final ItemStack SOLAR_SYNTHESIZER = new ItemStack(ECBlocks.SOLAR_SYNTHESIZER);

	protected final ItemStack tank = new ItemStack(ECItems.TANK);

	public SolarSynthesisRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.solar_synthesis", guiHelper.createDrawableIngredient(SOLAR_SYNTHESIZER), guiHelper.createBlankDrawable(84, 66));
		setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/overlay/solar_synthesis.png"), 0, 0, 49, 54), 8, 4);
	}


	@Nonnull
    @Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, @Nonnull Ingredient recipe, IIngredients ingredients) {
		recipeLayout.getItemStacks().init(0, true, 15, 0);
		recipeLayout.getItemStacks().set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));

		recipeLayout.getItemStacks().init(1, false, 15, 47);
		recipeLayout.getItemStacks().set(1, tank);
		recipeLayout.getItemStacks().init(2, false, 15, 31);
		recipeLayout.getItemStacks().set(2, SOLAR_SYNTHESIZER);

		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).init(2, true, 60, 44);
		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).set(2, ingredients.getOutputs(ECIngredientTypes.ELEMENT).get(0));

	}

	public static List<Ingredient> getLenses() {
		return Lists.newArrayList(Ingredient.of(ECItems.FIRE_LENSE), Ingredient.of(ECItems.WATER_LENSE), Ingredient.of(ECItems.EARTH_LENSE), Ingredient.of(ECItems.AIR_LENSE));
	}

}
