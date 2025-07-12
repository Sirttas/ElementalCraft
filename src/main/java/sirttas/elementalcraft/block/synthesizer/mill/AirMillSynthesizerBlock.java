package sirttas.elementalcraft.block.synthesizer.mill;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.AbstractECContainerBlock;
import sirttas.elementalcraft.block.WaterLoggingHelper;
import sirttas.elementalcraft.block.airmill.AirMill;
import sirttas.elementalcraft.block.container.ElementContainer;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.io.mill.AbstractAirMillBlock;
import sirttas.elementalcraft.block.shrine.AbstractPylonShrineBlock;
import sirttas.elementalcraft.block.synthesizer.AbstractSynthesizerBlockEntity;
import sirttas.elementalcraft.item.ECItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AirMillSynthesizerBlock extends AbstractECContainerBlock {

	public static final String NAME = "air_mill_synthesizer";
	public static final MapCodec<AirMillSynthesizerBlock> CODEC = simpleCodec(AirMillSynthesizerBlock::new);

	private static final VoxelShape BASE_1 = Block.box(6D, 0D, 6D, 10D, 1D, 10D);
	private static final VoxelShape BASE_2 = Block.box(0D, 1D, 0D, 16D, 3D, 16D);
	private static final VoxelShape BASE_3 = Block.box(3D, 8D, 3D, 13D, 10D, 13D);

	private static final VoxelShape SIDE_PILLAR_1 = Block.box(1D, 0D, 1D, 3D, 4D, 3D);
	private static final VoxelShape SIDE_PILLAR_2 = SIDE_PILLAR_1.move(12D / 16, 0D, 0D);
	private static final VoxelShape SIDE_PILLAR_3 = SIDE_PILLAR_1.move(0D, 0D, 12D / 16);
	private static final VoxelShape SIDE_PILLAR_4 = SIDE_PILLAR_1.move(12D / 16, 0D, 12D / 16);

	private static final VoxelShape INNER_PILLAR_1 = Block.box(4D, 3D, 4D, 6D, 11D, 6D);
	private static final VoxelShape INNER_PILLAR_2 = INNER_PILLAR_1.move(6D / 16, 0D, 0D);
	private static final VoxelShape INNER_PILLAR_3 = INNER_PILLAR_1.move(0D, 0D, 6D / 16);
	private static final VoxelShape INNER_PILLAR_4 = INNER_PILLAR_1.move(6D / 16, 0D, 6D / 16);

	private static final VoxelShape SHAFT = Block.box(7D, 3D, 7D, 9D, 10D, 9D);

	private static final VoxelShape SHAPE = Shapes.or(BASE_1, BASE_2, BASE_3, SIDE_PILLAR_1, SIDE_PILLAR_2, SIDE_PILLAR_3, SIDE_PILLAR_4, INNER_PILLAR_1, INNER_PILLAR_2, INNER_PILLAR_3, INNER_PILLAR_4, SHAFT);

	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

	public AirMillSynthesizerBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(HALF, DoubleBlockHalf.LOWER)
				.setValue(AbstractAirMillBlock.BROKEN, false));
	}

	protected boolean isLower(BlockState state) {
		return state.getValue(HALF) == DoubleBlockHalf.LOWER;
	}

	@Override
	protected @NotNull MapCodec<? extends AirMillSynthesizerBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		if (!isLower(state)) {
			return null;
		}
		return new AirMillSynthesizerBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
		if (!isLower(state)) {
			return null;
		}
		return createECServerTicker(level, type, ECBlockEntityTypes.AIR_MILL_SYNTHESIZER, AirMillSynthesizerBlockEntity::serverTick);
	}
	
	@Nonnull
    @Override
	protected ItemInteractionResult useItemOn(@Nonnull ItemStack stack, @Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @NotNull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
		if (!isLower(state)) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		} else if (stack.isEmpty() || stack.is(ECItems.AIR_MILL)) {
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
		return this.defaultBlockState();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(HALF, AbstractAirMillBlock.BROKEN);
	}

	@Nonnull
    @Override
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return isLower(state) ? SHAPE : AbstractAirMillBlock.SHAPE_UPPER;
	}

	@Override
	public void animateTick(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
		AbstractSynthesizerBlockEntity.renderElementFlow(level, pos, rand);
	}
	
	@Override
	public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader level, BlockPos pos) {
		var below = pos.below();

		return (isLower(state) && ElementContainer.isValidContainer(state, level, below)) || level.getBlockState(below).is(this);
	}
}
