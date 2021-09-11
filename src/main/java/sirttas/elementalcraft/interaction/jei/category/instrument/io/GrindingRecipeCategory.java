package sirttas.elementalcraft.interaction.jei.category.instrument.io;

import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.resources.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.io.mill.AirMillBlockEntity;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.instrument.io.grinding.AirMillGrindingRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

public class GrindingRecipeCategory extends AbstractIOInstrumentRecipeCategory<AirMillBlockEntity, AirMillGrindingRecipe> {

	public static final ResourceLocation UID = ElementalCraft.createRL(IGrindingRecipe.NAME);

	public GrindingRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, "elementalcraft.jei.grinding", ECItems.AIR_MILL);
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<AirMillGrindingRecipe> getRecipeClass() {
		return AirMillGrindingRecipe.class;
	}
}
