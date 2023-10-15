package sirttas.elementalcraft.block.instrument.io.mill;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.block.WaterLoggingHelper;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.shrine.AbstractPylonShrineBlock;

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
	protected static final VoxelShape SHAPE_UPPER =  Block.box(7D, 0D, 7D, 9D, 16D, 9D);

	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

	protected AbstractAirMillBlock() {
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(FACING, Direction.NORTH)
				.setValue(HALF, DoubleBlockHalf.LOWER)
				.setValue(WATERLOGGED, false));
	}

	protected boolean isLower(BlockState state) {
		return state.getValue(HALF) == DoubleBlockHalf.LOWER;
	}

	@Nonnull
    @Override
	@Deprecated
	public InteractionResult use(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
		if (isLower(state)) {
			return super.use(state, world, pos, player, hand, hit);
		}
		return InteractionResult.PASS;
	}
	
	/**
	 * Called by ItemBlocks after a block is set in the world, to allow post-place
	 * logic
	 */
	@Override
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, @Nonnull ItemStack stack) {
		worldIn.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
	}

	/**
	 * Called before the Block is set to air in the world. Called regardless of if
	 * the player's tool can actually collect this block
	 */
	@Override
	public void playerWillDestroy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Player player) {
		AbstractPylonShrineBlock.doubleHalfHarvest(level, pos, state, player);
		super.playerWillDestroy(level, pos, state, player);
	}

	@Override
	@Nonnull
	@Deprecated
	public BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull LevelAccessor level, @Nonnull BlockPos pos, @Nonnull BlockPos facingPos) {
		return AbstractPylonShrineBlock.doubleHalfUpdateShape(state, facing, facingState, level, pos, () -> {
			WaterLoggingHelper.scheduleWaterTick(state, level, pos);
			return !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, level, pos, facingPos);
		});
	}

	@Override
	public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
		if (!AbstractPylonShrineBlock.canReplaceAboveBlock(context)) {
			return null;
		}
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
		builder.add(WATERLOGGED, FACING, HALF);
	}
	
	@Override
	@Deprecated
	public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader level, BlockPos pos) {
		var below = pos.below();
		
		return (isLower(state) && BlockEntityHelper.isValidContainer(state, level, below)) || level.getBlockState(below).is(this);
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
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return isLower(state) ? SHAPE_LOWER : SHAPE_UPPER;
	}
}
