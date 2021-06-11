package sirttas.elementalcraft.block.pipe;

import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.extensions.IForgeBlock;

public interface IPipeConnectedBlock extends IForgeBlock {

	public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
	public static final BooleanProperty EAST = BlockStateProperties.EAST;
	public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
	public static final BooleanProperty WEST = BlockStateProperties.WEST;

	default BlockState doGetStateForPlacement(IBlockReader world, BlockPos pos) {
		return this.getBlock().defaultBlockState()
				.setValue(NORTH, ElementPipeBlock.isConnected(world.getBlockState(pos.relative(Direction.NORTH)), ElementPipeBlock.SOUTH))
				.setValue(SOUTH, ElementPipeBlock.isConnected(world.getBlockState(pos.relative(Direction.SOUTH)), ElementPipeBlock.NORTH))
				.setValue(EAST, ElementPipeBlock.isConnected(world.getBlockState(pos.relative(Direction.EAST)), ElementPipeBlock.EAST))
				.setValue(WEST, ElementPipeBlock.isConnected(world.getBlockState(pos.relative(Direction.WEST)), ElementPipeBlock.WEST));
	}

	default BlockState doUpdatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState) {
		switch (facing) {
		case NORTH:
			return stateIn.setValue(NORTH, ElementPipeBlock.isConnected(facingState, ElementPipeBlock.SOUTH));
		case SOUTH:
			return stateIn.setValue(SOUTH, ElementPipeBlock.isConnected(facingState, ElementPipeBlock.NORTH));
		case EAST:
			return stateIn.setValue(EAST, ElementPipeBlock.isConnected(facingState, ElementPipeBlock.WEST));
		case WEST:
			return stateIn.setValue(WEST, ElementPipeBlock.isConnected(facingState, ElementPipeBlock.EAST));
		default:
			return stateIn;
		}
	}
	
}
