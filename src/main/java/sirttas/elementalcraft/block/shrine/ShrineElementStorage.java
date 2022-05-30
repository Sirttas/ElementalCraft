package sirttas.elementalcraft.block.shrine;

import net.minecraft.core.Direction;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.StaticElementStorage;

public class ShrineElementStorage extends StaticElementStorage {

	private final AbstractShrineBlockEntity shrine;

	public ShrineElementStorage(AbstractShrineBlockEntity shrine) {
		super(ElementType.NONE, 0, shrine::setChanged);
		this.shrine = shrine;
	}

	@Override
	public boolean canPipeExtract(ElementType elementType, Direction side) {
		return false;
	}

	void refresh() {
		this.elementType = shrine.getElementType();
		this.elementCapacity = shrine.getCapacity();
	}
}
