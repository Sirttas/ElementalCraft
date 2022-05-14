package sirttas.elementalcraft.block.shrine;

import net.minecraft.core.Direction;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.StaticElementStorage;

public class ShrineElementStorage extends StaticElementStorage {

	public ShrineElementStorage(ElementType elementType, int capacity, Runnable syncCallback) {
		super(elementType, capacity, syncCallback);
	}

	@Override
	public boolean canPipeExtract(ElementType elementType, Direction side) {
		return false;
	}

	void setCapacity(int capacity) {
		this.elementCapacity = capacity;
	}

}
