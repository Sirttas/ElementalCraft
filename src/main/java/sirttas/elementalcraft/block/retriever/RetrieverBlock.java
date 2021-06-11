package sirttas.elementalcraft.block.retriever;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import sirttas.elementalcraft.block.sorter.ISorterBlock;
import sirttas.elementalcraft.inventory.ECInventoryHelper;
import sirttas.elementalcraft.property.ECProperties;

public class RetrieverBlock extends Block implements ISorterBlock {

	public static final String NAME = "instrument_retriever";

	private static final VoxelShape CORE = Block.box(5D, 5D, 5D, 11D, 11D, 11D);

	public RetrieverBlock() {
		super(ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES);
		this.registerDefaultState(this.stateDefinition.any().setValue(SOURCE, Direction.SOUTH).setValue(TARGET, Direction.NORTH));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction direction = context.getClickedFace();
		return this.defaultBlockState().setValue(SOURCE, direction.getOpposite()).setValue(TARGET, direction);
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> container) {
		container.add(SOURCE, TARGET);
	}

	@Override
	public VoxelShape getCoreShape(BlockState state) {
		return CORE;
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return worldIn instanceof World && ((World) worldIn).isClientSide ? getShape(state, pos, Minecraft.getInstance().hitResult) : getCurentShape(state);
	}

	@Override
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return getCurentShape(state);
	}

	@Override
	@Deprecated
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		return this.moveIO(state, world, pos, hit);
	}

	public static void sendOutputToRetriever(World world, BlockPos pos, IInventory inventory, int slot) {
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

	public static ItemStack retrive(BlockState state, IBlockReader world, BlockPos pos, ItemStack output) {
		Direction direction = state.getValue(TARGET);

		return ItemHandlerHelper.insertItem(ECInventoryHelper.getItemHandlerAt(world, pos.relative(direction), direction.getOpposite()), output, false);
	}

}
