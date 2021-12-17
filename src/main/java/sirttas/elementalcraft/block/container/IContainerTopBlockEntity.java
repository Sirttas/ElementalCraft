package sirttas.elementalcraft.block.container;

import net.minecraft.world.level.block.entity.BlockEntity;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;

public interface IContainerTopBlockEntity {

	private BlockEntity self() {
		return (BlockEntity) this;
	}

	default ISingleElementStorage getContainer() {
		var self = self();

		//noinspection ConstantConditions
		return self.hasLevel() ? BlockEntityHelper.getElementContainer(self.getBlockState().getBlock(), self.getLevel(), self.getBlockPos().below()).orElse(null) : null;
	}

	default ElementType getContainerElementType() {
		ISingleElementStorage tank = getContainer();

		return tank != null ? tank.getElementType() : ElementType.NONE;
	}

}
