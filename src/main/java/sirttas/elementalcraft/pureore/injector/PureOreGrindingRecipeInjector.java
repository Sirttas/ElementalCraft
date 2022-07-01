package sirttas.elementalcraft.pureore.injector;

import net.minecraft.world.item.crafting.Ingredient;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;
import sirttas.elementalcraft.block.instrument.io.mill.AirMillBlockEntity;
import sirttas.elementalcraft.container.ContainerBlockEntityWrapper;
import sirttas.elementalcraft.recipe.instrument.io.grinding.AirMillGrindingRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

public class PureOreGrindingRecipeInjector extends AbstractPureOreRecipeInjector<ContainerBlockEntityWrapper<AirMillBlockEntity>, IGrindingRecipe> {

    protected PureOreGrindingRecipeInjector() {
        super(IGrindingRecipe.TYPE, true);
    }

    @Override
    public IGrindingRecipe build(IGrindingRecipe recipe, Ingredient ingredient) {
        return new AirMillGrindingRecipe(ElementalCraft.createRL(buildRecipeId(recipe.getId())), ingredient, getRecipeOutput(recipe), recipe.getElementAmount(), recipe instanceof AirMillGrindingRecipe airMillGrindingRecipe ? airMillGrindingRecipe.luckRation() : 0);
    }
}
