package sirttas.elementalcraft.interaction.jei.category.instrument.io;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.block.instrument.infuser.IInfuser;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.ToolInfusionRecipe;

public class InfusionRecipeCategory extends AbstractIOInstrumentRecipeCategory<IInfuser, IInfusionRecipe> {

	public static final ResourceLocation UID = ElementalCraft.createRL(IInfusionRecipe.NAME);

	public InfusionRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, "elementalcraft.jei.infusion", ECItems.INFUSER);
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<IInfusionRecipe> getRecipeClass() {
		return IInfusionRecipe.class;
	}

	@Override
	protected List<ItemStack> getTanks() {
		return ImmutableList.of(tank, new ItemStack(ECItems.TANK_SMALL));
	}

	@Override
	public void setIngredients(IInfusionRecipe recipe, IIngredients ingredients) {
		super.setIngredients(recipe, ingredients);
		if (recipe instanceof ToolInfusionRecipe) {
			ToolInfusion infusion = ((ToolInfusionRecipe) recipe).getToolInfusion();

			ingredients.setOutputLists(VanillaTypes.ITEM, recipe.getIngredients().stream()
					.map(i -> Arrays.stream(i.getItems())
							.map(stack -> {
								ItemStack copy = stack.copy();

								ToolInfusionHelper.setInfusion(copy, infusion);
								return copy;
							}).collect(Collectors.toList()))
					.collect(Collectors.toList()));
		}
	}
	
}
