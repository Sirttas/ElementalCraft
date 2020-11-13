package sirttas.elementalcraft.inventory;

import com.google.common.collect.Lists;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import sirttas.elementalcraft.nbt.ECNames;
import sirttas.elementalcraft.nbt.NBTHelper;

public class IOInventory extends SynchronizableInventory implements INBTSerializable<CompoundNBT> {

	private ItemStack input = ItemStack.EMPTY;
	private ItemStack output = ItemStack.EMPTY;

	public IOInventory() {
		this(null);
	}

	public IOInventory(Runnable syncCallback) {
		super(syncCallback);
		input = ItemStack.EMPTY;
		output = ItemStack.EMPTY;
	}

	@Override
	public void clear() {
		input = ItemStack.EMPTY;
		output = ItemStack.EMPTY;

	}

	@Override
	public int getSizeInventory() {
		return 2;
	}

	@Override
	public boolean isEmpty() {
		return input.isEmpty() && output.isEmpty();
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		if (index == 0) {
			return input;
		} else if (index == 1) {
			return output;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (index == 0) {
			this.input = stack;
		} else if (index == 1) {
			this.output = stack;
		}
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return index == 0;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(Lists.newArrayList(input, output), index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack ret = getStackInSlot(index);

		setInventorySlotContents(index, ItemStack.EMPTY);
		return ret;
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();

		NBTHelper.writeItemStack(nbt, ECNames.INPUT, this.input);
		NBTHelper.writeItemStack(nbt, ECNames.OUTPUT, this.output);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		this.input = NBTHelper.readItemStack(nbt, ECNames.INPUT);
		this.output = NBTHelper.readItemStack(nbt, ECNames.OUTPUT);

	}
}
