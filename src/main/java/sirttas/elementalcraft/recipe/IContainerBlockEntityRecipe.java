package sirttas.elementalcraft.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.container.IContainerBlockEntity;
import sirttas.elementalcraft.container.ContainerBlockEntityWrapper;

public interface IContainerBlockEntityRecipe<T extends IContainerBlockEntity> extends IECRecipe<ContainerBlockEntityWrapper<T>> {

	int getElementAmount();

	boolean matches(T inv);

	@Override
	default boolean matches(ContainerBlockEntityWrapper<T> inv, Level worldIn) {
		return matches(inv.getEntity());
	}

	@Override
	default ItemStack assemble(ContainerBlockEntityWrapper<T> inv) {
		return assemble(inv.getEntity());
	}
	
	@Override
	default boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	default ItemStack assemble(T instrument) {
		return this.getResultItem().copy();
	}
}
