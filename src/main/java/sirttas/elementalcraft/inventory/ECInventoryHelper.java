package sirttas.elementalcraft.inventory;

import java.util.stream.IntStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import sirttas.elementalcraft.block.tile.TileEntityHelper;

public class ECInventoryHelper {

	private ECInventoryHelper() {}
	
	public static IItemHandler getItemHandlerAt(@Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nullable Direction side) {
		return TileEntityHelper.getTileEntity(world, pos).map(t -> t.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side).orElseGet(() -> {
			if (t instanceof ISidedInventory && side != null) {
				return new SidedInvWrapper((ISidedInventory) t, side);
			}
			if (t instanceof IInventory) {
				return new InvWrapper((IInventory) t);
			}
			return EmptyHandler.INSTANCE;
		})).orElse(EmptyHandler.INSTANCE);
	}

	public static boolean stackEqualExact(ItemStack stack1, ItemStack stack2) {
		return ItemStack.areItemsEqual(stack1, stack2) && ItemStack.areItemStackTagsEqual(stack1, stack2);
	}

	public static boolean stackEqualCount(ItemStack stack1, ItemStack stack2) {
		return ItemStack.areItemsEqual(stack1, stack2) && stack1.getCount() == stack2.getCount();
	}

	public static int getSlotFor(IInventory inv, ItemStack stack) {
		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack current = inv.getStackInSlot(i);

			if (!current.isEmpty() && stackEqualExact(stack, current)) {
				return i;
			}
		}

		return -1;
	}

	public static int getItemCount(IInventory inv) {
		return (int) IntStream.range(0, inv.getSizeInventory()).filter(i -> !inv.getStackInSlot(i).isEmpty()).count();
	}

	public static boolean isEmpty(IItemHandler targetInv) {
		for (int i = 0; i < targetInv.getSlots(); i++) {
			if (!targetInv.getStackInSlot(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}
}
