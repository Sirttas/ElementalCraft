package sirttas.elementalcraft.block.shrine.upgrade.directional;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class BlockOptimizationShrineUpgrade extends AbstractBlockDirectionalShrineUpgrade {

	public static final String NAME = "shrine_upgrade_optimization";

	private static final VoxelShape BASE_1_UP = Block.makeCuboidShape(6D, 10D, 6D, 10D, 12D, 10D);
	private static final VoxelShape BASE_2_UP = Block.makeCuboidShape(5D, 4D, 5D, 11D, 10D, 11D);
	private static final VoxelShape PIPE_1_UP = Block.makeCuboidShape(7D, 12D, 7D, 9D, 16D, 9D);
	private static final VoxelShape PIPE_2_UP = Block.makeCuboidShape(7D, 2D, 7D, 9D, 4D, 9D);
	private static final VoxelShape PLATE_UP = Block.makeCuboidShape(4D, 0D, 4D, 12D, 2D, 12D);
	private static final VoxelShape SHAPE_UP = VoxelShapes.or(BASE_1_UP, BASE_2_UP, PIPE_1_UP, PIPE_2_UP, PLATE_UP);

	private static final VoxelShape BASE_1_DOWN = Block.makeCuboidShape(6D, 4D, 6D, 10D, 6D, 10D);
	private static final VoxelShape BASE_2_DOWN = Block.makeCuboidShape(5D, 6D, 5D, 11D, 12D, 11D);
	private static final VoxelShape PIPE_1_DOWN = Block.makeCuboidShape(7D, 0D, 7D, 9D, 4D, 9D);
	private static final VoxelShape PIPE_2_DOWN = Block.makeCuboidShape(7D, 12D, 7D, 9D, 14D, 9D);
	private static final VoxelShape PLATE_DOWN = Block.makeCuboidShape(4D, 14D, 4D, 12D, 16D, 12D);
	private static final VoxelShape SHAPE_DOWN = VoxelShapes.or(BASE_1_DOWN, BASE_2_DOWN, PIPE_1_DOWN, PIPE_2_DOWN, PLATE_DOWN);

	private static final VoxelShape BASE_1_NORTH = Block.makeCuboidShape(6D, 6D, 4D, 10D, 10D, 6D);
	private static final VoxelShape BASE_2_NORTH = Block.makeCuboidShape(5D, 5D, 6D, 11D, 11D, 12D);
	private static final VoxelShape PIPE_1_NORTH = Block.makeCuboidShape(7D, 7D, 0D, 9D, 9D, 4D);
	private static final VoxelShape PIPE_2_NORTH = Block.makeCuboidShape(7D, 7D, 12D, 9D, 9D, 14D);
	private static final VoxelShape PLATE_NORTH = Block.makeCuboidShape(4D, 4D, 14D, 12D, 12D, 16D);
	private static final VoxelShape SHAPE_NORTH = VoxelShapes.or(BASE_1_NORTH, BASE_2_NORTH, PIPE_1_NORTH, PIPE_2_NORTH, PLATE_NORTH);

	private static final VoxelShape BASE_1_SOUTH = Block.makeCuboidShape(6D, 6D, 10D, 10D, 10D, 12D);
	private static final VoxelShape BASE_2_SOUTH = Block.makeCuboidShape(5D, 5D, 4D, 11D, 11D, 10D);
	private static final VoxelShape PIPE_1_SOUTH = Block.makeCuboidShape(7D, 7D, 12D, 9D, 9D, 16D);
	private static final VoxelShape PIPE_2_SOUTH = Block.makeCuboidShape(7D, 7D, 2D, 9D, 9D, 4D);
	private static final VoxelShape PLATE_SOUTH = Block.makeCuboidShape(4D, 4D, 0D, 12D, 12D, 2D);
	private static final VoxelShape SHAPE_SOUTH = VoxelShapes.or(BASE_1_SOUTH, BASE_2_SOUTH, PIPE_1_SOUTH, PIPE_2_SOUTH, PLATE_SOUTH);

	private static final VoxelShape BASE_1_WEST = Block.makeCuboidShape(4D, 6D, 6D, 6D, 10D, 10D);
	private static final VoxelShape BASE_2_WEST = Block.makeCuboidShape(6D, 5D, 5D, 12D, 11D, 11D);
	private static final VoxelShape PIPE_1_WEST = Block.makeCuboidShape(0D, 7D, 7D, 4D, 9D, 9D);
	private static final VoxelShape PIPE_2_WEST = Block.makeCuboidShape(12D, 7D, 7D, 14D, 9D, 9D);
	private static final VoxelShape PLATE_WEST = Block.makeCuboidShape(14D, 4D, 4D, 16D, 12D, 12D);
	private static final VoxelShape SHAPE_WEST = VoxelShapes.or(BASE_1_WEST, BASE_2_WEST, PIPE_1_WEST, PIPE_2_WEST, PLATE_WEST);

	private static final VoxelShape BASE_1_EAST = Block.makeCuboidShape(10D, 6D, 6D, 12D, 10D, 10D);
	private static final VoxelShape BASE_2_EAST = Block.makeCuboidShape(4D, 5D, 5D, 10D, 11D, 11D);
	private static final VoxelShape PIPE_1_EAST = Block.makeCuboidShape(12D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape PIPE_2_EAST = Block.makeCuboidShape(2D, 7D, 7D, 4D, 9D, 9D);
	private static final VoxelShape PLATE_EAST = Block.makeCuboidShape(0D, 4D, 4D, 2D, 12D, 12D);
	private static final VoxelShape SHAPE_EAST = VoxelShapes.or(BASE_1_EAST, BASE_2_EAST, PIPE_1_EAST, PIPE_2_EAST, PLATE_EAST);

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch (state.get(FACING)) {
		case DOWN:
			return SHAPE_DOWN;
		case EAST:
			return SHAPE_EAST;
		case NORTH:
			return SHAPE_NORTH;
		case SOUTH:
			return SHAPE_SOUTH;
		case WEST:
			return SHAPE_WEST;
		default:
			return SHAPE_UP;
		}
	}
}
