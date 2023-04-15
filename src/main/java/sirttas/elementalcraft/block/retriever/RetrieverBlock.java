package sirttas.elementalcraft.block.retriever;

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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemHandlerHelper;
import sirttas.elementalcraft.block.sorter.ISorterBlock;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.property.ECProperties;

import javax.annotation.Nonnull;

public class RetrieverBlock extends Block implements ISorterBlock {

	public static final String NAME = "instrument_retriever";

	private static final VoxelShape CORE = Block.box(5D, 5D, 5D, 11D, 11D, 11D);

	public RetrieverBlock() {
		super(ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES);
		this.registerDefaultState(this.stateDefinition.any().setValue(SOURCE, Direction.SOUTH).setValue(TARGET, Direction.NORTH));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction = context.getClickedFace();
		return this.defaultBlockState().setValue(SOURCE, direction.getOpposite()).setValue(TARGET, direction);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
		container.add(SOURCE, TARGET);
	}

	@Override
	public VoxelShape getCoreShape(BlockState state) {
		return CORE;
	}

	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return worldIn instanceof Level && ((Level) worldIn).isClientSide ? getShape(state, pos, Minecraft.getInstance().hitResult) : getCurentShape(state);
	}

	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return getCurentShape(state);
	}

	@Nonnull
    @Override
	@Deprecated
	public InteractionResult use(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
		return this.moveIO(state, world, pos, hit);
	}

	public static void sendOutputToRetriever(Level level, BlockPos pos, Container inventory, int slot) {
		var stack = inventory.getItem(slot);

		if (level.hasNeighborSignal(pos) || stack.isEmpty()) {
			return;
		}

		for (Direction direction : Direction.values()) {
			BlockPos retrieverPos = pos.relative(direction);
			BlockState blockState = level.getBlockState(retrieverPos);

			if (blockState.getBlock() instanceof RetrieverBlock && blockState.getValue(SOURCE) == direction.getOpposite()) {
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

		return ItemHandlerHelper.insertItem(ECContainerHelper.getItemHandlerAt(world, pos.relative(direction), direction.getOpposite()), output, false);
	}

}
