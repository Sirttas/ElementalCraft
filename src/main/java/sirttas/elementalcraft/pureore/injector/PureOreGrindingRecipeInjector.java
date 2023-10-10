package sirttas.elementalcraft.pureore.injector;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.AirMillGrindstoneBlockEntity;
import sirttas.elementalcraft.container.ContainerBlockEntityWrapper;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.io.grinding.GrindingRecipe;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

import javax.annotation.Nonnull;

public class PureOreGrindingRecipeInjector extends AbstractPureOreRecipeInjector<ContainerBlockEntityWrapper<AirMillGrindstoneBlockEntity>, IGrindingRecipe> {

    protected PureOreGrindingRecipeInjector() {
        super(ECRecipeTypes.GRINDING.get(), true);
    }

    @Override
    public IGrindingRecipe build(@Nonnull RegistryAccess registry, @NotNull IGrindingRecipe recipe, @NotNull Ingredient ingredient) {
        return new GrindingRecipe(buildRecipeId(recipe.getId()), ingredient, getRecipeOutput(registry, recipe), recipe.getElementAmount(), recipe instanceof GrindingRecipe airMillGrindingRecipe ? airMillGrindingRecipe.luckRation() : 0);
    }
}
