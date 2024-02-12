package sirttas.elementalcraft.container;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.EmptyHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.IntStream;

public class ECContainerHelper {

	private ECContainerHelper() {}

	public static IItemHandler getItemHandlerAt(@Nonnull BlockGetter world, @Nonnull BlockPos pos) {
		return getItemHandlerAt(world, pos, null);
	}

	public static IItemHandler getItemHandlerAt(@Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nullable Direction side) {
		if (level instanceof Level l) {
			var handler = l.getCapability(Capabilities.ItemHandler.BLOCK, pos, side);

			if (handler != null) {
				return handler;
			}
		}
		return BlockEntityHelper.getBlockEntity(level, pos)
				.map(t -> getItemHandler(t, side))
				.orElse(EmptyHandler.INSTANCE);
	}

	@Nonnull
	public static IItemHandler getItemHandler(BlockEntity entity, @Nullable Direction side) {
		var handler = BlockEntityHelper.getCapability(Capabilities.ItemHandler.BLOCK, entity, side);

		if (handler != null) {
			return handler;
		} else if (entity instanceof WorldlyContainer worldlyContainer && side != null) {
			return new SidedInvWrapper(worldlyContainer, side);
		} else if (entity instanceof Container container) {
			return new InvWrapper(container);
		}
		return EmptyHandler.INSTANCE;
	}

	public static int getSlotFor(Container inv, ItemStack stack) {
		for (int i = 0; i < inv.getContainerSize(); ++i) {
			ItemStack current = inv.getItem(i);

			if (!current.isEmpty() && ItemStack.isSameItem(stack, current)) {
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
