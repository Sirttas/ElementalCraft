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
		return this.getBlock().getDefaultState()
				.with(NORTH, BlockElementPipe.isConnected(world.getBlockState(pos.offset(Direction.NORTH)), BlockElementPipe.SOUTH))
				.with(SOUTH, BlockElementPipe.isConnected(world.getBlockState(pos.offset(Direction.SOUTH)), BlockElementPipe.NORTH))
				.with(EAST, BlockElementPipe.isConnected(world.getBlockState(pos.offset(Direction.EAST)), BlockElementPipe.EAST))
				.with(WEST, BlockElementPipe.isConnected(world.getBlockState(pos.offset(Direction.WEST)), BlockElementPipe.WEST));
	}

	default BlockState doUpdatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState) {
		switch (facing) {
		case NORTH:
			return stateIn.with(NORTH, BlockElementPipe.isConnected(facingState, BlockElementPipe.SOUTH));
		case SOUTH:
			return stateIn.with(SOUTH, BlockElementPipe.isConnected(facingState, BlockElementPipe.NORTH));
		case EAST:
			return stateIn.with(EAST, BlockElementPipe.isConnected(facingState, BlockElementPipe.WEST));
		case WEST:
			return stateIn.with(WEST, BlockElementPipe.isConnected(facingState, BlockElementPipe.EAST));
		default:
			return stateIn;
		}
	}
	
}
