package sirttas.elementalcraft.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class InventoryBlockEntityWrapper<T extends IInventoryBlockEntity> implements Container {

	private final T entity;

	private InventoryBlockEntityWrapper(T inventoryTile) {
		this.entity = inventoryTile;
	}

	public static <T extends IInventoryBlockEntity> InventoryBlockEntityWrapper<T> from(T inventoryTile) {
		return new InventoryBlockEntityWrapper<>(inventoryTile);
	}

	public T getEntity() {
		return entity;
	}

	@Override
	public void clearContent() {
		entity.getInventory().clearContent();
	}

	@Override
	public int getContainerSize() {
		return entity.getInventory().getContainerSize();
	}

	@Override
	public boolean isEmpty() {
		return entity.getInventory().isEmpty();
	}

	@Override
	public ItemStack getItem(int index) {
		return entity.getInventory().getItem(index);
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		return entity.getInventory().removeItem(index, count);
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		return entity.getInventory().removeItemNoUpdate(index);
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		entity.getInventory().setItem(index, stack);
	}

	@Override
	public void setChanged() {
		entity.getInventory().setChanged();
	}

	@Override
	public boolean stillValid(Player player) {
		return entity.getInventory().stillValid(player);
	}


}
