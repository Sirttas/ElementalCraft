package sirttas.elementalcraft.interaction.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.SpellCraftRecipe;

import javax.annotation.Nonnull;

public class SpellCraftRecipeCategory extends AbstractInventoryRecipeCategory<Container, SpellCraftRecipe> {

	public SpellCraftRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.spell_craft", createDrawableStack(guiHelper, new ItemStack(ECItems.SPELL_DESK)), guiHelper.createBlankDrawable(123, 55));
		setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/overlay/spell_craft.png"), 0, 0, 103, 36), 10, 10);
	}

	@Nonnull
	@Override
	public RecipeType<SpellCraftRecipe> getRecipeType() {
		return ECJEIRecipeTypes.SPELL_CRAFTING;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull SpellCraftRecipe recipe, @Nonnull IFocusGroup focuses) {
		var ingredients = recipe.getIngredients();

		builder.addSlot(RecipeIngredientRole.INPUT, 20, 11)
				.addIngredients(ingredients.get(0));
		builder.addSlot(RecipeIngredientRole.INPUT, 11, 29)
				.addIngredients(ingredients.get(1));
		builder.addSlot(RecipeIngredientRole.INPUT, 29, 29)
				.addIngredients(ingredients.get(2));

		builder.addSlot(RecipeIngredientRole.OUTPUT, 96, 20)
				.addItemStack(recipe.getResultItem());
	}
}
