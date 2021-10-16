package sirttas.elementalcraft.block.retriever;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;
import sirttas.elementalcraft.block.sorter.ISorterBlock;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.property.ECProperties;

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

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return worldIn instanceof Level && ((Level) worldIn).isClientSide ? getShape(state, pos, Minecraft.getInstance().hitResult) : getCurentShape(state);
	}

	@Override
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return getCurentShape(state);
	}

	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		return this.moveIO(state, world, pos, hit);
	}

	public static void sendOutputToRetriever(Level world, BlockPos pos, Container inventory, int slot) {
		ItemStack stack = inventory.getItem(slot);

		if (!world.hasNeighborSignal(pos) && !stack.isEmpty()) {
			for (Direction direction : Direction.values()) {
				BlockPos retriverPos = pos.relative(direction);
				BlockState blockState = world.getBlockState(retriverPos);

				if (blockState.getBlock() instanceof RetrieverBlock && blockState.getValue(SOURCE) == direction.getOpposite()) {
					stack = retrive(blockState, world, retriverPos, stack);

					inventory.setItem(slot, stack);
					if (stack.isEmpty()) {
						return;
					}
				}
			}
		}
	}

	public static ItemStack retrive(BlockState state, BlockGetter world, BlockPos pos, ItemStack output) {
		Direction direction = state.getValue(TARGET);

		return ItemHandlerHelper.insertItem(ECContainerHelper.getItemHandlerAt(world, pos.relative(direction), direction.getOpposite()), output, false);
	}

}
