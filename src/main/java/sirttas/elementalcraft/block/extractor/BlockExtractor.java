package sirttas.elementalcraft.block.extractor;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.AbstractBlockECTileProvider;
import sirttas.elementalcraft.block.tile.TileEntityHelper;
import sirttas.elementalcraft.particle.ParticleHelper;

public class BlockExtractor extends AbstractBlockECTileProvider {

	public static final String NAME = "extractor";

	private static final VoxelShape BASE = Block.makeCuboidShape(6D, 0D, 6D, 10D, 4D, 10D);
	private static final VoxelShape PILLAR = Block.makeCuboidShape(7D, 4D, 7D, 9D, 13D, 9D);
	private static final VoxelShape TOP = Block.makeCuboidShape(6D, 13D, 6D, 10D, 16D, 10D);

	private static final VoxelShape PIPE_N = Block.makeCuboidShape(7D, 1D, 3D, 9D, 3D, 6D);
	private static final VoxelShape PIPE_S = Block.makeCuboidShape(7D, 1D, 10D, 9D, 3D, 13D);
	private static final VoxelShape PIPE_E = Block.makeCuboidShape(10D, 1D, 7D, 13D, 3D, 9D);
	private static final VoxelShape PIPE_W = Block.makeCuboidShape(3D, 1D, 7D, 6D, 3D, 9D);

	private static final VoxelShape SHAPE = VoxelShapes.or(BASE, PILLAR, TOP, PIPE_N, PIPE_S, PIPE_E, PIPE_W);

	@Override
	public TileExtractor createTileEntity(BlockState state, IBlockReader world) {
		return new TileExtractor();
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

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		TileEntityHelper.getTileEntityAs(world, pos, TileExtractor.class).filter(TileExtractor::canExtract)
				.ifPresent(e -> ParticleHelper.createElementFlowParticle(e.getSourceElementType(), world, Vector3d.copyCentered(pos), Direction.DOWN, 1, rand));
	}
}
