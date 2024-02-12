package sirttas.elementalcraft.block.shrine;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.block.AbstractECEntityBlock;
import sirttas.elementalcraft.block.WaterLoggingHelper;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.shrine.upgrade.AbstractShrineUpgradeBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractShrineBlock<T extends AbstractShrineBlockEntity> extends AbstractECEntityBlock implements SimpleWaterloggedBlock, IElementTypeProvider {

	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	private final ElementType elementType;
	private BlockEntityType<T> entityType;

	protected AbstractShrineBlock(ElementType elementType, BlockBehaviour.Properties properties) {
		super(properties);
		this.elementType = elementType;
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(WATERLOGGED, false));
	}

	@Override
	public ElementType getElementType() {
		return this.elementType;
	}

	@Override
	@Deprecated
	public void onRemove(BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntityHelper.getBlockEntityAs(level, pos, AbstractShrineBlockEntity.class).ifPresent(shrine -> shrine.getUpgradeDirections().forEach(direction -> {
				BlockPos newPos = pos.relative(direction);
				BlockState upgradeState = level.getBlockState(newPos);
				Block block = upgradeState.getBlock();

				if (block instanceof AbstractShrineUpgradeBlock upgrade && upgrade.getFacing(upgradeState) == direction.getOpposite()) {
					level.destroyBlock(newPos, true);
				}
			}));
		}
		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Nonnull
	@Override
	@Deprecated
	public InteractionResult use(@Nonnull BlockState state, Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
		final AbstractShrineBlockEntity shrine = (AbstractShrineBlockEntity) level.getBlockEntity(pos);

		if (shrine != null && player.getItemInHand(hand).isEmpty() && player.isShiftKeyDown()) {
			if (level.isClientSide) {
				shrine.startShowingRange();
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, @Nonnull TooltipFlag flag) {
		tooltip.add(Component.translatable("tooltip.elementalcraft.consumes", elementType.getDisplayName()).withStyle(ChatFormatting.YELLOW));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
		BlockEntityHelper.getBlockEntityAs(level, pos, AbstractShrineBlockEntity.class)
				.filter(AbstractShrineBlockEntity::isRunning)
				.ifPresent(s -> this.doAnimateTick(s, state, level, pos, rand));
	}

	@OnlyIn(Dist.CLIENT)
	protected void doAnimateTick(AbstractShrineBlockEntity shrine, BlockState state, Level world, BlockPos pos, RandomSource rand) {
		// NO-OP
	}

	@Override
	public T newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return getEntityType().create(pos, state);
	}

	@Override
	@Nullable
	public <U extends BlockEntity> BlockEntityTicker<U> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<U> type) {
		return createECTicker(level, type, getEntityType(), level.isClientSide ? AbstractShrineBlockEntity::clientTick : AbstractShrineBlockEntity::serverTick);
	}

	@SuppressWarnings("unchecked")
	private BlockEntityType<T> getEntityType() {
		if (entityType == null) {
			entityType = (BlockEntityType<T>) BuiltInRegistries.BLOCK_ENTITY_TYPE.get(BuiltInRegistries.BLOCK.getKey(this));
		}
		return entityType;
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
