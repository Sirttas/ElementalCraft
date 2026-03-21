package sirttas.elementalcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.entity.player.ECPlayerHelper;

import javax.annotation.Nonnull;

public abstract class AbstractECContainerBlock extends AbstractECEntityBlock {

	protected AbstractECContainerBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	private boolean canInsertStack(IItemHandler inventory, ItemStack stack, ItemStack heldItem, int slot) {
		return ItemStack.isSameItemSameComponents(stack, heldItem) && stack.getCount() < stack.getMaxStackSize() && stack.getCount() < inventory.getSlotLimit(slot);
	}

	public ItemInteractionResult onSlotActivated(IItemHandler inventory, Player player, ItemStack heldItem, int slot) {
		ItemStack stack = inventory.getStackInSlot(slot);
		Level level = player.level();

		if (heldItem.isEmpty() || player.isShiftKeyDown() || (!stack.isEmpty() && !canInsertStack(inventory, stack, heldItem, slot))) {
			if (!stack.isEmpty()) {
				if (!level.isClientSide()) {
					EntityHelper.dropAtFeet(level, player, inventory.extractItem(slot, stack.getCount(), false));
				}
				return ItemInteractionResult.SUCCESS;
			}
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		} else if (stack.isEmpty() && inventory.isItemValid(slot, heldItem)) {
			int size = Math.min(heldItem.getCount(), inventory.getSlotLimit(slot));

			stack = heldItem.copy();
			stack.setCount(size);
			ECPlayerHelper.shrinkItemInHand(player, heldItem, InteractionHand.MAIN_HAND, size);
			inventory.insertItem(slot, stack, false);
			return ItemInteractionResult.SUCCESS;
		} else if (!stack.isEmpty() && canInsertStack(inventory, stack, heldItem, slot)) {
			int size = Math.min(heldItem.getCount(), inventory.getSlotLimit(slot) - stack.getCount());

			ECPlayerHelper.shrinkItemInHand(player, heldItem, InteractionHand.MAIN_HAND, size);
			stack.grow(size);
			return ItemInteractionResult.SUCCESS;
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	protected ItemInteractionResult onSingleSlotActivated(ItemStack stack, Level level, BlockPos pos, Player player, InteractionHand hand) {
		var inv = ECContainerHelper.getItemHandlerAt(level, pos, null);

		if (inv != null && hand == InteractionHand.MAIN_HAND) {
			return this.onSlotActivated(inv, player, stack, 0);
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	public boolean hasAnalogOutputSignal(@Nonnull BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(@Nonnull BlockState blockState, @Nonnull Level level, @Nonnull BlockPos pos) {
		return ItemHandlerHelper.calcRedstoneFromInventory(ECContainerHelper.getItemHandlerAt(level, pos));
	}
}
