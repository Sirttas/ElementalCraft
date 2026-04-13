package sirttas.elementalcraft.interaction.jei.category.instrument.io;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.jei.ECJEIRecipeTypes;
import sirttas.elementalcraft.recipe.input.SingleItemSingleElementRecipeInput;
import sirttas.elementalcraft.recipe.instrument.infusion.InfusionRecipe;

import javax.annotation.Nonnull;
import java.util.List;

public class InfusionRecipeCategory extends AbstractIOInstrumentRecipeCategory<SingleItemSingleElementRecipeInput, InfusionRecipe> {

	public InfusionRecipeCategory(IGuiHelper guiHelper) {
		this(guiHelper, "elementalcraft.jei.infusion");
	}

	protected InfusionRecipeCategory(IGuiHelper guiHelper, String translationKey) {
		super(guiHelper, translationKey, ECBlocks.INFUSER.get());
	}

	@Nonnull
	@Override
	public IRecipeType<@NotNull InfusionRecipe> getRecipeType() {
		return ECJEIRecipeTypes.INFUSION;
	}

	@Override
	protected List<ItemStack> getContainers() {
		return List.of(container, new ItemStack(ECBlocks.SMALL_CONTAINER.get()));
	}
}
