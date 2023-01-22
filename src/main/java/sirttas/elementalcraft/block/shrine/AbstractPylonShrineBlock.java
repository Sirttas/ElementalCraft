package sirttas.elementalcraft.block.shrine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public abstract class AbstractPylonShrineBlock<T extends AbstractShrineBlockEntity> extends AbstractShrineBlock<T> {


	protected AbstractPylonShrineBlock(ElementType elementType) {
		super(elementType);
		this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER).setValue(WATERLOGGED, false));
	}

	/**
	 * Called by ItemBlocks after a block is set in the world, to allow post-place
	 * logic
	 */
	@Override
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, @Nonnull ItemStack stack) {
		worldIn.setBlock(pos.above(), state.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER), 3);
	}

	/**
	 * Called before the Block is set to air in the world. Called regardless of if
	 * the player's tool can actually collect this block
	 */
	@Override
	public void playerWillDestroy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Player player) {
		doubleHalfHarvest(level, pos, state, player);
		super.playerWillDestroy(level, pos, state, player);
	}

	public static void doubleHalfHarvest(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Player player) {
		if (!level.isClientSide && player.isCreative()) {
			DoublePlantBlock.preventCreativeDropFromBottomPart(level, pos, state, player);
		}
	}

	@Override
	@Nonnull
	@Deprecated
	public BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull LevelAccessor level, @Nonnull BlockPos pos, @Nonnull BlockPos facingPos) {
		return doubleHalfUpdateShape(state, facing, facingState, level, pos, () -> super.updateShape(state, facing, facingState, level, pos, facingPos));
	}

	@Nonnull
	public static BlockState doubleHalfUpdateShape(@Nonnull BlockState state, Direction facing, @Nonnull BlockState facingState, @Nonnull LevelAccessor level, @Nonnull BlockPos pos, Supplier<BlockState> defaultState) {
		var doubleblockhalf = state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF);

		if (facing.getAxis() != Direction.Axis.Y || doubleblockhalf == DoubleBlockHalf.LOWER != (facing == Direction.UP) || facingState.is(state.getBlock()) && facingState.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) != doubleblockhalf) {
			return doubleblockhalf == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : defaultState.get();
		} else {
			return Blocks.AIR.defaultBlockState();
		}
	}


	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED, BlockStateProperties.DOUBLE_BLOCK_HALF);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
		if (state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
			BlockEntityHelper.getBlockEntityAs(level, pos.below(), AbstractShrineBlockEntity.class).filter(AbstractShrineBlockEntity::isRunning).ifPresent(s -> this.doAnimateTick(s, state, level, pos, rand));
		}
	}
	
	@Override
	@Deprecated
	public boolean canSurvive(BlockState state, @Nonnull LevelReader level, @Nonnull BlockPos pos) {
		return state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER || level.getBlockState(pos.below()).is(this);
	}
	
}
