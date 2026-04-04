package sirttas.elementalcraft.block.container;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.pipe.IPipeConnectedBlock;

import javax.annotation.Nonnull;

public abstract class AbstractConnectedElementContainerBlock extends AbstractElementContainerBlock implements IPipeConnectedBlock {

	private static final VoxelShape CONNECTOR_NORTH = Shapes.or(Block.box(5D, 5D, 1D, 11D, 11D, 2D), Block.box(6D, 6D, 0D, 10D, 10D, 1D));
	private static final VoxelShape CONNECTOR_SOUTH = Shapes.or(Block.box(5D, 5D, 14D, 11D, 11D, 15D), Block.box(6D, 6D, 15D, 10D, 10D, 16D));
	private static final VoxelShape CONNECTOR_WEST = Shapes.or(Block.box(1D, 5D, 5D, 2D, 11D, 11D), Block.box(0D, 6D, 6D, 1D, 10D, 10D));
	private static final VoxelShape CONNECTOR_EAST = Shapes.or(Block.box(14D, 5D, 5D, 15D, 11D, 11D), Block.box(15D, 6D, 6D, 16D, 10D, 10D));

	protected AbstractConnectedElementContainerBlock(BlockBehaviour.Properties properties, Holder<@NotNull IConfigurableBlockEntityProperties> entityProperties) {
		super(properties, entityProperties);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		VoxelShape shape = Shapes.empty();

		if (Boolean.TRUE.equals(state.getValue(NORTH))) {
			shape = Shapes.or(shape, CONNECTOR_NORTH);
		}
		if (Boolean.TRUE.equals(state.getValue(SOUTH))) {
			shape = Shapes.or(shape, CONNECTOR_SOUTH);
		}
		if (Boolean.TRUE.equals(state.getValue(EAST))) {
			shape = Shapes.or(shape, CONNECTOR_EAST);
		}
		if (Boolean.TRUE.equals(state.getValue(WEST))) {
			shape = Shapes.or(shape, CONNECTOR_WEST);
		}
		return shape;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return doGetStateForPlacement(context.getLevel(), context.getClickedPos());
	}

    @NotNull
    @Override
    protected BlockState updateShape(@NotNull BlockState state, @NotNull LevelReader level, @NotNull ScheduledTickAccess ticks, @NotNull BlockPos pos, @NotNull Direction directionToNeighbour, @NotNull BlockPos neighbourPos, @NotNull BlockState neighbourState, @NotNull RandomSource random) {
        return doUpdateShape(state, level, pos, directionToNeighbour);
    }
}
