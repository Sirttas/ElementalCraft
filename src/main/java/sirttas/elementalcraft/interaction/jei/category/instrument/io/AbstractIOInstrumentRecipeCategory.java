package sirttas.elementalcraft.interaction.jei.category.instrument.io;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.IInstrument;
import sirttas.elementalcraft.interaction.jei.category.instrument.AbstractInstrumentRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class AbstractIOInstrumentRecipeCategory<K extends IInstrument, T extends IInstrumentRecipe<K>> extends AbstractInstrumentRecipeCategory<K, T> {
	
	protected final ItemStack instrument;

	protected AbstractIOInstrumentRecipeCategory(IGuiHelper guiHelper, String translationKey, ItemLike item) {
		this(guiHelper, translationKey, new ItemStack(item));
	}
	
	protected AbstractIOInstrumentRecipeCategory(IGuiHelper guiHelper, String translationKey, ItemStack instrument) {
		super(translationKey, createDrawableStack(guiHelper, instrument), guiHelper.createBlankDrawable(75, 75));
		this.instrument = instrument;
		setOverlay(guiHelper.createDrawable(ElementalCraft.createRL("textures/gui/overlay/io.png"), 0, 0, 65, 16), 8, 20);
	}

	protected List<ItemStack> getTanks() {
		return List.of(container);
	}

	@Nonnull
	protected List<ItemStack> getOutputs(@Nonnull T recipe) {
		return List.of(recipe.getResultItem());
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull T recipe, @Nonnull IFocusGroup focuses) {
		var ingredients = recipe.getIngredients();

		builder.addSlot(RecipeIngredientRole.INPUT, 0, 0)
				.addIngredients(ingredients.get(0));

		builder.addSlot(RecipeIngredientRole.CATALYST, 30, 24)
				.addItemStack(instrument);
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 30, 40)
				.addItemStacks(getTanks());

		builder.addSlot(RecipeIngredientRole.INPUT, 30, 58)
				.addIngredients(ECIngredientTypes.ELEMENT, getElementTypeIngredients(recipe));

		builder.addSlot(RecipeIngredientRole.OUTPUT, 59, 0)
				.addItemStacks(getOutputs(recipe));
	}
}
