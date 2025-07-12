package sirttas.elementalcraft.recipe.instrument.io.grinding;

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
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;

import javax.annotation.Nonnull;

public record GrindingRecipe(
		int elementAmount,
		double luckRatio,
		Ingredient ingredient,
		ItemStack output
) implements IGrindingRecipe {

	public GrindingRecipe {
		if (output.isEmpty()) {
			throw new IllegalArgumentException("Grinding recipe output must not be empty");
		}
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}
	@Override
	public boolean matches(@NotNull ItemStack stack, @Nonnull Level level) {
		return ingredient.test(stack) && IGrindingRecipe.super.matches(stack, level);
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
		return ECRecipeSerializers.GRINDING.get();
	}

	@Override
	public int getLuck(SimpleIOInstrumentRecipeInput input) {
		return (int) Math.round(input.getRuneBonus(Rune.BonusType.LUCK) * luckRatio);
	}

	public static class Serializer implements RecipeSerializer<GrindingRecipe> {

		public static final MapCodec<GrindingRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
				Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(GrindingRecipe::elementAmount),
				Codec.DOUBLE.optionalFieldOf(ECNames.LUCK_RATIO, 0D).forGetter(GrindingRecipe::luckRatio),
				Ingredient.CODEC.fieldOf(ECNames.INGREDIENT).forGetter(GrindingRecipe::ingredient),
				ItemStack.CODEC.fieldOf(ECNames.OUTPUT).forGetter(GrindingRecipe::output)
		).apply(builder, GrindingRecipe::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, GrindingRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

		@Override
		@Nonnull
		public MapCodec<GrindingRecipe> codec() {
			return CODEC;
		}

		@Override
		public @NotNull StreamCodec<RegistryFriendlyByteBuf, GrindingRecipe> streamCodec() {
			return STREAM_CODEC;
		}

		public static GrindingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
			var elementAmount = buffer.readInt();
			var luckRatio = buffer.readDouble();
			var ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
			var output = ItemStack.STREAM_CODEC.decode(buffer);

			return new GrindingRecipe(elementAmount, luckRatio, ingredient, output);
		}

		public static void toNetwork(RegistryFriendlyByteBuf buffer, GrindingRecipe recipe) {
			buffer.writeInt(recipe.getElementAmount());
			buffer.writeDouble(recipe.luckRatio());
			Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient);
			ItemStack.STREAM_CODEC.encode(buffer, recipe.output);
		}

	}
}
