package sirttas.elementalcraft.block.source;

import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.StaticElementStorage;

public class SourceElementStorage extends StaticElementStorage {

	private boolean exhausted = false;

	public SourceElementStorage(int capacity, Runnable syncCallback) {
		super(ElementType.NONE, capacity, syncCallback);
		this.elementAmount = this.elementCapacity;
	}

	protected void setElementType(ElementType elementType) {
		this.elementType = elementType;
	}

	@Override
	public int insertElement(int count, ElementType type, boolean simulate) {
		int value = super.insertElement(count, type, simulate);

		if (!simulate && this.getElementAmount() >= this.elementCapacity * 0.9F) {
			this.exhausted = false;
		}
		return value;
	}

	@Override
	public int extractElement(int count, ElementType type, boolean simulate) {
		if (!exhausted) {
			int value = super.extractElement(count, type, simulate);

			if (!simulate && this.getElementAmount() <= this.elementCapacity * 0.1F) {
				this.exhausted = true;
			}
			return value;
		}
		return 0;
	}

	@Override
	public boolean canPipeInsert(ElementType elementType) {
		return false;
	}

	@Override
	public boolean canPipeExtract(ElementType elementType) {
		return false;
	}

	public boolean isExhausted() {
		return exhausted;
	}

	protected void setExhausted(boolean exausted) {
		this.exhausted = exausted;
	}

}
