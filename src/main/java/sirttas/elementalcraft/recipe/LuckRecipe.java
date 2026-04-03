package sirttas.elementalcraft.recipe;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.recipe.input.RandomSourceRecipeInput;

public interface LuckRecipe<I extends RandomSourceRecipeInput> extends Recipe<@NotNull I> {

    default RandomSource getRandomSource(I input) {
        return input.getRandomSource();
    }

    default int getLuck(I input) {
        return 0;
    }
}
