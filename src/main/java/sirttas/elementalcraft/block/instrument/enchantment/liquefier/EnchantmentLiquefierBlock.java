package sirttas.elementalcraft.block.instrument.enchantment.liquefier;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.block.AbstractECContainerBlock;
import sirttas.elementalcraft.block.WaterLoggingHelper;
import sirttas.elementalcraft.block.container.ElementContainer;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.IInstrumentBlock;
import sirttas.elementalcraft.block.instrument.binder.BinderBlock;
import sirttas.elementalcraft.block.shrine.AbstractPylonShrineBlock;
import sirttas.elementalcraft.container.ECContainerHelper;

import javax.annotation.Nonnull;

public class EnchantmentLiquefierBlock extends AbstractECContainerBlock implements IInstrumentBlock {

    public static final String NAME = "enchantment_liquefier";
    public static final MapCodec<EnchantmentLiquefierBlock> CODEC = simpleCodec(EnchantmentLiquefierBlock::new);

    private static final VoxelShape SLAB = Block.box(2D, 2D, 2D, 14D, 4D, 14D);
    private static final VoxelShape SLAB_2 = Block.box(0D, 4D, 0D, 16D, 9D, 16D);
    private static final VoxelShape CONNECTION = Block.box(6D, 0D, 6D, 10D, 2D, 10D);
    private static final VoxelShape PILLAR_1 = Block.box(1D, 0D, 1D, 3D, 16D, 3D);
    private static final VoxelShape PILLAR_2 = Block.box(13D, 0D, 1D, 15D, 16D, 3D);
    private static final VoxelShape PILLAR_3 = Block.box(1D, 0D, 13D, 3D, 16D, 15D);
    private static final VoxelShape PILLAR_4 = Block.box(13D, 0D, 13D, 15D, 16D, 15D);
    protected static final VoxelShape SHAPE_LOWER = Shapes.or(SLAB, SLAB_2, CONNECTION, PILLAR_1, PILLAR_2, PILLAR_3, PILLAR_4);
    
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public EnchantmentLiquefierBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected @NotNull MapCodec<EnchantmentLiquefierBlock> codec() {
        return CODEC;
    }

    protected boolean isLower(BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.LOWER;
    }

    @Nonnull
    @Override
    protected InteractionResult useItemOn(@Nonnull ItemStack stack, @Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
        if (!isLower(state)) {
            pos = pos.below();
        }

        var enchantmentLiquefier = (EnchantmentLiquefierBlockEntity) level.getBlockEntity(pos);
        var heldItem = player.getItemInHand(hand);
        var inv = ECContainerHelper.getItemResourceHandlerAt(level, pos, null);

        if (enchantmentLiquefier != null && hand == InteractionHand.MAIN_HAND) {
            if ((enchantmentLiquefier.isLocked() || heldItem.isEmpty() || player.isShiftKeyDown()) && !enchantmentLiquefier.getInventory().isEmpty()) {
                for (int i = 0; i < inv.size(); i++) {
                    this.onSlotActivated(inv, player, ItemStack.EMPTY, i);
                }
                return InteractionResult.SUCCESS;
            }
            for (int i = 0; i < inv.size(); i++) {
                if (inv.getResource(i).isEmpty() && this.onSlotActivated(inv, player, heldItem, i) == InteractionResult.SUCCESS) {
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return isLower(state) ? new EnchantmentLiquefierBlockEntity(pos, state) : null;
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
        return isLower(state) ? createInstrumentTicker(level, type, ECBlockEntityTypes.ENCHANTMENT_LIQUEFIER) : null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, @Nonnull ItemStack stack) {
        level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    public @NotNull BlockState playerWillDestroy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Player player) {
        AbstractPylonShrineBlock.doubleHalfHarvest(level, pos, state, player);
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    @Nonnull
    protected BlockState updateShape(@NotNull BlockState state, @NotNull LevelReader level, @NotNull ScheduledTickAccess ticks, @NotNull BlockPos pos, @NotNull Direction directionToNeighbour, @NotNull BlockPos neighbourPos, @NotNull BlockState neighbourState, @NotNull RandomSource random) {
        return AbstractPylonShrineBlock.doubleHalfUpdateShape(state, directionToNeighbour, neighbourState, level, pos, () -> {
            WaterLoggingHelper.scheduleWaterTick(state, level, ticks, pos);
            return !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
        });
    }

    @Override
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
        if (!AbstractPylonShrineBlock.canReplaceAboveBlock(context)) {
            return null;
        }
        return this.defaultBlockState().setValue(WATERLOGGED, WaterLoggingHelper.isPlacedInWater(context));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, HALF);
    }

    @Override
    public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader level, BlockPos pos) {
        var below = pos.below();

        return (isLower(state) && ElementContainer.isValidContainer(state, level, below)) || level.getBlockState(below).is(this);
    }

    @Nonnull
    @Override
    public FluidState getFluidState(@Nonnull BlockState state) {
        return WaterLoggingHelper.isWaterlogged(state) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return isLower(state) ? SHAPE_LOWER : BinderBlock.SHAPE;
    }
}
