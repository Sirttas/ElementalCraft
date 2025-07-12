package sirttas.elementalcraft.container;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SingleItemContainer extends SingleStackContainer {

	public SingleItemContainer() {
		this(null);
	}

	public SingleItemContainer(Runnable syncCallback) {
		super(syncCallback);
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	@Override
	public @NotNull ItemStack removeItem(int slot, int count) {
		ItemStack value = slot == 0 && !stack.isEmpty() && count == 1 ? stack.split(count) : ItemStack.EMPTY;

		this.setChanged();
		return value;
	}
}
