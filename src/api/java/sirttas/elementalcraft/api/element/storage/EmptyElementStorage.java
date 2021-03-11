package sirttas.elementalcraft.api.element.storage;

import sirttas.elementalcraft.api.element.ElementType;

public class EmptyElementStorage implements IElementStorage {

	public static final EmptyElementStorage INSTANCE = new EmptyElementStorage();

	private EmptyElementStorage() {
	}

	@Override
	public int getElementAmount(ElementType type) {
		return 0;
	}

	@Override
	public int getElementCapacity(ElementType type) {
		return 0;
	}

	@Override
	public int insertElement(int count, ElementType type, boolean simulate) {
		return count;
	}

	@Override
	public int extractElement(int count, ElementType type, boolean simulate) {
		return 0;
	}


}
