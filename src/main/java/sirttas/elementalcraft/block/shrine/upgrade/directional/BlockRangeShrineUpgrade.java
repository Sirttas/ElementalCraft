package sirttas.elementalcraft.block.shrine.upgrade.directional;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class BlockRangeShrineUpgrade extends AbstractBlockDirectionalShrineUpgrade {

	public static final String NAME = "shrine_upgrade_range";

	private static final VoxelShape BASE_UP = Block.makeCuboidShape(6D, 8D, 6D, 10D, 12D, 10D);
	private static final VoxelShape PIPE_UP = Block.makeCuboidShape(7D, 12D, 7D, 9D, 16D, 9D);
	private static final VoxelShape CONNECTOR_UP = Block.makeCuboidShape(7D, 6D, 7D, 9D, 8D, 9D);
	private static final VoxelShape PLATE_UP = Block.makeCuboidShape(4D, 4D, 4D, 12D, 6D, 12D);
	private static final VoxelShape SHAPE_UP = VoxelShapes.or(BASE_UP, PIPE_UP, CONNECTOR_UP, PLATE_UP);

	private static final VoxelShape BASE_DOWN = Block.makeCuboidShape(6D, 4D, 6D, 10D, 8D, 10D);
	private static final VoxelShape PIPE_DOWN = Block.makeCuboidShape(7D, 0D, 7D, 9D, 4D, 9D);
	private static final VoxelShape CONNECTOR_DOWN = Block.makeCuboidShape(7D, 8D, 7D, 9D, 10D, 9D);
	private static final VoxelShape PLATE_DOWN = Block.makeCuboidShape(4D, 10D, 4D, 12D, 12D, 12D);
	private static final VoxelShape SHAPE_DOWN = VoxelShapes.or(BASE_DOWN, PIPE_DOWN, CONNECTOR_DOWN, PLATE_DOWN);

	private static final VoxelShape BASE_NORTH = Block.makeCuboidShape(6D, 6D, 4D, 10D, 10D, 8D);
	private static final VoxelShape PIPE_NORTH = Block.makeCuboidShape(7D, 7D, 0D, 9D, 9D, 4D);
	private static final VoxelShape CONNECTOR_NORTH = Block.makeCuboidShape(7D, 7D, 8D, 9D, 9D, 10D);
	private static final VoxelShape PLATE_NORTH = Block.makeCuboidShape(4D, 4D, 10D, 12D, 12D, 12D);
	private static final VoxelShape SHAPE_NORTH = VoxelShapes.or(BASE_NORTH, PIPE_NORTH, CONNECTOR_NORTH, PLATE_NORTH);

	private static final VoxelShape BASE_SOUTH = Block.makeCuboidShape(6D, 6D, 8D, 10D, 10D, 12D);
	private static final VoxelShape PIPE_SOUTH = Block.makeCuboidShape(7D, 7D, 12D, 9D, 9D, 16D);
	private static final VoxelShape CONNECTOR_SOUTH = Block.makeCuboidShape(7D, 7D, 6D, 9D, 9D, 8D);
	private static final VoxelShape PLATE_SOUTH = Block.makeCuboidShape(4D, 4D, 4D, 12D, 12D, 6D);
	private static final VoxelShape SHAPE_SOUTH = VoxelShapes.or(BASE_SOUTH, PIPE_SOUTH, CONNECTOR_SOUTH, PLATE_SOUTH);

	private static final VoxelShape BASE_WEST = Block.makeCuboidShape(4D, 6D, 6D, 8D, 10D, 10D);
	private static final VoxelShape PIPE_WEST = Block.makeCuboidShape(0D, 7D, 7D, 4D, 9D, 9D);
	private static final VoxelShape CONNECTOR_WEST = Block.makeCuboidShape(8D, 7D, 7D, 10D, 9D, 9D);
	private static final VoxelShape PLATE_WEST = Block.makeCuboidShape(10D, 4D, 4D, 12D, 12D, 12D);
	private static final VoxelShape SHAPE_WEST = VoxelShapes.or(BASE_WEST, PIPE_WEST, CONNECTOR_WEST, PLATE_WEST);

	private static final VoxelShape BASE_EAST = Block.makeCuboidShape(8D, 6D, 6D, 12D, 10D, 10D);
	private static final VoxelShape PIPE_EAST = Block.makeCuboidShape(12D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape CONNECTOR_EAST = Block.makeCuboidShape(6D, 7D, 7D, 8D, 9D, 9D);
	private static final VoxelShape PLATE_EAST = Block.makeCuboidShape(4D, 4D, 4D, 6D, 12D, 12D);
	private static final VoxelShape SHAPE_EAST = VoxelShapes.or(BASE_EAST, PIPE_EAST, CONNECTOR_EAST, PLATE_EAST);

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
