package sirttas.elementalcraft.block.container.creative;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.container.AbstractElementContainerBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;

public class CreativeElementContainerBlockEntity extends AbstractElementContainerBlockEntity {

	public CreativeElementContainerBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.CREATIVE_CONTAINER, self -> new CreativeElementStorage(self::setChanged), pos, state);
	}

	@Override
	protected void setElementType(ElementType type) {
		((CreativeElementStorage) elementStorage).setElementType(type);
	}
}
