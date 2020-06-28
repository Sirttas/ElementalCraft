package sirttas.elementalcraft.block.shrine.vacuum;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.BlockECTileProvider;
import sirttas.elementalcraft.block.shrine.TileShrine;

public class BlockVacuumShrine extends BlockECTileProvider {

	public static final String NAME = "vacuumshrine";

	private static final VoxelShape BASE_1 = Block.makeCuboidShape(0D, 0D, 0D, 16D, 3D, 16D);
	private static final VoxelShape BASE_2 = Block.makeCuboidShape(1D, 3D, 1D, 15D, 7D, 15D);
	private static final VoxelShape BASE_3 = Block.makeCuboidShape(3D, 7D, 3D, 13D, 12D, 13D);
	private static final VoxelShape BASE_4 = Block.makeCuboidShape(6D, 12D, 6D, 10D, 15D, 10D);

	private static final VoxelShape PIPE_UP = Block.makeCuboidShape(7D, 15D, 7D, 9D, 16D, 9D);
	private static final VoxelShape PIPE_NORTH = Block.makeCuboidShape(7D, 7D, 0D, 9D, 9D, 3D);
	private static final VoxelShape PIPE_SOUTH = Block.makeCuboidShape(7D, 7D, 13D, 9D, 9D, 16D);
	private static final VoxelShape PIPE_EAST = Block.makeCuboidShape(13D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape PIPE_WEST = Block.makeCuboidShape(0D, 7D, 7D, 3D, 9D, 9D);

	private static final VoxelShape SHAPE = VoxelShapes.or(BASE_1, BASE_2, BASE_3, BASE_4, PIPE_UP, PIPE_NORTH, PIPE_SOUTH, PIPE_EAST, PIPE_WEST);

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileVacuumShrine();
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		TileShrine shrine = (TileShrine) world.getTileEntity(pos);

		if (shrine != null && shrine.isRunning()) {
			for (int i = 0; i < 3; ++i) {
				int j = rand.nextInt(2) * 2 - 1;
				int k = rand.nextInt(2) * 2 - 1;
				double d0 = pos.getX() + 0.5D + 0.25D * j;
				double d1 = pos.getY() + rand.nextFloat();
				double d2 = pos.getZ() + 0.5D + 0.25D * k;
				double d3 = rand.nextFloat() * j;
				double d4 = (rand.nextFloat() - 0.5D) * 0.125D;
				double d5 = rand.nextFloat() * k;
				world.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
			}
		}
	}
}