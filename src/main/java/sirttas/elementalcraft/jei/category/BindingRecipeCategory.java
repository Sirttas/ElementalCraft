package sirttas.elementalcraft.jei.category;

import java.util.List;

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
import sirttas.elementalcraft.block.instrument.binder.TileBinder;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.instrument.BinderRecipe;

public class BindingRecipeCategory extends AbstractInstrumentRecipeCategory<TileBinder, BinderRecipe> {

	public static final ResourceLocation UID = new ResourceLocation(ElementalCraft.MODID, "binding");

	private static final int RADIUS = 42;

	private final IDrawable icon;
	private final IDrawable overlay;
	private final IDrawable background;
	private ItemStack binder = new ItemStack(ECItems.binder).copy();

	public BindingRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createBlankDrawable(RADIUS * 2 + 48, RADIUS * 2 + 16);
		icon = guiHelper.createDrawableIngredient(binder);
		overlay = guiHelper.createDrawable(new ResourceLocation(ElementalCraft.MODID, "textures/gui/binding_overlay.png"), 0, 0, 124, 83);
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends BinderRecipe> getRecipeClass() {
		return BinderRecipe.class;
	}

	@Override
	public String getTitle() {
		return I18n.format("elementalcraft.jei.binding");
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
	public void draw(BinderRecipe recipe, double mouseX, double mouseY) {
		RenderSystem.enableAlphaTest();
		RenderSystem.enableBlend();
		overlay.draw(10, 10);
		renderElementGauge(RADIUS + 1, RADIUS + 18, recipe);
		RenderSystem.disableBlend();
		RenderSystem.disableAlphaTest();
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, BinderRecipe recipe, IIngredients ingredients) {
		int i = 0;
		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);

		for (List<ItemStack> input : inputs) {
			double a = Math.toRadians((double) i / (double) inputs.size() * 360D + 180);

			recipeLayout.getItemStacks().init(i, true, RADIUS + (int) (-RADIUS * Math.sin(a)), RADIUS + (int) (RADIUS * Math.cos(a)));
			recipeLayout.getItemStacks().set(i, input);
			i++;
		}
		recipeLayout.getItemStacks().init(i, false, RADIUS, RADIUS);
		recipeLayout.getItemStacks().set(i, tank);
		recipeLayout.getItemStacks().init(i + 1, false, RADIUS, RADIUS - 16);
		recipeLayout.getItemStacks().set(i + 1, binder);

		recipeLayout.getItemStacks().init(i + 2, false, RADIUS * 2 + 32, RADIUS);
		recipeLayout.getItemStacks().set(i + 2, ingredients.getOutputs(VanillaTypes.ITEM).get(0));

	}

}
