package sirttas.elementalcraft.interaction.ie.injector;

import blusunrize.immersiveengineering.api.crafting.ArcFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.IERecipeTypes;
import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.util.Lazy;

public class ArcFurnacePureOreRecipeInjector extends AbstractIEPureOreRecipeInjector<ArcFurnaceRecipe> {

    public ArcFurnacePureOreRecipeInjector() {
        super(IERecipeTypes.ARC_FURNACE, ArcFurnaceRecipe.class);
    }

    @Override
    public ArcFurnaceRecipe buildIERecipe(ArcFurnaceRecipe recipe, Ingredient ingredient) {
        var output = recipe.output.get().stream()
                .map(s -> Lazy.of(() -> s))
                .toList();

        return new ArcFurnaceRecipe(
                buildRecipeId(recipe.getId()),
                output,
                recipe.slag,
                recipe.secondaryOutputs,
                recipe.getTotalProcessTime(),
                recipe.getTotalProcessEnergy(),
                new IngredientWithSize(ingredient, recipe.input.getCount()),
                recipe.additives
        );
    }

    @Override
    public boolean filterIERecipe(ArcFurnaceRecipe recipe, ItemStack stack) {
        return recipe.input.test(stack);
    }
}
