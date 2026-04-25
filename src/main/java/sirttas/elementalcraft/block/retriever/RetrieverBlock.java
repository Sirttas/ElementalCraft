package sirttas.elementalcraft.block.retriever;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.AbstractECEntityBlock;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.cover.CoverType;
import sirttas.elementalcraft.block.cover.CoverableBlockEntity;
import sirttas.elementalcraft.block.sorter.ISorterBlock;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.item.ECItems;

import javax.annotation.Nonnull;

public class RetrieverBlock extends AbstractECEntityBlock implements ISorterBlock {

	public static final String NAME = "instrument_retriever";
	public static final MapCodec<RetrieverBlock> CODEC = simpleCodec(RetrieverBlock::new);

	private static final VoxelShape CORE = Block.box(5D, 5D, 5D, 11D, 11D, 11D);

	public RetrieverBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(SOURCE, Direction.SOUTH)
				.setValue(TARGET, Direction.NORTH)
                .setValue(CoverType.PROPERTY, CoverType.NONE));
	}

	@Override
	protected @NotNull MapCodec<RetrieverBlock> codec() {
		return CODEC;
	}

    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new CoverableBlockEntity(pos, state);
    }

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction = context.getClickedFace();
		return this.defaultBlockState().setValue(SOURCE, direction.getOpposite()).setValue(TARGET, direction);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
		container.add(SOURCE, TARGET, CoverType.PROPERTY);
	}

	@Override
	public VoxelShape getCoreShape(BlockState state) {
		return CORE;
	}

	@Nonnull
    @Override
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter blockGetter, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return blockGetter instanceof Level level && level.isClientSide() ? getShape(state, pos, Minecraft.getInstance().hitResult) : getCurrentShape(state);
	}

	@Nonnull
    @Override
	public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return getCurrentShape(state);
	}

	@Nonnull
    @Override
	protected InteractionResult useItemOn(@Nonnull ItemStack stack, @Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
        if (stack.is(ECItems.COVER_FRAME.get()) && !player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }
		return this.moveIO(state, level, pos, hit);
	}

	public static void sendOutputToRetriever(Level level, BlockPos pos, Container inventory, int slot) {
		var stack = inventory.getItem(slot);

		if (level.hasNeighborSignal(pos) || stack.isEmpty()) {
			return;
		}

		for (Direction direction : Direction.values()) {
			var retrieverPos = pos.relative(direction);
			var blockState = level.getBlockState(retrieverPos);

			if (blockState.is(ECBlocks.RETRIEVER.get()) && blockState.getValue(SOURCE) == direction.getOpposite() && !level.hasNeighborSignal(retrieverPos)) {
				stack = retrieve(blockState, level, retrieverPos, stack);

				inventory.setItem(slot, stack);
				if (stack.isEmpty()) {
					return;
				}
			}
		}
	}

	public static ItemStack retrieve(BlockState state, BlockGetter world, BlockPos pos, ItemStack output) {
		Direction direction = state.getValue(TARGET);

		return ItemUtil.insertItemReturnRemaining(ECContainerHelper.getItemResourceHandlerAt(world, pos.relative(direction), direction.getOpposite()), output, false, null);
	}

}
