package sirttas.elementalcraft.block.instrument.binder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraftforge.items.IItemHandler;
import sirttas.elementalcraft.block.AbstractECContainerBlock;
import sirttas.elementalcraft.block.WaterLoggingHelper;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.instrument.IInstrumentBlock;
import sirttas.elementalcraft.container.ECContainerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BinderBlock extends AbstractECContainerBlock implements IInstrumentBlock {

	public static final String NAME = "binder";

	private static final VoxelShape BASE_1 = Block.box(0D, 0D, 0D, 16D, 2D, 16D);
	private static final VoxelShape BASE_2 = Block.box(2D, 2D, 2D, 14D, 5D, 14D);

	private static final VoxelShape PIPE_1 = Block.box(1D, 2D, 1D, 3D, 7D, 3D);
	private static final VoxelShape PIPE_2 = Block.box(13D, 2D, 1D, 15D, 7D, 3D);
	private static final VoxelShape PIPE_3 = Block.box(1D, 2D, 13D, 3D, 7D, 15D);
	private static final VoxelShape PIPE_4 = Block.box(13D, 2D, 13D, 15D, 7D, 15D);

	private static final VoxelShape SHAPE = Shapes.or(BASE_1, BASE_2, PIPE_1, PIPE_2, PIPE_3, PIPE_4);

	public BinderBlock() {
		this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
	}
	
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new BinderBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
		return createInstrumentTicker(level, type, BinderBlockEntity.TYPE);
	}

	@Nonnull
	@Override
	@Deprecated
	public InteractionResult use(@Nonnull BlockState state, Level world, @Nonnull BlockPos pos, Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
		final BinderBlockEntity binder = (BinderBlockEntity) world.getBlockEntity(pos);
		ItemStack heldItem = player.getItemInHand(hand);
		IItemHandler inv = ECContainerHelper.getItemHandlerAt(world, pos, null);

		if (binder != null && hand == InteractionHand.MAIN_HAND) {
			if ((binder.isLocked() || heldItem.isEmpty() || player.isShiftKeyDown()) && !binder.getInventory().isEmpty()) {
				for (int i = 0; i < inv.getSlots(); i++) {
					this.onSlotActivated(inv, player, ItemStack.EMPTY, i);
				}
				return InteractionResult.SUCCESS;
			}
			for (int i = 0; i < inv.getSlots(); i++) {
				if (inv.getStackInSlot(i).isEmpty()) {
					return this.onSlotActivated(inv, player, heldItem, i);
				}
			}
		}
		return InteractionResult.PASS;
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
