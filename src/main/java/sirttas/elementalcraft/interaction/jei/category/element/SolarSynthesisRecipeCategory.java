package sirttas.elementalcraft.interaction.jei.category.element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.elemental.ItemElemental;

public class SolarSynthesisRecipeCategory implements IRecipeCategory<Ingredient> {

	public static final ResourceLocation UID = ElementalCraft.createRL("solar_synthesis");

	private final IDrawable icon;
	private final IDrawable background;
	private final IDrawable overlay;
	private final ItemStack solarSynthesizer = new ItemStack(ECBlocks.SOLAR_SYNTHESIZER);
	protected final ItemStack tank = new ItemStack(ECItems.TANK);

	public SolarSynthesisRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createBlankDrawable(84, 66);
		icon = guiHelper.createDrawableIngredient(solarSynthesizer);
		overlay = guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/solar_synthesis_overlay.png"), 0, 0, 49, 54);
	}


	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends Ingredient> getRecipeClass() {
		return Ingredient.class;
	}

	@Override
	public String getTitle() {
		return I18n.format("elementalcraft.jei.solar_synthesis");
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
	public List<ITextComponent> getTooltipStrings(Ingredient recipe, double mouseX, double mouseY) {
		if (mouseX > 0 && mouseX < 16 && mouseY > 0 && mouseY < 16) {
			return Lists.newArrayList(new TranslationTextComponent("block.elementalcraft.source." + getElementType(recipe).getString()));
		}
		return Collections.emptyList();
	}

	@Override
	public void draw(Ingredient recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		RenderSystem.enableBlend();
		overlay.draw(matrixStack, 8, 4);
		RenderSystem.disableBlend();
	}

	@Override
	public void setIngredients(Ingredient recipe, IIngredients ingredients) {
		List<List<ItemStack>> inputs = new ArrayList<>();

		inputs.add(Stream.of(recipe.getMatchingStacks()).collect(Collectors.toList()));
		ingredients.setInputLists(VanillaTypes.ITEM, inputs);
		ingredients.setOutput(ECIngredientTypes.ELEMENT, new IngredientElementType(getElementType(recipe), 1));
	}

	private ElementType getElementType(Ingredient recipe) {
		ItemStack[] stacks = recipe.getMatchingStacks();

		if (stacks != null && stacks.length > 0) {
			Item item = stacks[0].getItem();

			if (item instanceof ItemElemental) {
				return ((ItemElemental) item).getElementType();
			}
		}
		return ElementType.NONE;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, Ingredient recipe, IIngredients ingredients) {
		recipeLayout.getItemStacks().init(0, true, 15, 0);
		recipeLayout.getItemStacks().set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));

		recipeLayout.getItemStacks().init(1, false, 15, 47);
		recipeLayout.getItemStacks().set(1, tank);
		recipeLayout.getItemStacks().init(2, false, 15, 31);
		recipeLayout.getItemStacks().set(2, solarSynthesizer);

		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).init(2, true, 60, 44);
		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).set(2, ingredients.getOutputs(ECIngredientTypes.ELEMENT).get(0));

	}

	public static List<Ingredient> getLenses() {
		return Lists.newArrayList(Ingredient.fromItems(ECItems.FIRE_LENSE), Ingredient.fromItems(ECItems.WATER_LENSE), Ingredient.fromItems(ECItems.EARTH_LENSE), Ingredient.fromItems(ECItems.AIR_LENSE));
	}

}
