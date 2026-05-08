package sirttas.elementalcraft.pureore.factory;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import sirttas.elementalcraft.api.pureore.factory.AbstractPureOreRecipeFactory;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;
import sirttas.elementalcraft.recipe.instrument.io.grinding.GrindingRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.SimpleGrindingRecipe;

public class PureOreGrindingRecipeFactory extends AbstractPureOreRecipeFactory<SimpleIOInstrumentRecipeInput, GrindingRecipe> {

    protected PureOreGrindingRecipeFactory(RecipeManager recipeManager) {
        super(recipeManager, ECRecipeTypes.GRINDING.get());
    }

    @Override
    public GrindingRecipe create(RegistryAccess registry, GrindingRecipe recipe, Ingredient ingredient) {
        return new SimpleGrindingRecipe(
                new Recipe.CommonInfo(recipe.showNotification()),
                recipe.getElementAmount(),
                recipe instanceof SimpleGrindingRecipe grindingRecipe ? grindingRecipe.getLuckRatio() : 0,
                ingredient,
                recipe.getInputSize(),
                ItemStackTemplate.fromNonEmptyStack(recipe.assemble(SimpleIOInstrumentRecipeInput.EMPTY)));
    }
}
