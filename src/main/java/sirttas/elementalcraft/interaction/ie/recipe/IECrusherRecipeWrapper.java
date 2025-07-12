package sirttas.elementalcraft.interaction.ie.recipe;

import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

import javax.annotation.Nonnull;

public class IECrusherRecipeWrapper implements IGrindingRecipe {

    private final CrusherRecipe crushingRecipe;

    public IECrusherRecipeWrapper(CrusherRecipe crushingRecipe) {
        this.crushingRecipe = crushingRecipe;
    }

    @Override
    public int getElementAmount() {
        return 1000;
    }

    @Nonnull
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return crushingRecipe.getIngredients();
    }

    @Nonnull
    @Override
    public ItemStack getResultItem(@Nonnull HolderLookup.Provider provider) {
        return crushingRecipe.getResultItem(provider);
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return crushingRecipe.getSerializer();
    }

    @Override
    public boolean matches(@NotNull ItemStack stack, @NotNull Level level) {
        return crushingRecipe.input.test(stack) && IGrindingRecipe.super.matches(stack, level);
    }

    @Nonnull
    @Override
    public @NotNull ItemStack assemble(@Nonnull SimpleIOInstrumentRecipeInput input, @Nonnull HolderLookup.Provider provider) {
        var luck = getLuck(input);
        var result = IGrindingRecipe.super.assemble(input, provider);
        var rand = getRandomSource(input);

        crushingRecipe.secondaryOutputs.forEach(output -> {
            var stack = output.stack().get();

            if (ItemStack.isSameItemSameComponents(stack, result) && rand.nextFloat() < output.chance() * luck) {
                result.grow(stack.getCount());
            }
        });
        return result;
    }
}
