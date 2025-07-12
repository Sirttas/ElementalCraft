package sirttas.elementalcraft.block.container;

import net.minecraft.world.entity.player.Player;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.DynamicSingleElementStorage;

public class ElementContainerElementStorage extends DynamicSingleElementStorage {

	private final ElementContainerBlockEntity container;

	public ElementContainerElementStorage(ElementContainerBlockEntity container) {
		super(container.getProperties().capacity(), container::setChanged);
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
	public boolean doesRenderGauge(Player player) {
		return true;
	}

	void setElementType(ElementType elementType) {
		this.elementType = elementType;
	}
}
