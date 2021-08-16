package sirttas.elementalcraft.block.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.pipe.IElementPipe.ConnectionType;

public interface IPipeConnectedBlock {

	public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
	public static final BooleanProperty EAST = BlockStateProperties.EAST;
	public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
	public static final BooleanProperty WEST = BlockStateProperties.WEST;

	default BlockState doGetStateForPlacement(BlockGetter world, BlockPos pos) {
		return ((Block) this).defaultBlockState()
				.setValue(NORTH, isConnectable(world, pos, Direction.NORTH))
				.setValue(SOUTH, isConnectable(world, pos, Direction.SOUTH))
				.setValue(EAST, isConnectable(world, pos, Direction.EAST))
				.setValue(WEST, isConnectable(world, pos, Direction.WEST));
	}

	default BlockState doUpdateShape(BlockState stateIn, BlockGetter world, BlockPos pos, Direction facing) {
		switch (facing) {
		case NORTH:
			return stateIn.setValue(NORTH, isConnectable(world, pos, Direction.NORTH));
		case SOUTH:
			return stateIn.setValue(SOUTH, isConnectable(world, pos, Direction.SOUTH));
		case EAST:
			return stateIn.setValue(EAST, isConnectable(world, pos, Direction.WEST));
		case WEST:
			return stateIn.setValue(WEST, isConnectable(world, pos, Direction.EAST));
		default:
			return stateIn;
		}
	}
	
	static boolean isConnectable(BlockGetter world, BlockPos from, Direction face) {
		IElementPipe entity = BlockEntityHelper.getTileEntityAs(world, from.relative(face), IElementPipe.class).orElse(null);
		
		if (entity != null) {
			ConnectionType connection = entity.getConection(face);
			
			return connection.isConnected() || connection == ConnectionType.NONE;
		}
		return false;
	}
}
