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
import sirttas.elementalcraft.block.shrine.upgrade.AbstractBlockShrineUpgrade;

public abstract class AbstractBlockHorizontalShrineUpgrade extends AbstractBlockShrineUpgrade {

	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

	protected AbstractBlockHorizontalShrineUpgrade() {
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
		container.add(FACING);
	}

	@Override
	@Deprecated
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}

	@Override
	public Direction getFacing(BlockState state) {
		return state.get(FACING);
	}

}