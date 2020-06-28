package sirttas.elementalcraft.block.instrument.infuser;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.BlockECContainer;
import sirttas.elementalcraft.particle.ParticleHelper;

public class BlockInfuser extends BlockECContainer {

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
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		final TileInfuser infuser = (TileInfuser) world.getTileEntity(pos);

		if (infuser != null) {
			return this.onSlotActivated(infuser, player, player.getHeldItem(hand), 0);
		}
		return ActionResultType.PASS;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		TileInfuser infuser = (TileInfuser) world.getTileEntity(pos);

		if (infuser != null && infuser.isRunning()) {
			ParticleHelper.createElementFlowParticle(infuser.getTankElementType(), world, new Vec3d(pos), Direction.UP, rand);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
}
