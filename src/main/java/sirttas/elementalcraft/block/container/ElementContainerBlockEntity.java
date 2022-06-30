package sirttas.elementalcraft.block.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.config.ECConfig;

public class ElementContainerBlockEntity extends AbstractElementContainerBlockEntity {


	public ElementContainerBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.CONTAINER, pos, state, sync -> new ElementContainerElementStorage(state.getBlock() == ECBlocks.SMALL_CONTAINER.get() ? ECConfig.COMMON.tankSmallCapacity.get() : ECConfig.COMMON.tankCapacity.get(), sync));
	}

	@Override
	public boolean isSmall() {
		return this.getBlockState().getBlock() == ECBlocks.SMALL_CONTAINER.get();
	}
}
