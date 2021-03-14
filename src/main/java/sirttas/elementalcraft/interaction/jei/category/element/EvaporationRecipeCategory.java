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
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.elemental.ItemElemental;
import sirttas.elementalcraft.tag.ECTags;

public class EvaporationRecipeCategory implements IRecipeCategory<Ingredient> {

	public static final ResourceLocation UID = ElementalCraft.createRL("evaporation");

	private final IDrawable icon;
	private final IDrawable background;
	private final IDrawable overlay;
	private final ItemStack evaporator = new ItemStack(ECItems.EVAPORATOR);
	protected final List<ItemStack> tanks = Lists.newArrayList(new ItemStack(ECItems.TANK), new ItemStack(ECItems.TANK_SMALL));

	public EvaporationRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createBlankDrawable(99, 59);
		icon = guiHelper.createDrawableIngredient(evaporator);
		overlay = guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/evaporation_overlay.png"), 0, 0, 64, 29);
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
		return I18n.format("elementalcraft.jei.evaporation");
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
		overlay.draw(matrixStack, 8, 20);
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
		recipeLayout.getItemStacks().init(0, true, 0, 0);
		recipeLayout.getItemStacks().set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));

		recipeLayout.getItemStacks().init(1, false, 30, 40);
		recipeLayout.getItemStacks().set(1, tanks);
		recipeLayout.getItemStacks().init(2, false, 30, 24);
		recipeLayout.getItemStacks().set(2, evaporator);

		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).init(2, true, 75, 36);
		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).set(2, ingredients.getOutputs(ECIngredientTypes.ELEMENT).get(0));

	}

	public static List<Ingredient> getShards() {
		return Lists.newArrayList(Ingredient.fromTag(ECTags.Items.FIRE_SHARDS), Ingredient.fromTag(ECTags.Items.WATER_SHARDS), Ingredient.fromTag(ECTags.Items.EARTH_SHARDS),
				Ingredient.fromTag(ECTags.Items.AIR_SHARDS));
	}

}
