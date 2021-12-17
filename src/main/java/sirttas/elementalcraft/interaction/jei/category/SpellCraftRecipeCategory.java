package sirttas.elementalcraft.interaction.jei.category;

import java.util.List;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.SpellCraftRecipe;

import javax.annotation.Nonnull;

public class SpellCraftRecipeCategory extends AbstractInventoryRecipeCategory<Container, SpellCraftRecipe> {

	public static final ResourceLocation UID = ElementalCraft.createRL(SpellCraftRecipe.NAME);

	public SpellCraftRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.spell_craft", guiHelper.createDrawableIngredient(new ItemStack(ECItems.SPELL_DESK)), guiHelper.createBlankDrawable(123, 55));
		setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/overlay/spell_craft.png"), 0, 0, 103, 36), 10, 10);
	}

	@Nonnull
    @Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
    @Override
	public Class<? extends SpellCraftRecipe> getRecipeClass() {
		return SpellCraftRecipe.class;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, @Nonnull SpellCraftRecipe recipe, IIngredients ingredients) {
		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);

		recipeLayout.getItemStacks().init(0, true, 19, 10);
		recipeLayout.getItemStacks().set(0, inputs.get(0));
		recipeLayout.getItemStacks().init(1, true, 10, 28);
		recipeLayout.getItemStacks().set(1, inputs.get(1));
		recipeLayout.getItemStacks().init(2, true, 28, 28);
		recipeLayout.getItemStacks().set(2, inputs.get(2));
		recipeLayout.getItemStacks().init(3, false, 95, 19);
		recipeLayout.getItemStacks().set(3, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
	}
}
