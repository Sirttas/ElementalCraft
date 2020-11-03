package sirttas.elementalcraft.api.element;

import sirttas.elementalcraft.ElementType;

public interface IElementStorage {

	int getElementAmount();

	ElementType getElementType();

	int getElementCapacity();

	default boolean doesRenderGauge() {
		return false;
	}

}
