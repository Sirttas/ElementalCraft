package sirttas.elementalcraft.block.instrument;

import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.NonNullList;
import net.minecraftforge.common.util.INBTSerializable;
import sirttas.elementalcraft.inventory.AbstractSynchronizableInventory;

public class InstrumentInventory extends AbstractSynchronizableInventory implements INBTSerializable<CompoundTag> {

	private final NonNullList<ItemStack> stacks;
	private final int size;

	public InstrumentInventory(int size) {
		this(null, size);
	}

	public InstrumentInventory(Runnable syncCallback, int size) {
		super(syncCallback);
		this.size = size;
		stacks = NonNullList.withSize(size, ItemStack.EMPTY);
	}

	@Override
	public int getContainerSize() {
		return size;
	}

	public int getItemCount() {
		return (int) stacks.stream().filter(i -> !i.isEmpty()).count();
	}

	@Override
	public boolean isEmpty() {
		return stacks.isEmpty() || stacks.stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public ItemStack getItem(int index) {
		return stacks.size() > index ? stacks.get(index) : ItemStack.EMPTY;
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		if (index < stacks.size()) {
			stacks.set(index, stack);
		} else if (stack.isEmpty()) {
			stacks.add(stack);
		}
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	@Override
	public void clearContent() {
		stacks.clear();

	}

	@Override
	public ItemStack removeItem(int index, int count) {
		return ContainerHelper.removeItem(stacks, index, count);
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {
		ItemStack ret = getItem(index);

		setItem(index, ItemStack.EMPTY);
		return ret;
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();

		ContainerHelper.saveAllItems(nbt, this.stacks);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		clearContent();
		ContainerHelper.loadAllItems(nbt, this.stacks);
	}
}
