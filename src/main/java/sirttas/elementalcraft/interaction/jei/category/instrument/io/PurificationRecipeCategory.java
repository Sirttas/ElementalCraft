package sirttas.elementalcraft.interaction.jei.category.instrument.io;

import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.purifier.TilePurifier;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.instrument.io.PurifierRecipe;

public class PurificationRecipeCategory extends AbstractIOInstrumentRecipeCategory<TilePurifier, PurifierRecipe> {

	public static final ResourceLocation UID = ElementalCraft.createRL("purification");

	public PurificationRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, new ItemStack(ECItems.PURIFIER));
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends PurifierRecipe> getRecipeClass() {
		return PurifierRecipe.class;
	}

	@Override
	public String getTitle() {
		return I18n.format("elementalcraft.jei.purification");
	}
}
