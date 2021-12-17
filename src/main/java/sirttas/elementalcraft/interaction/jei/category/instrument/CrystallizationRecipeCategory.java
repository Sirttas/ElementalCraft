package sirttas.elementalcraft.interaction.jei.category.instrument;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.crystallizer.CrystallizerBlockEntity;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe.ResultEntry;

import javax.annotation.Nonnull;

public class CrystallizationRecipeCategory extends AbstractInstrumentRecipeCategory<CrystallizerBlockEntity, CrystallizationRecipe> {

	public static final ResourceLocation UID = ElementalCraft.createRL(CrystallizationRecipe.NAME);

	private static final ItemStack CRYSTALLIZER = new ItemStack(ECItems.CRYSTALLIZER);

	public CrystallizationRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.crystallization", guiHelper.createDrawableIngredient(CRYSTALLIZER), guiHelper.createBlankDrawable(132, 110));
		setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/overlay/crystallization.png"), 0, 0, 124, 52), 10, 10);
	}

	@Nonnull
    @Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
    @Override
	public Class<CrystallizationRecipe> getRecipeClass() {
		return CrystallizationRecipe.class;
	}

	@Override
	public void setIngredients(CrystallizationRecipe recipe, IIngredients ingredients) {
		List<List<ItemStack>> outputs = new ArrayList<>();

		outputs.add(recipe.getOutputs().stream().map(ResultEntry::getResult).collect(Collectors.toList()));
		super.setIngredients(recipe, ingredients);
		ingredients.setOutputLists(VanillaTypes.ITEM, outputs);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, @Nonnull CrystallizationRecipe recipe, IIngredients ingredients) {
		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);

		recipeLayout.getItemStacks().addTooltipCallback((slot, input, ingredient, tooltip) -> {
			if (slot == 6) {
				tooltip.add(new TranslatableComponent("tooltip.elementalcraft.chance", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(recipe.getWeight(ingredient) * 100F / recipe.getTotalWeight())));
			} else if (slot == 2) {
				tooltip.add(new TranslatableComponent("tooltip.elementalcraft.optional"));
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
		recipeLayout.getItemStacks().set(4, CRYSTALLIZER);

		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).init(5, true, 43, 86);
		recipeLayout.getIngredientsGroup(ECIngredientTypes.ELEMENT).set(5, ingredients.getInputs(ECIngredientTypes.ELEMENT).get(0));

		recipeLayout.getItemStacks().init(6, false, 116, 42);
		recipeLayout.getItemStacks().set(6, ingredients.getOutputs(VanillaTypes.ITEM).get(0));

	}

}
