package sirttas.elementalcraft.block.container;

import net.minecraft.world.level.block.entity.BlockEntity;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;

public interface IContainerTopBlockEntity {

	private BlockEntity self() {
		return (BlockEntity) this;
	}

	default ISingleElementStorage getContainer() {
		var self = self();

		//noinspection ConstantConditions
		return self.hasLevel() ? ElementContainer.getElementContainer(self.getBlockState(), self.getLevel(), self.getBlockPos().below()) : null;
	}

	default ElementType getContainerElementType() {
		ISingleElementStorage container = getContainer();

		return container != null ? container.getElementType() : ElementType.NONE;
	}

}
