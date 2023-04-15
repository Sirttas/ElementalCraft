package sirttas.elementalcraft.recipe.instrument.io.grinding;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.AirMillGrindstoneBlockEntity;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.RecipeHelper;

import javax.annotation.Nonnull;

public record AirMillGrindingRecipe(
		ResourceLocation id,
		Ingredient ingredient,
		ItemStack output,
		int elementAmount,
		int luckRation
) implements IGrindingRecipe {

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
	public boolean matches(ItemStack stack) {
		return ingredient.test(stack) && IGrindingRecipe.super.matches(stack);
	}

	@Nonnull
    @Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(Ingredient.EMPTY, ingredient);
	}

	@Nonnull
    @Override
	public ItemStack getResultItem() {
		return output;
	}

	@Nonnull
    @Override
	public RecipeSerializer<?> getSerializer() {
		return ECRecipeSerializers.AIR_MILL_GRINDING.get();
	}

	@Override
	public int getLuck(AirMillGrindstoneBlockEntity instrument) {
		return Math.round(instrument.getRuneHandler().getBonus(Rune.BonusType.LUCK) * luckRation);
	}

	public static class Serializer implements RecipeSerializer<AirMillGrindingRecipe> {

		@Nonnull
        @Override
		public AirMillGrindingRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			int elementAmount = GsonHelper.getAsInt(json, ECNames.ELEMENT_AMOUNT);
			Ingredient ingredient = RecipeHelper.deserializeIngredient(json, ECNames.INPUT);
			ItemStack output = RecipeHelper.readRecipeOutput(json, ECNames.OUTPUT);
			int luckRation = GsonHelper.getAsInt(json, ECNames.LUCK_RATIO);

			if (!output.isEmpty()) {
				return new AirMillGrindingRecipe(recipeId, ingredient, output, elementAmount, luckRation);
			}
			throw new IllegalStateException("Grinding recipe output is empty!");
		}

		@Override
		public AirMillGrindingRecipe fromNetwork(@Nonnull ResourceLocation recipeId, FriendlyByteBuf buffer) {
			int elementAmount = buffer.readInt();
			Ingredient ingredient = Ingredient.fromNetwork(buffer);
			ItemStack output = buffer.readItem();
			int luckRation = buffer.readInt();

			return new AirMillGrindingRecipe(recipeId, ingredient, output, elementAmount, luckRation);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, AirMillGrindingRecipe recipe) {
			buffer.writeInt(recipe.getElementAmount());
			recipe.getIngredients().get(0).toNetwork(buffer);
			buffer.writeItem(recipe.getResultItem());
			buffer.writeInt(recipe.luckRation());
		}

	}
}
