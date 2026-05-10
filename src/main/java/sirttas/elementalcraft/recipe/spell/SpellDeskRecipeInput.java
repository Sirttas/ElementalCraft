package sirttas.elementalcraft.recipe.spell;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record SpellDeskRecipeInput(ItemStack scrollPaper, ItemStack gem, ItemStack crystal) implements RecipeInput {

	@Override
	public ItemStack getItem(int slot) {
		return switch (slot) {
			case 0 -> scrollPaper;
			case 1 -> gem;
			case 2 -> crystal;
			default -> throw new IllegalArgumentException("No item for index " + slot);
		};
	}

	@Override
	public int size() {
		return 3;
	}
}