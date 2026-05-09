package sirttas.elementalcraft.block.sorter.ordered;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.block.AbstractECEntityBlock;
import sirttas.elementalcraft.block.cover.CoverType;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.shape.ECShapes;
import sirttas.elementalcraft.block.sorter.ISorterBlock;
import sirttas.elementalcraft.item.ECItems;

public class OrderedSorterBlock extends AbstractECEntityBlock implements ISorterBlock {

	public static final String NAME = "ordered_sorter";
	public static final MapCodec<OrderedSorterBlock> CODEC = simpleCodec(OrderedSorterBlock::new);

	private static final VoxelShape CORE_VOID = Shapes.or(Block.box(5D, 6D, 6D, 11D, 10D, 10D), Block.box(6D, 5D, 6D, 10D, 11D, 10D),
			Block.box(6D, 6D, 5D, 10D, 10D, 11D));
	private static final VoxelShape CORE = Shapes.or(Shapes.join(Block.box(5D, 5D, 5D, 11D, 11D, 11D), CORE_VOID, BooleanOp.ONLY_FIRST),
			Block.box(6D, 6D, 6D, 10D, 10D, 10D));


	public OrderedSorterBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(SOURCE, Direction.SOUTH)
				.setValue(TARGET, Direction.NORTH)
                .setValue(CoverType.PROPERTY, CoverType.NONE));
	}

	@Override
	protected MapCodec<OrderedSorterBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction = context.getClickedFace();
		return this.defaultBlockState().setValue(SOURCE, direction.getOpposite()).setValue(TARGET, direction);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new OrderedSorterBlockEntity(pos, state);
	}

	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createECServerTicker(level, type, ECBlockEntityTypes.SORTER, OrderedSorterBlockEntity::serverTick);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
		container.add(SOURCE, TARGET, CoverType.PROPERTY);
	}

	@Override
	public VoxelShape getCoreShape(BlockState state) {
		return CORE;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return blockGetter instanceof Level level && level.isClientSide() ? getShape(state, pos, Minecraft.getInstance().hitResult) : getCurrentShape(state);
	}
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return getCurrentShape(state);
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.is(ECItems.COVER_FRAME.get()) && !player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

		VoxelShape shape = getShape(state, pos, hit);
        var sorter = BlockEntityHelper.getBlockEntityAs(level, pos, OrderedSorterBlockEntity.class).orElse(null);

        if (sorter == null) {
            return InteractionResult.PASS;
        } else if (CORE.equals(shape)) {
			return sorter.addStack(player.getItemInHand(hand));
		} else if (shape == ECShapes.COVER_FRAME_SHAPE || state.getValue(CoverType.PROPERTY) == CoverType.FRAME) {
            return sorter.putCover(player, hand);
        }
		return this.moveIO(state, level, pos, hit, shape);
	}
}
