package sirttas.elementalcraft.block.shrine.upgrade.horizontal;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import sirttas.elementalcraft.block.shrine.upgrade.AbstractShrineUpgradeBlock;

public abstract class AbstractHorizontalShrineUpgradeBlock extends AbstractShrineUpgradeBlock {

	public static final DirectionProperty FACING = HorizontalBlock.FACING;

	protected AbstractHorizontalShrineUpgradeBlock() {
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> container) {
		container.add(FACING);
	}

	@Override
	@Deprecated
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

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