package sirttas.elementalcraft.block.shrine.upgrade.acceleration.overclocked;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.pipe.IPipeConnectedBlock;
import sirttas.elementalcraft.block.shape.ShapeHelper;
import sirttas.elementalcraft.block.shrine.AbstractPylonShrineBlock;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.AbstractHorizontalShrineUpgradeBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class OverclockedAccelerationShrineUpgradeBlock extends AbstractHorizontalShrineUpgradeBlock implements EntityBlock {

    public static final String NAME = "shrine_upgrade_overclocked_acceleration";
    public static final MapCodec<OverclockedAccelerationShrineUpgradeBlock> CODEC = simpleCodec(OverclockedAccelerationShrineUpgradeBlock::new);

    private static final VoxelShape CORE_1 = Block.box(5D, 5D, 3D, 11D, 11D, 9D);
    private static final VoxelShape CORE_2 = Block.box(6D, 11D, 4D, 10D, 16D, 8D);
    private static final VoxelShape PILAR = Block.box(7D, 2D, 5D, 9D, 5D, 7D);
    private static final VoxelShape BASE_1 = Block.box(5D, 0D, 3D, 11D, 1D, 9D);
    private static final VoxelShape BASE_2 = Block.box(6D, 1D, 4D, 10D, 2D, 8D);
    private static final VoxelShape PIPE = Block.box(7D, 7D, 0D, 9D, 9D, 3D);
    private static final VoxelShape LOWER_SHAPE = Shapes.or(CORE_1, CORE_2, PILAR, BASE_1, BASE_2, PIPE);
    private static final VoxelShape CONNECTOR = Block.box(7D, 7D, 9D, 9D, 9D, 16D);
    private static final VoxelShape PIPE_UPPER = Block.box(7D, 0D, 5D, 9D, 3D, 7D);
    private static final VoxelShape CORE_UPPER = Block.box(5D, 3D, 3D, 11D, 9D, 9D);
    private static final Map<Direction, VoxelShape> UPPER_SHAPES = ShapeHelper.directionShapes(Direction.NORTH, Shapes.or(PIPE_UPPER, CORE_UPPER));
    private static final Map<Direction, VoxelShape> LOWER_SHAPES = ShapeHelper.directionShapes(Direction.NORTH, LOWER_SHAPE);
    private static final Map<Direction, VoxelShape> CONNECTED_LOWER_SHAPES = ShapeHelper.directionShapes(Direction.NORTH, Shapes.or(LOWER_SHAPE, CONNECTOR));

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final BooleanProperty CONNECTED = BooleanProperty.create("connected");

    public OverclockedAccelerationShrineUpgradeBlock(BlockBehaviour.Properties properties) {
        super(ShrineUpgrades.OVERCLOCKED_ACCELERATION, properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(CONNECTED, false)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected @NotNull MapCodec<OverclockedAccelerationShrineUpgradeBlock> codec() {
        return CODEC;
    }

    @Nonnull
    @Override
    @Deprecated
    public VoxelShape getShape(BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return UPPER_SHAPES.get(state.getValue(FACING));
        } else if (Boolean.TRUE.equals(state.getValue(CONNECTED))) {
            return CONNECTED_LOWER_SHAPES.get(state.getValue(FACING));
        }
        return LOWER_SHAPES.get(state.getValue(FACING));
    }

    @Override
    public void setPlacedBy(@Nonnull Level level, BlockPos pos, @Nonnull BlockState state, LivingEntity placer, @Nonnull ItemStack stack) {
        level.setBlock(pos.above(), this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER).setValue(FACING, state.getValue(FACING)), 3);
        super.setPlacedBy(level, pos, state, placer, stack);
    }


    @Nonnull
    @Override
    public Direction getFacing(@Nonnull BlockState state) {
        return state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? state.getValue(FACING) : Direction.DOWN;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? new OverclockedAccelerationShrineUpgradeBlockEntity(pos, state) : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        super.createBlockStateDefinition(container);
        container.add(HALF, CONNECTED);
    }

    @Override
    @Deprecated
    public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader level, @Nonnull BlockPos pos) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return super.canSurvive(state, level, pos);
        }
        return level.getBlockState(pos.below()).is(this);
    }

    @Override
    public BlockState playerWillDestroy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Player player) {
        AbstractPylonShrineBlock.doubleHalfHarvest(level, pos, state, player);
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
        if (!AbstractPylonShrineBlock.canReplaceAboveBlock(context)) {
            return null;
        }

        var state = super.getStateForPlacement(context);

        if (state == null) {
            return null;
        }

        return getConnectionState(state, context.getLevel(), context.getClickedPos());
    }

    @Nonnull
    @Override
    @Deprecated
    public BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull LevelAccessor level, @Nonnull BlockPos pos, @Nonnull BlockPos facingPos) {
        return AbstractPylonShrineBlock.doubleHalfUpdateShape(state, facing, facingState, level, pos, () -> {
            var newState = super.updateShape(state, facing, facingState, level, pos, facingPos);

            return newState.is(this) ? getConnectionState(newState, level, pos) : newState;
        });
    }

    @Nonnull
    private static BlockState getConnectionState(@Nonnull BlockState state, @Nonnull LevelAccessor level, @Nonnull BlockPos currentPos) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return state.setValue(CONNECTED, false);
        }
        return state.setValue(CONNECTED, IPipeConnectedBlock.isConnectable(level, currentPos, state.getValue(FACING).getOpposite()));
    }
}
