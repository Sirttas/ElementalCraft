package sirttas.elementalcraft.recipe;

import com.google.gson.JsonObject;

import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JSONUtils;

public class RecipeHelper {
	public static Ingredient deserializeIngredient(JsonObject json, String key) {
		if (JSONUtils.isJsonArray(json, key)) {
			return Ingredient.deserialize(JSONUtils.getJsonArray(json, key));
		}
		return Ingredient.deserialize(JSONUtils.getJsonObject(json, key));
	}
}
