package sirttas.elementalcraft.recipe.instrument.binding;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.instrument.binder.IBinder;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.RecipeHelper;

import javax.annotation.Nonnull;
import java.util.List;

public class BindingRecipe extends AbstractBindingRecipe {

	public static final Codec<BindingRecipe> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			ElementType.CODEC.fieldOf(ECNames.ELEMENT_TYPE).forGetter(BindingRecipe::getElementType),
			Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(BindingRecipe::getElementAmount),
			Ingredient.LIST_CODEC.fieldOf(ECNames.INGREDIENTS).forGetter(BindingRecipe::getIngredients),
			ItemStack.CODEC.fieldOf(ECNames.OUTPUT).forGetter(r -> r.output)
	).apply(builder, BindingRecipe::new));

	private final NonNullList<Ingredient> ingredients;
	private final ItemStack output;
	private final int elementAmount;

	public BindingRecipe(ElementType type, int elementAmount, List<Ingredient> ingredients, ItemStack output) {
		super(type);
		this.ingredients = NonNullList.of(Ingredient.EMPTY, ingredients.toArray(Ingredient[]::new));
		this.output = output;
		this.elementAmount = elementAmount;
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	@Override
	public boolean matches(IBinder binder, @Nonnull Level level) {
		if (binder.getContainerElementType() != getElementType() || binder.getItemCount() != ingredients.size()) {
			return false;
		}
		return Boolean.TRUE.equals(ECConfig.COMMON.binderRecipeMatchOrder.get()) ? matchesOrdered(binder) : RecipeHelper.matchesUnordered(binder.getInventory(), ingredients);
	}

	private boolean matchesOrdered(IBinder binder) {
		var inv = binder.getInventory();

		int ingredientIndex = 0;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			var s = inv.getItem(i);

			if (s.isEmpty()) {
				continue;
			} else if (ingredientIndex >= ingredients.size() || !ingredients.get(ingredientIndex).test(s)) {
				return false;
			}
			ingredientIndex++;
		}
		return true;
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
	public RecipeSerializer<?> getSerializer() {
		return ECRecipeSerializers.BINDING.get();
	}

	public static class Serializer implements RecipeSerializer<BindingRecipe> {

		@Nonnull
		@Override
		public Codec<BindingRecipe> codec() {
			return CODEC;
		}

		@Override
		public BindingRecipe fromNetwork(FriendlyByteBuf buffer) {
			var type = ElementType.byName(buffer.readUtf());
			var elementAmount = buffer.readInt();
			var output = buffer.readItem();
			var i = buffer.readVarInt();
			var ingredients = NonNullList.withSize(i, Ingredient.EMPTY);

			ingredients.replaceAll(ignored -> Ingredient.fromNetwork(buffer));
			return new BindingRecipe(type, elementAmount, ingredients, output);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, BindingRecipe recipe) {
			buffer.writeUtf(recipe.getElementType().getSerializedName());
			buffer.writeInt(recipe.getElementAmount());
			buffer.writeItem(recipe.output);
			buffer.writeVarInt(recipe.getIngredients().size());

			for (Ingredient ingredient : recipe.getIngredients()) {
				ingredient.toNetwork(buffer);
			}
		}
	}
}
