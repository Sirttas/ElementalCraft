package sirttas.elementalcraft.block;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import sirttas.elementalcraft.block.tile.IForcableSync;
import sirttas.elementalcraft.property.ECProperties;

public abstract class BlockECContainer extends ContainerBlock implements IBlockECTileProvider {

	public BlockECContainer(Block.Properties properties) {
		super(properties);
	}

	public BlockECContainer() {
		this(ECProperties.Blocks.BLOCK_NOT_SOLID);
	}

	@Override
	public final boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public abstract TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world);

	@Override
	public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return false;
	}

	@Override
	@Deprecated
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public final TileEntity createNewTileEntity(IBlockReader worldIn) {
		return null;
	}

	private ActionResultType onSlotActivatedUnsync(IInventory inventory, PlayerEntity player, ItemStack heldItem, int index) {
		ItemStack stack = inventory.getStackInSlot(index);
		TileEntity entity = (TileEntity) inventory;

		if (heldItem.isEmpty() || (!stack.isEmpty() && !stack.isItemEqual(heldItem))) {
			if (!stack.isEmpty()) {
				if (entity.hasWorld() && !entity.getWorld().isRemote()) {
					ItemEntity invItem = new ItemEntity(entity.getWorld(), player.getPosX(), player.getPosY() + 0.25, player.getPosZ(), inventory.getStackInSlot(index));
					entity.getWorld().addEntity(invItem);
				}
				inventory.removeStackFromSlot(index);
				return ActionResultType.SUCCESS;
			}
			return ActionResultType.PASS;
		} else if (stack.isEmpty() && inventory.isItemValidForSlot(index, heldItem)) {
			int size = Math.min(heldItem.getCount(), inventory.getInventoryStackLimit());

			stack = heldItem.copy();
			stack.setCount(size);
			if (!player.isCreative()) {
				heldItem.shrink(size);
			}
			inventory.setInventorySlotContents(index, stack);
			return ActionResultType.SUCCESS;
		} else if (!stack.isEmpty() && stack.isItemEqual(heldItem) && stack.getCount() < stack.getMaxStackSize() && stack.getCount() < inventory.getInventoryStackLimit()) {
			int size = Math.min(heldItem.getCount(), inventory.getInventoryStackLimit() - stack.getCount());

			if (!player.isCreative()) {
				heldItem.shrink(size);
			}
			stack.grow(size);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	protected ActionResultType onSlotActivated(IInventory inventory, PlayerEntity player, ItemStack heldItem, int index) {
		ActionResultType ret = this.onSlotActivatedUnsync(inventory, player, heldItem, index);

		if (ret.isSuccess() && inventory instanceof IForcableSync) {
			((IForcableSync) inventory).forceSync();
		}
		return ret;
	}

	protected ActionResultType onSingleSlotActivated(World world, BlockPos pos, PlayerEntity player, Hand hand) {
		final IInventory inv = (IInventory) world.getTileEntity(pos);

		if (inv != null) {
			return this.onSlotActivated(inv, player, player.getHeldItem(hand), 0);
		}
		return ActionResultType.PASS;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof IInventory) {
				InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileentity);
				worldIn.updateComparatorOutputLevel(pos, this);
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}
}