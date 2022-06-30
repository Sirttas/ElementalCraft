package sirttas.elementalcraft.interaction.jei.category.element;

import com.google.common.collect.Lists;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;

import javax.annotation.Nonnull;

public class ImprovedExtractionRecipeCategory extends ExtractionRecipeCategory {

	public static final String NAME = "extraction_improved";

	public ImprovedExtractionRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, "elementalcraft.jei.extraction_improved", new ItemStack(ECBlocks.EXTRACTOR_IMPROVED.get()), Lists.newArrayList(new ItemStack(ECBlocks.CONTAINER.get())), 2);
	}

	@Nonnull
	@Override
	public RecipeType<ElementType> getRecipeType() {
		return ECJEIRecipeTypes.EXTRACTION_IMPROVED;
	}
}
