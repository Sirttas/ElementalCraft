package sirttas.elementalcraft.block.shrine.upgrade.directional;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class BlockStrengthShrineUpgrade extends AbstractBlockDirectionalShrineUpgrade {

	public static final String NAME = "shrine_upgrade_strength";

	private static final VoxelShape BASE_UP = Block.makeCuboidShape(6D, 7D, 6D, 10D, 11D, 10D);
	private static final VoxelShape PIPE_UP = Block.makeCuboidShape(7D, 11D, 7D, 9D, 16D, 9D);
	private static final VoxelShape PIPE_SIDE_1_UP = Block.makeCuboidShape(4D, 8D, 7D, 6D, 10D, 9D);
	private static final VoxelShape PIPE_SIDE_2_UP = Block.makeCuboidShape(10D, 8D, 7D, 12D, 10D, 9D);
	private static final VoxelShape PLATE_1_UP = Block.makeCuboidShape(3D, 6D, 5D, 4D, 12D, 11D);
	private static final VoxelShape PLATE_2_UP = Block.makeCuboidShape(12D, 6D, 5D, 13D, 12D, 11D);
	private static final VoxelShape SHAPE_UP = VoxelShapes.or(BASE_UP, PIPE_UP, PIPE_SIDE_1_UP, PIPE_SIDE_2_UP, PLATE_1_UP, PLATE_2_UP);

	private static final VoxelShape BASE_DOWN = Block.makeCuboidShape(6D, 5D, 6D, 10D, 9D, 10D);
	private static final VoxelShape PIPE_DOWN = Block.makeCuboidShape(7D, 0D, 7D, 9D, 5D, 9D);
	private static final VoxelShape PIPE_SIDE_1_DOWN = Block.makeCuboidShape(4D, 6D, 7D, 6D, 8D, 9D);
	private static final VoxelShape PIPE_SIDE_2_DOWN = Block.makeCuboidShape(10D, 6D, 7D, 12D, 8D, 9D);
	private static final VoxelShape PLATE_1_DOWN = Block.makeCuboidShape(3D, 4D, 5D, 4D, 10D, 11D);
	private static final VoxelShape PLATE_2_DOWN = Block.makeCuboidShape(12D, 4D, 5D, 13D, 10D, 11D);
	private static final VoxelShape SHAPE_DOWN = VoxelShapes.or(BASE_DOWN, PIPE_DOWN, PIPE_SIDE_1_DOWN, PIPE_SIDE_2_DOWN, PLATE_1_DOWN, PLATE_2_DOWN);

	private static final VoxelShape BASE_NORTH = Block.makeCuboidShape(6D, 6D, 5D, 10D, 10D, 9D);
	private static final VoxelShape PIPE_NORTH = Block.makeCuboidShape(7D, 7D, 0D, 9D, 9D, 5D);
	private static final VoxelShape PIPE_SIDE_1_NORTH = Block.makeCuboidShape(4D, 7D, 6D, 6D, 9D, 8D);
	private static final VoxelShape PIPE_SIDE_2_NORTH = Block.makeCuboidShape(10D, 7D, 6D, 12D, 9D, 8D);
	private static final VoxelShape PLATE_1_NORTH = Block.makeCuboidShape(3D, 5D, 4D, 4D, 11D, 10D);
	private static final VoxelShape PLATE_2_NORTH = Block.makeCuboidShape(12D, 5D, 4D, 13D, 11D, 10D);
	private static final VoxelShape SHAPE_NORTH = VoxelShapes.or(BASE_NORTH, PIPE_NORTH, PIPE_SIDE_1_NORTH, PIPE_SIDE_2_NORTH, PLATE_1_NORTH, PLATE_2_NORTH);

	private static final VoxelShape BASE_SOUTH = Block.makeCuboidShape(6D, 6D, 7D, 10D, 10D, 11D);
	private static final VoxelShape PIPE_SOUTH = Block.makeCuboidShape(7D, 7D, 11D, 9D, 9D, 16D);
	private static final VoxelShape PIPE_SIDE_1_SOUTH = Block.makeCuboidShape(4D, 7D, 8D, 6D, 9D, 10D);
	private static final VoxelShape PIPE_SIDE_2_SOUTH = Block.makeCuboidShape(10D, 7D, 8D, 12D, 9D, 10D);
	private static final VoxelShape PLATE_1_SOUTH = Block.makeCuboidShape(3D, 5D, 6D, 4D, 11D, 12D);
	private static final VoxelShape PLATE_2_SOUTH = Block.makeCuboidShape(12D, 5D, 6D, 13D, 11D, 12D);
	private static final VoxelShape SHAPE_SOUTH = VoxelShapes.or(BASE_SOUTH, PIPE_SOUTH, PIPE_SIDE_1_SOUTH, PIPE_SIDE_2_SOUTH, PLATE_1_SOUTH, PLATE_2_SOUTH);

	private static final VoxelShape BASE_WEST = Block.makeCuboidShape(5D, 6D, 6D, 9D, 10D, 10D);
	private static final VoxelShape PIPE_WEST = Block.makeCuboidShape(0D, 7D, 7D, 5D, 9D, 9D);
	private static final VoxelShape PIPE_SIDE_1_WEST = Block.makeCuboidShape(6D, 7D, 4D, 8D, 9D, 6D);
	private static final VoxelShape PIPE_SIDE_2_WEST = Block.makeCuboidShape(6D, 7D, 10D, 8D, 9D, 12D);
	private static final VoxelShape PLATE_1_WEST = Block.makeCuboidShape(4D, 5D, 3D, 10D, 11D, 4D);
	private static final VoxelShape PLATE_2_WEST = Block.makeCuboidShape(4D, 5D, 12D, 10D, 11D, 13D);
	private static final VoxelShape SHAPE_WEST = VoxelShapes.or(BASE_WEST, PIPE_WEST, PIPE_SIDE_1_WEST, PIPE_SIDE_2_WEST, PLATE_1_WEST, PLATE_2_WEST);

	private static final VoxelShape BASE_EAST = Block.makeCuboidShape(7D, 6D, 6D, 11D, 10D, 10D);
	private static final VoxelShape PIPE_EAST = Block.makeCuboidShape(11D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape PIPE_SIDE_1_EAST = Block.makeCuboidShape(8D, 7D, 4D, 10D, 9D, 6D);
	private static final VoxelShape PIPE_SIDE_2_EAST = Block.makeCuboidShape(8D, 7D, 10D, 10D, 9D, 12D);
	private static final VoxelShape PLATE_1_EAST = Block.makeCuboidShape(6D, 5D, 3D, 12D, 11D, 4D);
	private static final VoxelShape PLATE_2_EAST = Block.makeCuboidShape(6D, 5D, 12D, 12D, 11D, 13D);
	private static final VoxelShape SHAPE_EAST = VoxelShapes.or(BASE_EAST, PIPE_EAST, PIPE_SIDE_1_EAST, PIPE_SIDE_2_EAST, PLATE_1_EAST, PLATE_2_EAST);

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
