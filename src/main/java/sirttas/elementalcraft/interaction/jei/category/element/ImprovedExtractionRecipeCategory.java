package sirttas.elementalcraft.interaction.jei.category.element;

import com.google.common.collect.Lists;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.item.ECItems;

import javax.annotation.Nonnull;

public class ImprovedExtractionRecipeCategory extends ExtractionRecipeCategory {


	public static final String NAME = "extraction_improved";
	private static final ResourceLocation UID = ElementalCraft.createRL(NAME);

	public ImprovedExtractionRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, "elementalcraft.jei.extraction_improved", new ItemStack(ECItems.EXTRACTOR_IMPROVED), Lists.newArrayList(new ItemStack(ECItems.TANK)), 2);
	}

	@Nonnull
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public RecipeType<ElementType> getRecipeType() {
		return ECJEIRecipeTypes.EXTRACTION_IMPROVED;
	}
}
