package sirttas.elementalcraft.inventory;

import net.minecraft.inventory.IInventory;

public abstract class SynchronizableInventory implements IInventory {

	private Runnable syncCallback;

	public SynchronizableInventory(Runnable syncCallback) {
		this.syncCallback = syncCallback;
	}

	@Override
	public void markDirty() {
		if (syncCallback != null) {
			syncCallback.run();
		}
	}
}
