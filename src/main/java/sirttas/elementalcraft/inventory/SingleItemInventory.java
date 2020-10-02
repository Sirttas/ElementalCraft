package sirttas.elementalcraft.inventory;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class SingleItemInventory extends SynchronizableInventory implements INBTSerializable<CompoundNBT> {

	private ItemStack stack;

	public SingleItemInventory() {
		this(null);
	}

	public SingleItemInventory(Runnable syncCallback) {
		super(syncCallback);
		stack = ItemStack.EMPTY;
	}

	@Override
	public void clear() {
		stack = ItemStack.EMPTY;

	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return stack.isEmpty();
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return index == 0 ? stack : ItemStack.EMPTY;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (index == 0) {
			this.stack = stack;
		}
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return index == 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public ItemStack decrStackSize(int slot, int count) {
		return slot == 0 && !stack.isEmpty() && count == 1 ? stack.split(count) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack ret = stack;

		stack = ItemStack.EMPTY;
		return ret;
	}



	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return true;
	}

	@Override
	@Nonnull
	public CompoundNBT serializeNBT() {
		CompoundNBT stackNbt = new CompoundNBT();

		stack.write(stackNbt);
		return stackNbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		stack = ItemStack.read(nbt);
	}

}
