package sirttas.elementalcraft.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;

public abstract class SynchronizableInventory implements IInventory {

	private Runnable syncFunction;

	public SynchronizableInventory(Runnable syncCallback) {
		this.syncFunction = syncCallback;
	}

	@Override
	public void markDirty() {
		if (syncFunction != null) {
			syncFunction.run();
		}
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return true;
	}
}
