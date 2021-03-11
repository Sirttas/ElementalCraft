package sirttas.elementalcraft.inventory;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class SingleStackInventory extends AbstractSynchronizableInventory implements INBTSerializable<CompoundNBT> {

	protected ItemStack stack;

	public SingleStackInventory() {
		this(null);
	}

	public SingleStackInventory(Runnable syncCallback) {
		super(syncCallback);
		stack = ItemStack.EMPTY;
	}

	@Override
	public void clear() {
		stack = ItemStack.EMPTY;
		this.markDirty();

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
		this.markDirty();
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return index == 0;
	}

	@Override
	public ItemStack decrStackSize(int slot, int count) {
		ItemStack value = ItemStackHelper.getAndSplit(Lists.newArrayList(stack), slot, count);

		this.markDirty();
		return value;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack ret = stack;

		stack = ItemStack.EMPTY;
		this.markDirty();
		return ret;
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
