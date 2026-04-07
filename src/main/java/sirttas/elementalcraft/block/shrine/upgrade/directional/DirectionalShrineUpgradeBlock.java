package sirttas.elementalcraft.block.shrine.upgrade.directional;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgradeBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class DirectionalShrineUpgradeBlock extends ShrineUpgradeBlock {

	public static final EnumProperty<@NotNull Direction> FACING = DirectionalBlock.FACING;

	protected DirectionalShrineUpgradeBlock(ResourceKey<ShrineUpgrade> key, BlockBehaviour.Properties properties) {
		super(key, properties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(FACING, Direction.UP)
				.setValue(WATERLOGGED, false));
	}

	@Nullable
    @Override
	public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
		container.add(WATERLOGGED, FACING);
	}

	@Nonnull
    @Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@SuppressWarnings("deprecation")
    @Nonnull
    @Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Nonnull
	@Override
	public Direction getFacing(@Nonnull BlockState state) {
		return state.getValue(FACING);
	}

}
