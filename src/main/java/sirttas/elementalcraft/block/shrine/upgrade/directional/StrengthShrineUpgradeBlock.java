package sirttas.elementalcraft.block.shrine.upgrade.directional;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.ElementalCraft;

import javax.annotation.Nonnull;

public class StrengthShrineUpgradeBlock extends AbstractDirectionalShrineUpgradeBlock {

	public static final String NAME = "shrine_upgrade_strength";

	private static final VoxelShape BASE_UP = Block.box(6D, 7D, 6D, 10D, 11D, 10D);
	private static final VoxelShape PIPE_UP = Block.box(7D, 11D, 7D, 9D, 16D, 9D);
	private static final VoxelShape PIPE_SIDE_1_UP = Block.box(4D, 8D, 7D, 6D, 10D, 9D);
	private static final VoxelShape PIPE_SIDE_2_UP = Block.box(10D, 8D, 7D, 12D, 10D, 9D);
	private static final VoxelShape PLATE_1_UP = Block.box(3D, 6D, 5D, 4D, 12D, 11D);
	private static final VoxelShape PLATE_2_UP = Block.box(12D, 6D, 5D, 13D, 12D, 11D);
	private static final VoxelShape SHAPE_UP = Shapes.or(BASE_UP, PIPE_UP, PIPE_SIDE_1_UP, PIPE_SIDE_2_UP, PLATE_1_UP, PLATE_2_UP);

	private static final VoxelShape BASE_DOWN = Block.box(6D, 5D, 6D, 10D, 9D, 10D);
	private static final VoxelShape PIPE_DOWN = Block.box(7D, 0D, 7D, 9D, 5D, 9D);
	private static final VoxelShape PIPE_SIDE_1_DOWN = Block.box(4D, 6D, 7D, 6D, 8D, 9D);
	private static final VoxelShape PIPE_SIDE_2_DOWN = Block.box(10D, 6D, 7D, 12D, 8D, 9D);
	private static final VoxelShape PLATE_1_DOWN = Block.box(3D, 4D, 5D, 4D, 10D, 11D);
	private static final VoxelShape PLATE_2_DOWN = Block.box(12D, 4D, 5D, 13D, 10D, 11D);
	private static final VoxelShape SHAPE_DOWN = Shapes.or(BASE_DOWN, PIPE_DOWN, PIPE_SIDE_1_DOWN, PIPE_SIDE_2_DOWN, PLATE_1_DOWN, PLATE_2_DOWN);

	private static final VoxelShape BASE_NORTH = Block.box(6D, 6D, 5D, 10D, 10D, 9D);
	private static final VoxelShape PIPE_NORTH = Block.box(7D, 7D, 0D, 9D, 9D, 5D);
	private static final VoxelShape PIPE_SIDE_1_NORTH = Block.box(4D, 7D, 6D, 6D, 9D, 8D);
	private static final VoxelShape PIPE_SIDE_2_NORTH = Block.box(10D, 7D, 6D, 12D, 9D, 8D);
	private static final VoxelShape PLATE_1_NORTH = Block.box(3D, 5D, 4D, 4D, 11D, 10D);
	private static final VoxelShape PLATE_2_NORTH = Block.box(12D, 5D, 4D, 13D, 11D, 10D);
	private static final VoxelShape SHAPE_NORTH = Shapes.or(BASE_NORTH, PIPE_NORTH, PIPE_SIDE_1_NORTH, PIPE_SIDE_2_NORTH, PLATE_1_NORTH, PLATE_2_NORTH);

	private static final VoxelShape BASE_SOUTH = Block.box(6D, 6D, 7D, 10D, 10D, 11D);
	private static final VoxelShape PIPE_SOUTH = Block.box(7D, 7D, 11D, 9D, 9D, 16D);
	private static final VoxelShape PIPE_SIDE_1_SOUTH = Block.box(4D, 7D, 8D, 6D, 9D, 10D);
	private static final VoxelShape PIPE_SIDE_2_SOUTH = Block.box(10D, 7D, 8D, 12D, 9D, 10D);
	private static final VoxelShape PLATE_1_SOUTH = Block.box(3D, 5D, 6D, 4D, 11D, 12D);
	private static final VoxelShape PLATE_2_SOUTH = Block.box(12D, 5D, 6D, 13D, 11D, 12D);
	private static final VoxelShape SHAPE_SOUTH = Shapes.or(BASE_SOUTH, PIPE_SOUTH, PIPE_SIDE_1_SOUTH, PIPE_SIDE_2_SOUTH, PLATE_1_SOUTH, PLATE_2_SOUTH);

	private static final VoxelShape BASE_WEST = Block.box(5D, 6D, 6D, 9D, 10D, 10D);
	private static final VoxelShape PIPE_WEST = Block.box(0D, 7D, 7D, 5D, 9D, 9D);
	private static final VoxelShape PIPE_SIDE_1_WEST = Block.box(6D, 7D, 4D, 8D, 9D, 6D);
	private static final VoxelShape PIPE_SIDE_2_WEST = Block.box(6D, 7D, 10D, 8D, 9D, 12D);
	private static final VoxelShape PLATE_1_WEST = Block.box(4D, 5D, 3D, 10D, 11D, 4D);
	private static final VoxelShape PLATE_2_WEST = Block.box(4D, 5D, 12D, 10D, 11D, 13D);
	private static final VoxelShape SHAPE_WEST = Shapes.or(BASE_WEST, PIPE_WEST, PIPE_SIDE_1_WEST, PIPE_SIDE_2_WEST, PLATE_1_WEST, PLATE_2_WEST);

	private static final VoxelShape BASE_EAST = Block.box(7D, 6D, 6D, 11D, 10D, 10D);
	private static final VoxelShape PIPE_EAST = Block.box(11D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape PIPE_SIDE_1_EAST = Block.box(8D, 7D, 4D, 10D, 9D, 6D);
	private static final VoxelShape PIPE_SIDE_2_EAST = Block.box(8D, 7D, 10D, 10D, 9D, 12D);
	private static final VoxelShape PLATE_1_EAST = Block.box(6D, 5D, 3D, 12D, 11D, 4D);
	private static final VoxelShape PLATE_2_EAST = Block.box(6D, 5D, 12D, 12D, 11D, 13D);
	private static final VoxelShape SHAPE_EAST = Shapes.or(BASE_EAST, PIPE_EAST, PIPE_SIDE_1_EAST, PIPE_SIDE_2_EAST, PLATE_1_EAST, PLATE_2_EAST);

	public StrengthShrineUpgradeBlock() {
		super(ElementalCraft.createRL(NAME));
	}
	
	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getShape(BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return switch (state.getValue(FACING)) {
			case DOWN -> SHAPE_DOWN;
			case EAST -> SHAPE_EAST;
			case NORTH -> SHAPE_NORTH;
			case SOUTH -> SHAPE_SOUTH;
			case WEST -> SHAPE_WEST;
			default -> SHAPE_UP;
		};
	}
}
