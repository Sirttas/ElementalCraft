package sirttas.elementalcraft.container;

import java.util.stream.IntStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;

public class ECContainerHelper {

	private ECContainerHelper() {}
	
	public static IItemHandler getItemHandlerAt(@Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nullable Direction side) {
		return BlockEntityHelper.getBlockEntity(world, pos).map(t -> t.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side).orElseGet(() -> {
			if (t instanceof WorldlyContainer && side != null) {
				return new SidedInvWrapper((WorldlyContainer) t, side);
			}
			if (t instanceof Container) {
				return new InvWrapper((Container) t);
			}
			return EmptyHandler.INSTANCE;
		})).orElse(EmptyHandler.INSTANCE);
	}

	public static boolean stackEqualExact(ItemStack stack1, ItemStack stack2) {
		return ItemStack.isSame(stack1, stack2) && ItemStack.tagMatches(stack1, stack2);
	}

	public static boolean stackEqualCount(ItemStack stack1, ItemStack stack2) {
		return ItemStack.isSame(stack1, stack2) && stack1.getCount() == stack2.getCount();
	}

	public static int getSlotFor(Container inv, ItemStack stack) {
		for (int i = 0; i < inv.getContainerSize(); ++i) {
			ItemStack current = inv.getItem(i);

			if (!current.isEmpty() && stackEqualExact(stack, current)) {
				return i;
			}
		}

		return -1;
	}

	public static int getItemCount(Container inv) {
		return (int) IntStream.range(0, inv.getContainerSize()).filter(i -> !inv.getItem(i).isEmpty()).count();
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
