package sirttas.elementalcraft.interaction.ie.injector;

import blusunrize.immersiveengineering.api.crafting.IESerializableRecipe;
import blusunrize.immersiveengineering.common.crafting.GeneratedListRecipe;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;
import sirttas.elementalcraft.tag.ECTags;

import java.util.Objects;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public abstract class AbstractIEPureOreRecipeInjector<T extends IESerializableRecipe> extends AbstractPureOreRecipeInjector<Container, IESerializableRecipe> {

    private final Class<T> recipeClass;

    protected AbstractIEPureOreRecipeInjector(Supplier<RecipeType<T>> recipeType, Class<T> recipeClass) {
        super(() -> (RecipeType<IESerializableRecipe>) recipeType.get(), true);
        this.recipeClass = recipeClass;
    }

    @Override
    public final IESerializableRecipe build(IESerializableRecipe recipe, Ingredient ingredient) {
        if (recipeClass.isInstance(recipe)) {
            return buildIERecipe(recipeClass.cast(recipe), ingredient);
        } else if (recipe instanceof GeneratedListRecipe<?, ?> generatedListRecipe) {
            return generatedListRecipe.getSubRecipes().stream()
                    .map(r -> this.build(r, ingredient))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    protected abstract T buildIERecipe(T recipe, Ingredient ingredient);

    @Override
    public final boolean filter(IESerializableRecipe recipe, ItemStack stack) {
        try {
            if (!stack.is(ECTags.Items.PURE_ORES_SOURCE_RAW_MATERIALS)) {
                return false;
            } else if (recipeClass.isInstance(recipe)) {
                return filterIERecipe(recipeClass.cast(recipe), stack);
            } else if (recipe instanceof GeneratedListRecipe<?, ?> generatedListRecipe) {
                return generatedListRecipe.getSubRecipes().stream()
                        .anyMatch(r -> this.filter(r, stack));
            }
        } catch (Exception e) {
            ElementalCraftApi.LOGGER.warn("Error while filtering immersive engineering recipe {} for pure ore", recipe.getId(), e);
        }
        return false;
    }

    protected abstract boolean filterIERecipe(T recipe, ItemStack stack);
}
