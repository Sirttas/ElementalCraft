package sirttas.elementalcraft.api.element.storage.single;

import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.IElementStorage;

public interface ISingleElementStorage extends IElementStorage, IElementTypeProvider {

	int getElementAmount();

	int getElementCapacity();

	default int insertElement(int count, boolean simulate) {
		return insertElement(count, getElementType(), simulate);
	}

	default int extractElement(int count, boolean simulate) {
		return extractElement(count, getElementType(), simulate);
	}

	default int transferTo(IElementStorage other, int count) {
		return transferTo(other, getElementType(), count);
	}

	default int transferTo(IElementStorage other, float count, float multiplier) {
		return transferTo(other, getElementType(), count, multiplier);
	}

	@Override
	default int getElementAmount(ElementType type) {
		return type == this.getElementType() ? this.getElementAmount() : 0;
	}

	@Override
	default int getElementCapacity(ElementType type) {
		return type == this.getElementType() ? this.getElementCapacity() : 0;
	}

	@Override
	default boolean isEmpty() {
		return getElementType() == ElementType.NONE || getElementAmount() <= 0;
	}

	@Override
	default boolean canPipeInsert(ElementType type) {
		ElementType localType = this.getElementType();
		
		return type != ElementType.NONE && (localType == ElementType.NONE || type == localType);
	}

	@Override
	default boolean canPipeExtract(ElementType type) {
		ElementType localType = this.getElementType();
		
		return type != ElementType.NONE && (localType == ElementType.NONE || type == localType);
	}
}
