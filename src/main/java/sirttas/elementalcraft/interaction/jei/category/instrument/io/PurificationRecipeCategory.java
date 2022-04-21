package sirttas.elementalcraft.interaction.jei.category.instrument.io;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.io.purifier.PurifierBlockEntity;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.instrument.io.IPurifierRecipe;

import javax.annotation.Nonnull;

public class PurificationRecipeCategory extends AbstractIOInstrumentRecipeCategory<PurifierBlockEntity, IPurifierRecipe> {

	public static final String NAME = "purification";
	private static final ResourceLocation UID = ElementalCraft.createRL(NAME);

	public PurificationRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, "elementalcraft.jei.purification", ECItems.PURIFIER);
	}

	@Nonnull
    @Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
    @Override
	public Class<IPurifierRecipe> getRecipeClass() {
		return IPurifierRecipe.class;
	}

	@Nonnull
	@Override
	public RecipeType<IPurifierRecipe> getRecipeType() {
		return ECJEIRecipeTypes.PURIFICATION;
	}
}
