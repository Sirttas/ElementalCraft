package sirttas.elementalcraft.container;

import net.minecraft.core.Direction;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IContainerBlockEntity extends Clearable {

	@Nonnull
	Container getInventory();

	@Override
	default void clearContent() {
		getInventory().clearContent();
	}

	@Nonnull
	default IItemHandler getItemHandler(@Nullable Direction direction) {
		var inv = this.getInventory();

		if (inv instanceof WorldlyContainer worldlyContainer) {
			return new SidedInvWrapper(worldlyContainer, direction);
		}
		return new InvWrapper(this.getInventory());
	}
}
