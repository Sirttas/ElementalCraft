package sirttas.elementalcraft.recipe.instrument.infusion;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.infuser.IInfuser;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.infusion.InfusionHelper;

public class ToolInfusionRecipe extends AbstractInfusionRecipe {

	public ToolInfusionRecipe() {
		super(null, ElementType.NONE);
	}

	@Override
	public boolean matches(IInfuser instrument) {
		ItemStack stack = instrument.getItem();

		if (super.matches(instrument) && InfusionHelper.isInfusable(stack)) {
			return InfusionHelper.getInfusion(stack) != instrument.getTankElementType();
		}
		return false;
	}

	@Override
	public int getElementAmount() {
		return ECConfig.COMMON.toolInfustionBaseCost.get();
	}

	@Override
	public ItemStack getCraftingResult(IInfuser instrument) {
		ItemStack stack = instrument.getItem();

		InfusionHelper.setInfusion(stack, instrument.getTankElementType());
		return stack;
	}

	@Deprecated
	@Override
	public ItemStack getRecipeOutput() {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public IRecipeSerializer<?> getSerializer() {
		throw new UnsupportedOperationException();
	}

	public ToolInfusionRecipe with(ElementType type) {
		this.elementType = type;
		return this;
	}
}
