package sirttas.elementalcraft.interaction.jei.category.element;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.item.ECItems;

public class ExtractionRecipeCategory extends AbstractECRecipeCategory<ElementType> {

	public static final ResourceLocation UID = ElementalCraft.createRL("extraction");

	private final int amount;
	private final ItemStack extractor;
	protected final List<ItemStack> tanks;

	public ExtractionRecipeCategory(IGuiHelper guiHelper) {
		this(guiHelper, "elementalcraft.jei.extraction", new ItemStack(ECItems.EXTRACTOR), Lists.newArrayList(new ItemStack(ECItems.TANK), new ItemStack(ECItems.TANK_SMALL)), 1);
	}

	protected ExtractionRecipeCategory(IGuiHelper guiHelper, String translationKey, ItemStack extractor, List<ItemStack> tanks, int amount) {
		super(translationKey, guiHelper.createDrawableIngredient(extractor), guiHelper.createBlankDrawable(64, 48));
		this.extractor = extractor;
		this.tanks = tanks;
		this.amount = amount;
		setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/overlay/extraction.png"), 0, 0, 45, 44), 0, 0);
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<ElementType> getRecipeClass() {
		return ElementType.class;
	}

	@Override
	public List<ITextComponent> getTooltipStrings(ElementType recipe, double mouseX, double mouseY) {
		if (mouseX > 0 && mouseX < 16 && mouseY > 0 && mouseY < 16) {
			return Lists.newArrayList(new TranslationTextComponent("block.elementalcraft.source." + recipe.getSerializedName()));
		}
		return Collections.emptyList();
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
