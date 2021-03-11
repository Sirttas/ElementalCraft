package sirttas.elementalcraft.block.instrument.crystallizer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
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
import sirttas.elementalcraft.block.AbstractBlockECContainer;
import sirttas.elementalcraft.block.tile.TileEntityHelper;
import sirttas.elementalcraft.inventory.ECInventoryHelper;

public class BlockCrystallizer extends AbstractBlockECContainer {

	public static final String NAME = "crystallizer";

	private static final VoxelShape BASE_1 = Block.makeCuboidShape(0D, 1D, 0D, 16D, 3D, 16D);

	private static final VoxelShape CONNECTION = Block.makeCuboidShape(6D, 0D, 6D, 10D, 1D, 10D);

	private static final VoxelShape BASE_PIPE_1 = Block.makeCuboidShape(1D, 0D, 1D, 3D, 5D, 3D);
	private static final VoxelShape BASE_PIPE_2 = Block.makeCuboidShape(13D, 0D, 1D, 15D, 5D, 3D);
	private static final VoxelShape BASE_PIPE_3 = Block.makeCuboidShape(1D, 0D, 13D, 3D, 5D, 15D);
	private static final VoxelShape BASE_PIPE_4 = Block.makeCuboidShape(13D, 0D, 13D, 15D, 5D, 15D);

	private static final VoxelShape TOP_1 = Block.makeCuboidShape(4D, 3D, 4D, 12D, 12D, 12D);
	private static final VoxelShape TOP_2 = Block.makeCuboidShape(3D, 9D, 3D, 13D, 10D, 13D);
	private static final VoxelShape HOLE_1 = Block.makeCuboidShape(6D, 3D, 4D, 10D, 9D, 12D);
	private static final VoxelShape HOLE_2 = Block.makeCuboidShape(4D, 3D, 6D, 12D, 9D, 10D);
	private static final VoxelShape TOP = VoxelShapes.combineAndSimplify(VoxelShapes.or(TOP_1, TOP_2), VoxelShapes.or(HOLE_1, HOLE_2), IBooleanFunction.ONLY_FIRST);

	private static final VoxelShape SHAPE = VoxelShapes.or(BASE_1, CONNECTION, BASE_PIPE_1, BASE_PIPE_2, BASE_PIPE_3, BASE_PIPE_4, TOP);

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileCrystallizer();
	}

	@Override
	@Deprecated
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		final TileCrystallizer crystallizer = (TileCrystallizer) world.getTileEntity(pos);
		ItemStack heldItem = player.getHeldItem(hand);
		IItemHandler inv = ECInventoryHelper.getItemHandlerAt(world, pos, null);

		if (crystallizer != null && (hand == Hand.MAIN_HAND || !heldItem.isEmpty())) {
			if ((crystallizer.isLocked() || heldItem.isEmpty() || player.isSneaking()) && !crystallizer.getInventory().isEmpty()) {
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

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
	
	@Override
	@Deprecated
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		return TileEntityHelper.isValidContainer(state.getBlock(), world, pos.down());
	}
}
