package sirttas.elementalcraft.recipe;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.inventory.ECInventoryHelper;

public class RecipeHelper {

	private RecipeHelper() {}
	
	public static Ingredient deserializeIngredient(JsonObject json, String key) {
		if (GsonHelper.isArrayNode(json, key)) {
			return Ingredient.fromJson(GsonHelper.getAsJsonArray(json, key));
		}
		return Ingredient.fromJson(GsonHelper.getAsJsonObject(json, key));
	}

	public static ItemStack readRecipeOutput(JsonObject json, String key) {
		if (json.has(key)) {
			JsonElement element = json.get(key);

			if (element.isJsonPrimitive()) {
				return readRecipeOutput(element.getAsString());
			}
			return ShapedRecipe.itemStackFromJson(element.getAsJsonObject());
		}
		throw new JsonSyntaxException("Missing " + key + ", expected to find a string");
	}

	private static ItemStack readRecipeOutput(String output) {
		return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(output)));
	}

	public static NonNullList<Ingredient> readIngredients(JsonArray json) {
		NonNullList<Ingredient> nonnulllist = NonNullList.create();

		for (int i = 0; i < json.size(); ++i) {
			Ingredient ingredient = Ingredient.fromJson(json.get(i));

			if (!ingredient.isEmpty()) {
				nonnulllist.add(ingredient);
			}
		}

		return nonnulllist;
	}

	public static boolean matchesUnordered(Container inv, List<Ingredient> ingredients) {
		Set<Integer> usedIndex = Sets.newHashSet();
		int count = ECInventoryHelper.getItemCount(inv);

		return ingredients.stream().allMatch(ingredient -> {
			for (int i = 0; i < count; i++) {
				if (ingredient.test(inv.getItem(i)) && !usedIndex.contains(i)) {
					usedIndex.add(i);
					return true;
				}
			}
			return false;
		}) && usedIndex.size() == count;
	}
}
