package sirttas.elementalcraft.block.shrine.upgrade;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.dpanvil.api.data.IDataWrapper;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.WaterloggingHelper;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.property.ECProperties;

public abstract class AbstractShrineUpgradeBlock extends Block implements SimpleWaterloggedBlock {

	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	private final IDataWrapper<ShrineUpgrade> upgrade;

	protected AbstractShrineUpgradeBlock(ResourceLocation name) {
		super(ECProperties.Blocks.BLOCK_NOT_SOLID);
		upgrade = ElementalCraft.SHRINE_UPGRADE_MANAGER.getWrapper(name);
		this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
	}

	@Override
	@Deprecated
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		BlockEntityHelper.getBlockEntityAs(world, pos.relative(getFacing(state)), AbstractShrineBlockEntity.class).ifPresent(AbstractShrineBlockEntity::setChanged);
	}

	@Override
	@Deprecated
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		BlockEntityHelper.getBlockEntityAs(world, pos.relative(getFacing(state)), AbstractShrineBlockEntity.class).ifPresent(AbstractShrineBlockEntity::setChanged);
		super.onRemove(state, world, pos, newState, isMoving);
	}

	@Override
	@Deprecated
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		Direction facing = getFacing(state);

		return upgrade.isPresent() && BlockEntityHelper.getBlockEntityAs(level, pos.relative(facing), AbstractShrineBlockEntity.class)
				.filter(shrine -> shrine.getUpgradeDirections().contains(facing.getOpposite()) && upgrade.get().canUpgrade(shrine, level.getBlockState(pos).is(this))).isPresent();
	}
	
	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(WATERLOGGED, WaterloggingHelper.isPlacedInWater(context));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED);
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
	
	@Override
	@Deprecated
	public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
		if (!state.canSurvive(worldIn, pos)) {
			worldIn.destroyBlock(pos, true);
		} else {
			super.tick(state, worldIn, pos, rand);
		}
	}
	
	public abstract Direction getFacing(BlockState state);

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		upgrade.ifPresent(u -> u.addInformation(tooltip));
	}

	public ShrineUpgrade getUpgrade() {
		return upgrade.get();
	}
}
