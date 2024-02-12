package sirttas.elementalcraft.recipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.container.ContainerBlockEntityWrapper;
import sirttas.elementalcraft.container.IContainerBlockEntity;

import javax.annotation.Nonnull;

public interface IContainerBlockEntityRecipe<T extends IContainerBlockEntity> extends IECRecipe<ContainerBlockEntityWrapper<T>> {

	boolean matches(T inv, @Nonnull Level level);

	@Override
	default boolean matches(ContainerBlockEntityWrapper<T> wrapper, @Nonnull Level level) {
		return matches(wrapper.getEntity(), level);
	}

	@Nonnull
    @Override
	default ItemStack assemble(@Nonnull ContainerBlockEntityWrapper<T> wrapper, @Nonnull RegistryAccess registry) {
		return assemble(wrapper.getEntity(), registry);
	}

	@Nonnull
	default ItemStack assemble(@Nonnull T instrument, @Nonnull RegistryAccess registry) {
		return this.getResultItem(registry).copy();
	}
}
