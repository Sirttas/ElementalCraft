package sirttas.elementalcraft.block.synthesizer.vibration;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.AbstractECContainerBlock;
import sirttas.elementalcraft.block.container.ElementContainer;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.synthesizer.AbstractSynthesizerBlockEntity;
import sirttas.elementalcraft.gameevent.ECGameEvents;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VibrationSynthesizerBlock extends AbstractECContainerBlock {

	public static final String NAME = "vibration_air_synthesizer";
	public static final MapCodec<VibrationSynthesizerBlock> CODEC = simpleCodec(VibrationSynthesizerBlock::new);

	private static final VoxelShape BASE_1 = Block.box(6D, 0D, 6D, 10D, 1D, 10D);
	private static final VoxelShape BASE_2 = Block.box(4D, 1D, 4D, 12D, 3D, 12D);
	private static final VoxelShape BASE_3 = Block.box(5D, 3D, 5D, 7D, 4D, 7D);
	private static final VoxelShape BASE_4 = Block.box(9D, 3D, 5D, 11D, 4D, 7D);
	private static final VoxelShape BASE_5 = Block.box(5D, 3D, 9D, 7D, 4D, 11D);
	private static final VoxelShape BASE_6 = Block.box(9D, 3D, 9D, 11D, 4D, 11D);

	private static final VoxelShape SHAPE = Shapes.or(BASE_1, BASE_2, BASE_3, BASE_4, BASE_5, BASE_6);

	public static final EnumProperty<SculkSensorPhase> PHASE = BlockStateProperties.SCULK_SENSOR_PHASE;

	public VibrationSynthesizerBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(PHASE, SculkSensorPhase.INACTIVE));
	}

	@Override
	protected @NotNull MapCodec<? extends VibrationSynthesizerBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new VibrationSynthesizerBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
		return createECServerTicker(level, type, ECBlockEntityTypes.VIBRATION_SYNTHESIZER, VibrationSynthesizerBlockEntity::serverTick);
	}

	@Override
	protected void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource source) {
		var phase = getPhase(state);

		if (phase == SculkSensorPhase.ACTIVE) {
            level.gameEvent(ECGameEvents.AIR_SYNTHESIS, pos, GameEvent.Context.of(state));
			level.setBlockAndUpdate(pos, state.setValue(PHASE, SculkSensorPhase.COOLDOWN));
			level.scheduleTick(pos, state.getBlock(), 10);
		} else if (phase == SculkSensorPhase.COOLDOWN) {
			level.setBlockAndUpdate(pos, state.setValue(PHASE, SculkSensorPhase.INACTIVE));
		}
	}
	
	@Nonnull
    @Override
	protected InteractionResult useItemOn(@Nonnull ItemStack stack, @Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @NotNull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
		final VibrationSynthesizerBlockEntity synthesizer = (VibrationSynthesizerBlockEntity) level.getBlockEntity(pos);

		if (synthesizer != null && player.getItemInHand(hand).isEmpty() && player.isShiftKeyDown()) {
			if (level.isClientSide) {
				synthesizer.startShowingRange();
			}
			return InteractionResult.SUCCESS;
		}
		return super.useItemOn(stack, state, level, pos, player, hand, hit);
	}

	@Nonnull
    @Override
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}

	@Override
	public void animateTick(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
		AbstractSynthesizerBlockEntity.renderElementFlow(level, pos, rand);
	}
	
	@Override
	public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader level, BlockPos pos) {
		return ElementContainer.isValidContainer(state, level, pos.below());
	}

	public static SculkSensorPhase getPhase(BlockState state) {
		return state.getValue(PHASE);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(PHASE);
	}
}
