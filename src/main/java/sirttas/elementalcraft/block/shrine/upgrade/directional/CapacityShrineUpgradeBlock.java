package sirttas.elementalcraft.block.shrine.upgrade.directional;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;

import javax.annotation.Nonnull;

public class CapacityShrineUpgradeBlock extends AbstractDirectionalShrineUpgradeBlock {

	public static final String NAME = "shrine_upgrade_capacity";

	private static final VoxelShape BASE_UP = Block.box(5D, 6D, 5D, 11D, 12D, 11D);
	private static final VoxelShape PIPE_UP = Block.box(7D, 12D, 7D, 9D, 16D, 9D);
	private static final VoxelShape SHAPE_UP = Shapes.or(BASE_UP, PIPE_UP);

	private static final VoxelShape BASE_DOWN = Block.box(5D, 4D, 5D, 11D, 10D, 11D);
	private static final VoxelShape PIPE_DOWN = Block.box(7D, 0D, 7D, 9D, 4D, 9D);
	private static final VoxelShape SHAPE_DOWN = Shapes.or(BASE_DOWN, PIPE_DOWN);

	private static final VoxelShape BASE_NORTH = Block.box(5D, 5D, 4D, 11D, 11D, 10D);
	private static final VoxelShape PIPE_NORTH = Block.box(7D, 7D, 0D, 9D, 9D, 4D);
	private static final VoxelShape SHAPE_NORTH = Shapes.or(BASE_NORTH, PIPE_NORTH);

	private static final VoxelShape BASE_SOUTH = Block.box(5D, 5D, 6D, 11D, 11D, 12D);
	private static final VoxelShape PIPE_SOUTH = Block.box(7D, 7D, 12D, 9D, 9D, 16D);
	private static final VoxelShape SHAPE_SOUTH = Shapes.or(BASE_SOUTH, PIPE_SOUTH);

	private static final VoxelShape BASE_WEST = Block.box(4D, 5D, 5D, 10D, 11D, 11D);
	private static final VoxelShape PIPE_WEST = Block.box(0D, 7D, 7D, 4D, 9D, 9D);
	private static final VoxelShape SHAPE_WEST = Shapes.or(BASE_WEST, PIPE_WEST);

	private static final VoxelShape BASE_EAST = Block.box(6D, 5D, 5D, 12D, 11D, 11D);
	private static final VoxelShape PIPE_EAST = Block.box(12D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape SHAPE_EAST = Shapes.or(BASE_EAST, PIPE_EAST);

	public CapacityShrineUpgradeBlock() {
		super(ShrineUpgrades.CAPACITY);
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
