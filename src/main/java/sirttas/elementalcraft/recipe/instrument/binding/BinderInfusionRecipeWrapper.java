package sirttas.elementalcraft.recipe.instrument.binding;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import sirttas.elementalcraft.block.instrument.binder.IBinder;
import sirttas.elementalcraft.block.instrument.infuser.IInfuser;
import sirttas.elementalcraft.recipe.instrument.infusion.IInfusionRecipe;

public class BinderInfusionRecipeWrapper extends AbstractBindingRecipe {

	private final IInfusionRecipe recipe;
	
	public BinderInfusionRecipeWrapper(IInfusionRecipe infusionRecipe) {
		super(infusionRecipe.getId(), infusionRecipe.getElementType());
		this.recipe = infusionRecipe;
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
	public ItemStack assemble(IBinder instrument) {
		if (instrument instanceof IInfuser) {
			return recipe.assemble((IInfuser) instrument);
		}
		return super.assemble(instrument);
	}

	@Override
	public ItemStack getResultItem() {
		return recipe.getResultItem();
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return recipe.getSerializer();
	}
}
