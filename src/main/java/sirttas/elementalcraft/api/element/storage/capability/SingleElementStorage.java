package sirttas.elementalcraft.api.element.storage.capability;

import sirttas.elementalcraft.api.element.ElementType;

public class SingleElementStorage extends ElementStorage {

	public SingleElementStorage(ElementType elementType, int elementCapacity) {
		this(elementType, elementCapacity, null);
	}

	public SingleElementStorage(ElementType elementType, int elementCapacity, Runnable syncCallback) {
		super(elementCapacity, syncCallback);
		this.elementType = elementType;
	}

	@Override
	public int extractElement(int count, ElementType type, boolean simulate) {
		ElementType old = this.elementType;
		int value = insertElement(count, type, simulate);

		this.elementType = old;
		return value;
	}
}
