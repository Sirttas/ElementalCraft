package sirttas.elementalcraft.block.shrine.overload;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.block.shrine.BlockShrine;

public class BlockOverloadShrine extends BlockShrine {

	public static final String NAME = "overloadshrine";

	private static final VoxelShape BASE_1 = Block.makeCuboidShape(0D, 0D, 0D, 16D, 3D, 16D);
	private static final VoxelShape BASE_2 = Block.makeCuboidShape(1D, 3D, 1D, 15D, 7D, 15D);
	private static final VoxelShape BASE_3 = Block.makeCuboidShape(3D, 7D, 3D, 13D, 12D, 13D);
	private static final VoxelShape BASE_4 = Block.makeCuboidShape(5D, 12D, 5D, 11D, 13D, 11D);

	private static final VoxelShape PIPE_NORTH = Block.makeCuboidShape(7D, 7D, 0D, 9D, 9D, 3D);
	private static final VoxelShape PIPE_SOUTH = Block.makeCuboidShape(7D, 7D, 13D, 9D, 9D, 16D);
	private static final VoxelShape PIPE_EAST = Block.makeCuboidShape(13D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape PIPE_WEST = Block.makeCuboidShape(0D, 7D, 7D, 3D, 9D, 9D);
	private static final VoxelShape PIPE_UP = Block.makeCuboidShape(7D, 13D, 7D, 9D, 16D, 9D);

	private static final VoxelShape BASE = VoxelShapes.or(BASE_1, BASE_2, BASE_3, BASE_4, PIPE_NORTH, PIPE_SOUTH, PIPE_EAST, PIPE_WEST, PIPE_UP);

	private static final VoxelShape UP_SHAPE = VoxelShapes.or(BASE, Block.makeCuboidShape(0D, 13D, 0D, 16D, 16D, 16D));
	private static final VoxelShape NORTH_SHAPE = VoxelShapes.or(BASE, Block.makeCuboidShape(0D, 3D, 0D, 16D, 16D, 3D));
	private static final VoxelShape SOUTH_SHAPE = VoxelShapes.or(BASE, Block.makeCuboidShape(0D, 3D, 13D, 16D, 16D, 16D));
	private static final VoxelShape WEST_SHAPE = VoxelShapes.or(BASE, Block.makeCuboidShape(0D, 3D, 0D, 3D, 16D, 16D));
	private static final VoxelShape EAST_SHAPE = VoxelShapes.or(BASE, Block.makeCuboidShape(13D, 3D, 0D, 16D, 16D, 16D));

	public static final DirectionProperty FACING = DirectionProperty.create("facing", d -> d != Direction.DOWN);

	public BlockOverloadShrine() {
		super(ElementType.AIR);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.UP));
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileOverloadShrine();
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch (state.get(FACING)) {
		case UP:
			return UP_SHAPE;
		case NORTH:
			return NORTH_SHAPE;
		case SOUTH:
			return SOUTH_SHAPE;
		case WEST:
			return WEST_SHAPE;
		case EAST:
			return EAST_SHAPE;
		default:
			return BASE;
		}
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
		container.add(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction direction = context.getFace().getOpposite();
		return this.getDefaultState().with(FACING, direction.getAxis() == Direction.Axis.Y ? Direction.UP : direction);
	}
}