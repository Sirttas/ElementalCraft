package sirttas.elementalcraft.block.retriever;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import sirttas.elementalcraft.block.shape.ShapeHelper;
import sirttas.elementalcraft.inventory.ECInventoryHelper;
import sirttas.elementalcraft.property.ECProperties;

public class BlockRetriever extends Block {

	public static final String NAME = "instrument_retriever";

	private static final VoxelShape CORE = Block.makeCuboidShape(5D, 5D, 5D, 11D, 11D, 11D);

	private static final VoxelShape SOURCE_NORTH = VoxelShapes.or(Block.makeCuboidShape(4D, 4D, 0D, 12D, 12D, 2D), Block.makeCuboidShape(6D, 6D, 2D, 10D, 10D, 5D));
	private static final VoxelShape SOURCE_SOUTH = VoxelShapes.or(Block.makeCuboidShape(4D, 4D, 14D, 12D, 12D, 16D), Block.makeCuboidShape(6D, 6D, 11D, 10D, 10D, 14D));
	private static final VoxelShape SOURCE_WEST = VoxelShapes.or(Block.makeCuboidShape(0D, 4D, 4D, 2D, 12D, 12D), Block.makeCuboidShape(2D, 6D, 6D, 5D, 10D, 10D));
	private static final VoxelShape SOURCE_EAST = VoxelShapes.or(Block.makeCuboidShape(14D, 4D, 4D, 16D, 12D, 12D), Block.makeCuboidShape(11D, 6D, 6D, 14D, 10D, 10D));
	private static final VoxelShape SOURCE_DOWN = VoxelShapes.or(Block.makeCuboidShape(4D, 0D, 4D, 12D, 2D, 12D), Block.makeCuboidShape(6D, 2D, 6D, 10D, 5D, 10D));
	private static final VoxelShape SOURCE_UP = VoxelShapes.or(Block.makeCuboidShape(4D, 14D, 4D, 12D, 16D, 12D), Block.makeCuboidShape(6D, 11D, 6D, 10D, 14D, 10D));

	private static final VoxelShape TARGET_NORTH = VoxelShapes.or(Block.makeCuboidShape(7D, 7D, 0D, 9D, 9D, 3D), Block.makeCuboidShape(6D, 6D, 3D, 10D, 10D, 5D));
	private static final VoxelShape TARGET_SOUTH = VoxelShapes.or(Block.makeCuboidShape(7D, 7D, 13D, 9D, 9D, 16D), Block.makeCuboidShape(6D, 6D, 11D, 10D, 10D, 13D));
	private static final VoxelShape TARGET_WEST = VoxelShapes.or(Block.makeCuboidShape(0D, 7D, 7D, 3D, 9D, 9D), Block.makeCuboidShape(3D, 6D, 6D, 5D, 10D, 10D));
	private static final VoxelShape TARGET_EAST = VoxelShapes.or(Block.makeCuboidShape(13D, 7D, 7D, 16D, 9D, 9D), Block.makeCuboidShape(11D, 6D, 6D, 13D, 10D, 10D));
	private static final VoxelShape TARGET_DOWN = VoxelShapes.or(Block.makeCuboidShape(7D, 0D, 7D, 9D, 3D, 9D), Block.makeCuboidShape(6D, 3D, 6D, 10D, 5D, 10D));
	private static final VoxelShape TARGET_UP = VoxelShapes.or(Block.makeCuboidShape(7D, 13D, 7D, 9D, 16D, 9D), Block.makeCuboidShape(6D, 11D, 6D, 10D, 13D, 10D));

	private static final List<VoxelShape> SOURCE_SHAPES = ImmutableList.of(SOURCE_NORTH, SOURCE_SOUTH, SOURCE_WEST, SOURCE_EAST, SOURCE_DOWN);
	private static final List<VoxelShape> TARGET_SHAPES = ImmutableList.of(TARGET_NORTH, TARGET_SOUTH, TARGET_WEST, TARGET_EAST, TARGET_DOWN, TARGET_UP);

	public static final DirectionProperty SOURCE = DirectionProperty.create("source", Direction.values());
	public static final DirectionProperty TARGET = DirectionProperty.create("target", Direction.values());

	public BlockRetriever() {
		super(ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES);
		this.setDefaultState(this.stateContainer.getBaseState().with(SOURCE, Direction.SOUTH).with(TARGET, Direction.NORTH));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction direction = context.getFace();
		return this.getDefaultState().with(SOURCE, direction.getOpposite()).with(TARGET, direction);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
		container.add(SOURCE, TARGET);
	}

	private VoxelShape getSourceShape(BlockState state) {
		switch (state.get(SOURCE)) {
		case EAST:
			return SOURCE_EAST;
		case NORTH:
			return SOURCE_NORTH;
		case SOUTH:
			return SOURCE_SOUTH;
		case WEST:
			return SOURCE_WEST;
		case DOWN:
			return SOURCE_DOWN;
		default:
			return SOURCE_UP;
		}
	}

	private VoxelShape getTargetShape(BlockState state) {
		switch (state.get(TARGET)) {
		case EAST:
			return TARGET_EAST;
		case NORTH:
			return TARGET_NORTH;
		case SOUTH:
			return TARGET_SOUTH;
		case WEST:
			return TARGET_WEST;
		case DOWN:
			return TARGET_DOWN;
		default:
			return TARGET_UP;
		}
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

		if (state.get(SOURCE) == direction || state.get(TARGET) == direction) {
			return ActionResultType.PASS;
		} else if (SOURCE_SHAPES.contains(shape)) {
			world.setBlockState(pos, state.with(SOURCE, direction));
			return ActionResultType.SUCCESS;
		} else if (TARGET_SHAPES.contains(shape)) {
			world.setBlockState(pos, state.with(TARGET, direction));
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	public static void sendOutputToRetriever(World world, BlockPos pos, IInventory inventory, int slot) {
		ItemStack stack = inventory.getStackInSlot(slot);

		if (!world.isBlockPowered(pos) && !stack.isEmpty()) {
			for (Direction direction : Direction.values()) {
				BlockPos retriverPos = pos.offset(direction);
				BlockState blockState = world.getBlockState(retriverPos);

				if (blockState.getBlock() instanceof BlockRetriever && blockState.get(BlockRetriever.SOURCE) == direction.getOpposite()) {
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
