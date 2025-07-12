package sirttas.elementalcraft.api.element.storage.single;

import net.minecraft.core.Direction;
import sirttas.elementalcraft.api.element.ElementType;

public class DynamicSingleElementStorage extends SingleElementStorage {

	public DynamicSingleElementStorage(int elementCapacity) {
		this(elementCapacity, null);
	}

	public DynamicSingleElementStorage(int elementCapacity, Runnable syncCallback) {
		this(ElementType.NONE, 0, elementCapacity, syncCallback);
	}

	public DynamicSingleElementStorage(ElementType elementType, int elementAmount, int elementCapacity) {
		this(elementType, elementAmount, elementCapacity, null);
	}
		
	private DynamicSingleElementStorage(ElementType elementType, int elementAmount, int elementCapacity, Runnable syncCallback) {
		super(elementType, elementAmount, elementCapacity, syncCallback);
	}

	@Override
	public int getElementCapacity(ElementType type) {
		return this.getElementType() == ElementType.NONE ? this.getElementCapacity() : super.getElementCapacity(type);
	}

	@Override
	public int insertElement(int count, ElementType type, boolean simulate) {
		if (type != this.elementType && this.elementType != ElementType.NONE) {
			return count - this.extractElement(count, simulate);
		}
		int ret = super.insertElement(count, this.elementType == ElementType.NONE ? ElementType.NONE : type, simulate);

		if (!simulate && this.elementType == ElementType.NONE) {
			this.elementType = type;
		}
		return ret;
	}

	@Override
	public int extractElement(int count, ElementType type, boolean simulate) {
		var ret = super.extractElement(count, type, simulate);

		if (!simulate && this.elementAmount == 0) {
			this.elementType = ElementType.NONE;
		}
		return ret;
	}

	@Override
	public boolean canPipeInsert(ElementType type, Direction side) {
		ElementType localType = this.getElementType();

		return type != ElementType.NONE && (localType == ElementType.NONE || type == localType);
	}
}
