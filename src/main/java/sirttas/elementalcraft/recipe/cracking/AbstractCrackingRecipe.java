package sirttas.elementalcraft.recipe.cracking;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.recipe.IECRecipe;

public abstract class AbstractCrackingRecipe implements IECRecipe<CrackingRecipeInput> {

    private final HolderSet<Block> input;
    private final Block result;
    private final int elementAmount;

    protected AbstractCrackingRecipe(HolderSet<Block> input, Block result, int elementAmount) {
        this.input = input;
        this.result = result;
        this.elementAmount = elementAmount;
    }

    @Override
    public boolean matches(@NotNull CrackingRecipeInput recipeInput, @NotNull Level level) {
        return recipeInput.state().is(input);
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    public HolderSet<Block> input() {
        return input;
    }

    public Block result() {
        return result;
    }

    public int elementAmount() {
        return elementAmount;
    }

    public boolean hasResult() {
        return !result.defaultBlockState().isAir();
    }

    public interface Factory<T extends AbstractCrackingRecipe> {
        T create(HolderSet<Block> input, Block result, int elementAmount);
    }

}
