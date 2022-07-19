package sirttas.elementalcraft.recipe.instrument.binding;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.instrument.binder.IBinder;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.RecipeHelper;

import javax.annotation.Nonnull;
import java.util.List;

public class BindingRecipe extends AbstractBindingRecipe {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + NAME) public static final RecipeSerializer<BindingRecipe> SERIALIZER = null;

	private final NonNullList<Ingredient> ingredients;
	private final ItemStack output;
	private final int elementAmount;

	public BindingRecipe(ResourceLocation id, ElementType type, int elementAmount, ItemStack output, List<Ingredient> ingredients) {
		super(id, type);
		this.ingredients = NonNullList.of(Ingredient.EMPTY, ingredients.toArray(Ingredient[]::new));
		this.output = output;
		this.elementAmount = elementAmount;
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	@Override
	public boolean matches(IBinder binder) {
		if (binder.getContainerElementType() != getElementType() || binder.getItemCount() != ingredients.size()) {
			return false;
		}
		return Boolean.TRUE.equals(ECConfig.COMMON.binderRecipeMatchOrder.get()) ? matchesOrdered(binder) : RecipeHelper.matchesUnordered(binder.getInventory(), ingredients);
	}

	private boolean matchesOrdered(IBinder binder) {
		var inv = binder.getInventory();

		for (int i = 0; i < inv.getContainerSize(); i++) {
			var s = inv.getItem(i);

			if (!s.isEmpty() && !ingredients.get(i).test(s)) {
				return false;
			}
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
	public ItemStack getResultItem() {
		return output;
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<BindingRecipe> {

		@Nonnull
		@Override
		public BindingRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			ElementType type = ElementType.byName(GsonHelper.getAsString(json, ECNames.ELEMENT_TYPE));
			int elementAmount = GsonHelper.getAsInt(json, ECNames.ELEMENT_AMOUNT);
			NonNullList<Ingredient> ingredients = RecipeHelper.readIngredients(GsonHelper.getAsJsonArray(json, ECNames.INGREDIENTS));
			ItemStack output = RecipeHelper.readRecipeOutput(json, ECNames.OUTPUT);

			if (!output.isEmpty()) {
				return new BindingRecipe(recipeId, type, elementAmount, output, ingredients);
			}
			throw new IllegalStateException("Binding recipe output is empty!");
		}

		@Override
		public BindingRecipe fromNetwork(@Nonnull ResourceLocation recipeId, FriendlyByteBuf buffer) {
			ElementType type = ElementType.byName(buffer.readUtf());
			int elementAmount = buffer.readInt();
			ItemStack output = buffer.readItem();
			int i = buffer.readVarInt();
			NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);

			for (int j = 0; j < ingredients.size(); ++j) {
				ingredients.set(j, Ingredient.fromNetwork(buffer));
			}

			return new BindingRecipe(recipeId, type, elementAmount, output, ingredients);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, BindingRecipe recipe) {
			buffer.writeUtf(recipe.getElementType().getSerializedName());
			buffer.writeInt(recipe.getElementAmount());
			buffer.writeItem(recipe.getResultItem());
			buffer.writeVarInt(recipe.getIngredients().size());

			for (Ingredient ingredient : recipe.getIngredients()) {
				ingredient.toNetwork(buffer);
			}
		}
	}
}
