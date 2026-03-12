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

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

public interface IPureOreRecipeFactory<C extends RecipeInput, T extends Recipe<@NotNull C>> {

    boolean filter(RecipeHolder<@NotNull T> holder, ItemStack stack);

    ItemStack getRecipeOutput(@Nonnull RegistryAccess registry, @Nonnull T recipe);

    T create(@Nonnull RegistryAccess registry, @Nonnull T recipe, @Nonnull Ingredient ingredient);

    List<RecipeHolder<@NotNull T>> getRecipes(Collection<Holder<@NotNull Item>> ores);
    RecipeType<@NotNull T> getRecipeType();
}
