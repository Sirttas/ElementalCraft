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
import net.neoforged.neoforge.transfer.EmptyResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.item.WorldlyContainerWrapper;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;


public class ECContainerHelper {

	private ECContainerHelper() {}

	public static ResourceHandler<ItemResource> getItemResourceHandlerAt(BlockGetter world, BlockPos pos) {
		return getItemResourceHandlerAt(world, pos, null);
	}

	public static ResourceHandler<ItemResource> getItemResourceHandlerAt(BlockGetter level, BlockPos pos, @Nullable Direction side) {
		if (level instanceof Level l) {
			var handler = l.getCapability(Capabilities.Item.BLOCK, pos, side);

			if (handler != null) {
				return handler;
			}
		}
		return BlockEntityHelper.getBlockEntity(level, pos)
				.map(t -> getItemResourceHandler(t, side))
				.orElseGet(EmptyResourceHandler::instance);
	}

	public static ResourceHandler<ItemResource> getItemResourceHandler(BlockEntity entity, @Nullable Direction side) {
		var handler = BlockEntityHelper.getCapability(Capabilities.Item.BLOCK, entity, side);

		if (handler != null) {
			return handler;
		} else if (entity instanceof WorldlyContainer worldlyContainer && side != null) {
			return new WorldlyContainerWrapper(worldlyContainer, side);
		} else if (entity instanceof Container container) {
			return VanillaContainerWrapper.of(container);
		}
		return EmptyResourceHandler.instance();
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

	public static boolean isEmpty(ResourceHandler<ItemResource> targetInv) {
		for (int i = 0; i < targetInv.size(); i++) {
			if (!targetInv.getResource(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}
}
