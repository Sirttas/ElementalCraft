package sirttas.elementalcraft.container;

import com.google.common.collect.Lists;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.name.ECNames;

import javax.annotation.Nonnull;

public class IOContainer extends AbstractSynchronizableContainer implements WorldlyContainer {

	private ItemStack input;
	private ItemStack output;

	public IOContainer() {
		this(null);
	}

	public IOContainer(Runnable syncCallback) {
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

	@Nonnull
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
	public void setItem(int index, @Nonnull ItemStack stack) {
		if (index == 0) {
			this.input = stack;
		} else if (index == 1) {
			this.output = stack;
		}
		this.setChanged();
	}

	@Override
	public boolean canPlaceItem(int index, @Nonnull ItemStack stack) {
		return index == 0;
	}

	@Nonnull
    @Override
	public ItemStack removeItem(int index, int count) {
		ItemStack value = ContainerHelper.removeItem(Lists.newArrayList(input, output), index, count);

		this.setChanged();
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
	public CompoundTag serializeNBT(@Nonnull HolderLookup.Provider provider) {
		var tag = new CompoundTag();

		if (!input.isEmpty()) {
			tag.put(ECNames.INPUT, this.input.save(provider));
		}
		if (!output.isEmpty()) {
			tag.put(ECNames.OUTPUT, this.output.save(provider));
		}
		return tag;
	}

	@Override
	public void deserializeNBT(@Nonnull HolderLookup.Provider provider, @NotNull CompoundTag tag) {
		this.input = tag.contains(ECNames.INPUT) ? ItemStack.parseOptional(provider, tag.getCompound(ECNames.INPUT)) : ItemStack.EMPTY;
		this.output = tag.contains(ECNames.OUTPUT) ? ItemStack.parseOptional(provider, tag.getCompound(ECNames.OUTPUT)) : ItemStack.EMPTY;
	}

	@Nonnull
    @Override
	public int[] getSlotsForFace(@Nonnull Direction side) {
		return new int[] { 0, 1 };
	}

	@Override
	public boolean canPlaceItemThroughFace(int index, @Nonnull ItemStack stack, Direction direction) {
		return index == 0 || direction == null;
	}

	@Override
	public boolean canTakeItemThroughFace(int index, @Nonnull ItemStack stack, @Nonnull Direction direction) {
		return index == 1 || direction == null;
	}
}
