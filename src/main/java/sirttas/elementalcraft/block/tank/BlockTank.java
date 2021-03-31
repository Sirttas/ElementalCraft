package sirttas.elementalcraft.block.tank;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import sirttas.elementalcraft.config.ECConfig;

public class BlockTank extends AbstractBlockConnectedTank {

	public static final String NAME = "tank"; // TODO 1.17 rename "container"

	private static final VoxelShape BASE = Block.makeCuboidShape(0D, 0D, 0D, 16D, 2D, 16D);
	private static final VoxelShape GLASS = Block.makeCuboidShape(2D, 2D, 2D, 14D, 15D, 14D);

	private static final VoxelShape PIPE_1 = Block.makeCuboidShape(1D, 2D, 1D, 3D, 16D, 3D);
	private static final VoxelShape PIPE_2 = Block.makeCuboidShape(13D, 2D, 1D, 15D, 16D, 3D);
	private static final VoxelShape PIPE_3 = Block.makeCuboidShape(1D, 2D, 13D, 3D, 16D, 15D);
	private static final VoxelShape PIPE_4 = Block.makeCuboidShape(13D, 2D, 13D, 15D, 16D, 15D);

	private static final VoxelShape CONNECTOR = Block.makeCuboidShape(6D, 15D, 6D, 10D, 16D, 10D);

	private static final VoxelShape SHAPE = VoxelShapes.or(BASE, GLASS, PIPE_1, PIPE_2, PIPE_3, PIPE_4, CONNECTOR);

	public BlockTank() {
		this.setDefaultState(this.stateContainer.getBaseState().with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false));
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileTank();
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return VoxelShapes.or(SHAPE, super.getShape(state, worldIn, pos, context));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
		container.add(NORTH, SOUTH, EAST, WEST);
	}

	@Override
	protected int getDefaultCapacity() {
		return ECConfig.COMMON.tankCapacity.get();
	}
}
