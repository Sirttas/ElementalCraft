package sirttas.elementalcraft.block.source.breeder.pedestal;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.AbstractECContainerBlock;
import sirttas.elementalcraft.block.WaterLoggingHelper;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SourceBreederPedestalBlock extends AbstractECContainerBlock implements SimpleWaterloggedBlock {

    public static final String NAME = "source_breeder_pedestal";
    public static final MapCodec<SourceBreederPedestalBlock> CODEC = simpleCodec(SourceBreederPedestalBlock::new);

    private static final VoxelShape BASE_1 = Block.box(0D, 0D, 0D, 16D, 2D, 16D);
    private static final VoxelShape BASE_2 = Block.box(2D, 2D, 2D, 14D, 7D, 14D);
    private static final VoxelShape BASE_3 = Block.box(0D, 7D, 0D, 16D, 9D, 16D);
    private static final VoxelShape BASE_4 = Block.box(1D, 9D, 1D, 15D, 10D, 15D);

    private static final VoxelShape PIPE_1 = Block.box(1D, 2D, 1D, 3D, 7D, 3D);
    private static final VoxelShape PIPE_2 = Block.box(13D, 2D, 1D, 15D, 7D, 3D);
    private static final VoxelShape PIPE_3 = Block.box(1D, 2D, 13D, 3D, 7D, 15D);
    private static final VoxelShape PIPE_4 = Block.box(13D, 2D, 13D, 15D, 7D, 15D);

    private static final VoxelShape SHAPE = Shapes.or(BASE_1, BASE_2, BASE_3, BASE_4, PIPE_1, PIPE_2, PIPE_3, PIPE_4);

    public SourceBreederPedestalBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    protected @NotNull MapCodec<SourceBreederPedestalBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public <U extends BlockEntity> BlockEntityTicker<U> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<U> type) {
        return createECTicker(level, type, ECBlockEntityTypes.SOURCE_BREEDER_PEDESTAL, SourceBreederPedestalBlockEntity::tick);
    }

    @Nonnull
    @Override
    @Deprecated
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE;
    }

    @Nonnull
    @Override
    @Deprecated
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
        return onSingleSlotActivated(level, pos, player, hand);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new SourceBreederPedestalBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.WATERLOGGED);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
        return this.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, WaterLoggingHelper.isPlacedInWater(context));
    }

    @Nonnull
    @Override
    @Deprecated
    public FluidState getFluidState(@Nonnull BlockState state) {
        return WaterLoggingHelper.isWaterlogged(state) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

}
