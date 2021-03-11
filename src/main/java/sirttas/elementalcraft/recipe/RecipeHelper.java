package sirttas.elementalcraft.recipe;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.inventory.ECInventoryHelper;

public class RecipeHelper {

	private RecipeHelper() {}
	
	public static Ingredient deserializeIngredient(JsonObject json, String key) {
		if (JSONUtils.isJsonArray(json, key)) {
			return Ingredient.deserialize(JSONUtils.getJsonArray(json, key));
		}
		return Ingredient.deserialize(JSONUtils.getJsonObject(json, key));
	}

	public static ItemStack readRecipeOutput(JsonObject json, String key) {
		if (json.has(key)) {
			JsonElement element = json.get(key);

			if (element.isJsonPrimitive()) {
				return readRecipeOutput(element.getAsString());
			}
			return ShapedRecipe.deserializeItem(element.getAsJsonObject());
		}
		throw new JsonSyntaxException("Missing " + key + ", expected to find a string");
	}

	private static ItemStack readRecipeOutput(String output) {
		return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(output)));
	}

	public static NonNullList<Ingredient> readIngredients(JsonArray json) {
		NonNullList<Ingredient> nonnulllist = NonNullList.create();

		for (int i = 0; i < json.size(); ++i) {
			Ingredient ingredient = Ingredient.deserialize(json.get(i));

			if (!ingredient.hasNoMatchingItems()) {
				nonnulllist.add(ingredient);
			}
		}

		return nonnulllist;
	}

	public static boolean matchesUnordered(IInventory inv, List<Ingredient> ingredients) {
		Set<Integer> usedIndex = Sets.newHashSet();
		int count = ECInventoryHelper.getItemCount(inv);

		return ingredients.stream().allMatch(ingredient -> {
			for (int i = 0; i < count; i++) {
				if (ingredient.test(inv.getStackInSlot(i)) && !usedIndex.contains(i)) {
					usedIndex.add(i);
					return true;
				}
			}
			return false;
		}) && usedIndex.size() == count;
	}
}
