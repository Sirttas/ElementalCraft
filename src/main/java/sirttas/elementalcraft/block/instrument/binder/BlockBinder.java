package sirttas.elementalcraft.block.instrument.binder;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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

public class BlockBinder extends BlockECContainer {

	public static final String NAME = "binder";

	private static final VoxelShape BASE_1 = Block.makeCuboidShape(0D, 0D, 0D, 16D, 2D, 16D);
	private static final VoxelShape BASE_2 = Block.makeCuboidShape(2D, 2D, 2D, 14D, 5D, 14D);

	private static final VoxelShape PIPE_1 = Block.makeCuboidShape(1D, 2D, 1D, 3D, 7D, 3D);
	private static final VoxelShape PIPE_2 = Block.makeCuboidShape(13D, 2D, 1D, 15D, 7D, 3D);
	private static final VoxelShape PIPE_3 = Block.makeCuboidShape(1D, 2D, 13D, 3D, 7D, 15D);
	private static final VoxelShape PIPE_4 = Block.makeCuboidShape(13D, 2D, 13D, 15D, 7D, 15D);

	private static final VoxelShape SHAPE = VoxelShapes.or(BASE_1, BASE_2, PIPE_1, PIPE_2, PIPE_3, PIPE_4);

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileBinder();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		final TileBinder binder = (TileBinder) world.getTileEntity(pos);
		ItemStack heldItem = player.getHeldItem(hand);

		if (binder != null) {
			if (heldItem.isEmpty() && !binder.isEmpty()) {
				for (int i = 0; i < binder.getSizeInventory(); i++) {
					this.onSlotActivated(binder, player, heldItem, i);
				}
				return ActionResultType.SUCCESS;
			}
			for (int i = 0; i < binder.getSizeInventory(); i++) {
				if (binder.getStackInSlot(i).isEmpty()) {
					return this.onSlotActivated(binder, player, heldItem, i);
				}
			}
		}
		return ActionResultType.PASS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		TileBinder binder = (TileBinder) world.getTileEntity(pos);

		if (binder != null && binder.isRunning()) {
			ParticleHelper.createElementFlowParticle(binder.getTankElementType(), world, new Vec3d(pos).add(0, 0.2D, 0), Direction.UP, rand);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
}
