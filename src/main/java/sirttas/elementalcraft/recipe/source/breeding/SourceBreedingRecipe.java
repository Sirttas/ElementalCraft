package sirttas.elementalcraft.recipe.source.breeding;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.recipe.RuntimeRecipe;
import sirttas.elementalcraft.tag.ECTags;

public class SourceBreedingRecipe implements RuntimeRecipe<SourceBreedingRecipeInput> {

    @Override
    public boolean matches(@NotNull SourceBreedingRecipeInput input, @NotNull Level level) {
        var seed = input.seed();
        var type = input.getElementType();
        var pedestals = input.pedestalInputs();

        return seed.is(ECTags.Items.SOURCE_SEEDS) && pedestals.size() == 2 && pedestals.stream().allMatch(p -> !p.getItem(0).isEmpty() && p.getElementType() == type);
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SourceBreedingRecipeInput input) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
