package sirttas.elementalcraft.interaction.jei.category.instrument.io.infusion;

import java.util.List;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.instrument.infuser.IInfuser;
import sirttas.elementalcraft.interaction.jei.category.instrument.io.AbstractIOInstrumentRecipeCategory;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.instrument.infusion.AbstractInfusionRecipe;

public class InfusionRecipeCategory extends AbstractIOInstrumentRecipeCategory<IInfuser, AbstractInfusionRecipe> {

	public static final ResourceLocation UID = ElementalCraft.createRL("infusion");

	public InfusionRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, new ItemStack(ECItems.INFUSER));
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends AbstractInfusionRecipe> getRecipeClass() {
		return AbstractInfusionRecipe.class;
	}

	@Override
	public String getTitle() {
		return I18n.format("elementalcraft.jei.infusion");
	}

	@Override
	protected List<ItemStack> getTanks() {
		return ImmutableList.of(tank, new ItemStack(ECItems.TANK_SMALL));
	}

}
