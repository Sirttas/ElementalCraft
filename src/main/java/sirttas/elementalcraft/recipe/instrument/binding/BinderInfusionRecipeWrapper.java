package sirttas.elementalcraft.recipe.instrument.binding;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
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
	public ItemStack getResultItem() {
		return recipe.getResultItem();
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return recipe.getSerializer();
	}
}
