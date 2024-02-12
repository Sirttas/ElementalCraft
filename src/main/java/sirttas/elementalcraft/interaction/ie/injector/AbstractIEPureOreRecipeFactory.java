package sirttas.elementalcraft.interaction.ie.injector;

import blusunrize.immersiveengineering.api.crafting.IESerializableRecipe;
import blusunrize.immersiveengineering.common.crafting.GeneratedListRecipe;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.pureore.factory.AbstractPureOreRecipeFactory;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.Objects;

@SuppressWarnings("unchecked")
public abstract class AbstractIEPureOreRecipeFactory<T extends IESerializableRecipe> extends AbstractPureOreRecipeFactory<Container, IESerializableRecipe> {

    private final Class<T> recipeClass;

    protected AbstractIEPureOreRecipeFactory(@Nonnull RecipeManager recipeManager, RecipeType<T> recipeType, @Nonnull Class<T> recipeClass) {
        super(recipeManager, (RecipeType<IESerializableRecipe>) recipeType);
        this.recipeClass = recipeClass;
    }

    @Override
    public final IESerializableRecipe create(@NotNull RegistryAccess registry, @NotNull IESerializableRecipe recipe, @NotNull Ingredient ingredient) {
        if (recipeClass.isInstance(recipe)) {
            return buildIERecipe(recipeClass.cast(recipe), ingredient);
        } else if (recipe instanceof GeneratedListRecipe<?, ?> generatedListRecipe) {
            return generatedListRecipe.getSubRecipes().stream()
                    .map(r -> this.create(registry, r, ingredient))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    protected abstract T buildIERecipe(T recipe, Ingredient ingredient);

    @Override
    public final boolean filter(RecipeHolder<IESerializableRecipe> holder, ItemStack stack) {
        var recipe = holder.value();
        var id = holder.id();

        try {
            if (!stack.is(ECTags.Items.PURE_ORES_SOURCE_RAW_MATERIALS)) {
                return false;
            } else if (recipeClass.isInstance(recipe)) {
                return filterIERecipe(recipeClass.cast(recipe), stack);
            } else if (recipe instanceof GeneratedListRecipe<?, ?> generatedListRecipe) {
                return generatedListRecipe.getSubRecipes().stream()
                        .anyMatch(r -> this.filter(new RecipeHolder<>(id, r), stack));
            }
        } catch (Exception e) {
            ElementalCraftApi.LOGGER.warn("Error while filtering immersive engineering recipe {} for pure ore", id, e);
        }
        return false;
    }

    protected abstract boolean filterIERecipe(T recipe, ItemStack stack);
}
