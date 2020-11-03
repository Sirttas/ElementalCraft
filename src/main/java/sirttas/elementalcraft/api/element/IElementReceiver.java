package sirttas.elementalcraft.api.element;

import sirttas.elementalcraft.ElementType;

public interface IElementReceiver extends IElementStorage {
	int inserElement(int count, ElementType type, boolean simulate);

}
