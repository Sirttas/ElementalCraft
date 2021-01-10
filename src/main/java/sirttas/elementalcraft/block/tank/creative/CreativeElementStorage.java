package sirttas.elementalcraft.block.tank.creative;

import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.tank.ContainerElementStorage;

public class CreativeElementStorage extends ContainerElementStorage {

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
}
