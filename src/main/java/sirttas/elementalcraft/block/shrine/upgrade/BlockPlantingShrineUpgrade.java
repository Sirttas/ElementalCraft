package sirttas.elementalcraft.block.shrine.upgrade;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class BlockPlantingShrineUpgrade extends BlockShrineUpgrade {

	public static final String NAME = "shrine_upgrade_planting";

	private static final VoxelShape BASE_1 = Block.makeCuboidShape(6D, 14D, 6D, 10D, 16D, 10D);
	private static final VoxelShape BASE_2 = Block.makeCuboidShape(2D, 12D, 2D, 14D, 14D, 14D);
	private static final VoxelShape PIPE_1 = Block.makeCuboidShape(13D, 11D, 1D, 15D, 18D, 3D);
	private static final VoxelShape PIPE_2 = Block.makeCuboidShape(13D, 11D, 13D, 15D, 18D, 15D);
	private static final VoxelShape PIPE_3 = Block.makeCuboidShape(1D, 11D, 13D, 3D, 18D, 15D);
	private static final VoxelShape PIPE_4 = Block.makeCuboidShape(1D, 11D, 1D, 3D, 18D, 3D);

	private static final VoxelShape SHAPE = VoxelShapes.or(BASE_1, BASE_2, PIPE_1, PIPE_2, PIPE_3, PIPE_4);

	@Override
	public Direction getFacing(BlockState state) {
		return Direction.UP;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
}
