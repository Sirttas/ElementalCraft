package sirttas.elementalcraft.recipe.instrument.binding;

import java.util.List;

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

public class BindingRecipe extends AbstractBindingRecipe {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + NAME) public static final RecipeSerializer<BindingRecipe> SERIALIZER = null;

	private NonNullList<Ingredient> ingredients;
	private ItemStack output;
	private int elementAmount;

	public BindingRecipe(ResourceLocation id, ElementType type, int elementAmount, ItemStack output, List<Ingredient> ingredients) {
		super(id, type);
		this.ingredients = NonNullList.of(Ingredient.EMPTY, ingredients.stream().toArray(s -> new Ingredient[s]));
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

	private boolean matchesOrdered(IBinder inv) {
		for (int i = 0; i < inv.getItemCount(); i++) {
			if (!ingredients.get(i).test(inv.getInventory().getItem(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}

	@Override
	public ItemStack getResultItem() {
		return output;
	}

	@Override
	public void process(IBinder instrument) {
		instrument.clearContent();
		super.process(instrument);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<BindingRecipe> {

		@Override
		public BindingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			ElementType type = ElementType.byName(GsonHelper.getAsString(json, ECNames.ELEMENT_TYPE));
			int elementAmount = GsonHelper.getAsInt(json, ECNames.ELEMENT_AMOUNT);
			NonNullList<Ingredient> ingredients = RecipeHelper.readIngredients(GsonHelper.getAsJsonArray(json, ECNames.INGREDIENTS));
			ItemStack output = RecipeHelper.readRecipeOutput(json, ECNames.OUTPUT);

			if (!output.isEmpty()) {
				return new BindingRecipe(recipeId, type, elementAmount, output, ingredients);
			}
			return null;
		}

		@Override
		public BindingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
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
