package sirttas.elementalcraft.api.element.storage.capability;

import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;

public class ElementStorage implements IElementStorage {

	protected int elementAmount;
	protected int elementCapacity;
	protected ElementType elementType;
	private final Runnable syncCallback;
	
	public ElementStorage(int elementCapacity) {
		this(elementCapacity, null);
	}

	public ElementStorage(int elementCapacity, Runnable syncCallback) {
		this.syncCallback = syncCallback;
		this.elementCapacity = elementCapacity;
		this.elementAmount = 0;
		this.elementType = ElementType.NONE;
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	@Override
	public int getElementCapacity() {
		return elementCapacity;
	}

	@Override
	public int insertElement(int count, ElementType type, boolean simulate) {
		if (type != this.elementType && this.elementType != ElementType.NONE) {
			return count - this.extractElement(count, simulate);
		} else {
			int newCount = Math.min(elementAmount + count, elementCapacity);
			int ret = count - newCount + elementAmount;

			if (!simulate) {
				elementAmount = newCount;
				if (this.elementType == ElementType.NONE) {
					this.elementType = type;
				}
			}
			markDirty();
			return ret;
		}
	}

	@Override
	public int extractElement(int count, ElementType type, boolean simulate) {
		if (type != this.elementType) {
			return 0;
		}
		int newCount = Math.max(elementAmount - count, 0);
		int ret = elementAmount - newCount;

		if (!simulate) {
			elementAmount = newCount;
			if (this.elementAmount <= 0) {
				this.elementType = ElementType.NONE;
			}
		}
		markDirty();
		return ret;
	}

	public void markDirty() {
		if (syncCallback != null) {
			syncCallback.run();
		}
	}
}
