package sirttas.elementalcraft.recipe.instrument;

import net.minecraft.util.RandomSource;
import sirttas.elementalcraft.recipe.IECRecipe;
import sirttas.elementalcraft.recipe.input.RandomSourceRecipeInput;

public interface ILuckRecipe<I extends RandomSourceRecipeInput> extends IECRecipe<I> {

    default RandomSource getRandomSource(I input) {
        return input.getRandomSource();
    }

    default int getLuck(I input) {
        return 0;
    }
}
