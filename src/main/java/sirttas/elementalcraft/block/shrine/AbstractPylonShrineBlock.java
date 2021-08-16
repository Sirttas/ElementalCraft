package sirttas.elementalcraft.block.shrine;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;

public abstract class AbstractPylonShrineBlock<T extends AbstractShrineBlockEntity>  extends AbstractShrineBlock<T> {

	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

	protected AbstractPylonShrineBlock(ElementType elementType) {
		super(elementType);
		this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER).setValue(WATERLOGGED, false));
	}

	/**
	 * Called by ItemBlocks after a block is set in the world, to allow post-place
	 * logic
	 */
	@Override
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		worldIn.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
	}

	/**
	 * Called before the Block is set to air in the world. Called regardless of if
	 * the player's tool can actually collect this block
	 */
	@Override
	public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
		doubeHalfHarvest(this, worldIn, pos, state, player);
		super.playerWillDestroy(worldIn, pos, state, player);
	}

	public static void doubeHalfHarvest(Block block, Level worldIn, BlockPos pos, BlockState state, Player player) {
		DoubleBlockHalf doubleblockhalf = state.getValue(HALF);
		BlockPos blockpos = doubleblockhalf == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
		BlockState blockstate = worldIn.getBlockState(blockpos);
	
		if (blockstate.getBlock() == block && blockstate.getValue(HALF) != doubleblockhalf) {
			worldIn.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
			worldIn.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED, HALF);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
		if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
			BlockEntityHelper.getTileEntityAs(world, pos.below(), AbstractShrineBlockEntity.class).filter(AbstractShrineBlockEntity::isRunning).ifPresent(s -> this.doAnimateTick(s, state, world, pos, rand));
		}
	}

	
	@Override
	@Deprecated
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return state.getValue(HALF) == DoubleBlockHalf.UPPER && !level.getBlockState(pos.below()).is(this);
	}
	
}