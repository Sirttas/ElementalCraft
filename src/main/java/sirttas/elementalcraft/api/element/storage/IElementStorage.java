package sirttas.elementalcraft.api.element.storage;

import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;

public interface IElementStorage extends IElementTypeProvider {

	int getElementAmount();

	int getElementCapacity();

	int insertElement(int count, ElementType type, boolean simulate);

	int extractElement(int count, ElementType type, boolean simulate);

	default int insertElement(int count, boolean simulate) {
		return insertElement(count, getElementType(), simulate);
	}

	default int extractElement(int count, boolean simulate) {
		return extractElement(count, getElementType(), simulate);
	}

	default int transferTo(IElementStorage other, int count) {
		ElementType type = getElementType();
		int amount = extractElement(count, type, false);

		return count - amount + other.insertElement(amount, type, false);
	}

	default boolean canPipeInsert() {
		return true;
	}

	default boolean canPipeExtract() {
		return true;
	}

	default boolean doesRenderGauge() {
		return false;
	}

	default boolean isEmpty() {
		return getElementType() == ElementType.NONE || getElementAmount() <= 0;
	}
}
