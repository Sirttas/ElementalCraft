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
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.List;

public class EvaporationRecipeCategory extends AbstractElementFromItemRecipeCategory {

	public static final String NAME = "evaporation";

	protected static final List<ItemStack> TANKS = Lists.newArrayList(new ItemStack(ECItems.TANK), new ItemStack(ECItems.TANK_SMALL));
	private static final ItemStack EVAPORATOR = new ItemStack(ECItems.EVAPORATOR);

	public EvaporationRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.evaporation", createDrawableStack(guiHelper, EVAPORATOR), guiHelper.createBlankDrawable(99, 59));
		setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/overlay/evaporation.png"), 0, 0, 64, 29), 8, 20);
	}

	@Nonnull
	@Override
	public RecipeType<Ingredient> getRecipeType() {
		return ECJEIRecipeTypes.EVAPORATION;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull Ingredient ingredient, @Nonnull IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 0, 0)
				.addIngredients(ingredient);

		builder.addSlot(RecipeIngredientRole.CATALYST, 30, 24)
				.addItemStack(EVAPORATOR);
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 30, 40)
				.addItemStacks(TANKS);
		builder.addSlot(RecipeIngredientRole.OUTPUT, 75, 36)
				.addIngredient(ECIngredientTypes.ELEMENT, getOutput(ingredient));
	}

	public static List<Ingredient> getShards() {
		return Lists.newArrayList(Ingredient.of(ECTags.Items.FIRE_SHARDS), Ingredient.of(ECTags.Items.WATER_SHARDS), Ingredient.of(ECTags.Items.EARTH_SHARDS),
				Ingredient.of(ECTags.Items.AIR_SHARDS));
	}

}
