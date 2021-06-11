package sirttas.elementalcraft.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public interface IECRecipe<T extends IInventory> extends IRecipe<T> {

	@Override
	default boolean canCraftInDimensions(int width, int height) {
		return true;
	}
	
	@Override
	default ItemStack assemble(T inv) {
		return this.getResultItem().copy();
	}
	
	@Override
	default boolean isSpecial() {
		return getResultItem().isEmpty();
	}
	
}
