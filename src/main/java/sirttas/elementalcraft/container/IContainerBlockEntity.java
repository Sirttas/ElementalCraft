package sirttas.elementalcraft.container;

import net.minecraft.core.Direction;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.item.WorldlyContainerWrapper;
import org.jspecify.annotations.Nullable;


@Deprecated
public interface IContainerBlockEntity extends Clearable {

	Container getInventory();

	@Override
	default void clearContent() {
		getInventory().clearContent();
	}

    default ResourceHandler<ItemResource> getItemResourceHandler(@Nullable Direction direction) {
        var inv = this.getInventory();

        if (inv instanceof WorldlyContainer worldlyContainer) {
            return new WorldlyContainerWrapper(worldlyContainer, direction);
        }
        return VanillaContainerWrapper.of(this.getInventory());
    }
}
