package sirttas.elementalcraft.block.instrument.inscriber;

import javax.annotation.Nullable;

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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.IItemHandler;
import sirttas.elementalcraft.block.AbstractECContainerBlock;
import sirttas.elementalcraft.block.WaterloggingHelper;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.instrument.IInstrumentBlock;
import sirttas.elementalcraft.inventory.ECInventoryHelper;
import sirttas.elementalcraft.item.ECItems;

public class InscriberBlock extends AbstractECContainerBlock implements IInstrumentBlock {

	public static final String NAME = "inscriber";

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
	private static final VoxelShape PLATE_3_EAST = Block.box(3D, 6D, 1D, 6D, 8D, 15D);
	private static final VoxelShape PLATE_4_EAST = Block.box(2D, 11D, 1D, 5D, 14D, 15D);
	private static final VoxelShape PLATE_5_EAST = Block.box(1D, 14D, 1D, 4D, 17D, 15D);
	private static final VoxelShape PLATE_EAST = Shapes.or(PLATE_1_EAST, PLATE_2_EAST, PLATE_3_EAST, PLATE_4_EAST, PLATE_5_EAST);

	private static final VoxelShape NORTH_SHAPE = Shapes.or(BASE_SHAPE, BACK_NORTH, PLATE_NORTH);
	private static final VoxelShape SOUTH_SHAPE = Shapes.or(BASE_SHAPE, BACK_SOUTH, PLATE_SOUTH);
	private static final VoxelShape WEST_SHAPE = Shapes.or(BASE_SHAPE, BACK_WEST, PLATE_WEST);
	private static final VoxelShape EAST_SHAPE = Shapes.or(BASE_SHAPE, BACK_EAST, PLATE_EAST);

	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	public InscriberBlock() {
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new InscriberBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createInstrumentTicker(level, type, InscriberBlockEntity.TYPE);
	}

	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		final InscriberBlockEntity inscriber = (InscriberBlockEntity) world.getBlockEntity(pos);
		ItemStack heldItem = player.getItemInHand(hand);
		IItemHandler inv = ECInventoryHelper.getItemHandlerAt(world, pos, null);

		if (inscriber != null && (hand == InteractionHand.MAIN_HAND || !heldItem.isEmpty())) {
			if (heldItem.getItem() == ECItems.CHISEL && !inscriber.isLocked()) {
				return makeProgress(player, hand, inscriber, heldItem);
			} else if ((inscriber.isLocked() || heldItem.isEmpty() || player.isShiftKeyDown()) && !inscriber.getInventory().isEmpty()) {
				for (int i = 0; i < inv.getSlots(); i++) {
					this.onSlotActivated(inv, player, ItemStack.EMPTY, i);
				}
				return InteractionResult.SUCCESS;
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
		if (inscriber.useChisle()) {
			if (heldItem.isDamageableItem() && !player.isCreative()) {
				heldItem.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		switch (state.getValue(FACING)) {
		case NORTH:
			return NORTH_SHAPE;
		case SOUTH:
			return SOUTH_SHAPE;
		case WEST:
			return WEST_SHAPE;
		case EAST:
			return EAST_SHAPE;
		default:
			return BASE_SHAPE;
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, WaterloggingHelper.isPlacedInWater(context));
	}

	@Override
	@Deprecated
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

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
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return BlockEntityHelper.isValidContainer(state.getBlock(), world, pos.below());
	}
	
	@Override
	@Deprecated
	public FluidState getFluidState(BlockState state) {
		return WaterloggingHelper.isWaterlogged(state) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	@Deprecated
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos) {
		WaterloggingHelper.sheduleWaterTick(state, level, pos);
		return !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, level, pos, facingPos);
	}
}
