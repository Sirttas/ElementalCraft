package sirttas.elementalcraft.interaction.jei.category.element.synthesis.cracking;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.recipe.cracking.AbstractCrackingRecipe;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class AbstractCrackingRecipeCategory<T extends AbstractCrackingRecipe> extends AbstractECRecipeCategory<T> {

	private final List<ItemStack> containers;

	private final ItemStack synthesizer;

	protected AbstractCrackingRecipeCategory(IGuiHelper guiHelper, String translationKey, ItemStack synthesizer, List<ItemStack> containers) {
		super(translationKey, createDrawableStack(guiHelper, synthesizer), 108, 36);
		addOverlay(guiHelper.createDrawable(ElementalCraftApi.createRL("textures/gui/overlay/cracking.png"), 0, 0, 73, 14), 17, 17, AbstractCrackingRecipe::hasResult);
		addOverlay(guiHelper.createDrawable(ElementalCraftApi.createRL("textures/gui/overlay/cracking_no_output.png"), 0, 0, 73, 14), 17, 17, recipe -> !recipe.hasResult());
		this.synthesizer = synthesizer;
		this.containers = containers;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull T recipe, @Nonnull IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 0, 20)
				.addItemStacks(recipe.input().stream()
						.map(b -> new ItemStack(b.value()))
						.toList());

		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 41, 17)
				.addItemStacks(containers);
		builder.addSlot(RecipeIngredientRole.CATALYST, 41, 1)
				.addItemStack(synthesizer);
		builder.addSlot(RecipeIngredientRole.OUTPUT, 70, 0)
				.addIngredient(ECIngredientTypes.ELEMENT, new IngredientElementType(ElementType.EARTH, IngredientElementType.getGaugeValue(recipe.elementAmount())));

		if (recipe.hasResult()) {
			builder.addSlot(RecipeIngredientRole.OUTPUT, 92, 20)
					.addItemStack(new ItemStack(recipe.result()));
		}
	}
}
