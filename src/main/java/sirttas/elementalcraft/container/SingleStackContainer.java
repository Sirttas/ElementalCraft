package sirttas.elementalcraft.container;

import com.google.common.collect.Lists;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

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

	@Nonnull
    @Override
	public ItemStack getItem(int index) {
		return index == 0 ? stack : ItemStack.EMPTY;
	}

	@Override
	public void setItem(int index, @Nonnull ItemStack stack) {
		if (index == 0) {
			this.stack = stack;
		}
		this.setChanged();
	}

	@Override
	public boolean canPlaceItem(int index, @Nonnull ItemStack stack) {
		return index == 0;
	}

	@Nonnull
    @Override
	public ItemStack removeItem(int slot, int count) {
		ItemStack value = ContainerHelper.removeItem(Lists.newArrayList(stack), slot, count);

		this.setChanged();
		return value;
	}

	@Nonnull
    @Override
	public ItemStack removeItemNoUpdate(int index) {
		ItemStack ret = stack;

		stack = ItemStack.EMPTY;
		return ret;
	}

	@Override
	@Nonnull
	public CompoundTag serializeNBT(@Nonnull HolderLookup.Provider provider) {
		var compoundTag = new CompoundTag();

		if (!stack.isEmpty()) {
			compoundTag.put("stack", stack.save(provider));
		}
		return compoundTag;
	}

	@Override
	public void deserializeNBT(@Nonnull HolderLookup.Provider provider, @NotNull CompoundTag compoundTag) {
		stack = ItemStack.parseOptional(provider, compoundTag.getCompound("stack"));
	}

}
