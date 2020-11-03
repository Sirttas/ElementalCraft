package sirttas.elementalcraft.api.element;

import sirttas.elementalcraft.ElementType;

public interface IElementSender extends IElementStorage {
	int extractElement(int count, ElementType type, boolean simulate);

}
