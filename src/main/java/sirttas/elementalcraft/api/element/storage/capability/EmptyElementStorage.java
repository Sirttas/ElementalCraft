package sirttas.elementalcraft.api.element.storage.capability;

import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;

public class EmptyElementStorage implements IElementStorage {

	public static final EmptyElementStorage INSTANCE = new EmptyElementStorage();

	private EmptyElementStorage() {
	}

	@Override
	public ElementType getElementType() {
		return ElementType.NONE;
	}

	@Override
	public int getElementAmount() {
		return 0;
	}

	@Override
	public int getElementCapacity() {
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
