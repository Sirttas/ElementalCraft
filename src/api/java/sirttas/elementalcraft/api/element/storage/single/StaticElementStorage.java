package sirttas.elementalcraft.api.element.storage.single;

import net.minecraft.core.Direction;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.EmptyElementStorage;

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
	
	@Override
	public ISingleElementStorage forElement(ElementType type) {
		if (type != elementType) {
			return EmptyElementStorage.getSingle(type);
		}
		return this;
	}

	@Override
	public boolean canPipeInsert(ElementType type, Direction side) {
		return isValidType(type);
	}

	@Override
	public boolean canPipeExtract(ElementType type, Direction side) {
		return isValidType(type);
	}

	private boolean isValidType(ElementType type) {
		ElementType localType = this.getElementType();

		return type != ElementType.NONE && (localType == ElementType.NONE || type == localType);
	}
}
