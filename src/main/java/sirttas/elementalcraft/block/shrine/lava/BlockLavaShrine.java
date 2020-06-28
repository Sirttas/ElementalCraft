package sirttas.elementalcraft.block.shrine.lava;

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
import sirttas.elementalcraft.block.BlockEC;
import sirttas.elementalcraft.block.BlockECTileProvider;
import sirttas.elementalcraft.block.shrine.TileShrine;

public class BlockLavaShrine extends BlockECTileProvider {

	public static final String NAME = "lavashrine";

	private static final VoxelShape BASE_1 = Block.makeCuboidShape(0D, 0D, 0D, 16D, 3D, 16D);
	private static final VoxelShape BASE_2 = Block.makeCuboidShape(1D, 3D, 1D, 15D, 7D, 15D);
	private static final VoxelShape BASE_3 = Block.makeCuboidShape(3D, 7D, 3D, 13D, 13D, 13D);
	private static final VoxelShape BASE_4 = Block.makeCuboidShape(0D, 13D, 0D, 16D, 16D, 16D);

	private static final VoxelShape PIPE_NORTH = Block.makeCuboidShape(7D, 7D, 0D, 9D, 9D, 3D);
	private static final VoxelShape PIPE_SOUTH = Block.makeCuboidShape(7D, 7D, 13D, 9D, 9D, 16D);
	private static final VoxelShape PIPE_EAST = Block.makeCuboidShape(13D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape PIPE_WEST = Block.makeCuboidShape(0D, 7D, 7D, 3D, 9D, 9D);

	private static final VoxelShape SHAPE = VoxelShapes.or(BASE_1, BASE_2, BASE_3, BASE_4, PIPE_NORTH, PIPE_SOUTH, PIPE_EAST, PIPE_WEST);


	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileLavaShrine();
	}


	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		double x = pos.getX() + (4 + rand.nextDouble() * 7) * BlockEC.BIT_SIZE;
		double y = pos.getY() + 6 * BlockEC.BIT_SIZE;
		double z = pos.getZ() + (4 + rand.nextDouble() * 7) * BlockEC.BIT_SIZE;

		TileShrine shrine = (TileShrine) world.getTileEntity(pos);

		if (shrine != null && shrine.isRunning()) {
			world.addParticle(ParticleTypes.LAVA, x, y, z, 0.0D, 0.0D, 0.0D);
		}
	}
}