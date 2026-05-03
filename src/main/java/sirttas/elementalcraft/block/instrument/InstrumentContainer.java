package sirttas.elementalcraft.block.instrument;

import net.minecraft.core.NonNullList;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import sirttas.elementalcraft.container.AbstractSynchronizableContainer;

import javax.annotation.Nonnull;
import java.util.List;

public class InstrumentContainer extends AbstractSynchronizableContainer implements ValueIOSerializable {

	private final NonNullList<ItemStack> stacks;
	private final int size;

	public InstrumentContainer(Runnable syncCallback, int size) {
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

	@Nonnull
	@Override
	public ItemStack getItem(int index) {
		return stacks.size() > index ? stacks.get(index) : ItemStack.EMPTY;
	}

	@Override
	public void setItem(int slot, @NonNull ItemStack itemStack) {
		this.setItem(slot, itemStack, false);
	}

	@Override
	public void setItem(int index, @Nonnull ItemStack stack, boolean insideTransaction) {
		if (index < stacks.size()) {
			stacks.set(index, stack);
		} else if (stack.isEmpty()) {
			stacks.add(stack);
		}
		if (!insideTransaction) {
			setChanged();
		}
	}

	public List<ItemStack> getStacks() {
		return stacks;
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	@Override
	public void clearContent() {
		stacks.clear();
		setChanged();
	}

	@Nonnull
	@Override
	public ItemStack removeItem(int index, int count) {
		var value = ContainerHelper.removeItem(stacks, index, count);

		setChanged();
		return value;
	}

	@Nonnull
	@Override
	public ItemStack removeItemNoUpdate(int index) {
		ItemStack ret = getItem(index);

		setItem(index, ItemStack.EMPTY);
		return ret;
	}

    @Override
    public void serialize(@NotNull ValueOutput output) {
        ContainerHelper.saveAllItems(output, this.stacks);
    }

    @Override
    public void deserialize(@NotNull ValueInput input) {
		this.stacks.clear();
        ContainerHelper.loadAllItems(input, this.stacks);
    }
}
