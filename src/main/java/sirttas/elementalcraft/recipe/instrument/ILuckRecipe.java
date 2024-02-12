package sirttas.elementalcraft.recipe.instrument;

import net.minecraft.util.RandomSource;
import sirttas.elementalcraft.container.IContainerBlockEntity;
import sirttas.elementalcraft.recipe.IContainerBlockEntityRecipe;

public interface ILuckRecipe<T extends IContainerBlockEntity> extends IContainerBlockEntityRecipe<T> {

    default RandomSource getRand(T be) {
        return RandomSource.create();
    }

    default int getLuck(T instrument) {
        return 0;
    }


}
