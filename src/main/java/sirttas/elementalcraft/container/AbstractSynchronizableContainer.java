package sirttas.elementalcraft.container;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;

public abstract class AbstractSynchronizableContainer implements Container {

	private Runnable syncFunction;

	protected AbstractSynchronizableContainer(Runnable syncCallback) {
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
