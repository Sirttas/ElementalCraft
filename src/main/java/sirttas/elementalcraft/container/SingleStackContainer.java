package sirttas.elementalcraft.container;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class SingleStackContainer extends AbstractSynchronizableContainer implements INBTSerializable<CompoundTag> {

	protected ItemStack stack;

	public SingleStackContainer() {
		this(null);
	}

	public SingleStackContainer(Runnable syncCallback) {
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
		ItemStack value = ContainerHelper.removeItem(Lists.newArrayList(stack), slot, count);

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
	public CompoundTag serializeNBT() {
		CompoundTag stackNbt = new CompoundTag();

		stack.save(stackNbt);
		return stackNbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		stack = ItemStack.of(nbt);
	}

}
