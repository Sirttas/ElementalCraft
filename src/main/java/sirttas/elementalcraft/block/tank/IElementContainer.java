package sirttas.elementalcraft.block.tank;

import sirttas.elementalcraft.api.element.storage.IElementStorage;

public interface IElementContainer {

	boolean isSmall();

	IElementStorage getElementStorage();
}