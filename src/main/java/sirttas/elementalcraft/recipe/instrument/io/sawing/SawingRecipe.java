package sirttas.elementalcraft.recipe.instrument.io.sawing;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.block.instrument.io.mill.woodsaw.WaterMillWoodSawBlockEntity;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.RecipeHelper;
import sirttas.elementalcraft.recipe.instrument.io.IIOInstrumentRecipe;

import javax.annotation.Nonnull;
import java.util.List;

public record SawingRecipe(
		ResourceLocation id,
		Ingredient ingredient,
		ItemStack output,
		int elementAmount,
		int luckRation
) implements IIOInstrumentRecipe<WaterMillWoodSawBlockEntity> {

	public static final String NAME = "sawing";

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	@Nonnull
    @Override
	public ResourceLocation getId() {
		return id;
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
	public int getLuck(WaterMillWoodSawBlockEntity instrument) {
		return Math.round(instrument.getRuneHandler().getBonus(Rune.BonusType.LUCK) * luckRation);
	}

	@Override
	public List<ElementType> getValidElementTypes() {
		return List.of(ElementType.WATER);
	}

	@Nonnull
	@Override
	public RecipeType<?> getType() {
		return ECRecipeTypes.SAWING.get();
	}

	@Override
	public RandomSource getRand(WaterMillWoodSawBlockEntity instrument) {
		return instrument.getLevel().getRandom();
	}

	public static class Serializer implements RecipeSerializer<SawingRecipe> {

		@Nonnull
        @Override
		public SawingRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			int elementAmount = GsonHelper.getAsInt(json, ECNames.ELEMENT_AMOUNT);
			Ingredient ingredient = RecipeHelper.deserializeIngredient(json, ECNames.INPUT);
			ItemStack output = RecipeHelper.readRecipeOutput(json, ECNames.OUTPUT);
			int luckRation = GsonHelper.getAsInt(json, ECNames.LUCK_RATIO);

			if (!output.isEmpty()) {
				return new SawingRecipe(recipeId, ingredient, output, elementAmount, luckRation);
			}
			throw new IllegalStateException("Sawing recipe output is empty!");
		}

		@Override
		public SawingRecipe fromNetwork(@Nonnull ResourceLocation recipeId, FriendlyByteBuf buffer) {
			int elementAmount = buffer.readInt();
			Ingredient ingredient = Ingredient.fromNetwork(buffer);
			ItemStack output = buffer.readItem();
			int luckRation = buffer.readInt();

			return new SawingRecipe(recipeId, ingredient, output, elementAmount, luckRation);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, SawingRecipe recipe) {
			buffer.writeInt(recipe.getElementAmount());
			recipe.getIngredients().get(0).toNetwork(buffer);
			buffer.writeItem(recipe.output);
			buffer.writeInt(recipe.luckRation());
		}

	}
}
