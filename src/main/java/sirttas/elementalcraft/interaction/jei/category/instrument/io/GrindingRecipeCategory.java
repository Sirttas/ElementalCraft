package sirttas.elementalcraft.interaction.jei.category.instrument.io;

import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.mill.TileAirMill;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.instrument.io.grinding.AirMillGrindingRecipe;

public class GrindingRecipeCategory extends AbstractIOInstrumentRecipeCategory<TileAirMill, AirMillGrindingRecipe> {

	public static final ResourceLocation UID = ElementalCraft.createRL("grinding");

	public GrindingRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, new ItemStack(ECItems.AIR_MILL));
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends AirMillGrindingRecipe> getRecipeClass() {
		return AirMillGrindingRecipe.class;
	}

	@Override
	public String getTitle() {
		return I18n.format("elementalcraft.jei.grinding");
	}


}
