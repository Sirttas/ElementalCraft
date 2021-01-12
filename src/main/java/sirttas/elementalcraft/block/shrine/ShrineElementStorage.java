package sirttas.elementalcraft.block.shrine;

import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.SingleElementStorage;

public class ShrineElementStorage extends SingleElementStorage {

	public ShrineElementStorage(ElementType elementType, int capacity, Runnable syncCallback) {
		super(elementType, capacity, syncCallback);
	}

	@Override
	public boolean canPipeExtract() {
		return false;
	}

	void setCapacity(int capacity) {
		this.elementCapacity = capacity;
	}

}
