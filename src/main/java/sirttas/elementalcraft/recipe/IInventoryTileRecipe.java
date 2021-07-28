package sirttas.elementalcraft.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.inventory.IInventoryTile;
import sirttas.elementalcraft.inventory.InventoryTileWrapper;

public interface IInventoryTileRecipe<T extends IInventoryTile> extends IECRecipe<InventoryTileWrapper<T>> {

	void process(T instrument);

	int getElementAmount();

	boolean matches(T inv);

	@Override
	default boolean matches(InventoryTileWrapper<T> inv, Level worldIn) {
		return matches(inv.getInstrument());
	}

	@Override
	default ItemStack assemble(InventoryTileWrapper<T> inv) {
		return getCraftingResult(inv.getInstrument());
	}
	
	@Override
	default boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	default ItemStack getCraftingResult(T instrument) {
		return this.getResultItem().copy();
	}
}
