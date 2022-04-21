package sirttas.elementalcraft.interaction.jei.category.instrument.io;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.block.instrument.infuser.IInfuser;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;
import sirttas.elementalcraft.recipe.instrument.infusion.ToolInfusionRecipe;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InfusionRecipeCategory extends AbstractIOInstrumentRecipeCategory<IInfuser, IInfusionRecipe> {

	private static final ResourceLocation UID = ElementalCraft.createRL(IInfusionRecipe.NAME);

	public InfusionRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, "elementalcraft.jei.infusion", ECItems.INFUSER);
	}

	@Nonnull
    @Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
    @Override
	public Class<IInfusionRecipe> getRecipeClass() {
		return IInfusionRecipe.class;
	}

	@Nonnull
	@Override
	public RecipeType<IInfusionRecipe> getRecipeType() {
		return ECJEIRecipeTypes.INFUSION;
	}

	@Override
	protected List<ItemStack> getTanks() {
		return List.of(tank, new ItemStack(ECItems.TANK_SMALL));
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
