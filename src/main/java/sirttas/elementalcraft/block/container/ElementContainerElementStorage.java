package sirttas.elementalcraft.block.container;

import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;

public class ElementContainerElementStorage extends SingleElementStorage {

	public ElementContainerElementStorage(int elementCapacity, Runnable syncCallback) {
		super(elementCapacity, syncCallback);
	}

	@Override
	public boolean doesRenderGauge() {
		return true;
	}

}
