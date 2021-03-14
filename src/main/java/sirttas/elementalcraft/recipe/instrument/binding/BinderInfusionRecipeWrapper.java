package sirttas.elementalcraft.recipe.instrument.binding;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import sirttas.elementalcraft.block.instrument.binder.IBinder;
import sirttas.elementalcraft.block.instrument.infuser.IInfuser;
import sirttas.elementalcraft.recipe.instrument.infusion.AbstractInfusionRecipe;

public class BinderInfusionRecipeWrapper extends AbstractBindingRecipe {

	private final AbstractInfusionRecipe recipe;
	
	public BinderInfusionRecipeWrapper(AbstractInfusionRecipe recipe) {
		super(recipe.getId(), recipe.getElementType());
		this.recipe = recipe;
	}

	@Override
	public int getElementAmount() {
		return recipe.getElementAmount();
	}

	@Override
	public boolean matches(IBinder inv) {
		return inv instanceof IInfuser && recipe.matches((IInfuser) inv);
	}

	@Override
	public ItemStack getRecipeOutput() {
		return recipe.getRecipeOutput();
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return recipe.getSerializer();
	}
}
