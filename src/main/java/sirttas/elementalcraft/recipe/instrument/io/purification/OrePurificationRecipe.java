package sirttas.elementalcraft.recipe.instrument.io.purification;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.recipe.ECRecipeBookCategories;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.SingleElementInstrumentRecipe;
import sirttas.elementalcraft.recipe.instrument.io.AbstractIOInstrumentRecipe;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;

import javax.annotation.Nonnull;

public class OrePurificationRecipe extends AbstractIOInstrumentRecipe implements SingleElementInstrumentRecipe<SimpleIOInstrumentRecipeInput> {

    public static final String NAME = "ore_purification";
    public static final MapCodec<OrePurificationRecipe> CODEC = codec(OrePurificationRecipe::new);
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull OrePurificationRecipe> STREAM_CODEC = streamCodec(OrePurificationRecipe::new);

    public OrePurificationRecipe(CommonInfo commonInfo, int elementAmount, double luckRatio, Ingredient ingredient, int inputSize, ItemStackTemplate output) {
        super(commonInfo, elementAmount, luckRatio, ingredient, inputSize, output);
    }

    @Override
    public @NotNull String group() {
        return NAME;
    }

    @Nonnull
    @Override
    public RecipeType<@NotNull OrePurificationRecipe> getType() {
        return ECRecipeTypes.ORE_PURIFICATION.get();
    }

    @Nonnull
    @Override
    public RecipeSerializer<@NotNull OrePurificationRecipe> getSerializer() {
        return ECRecipeSerializers.ORE_PURIFICATION.get();
    }


    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return ECRecipeBookCategories.ORE_PURIFICATION.get();
    }

    @Override
    public @NotNull ElementType getElementType() {
        return ElementType.EARTH;
    }
}
