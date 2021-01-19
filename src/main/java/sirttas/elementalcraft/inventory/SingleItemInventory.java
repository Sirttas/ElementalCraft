package sirttas.elementalcraft.inventory;

import net.minecraft.item.ItemStack;

public class SingleItemInventory extends SingleStackInventory {

	public SingleItemInventory() {
		this(null);
	}

	public SingleItemInventory(Runnable syncCallback) {
		super(syncCallback);
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public ItemStack decrStackSize(int slot, int count) {
		ItemStack value = slot == 0 && !stack.isEmpty() && count == 1 ? stack.split(count) : ItemStack.EMPTY;

		this.markDirty();
		return value;
	}
}
