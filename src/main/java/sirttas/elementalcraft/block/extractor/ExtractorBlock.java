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
import sirttas.elementalcraft.block.AbstractECBlockEntityProviderBlock;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.particle.ParticleHelper;

public class ExtractorBlock extends AbstractECBlockEntityProviderBlock {

	public static final String NAME = "extractor";

	private static final VoxelShape BASE = Block.box(6D, 0D, 6D, 10D, 4D, 10D);
	private static final VoxelShape PILLAR = Block.box(7D, 4D, 7D, 9D, 13D, 9D);
	private static final VoxelShape TOP = Block.box(6D, 13D, 6D, 10D, 16D, 10D);

	private static final VoxelShape PIPE_N = Block.box(7D, 1D, 3D, 9D, 3D, 6D);
	private static final VoxelShape PIPE_S = Block.box(7D, 1D, 10D, 9D, 3D, 13D);
	private static final VoxelShape PIPE_E = Block.box(10D, 1D, 7D, 13D, 3D, 9D);
	private static final VoxelShape PIPE_W = Block.box(3D, 1D, 7D, 6D, 3D, 9D);

	private static final VoxelShape SHAPE = VoxelShapes.or(BASE, PILLAR, TOP, PIPE_N, PIPE_S, PIPE_E, PIPE_W);

	@Override
	public ExtractorBlockEntity createTileEntity(BlockState state, IBlockReader world) {
		return new ExtractorBlockEntity();
	}
	
	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
	
	@Override
	@Deprecated
	public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
		return BlockEntityHelper.isValidContainer(state.getBlock(), world, pos.below());
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		BlockEntityHelper.getTileEntityAs(world, pos, ExtractorBlockEntity.class).filter(ExtractorBlockEntity::canExtract)
				.ifPresent(e -> ParticleHelper.createElementFlowParticle(e.getSourceElementType(), world, Vector3d.atCenterOf(pos), Direction.DOWN, 1, rand));
	}
}
