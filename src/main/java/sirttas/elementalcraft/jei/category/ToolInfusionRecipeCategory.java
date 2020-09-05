package sirttas.elementalcraft.jei.category;

import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;

public class ToolInfusionRecipeCategory extends InfusionRecipeCategory {

	public static final ResourceLocation UID = new ResourceLocation(ElementalCraft.MODID, "tool_infusion");

	public ToolInfusionRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper);
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public String getTitle() {
		return I18n.format("elementalcraft.jei.tool_infusion");
	}
}
