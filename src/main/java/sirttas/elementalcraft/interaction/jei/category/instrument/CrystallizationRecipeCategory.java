package sirttas.elementalcraft.interaction.jei.category.instrument;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.crystallizer.TileCrystallizer;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe;

public class CrystallizationRecipeCategory extends AbstractInstrumentRecipeCategory<TileCrystallizer, CrystallizationRecipe> {

	public static final ResourceLocation UID = ElementalCraft.createRL(CrystallizationRecipe.NAME);

	private final IDrawable icon;
	private final IDrawable background;
	private ItemStack crystallizer = new ItemStack(ECItems.CRYSTALLIZER);

	public CrystallizationRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createBlankDrawable(132, 110);
		icon = guiHelper.createDrawableIngredient(crystallizer);
		setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/crystallization_overlay.png"), 0, 0, 124, 52), 10, 10);
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends CrystallizationRecipe> getRecipeClass() {
		return CrystallizationRecipe.class;
	}

	@Override
	public String getTitle() {
		return I18n.format("elementalcraft.jei.crystallization");
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
	public void setIngredients(CrystallizationRecipe recipe, IIngredients ingredients) {
		List<List<ItemStack>> outputs = new ArrayList<>();

		outputs.add(recipe.getOutputs().keySet().stream().collect(Collectors.toList()));
		super.setIngredients(recipe, ingredients);
		ingredients.setOutputLists(VanillaTypes.ITEM, outputs);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, CrystallizationRecipe recipe, IIngredients ingredients) {
		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);

		recipeLayout.getItemStacks().addTooltipCallback((slot, input, ingredient, tooltip) -> {
			if (slot == 6) {
				tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.chance", ItemStack.DECIMALFORMAT.format(recipe.getOutputs().get(ingredient) * 100 / recipe.getTotalWeight())));
			}
		});
		
		recipeLayout.getItemStacks().init(0, true, 42, 32);
		recipeLayout.getItemStacks().set(0, inputs.get(0));
		recipeLayout.getItemStacks().init(1, true, 42, 14);
		recipeLayout.getItemStacks().set(1, inputs.get(1));

		recipeLayout.getItemStacks().init(2, true, 6, 21);
		recipeLayout.getItemStacks().set(2, inputs.get(2));

		recipeLayout.getItemStacks().init(3, false, 42, 68);
		recipeLayout.getItemStacks().set(3, tank);
		recipeLayout.getItemStacks().init(4, false, 42, 52);
		recipeLayout.getItemStacks().set(4, crystallizer);

		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).init(5, true, 43, 86);
		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).set(5, ingredients.getInputs(ECIngredientTypes.ELEMENT).get(0));

		recipeLayout.getItemStacks().init(6, false, 116, 42);
		recipeLayout.getItemStacks().set(6, ingredients.getOutputs(VanillaTypes.ITEM).get(0));

	}

}
