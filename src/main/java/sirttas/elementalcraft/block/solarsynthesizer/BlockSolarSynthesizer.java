package sirttas.elementalcraft.block.solarsynthesizer;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.AbstractBlockECContainer;
import sirttas.elementalcraft.block.tile.TileEntityHelper;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.tag.ECTags;

public class BlockSolarSynthesizer extends AbstractBlockECContainer {

	public static final String NAME = "solar_synthesizer";

	private static final VoxelShape BASE_1 = Block.makeCuboidShape(0D, 1D, 0D, 16D, 3D, 16D);
	private static final VoxelShape BASE_2 = VoxelShapes.combineAndSimplify(Block.makeCuboidShape(2D, 3D, 2D, 14D, 5D, 14D), Block.makeCuboidShape(6D, 4D, 6D, 10D, 5D, 10D), IBooleanFunction.ONLY_FIRST);
	private static final VoxelShape BASE_3 = Block.makeCuboidShape(6D, 0D, 6D, 10D, 1D, 10D);

	private static final VoxelShape PIPE_1 = Block.makeCuboidShape(1D, 0D, 1D, 3D, 6D, 3D);
	private static final VoxelShape PIPE_2 = Block.makeCuboidShape(13D, 0D, 1D, 15D, 6D, 3D);
	private static final VoxelShape PIPE_3 = Block.makeCuboidShape(1D, 0D, 13D, 3D, 6D, 15D);
	private static final VoxelShape PIPE_4 = Block.makeCuboidShape(13D, 0D, 13D, 15D, 6D, 15D);
	private static final VoxelShape PIPE_5 = Block.makeCuboidShape(7D, 5D, 3D, 9D, 15D, 5D);
	private static final VoxelShape PIPE_6 = Block.makeCuboidShape(7D, 5D, 11D, 9D, 15D, 13D);

	private static final VoxelShape SHAPE = VoxelShapes.or(BASE_1, BASE_2, BASE_3, PIPE_1, PIPE_2, PIPE_3, PIPE_4, PIPE_5, PIPE_6);

	@Override
	public TileSolarSynthesizer createTileEntity(BlockState state, IBlockReader world) {
		return new TileSolarSynthesizer();
	}

	@Override
	@Deprecated
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		ItemStack stack = player.getHeldItem(hand);

		if (stack.isEmpty() || ECTags.Items.LENSES.contains(stack.getItem())) {
			return onSingleSlotActivated(world, pos, player, hand);
		}
		return ActionResultType.PASS;
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		TileEntityHelper.getTileEntityAs(world, pos, TileSolarSynthesizer.class).filter(TileSolarSynthesizer::isWorking).flatMap(TileSolarSynthesizer::getElementStorage)
				.ifPresent(storage -> ParticleHelper.createElementFlowParticle(storage.getElementType(), world, Vector3d.copyCentered(pos.down()), Direction.DOWN, 1, rand));
	}
	
	@Override
	@Deprecated
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		return TileEntityHelper.isValidContainer(state.getBlock(), world, pos.down());
	}
}
