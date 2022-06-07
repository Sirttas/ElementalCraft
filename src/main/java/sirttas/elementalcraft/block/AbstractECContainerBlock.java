package sirttas.elementalcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import sirttas.elementalcraft.container.ECContainerHelper;

import javax.annotation.Nonnull;

public abstract class AbstractECContainerBlock extends AbstractECEntityBlock {

	protected AbstractECContainerBlock() {
		super();
	}

	protected AbstractECContainerBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	private boolean canInsertStack(IItemHandler inventory, ItemStack stack, ItemStack heldItem, int slot) {
		return ItemHandlerHelper.canItemStacksStack(stack, heldItem) && stack.getCount() < stack.getMaxStackSize() && stack.getCount() < inventory.getSlotLimit(slot);
	}

	public InteractionResult onSlotActivated(IItemHandler inventory, Player player, ItemStack heldItem, int slot) {
		ItemStack stack = inventory.getStackInSlot(slot);
		Level world = player.getLevel();

		if (heldItem.isEmpty() || player.isShiftKeyDown() || (!stack.isEmpty() && !canInsertStack(inventory, stack, heldItem, slot))) {
			if (!stack.isEmpty()) {
				if (!world.isClientSide()) {
					ItemStack extracted = inventory.extractItem(slot, stack.getCount(), false);

					world.addFreshEntity(new ItemEntity(world, player.getX(), player.getY() + 0.25, player.getZ(), extracted));
				}
				return InteractionResult.SUCCESS;
			}
			return InteractionResult.PASS;
		} else if (stack.isEmpty() && inventory.isItemValid(slot, heldItem)) {
			int size = Math.min(heldItem.getCount(), inventory.getSlotLimit(slot));

			stack = heldItem.copy();
			stack.setCount(size);
			if (!player.isCreative()) {
				heldItem.shrink(size);
			}
			inventory.insertItem(slot, stack, false);
			return InteractionResult.SUCCESS;
		} else if (!stack.isEmpty() && canInsertStack(inventory, stack, heldItem, slot)) {
			int size = Math.min(heldItem.getCount(), inventory.getSlotLimit(slot) - stack.getCount());

			if (!player.isCreative()) {
				heldItem.shrink(size);
			}
			stack.grow(size);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	protected InteractionResult onSingleSlotActivated(Level world, BlockPos pos, Player player, InteractionHand hand) {
		final IItemHandler inv = ECContainerHelper.getItemHandlerAt(world, pos, null);
		ItemStack heldItem = player.getItemInHand(hand);

		if (inv != null && (hand == InteractionHand.MAIN_HAND || !heldItem.isEmpty())) {
			return this.onSlotActivated(inv, player, heldItem, 0);
		}
		return InteractionResult.PASS;
	}

	@Override
	@Deprecated
	public boolean hasAnalogOutputSignal(@Nonnull BlockState state) {
		return true;
	}

	@Override
	@Deprecated
	public int getAnalogOutputSignal(@Nonnull BlockState blockState, Level level, @Nonnull BlockPos pos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
	}
}
