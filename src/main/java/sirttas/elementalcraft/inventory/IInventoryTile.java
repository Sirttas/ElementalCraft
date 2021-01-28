package sirttas.elementalcraft.inventory;

import javax.annotation.Nonnull;

import net.minecraft.inventory.IClearable;
import net.minecraft.inventory.IInventory;

public interface IInventoryTile extends IClearable {

	@Nonnull
	IInventory getInventory();

	default void clear() {
		getInventory().clear();
	}
}
