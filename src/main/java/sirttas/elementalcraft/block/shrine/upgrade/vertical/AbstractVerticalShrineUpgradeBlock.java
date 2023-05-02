package sirttas.elementalcraft.block.shrine.upgrade.vertical;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import sirttas.elementalcraft.block.shrine.upgrade.AbstractShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractVerticalShrineUpgradeBlock extends AbstractShrineUpgradeBlock {

	public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.VERTICAL);

	protected AbstractVerticalShrineUpgradeBlock(ResourceKey<ShrineUpgrade> key) {
		super(key);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(FACING, Direction.UP)
				.setValue(WATERLOGGED, false));
	}

	@Nullable
    @Override
	public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
		var direction = Direction.UP;

		if (context.getPlayer() != null) {
			direction = context.getNearestLookingVerticalDirection();
		} else if (context instanceof DirectionalPlaceContext directionalPlaceContext && directionalPlaceContext.direction.getAxis() == Direction.Axis.Y) {
			direction = directionalPlaceContext.direction;
		}
		return this.defaultBlockState().setValue(FACING, direction);
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
	@Override
	@Deprecated
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Nonnull
	@Override
	public Direction getFacing(@Nonnull BlockState state) {
		return state.getValue(FACING);
	}

}
