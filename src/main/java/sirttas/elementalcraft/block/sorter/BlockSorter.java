package sirttas.elementalcraft.block.sorter;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import sirttas.elementalcraft.block.AbstractBlockECTileProvider;
import sirttas.elementalcraft.block.retriever.BlockRetriever;
import sirttas.elementalcraft.block.shape.ShapeHelper;
import sirttas.elementalcraft.block.shape.Shapes;
import sirttas.elementalcraft.block.tile.TileEntityHelper;
import sirttas.elementalcraft.inventory.ECInventoryHelper;

public class BlockSorter extends AbstractBlockECTileProvider {

	public static final String NAME = "sorter";

	private static final VoxelShape CORE_VOID = VoxelShapes.or(Block.makeCuboidShape(5D, 6D, 6D, 11D, 10D, 10D), Block.makeCuboidShape(6D, 5D, 6D, 10D, 11D, 10D),
			Block.makeCuboidShape(6D, 6D, 5D, 10D, 10D, 11D));
	private static final VoxelShape CORE = VoxelShapes.or(VoxelShapes.combineAndSimplify(Block.makeCuboidShape(5D, 5D, 5D, 11D, 11D, 11D), CORE_VOID, IBooleanFunction.ONLY_FIRST),
			Block.makeCuboidShape(6D, 6D, 6D, 10D, 10D, 10D));


	public static final DirectionProperty SOURCE = BlockRetriever.SOURCE;
	public static final DirectionProperty TARGET = BlockRetriever.TARGET;

	public BlockSorter() {
		this.setDefaultState(this.stateContainer.getBaseState().with(SOURCE, Direction.SOUTH).with(TARGET, Direction.NORTH));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction direction = context.getFace();
		return this.getDefaultState().with(SOURCE, direction.getOpposite()).with(TARGET, direction);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileSorter();
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
		container.add(SOURCE, TARGET);
	}

	private VoxelShape getSourceShape(BlockState state) {
		return Shapes.sourceShape(state.get(SOURCE));
	}

	private VoxelShape getTargetShape(BlockState state) {
		return Shapes.sourceShape(state.get(TARGET));
	}

	private VoxelShape getCurentShape(BlockState state) {
		return VoxelShapes.or(getSourceShape(state), getTargetShape(state), CORE).simplify();
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return worldIn instanceof World && ((World) worldIn).isRemote ? getShape(state, pos, Minecraft.getInstance().objectMouseOver) : getCurentShape(state);
	}

	public VoxelShape getShape(BlockState state, BlockPos pos, RayTraceResult result) {
		if (result != null && result.getType() == RayTraceResult.Type.BLOCK && ((BlockRayTraceResult) result).getPos().equals(pos)) {
			final Vector3d hit = result.getHitVec();
			VoxelShape source = getSourceShape(state);
			VoxelShape target = getTargetShape(state);

			if (ShapeHelper.vectorCollideWithShape(source, pos, hit)) {
				return source;
			} else if (ShapeHelper.vectorCollideWithShape(target, pos, hit)) {
				return target;
			} else if (ShapeHelper.vectorCollideWithShape(CORE, pos, hit)) {
				return CORE;
			}
		}
		return getCurentShape(state);
	}

	@Override
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return getCurentShape(state);
	}

	@Override
	@Deprecated
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		VoxelShape shape = getShape(state, pos, hit);
		Direction direction = hit.getFace().getOpposite();

		if (CORE.equals(shape)) {
			return TileEntityHelper.getTileEntityAs(world, pos, TileSorter.class).map(sorter -> sorter.addStack(player.getHeldItem(hand))).orElse(ActionResultType.PASS);
		} else if (state.get(SOURCE) == direction || state.get(TARGET) == direction) {
			return ActionResultType.PASS;
		} else if (Shapes.SOURCE_SHAPES.contains(shape)) {
			world.setBlockState(pos, state.with(SOURCE, direction));
			return ActionResultType.SUCCESS;
		} else if (Shapes.TARGET_SHAPES.contains(shape)) {
			world.setBlockState(pos, state.with(TARGET, direction));
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	public static void sendOutputToRetriever(World world, BlockPos pos, IInventory inventory, int slot) {
		ItemStack stack = inventory.getStackInSlot(slot);

		if (!stack.isEmpty()) {
			for (Direction direction : Direction.values()) {
				BlockPos retriverPos = pos.offset(direction);
				BlockState blockState = world.getBlockState(retriverPos);

				if (blockState.getBlock() instanceof BlockSorter && blockState.get(BlockSorter.SOURCE) == direction.getOpposite()) {
					stack = retrive(blockState, world, retriverPos, stack);

					inventory.setInventorySlotContents(slot, stack);
					if (stack.isEmpty()) {
						return;
					}
				}
			}
		}
	}

	public static ItemStack retrive(BlockState state, IBlockReader world, BlockPos pos, ItemStack output) {
		Direction direction = state.get(TARGET);

		return ItemHandlerHelper.insertItem(ECInventoryHelper.getItemHandlerAt(world, pos.offset(direction), direction.getOpposite()), output, false);
	}

}
