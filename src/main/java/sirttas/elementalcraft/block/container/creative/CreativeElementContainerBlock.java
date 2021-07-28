package sirttas.elementalcraft.block.container.creative;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.block.container.ElementContainerBlock;

public class CreativeElementContainerBlock extends ElementContainerBlock {

	public static final String NAME = "creative_container";

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CreativeElementContainerBlockEntity(pos, state);
	}
	
	@Override
	protected int getDefaultCapacity() {
		return 1000000;
	}
}
