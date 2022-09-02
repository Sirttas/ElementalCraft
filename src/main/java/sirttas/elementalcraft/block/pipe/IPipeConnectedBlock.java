package sirttas.elementalcraft.block.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import sirttas.elementalcraft.api.element.transfer.ElementTransfererHelper;
import sirttas.elementalcraft.api.element.transfer.IElementTransferer;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;

public interface IPipeConnectedBlock {

	BooleanProperty NORTH = BlockStateProperties.NORTH;
	BooleanProperty EAST = BlockStateProperties.EAST;
	BooleanProperty SOUTH = BlockStateProperties.SOUTH;
	BooleanProperty WEST = BlockStateProperties.WEST;

	default BlockState doGetStateForPlacement(BlockGetter world, BlockPos pos) {
		return ((Block) this).defaultBlockState()
				.setValue(NORTH, isConnectable(world, pos, Direction.NORTH))
				.setValue(SOUTH, isConnectable(world, pos, Direction.SOUTH))
				.setValue(EAST, isConnectable(world, pos, Direction.EAST))
				.setValue(WEST, isConnectable(world, pos, Direction.WEST));
	}

	default BlockState doUpdateShape(BlockState state, BlockGetter world, BlockPos pos, Direction facing) {
		return switch (facing) {
			case NORTH -> state.setValue(NORTH, isConnectable(world, pos, Direction.NORTH));
			case SOUTH -> state.setValue(SOUTH, isConnectable(world, pos, Direction.SOUTH));
			case EAST -> state.setValue(EAST, isConnectable(world, pos, Direction.EAST));
			case WEST -> state.setValue(WEST, isConnectable(world, pos, Direction.WEST));
			default -> state;
		};
	}
	
	static boolean isConnectable(BlockGetter world, BlockPos from, Direction face) {
		var opposite = face.getOpposite();
		IElementTransferer transferer = BlockEntityHelper.getBlockEntity(world, from.relative(face))
				.flatMap(b -> ElementTransfererHelper.get(b, opposite).resolve())
				.orElse(null);
		
		if (transferer != null) {
			IElementTransferer.ConnectionType connection = transferer.getConnection(opposite);
			
			return connection.isConnected() || connection == IElementTransferer.ConnectionType.NONE;
		}
		return false;
	}
}
