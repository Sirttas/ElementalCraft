package sirttas.elementalcraft.block.instrument.io.mill.woodsaw;

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
import sirttas.elementalcraft.block.WaterLoggingHelper;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.IInstrumentBlock;
import sirttas.elementalcraft.container.ECContainerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WaterMillWoodSawBlock extends AbstractECContainerBlock implements IInstrumentBlock {

	public static final String NAME = "water_mill_wood_saw";

	private static final VoxelShape OVEN_SLAB = Block.box(0D, 2D, 0D, 16D, 4D, 16D);
	private static final VoxelShape OVEN_SLAB_2 = Block.box(0D, 10D, 0D, 16D, 12D, 16D);
	private static final VoxelShape CONNECTION = Block.box(6D, 0D, 6D, 10D, 2D, 10D);
	private static final VoxelShape PILLAR_1 = Block.box(1D, 0D, 1D, 3D, 10D, 3D);
	private static final VoxelShape PILLAR_2 = Block.box(13D, 0D, 1D, 15D, 10D, 3D);
	private static final VoxelShape PILLAR_3 = Block.box(1D, 0D, 13D, 3D, 10D, 15D);
	private static final VoxelShape PILLAR_4 = Block.box(13D, 0D, 13D, 15D, 10D, 15D);
	private static final VoxelShape SHAFT = Block.box(7D, 4D, 7D, 9D, 10D, 9D);
	private static final VoxelShape SHAPE = Shapes.or(OVEN_SLAB, OVEN_SLAB_2, CONNECTION, PILLAR_1, PILLAR_2, PILLAR_3, PILLAR_4, SHAFT);

	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	public WaterMillWoodSawBlock() {
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(FACING, Direction.NORTH)
				.setValue(WATERLOGGED, false));
	}


	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new WaterMillWoodSawBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
		return createInstrumentTicker(level, type, ECBlockEntityTypes.WATER_MILL_WOOD_SAW);
	}

	@Nonnull
    @Override
	@Deprecated
	public InteractionResult use(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
		final WaterMillWoodSawBlockEntity airMill = (WaterMillWoodSawBlockEntity) world.getBlockEntity(pos);
		IItemHandler inv = ECContainerHelper.getItemHandlerAt(world, pos, null);
		ItemStack heldItem = player.getItemInHand(hand);

		if (airMill != null && hand == InteractionHand.MAIN_HAND) {
			if (!airMill.getInventory().getItem(1).isEmpty()) {
				return this.onSlotActivated(inv, player, ItemStack.EMPTY, 1);
			}
			return this.onSlotActivated(inv, player, heldItem, 0);
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
    @Override
	@Deprecated
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED, FACING);
	}
	
	@Override
	@Deprecated
	public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader level, BlockPos pos) {
		return BlockEntityHelper.isValidContainer(state, level,  pos.below());
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
