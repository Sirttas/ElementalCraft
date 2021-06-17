package sirttas.elementalcraft.block.pipe;

import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.extensions.IForgeBlock;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.pipe.IElementPipe.ConnectionType;

public interface IPipeConnectedBlock extends IForgeBlock {

	public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
	public static final BooleanProperty EAST = BlockStateProperties.EAST;
	public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
	public static final BooleanProperty WEST = BlockStateProperties.WEST;

	default BlockState doGetStateForPlacement(IBlockReader world, BlockPos pos) {
		return this.getBlock().defaultBlockState()
				.setValue(NORTH, isConnectable(world, pos, Direction.NORTH))
				.setValue(SOUTH, isConnectable(world, pos, Direction.SOUTH))
				.setValue(EAST, isConnectable(world, pos, Direction.EAST))
				.setValue(WEST, isConnectable(world, pos, Direction.WEST));
	}

	default BlockState doUpdatePostPlacement(BlockState stateIn, IBlockReader world, BlockPos pos, Direction facing) {
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
	
	static boolean isConnectable(IBlockReader world, BlockPos from, Direction face) {
		IElementPipe entity = BlockEntityHelper.getTileEntityAs(world, from.relative(face), IElementPipe.class).orElse(null);
		
		if (entity != null) {
			ConnectionType connection = entity.getConection(face);
			
			return connection.isConnected() || connection == ConnectionType.NONE;
		}
		return false;
	}
}
