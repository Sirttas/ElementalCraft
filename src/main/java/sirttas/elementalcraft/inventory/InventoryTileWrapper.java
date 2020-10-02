package sirttas.elementalcraft.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryTileWrapper<T extends IInventoryTile> implements IInventory {

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
	public void clear() {
		inventoryTile.getInventory().clear();
	}

	@Override
	public int getSizeInventory() {
		return inventoryTile.getInventory().getSizeInventory();
	}

	@Override
	public boolean isEmpty() {
		return inventoryTile.getInventory().isEmpty();
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return inventoryTile.getInventory().getStackInSlot(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return inventoryTile.getInventory().decrStackSize(index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return inventoryTile.getInventory().removeStackFromSlot(index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		inventoryTile.getInventory().setInventorySlotContents(index, stack);
	}

	@Override
	public void markDirty() {
		inventoryTile.getInventory().markDirty();
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return inventoryTile.getInventory().isUsableByPlayer(player);
	}


}
