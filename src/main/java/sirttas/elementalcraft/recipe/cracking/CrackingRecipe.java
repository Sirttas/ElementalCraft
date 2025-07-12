package sirttas.elementalcraft.recipe.cracking;

import net.minecraft.core.HolderSet;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.ECRecipeTypes;

public class CrackingRecipe extends AbstractCrackingRecipe {

    public static final String NAME = "cracking";

    public CrackingRecipe(HolderSet<Block> input, Block result, int elementAmount) {
        super(input, result, elementAmount);
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ECRecipeSerializers.CRACKING.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ECRecipeTypes.CRACKING.get();
    }
}
