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

	default BlockState doGetStateForPlacement(BlockGetter level, BlockPos pos) {
		return ((Block) this).defaultBlockState()
				.setValue(NORTH, isConnectable(level, pos, Direction.NORTH))
				.setValue(SOUTH, isConnectable(level, pos, Direction.SOUTH))
				.setValue(EAST, isConnectable(level, pos, Direction.EAST))
				.setValue(WEST, isConnectable(level, pos, Direction.WEST));
	}

	default BlockState doUpdateShape(BlockState state, BlockGetter level, BlockPos pos, Direction facing) {
		return switch (facing) {
			case NORTH -> state.setValue(NORTH, isConnectable(level, pos, Direction.NORTH));
			case SOUTH -> state.setValue(SOUTH, isConnectable(level, pos, Direction.SOUTH));
			case EAST -> state.setValue(EAST, isConnectable(level, pos, Direction.EAST));
			case WEST -> state.setValue(WEST, isConnectable(level, pos, Direction.WEST));
			default -> state;
		};
	}
	
	static boolean isConnectable(BlockGetter level, BlockPos from, Direction face) {
		var opposite = face.getOpposite();
		IElementTransferer transferer = BlockEntityHelper.getBlockEntity(level, from.relative(face))
				.flatMap(b -> ElementTransfererHelper.get(b, opposite).resolve())
				.orElse(null);
		
		if (transferer instanceof ElementPipeTransferer elementPipeTransferer) {
			ConnectionType connection = elementPipeTransferer.getConnection(opposite);
			
			return connection.isConnected() || connection == ConnectionType.NONE;
		}
		return false;
	}
}
