package sirttas.elementalcraft.inventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
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
}
