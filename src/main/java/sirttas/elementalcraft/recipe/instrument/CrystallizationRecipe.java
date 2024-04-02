package sirttas.elementalcraft.recipe.instrument;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.instrument.crystallizer.CrystallizerBlockEntity;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.ECRecipeTypes;

import javax.annotation.Nonnull;
import java.util.List;

public class CrystallizationRecipe extends AbstractInstrumentRecipe<CrystallizerBlockEntity> {

	public static final String NAME = "crystallization";

	private static final Codec<List<Ingredient>> INGREDIENTS_CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Ingredient.CODEC.fieldOf(ECNames.GEM).forGetter(i -> i.get(0)),
			Ingredient.CODEC.fieldOf(ECNames.CRYSTAL).forGetter(i -> i.get(1))
	).apply(builder, List::of));

	public static final Codec<CrystallizationRecipe> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			ElementType.CODEC.fieldOf(ECNames.ELEMENT_TYPE).forGetter(CrystallizationRecipe::getElementType),
			Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(CrystallizationRecipe::getElementAmount),
			INGREDIENTS_CODEC.fieldOf(ECNames.INGREDIENTS).forGetter(CrystallizationRecipe::getIngredients),
			ItemStack.CODEC.fieldOf(ECNames.RESULT).forGetter(r -> r.result)
	).apply(builder, CrystallizationRecipe::new));
	
	private final NonNullList<Ingredient> ingredients;
	private final ItemStack result;
	private final int elementAmount;

	public CrystallizationRecipe(ElementType type, int elementAmount, List<Ingredient> ingredients, ItemStack result) {
		super(type);
		this.ingredients = NonNullList.of(Ingredient.EMPTY, ingredients.toArray(Ingredient[]::new));
		this.result = result;
		this.elementAmount = elementAmount;
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	@Override
	public boolean matches(@Nonnull CrystallizerBlockEntity crystallizer, @Nonnull Level level) {
		if (crystallizer.getContainerElementType() == getElementType() && crystallizer.getItemCount() >= 2) {
			for (int i = 0; i < 2; i++) {
				if (!ingredients.get(i).test(crystallizer.getInventory().getItem(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}

	@Nonnull
	@Override
	public ItemStack getResultItem(@Nonnull RegistryAccess registry) {
		return result;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Nonnull
	@Override
	public RecipeType<?> getType() {
		return ECRecipeTypes.CRYSTALLIZATION.get();
	}
	
	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return ECRecipeSerializers.CRYSTALLIZATION.get();
	}

	public static class Serializer implements RecipeSerializer<CrystallizationRecipe> {

		@Override
		@Nonnull
		public Codec<CrystallizationRecipe> codec() {
			return CODEC;
		}

		@Override
		public CrystallizationRecipe fromNetwork(FriendlyByteBuf buffer) {
			ElementType type = ElementType.byName(buffer.readUtf());
			int elementAmount = buffer.readInt();
			var output = buffer.readItem();
			
			int i = buffer.readInt();
			NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);

			for (int j = 0; j < i; ++j) {
				ingredients.set(j, Ingredient.fromNetwork(buffer));
			}

			return new CrystallizationRecipe(type, elementAmount, ingredients, output);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, CrystallizationRecipe recipe) {
			buffer.writeUtf(recipe.getElementType().getSerializedName());
			buffer.writeInt(recipe.getElementAmount());
			buffer.writeItem(recipe.result);
			buffer.writeInt(recipe.getIngredients().size());
			recipe.getIngredients().forEach(ingredient -> ingredient.toNetwork(buffer));
		}
	}
}
