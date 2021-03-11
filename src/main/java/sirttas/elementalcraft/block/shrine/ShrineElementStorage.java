package sirttas.elementalcraft.block.shrine;

import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.StaticElementStorage;

public class ShrineElementStorage extends StaticElementStorage {

	public ShrineElementStorage(ElementType elementType, int capacity, Runnable syncCallback) {
		super(elementType, capacity, syncCallback);
	}

	@Override
	public boolean canPipeExtract(ElementType elementType) {
		return false;
	}

	void setCapacity(int capacity) {
		this.elementCapacity = capacity;
	}

}
