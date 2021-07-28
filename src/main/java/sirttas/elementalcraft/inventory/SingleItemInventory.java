package sirttas.elementalcraft.inventory;

import net.minecraft.world.item.ItemStack;

public class SingleItemInventory extends SingleStackInventory {

	public SingleItemInventory() {
		this(null);
	}

	public SingleItemInventory(Runnable syncCallback) {
		super(syncCallback);
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	@Override
	public ItemStack removeItem(int slot, int count) {
		ItemStack value = slot == 0 && !stack.isEmpty() && count == 1 ? stack.split(count) : ItemStack.EMPTY;

		this.setChanged();
		return value;
	}
}
