package sirttas.elementalcraft.block.pureinfuser.pedestal;

import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.capability.SingleElementStorage;

public class PedestalElementStorage extends SingleElementStorage {

	public PedestalElementStorage(ElementType elementType, Runnable syncCallback) {
		super(elementType, 10000, syncCallback);
	}

	@Override
	public boolean canPipeExtract() {
		return false;
	}

}
