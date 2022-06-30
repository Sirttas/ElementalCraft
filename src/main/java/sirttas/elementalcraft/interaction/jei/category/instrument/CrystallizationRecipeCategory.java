package sirttas.elementalcraft.interaction.jei.category.instrument;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.instrument.crystallizer.CrystallizerBlockEntity;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe.ResultEntry;

import javax.annotation.Nonnull;

public class CrystallizationRecipeCategory extends AbstractInstrumentRecipeCategory<CrystallizerBlockEntity, CrystallizationRecipe> {

	private static final ItemStack CRYSTALLIZER = new ItemStack(ECBlocks.CRYSTALLIZER.get());

	public CrystallizationRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.crystallization", createDrawableStack(guiHelper, CRYSTALLIZER), guiHelper.createBlankDrawable(132, 110));
		setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/overlay/crystallization.png"), 0, 0, 124, 52), 10, 10);
	}

	@Nonnull
	@Override
	public RecipeType<CrystallizationRecipe> getRecipeType() {
		return ECJEIRecipeTypes.CRYSTALLIZATION;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull CrystallizationRecipe recipe, @Nonnull IFocusGroup focuses) {
		var ingredients = recipe.getIngredients();

		builder.addSlot(RecipeIngredientRole.INPUT, 42, 32)
				.addIngredients(ingredients.get(0));
		builder.addSlot(RecipeIngredientRole.INPUT, 42, 14)
				.addIngredients(ingredients.get(1));
		builder.addSlot(RecipeIngredientRole.INPUT, 6, 21)
				.addIngredients(ingredients.get(2))
				.addTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(Component.translatable("tooltip.elementalcraft.optional")));

		builder.addSlot(RecipeIngredientRole.CATALYST, 42, 52)
				.addItemStack(CRYSTALLIZER);
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 42, 68)
				.addItemStack(container);

		builder.addSlot(RecipeIngredientRole.INPUT, 42, 86)
				.addIngredient(ECIngredientTypes.ELEMENT, getElementTypeIngredient(recipe));

		builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 42)
				.addItemStacks(recipe.getOutputs().stream().map(ResultEntry::getResult).toList())
				.addTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(Component.translatable("tooltip.elementalcraft.chance", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(recipeSlotView.getDisplayedIngredient(VanillaTypes.ITEM_STACK).map(recipe::getWeight).orElse(0F) * 100F / recipe.getTotalWeight()))));
	}

}
