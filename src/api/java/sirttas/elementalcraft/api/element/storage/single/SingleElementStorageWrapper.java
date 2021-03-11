package sirttas.elementalcraft.api.element.storage.single;

import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;

public class SingleElementStorageWrapper implements ISingleElementStorage {

	private final ElementType elementType;
	private final IElementStorage storage;

	public SingleElementStorageWrapper(ElementType elementType, IElementStorage storage) {
		this.elementType = elementType;
		this.storage = storage;
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}

	@Override
	public int getElementAmount() {
		return storage.getElementAmount(elementType);
	}

	@Override
	public int getElementCapacity() {
		return storage.getElementCapacity(elementType);
	}

	@Override
	public int insertElement(int count, ElementType type, boolean simulate) {
		return storage.insertElement(count, type, simulate);
	}

	@Override
	public int extractElement(int count, ElementType type, boolean simulate) {
		return storage.extractElement(count, type, simulate);
	}

}
