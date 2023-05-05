package sirttas.elementalcraft.pureore.injector;

import net.minecraft.world.item.crafting.Ingredient;
import sirttas.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.AirMillGrindstoneBlockEntity;
import sirttas.elementalcraft.container.ContainerBlockEntityWrapper;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.io.grinding.GrindingRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

public class PureOreGrindingRecipeInjector extends AbstractPureOreRecipeInjector<ContainerBlockEntityWrapper<AirMillGrindstoneBlockEntity>, IGrindingRecipe> {

    protected PureOreGrindingRecipeInjector() {
        super(ECRecipeTypes.GRINDING.get(), true);
    }

    @Override
    public IGrindingRecipe build(IGrindingRecipe recipe, Ingredient ingredient) {
        return new GrindingRecipe(buildRecipeId(recipe.getId()), ingredient, getRecipeOutput(recipe), recipe.getElementAmount(), recipe instanceof GrindingRecipe airMillGrindingRecipe ? airMillGrindingRecipe.luckRation() : 0);
    }
}
