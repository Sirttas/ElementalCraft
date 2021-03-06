package sirttas.elementalcraft.block.container;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import sirttas.elementalcraft.block.pipe.IPipeConnectedBlock;

public abstract class AbstractConnectedElementContainerBlock extends AbstractElementContainerBlock implements IPipeConnectedBlock {

	private static final VoxelShape CONNECTOR_NORTH = VoxelShapes.or(Block.box(5D, 5D, 1D, 11D, 11D, 2D), Block.box(6D, 6D, 0D, 10D, 10D, 1D));
	private static final VoxelShape CONNECTOR_SOUTH = VoxelShapes.or(Block.box(5D, 5D, 14D, 11D, 11D, 15D), Block.box(6D, 6D, 15D, 10D, 10D, 16D));
	private static final VoxelShape CONNECTOR_WEST = VoxelShapes.or(Block.box(2D, 5D, 5D, 3D, 11D, 11D), Block.box(0D, 6D, 6D, 1D, 10D, 10D));
	private static final VoxelShape CONNECTOR_EAST = VoxelShapes.or(Block.box(14D, 5D, 5D, 15D, 11D, 11D), Block.box(15D, 6D, 6D, 16D, 10D, 10D));
	
	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		VoxelShape shape = VoxelShapes.empty();
		
		if (Boolean.TRUE.equals(state.getValue(NORTH))) {
			shape = VoxelShapes.or(shape, CONNECTOR_NORTH);
		}
		if (Boolean.TRUE.equals(state.getValue(SOUTH))) {
			shape = VoxelShapes.or(shape, CONNECTOR_SOUTH);
		}
		if (Boolean.TRUE.equals(state.getValue(EAST))) {
			shape = VoxelShapes.or(shape, CONNECTOR_EAST);
		}
		if (Boolean.TRUE.equals(state.getValue(WEST))) {
			shape = VoxelShapes.or(shape, CONNECTOR_WEST);
		}
		return shape;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return doGetStateForPlacement(context.getLevel(), context.getClickedPos());
	}

	@Override
	@Deprecated
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		return doUpdatePostPlacement(stateIn, worldIn, currentPos, facing);
	}
	
}
