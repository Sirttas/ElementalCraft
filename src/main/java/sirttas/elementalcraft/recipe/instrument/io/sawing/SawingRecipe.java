package sirttas.elementalcraft.recipe.instrument.io.sawing;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.block.instrument.io.mill.woodsaw.AbstractMillWoodSawBlockEntity;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.io.IIOInstrumentRecipe;

import javax.annotation.Nonnull;
import java.util.List;

public record SawingRecipe(
		int elementAmount,
		int luckRation,
		Ingredient ingredient,
		ItemStack output
) implements IIOInstrumentRecipe<AbstractMillWoodSawBlockEntity> {

	public static final String NAME = "sawing";

	public static final Codec<SawingRecipe> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(SawingRecipe::elementAmount),
			Codec.INT.fieldOf(ECNames.LUCK_RATIO).forGetter(SawingRecipe::luckRation),
			Ingredient.CODEC.fieldOf(ECNames.INGREDIENT).forGetter(SawingRecipe::ingredient),
			ItemStack.CODEC.fieldOf(ECNames.OUTPUT).forGetter(SawingRecipe::output)
	).apply(builder, SawingRecipe::new));

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
	public boolean matches(ItemStack stack, @Nonnull Level level) {
		return ingredient.test(stack) && IIOInstrumentRecipe.super.matches(stack, level);
	}

	@Nonnull
    @Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(Ingredient.EMPTY, ingredient);
	}

	@Nonnull
    @Override
	public ItemStack getResultItem(@Nonnull RegistryAccess registry) {
		return output;
	}

	@Nonnull
    @Override
	public RecipeSerializer<?> getSerializer() {
		return ECRecipeSerializers.SAWING.get();
	}

	@Override
	public int getLuck(AbstractMillWoodSawBlockEntity instrument) {
		return Math.round(instrument.getRuneHandler().getBonus(Rune.BonusType.LUCK) * luckRation);
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

	@Override
	public RandomSource getRand(AbstractMillWoodSawBlockEntity instrument) {
		return instrument.getLevel().getRandom();
	}

	public static class Serializer implements RecipeSerializer<SawingRecipe> {

		@Override
		@Nonnull
		public Codec<SawingRecipe> codec() {
			return CODEC;
		}

		@Override
		public SawingRecipe fromNetwork(FriendlyByteBuf buffer) {
			int elementAmount = buffer.readInt();
			int luckRation = buffer.readInt();
			Ingredient ingredient = Ingredient.fromNetwork(buffer);
			ItemStack output = buffer.readItem();

			return new SawingRecipe(elementAmount, luckRation, ingredient, output);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, SawingRecipe recipe) {
			buffer.writeInt(recipe.getElementAmount());
			buffer.writeInt(recipe.luckRation());
			recipe.getIngredients().get(0).toNetwork(buffer);
			buffer.writeItem(recipe.output);
		}

	}
}
