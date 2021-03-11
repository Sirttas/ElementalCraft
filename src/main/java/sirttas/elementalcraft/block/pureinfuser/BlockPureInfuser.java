package sirttas.elementalcraft.block.pureinfuser;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import sirttas.elementalcraft.block.AbstractBlockECContainer;

public class BlockPureInfuser extends AbstractBlockECContainer {

	private static final VoxelShape BASE_1 = Block.makeCuboidShape(0D, 0D, 0D, 16D, 3D, 16D);
	private static final VoxelShape BASE_2 = Block.makeCuboidShape(2D, 3D, 2D, 14D, 9D, 14D);
	private static final VoxelShape BASE_3 = Block.makeCuboidShape(0D, 9D, 0D, 16D, 12D, 16D);

	private static final VoxelShape PIPE_1 = Block.makeCuboidShape(1D, 3D, 1D, 3D, 16D, 3D);
	private static final VoxelShape PIPE_2 = Block.makeCuboidShape(13D, 3D, 1D, 15D, 16D, 3D);
	private static final VoxelShape PIPE_3 = Block.makeCuboidShape(1D, 3D, 13D, 3D, 16D, 15D);
	private static final VoxelShape PIPE_4 = Block.makeCuboidShape(13D, 3D, 13D, 15D, 16D, 15D);

	private static final VoxelShape SHAPE = VoxelShapes.or(BASE_1, BASE_2, BASE_3, PIPE_1, PIPE_2, PIPE_3, PIPE_4);

	public static final String NAME = "pureinfuser";

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TilePureInfuser();
	}

	@Override
	@Deprecated
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		return onSingleSlotActivated(world, pos, player, hand);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
}