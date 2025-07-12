package sirttas.elementalcraft.pureore.factory;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.pureore.factory.AbstractPureOreRecipeFactory;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;
import sirttas.elementalcraft.recipe.instrument.io.grinding.GrindingRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

import javax.annotation.Nonnull;

public class PureOreGrindingRecipeFactory extends AbstractPureOreRecipeFactory<SimpleIOInstrumentRecipeInput, IGrindingRecipe> {

    protected PureOreGrindingRecipeFactory(@Nonnull RecipeManager recipeManager) {
        super(recipeManager, ECRecipeTypes.GRINDING.get());
    }

    @Override
    public IGrindingRecipe create(@Nonnull RegistryAccess registry, @NotNull IGrindingRecipe recipe, @NotNull Ingredient ingredient) {
        return new GrindingRecipe(recipe.getElementAmount(), recipe instanceof GrindingRecipe grindingRecipe ? grindingRecipe.luckRatio() : 0, ingredient, getRecipeOutput(registry, recipe));
    }
}
