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
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.tag.ECTags;

public class EvaporationRecipeCategory extends AbstractElementFromItemRecipeCategory {

	public static final ResourceLocation UID = ElementalCraft.createRL("evaporation");

	protected static final List<ItemStack> TANKS = Lists.newArrayList(new ItemStack(ECItems.TANK), new ItemStack(ECItems.TANK_SMALL));
	private static final ItemStack EVAPORATOR = new ItemStack(ECItems.EVAPORATOR);

	public EvaporationRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.evaporation", guiHelper.createDrawableIngredient(EVAPORATOR), guiHelper.createBlankDrawable(99, 59));
		setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/overlay/evaporation.png"), 0, 0, 64, 29), 8, 20);
	}


	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, Ingredient recipe, IIngredients ingredients) {
		recipeLayout.getItemStacks().init(0, true, 0, 0);
		recipeLayout.getItemStacks().set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));

		recipeLayout.getItemStacks().init(1, false, 30, 40);
		recipeLayout.getItemStacks().set(1, TANKS);
		recipeLayout.getItemStacks().init(2, false, 30, 24);
		recipeLayout.getItemStacks().set(2, EVAPORATOR);

		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).init(2, true, 75, 36);
		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).set(2, ingredients.getOutputs(ECIngredientTypes.ELEMENT).get(0));

	}

	public static List<Ingredient> getShards() {
		return Lists.newArrayList(Ingredient.of(ECTags.Items.FIRE_SHARDS), Ingredient.of(ECTags.Items.WATER_SHARDS), Ingredient.of(ECTags.Items.EARTH_SHARDS),
				Ingredient.of(ECTags.Items.AIR_SHARDS));
	}

}
