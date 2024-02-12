package sirttas.elementalcraft.recipe;

import sirttas.elementalcraft.container.ContainerBlockEntityWrapper;
import sirttas.elementalcraft.container.IContainerBlockEntity;

public interface IRuntimeContainerBlockEntityRecipe<T extends IContainerBlockEntity> extends IContainerBlockEntityRecipe<T>, IRuntimeRecipe<ContainerBlockEntityWrapper<T>> {

}
