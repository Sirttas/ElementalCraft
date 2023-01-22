package sirttas.elementalcraft.block.source.breeder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.block.AbstractECEntityBlock;
import sirttas.elementalcraft.block.WaterLoggingHelper;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.shrine.AbstractPylonShrineBlock;
import sirttas.elementalcraft.item.ECItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SourceBreederBlock extends AbstractECEntityBlock implements SimpleWaterloggedBlock {

    public static final String NAME = "source_breeder";

    private static final VoxelShape BASE_LOWER_1 = Block.box(0D, 0D, 0D, 16D, 3D, 16D);
    private static final VoxelShape BASE_LOWER_2 = Block.box(2D, 3D, 2D, 14D, 8D, 14D);

    private static final VoxelShape PIPE_LOWER_1 = Block.box(1D, 3D, 1D, 3D, 10D, 3D);
    private static final VoxelShape PIPE_LOWER_2 = Block.box(13D, 3D, 1D, 15D, 10D, 3D);
    private static final VoxelShape PIPE_LOWER_3 = Block.box(1D, 3D, 13D, 3D, 10D, 15D);
    private static final VoxelShape PIPE_LOWER_4 = Block.box(13D, 3D, 13D, 15D, 10D, 15D);

    private static final VoxelShape PIPE_LOWER_5 = Block.box(4D, 8D, 4D, 6D, 16D, 6D);
    private static final VoxelShape PIPE_LOWER_6 = Block.box(10D, 8D, 4D, 12D, 16D, 6D);
    private static final VoxelShape PIPE_LOWER_7 = Block.box(4D, 8D, 10D, 6D, 16D, 12D);
    private static final VoxelShape PIPE_LOWER_8 = Block.box(10D, 8D, 10D, 12D, 16D, 12D);

    private static final VoxelShape BASE_UPPER_1 = Block.box(3D, 0D, 3D, 13D, 2D, 13D);
    private static final VoxelShape BASE_UPPER_2 = Block.box(5D, 8D, 5D, 11D, 14D, 11D);

    private static final VoxelShape PIPE_UPPER_1 = Block.box(4D, 2D, 4D, 6D, 16D, 6D);
    private static final VoxelShape PIPE_UPPER_2 = Block.box(10D, 2D, 4D, 12D, 16D, 6D);
    private static final VoxelShape PIPE_UPPER_3 = Block.box(4D, 2D, 10D, 6D, 16D, 12D);
    private static final VoxelShape PIPE_UPPER_4 = Block.box(10D, 2D, 10D, 12D, 16D, 12D);

    private static final VoxelShape SHAPE_LOWER = Shapes.or(BASE_LOWER_1, BASE_LOWER_2, PIPE_LOWER_1, PIPE_LOWER_2, PIPE_LOWER_3, PIPE_LOWER_4, PIPE_LOWER_5, PIPE_LOWER_6, PIPE_LOWER_7, PIPE_LOWER_8);
    private static final VoxelShape SHAPE_UPPER = Shapes.or(BASE_UPPER_1, BASE_UPPER_2, PIPE_UPPER_1, PIPE_UPPER_2, PIPE_UPPER_3, PIPE_UPPER_4);


    public SourceBreederBlock() {
        this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER).setValue(BlockStateProperties.WATERLOGGED, false));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        if (state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
            return new SourceBreederBlockEntity(pos, state);
        }
        return null;
    }

    @Override
    @Nullable
    public <U extends BlockEntity> BlockEntityTicker<U> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<U> type) {
        return createECTicker(level, type, ECBlockEntityTypes.SOURCE_BREEDER, SourceBreederBlockEntity::tick);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, @Nonnull ItemStack stack) {
        level.setBlock(pos.above(), state.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    public void playerWillDestroy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Player player) {
        AbstractPylonShrineBlock.doubleHalfHarvest(level, pos, state, player);
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    @Nonnull
    @Deprecated
    public BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull LevelAccessor level, @Nonnull BlockPos pos, @Nonnull BlockPos facingPos) {
        return AbstractPylonShrineBlock.doubleHalfUpdateShape(state, facing, facingState, level, pos, () -> super.updateShape(state, facing, facingState, level, pos, facingPos));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.WATERLOGGED, BlockStateProperties.DOUBLE_BLOCK_HALF);
    }

    @Override
    @Deprecated
    public boolean canSurvive(BlockState state, @Nonnull LevelReader level, @Nonnull BlockPos pos) {
        return state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER || level.getBlockState(pos.below()).is(this);
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

    @Nonnull
    @Override
    @Deprecated
    public VoxelShape getShape(BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? SHAPE_LOWER : SHAPE_UPPER;
    }

    @Nonnull
    @Override
    @Deprecated
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
        var breeder = (SourceBreederBlockEntity) level.getBlockEntity(pos);
        var heldItem = player.getItemInHand(hand);

        if (breeder != null && breeder.getProgress() < 0 && heldItem.is(ECItems.PURE_CRYSTAL.get())) {
            breeder.start();
            if (!player.isCreative()) {
                heldItem.shrink(1);
                if (heldItem.isEmpty()) {
                    player.setItemInHand(hand, ItemStack.EMPTY);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
