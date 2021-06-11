package sirttas.elementalcraft.block.shrine;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;

public abstract class AbstractPylonShrineBlock extends AbstractShrineBlock {

	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

	protected AbstractPylonShrineBlock(ElementType elementType) {
		super(elementType);
		this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return state.getValue(HALF) == DoubleBlockHalf.LOWER;
	}

	/**
	 * Called by ItemBlocks after a block is set in the world, to allow post-place
	 * logic
	 */
	@Override
	public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		worldIn.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
	}

	/**
	 * Called before the Block is set to air in the world. Called regardless of if
	 * the player's tool can actually collect this block
	 */
	@Override
	public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		doubeHalfHarvest(this, worldIn, pos, state, player);
		super.playerWillDestroy(worldIn, pos, state, player);
	}

	public static void doubeHalfHarvest(Block block, World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		DoubleBlockHalf doubleblockhalf = state.getValue(HALF);
		BlockPos blockpos = doubleblockhalf == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
		BlockState blockstate = worldIn.getBlockState(blockpos);
	
		if (blockstate.getBlock() == block && blockstate.getValue(HALF) != doubleblockhalf) {
			worldIn.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
			worldIn.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
		}
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HALF);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
			BlockEntityHelper.getTileEntityAs(world, pos.below(), AbstractShrineBlockEntity.class).filter(AbstractShrineBlockEntity::isRunning).ifPresent(s -> this.doAnimateTick(s, state, world, pos, rand));
		}
	}

}