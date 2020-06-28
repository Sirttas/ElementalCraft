package sirttas.elementalcraft.block.tile.element;

import sirttas.elementalcraft.ElementType;

public interface IElementStorage {

	int getElementAmount();

	ElementType getElementType();

	int getMaxElement();

	default boolean doesRenderGauge() {
		return false;
	}

}
