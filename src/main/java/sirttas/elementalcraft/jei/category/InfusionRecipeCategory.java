package sirttas.elementalcraft.jei.category;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;

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
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.instrument.infusion.AbstractInfusionRecipe;

public class InfusionRecipeCategory extends AbstractInstrumentRecipeCategory<TileInfuser, AbstractInfusionRecipe> {

	public static final ResourceLocation UID = new ResourceLocation(ElementalCraft.MODID, "infusion");

	private final IDrawable icon;
	private final IDrawable overlay;
	private final IDrawable background;
	private ItemStack infuser = new ItemStack(ECItems.infuser).copy();
	protected List<ItemStack> tanks = Lists.newArrayList(tank, new ItemStack(ECItems.tankSmall).copy());


	public InfusionRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createBlankDrawable(75, 59);
		icon = guiHelper.createDrawableIngredient(infuser);
		overlay = guiHelper.createDrawable(new ResourceLocation(ElementalCraft.MODID, "textures/gui/infusion_overlay.png"), 0, 0, 65, 16);
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
	public void draw(AbstractInfusionRecipe recipe, double mouseX, double mouseY) {
		RenderSystem.enableAlphaTest();
		RenderSystem.enableBlend();
		overlay.draw(8, 20);
		renderElementGauge(31, 42, recipe);
		RenderSystem.disableBlend();
		RenderSystem.disableAlphaTest();
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, AbstractInfusionRecipe recipe, IIngredients ingredients) {
		recipeLayout.getItemStacks().init(0, true, 0, 0);
		recipeLayout.getItemStacks().set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));

		recipeLayout.getItemStacks().init(1, false, 30, 24);
		recipeLayout.getItemStacks().set(1, tanks);
		recipeLayout.getItemStacks().init(2, false, 30, 8);
		recipeLayout.getItemStacks().set(2, infuser);

		recipeLayout.getItemStacks().init(3, false, 59, 0);
		recipeLayout.getItemStacks().set(3, ingredients.getOutputs(VanillaTypes.ITEM).get(0));

	}

}
