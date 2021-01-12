package sirttas.elementalcraft.block.tank;

import sirttas.elementalcraft.api.element.storage.ElementStorage;

public class ContainerElementStorage extends ElementStorage {

	public ContainerElementStorage(int elementCapacity, Runnable syncCallback) {
		super(elementCapacity, syncCallback);
	}

	@Override
	public boolean doesRenderGauge() {
		return true;
	}

}
