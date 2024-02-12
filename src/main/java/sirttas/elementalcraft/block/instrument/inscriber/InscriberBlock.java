package sirttas.elementalcraft.block.instrument.inscriber;

import com.mojang.serialization.MapCodec;
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
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.AbstractECContainerBlock;
import sirttas.elementalcraft.block.WaterLoggingHelper;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.IInstrumentBlock;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.item.ECItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class InscriberBlock extends AbstractECContainerBlock implements IInstrumentBlock {

	public static final String NAME = "inscriber";
	public static final MapCodec<InscriberBlock> CODEC = simpleCodec(InscriberBlock::new);

	private static final VoxelShape BASE_1 = Block.box(0D, 1D, 0D, 16D, 2D, 16D);

	private static final VoxelShape CONNECTION = Block.box(6D, 0D, 6D, 10D, 1D, 10D);

	private static final VoxelShape BASE_PIPE_1 = Block.box(1D, 0D, 1D, 3D, 3D, 3D);
	private static final VoxelShape BACK_BASE_PIPE_1 = Block.box(1D, 4D, 1D, 3D, 5D, 3D);
	private static final VoxelShape BASE_PIPE_2 = Block.box(13D, 0D, 1D, 15D, 3D, 3D);
	private static final VoxelShape BACK_BASE_PIPE_2 = Block.box(13D, 4D, 1D, 15D, 5D, 3D);
	private static final VoxelShape BASE_PIPE_3 = Block.box(1D, 0D, 13D, 3D, 3D, 15D);
	private static final VoxelShape BACK_BASE_PIPE_3 = Block.box(1D, 4D, 13D, 3D, 5D, 15D);
	private static final VoxelShape BASE_PIPE_4 = Block.box(13D, 0D, 13D, 15D, 3D, 15D);
	private static final VoxelShape BACK_BASE_PIPE_4 = Block.box(13D, 4D, 13D, 15D, 5D, 15D);
	private static final VoxelShape BASE_SHAPE = Shapes.or(BASE_1, CONNECTION, BASE_PIPE_1, BASE_PIPE_2, BASE_PIPE_3, BASE_PIPE_4);

	private static final VoxelShape BACK_BASE_NORTH = Block.box(0D, 3D, 12D, 16D, 4D, 16D);
	private static final VoxelShape BACK_PIPE_1_NORTH = Block.box(4D, 2D, 13D, 6D, 15D, 15D);
	private static final VoxelShape BACK_PIPE_2_NORTH = Block.box(10D, 2D, 13D, 12D, 15D, 15D);
	private static final VoxelShape BACK_NORTH = Shapes.or(BACK_BASE_NORTH, BACK_BASE_PIPE_3, BACK_BASE_PIPE_4, BACK_PIPE_1_NORTH, BACK_PIPE_2_NORTH);

	private static final VoxelShape PLATE_1_NORTH = Block.box(1D, 2D, 8D, 15D, 5D, 11D);
	private static final VoxelShape PLATE_2_NORTH = Block.box(1D, 5D, 9D, 15D, 8D, 12D);
	private static final VoxelShape PLATE_3_NORTH = Block.box(1D, 8D, 10D, 15D, 11D, 13D);
	private static final VoxelShape PLATE_4_NORTH = Block.box(1D, 11D, 11D, 15D, 14D, 14D);
	private static final VoxelShape PLATE_5_NORTH = Block.box(1D, 14D, 12D, 15D, 17D, 15D);
	private static final VoxelShape PLATE_NORTH = Shapes.or(PLATE_1_NORTH, PLATE_2_NORTH, PLATE_3_NORTH, PLATE_4_NORTH, PLATE_5_NORTH);

	private static final VoxelShape BACK_BASE_SOUTH = Block.box(0D, 3D, 0D, 16D, 4D, 4D);
	private static final VoxelShape BACK_PIPE_1_SOUTH = Block.box(4D, 2D, 1D, 6D, 15D, 3D);
	private static final VoxelShape BACK_PIPE_2_SOUTH = Block.box(10D, 2D, 1D, 12D, 15D, 3D);
	private static final VoxelShape BACK_SOUTH = Shapes.or(BACK_BASE_SOUTH, BACK_BASE_PIPE_1, BACK_BASE_PIPE_2, BACK_PIPE_1_SOUTH, BACK_PIPE_2_SOUTH);

	private static final VoxelShape PLATE_1_SOUTH = Block.box(1D, 2D, 5D, 15D, 5D, 8D);
	private static final VoxelShape PLATE_2_SOUTH = Block.box(1D, 5D, 4D, 15D, 8D, 7D);
	private static final VoxelShape PLATE_3_SOUTH = Block.box(1D, 8D, 3D, 15D, 11D, 6D);
	private static final VoxelShape PLATE_4_SOUTH = Block.box(1D, 11D, 2D, 15D, 14D, 5D);
	private static final VoxelShape PLATE_5_SOUTH = Block.box(1D, 14D, 1D, 15D, 17D, 4D);
	private static final VoxelShape PLATE_SOUTH = Shapes.or(PLATE_1_SOUTH, PLATE_2_SOUTH, PLATE_3_SOUTH, PLATE_4_SOUTH, PLATE_5_SOUTH);

	private static final VoxelShape BACK_BASE_WEST = Block.box(12D, 3D, 0D, 16D, 4D, 16D);
	private static final VoxelShape BACK_PIPE_1_WEST = Block.box(13D, 2D, 4D, 15D, 15D, 6D);
	private static final VoxelShape BACK_PIPE_2_WEST = Block.box(13D, 2D, 10D, 15D, 15D, 12D);
	private static final VoxelShape BACK_WEST = Shapes.or(BACK_BASE_WEST, BACK_BASE_PIPE_2, BACK_BASE_PIPE_4, BACK_PIPE_1_WEST, BACK_PIPE_2_WEST);

	private static final VoxelShape PLATE_1_WEST = Block.box(8D, 2D, 1D, 11D, 5D, 15D);
	private static final VoxelShape PLATE_2_WEST = Block.box(9D, 5D, 1D, 12D, 8D, 15D);
	private static final VoxelShape PLATE_3_WEST = Block.box(10D, 8D, 1D, 13D, 11D, 15D);
	private static final VoxelShape PLATE_4_WEST = Block.box(11D, 11D, 1D, 14D, 14D, 15D);
	private static final VoxelShape PLATE_5_WEST = Block.box(12D, 14D, 1D, 15D, 17D, 15D);
	private static final VoxelShape PLATE_WEST = Shapes.or(PLATE_1_WEST, PLATE_2_WEST, PLATE_3_WEST, PLATE_4_WEST, PLATE_5_WEST);

	private static final VoxelShape BACK_BASE_EAST = Block.box(0D, 3D, 0D, 4D, 4D, 16D);
	private static final VoxelShape BACK_PIPE_1_EAST = Block.box(1D, 2D, 4D, 3D, 15D, 6D);
	private static final VoxelShape BACK_PIPE_2_EAST = Block.box(1D, 2D, 10D, 3D, 15D, 12D);
	private static final VoxelShape BACK_EAST = Shapes.or(BACK_BASE_EAST, BACK_BASE_PIPE_1, BACK_BASE_PIPE_3, BACK_PIPE_1_EAST, BACK_PIPE_2_EAST);

	private static final VoxelShape PLATE_1_EAST = Block.box(5D, 2D, 1D, 8D, 5D, 15D);
	private static final VoxelShape PLATE_2_EAST = Block.box(4D, 5D, 1D, 7D, 8D, 15D);
	private static final VoxelShape PLATE_3_EAST = Block.box(3D, 8D, 1D, 6D, 11D, 15D);
	private static final VoxelShape PLATE_4_EAST = Block.box(2D, 11D, 1D, 5D, 14D, 15D);
	private static final VoxelShape PLATE_5_EAST = Block.box(1D, 14D, 1D, 4D, 17D, 15D);
	private static final VoxelShape PLATE_EAST = Shapes.or(PLATE_1_EAST, PLATE_2_EAST, PLATE_3_EAST, PLATE_4_EAST, PLATE_5_EAST);

	private static final VoxelShape NORTH_SHAPE = Shapes.or(BASE_SHAPE, BACK_NORTH, PLATE_NORTH);
	private static final VoxelShape SOUTH_SHAPE = Shapes.or(BASE_SHAPE, BACK_SOUTH, PLATE_SOUTH);
	private static final VoxelShape WEST_SHAPE = Shapes.or(BASE_SHAPE, BACK_WEST, PLATE_WEST);
	private static final VoxelShape EAST_SHAPE = Shapes.or(BASE_SHAPE, BACK_EAST, PLATE_EAST);

	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	public InscriberBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(FACING, Direction.NORTH)
				.setValue(WATERLOGGED, false));
	}

	@Override
	protected @NotNull MapCodec<InscriberBlock> codec() {
		return CODEC;
	}


	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new InscriberBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
		return createInstrumentTicker(level, type, ECBlockEntityTypes.INSCRIBER);
	}

	@Nonnull
	@Override
	@Deprecated
	public InteractionResult use(@Nonnull BlockState state, Level world, @Nonnull BlockPos pos, Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
		final InscriberBlockEntity inscriber = (InscriberBlockEntity) world.getBlockEntity(pos);
		ItemStack heldItem = player.getItemInHand(hand);
		IItemHandler inv = ECContainerHelper.getItemHandlerAt(world, pos, null);

		if (inscriber != null && hand == InteractionHand.MAIN_HAND) {
			if (heldItem.is(ECItems.CHISEL.get()) && !inscriber.isLocked()) {
				return makeProgress(player, hand, inscriber, heldItem);
			} else if ((inscriber.isLocked() || heldItem.isEmpty() || player.isShiftKeyDown()) && !inscriber.getInventory().isEmpty()) {
				for (int i = 0; i < inv.getSlots(); i++) {
					this.onSlotActivated(inv, player, ItemStack.EMPTY, i);
				}
				return InteractionResult.SUCCESS;
			} else if (heldItem.is(ECItems.CHISEL.get())) {
				return InteractionResult.PASS;
			}
			for (int i = 0; i < inv.getSlots(); i++) {
				if (inv.getStackInSlot(i).isEmpty() && this.onSlotActivated(inv, player, heldItem, i).shouldSwing()) {
					return InteractionResult.SUCCESS;
				}
			}
		}
		return InteractionResult.PASS;
	}

	private InteractionResult makeProgress(Player player, InteractionHand hand, InscriberBlockEntity inscriber, ItemStack heldItem) {
		if (inscriber.useChisel()) {
			if (heldItem.isDamageableItem() && !player.getAbilities().instabuild) {
				heldItem.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Nonnull
	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return switch (state.getValue(FACING)) {
			case NORTH -> NORTH_SHAPE;
			case SOUTH -> SOUTH_SHAPE;
			case WEST -> WEST_SHAPE;
			case EAST -> EAST_SHAPE;
			default -> BASE_SHAPE;
		};
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, WaterLoggingHelper.isPlacedInWater(context));
	}

	@Nonnull
	@Override
	@Deprecated
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Nonnull
	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED, FACING);
	}
	
	@Override
	@Deprecated
	public boolean canSurvive(BlockState state, @Nonnull LevelReader world, BlockPos pos) {
		return BlockEntityHelper.isValidContainer(state, world, pos.below());
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
