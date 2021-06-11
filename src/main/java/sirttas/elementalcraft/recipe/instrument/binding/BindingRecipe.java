package sirttas.elementalcraft.recipe.instrument.binding;

import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.instrument.binder.IBinder;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.RecipeHelper;

public class BindingRecipe extends AbstractBindingRecipe {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + NAME) public static final IRecipeSerializer<BindingRecipe> SERIALIZER = null;

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
	public boolean matches(IBinder inv) {
		if (inv.getTankElementType() != getElementType() || inv.getItemCount() != ingredients.size()) {
			return false;
		}
		return Boolean.TRUE.equals(ECConfig.COMMON.binderRecipeMatchOrder.get()) ? matchesOrdered(inv) : RecipeHelper.matchesUnordered(inv.getInventory(), ingredients);
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
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BindingRecipe> {

		@Override
		public BindingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			ElementType type = ElementType.byName(JSONUtils.getAsString(json, ECNames.ELEMENT_TYPE));
			int elementAmount = JSONUtils.getAsInt(json, ECNames.ELEMENT_AMOUNT);
			NonNullList<Ingredient> ingredients = RecipeHelper.readIngredients(JSONUtils.getAsJsonArray(json, ECNames.INGREDIENTS));
			ItemStack output = RecipeHelper.readRecipeOutput(json, ECNames.OUTPUT);

			if (!output.isEmpty()) {
				return new BindingRecipe(recipeId, type, elementAmount, output, ingredients);
			}
			return null;
		}

		@Override
		public BindingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
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
		public void toNetwork(PacketBuffer buffer, BindingRecipe recipe) {
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
