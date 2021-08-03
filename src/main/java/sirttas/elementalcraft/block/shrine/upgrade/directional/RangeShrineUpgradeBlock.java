package sirttas.elementalcraft.block.shrine.upgrade.directional;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.ElementalCraft;

public class RangeShrineUpgradeBlock extends AbstractDirectionalShrineUpgradeBlock {

	public static final String NAME = "shrine_upgrade_range";

	private static final VoxelShape BASE_UP = Block.box(6D, 8D, 6D, 10D, 12D, 10D);
	private static final VoxelShape PIPE_UP = Block.box(7D, 12D, 7D, 9D, 16D, 9D);
	private static final VoxelShape CONNECTOR_UP = Block.box(7D, 6D, 7D, 9D, 8D, 9D);
	private static final VoxelShape PLATE_UP = Block.box(4D, 4D, 4D, 12D, 6D, 12D);
	private static final VoxelShape SHAPE_UP = Shapes.or(BASE_UP, PIPE_UP, CONNECTOR_UP, PLATE_UP);

	private static final VoxelShape BASE_DOWN = Block.box(6D, 4D, 6D, 10D, 8D, 10D);
	private static final VoxelShape PIPE_DOWN = Block.box(7D, 0D, 7D, 9D, 4D, 9D);
	private static final VoxelShape CONNECTOR_DOWN = Block.box(7D, 8D, 7D, 9D, 10D, 9D);
	private static final VoxelShape PLATE_DOWN = Block.box(4D, 10D, 4D, 12D, 12D, 12D);
	private static final VoxelShape SHAPE_DOWN = Shapes.or(BASE_DOWN, PIPE_DOWN, CONNECTOR_DOWN, PLATE_DOWN);

	private static final VoxelShape BASE_NORTH = Block.box(6D, 6D, 4D, 10D, 10D, 8D);
	private static final VoxelShape PIPE_NORTH = Block.box(7D, 7D, 0D, 9D, 9D, 4D);
	private static final VoxelShape CONNECTOR_NORTH = Block.box(7D, 7D, 8D, 9D, 9D, 10D);
	private static final VoxelShape PLATE_NORTH = Block.box(4D, 4D, 10D, 12D, 12D, 12D);
	private static final VoxelShape SHAPE_NORTH = Shapes.or(BASE_NORTH, PIPE_NORTH, CONNECTOR_NORTH, PLATE_NORTH);

	private static final VoxelShape BASE_SOUTH = Block.box(6D, 6D, 8D, 10D, 10D, 12D);
	private static final VoxelShape PIPE_SOUTH = Block.box(7D, 7D, 12D, 9D, 9D, 16D);
	private static final VoxelShape CONNECTOR_SOUTH = Block.box(7D, 7D, 6D, 9D, 9D, 8D);
	private static final VoxelShape PLATE_SOUTH = Block.box(4D, 4D, 4D, 12D, 12D, 6D);
	private static final VoxelShape SHAPE_SOUTH = Shapes.or(BASE_SOUTH, PIPE_SOUTH, CONNECTOR_SOUTH, PLATE_SOUTH);

	private static final VoxelShape BASE_WEST = Block.box(4D, 6D, 6D, 8D, 10D, 10D);
	private static final VoxelShape PIPE_WEST = Block.box(0D, 7D, 7D, 4D, 9D, 9D);
	private static final VoxelShape CONNECTOR_WEST = Block.box(8D, 7D, 7D, 10D, 9D, 9D);
	private static final VoxelShape PLATE_WEST = Block.box(10D, 4D, 4D, 12D, 12D, 12D);
	private static final VoxelShape SHAPE_WEST = Shapes.or(BASE_WEST, PIPE_WEST, CONNECTOR_WEST, PLATE_WEST);

	private static final VoxelShape BASE_EAST = Block.box(8D, 6D, 6D, 12D, 10D, 10D);
	private static final VoxelShape PIPE_EAST = Block.box(12D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape CONNECTOR_EAST = Block.box(6D, 7D, 7D, 8D, 9D, 9D);
	private static final VoxelShape PLATE_EAST = Block.box(4D, 4D, 4D, 6D, 12D, 12D);
	private static final VoxelShape SHAPE_EAST = Shapes.or(BASE_EAST, PIPE_EAST, CONNECTOR_EAST, PLATE_EAST);

	public RangeShrineUpgradeBlock() {
		super(ElementalCraft.createRL(NAME));
	}
	
	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
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
}
