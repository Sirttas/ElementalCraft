package sirttas.elementalcraft.recipe.cracking;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.recipe.ECRecipeBookCategories;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.ingredient.BlockHolderSetIngredient;

public class CrackingRecipe extends AbstractCrackingRecipe {

    public static final String NAME = "cracking";
    public static final MapCodec<CrackingRecipe> CODEC = codec(CrackingRecipe::new);
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull CrackingRecipe> STREAM_CODEC = streamCodec(CrackingRecipe::new);

    public CrackingRecipe(Recipe.CommonInfo commonInfo, BlockHolderSetIngredient input, Block result, int elementAmount) {
        super(commonInfo, input, result, elementAmount);
    }

    @Override
    public @NotNull String group() {
        return NAME;
    }

    @Override
    public @NotNull RecipeSerializer<@NotNull CrackingRecipe> getSerializer() {
        return ECRecipeSerializers.CRACKING.get();
    }

    @Override
    public @NotNull RecipeType<@NotNull CrackingRecipe> getType() {
        return ECRecipeTypes.CRACKING.get();
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return ECRecipeBookCategories.CRACKING.get();
    }
}
