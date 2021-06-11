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
import sirttas.elementalcraft.block.AbstractECContainerBlock;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.tag.ECTags;

public class SolarSynthesizerBlock extends AbstractECContainerBlock {

	public static final String NAME = "solar_synthesizer";

	private static final VoxelShape BASE_1 = Block.box(0D, 1D, 0D, 16D, 3D, 16D);
	private static final VoxelShape BASE_2 = VoxelShapes.join(Block.box(2D, 3D, 2D, 14D, 5D, 14D), Block.box(6D, 4D, 6D, 10D, 5D, 10D), IBooleanFunction.ONLY_FIRST);
	private static final VoxelShape BASE_3 = Block.box(6D, 0D, 6D, 10D, 1D, 10D);

	private static final VoxelShape PIPE_1 = Block.box(1D, 0D, 1D, 3D, 6D, 3D);
	private static final VoxelShape PIPE_2 = Block.box(13D, 0D, 1D, 15D, 6D, 3D);
	private static final VoxelShape PIPE_3 = Block.box(1D, 0D, 13D, 3D, 6D, 15D);
	private static final VoxelShape PIPE_4 = Block.box(13D, 0D, 13D, 15D, 6D, 15D);
	private static final VoxelShape PIPE_5 = Block.box(7D, 5D, 3D, 9D, 15D, 5D);
	private static final VoxelShape PIPE_6 = Block.box(7D, 5D, 11D, 9D, 15D, 13D);

	private static final VoxelShape SHAPE = VoxelShapes.or(BASE_1, BASE_2, BASE_3, PIPE_1, PIPE_2, PIPE_3, PIPE_4, PIPE_5, PIPE_6);

	@Override
	public SolarSynthesizerBlockEntity createTileEntity(BlockState state, IBlockReader world) {
		return new SolarSynthesizerBlockEntity();
	}

	@Override
	@Deprecated
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		ItemStack stack = player.getItemInHand(hand);

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
		BlockEntityHelper.getTileEntityAs(world, pos, SolarSynthesizerBlockEntity.class).filter(SolarSynthesizerBlockEntity::isWorking).flatMap(SolarSynthesizerBlockEntity::getElementStorage)
				.ifPresent(storage -> ParticleHelper.createElementFlowParticle(storage.getElementType(), world, Vector3d.atCenterOf(pos.below()), Direction.DOWN, 1, rand));
	}
	
	@Override
	@Deprecated
	public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
		return BlockEntityHelper.isValidContainer(state.getBlock(), world, pos.below());
	}
}
