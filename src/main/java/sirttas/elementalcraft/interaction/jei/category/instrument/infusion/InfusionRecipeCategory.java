package sirttas.elementalcraft.interaction.jei.category.instrument.infusion;

import java.util.List;

import com.google.common.collect.Lists;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.infuser.TileInfuser;
import sirttas.elementalcraft.interaction.jei.category.instrument.AbstractInstrumentRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.instrument.infusion.AbstractInfusionRecipe;

public class InfusionRecipeCategory extends AbstractInstrumentRecipeCategory<TileInfuser, AbstractInfusionRecipe> {

	public static final ResourceLocation UID = ElementalCraft.createRL("infusion");

	private final IDrawable icon;
	private final IDrawable background;
	private ItemStack infuser = new ItemStack(ECItems.infuser);
	protected List<ItemStack> tanks = Lists.newArrayList(tank, new ItemStack(ECItems.tankSmall));


	public InfusionRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createBlankDrawable(75, 75);
		icon = guiHelper.createDrawableIngredient(infuser);
		setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/infusion_overlay.png"), 0, 0, 65, 16), 8, 20);
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends AbstractInfusionRecipe> getRecipeClass() {
		return AbstractInfusionRecipe.class;
	}

	@Override
	public String getTitle() {
		return I18n.format("elementalcraft.jei.infusion");
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, AbstractInfusionRecipe recipe, IIngredients ingredients) {
		recipeLayout.getItemStacks().init(0, true, 0, 0);
		recipeLayout.getItemStacks().set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));

		recipeLayout.getItemStacks().init(1, false, 30, 40);
		recipeLayout.getItemStacks().set(1, tanks);
		recipeLayout.getItemStacks().init(2, false, 30, 24);
		recipeLayout.getItemStacks().set(2, infuser);

		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).init(3, true, 31, 58);
		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).set(3, ingredients.getInputs(ECIngredientTypes.ELEMENT).get(0));

		recipeLayout.getItemStacks().init(4, false, 59, 0);
		recipeLayout.getItemStacks().set(4, ingredients.getOutputs(VanillaTypes.ITEM).get(0));

	}

}
