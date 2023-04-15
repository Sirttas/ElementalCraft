package sirttas.elementalcraft.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.container.ContainerBlockEntityWrapper;
import sirttas.elementalcraft.container.IContainerBlockEntity;

import javax.annotation.Nonnull;

public interface IContainerBlockEntityRecipe<T extends IContainerBlockEntity> extends IECRecipe<ContainerBlockEntityWrapper<T>> {

	boolean matches(T inv);

	@Override
	default boolean matches(ContainerBlockEntityWrapper<T> inv, @Nonnull Level level) {
		return matches(inv.getEntity());
	}

	@Nonnull
    @Override
	default ItemStack assemble(@Nonnull ContainerBlockEntityWrapper<T> inv) {
		return assemble(inv.getEntity());
	}

	default ItemStack assemble(T instrument) {
		return this.getResultItem().copy();
	}
}
