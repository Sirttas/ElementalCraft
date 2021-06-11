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
	public void clearContent() {
		stack = ItemStack.EMPTY;
		this.setChanged();

	}

	@Override
	public int getContainerSize() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return stack.isEmpty();
	}

	@Override
	public ItemStack getItem(int index) {
		return index == 0 ? stack : ItemStack.EMPTY;
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		if (index == 0) {
			this.stack = stack;
		}
		this.setChanged();
	}

	@Override
	public boolean canPlaceItem(int index, ItemStack stack) {
		return index == 0;
	}

	@Override
	public ItemStack removeItem(int slot, int count) {
		ItemStack value = ItemStackHelper.removeItem(Lists.newArrayList(stack), slot, count);

		this.setChanged();
		return value;
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		ItemStack ret = stack;

		stack = ItemStack.EMPTY;
		this.setChanged();
		return ret;
	}

	@Override
	@Nonnull
	public CompoundNBT serializeNBT() {
		CompoundNBT stackNbt = new CompoundNBT();

		stack.save(stackNbt);
		return stackNbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		stack = ItemStack.of(nbt);
	}

}
