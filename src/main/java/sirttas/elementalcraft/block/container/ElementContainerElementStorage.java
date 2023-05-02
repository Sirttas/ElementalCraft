package sirttas.elementalcraft.block.container;

import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;

public class ElementContainerElementStorage extends SingleElementStorage {

	private final ElementContainerBlockEntity container;

	public ElementContainerElementStorage(ElementContainerBlockEntity container, int elementCapacity) {
		super(elementCapacity, container::setChanged);
		this.container = container;
	}

	@Override
	public int insertElement(int count, ElementType type, boolean simulate) {
		var value = super.insertElement(count, type, simulate);

		if (!simulate && type != this.elementType && this.elementType != ElementType.NONE && value < count) {
			container.onWrongElementInserted();
		}
		return value;
	}

	@Override
	public boolean doesRenderGauge() {
		return true;
	}
}
