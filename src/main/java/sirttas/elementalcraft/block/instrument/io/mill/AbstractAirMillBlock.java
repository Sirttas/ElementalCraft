package sirttas.elementalcraft.block.instrument.io.mill;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.WaterLoggingHelper;
import sirttas.elementalcraft.block.airmill.AirMill;
import sirttas.elementalcraft.block.container.ElementContainer;
import sirttas.elementalcraft.block.shrine.AbstractPylonShrineBlock;
import sirttas.elementalcraft.item.ECItems;

import javax.annotation.Nonnull;

public abstract class AbstractAirMillBlock extends AbstractMillBlock {
	private static final VoxelShape OVEN_SLAB = Block.box(0D, 2D, 0D, 16D, 4D, 16D);
	private static final VoxelShape OVEN_SLAB_2 = Block.box(0D, 10D, 0D, 16D, 12D, 16D);
	private static final VoxelShape OVEN_CONNECTION = Block.box(6D, 0D, 6D, 10D, 2D, 10D);
	private static final VoxelShape OVEN_PILLAR_1 = Block.box(1D, 0D, 1D, 3D, 10D, 3D);
	private static final VoxelShape OVEN_PILLAR_2 = Block.box(13D, 0D, 1D, 15D, 10D, 3D);
	private static final VoxelShape OVEN_PILLAR_3 = Block.box(1D, 0D, 13D, 3D, 10D, 15D);
	private static final VoxelShape OVEN_PILLAR_4 = Block.box(13D, 0D, 13D, 15D, 10D, 15D);
	private static final VoxelShape OVEN_SHAFT = Block.box(7D, 4D, 7D, 9D, 10D, 9D);
	protected static final VoxelShape SHAPE_LOWER = Shapes.or(OVEN_SLAB, OVEN_SLAB_2, OVEN_CONNECTION, OVEN_PILLAR_1, OVEN_PILLAR_2, OVEN_PILLAR_3, OVEN_PILLAR_4, OVEN_SHAFT);
	public static final VoxelShape SHAPE_UPPER =  Block.box(7D, 0D, 7D, 9D, 16D, 9D);

	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
	public static final BooleanProperty BROKEN = BooleanProperty.create("broken");

	protected AbstractAirMillBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(FACING, Direction.NORTH)
				.setValue(HALF, DoubleBlockHalf.LOWER)
				.setValue(BROKEN, false)
				.setValue(WATERLOGGED, false));
	}

	protected boolean isLower(BlockState state) {
		return state.getValue(HALF) == DoubleBlockHalf.LOWER;
	}

	@Nonnull
    @Override
	protected InteractionResult useItemOn(@Nonnull ItemStack stack, @Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
		if (!isLower(state)) {
			return InteractionResult.PASS;
		} else if (stack.is(ECItems.AIR_MILL)) {
			return AirMill.setMill(stack, state, level, pos, player, hand);
		}
		return super.useItemOn(stack, state, level, pos, player, hand, hit);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, @Nonnull ItemStack stack) {
		level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
	}

	@Override
	public @NotNull BlockState playerWillDestroy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Player player) {
		AbstractPylonShrineBlock.doubleHalfHarvest(level, pos, state, player);
		return super.playerWillDestroy(level, pos, state, player);
	}

	@Override
	@Nonnull
    protected BlockState updateShape(@NotNull BlockState state, @NotNull LevelReader level, @NotNull ScheduledTickAccess ticks, @NotNull BlockPos pos, @NotNull Direction directionToNeighbour, @NotNull BlockPos neighbourPos, @NotNull BlockState neighbourState, @NotNull RandomSource random) {
        return AbstractPylonShrineBlock.doubleHalfUpdateShape(state, directionToNeighbour, neighbourState, level, pos, () -> {
			WaterLoggingHelper.scheduleWaterTick(state, level, ticks, pos);
			return !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
		});
	}

	@Override
	public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
		if (!AbstractPylonShrineBlock.canReplaceAboveBlock(context)) {
			return null;
		}
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, WaterLoggingHelper.isPlacedInWater(context));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED, FACING, HALF, BROKEN);
	}
	
	@Override
	public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader level, BlockPos pos) {
		var below = pos.below();
		
		return (isLower(state) && ElementContainer.isValidContainer(state, level, below)) || level.getBlockState(below).is(this);
	}
	
	@Nonnull
    @Override
	public FluidState getFluidState(@Nonnull BlockState state) {
		return WaterLoggingHelper.isWaterlogged(state) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return isLower(state) ? SHAPE_LOWER : SHAPE_UPPER;
	}
}
