package sirttas.elementalcraft.block.container;

import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.container.IElementStorageBlocKEntity;

public interface IElementContainer extends IElementStorageBlocKEntity {

	boolean isSmall();

	@Override
	ISingleElementStorage getElementStorage();
}
