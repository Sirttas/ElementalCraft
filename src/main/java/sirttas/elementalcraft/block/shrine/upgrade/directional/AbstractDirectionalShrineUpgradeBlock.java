package sirttas.elementalcraft.block.shrine.upgrade.directional;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import sirttas.elementalcraft.block.shrine.upgrade.AbstractShrineUpgradeBlock;

public abstract class AbstractDirectionalShrineUpgradeBlock extends AbstractShrineUpgradeBlock {

	public static final DirectionProperty FACING = DirectionalBlock.FACING;

	protected AbstractDirectionalShrineUpgradeBlock() {
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite());
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

	@Override
	@Deprecated
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	public Direction getFacing(BlockState state) {
		return state.getValue(FACING);
	}

}