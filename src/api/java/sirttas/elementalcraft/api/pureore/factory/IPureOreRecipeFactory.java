package sirttas.elementalcraft.api.pureore.factory;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.pureore.PureOreException;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

public interface IPureOreRecipeFactory<C extends RecipeInput, T extends Recipe<@NotNull C>> {

    default boolean filter(RecipeHolder<T> holder, ItemStack stack) {
        try {
            return holder.value().placementInfo().ingredients().getFirst().test(stack);
        } catch (Exception e) {
            throw new PureOreException(MessageFormat.format("Error while reading ingredients for recipe {0}. Please setup a custom filter for {1}", holder.id(), this), e);
        }
    }

    @Deprecated
    ItemStack getRecipeOutput(@Nonnull RegistryAccess registry, @Nonnull T recipe);

    T create(@Nonnull RegistryAccess registry, @Nonnull T recipe, @Nonnull Ingredient ingredient);

    List<RecipeHolder<@NotNull T>> getRecipes(Collection<Holder<@NotNull Item>> ores);
    RecipeType<@NotNull T> getRecipeType();
}
