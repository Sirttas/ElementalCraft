package sirttas.elementalcraft.block.container.reservoir;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.container.AbstractElementContainerBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;

public class ReservoirBlockEntity extends AbstractElementContainerBlockEntity {
	
	public ReservoirBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.RESERVOIR, self -> new ReservoirElementStorage(ElementType.getElementType(state), self.getProperties().capacity(), self::setChanged), pos, state);
	}

    @Override
	protected void setElementType(ElementType type) {
		// NO-OP
	}
}
