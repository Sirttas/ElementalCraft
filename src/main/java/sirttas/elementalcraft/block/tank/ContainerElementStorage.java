package sirttas.elementalcraft.block.tank;

import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;

public class ContainerElementStorage extends SingleElementStorage {

	public ContainerElementStorage(int elementCapacity, Runnable syncCallback) {
		super(elementCapacity, syncCallback);
	}

	@Override
	public boolean doesRenderGauge() {
		return true;
	}

}
