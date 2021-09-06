package sirttas.elementalcraft.interaction.jei.category.instrument.io;

import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.resources.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.purifier.PurifierBlockEntity;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.instrument.io.PurifierRecipe;

public class PurificationRecipeCategory extends AbstractIOInstrumentRecipeCategory<PurifierBlockEntity, PurifierRecipe> {

	public static final ResourceLocation UID = ElementalCraft.createRL("purification");

	public PurificationRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, "elementalcraft.jei.purification", ECItems.PURIFIER);
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<PurifierRecipe> getRecipeClass() {
		return PurifierRecipe.class;
	}
}
