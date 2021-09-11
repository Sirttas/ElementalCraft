package sirttas.elementalcraft.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.inventory.IInventoryBlockEntity;
import sirttas.elementalcraft.inventory.InventoryBlockEntityWrapper;

public interface IInventoryBlockEntityRecipe<T extends IInventoryBlockEntity> extends IECRecipe<InventoryBlockEntityWrapper<T>> {

	int getElementAmount();

	boolean matches(T inv);

	@Override
	default boolean matches(InventoryBlockEntityWrapper<T> inv, Level worldIn) {
		return matches(inv.getEntity());
	}

	@Override
	default ItemStack assemble(InventoryBlockEntityWrapper<T> inv) {
		return assemble(inv.getEntity());
	}
	
	@Override
	default boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	default ItemStack assemble(T instrument) {
		return this.getResultItem().copy();
	}
}
