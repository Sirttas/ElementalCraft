package sirttas.elementalcraft.recipe.instrument.io;

import net.minecraft.world.item.crafting.SingleRecipeInput;
import sirttas.elementalcraft.recipe.input.ElementRecipeInput;
import sirttas.elementalcraft.recipe.input.RandomSourceRecipeInput;

public interface IOInstrumentRecipeInput extends ElementRecipeInput, RandomSourceRecipeInput {

    default SingleRecipeInput toSingleRecipeInput() {
        return new SingleRecipeInput(getItem(0));
    }
}
