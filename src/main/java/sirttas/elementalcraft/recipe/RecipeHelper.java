package sirttas.elementalcraft.recipe;

import com.google.common.collect.Sets;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Set;

public class RecipeHelper {

	private RecipeHelper() {}

	public static boolean matchesUnordered(List<ItemStack> stacks, List<Ingredient> ingredients) {
		Set<Integer> usedIndex = Sets.newHashSet();
		int count = stacks.size();

		return ingredients.stream().allMatch(ingredient -> {
			for (int i = 0; i < count; i++) {
				if (ingredient.test(stacks.get(i)) && !usedIndex.contains(i)) {
					usedIndex.add(i);
					return true;
				}
			}
			return false;
		}) && usedIndex.size() == count;
	}
}
