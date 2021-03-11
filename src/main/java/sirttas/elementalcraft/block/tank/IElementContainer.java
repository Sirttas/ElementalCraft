package sirttas.elementalcraft.block.tank;

import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;

public interface IElementContainer {

	boolean isSmall();

	ISingleElementStorage getElementStorage();
}