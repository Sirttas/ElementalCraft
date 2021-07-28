package sirttas.elementalcraft.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;

public abstract class AbstractSynchronizableInventory implements Container {

	private Runnable syncFunction;

	protected AbstractSynchronizableInventory(Runnable syncCallback) {
		this.syncFunction = syncCallback;
	}

	@Override
	public void setChanged() {
		if (syncFunction != null) {
			syncFunction.run();
		}
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}
}
