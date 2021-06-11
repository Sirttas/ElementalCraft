package sirttas.elementalcraft.block.container;

import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;

public interface IElementContainer {

	boolean isSmall();

	ISingleElementStorage getElementStorage();
}