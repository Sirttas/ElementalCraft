package sirttas.elementalcraft.recipe.instrument;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.crafting.Recipe;
import sirttas.elementalcraft.recipe.input.RandomSourceRecipeInput;

public interface ILuckRecipe<I extends RandomSourceRecipeInput> extends Recipe<I> {

    default RandomSource getRandomSource(I input) {
        return input.getRandomSource();
    }

    default int getLuck(I input) {
        return 0;
    }
}
