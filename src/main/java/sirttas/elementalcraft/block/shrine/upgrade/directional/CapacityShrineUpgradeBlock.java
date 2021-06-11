package sirttas.elementalcraft.block.shrine.upgrade.directional;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class CapacityShrineUpgradeBlock extends AbstractDirectionalShrineUpgradeBlock {

	public static final String NAME = "shrine_upgrade_capacity";

	private static final VoxelShape BASE_UP = Block.box(5D, 6D, 5D, 11D, 12D, 11D);
	private static final VoxelShape PIPE_UP = Block.box(7D, 12D, 7D, 9D, 16D, 9D);
	private static final VoxelShape SHAPE_UP = VoxelShapes.or(BASE_UP, PIPE_UP);

	private static final VoxelShape BASE_DOWN = Block.box(5D, 4D, 5D, 11D, 10D, 11D);
	private static final VoxelShape PIPE_DOWN = Block.box(7D, 0D, 7D, 9D, 4D, 9D);
	private static final VoxelShape SHAPE_DOWN = VoxelShapes.or(BASE_DOWN, PIPE_DOWN);

	private static final VoxelShape BASE_NORTH = Block.box(5D, 5D, 4D, 11D, 11D, 10D);
	private static final VoxelShape PIPE_NORTH = Block.box(7D, 7D, 0D, 9D, 9D, 4D);
	private static final VoxelShape SHAPE_NORTH = VoxelShapes.or(BASE_NORTH, PIPE_NORTH);

	private static final VoxelShape BASE_SOUTH = Block.box(5D, 5D, 6D, 11D, 11D, 12D);
	private static final VoxelShape PIPE_SOUTH = Block.box(7D, 7D, 12D, 9D, 9D, 16D);
	private static final VoxelShape SHAPE_SOUTH = VoxelShapes.or(BASE_SOUTH, PIPE_SOUTH);

	private static final VoxelShape BASE_WEST = Block.box(4D, 5D, 5D, 10D, 11D, 11D);
	private static final VoxelShape PIPE_WEST = Block.box(0D, 7D, 7D, 4D, 9D, 9D);
	private static final VoxelShape SHAPE_WEST = VoxelShapes.or(BASE_WEST, PIPE_WEST);

	private static final VoxelShape BASE_EAST = Block.box(6D, 5D, 5D, 12D, 11D, 11D);
	private static final VoxelShape PIPE_EAST = Block.box(12D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape SHAPE_EAST = VoxelShapes.or(BASE_EAST, PIPE_EAST);

	public static VoxelShape getShape(BlockState state) {
		switch (state.getValue(FACING)) {
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
	
	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return getShape(state);
	}
}
