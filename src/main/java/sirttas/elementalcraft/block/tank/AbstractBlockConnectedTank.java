package sirttas.elementalcraft.block.tank;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import sirttas.elementalcraft.tag.ECTags;

public abstract class AbstractBlockConnectedTank extends AbstractBlockTank {

	private static final VoxelShape CONNECTOR_NORTH = VoxelShapes.or(Block.makeCuboidShape(5D, 5D, 1D, 11D, 11D, 2D), Block.makeCuboidShape(6D, 6D, 0D, 10D, 10D, 1D));
	private static final VoxelShape CONNECTOR_SOUTH = VoxelShapes.or(Block.makeCuboidShape(5D, 5D, 14D, 11D, 11D, 15D), Block.makeCuboidShape(6D, 6D, 15D, 10D, 10D, 16D));
	private static final VoxelShape CONNECTOR_WEST = VoxelShapes.or(Block.makeCuboidShape(2D, 5D, 5D, 3D, 11D, 11D), Block.makeCuboidShape(0D, 6D, 6D, 1D, 10D, 10D));
	private static final VoxelShape CONNECTOR_EAST = VoxelShapes.or(Block.makeCuboidShape(14D, 5D, 5D, 15D, 11D, 11D), Block.makeCuboidShape(15D, 6D, 6D, 16D, 10D, 10D));

	public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
	public static final BooleanProperty EAST = BlockStateProperties.EAST;
	public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
	public static final BooleanProperty WEST = BlockStateProperties.WEST;
	
	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		VoxelShape shape = VoxelShapes.empty();
		
		if (Boolean.TRUE.equals(state.get(NORTH))) {
			shape = VoxelShapes.or(shape, CONNECTOR_NORTH);
		}
		if (Boolean.TRUE.equals(state.get(SOUTH))) {
			shape = VoxelShapes.or(shape, CONNECTOR_SOUTH);
		}
		if (Boolean.TRUE.equals(state.get(EAST))) {
			shape = VoxelShapes.or(shape, CONNECTOR_EAST);
		}
		if (Boolean.TRUE.equals(state.get(WEST))) {
			shape = VoxelShapes.or(shape, CONNECTOR_WEST);
		}
		return shape;
	}

	private boolean isConnected(IWorld worldIn, BlockPos pos, Direction facing, BooleanProperty opposite) {
		BlockState state = worldIn.getBlockState(pos.offset(facing));
		
		return ECTags.Blocks.PIPES.contains(state.getBlock()) && Boolean.TRUE.equals(state.get(opposite));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState()
				.with(NORTH, isConnected(context.getWorld(), context.getPos(), Direction.NORTH, SOUTH))
				.with(SOUTH, isConnected(context.getWorld(), context.getPos(), Direction.SOUTH, NORTH))
				.with(EAST, isConnected(context.getWorld(), context.getPos(), Direction.EAST, EAST))
				.with(WEST, isConnected(context.getWorld(), context.getPos(), Direction.WEST, WEST));
	}

	@Override
	@Deprecated
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		switch (facing) {
		case NORTH:
			return stateIn.with(NORTH, ECTags.Blocks.PIPES.contains(facingState.getBlock()) && Boolean.TRUE.equals(facingState.get(SOUTH)));
		case SOUTH:
			return stateIn.with(SOUTH, ECTags.Blocks.PIPES.contains(facingState.getBlock()) && Boolean.TRUE.equals(facingState.get(NORTH)));
		case EAST:
			return stateIn.with(EAST, ECTags.Blocks.PIPES.contains(facingState.getBlock()) && Boolean.TRUE.equals(facingState.get(WEST)));
		case WEST:
			return stateIn.with(WEST, ECTags.Blocks.PIPES.contains(facingState.getBlock()) && Boolean.TRUE.equals(facingState.get(EAST)));
		default:
			break;
		}
		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
	
}
