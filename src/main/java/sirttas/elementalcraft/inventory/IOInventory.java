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
	public void clearContent() {
		input = ItemStack.EMPTY;
		output = ItemStack.EMPTY;

	}

	@Override
	public int getContainerSize() {
		return 2;
	}

	@Override
	public boolean isEmpty() {
		return input.isEmpty() && output.isEmpty();
	}

	@Override
	public ItemStack getItem(int index) {
		if (index == 0) {
			return input;
		} else if (index == 1) {
			return output;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		if (index == 0) {
			this.input = stack;
		} else if (index == 1) {
			this.output = stack;
		}
		this.setChanged();
	}

	@Override
	public boolean canPlaceItem(int index, ItemStack stack) {
		return index == 0;
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		ItemStack value = ItemStackHelper.removeItem(Lists.newArrayList(input, output), index, count);

		this.setChanged();
		return value;
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		ItemStack ret = getItem(index);

		setItem(index, ItemStack.EMPTY);
		this.setChanged();
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
	public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
		return index == 0 || direction == null;
	}

	@Override
	public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
		return index == 1 || direction == null;
	}
}
