package sirttas.elementalcraft.api.element.storage;

import sirttas.elementalcraft.api.element.ElementType;

public interface IElementStorage {

	int getElementAmount();

	ElementType getElementType();

	int getElementCapacity();

	default boolean doesRenderGauge() {
		return false;
	}

}
