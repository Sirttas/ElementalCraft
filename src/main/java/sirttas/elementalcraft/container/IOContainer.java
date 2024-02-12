package sirttas.elementalcraft.container;

import com.google.common.collect.Lists;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.nbt.NBTHelper;

import javax.annotation.Nonnull;

public class IOContainer extends AbstractSynchronizableContainer implements WorldlyContainer, INBTSerializable<CompoundTag> {

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
		this.setChanged();
		return ret;
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();

		NBTHelper.writeItemStack(nbt, ECNames.INPUT, this.input);
		NBTHelper.writeItemStack(nbt, ECNames.OUTPUT, this.output);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		this.input = NBTHelper.readItemStack(nbt, ECNames.INPUT);
		this.output = NBTHelper.readItemStack(nbt, ECNames.OUTPUT);

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
