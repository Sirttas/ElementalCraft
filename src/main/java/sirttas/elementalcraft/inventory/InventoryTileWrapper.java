package sirttas.elementalcraft.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class InventoryTileWrapper<T extends IInventoryTile> implements Container {

	private final T inventoryTile;

	private InventoryTileWrapper(T inventoryTile) {
		this.inventoryTile = inventoryTile;
	}

	public static <T extends IInventoryTile> InventoryTileWrapper<T> from(T inventoryTile) {
		return new InventoryTileWrapper<>(inventoryTile);
	}

	public T getInstrument() {
		return inventoryTile;
	}

	@Override
	public void clearContent() {
		inventoryTile.getInventory().clearContent();
	}

	@Override
	public int getContainerSize() {
		return inventoryTile.getInventory().getContainerSize();
	}

	@Override
	public boolean isEmpty() {
		return inventoryTile.getInventory().isEmpty();
	}

	@Override
	public ItemStack getItem(int index) {
		return inventoryTile.getInventory().getItem(index);
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		return inventoryTile.getInventory().removeItem(index, count);
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		return inventoryTile.getInventory().removeItemNoUpdate(index);
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		inventoryTile.getInventory().setItem(index, stack);
	}

	@Override
	public void setChanged() {
		inventoryTile.getInventory().setChanged();
	}

	@Override
	public boolean stillValid(Player player) {
		return inventoryTile.getInventory().stillValid(player);
	}


}
