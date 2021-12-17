package sirttas.elementalcraft.block.shrine.upgrade.horizontal;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import sirttas.elementalcraft.block.shrine.upgrade.AbstractShrineUpgradeBlock;

import javax.annotation.Nonnull;

public abstract class AbstractHorizontalShrineUpgradeBlock extends AbstractShrineUpgradeBlock {

	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	protected AbstractHorizontalShrineUpgradeBlock(ResourceLocation name) {
		super(name);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
		container.add(WATERLOGGED, FACING);
	}

	@Nonnull
    @Override
	@Deprecated
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Nonnull
    @SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	public Direction getFacing(BlockState state) {
		return state.getValue(FACING);
	}

}
