package sirttas.elementalcraft.api.element.storage;

import sirttas.elementalcraft.api.element.ElementType;

public interface IElementSender extends IElementStorage {
	int extractElement(int count, ElementType type, boolean simulate);

}
