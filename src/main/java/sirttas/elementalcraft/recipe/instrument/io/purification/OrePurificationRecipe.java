package sirttas.elementalcraft.recipe.instrument.io.purification;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.SingleElementInstrumentRecipe;
import sirttas.elementalcraft.recipe.instrument.io.IOInstrumentRecipe;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;

import javax.annotation.Nonnull;

public record OrePurificationRecipe(
        Ingredient input,
        ItemStack output,
        int elementAmount,
        int inputSize,
        double luckRatio
) implements IOInstrumentRecipe<SimpleIOInstrumentRecipeInput>, SingleElementInstrumentRecipe<SimpleIOInstrumentRecipeInput> {

    public static final String NAME = "ore_purification";
    public static final MapCodec<OrePurificationRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Ingredient.CODEC.fieldOf(ECNames.INPUT).forGetter(OrePurificationRecipe::input),
            ItemStack.CODEC.fieldOf(ECNames.OUTPUT).forGetter(OrePurificationRecipe::output),
            Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(OrePurificationRecipe::elementAmount),
            Codec.INT.optionalFieldOf("input_size", 1).forGetter(OrePurificationRecipe::inputSize),
            Codec.DOUBLE.optionalFieldOf(ECNames.LUCK_RATIO, 0D).forGetter(OrePurificationRecipe::luckRatio)
    ).apply(builder, OrePurificationRecipe::new));
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull OrePurificationRecipe> STREAM_CODEC = StreamCodec.of(OrePurificationRecipe::toNetwork, OrePurificationRecipe::fromNetwork);

    @Override
    public boolean matches(@NotNull ItemStack stack, @Nonnull Level level) {
        return getIngredients().stream().allMatch(i -> i.test(stack)) && !this.getResultItem(level.registryAccess()).isEmpty() && IOInstrumentRecipe.super.matches(stack, level);
    }

    @Override
    public @NotNull ElementType getElementType() {
        return ElementType.EARTH;
    }

    @Override
    public int getLuck(SimpleIOInstrumentRecipeInput input) {
        return (int) Math.round(input.getRuneBonus(Rune.BonusType.LUCK) * luckRatio);
    }

    @Nonnull
    @Override
    public RecipeType<?> getType() {
        return ECRecipeTypes.ORE_PURIFICATION.get();
    }

    @Nonnull
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, input);
    }

    @Override
    public @NotNull RecipeSerializer<@NotNull OrePurificationRecipe> getSerializer() {
        return ECRecipeSerializers.ORE_PURIFICATION.get();
    }

    @Nonnull
    @Override
    public ItemStack getResultItem(@Nonnull HolderLookup.Provider provider) {
        return output;
    }

    @Override
    public int getElementAmount() {
        return elementAmount;
    }

    @Override
    public int getInputSize() {
        return inputSize;
    }

    private static OrePurificationRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        var input = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        var output = ItemStack.STREAM_CODEC.decode(buffer);
        var elementAmount = buffer.readInt();
        var inputSize = buffer.readInt();
        var luckRatio = buffer.readDouble();

        return new OrePurificationRecipe(input, output, elementAmount, inputSize, luckRatio);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, OrePurificationRecipe recipe) {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.input());
        ItemStack.STREAM_CODEC.encode(buffer, recipe.output);
        buffer.writeInt(recipe.elementAmount());
        buffer.writeInt(recipe.inputSize());
        buffer.writeDouble(recipe.luckRatio());
    }
}
