package sirttas.elementalcraft.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import sirttas.elementalcraft.block.tile.IForcableSync;
import sirttas.elementalcraft.inventory.ECInventoryHelper;

public abstract class BlockECContainer extends BlockECTileProvider {

	public BlockECContainer() {
		super();
	}

	public BlockECContainer(AbstractBlock.Properties properties) {
		super(properties);
	}

	private boolean canInsertStack(IItemHandler inventory, ItemStack stack, ItemStack heldItem, int slot) {
		return stack.isItemEqual(heldItem) && stack.getCount() < stack.getMaxStackSize() && stack.getCount() < inventory.getSlotLimit(slot);
	}

	private ActionResultType onSlotActivatedUnsync(IItemHandler inventory, PlayerEntity player, ItemStack heldItem, int slot) {
		ItemStack stack = inventory.getStackInSlot(slot);
		World world = player.getEntityWorld();

		if (heldItem.isEmpty() || player.isSneaking() || (!stack.isEmpty() && !canInsertStack(inventory, stack, heldItem, slot))) {
			if (!stack.isEmpty()) {
				ItemStack extracted = inventory.extractItem(slot, stack.getCount(), false);

				if (!world.isRemote()) {
					world.addEntity(new ItemEntity(world, player.getPosX(), player.getPosY() + 0.25, player.getPosZ(), extracted));
				}
				return ActionResultType.SUCCESS;
			}
			return ActionResultType.PASS;
		} else if (stack.isEmpty() && inventory.isItemValid(slot, heldItem)) {
			int size = Math.min(heldItem.getCount(), inventory.getSlotLimit(slot));

			stack = heldItem.copy();
			stack.setCount(size);
			if (!player.isCreative()) {
				heldItem.shrink(size);
			}
			inventory.insertItem(slot, stack, false);
			return ActionResultType.SUCCESS;
		} else if (!stack.isEmpty() && canInsertStack(inventory, stack, heldItem, slot)) {
			int size = Math.min(heldItem.getCount(), inventory.getSlotLimit(slot) - stack.getCount());

			if (!player.isCreative()) {
				heldItem.shrink(size);
			}
			stack.grow(size);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	protected ActionResultType onSlotActivated(IItemHandler inventory, PlayerEntity player, ItemStack heldItem, int slot) {
		ActionResultType ret = this.onSlotActivatedUnsync(inventory, player, heldItem, slot);

		if (ret.isSuccessOrConsume() && inventory instanceof IForcableSync) {
			((IForcableSync) inventory).forceSync();
		}
		return ret;
	}

	protected ActionResultType onSingleSlotActivated(World world, BlockPos pos, PlayerEntity player, Hand hand) {
		final IItemHandler inv = ECInventoryHelper.getItemHandlerAt(world, pos, null);

		if (inv != null) {
			return this.onSlotActivated(inv, player, player.getHeldItem(hand), 0);
		}
		return ActionResultType.PASS;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			IItemHandler inv = ECInventoryHelper.getItemHandlerAt(worldIn, pos, null);
			
			for (int i = 0; i < inv.getSlots(); i++) {
				InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), inv.getStackInSlot(i));
			}
			worldIn.updateComparatorOutputLevel(pos, this);

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}
}