package sirttas.elementalcraft.block.tile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;

public abstract class TileECContainer extends TileECTickable implements IInventory {

	public TileECContainer(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}


	@Override
	public void openInventory(PlayerEntity player) {
	}

	@Override
	public void closeInventory(PlayerEntity player) {
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return true;
	}

	@Override
	public ItemStack decrStackSize(int slot, int count) {
		ItemStack oldStack = getStackInSlot(slot);
		int newCount = (count > oldStack.getCount()) ? oldStack.getCount() : count;

		oldStack.grow(-newCount);
		ItemStack stack = oldStack.copy();
		stack.setCount(newCount);
		if (stack.getCount() <= 0) {
			this.removeStackFromSlot(slot);
			return ItemStack.EMPTY;
		}
		return stack;
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		this.clear();
		super.onDataPacket(net, packet);
	}
}
