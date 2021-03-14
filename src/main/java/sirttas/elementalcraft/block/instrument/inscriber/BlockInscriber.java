package sirttas.elementalcraft.block.instrument.inscriber;

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
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import sirttas.elementalcraft.block.AbstractBlockECContainer;
import sirttas.elementalcraft.block.tile.TileEntityHelper;
import sirttas.elementalcraft.inventory.ECInventoryHelper;
import sirttas.elementalcraft.item.ECItems;

public class BlockInscriber extends AbstractBlockECContainer {

	public static final String NAME = "inscriber";

	private static final VoxelShape BASE_1 = Block.makeCuboidShape(0D, 1D, 0D, 16D, 2D, 16D);

	private static final VoxelShape CONNECTION = Block.makeCuboidShape(6D, 0D, 6D, 10D, 1D, 10D);

	private static final VoxelShape BASE_PIPE_1 = Block.makeCuboidShape(1D, 0D, 1D, 3D, 3D, 3D);
	private static final VoxelShape BACK_BASE_PIPE_1 = Block.makeCuboidShape(1D, 4D, 1D, 3D, 5D, 3D);
	private static final VoxelShape BASE_PIPE_2 = Block.makeCuboidShape(13D, 0D, 1D, 15D, 3D, 3D);
	private static final VoxelShape BACK_BASE_PIPE_2 = Block.makeCuboidShape(13D, 4D, 1D, 15D, 5D, 3D);
	private static final VoxelShape BASE_PIPE_3 = Block.makeCuboidShape(1D, 0D, 13D, 3D, 3D, 15D);
	private static final VoxelShape BACK_BASE_PIPE_3 = Block.makeCuboidShape(1D, 4D, 13D, 3D, 5D, 15D);
	private static final VoxelShape BASE_PIPE_4 = Block.makeCuboidShape(13D, 0D, 13D, 15D, 3D, 15D);
	private static final VoxelShape BACK_BASE_PIPE_4 = Block.makeCuboidShape(13D, 4D, 13D, 15D, 5D, 15D);
	private static final VoxelShape BASE_SHAPE = VoxelShapes.or(BASE_1, CONNECTION, BASE_PIPE_1, BASE_PIPE_2, BASE_PIPE_3, BASE_PIPE_4);

	private static final VoxelShape BACK_BASE_NORTH = Block.makeCuboidShape(0D, 3D, 12D, 16D, 4D, 16D);
	private static final VoxelShape BACK_PIPE_1_NORTH = Block.makeCuboidShape(4D, 2D, 13D, 6D, 15D, 15D);
	private static final VoxelShape BACK_PIPE_2_NORTH = Block.makeCuboidShape(10D, 2D, 13D, 12D, 15D, 15D);
	private static final VoxelShape BACK_NORTH = VoxelShapes.or(BACK_BASE_NORTH, BACK_BASE_PIPE_3, BACK_BASE_PIPE_4, BACK_PIPE_1_NORTH, BACK_PIPE_2_NORTH);

	private static final VoxelShape PLATE_1_NORTH = Block.makeCuboidShape(1D, 2D, 8D, 15D, 5D, 11D);
	private static final VoxelShape PLATE_2_NORTH = Block.makeCuboidShape(1D, 5D, 9D, 15D, 8D, 12D);
	private static final VoxelShape PLATE_3_NORTH = Block.makeCuboidShape(1D, 8D, 10D, 15D, 11D, 13D);
	private static final VoxelShape PLATE_4_NORTH = Block.makeCuboidShape(1D, 11D, 11D, 15D, 14D, 14D);
	private static final VoxelShape PLATE_5_NORTH = Block.makeCuboidShape(1D, 14D, 12D, 15D, 17D, 15D);
	private static final VoxelShape PLATE_NORTH = VoxelShapes.or(PLATE_1_NORTH, PLATE_2_NORTH, PLATE_3_NORTH, PLATE_4_NORTH, PLATE_5_NORTH);

	private static final VoxelShape BACK_BASE_SOUTH = Block.makeCuboidShape(0D, 3D, 0D, 16D, 4D, 4D);
	private static final VoxelShape BACK_PIPE_1_SOUTH = Block.makeCuboidShape(4D, 2D, 1D, 6D, 15D, 3D);
	private static final VoxelShape BACK_PIPE_2_SOUTH = Block.makeCuboidShape(10D, 2D, 1D, 12D, 15D, 3D);
	private static final VoxelShape BACK_SOUTH = VoxelShapes.or(BACK_BASE_SOUTH, BACK_BASE_PIPE_1, BACK_BASE_PIPE_2, BACK_PIPE_1_SOUTH, BACK_PIPE_2_SOUTH);

	private static final VoxelShape PLATE_1_SOUTH = Block.makeCuboidShape(1D, 2D, 5D, 15D, 5D, 8D);
	private static final VoxelShape PLATE_2_SOUTH = Block.makeCuboidShape(1D, 5D, 4D, 15D, 8D, 7D);
	private static final VoxelShape PLATE_3_SOUTH = Block.makeCuboidShape(1D, 8D, 3D, 15D, 11D, 6D);
	private static final VoxelShape PLATE_4_SOUTH = Block.makeCuboidShape(1D, 11D, 2D, 15D, 14D, 5D);
	private static final VoxelShape PLATE_5_SOUTH = Block.makeCuboidShape(1D, 14D, 1D, 15D, 17D, 4D);
	private static final VoxelShape PLATE_SOUTH = VoxelShapes.or(PLATE_1_SOUTH, PLATE_2_SOUTH, PLATE_3_SOUTH, PLATE_4_SOUTH, PLATE_5_SOUTH);

	private static final VoxelShape BACK_BASE_WEST = Block.makeCuboidShape(12D, 3D, 0D, 16D, 4D, 16D);
	private static final VoxelShape BACK_PIPE_1_WEST = Block.makeCuboidShape(13D, 2D, 4D, 15D, 15D, 6D);
	private static final VoxelShape BACK_PIPE_2_WEST = Block.makeCuboidShape(13D, 2D, 10D, 15D, 15D, 12D);
	private static final VoxelShape BACK_WEST = VoxelShapes.or(BACK_BASE_WEST, BACK_BASE_PIPE_2, BACK_BASE_PIPE_4, BACK_PIPE_1_WEST, BACK_PIPE_2_WEST);

	private static final VoxelShape PLATE_1_WEST = Block.makeCuboidShape(8D, 2D, 1D, 11D, 5D, 15D);
	private static final VoxelShape PLATE_2_WEST = Block.makeCuboidShape(9D, 5D, 1D, 12D, 8D, 15D);
	private static final VoxelShape PLATE_3_WEST = Block.makeCuboidShape(10D, 8D, 1D, 13D, 11D, 15D);
	private static final VoxelShape PLATE_4_WEST = Block.makeCuboidShape(11D, 11D, 1D, 14D, 14D, 15D);
	private static final VoxelShape PLATE_5_WEST = Block.makeCuboidShape(12D, 14D, 1D, 15D, 17D, 15D);
	private static final VoxelShape PLATE_WEST = VoxelShapes.or(PLATE_1_WEST, PLATE_2_WEST, PLATE_3_WEST, PLATE_4_WEST, PLATE_5_WEST);

	private static final VoxelShape BACK_BASE_EAST = Block.makeCuboidShape(0D, 3D, 0D, 4D, 4D, 16D);
	private static final VoxelShape BACK_PIPE_1_EAST = Block.makeCuboidShape(1D, 2D, 4D, 3D, 15D, 6D);
	private static final VoxelShape BACK_PIPE_2_EAST = Block.makeCuboidShape(1D, 2D, 10D, 3D, 15D, 12D);
	private static final VoxelShape BACK_EAST = VoxelShapes.or(BACK_BASE_EAST, BACK_BASE_PIPE_1, BACK_BASE_PIPE_3, BACK_PIPE_1_EAST, BACK_PIPE_2_EAST);

	private static final VoxelShape PLATE_1_EAST = Block.makeCuboidShape(5D, 2D, 1D, 8D, 5D, 15D);
	private static final VoxelShape PLATE_2_EAST = Block.makeCuboidShape(4D, 5D, 1D, 7D, 8D, 15D);
	private static final VoxelShape PLATE_3_EAST = Block.makeCuboidShape(3D, 8D, 1D, 6D, 6D, 15D);
	private static final VoxelShape PLATE_4_EAST = Block.makeCuboidShape(2D, 11D, 1D, 5D, 14D, 15D);
	private static final VoxelShape PLATE_5_EAST = Block.makeCuboidShape(1D, 14D, 1D, 4D, 17D, 15D);
	private static final VoxelShape PLATE_EAST = VoxelShapes.or(PLATE_1_EAST, PLATE_2_EAST, PLATE_3_EAST, PLATE_4_EAST, PLATE_5_EAST);

	private static final VoxelShape NORTH_SHAPE = VoxelShapes.or(BASE_SHAPE, BACK_NORTH, PLATE_NORTH);
	private static final VoxelShape SOUTH_SHAPE = VoxelShapes.or(BASE_SHAPE, BACK_SOUTH, PLATE_SOUTH);
	private static final VoxelShape WEST_SHAPE = VoxelShapes.or(BASE_SHAPE, BACK_WEST, PLATE_WEST);
	private static final VoxelShape EAST_SHAPE = VoxelShapes.or(BASE_SHAPE, BACK_EAST, PLATE_EAST);

	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

	public BlockInscriber() {
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileInscriber();
	}

	@Override
	@Deprecated
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		final TileInscriber inscriber = (TileInscriber) world.getTileEntity(pos);
		ItemStack heldItem = player.getHeldItem(hand);
		IItemHandler inv = ECInventoryHelper.getItemHandlerAt(world, pos, null);

		if (inscriber != null && (hand == Hand.MAIN_HAND || !heldItem.isEmpty())) {
			if (heldItem.getItem() == ECItems.CHISEL && !inscriber.isLocked()) {
				return makeProgress(player, hand, inscriber, heldItem);
			} else if ((inscriber.isLocked() || heldItem.isEmpty() || player.isSneaking()) && !inscriber.getInventory().isEmpty()) {
				for (int i = 0; i < inv.getSlots(); i++) {
					this.onSlotActivated(inv, player, ItemStack.EMPTY, i);
				}
				return ActionResultType.SUCCESS;
			}
			for (int i = 0; i < inv.getSlots(); i++) {
				if (inv.getStackInSlot(i).isEmpty() && this.onSlotActivated(inv, player, heldItem, i).isSuccess()) {
					return ActionResultType.SUCCESS;
				}
			}
		}
		return ActionResultType.PASS;
	}

	private ActionResultType makeProgress(PlayerEntity player, Hand hand, TileInscriber inscriber, ItemStack heldItem) {
		if (inscriber.makeProgress()) {
			if (heldItem.isDamageable() && !player.isCreative()) {
				heldItem.damageItem(1, player, p -> p.sendBreakAnimation(hand));
			}
			return ActionResultType.SUCCESS;
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
			return BASE_SHAPE;
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

	@SuppressWarnings("deprecation")
	@Override
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
