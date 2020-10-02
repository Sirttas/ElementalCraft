package sirttas.elementalcraft.block.instrument.binder;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.INBTSerializable;
import sirttas.elementalcraft.inventory.SynchronizableInventory;

public class BinderInventory extends SynchronizableInventory implements INBTSerializable<CompoundNBT> {

	private final NonNullList<ItemStack> stacks;

	public BinderInventory() {
		this(null);
	}

	public BinderInventory(Runnable syncCallback) {
		super(syncCallback);

		stacks = NonNullList.withSize(10, ItemStack.EMPTY);
	}

	@Override
	public int getSizeInventory() {
		return 10;
	}

	public int getItemCount() {
		return (int) stacks.stream().filter(i -> !i.isEmpty()).count();
	}

	@Override
	public boolean isEmpty() {
		return stacks.isEmpty() || stacks.stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return stacks.size() > index ? stacks.get(index) : ItemStack.EMPTY;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (index < stacks.size()) {
			stacks.set(index, stack);
		} else if (stack.isEmpty()) {
			stacks.add(stack);
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public void clear() {
		stacks.clear();

	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(stacks, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack ret = getStackInSlot(index);

		setInventorySlotContents(index, ItemStack.EMPTY);
		return ret;
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return true;
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();

		ItemStackHelper.saveAllItems(nbt, this.stacks);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		clear();
		ItemStackHelper.loadAllItems(nbt, this.stacks);
	}
}
