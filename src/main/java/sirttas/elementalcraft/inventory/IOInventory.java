package sirttas.elementalcraft.inventory;

import com.google.common.collect.Lists;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.INBTSerializable;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.nbt.NBTHelper;

public class IOInventory extends AbstractSynchronizableInventory implements ISidedInventory, INBTSerializable<CompoundNBT> {

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
		this.markDirty();
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return index == 0;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack value = ItemStackHelper.getAndSplit(Lists.newArrayList(input, output), index, count);

		this.markDirty();
		return value;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack ret = getStackInSlot(index);

		setInventorySlotContents(index, ItemStack.EMPTY);
		this.markDirty();
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

	@Override
	public int[] getSlotsForFace(Direction side) {
		return new int[] { 0, 1 };
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
		return index == 0 || direction == null;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
		return index == 1 || direction == null;
	}
}
