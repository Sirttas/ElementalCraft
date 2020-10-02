package sirttas.elementalcraft.inventory;

import javax.annotation.Nonnull;

import net.minecraft.inventory.IInventory;

public interface IInventoryTile {

	@Nonnull
	IInventory getInventory();
}
