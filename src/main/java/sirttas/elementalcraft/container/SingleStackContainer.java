package sirttas.elementalcraft.container;

import com.google.common.collect.Lists;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import org.jspecify.annotations.Nullable;

public class SingleStackContainer extends AbstractSynchronizableContainer implements ValueIOSerializable {

	protected ItemStack stack;

	public SingleStackContainer() {
		this(null);
	}

	public SingleStackContainer(@Nullable Runnable syncCallback) {
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
	public void setItem(int slot, ItemStack itemStack) {
		this.setItem(slot, itemStack, false);
	}


	@Override
	public void setItem(int index, ItemStack stack, boolean insideTransaction) {
		if (index == 0) {
			this.stack = stack;
		}
		if (!insideTransaction) {
			this.setChanged();
		}
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
		return ret;
	}

    @Override
    public void serialize(ValueOutput output) {
		if (stack.isEmpty()) {
			output.discard("stack");
			return;
		}
        output.store("stack", ItemStack.CODEC, stack);
    }

    @Override
    public void deserialize(ValueInput input) {
        stack = input.read("stack", ItemStack.CODEC).orElse(ItemStack.EMPTY);
    }
}
