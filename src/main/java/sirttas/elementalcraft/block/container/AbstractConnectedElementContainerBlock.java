package sirttas.elementalcraft.block.container;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.block.pipe.IPipeConnectedBlock;

public abstract class AbstractConnectedElementContainerBlock extends AbstractElementContainerBlock implements IPipeConnectedBlock {

	private static final VoxelShape CONNECTOR_NORTH = Shapes.or(Block.box(5D, 5D, 1D, 11D, 11D, 2D), Block.box(6D, 6D, 0D, 10D, 10D, 1D));
	private static final VoxelShape CONNECTOR_SOUTH = Shapes.or(Block.box(5D, 5D, 14D, 11D, 11D, 15D), Block.box(6D, 6D, 15D, 10D, 10D, 16D));
	private static final VoxelShape CONNECTOR_WEST = Shapes.or(Block.box(2D, 5D, 5D, 3D, 11D, 11D), Block.box(0D, 6D, 6D, 1D, 10D, 10D));
	private static final VoxelShape CONNECTOR_EAST = Shapes.or(Block.box(14D, 5D, 5D, 15D, 11D, 11D), Block.box(15D, 6D, 6D, 16D, 10D, 10D));
	
	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		VoxelShape shape = Shapes.empty();
		
		if (Boolean.TRUE.equals(state.getValue(NORTH))) {
			shape = Shapes.or(shape, CONNECTOR_NORTH);
		}
		if (Boolean.TRUE.equals(state.getValue(SOUTH))) {
			shape = Shapes.or(shape, CONNECTOR_SOUTH);
		}
		if (Boolean.TRUE.equals(state.getValue(EAST))) {
			shape = Shapes.or(shape, CONNECTOR_EAST);
		}
		if (Boolean.TRUE.equals(state.getValue(WEST))) {
			shape = Shapes.or(shape, CONNECTOR_WEST);
		}
		return shape;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return doGetStateForPlacement(context.getLevel(), context.getClickedPos());
	}

	@Override
	@Deprecated
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		return doUpdateShape(stateIn, worldIn, currentPos, facing);
	}
	
}
