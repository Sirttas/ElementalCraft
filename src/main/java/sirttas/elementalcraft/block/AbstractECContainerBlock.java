package sirttas.elementalcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.entity.player.ECPlayerHelper;

public abstract class AbstractECContainerBlock extends AbstractECEntityBlock {

	protected AbstractECContainerBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

    public InteractionResult onSlotActivated(ResourceHandler<ItemResource> inventory, Player player, ItemStack heldItem, int slot) {
		var level = player.level();

		var resourceInSlot = inventory.getResource(slot);
		var amountInSlot = inventory.getAmountAsInt(slot);
		var resourceInHand = ItemResource.of(heldItem);
		var amountInHand = heldItem.getCount();

		var capacity = Math.min(resourceInHand.getMaxStackSize(), inventory.getCapacityAsInt(slot, resourceInHand));
		var canInsert = (resourceInSlot.equals(resourceInHand) && amountInSlot < capacity) || resourceInSlot.isEmpty();

		// Extract
		if (resourceInHand.isEmpty() || player.isShiftKeyDown() || !canInsert) {
			if (!resourceInSlot.isEmpty()) {
				try (var transaction = Transaction.openRoot()) {
					var amount = inventory.extract(slot, resourceInSlot, amountInSlot, transaction);

					if (amount > 0) {
						EntityHelper.dropAtFeet(level, player, resourceInSlot.toStack(amount));
						transaction.commit();
						return InteractionResult.SUCCESS;
					}
				}
			}
			return InteractionResult.PASS;
		}

		// Insert
		var amount = Math.min(amountInHand, capacity - amountInSlot);

		try (var transaction = Transaction.openRoot()) {
			amount = inventory.insert(slot, resourceInHand, amount, transaction);

			if (amount > 0) {
				ECPlayerHelper.shrinkItemInHand(player, heldItem, InteractionHand.MAIN_HAND, amount);
				transaction.commit();
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

	protected InteractionResult onSingleSlotActivated(ItemStack stack, Level level, BlockPos pos, Player player, InteractionHand hand) {
		var inv = ECContainerHelper.getItemResourceHandlerAt(level, pos, null);

		if (inv != null && hand == InteractionHand.MAIN_HAND) {
			return this.onSlotActivated(inv, player, stack, 0);
		}
		return InteractionResult.PASS;
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        return ResourceHandlerUtil.getRedstoneSignalFromResourceHandler(ECContainerHelper.getItemResourceHandlerAt(level, pos));
    }
}
