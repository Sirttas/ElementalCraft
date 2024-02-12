package sirttas.elementalcraft.block.shrine.upgrade;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.WaterLoggingHelper;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractShrineUpgradeBlock extends Block implements SimpleWaterloggedBlock {

	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	private final Holder<ShrineUpgrade> upgrade;

	protected AbstractShrineUpgradeBlock(@Nonnull ResourceKey<ShrineUpgrade> key, BlockBehaviour.Properties properties) {
		super(properties);
		upgrade = ElementalCraft.SHRINE_UPGRADE_MANAGER.getOrCreateHolder(key);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(WATERLOGGED, false));
	}

	@Nonnull
    @Override
	@Deprecated
	public RenderShape getRenderShape(@Nonnull BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public void setPlacedBy(@Nonnull Level level, BlockPos pos, @Nonnull BlockState state, LivingEntity placer, @Nonnull ItemStack stack) {
		BlockEntityHelper.getBlockEntityAs(level, pos.relative(getFacing(state)), AbstractShrineBlockEntity.class).ifPresent(AbstractShrineBlockEntity::setChanged);
	}

	@Override
	@Deprecated
	public void onRemove(@Nonnull BlockState state, @Nonnull Level level, BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		BlockEntityHelper.getBlockEntityAs(level, pos.relative(getFacing(state)), AbstractShrineBlockEntity.class).ifPresent(AbstractShrineBlockEntity::setChanged);
		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override
	@Deprecated
	public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader level, @Nonnull BlockPos pos) {
		if (!upgrade.isBound()) { // we only remove upgrades if it is fully loaded
			return true;
		}

		var facing = getFacing(state);
		var shrinePos = pos.relative(facing);

		if (!level.isAreaLoaded(shrinePos, 1)) { // don't remove the upgrade is the shrine is not in a loaded chunk
			return true;
		}

		return BlockEntityHelper.getBlockEntityAs(level, shrinePos, AbstractShrineBlockEntity.class)
				.filter(shrine -> shrine.getUpgradeDirections().contains(facing.getOpposite()) && getUpgrade().canUpgrade(shrine, level.getBlockState(pos).is(this)))
				.isPresent();
	}

	@Nullable
	@Override
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
	
	@Override
	@Deprecated
	public void tick(BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
		if (!state.canSurvive(level, pos)) {
			level.destroyBlock(pos, true);
		} else {
			super.tick(state, level, pos, rand);
		}
	}

	@Nonnull
	public abstract Direction getFacing(@Nonnull BlockState state);

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable BlockGetter worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
		if (upgrade.isBound()) {
			getUpgrade().addInformation(tooltip);
		}
	}

	public ShrineUpgrade getUpgrade() {
		return upgrade.value();
	}
}
