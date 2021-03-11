package sirttas.elementalcraft.block.instrument.infuser;

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
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import sirttas.elementalcraft.block.AbstractBlockECContainer;
import sirttas.elementalcraft.block.tile.TileEntityHelper;

public class BlockInfuser extends AbstractBlockECContainer {

	public static final String NAME = "infuser";

	private static final VoxelShape BASE_1 = Block.makeCuboidShape(6D, 0D, 6D, 10D, 1D, 10D);
	private static final VoxelShape BASE_2 = Block.makeCuboidShape(4D, 1D, 4D, 12D, 2D, 12D);

	private static final VoxelShape PIPE_1 = Block.makeCuboidShape(3D, 0D, 3D, 5D, 4D, 5D);
	private static final VoxelShape PIPE_2 = Block.makeCuboidShape(11D, 0D, 3D, 13D, 4D, 5D);
	private static final VoxelShape PIPE_3 = Block.makeCuboidShape(3D, 0D, 11D, 5D, 4D, 13D);
	private static final VoxelShape PIPE_4 = Block.makeCuboidShape(11D, 0D, 11D, 13D, 4D, 13D);

	private static final VoxelShape SHAPE = VoxelShapes.or(BASE_1, BASE_2, PIPE_1, PIPE_2, PIPE_3, PIPE_4);

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileInfuser();
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
	
	@Override
	@Deprecated
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		return TileEntityHelper.isValidContainer(state.getBlock(), world, pos.down());
	}
}
