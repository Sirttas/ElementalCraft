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
		return slot >= 0 && slot < this.getSizeInventory() && !this.getStackInSlot(slot).isEmpty() && count > 0 ? this.getStackInSlot(slot).split(count) : ItemStack.EMPTY;
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		this.clear();
		super.onDataPacket(net, packet);
	}

	@Override
	public void markDirty() {
		this.forceSync();
		super.markDirty();
	}
}
