package sirttas.elementalcraft.inventory;

import javax.annotation.Nonnull;

import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public interface IInventoryBlockEntity extends Clearable {

	@Nonnull
	Container getInventory();
	
	LazyOptional<IItemHandler> getItemHandler();

	@Override
	default void clearContent() {
		getInventory().clearContent();
	}
}
