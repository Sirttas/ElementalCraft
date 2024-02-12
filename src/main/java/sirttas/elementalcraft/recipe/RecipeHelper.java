package sirttas.elementalcraft.recipe;

import com.google.common.collect.Sets;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Ingredient;
import sirttas.elementalcraft.container.ECContainerHelper;

import java.util.List;
import java.util.Set;

public class RecipeHelper {

	private RecipeHelper() {}

	public static boolean matchesUnordered(Container inv, List<Ingredient> ingredients) {
		Set<Integer> usedIndex = Sets.newHashSet();
		int count = ECContainerHelper.getItemCount(inv);

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
