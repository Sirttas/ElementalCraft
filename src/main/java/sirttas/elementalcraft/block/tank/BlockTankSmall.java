package sirttas.elementalcraft.block.tank;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import sirttas.elementalcraft.config.ECConfig;

public class BlockTankSmall extends AbstractBlockTank {

	public static final String NAME = "tank_small"; // TODO 1.17 rename "container_small"

	private static final VoxelShape GLASS = Block.makeCuboidShape(3D, 3D, 3D, 13D, 13D, 13D);

	private static final VoxelShape CONNECTOR_NORTH_1 = Block.makeCuboidShape(5D, 5D, 1D, 11D, 11D, 3D);
	private static final VoxelShape CONNECTOR_NORTH_2 = Block.makeCuboidShape(6D, 6D, 0D, 10D, 10D, 1D);
	private static final VoxelShape CONNECTOR_SOUTH_1 = Block.makeCuboidShape(5D, 5D, 13D, 11D, 11D, 15D);
	private static final VoxelShape CONNECTOR_SOUTH_2 = Block.makeCuboidShape(6D, 6D, 15D, 10D, 10D, 16D);

	private static final VoxelShape CONNECTOR_WEST_1 = Block.makeCuboidShape(1D, 5D, 5D, 3D, 11D, 11D);
	private static final VoxelShape CONNECTOR_WEST_2 = Block.makeCuboidShape(0D, 6D, 6D, 1D, 10D, 10D);
	private static final VoxelShape CONNECTOR_EAST_1 = Block.makeCuboidShape(13D, 5D, 5D, 15D, 11D, 11D);
	private static final VoxelShape CONNECTOR_EAST_2 = Block.makeCuboidShape(15D, 6D, 6D, 16D, 10D, 10D);

	private static final VoxelShape CONNECTOR_DOWN_1 = Block.makeCuboidShape(5D, 1D, 5D, 11D, 3D, 11D);
	private static final VoxelShape CONNECTOR_DOWN_2 = Block.makeCuboidShape(6D, 0D, 6D, 10D, 1D, 10D);
	private static final VoxelShape CONNECTOR_UP_1 = Block.makeCuboidShape(5D, 13D, 5D, 11D, 15D, 11D);
	private static final VoxelShape CONNECTOR_UP_2 = Block.makeCuboidShape(6D, 15D, 6D, 10D, 16D, 10D);

	private static final VoxelShape SHAPE = VoxelShapes.or(GLASS, CONNECTOR_NORTH_1, CONNECTOR_NORTH_2, CONNECTOR_SOUTH_1, CONNECTOR_SOUTH_2, CONNECTOR_WEST_1, CONNECTOR_WEST_2, CONNECTOR_EAST_1,
			CONNECTOR_EAST_2, CONNECTOR_DOWN_1, CONNECTOR_DOWN_2, CONNECTOR_UP_1, CONNECTOR_UP_2);

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileTank(true);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
	
	@Override
	protected int getDefaultCapacity() {
		return ECConfig.COMMON.tankSmallCapacity.get();
	}
}
