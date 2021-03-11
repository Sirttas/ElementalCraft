package sirttas.elementalcraft.block.pureinfuser.pedestal;

import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.StaticElementStorage;
import sirttas.elementalcraft.config.ECConfig;

public class PedestalElementStorage extends StaticElementStorage {

	public PedestalElementStorage(ElementType elementType, Runnable syncCallback) {
		super(elementType, ECConfig.COMMON.pedestalCapacity.get(), syncCallback);
	}

	@Override
	public boolean canPipeExtract(ElementType elementType) {
		return false;
	}

}
