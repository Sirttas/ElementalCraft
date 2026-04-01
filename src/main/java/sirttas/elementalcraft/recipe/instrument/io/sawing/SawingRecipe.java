package sirttas.elementalcraft.recipe.instrument.io.sawing;

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
import sirttas.elementalcraft.recipe.instrument.io.IOInstrumentRecipe;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;

import javax.annotation.Nonnull;
import java.util.List;

public record SawingRecipe(
		int elementAmount,
		double luckRatio,
		Ingredient ingredient,
		ItemStack output
) implements IOInstrumentRecipe<SimpleIOInstrumentRecipeInput> {

	public static final String NAME = "sawing";
    public static final MapCodec<SawingRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(SawingRecipe::elementAmount),
            Codec.DOUBLE.optionalFieldOf(ECNames.LUCK_RATIO, 0D).forGetter(SawingRecipe::luckRatio),
            Ingredient.CODEC.fieldOf(ECNames.INGREDIENT).forGetter(SawingRecipe::ingredient),
            ItemStack.CODEC.fieldOf(ECNames.OUTPUT).forGetter(SawingRecipe::output)
    ).apply(builder, SawingRecipe::new));
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull SawingRecipe> STREAM_CODEC = StreamCodec.of(SawingRecipe::toNetwork, SawingRecipe::fromNetwork); // TODO rework recipe stream codecs

	public SawingRecipe {
		if (output.isEmpty()) {
			throw new IllegalArgumentException("Sawing recipe output must not be empty");
		}
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	@Override
	public boolean matches(@NotNull ItemStack stack, @Nonnull Level level) {
		return ingredient.test(stack) && IOInstrumentRecipe.super.matches(stack, level);
	}

	@Nonnull
    @Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(Ingredient.EMPTY, ingredient);
	}

	@Nonnull
    @Override
	public ItemStack getResultItem(@Nonnull HolderLookup.Provider provider) {
		return output;
	}

	@Nonnull
    @Override
	public RecipeSerializer<?> getSerializer() {
		return ECRecipeSerializers.SAWING.get();
	}

	@Override
	public int getLuck(SimpleIOInstrumentRecipeInput input) {
		return (int) Math.round(input.getRuneBonus(Rune.BonusType.LUCK) * luckRatio);
	}

	@Override
	public List<ElementType> getValidElementTypes() {
		return List.of(ElementType.WATER, ElementType.AIR);
	}

	@Nonnull
	@Override
	public RecipeType<?> getType() {
		return ECRecipeTypes.SAWING.get();
	}

    private static SawingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        var elementAmount = buffer.readInt();
        var luckRation = buffer.readDouble();
        var ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        var output = ItemStack.STREAM_CODEC.decode(buffer);

        return new SawingRecipe(elementAmount, luckRation, ingredient, output);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, SawingRecipe recipe) {
        buffer.writeInt(recipe.getElementAmount());
        buffer.writeDouble(recipe.luckRatio());
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient);
        ItemStack.STREAM_CODEC.encode(buffer, recipe.output);
    }
}
