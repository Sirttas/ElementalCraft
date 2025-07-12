package sirttas.elementalcraft.block.synthesizer.culinary;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.AbstractECContainerBlock;
import sirttas.elementalcraft.block.container.ElementContainer;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.shape.ShapeHelper;
import sirttas.elementalcraft.block.synthesizer.AbstractSynthesizerBlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class CulinarySynthesizerBlock extends AbstractECContainerBlock {

	public static final String NAME = "culinary_water_synthesizer";
	public static final MapCodec<CulinarySynthesizerBlock> CODEC = simpleCodec(CulinarySynthesizerBlock::new);

	private static final VoxelShape BASE_1 = Block.box(0D, 1D, 0D, 16D, 3D, 16D);
	private static final VoxelShape BASE_2 = Shapes.join(
			Block.box(2D, 3D, 2D, 14D, 5D, 14D),
			Block.box(6D, 4D, 6D, 10D, 5D, 10D),
			BooleanOp.ONLY_FIRST);
	private static final VoxelShape BASE_3 = Block.box(6D, 0D, 6D, 10D, 1D, 10D);

	private static final VoxelShape PIPE_1 = Block.box(1D, 0D, 1D, 3D, 6D, 3D);
	private static final VoxelShape PIPE_2 = Block.box(13D, 0D, 1D, 15D, 6D, 3D);
	private static final VoxelShape PIPE_3 = Block.box(1D, 0D, 13D, 3D, 6D, 15D);
	private static final VoxelShape PIPE_4 = Block.box(13D, 0D, 13D, 15D, 6D, 15D);

	private static final VoxelShape FUNNEL_1 = Block.box(6D, 5D, 3D, 10D, 7D, 7D);
	private static final VoxelShape FUNNEL_2 = Block.box(6D, 7D, 1D, 10D, 9D, 5D);
	private static final VoxelShape FUNNEL_3 = Block.box(6D, 9D, 0D, 10D, 10D, 3D);

	private static final Map<Direction, VoxelShape> SHAPES = ShapeHelper.directionShapes(Direction.NORTH, Shapes.or(BASE_1, BASE_2, BASE_3, PIPE_1, PIPE_2, PIPE_3, PIPE_4, FUNNEL_1, FUNNEL_2, FUNNEL_3));

	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	public CulinarySynthesizerBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(FACING, Direction.NORTH));
	}

	@Override
	protected @NotNull MapCodec<? extends CulinarySynthesizerBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new CulinarySynthesizerBlockEntity(pos, state);
	}

	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
		return createECServerTicker(level, type, ECBlockEntityTypes.CULINARY_SYNTHESIZER, CulinarySynthesizerBlockEntity::serverTick);
	}
	
	@Nonnull
    @Override
	protected ItemInteractionResult useItemOn(@Nonnull ItemStack stack, @Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @NotNull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
		if (stack.isEmpty() || stack.getFoodProperties(null) != null) {
			return onSingleSlotActivated(stack, level, pos, player, hand);
		}
		return super.useItemOn(stack, state, level, pos, player, hand, hit);
	}

	@Override
	public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
		container.add(FACING);
	}

	@Nonnull
	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@SuppressWarnings("deprecation")
	@Nonnull
	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Nonnull
    @Override
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPES.get(state.getValue(FACING));
	}

	@Override
	public void animateTick(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
		AbstractSynthesizerBlockEntity.renderElementFlow(level, pos, rand);
	}
	
	@Override
	public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader level, BlockPos pos) {
		return ElementContainer.isValidContainer(state, level, pos.below());
	}
}
