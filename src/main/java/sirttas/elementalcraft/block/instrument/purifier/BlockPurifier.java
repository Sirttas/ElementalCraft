package sirttas.elementalcraft.block.instrument.purifier;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.AbstractBlockECContainer;
import sirttas.elementalcraft.block.tile.TileEntityHelper;
import sirttas.elementalcraft.inventory.ECInventoryHelper;

public class BlockPurifier extends AbstractBlockECContainer {

	public static final String NAME = "purifier";

	private static final VoxelShape OVEN_SLAB = Block.makeCuboidShape(0D, 2D, 0D, 16D, 4D, 16D);
	private static final VoxelShape OVEN_SLAB_2 = Block.makeCuboidShape(0D, 10D, 0D, 16D, 12D, 16D);
	private static final VoxelShape CONNECTION = Block.makeCuboidShape(6D, 0D, 6D, 10D, 2D, 10D);
	private static final VoxelShape PILLAT_1 = Block.makeCuboidShape(1D, 0D, 1D, 3D, 10D, 3D);
	private static final VoxelShape PILLAT_2 = Block.makeCuboidShape(13D, 0D, 1D, 15D, 10D, 3D);
	private static final VoxelShape PILLAT_3 = Block.makeCuboidShape(1D, 0D, 13D, 3D, 10D, 15D);
	private static final VoxelShape PILLAT_4 = Block.makeCuboidShape(13D, 0D, 13D, 15D, 10D, 15D);
	private static final VoxelShape MAIN_SHAPE = VoxelShapes.or(OVEN_SLAB, OVEN_SLAB_2, CONNECTION, PILLAT_1, PILLAT_2, PILLAT_3, PILLAT_4);

	private static final VoxelShape NORTH_EMPTY_SPACE = Block.makeCuboidShape(6D, 4D, 0D, 10D, 8D, 4D);
	private static final VoxelShape NORTH_OVEN_BLOCK = Block.makeCuboidShape(4D, 4D, 0D, 12D, 10D, 7D);
	private static final VoxelShape NORTH_OVEN_BLOCK_2 = Block.makeCuboidShape(6D, 4D, 7D, 10D, 8D, 11D);
	private static final VoxelShape NORTH_OVEN = VoxelShapes.combineAndSimplify(VoxelShapes.or(NORTH_OVEN_BLOCK, NORTH_OVEN_BLOCK_2), NORTH_EMPTY_SPACE, IBooleanFunction.ONLY_FIRST);

	private static final VoxelShape SOUTH_EMPTY_SPACE = Block.makeCuboidShape(6D, 4D, 12D, 10D, 8D, 16D);
	private static final VoxelShape SOUTH_OVEN_BLOCK = Block.makeCuboidShape(4D, 4D, 9D, 12D, 10D, 16D);
	private static final VoxelShape SOUTH_OVEN_BLOCK_2 = Block.makeCuboidShape(6D, 4D, 5D, 10D, 8D, 9D);
	private static final VoxelShape SOUTH_OVEN = VoxelShapes.combineAndSimplify(VoxelShapes.or(SOUTH_OVEN_BLOCK, SOUTH_OVEN_BLOCK_2), SOUTH_EMPTY_SPACE, IBooleanFunction.ONLY_FIRST);

	private static final VoxelShape WEST_EMPTY_SPACE = Block.makeCuboidShape(0D, 4D, 6D, 4D, 8D, 10D);
	private static final VoxelShape WEST_OVEN_BLOCK = Block.makeCuboidShape(0D, 4D, 4D, 7D, 10D, 12D);
	private static final VoxelShape WEST_OVEN_BLOCK_2 = Block.makeCuboidShape(7D, 4D, 6D, 11D, 8D, 10D);
	private static final VoxelShape WEST_OVEN = VoxelShapes.combineAndSimplify(VoxelShapes.or(WEST_OVEN_BLOCK, WEST_OVEN_BLOCK_2), WEST_EMPTY_SPACE, IBooleanFunction.ONLY_FIRST);

	private static final VoxelShape EAST_EMPTY_SPACE = Block.makeCuboidShape(12D, 4D, 6D, 16D, 8D, 10D);
	private static final VoxelShape EAST_OVEN_BLOCK = Block.makeCuboidShape(9D, 4D, 4D, 16D, 10D, 12D);
	private static final VoxelShape EAST_OVEN_BLOCK_2 = Block.makeCuboidShape(5D, 4D, 6D, 9D, 8D, 10D);
	private static final VoxelShape EAST_OVEN = VoxelShapes.combineAndSimplify(VoxelShapes.or(EAST_OVEN_BLOCK, EAST_OVEN_BLOCK_2), EAST_EMPTY_SPACE, IBooleanFunction.ONLY_FIRST);

	private static final VoxelShape NORTH_SHAPE = VoxelShapes.or(MAIN_SHAPE, NORTH_OVEN);
	private static final VoxelShape SOUTH_SHAPE = VoxelShapes.or(MAIN_SHAPE, SOUTH_OVEN);
	private static final VoxelShape EAST_SHAPE = VoxelShapes.or(MAIN_SHAPE, EAST_OVEN);
	private static final VoxelShape WEST_SHAPE = VoxelShapes.or(MAIN_SHAPE, WEST_OVEN);

	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

	public BlockPurifier() {
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TilePurifier();
	}

	@Override
	@Deprecated
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		final TilePurifier purifier = (TilePurifier) world.getTileEntity(pos);
		IItemHandler inv = ECInventoryHelper.getItemHandlerAt(world, pos, null);
		ItemStack heldItem = player.getHeldItem(hand);

		if (purifier != null && (hand == Hand.MAIN_HAND || !heldItem.isEmpty())) {
			if (!purifier.getInventory().getStackInSlot(1).isEmpty()) {
				return this.onSlotActivated(inv, player, ItemStack.EMPTY, 1);
			} else if (heldItem.isEmpty() || ElementalCraft.PURE_ORE_MANAGER.isValidOre(heldItem)) {
				return this.onSlotActivated(inv, player, heldItem, 0);
			}
		}
		return ActionResultType.PASS;
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch (state.get(FACING)) {
		case NORTH:
			return NORTH_SHAPE;
		case SOUTH:
			return SOUTH_SHAPE;
		case WEST:
			return WEST_SHAPE;
		case EAST:
			return EAST_SHAPE;
		default:
			return MAIN_SHAPE;
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	@Deprecated
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	@Override
	@Deprecated
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	@Override
	@Deprecated
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		return TileEntityHelper.isValidContainer(state.getBlock(), world, pos.down());
	}
}
