package sirttas.elementalcraft.inventory;

import net.minecraft.entity.player.PlayerEntity;
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

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return true;
	}
}
