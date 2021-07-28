package sirttas.elementalcraft.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

public interface IECRecipe<T extends Container> extends Recipe<T> {

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
