package sirttas.elementalcraft.block.solarsynthesizer;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.AbstractECContainerBlock;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.tag.ECTags;

public class SolarSynthesizerBlock extends AbstractECContainerBlock {

	public static final String NAME = "solar_synthesizer";

	private static final VoxelShape BASE_1 = Block.box(0D, 1D, 0D, 16D, 3D, 16D);
	private static final VoxelShape BASE_2 = Shapes.join(Block.box(2D, 3D, 2D, 14D, 5D, 14D), Block.box(6D, 4D, 6D, 10D, 5D, 10D), BooleanOp.ONLY_FIRST);
	private static final VoxelShape BASE_3 = Block.box(6D, 0D, 6D, 10D, 1D, 10D);

	private static final VoxelShape PIPE_1 = Block.box(1D, 0D, 1D, 3D, 6D, 3D);
	private static final VoxelShape PIPE_2 = Block.box(13D, 0D, 1D, 15D, 6D, 3D);
	private static final VoxelShape PIPE_3 = Block.box(1D, 0D, 13D, 3D, 6D, 15D);
	private static final VoxelShape PIPE_4 = Block.box(13D, 0D, 13D, 15D, 6D, 15D);
	private static final VoxelShape PIPE_5 = Block.box(7D, 5D, 3D, 9D, 15D, 5D);
	private static final VoxelShape PIPE_6 = Block.box(7D, 5D, 11D, 9D, 15D, 13D);

	private static final VoxelShape SHAPE = Shapes.or(BASE_1, BASE_2, BASE_3, PIPE_1, PIPE_2, PIPE_3, PIPE_4, PIPE_5, PIPE_6);

	@Override
	public SolarSynthesizerBlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SolarSynthesizerBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createECTicker(level, type, SolarSynthesizerBlockEntity.TYPE, SolarSynthesizerBlockEntity::tick);
	}
	
	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);

		if (stack.isEmpty() || ECTags.Items.LENSES.contains(stack.getItem())) {
			return onSingleSlotActivated(world, pos, player, hand);
		}
		return InteractionResult.PASS;
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE;

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
		BlockEntityHelper.getTileEntityAs(world, pos, SolarSynthesizerBlockEntity.class).filter(SolarSynthesizerBlockEntity::isWorking).flatMap(SolarSynthesizerBlockEntity::getElementStorage)
				.ifPresent(storage -> ParticleHelper.createElementFlowParticle(storage.getElementType(), world, Vec3.atCenterOf(pos.below()), Direction.DOWN, 1, rand));
	}
	
	@Override
	@Deprecated
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return BlockEntityHelper.isValidContainer(state.getBlock(), world, pos.below());
	}
}
