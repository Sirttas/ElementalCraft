package sirttas.elementalcraft.block.container.creative;

import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;

public class CreativeElementStorage extends SingleElementStorage {

	public CreativeElementStorage(Runnable syncCallback) {
		super(1000000, syncCallback);
	}

	@Override
	public int insertElement(int count, ElementType type, boolean simulate) {
		if (!simulate) {
			this.elementType = type;
			this.markDirty();
		}
		return 0;
	}

	@Override
	public int extractElement(int count, ElementType type, boolean simulate) {
		return count;
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}

	@Override
	public int getElementAmount() {
		return elementCapacity;
	}

	@Override
	public int getElementCapacity() {
		return elementCapacity;
	}

	@Override
	public boolean doesRenderGauge() {
		return true;
	}
}
