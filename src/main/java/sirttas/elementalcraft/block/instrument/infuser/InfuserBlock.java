package sirttas.elementalcraft.block.instrument.infuser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.block.AbstractECContainerBlock;
import sirttas.elementalcraft.block.WaterLoggingHelper;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.IInstrumentBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InfuserBlock extends AbstractECContainerBlock implements IInstrumentBlock {

	public static final String NAME = "infuser";

	private static final VoxelShape BASE_1 = Block.box(6D, 0D, 6D, 10D, 1D, 10D);
	private static final VoxelShape BASE_2 = Block.box(4D, 1D, 4D, 12D, 2D, 12D);

	private static final VoxelShape PIPE_1 = Block.box(3D, 0D, 3D, 5D, 4D, 5D);
	private static final VoxelShape PIPE_2 = Block.box(11D, 0D, 3D, 13D, 4D, 5D);
	private static final VoxelShape PIPE_3 = Block.box(3D, 0D, 11D, 5D, 4D, 13D);
	private static final VoxelShape PIPE_4 = Block.box(11D, 0D, 11D, 13D, 4D, 13D);

	private static final VoxelShape SHAPE = Shapes.or(BASE_1, BASE_2, PIPE_1, PIPE_2, PIPE_3, PIPE_4);

	public InfuserBlock() {
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(WATERLOGGED, false));
	}
	
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new InfuserBlockEntity(pos, state);
	}

	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
		return createInstrumentTicker(level, type, ECBlockEntityTypes.INFUSER);
	}
	
	@Nonnull
	@Override
	@Deprecated
	public InteractionResult use(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
		return onSingleSlotActivated(world, pos, player, hand);
	}

	@Nonnull
	@Override
	@Deprecated
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}
	
	@Override
	@Deprecated
	public boolean canSurvive(BlockState state, @Nonnull LevelReader world, BlockPos pos) {
		return BlockEntityHelper.isValidContainer(state, world, pos.below());
	}
	
	@Override
	@Nullable
	public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
		return this.defaultBlockState().setValue(WATERLOGGED, WaterLoggingHelper.isPlacedInWater(context));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED);
	}

	@Nonnull
	@Override
	@Deprecated
	public FluidState getFluidState(@Nonnull BlockState state) {
		return WaterLoggingHelper.isWaterlogged(state) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Nonnull
	@Override
	@Deprecated
	public BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull LevelAccessor level, @Nonnull BlockPos pos, @Nonnull BlockPos facingPos) {
		WaterLoggingHelper.scheduleWaterTick(state, level, pos);
		return !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, level, pos, facingPos);
	}
}
