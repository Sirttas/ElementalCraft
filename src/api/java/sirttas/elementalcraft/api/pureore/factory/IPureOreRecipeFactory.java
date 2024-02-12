package sirttas.elementalcraft.api.pureore.factory;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

public interface IPureOreRecipeFactory<C extends Container, T extends Recipe<C>> {
    boolean filter(RecipeHolder<T> holder, ItemStack stack);

    ItemStack getRecipeOutput(@Nonnull RegistryAccess registry, @Nonnull T recipe);

    T create(@Nonnull RegistryAccess registry, @Nonnull T recipe, @Nonnull Ingredient ingredient);

    List<RecipeHolder<T>> getRecipes(Collection<Item> ores);
    RecipeType<T> getRecipeType();
}
