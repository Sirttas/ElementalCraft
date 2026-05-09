package sirttas.elementalcraft.interaction.jei.category.element;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.interaction.jei.ingredient.source.IngredientSource;

import javax.annotation.Nonnull;
import java.util.List;

public class ExtractionRecipeCategory extends AbstractECRecipeCategory<ExtractionRecipeCategory.ExtractionRecipe> {

	public static final String NAME = "extraction";

	public ExtractionRecipeCategory(IGuiHelper guiHelper) {
		super("elementalcraft.jei.extraction", createDrawableStack(guiHelper, new ItemStack(ECBlocks.RUDIMENTARY_EXTRACTOR.get())), 64, 48);
		addOverlay(guiHelper.createDrawable(ElementalCraftApi.identifier("textures/gui/overlay/extraction.png"), 0, 0, 24, 9), 21, 35);
	}

	@Nonnull
	@Override
	public IRecipeType<@NotNull ExtractionRecipe> getRecipeType() {
		return ECJEIRecipeTypes.EXTRACTION;
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayoutBuilder builder, @Nonnull ExtractionRecipeCategory.ExtractionRecipe recipe, @Nonnull IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 0, 0).add(ECIngredientTypes.SOURCE, recipe.source());
		builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, 0, 16).add(recipe.extractor());
		builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, 0, 32).addItemStacks(recipe.containers());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 47, 32).add(ECIngredientTypes.ELEMENT, recipe.element());
	}

	public record ExtractionRecipe(
			IngredientElementType element,
			ItemStack extractor,
			List<ItemStack> containers
	) {

		public IngredientSource source() {
			return new IngredientSource(element.elementType());
		}
	}
}
