package sirttas.elementalcraft.recipe.source.breeding;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.recipe.IECRecipe;
import sirttas.elementalcraft.recipe.IRuntimeRecipe;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;

public class SourceBreedingRecipe implements IECRecipe<SourceBreedingRecipeInput>, IRuntimeRecipe<SourceBreedingRecipeInput> {

    @Override
    public boolean matches(@NotNull SourceBreedingRecipeInput input, @NotNull Level level) {
        var seed = input.seed();
        var type = input.getElementType();
        var pedestals = input.pedestalInputs();

        return seed.is(ECTags.Items.SOURCE_SEEDS) && pedestals.size() == 2 && pedestals.stream().allMatch(p -> !p.getItem(0).isEmpty() && p.getElementType() == type);
    }

    @Override
    public @NotNull ItemStack getResultItem(@Nonnull HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }
}
