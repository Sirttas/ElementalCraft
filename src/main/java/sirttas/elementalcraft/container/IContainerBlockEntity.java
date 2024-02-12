package sirttas.elementalcraft.container;

import net.minecraft.core.Direction;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IContainerBlockEntity extends Clearable {

	@Nonnull
	Container getInventory();

	@Nonnull
	IItemHandler getItemHandler(@Nullable Direction direction);

	@Override
	default void clearContent() {
		getInventory().clearContent();
	}
}
