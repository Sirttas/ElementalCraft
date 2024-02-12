package sirttas.elementalcraft.recipe.instrument.io.grinding;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.AbstractMillGrindstoneBlockEntity;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;

import javax.annotation.Nonnull;

public record GrindingRecipe(
		int elementAmount,
		int luckRation,
		Ingredient ingredient,
		ItemStack output
) implements IGrindingRecipe {

	public static final Codec<GrindingRecipe> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(GrindingRecipe::elementAmount),
			Codec.INT.fieldOf(ECNames.LUCK_RATIO).forGetter(GrindingRecipe::luckRation),
			Ingredient.CODEC.fieldOf(ECNames.INGREDIENT).forGetter(GrindingRecipe::ingredient),
			ItemStack.CODEC.fieldOf(ECNames.OUTPUT).forGetter(GrindingRecipe::output)
	).apply(builder, GrindingRecipe::new));

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
	public boolean matches(ItemStack stack, @Nonnull Level level) {
		return ingredient.test(stack) && IGrindingRecipe.super.matches(stack, level);
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
		return ECRecipeSerializers.GRINDING.get();
	}

	@Override
	public int getLuck(AbstractMillGrindstoneBlockEntity instrument) {
		return Math.round(instrument.getRuneHandler().getBonus(Rune.BonusType.LUCK) * luckRation);
	}

	public static class Serializer implements RecipeSerializer<GrindingRecipe> {

		@Override
		@Nonnull
		public Codec<GrindingRecipe> codec() {
			return CODEC;
		}

		@Override
		public GrindingRecipe fromNetwork(FriendlyByteBuf buffer) {
			int elementAmount = buffer.readInt();
			int luckRation = buffer.readInt();
			Ingredient ingredient = Ingredient.fromNetwork(buffer);
			ItemStack output = buffer.readItem();

			return new GrindingRecipe(elementAmount, luckRation, ingredient, output);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, GrindingRecipe recipe) {
			buffer.writeInt(recipe.getElementAmount());
			buffer.writeInt(recipe.luckRation());
			recipe.getIngredients().get(0).toNetwork(buffer);
			buffer.writeItem(recipe.output);
		}

	}
}
