package sirttas.elementalcraft.interaction.jei.category.element;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.item.ECItems;

public class ExtractionRecipeCategory implements IRecipeCategory<ElementType> {

	public static final ResourceLocation UID = ElementalCraft.createRL("extraction");

	private final IDrawable icon;
	private final IDrawable background;
	private final IDrawable overlay;
	private final int amount;
	private final ItemStack extractor;
	protected final List<ItemStack> tanks;

	public ExtractionRecipeCategory(IGuiHelper guiHelper) {
		this(guiHelper, new ItemStack(ECItems.EXTRACTOR), Lists.newArrayList(new ItemStack(ECItems.TANK), new ItemStack(ECItems.TANK_SMALL)), 1);
	}

	protected ExtractionRecipeCategory(IGuiHelper guiHelper, ItemStack extractor, List<ItemStack> tanks, int amount) {
		this.extractor = extractor;
		this.tanks = tanks;
		this.amount = amount;
		background = guiHelper.createBlankDrawable(64, 48);
		icon = guiHelper.createDrawableIngredient(extractor);
		overlay = guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/extraction_overlay.png"), 0, 0, 45, 44);
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends ElementType> getRecipeClass() {
		return ElementType.class;
	}

	@Override
	public String getTitle() {
		return I18n.format("elementalcraft.jei.extraction");
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public List<ITextComponent> getTooltipStrings(ElementType recipe, double mouseX, double mouseY) {
		if (mouseX > 0 && mouseX < 16 && mouseY > 0 && mouseY < 16) {
			return Lists.newArrayList(new TranslationTextComponent("block.elementalcraft.source." + recipe.getString()));
		}
		return Collections.emptyList();
	}

	@Override
	public void draw(ElementType recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		RenderSystem.enableBlend();
		overlay.draw(matrixStack, 0, 0);
		RenderSystem.disableBlend();
	}

	@Override
	public void setIngredients(ElementType recipe, IIngredients ingredients) {
		ingredients.setOutput(ECIngredientTypes.ELEMENT, new IngredientElementType(recipe, amount));
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, ElementType recipe, IIngredients ingredients) {
		recipeLayout.getItemStacks().init(0, false, 0, 32);
		recipeLayout.getItemStacks().set(0, tanks);
		recipeLayout.getItemStacks().init(1, false, 0, 16);
		recipeLayout.getItemStacks().set(1, extractor);

		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).init(2, true, 47, 31);
		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).set(2, ingredients.getOutputs(ECIngredientTypes.ELEMENT).get(0));

	}

}
