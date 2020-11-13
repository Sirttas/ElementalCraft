package sirttas.elementalcraft.api.element.storage;

import sirttas.elementalcraft.api.element.ElementType;

public interface IElementReceiver extends IElementStorage {
	int inserElement(int count, ElementType type, boolean simulate);

}
