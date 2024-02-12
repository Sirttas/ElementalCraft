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
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.instrument.inscriber.InscriberBlockEntity;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.RecipeHelper;

import javax.annotation.Nonnull;
import java.util.List;

public class InscriptionRecipe extends AbstractInstrumentRecipe<InscriberBlockEntity> {

	public static final String NAME = "inscription";

	public static final Codec<InscriptionRecipe> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			ElementType.CODEC.fieldOf(ECNames.ELEMENT_TYPE).forGetter(InscriptionRecipe::getElementType),
			Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(InscriptionRecipe::getElementAmount),
			Ingredient.LIST_CODEC.fieldOf(ECNames.INGREDIENTS).forGetter(InscriptionRecipe::getIngredients),
			ItemStack.CODEC.fieldOf(ECNames.OUTPUT).forGetter(r -> r.output)
	).apply(builder, InscriptionRecipe::new));

	private final NonNullList<Ingredient> ingredients;
	private final int elementAmount;
	private final ItemStack output;

	public InscriptionRecipe(ElementType type, int elementAmount, List<Ingredient> ingredients, ItemStack output) {
		super(type);
		this.ingredients = NonNullList.of(Ingredient.EMPTY, ingredients.toArray(Ingredient[]::new));
		this.elementAmount = elementAmount;
		this.output = output;
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	@Override
	public boolean matches(@Nonnull InscriberBlockEntity inscriber, @Nonnull Level level) {
		if (inscriber.getContainerElementType() == getElementType()) {
			return RecipeHelper.matchesUnordered(inscriber.getInventory(), ingredients);
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
		return output;
	}

	@Nonnull
	@Override
	public RecipeType<?> getType() {
		return ECRecipeTypes.INSCRIPTION.get();
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return ECRecipeSerializers.INSCRIPTION.get();
	}

	public static class Serializer implements RecipeSerializer<InscriptionRecipe> {

		@Override
		@NotNull
		public Codec<InscriptionRecipe> codec() {
			return CODEC;
		}

		@Override
		public InscriptionRecipe fromNetwork(FriendlyByteBuf buffer) {
			ElementType type = ElementType.byName(buffer.readUtf());
			int elementAmount = buffer.readInt();
			ItemStack output = buffer.readItem();
			int i = buffer.readInt();
			NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);

			for (int j = 0; j < i; ++j) {
				ingredients.set(j, Ingredient.fromNetwork(buffer));
			}

			return new InscriptionRecipe(type, elementAmount, ingredients, output);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, InscriptionRecipe recipe) {
			buffer.writeUtf(recipe.getElementType().getSerializedName());
			buffer.writeInt(recipe.getElementAmount());
			buffer.writeItem(recipe.output);
			buffer.writeInt(recipe.getIngredients().size());
			recipe.getIngredients().forEach(ingredient -> ingredient.toNetwork(buffer));
		}
	}
}
