package sirttas.elementalcraft.api.element.storage.single;

import sirttas.elementalcraft.api.element.ElementType;

public class StaticElementStorage extends SingleElementStorage {

	public StaticElementStorage(ElementType elementType, int elementCapacity) {
		this(elementType, elementCapacity, null);
	}

	public StaticElementStorage(ElementType elementType, int elementCapacity, Runnable syncCallback) {
		super(elementCapacity, syncCallback);
		this.elementType = elementType;
	}

	@Override
	public int insertElement(int count, ElementType type, boolean simulate) {
		ElementType old = this.elementType;
		int value = super.insertElement(count, type, simulate);

		this.elementType = old;
		return value;
	}

	@Override
	public int extractElement(int count, ElementType type, boolean simulate) {
		ElementType old = this.elementType;
		int value = super.extractElement(count, type, simulate);

		this.elementType = old;
		return value;
	}
}
