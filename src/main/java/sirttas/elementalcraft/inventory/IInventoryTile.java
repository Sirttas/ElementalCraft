package sirttas.elementalcraft.inventory;

import javax.annotation.Nonnull;

import net.minecraft.inventory.IClearable;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public interface IInventoryTile extends IClearable {

	@Nonnull
	IInventory getInventory();
	
	LazyOptional<IItemHandler> getItemHandler();

	@Override
	default void clear() {
		getInventory().clear();
	}
}
