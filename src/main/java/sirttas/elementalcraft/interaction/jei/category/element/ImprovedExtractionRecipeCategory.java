package sirttas.elementalcraft.interaction.jei.category.element;

import com.google.common.collect.Lists;

import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.item.ECItems;

public class ImprovedExtractionRecipeCategory extends ExtractionRecipeCategory {

	public static final ResourceLocation UID = ElementalCraft.createRL("extraction_improved");

	public ImprovedExtractionRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, new ItemStack(ECItems.EXTRACTOR_IMPROVED), Lists.newArrayList(new ItemStack(ECItems.TANK)), 2);
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public String getTitle() {
		return I18n.format("elementalcraft.jei.extraction_improved");
	}

}
