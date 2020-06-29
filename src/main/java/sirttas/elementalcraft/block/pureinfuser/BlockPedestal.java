package sirttas.elementalcraft.block.pureinfuser;

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

public class BlockPedestal extends BlockECContainer {

	private static final VoxelShape BASE_1 = Block.makeCuboidShape(0D, 0D, 0D, 16D, 3D, 16D);
	private static final VoxelShape BASE_2 = Block.makeCuboidShape(2D, 3D, 2D, 14D, 9D, 14D);
	private static final VoxelShape BASE_3 = Block.makeCuboidShape(0D, 9D, 0D, 16D, 12D, 16D);

	private static final VoxelShape SHAPE = VoxelShapes.or(BASE_1, BASE_2, BASE_3);

	public static final String NAME = "pedestal";

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TilePedestal();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		final TilePedestal pedestal = (TilePedestal) world.getTileEntity(pos);

		if (pedestal != null) {
			return this.onSlotActivated(pedestal, player, player.getHeldItem(hand), 0);
		}
		return ActionResultType.PASS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		TilePedestal pedestal = (TilePedestal) world.getTileEntity(pos);

		if (pedestal != null && pedestal.isPureInfuserRunning()) {
			Direction offset = pedestal.getPureInfuserDirection();
			
			ParticleHelper.createElementFlowParticle(pedestal.getElementType(), world, new Vec3d(pos.offset(offset)).add(0, 0.7, 0), offset, rand);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
}
