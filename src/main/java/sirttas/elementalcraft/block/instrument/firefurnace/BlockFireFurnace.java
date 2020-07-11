package sirttas.elementalcraft.block.instrument.firefurnace;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.BlockEC;
import sirttas.elementalcraft.block.BlockECContainer;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.particle.ParticleHelper;

public class BlockFireFurnace extends BlockECContainer {

	public static final String NAME = "firefurnace";

	private static final VoxelShape OVEN_SLAB = Block.makeCuboidShape(0D, 2D, 0D, 16D, 12D, 16D);
	private static final VoxelShape TOP_BOWL = Block.makeCuboidShape(3D, 11D, 3D, 13D, 12D, 13D);
	private static final VoxelShape MIDDLE_1 = Block.makeCuboidShape(0D, 4D, 3D, 16D, 10D, 13D);
	private static final VoxelShape MIDDLE_2 = Block.makeCuboidShape(3D, 4D, 0D, 13D, 10D, 16D);
	private static final VoxelShape EMPTY_SPACE = VoxelShapes.or(TOP_BOWL, MIDDLE_1, MIDDLE_2);
	private static final VoxelShape OVEN = VoxelShapes.combineAndSimplify(OVEN_SLAB, EMPTY_SPACE, IBooleanFunction.ONLY_FIRST);
	private static final VoxelShape CONNECTION = Block.makeCuboidShape(6D, 0D, 6D, 10D, 2D, 10D);
	private static final VoxelShape PILLAT_1 = Block.makeCuboidShape(1D, 0D, 1D, 3D, 2D, 3D);
	private static final VoxelShape PILLAT_2 = Block.makeCuboidShape(13D, 0D, 1D, 15D, 2D, 3D);
	private static final VoxelShape PILLAT_3 = Block.makeCuboidShape(1D, 0D, 13D, 3D, 2D, 15D);
	private static final VoxelShape PILLAT_4 = Block.makeCuboidShape(13D, 0D, 13D, 15D, 2D, 15D);
	private static final VoxelShape SHAPE = VoxelShapes.or(OVEN, CONNECTION, PILLAT_1, PILLAT_2, PILLAT_3, PILLAT_4);

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileFireFurnace();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		final TileFireFurnace furnace = (TileFireFurnace) world.getTileEntity(pos);

		if (furnace != null) {
			if (!ItemEC.isEmpty(furnace.getStackInSlot(1))) {
				return this.onSlotActivated(furnace, player, player.getHeldItem(hand), 1);
			}
			return this.onSlotActivated(furnace, player, player.getHeldItem(hand), 0);
		}
		return ActionResultType.PASS;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		double x = pos.getX() + (5 + rand.nextDouble() * 6) * BlockEC.BIT_SIZE;
		double y = pos.getY() + 6 * BlockEC.BIT_SIZE;
		double z = pos.getZ() + (5 + rand.nextDouble() * 6) * BlockEC.BIT_SIZE;

		TileFireFurnace furnace = (TileFireFurnace) world.getTileEntity(pos);

		if (furnace != null && furnace.isRunning()) {
			world.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
			world.addParticle(ParticleTypes.SMOKE, x, y + 0.5D, z, 0.0D, 0.0D, 0.0D);
			ParticleHelper.createElementFlowParticle(furnace.getTankElementType(), world, Vector3d.func_237489_a_(pos), Direction.UP, rand);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
}
