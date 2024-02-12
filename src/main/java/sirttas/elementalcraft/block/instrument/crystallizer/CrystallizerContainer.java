package sirttas.elementalcraft.block.instrument.crystallizer;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import sirttas.elementalcraft.block.instrument.InstrumentContainer;
import sirttas.elementalcraft.recipe.ECRecipeTypes;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CrystallizerContainer extends InstrumentContainer {

	private static final List<Ingredient> FIRST_SLOT_ITEMS = new ArrayList<>();
	private static final List<Ingredient> SECOND_SLOT_ITEMS = new ArrayList<>();


	private final CrystallizerBlockEntity crystallizer;

	public CrystallizerContainer(CrystallizerBlockEntity crystallizer) {
		super(crystallizer::setChanged, 12);
		this.crystallizer = crystallizer;
	}

	@Override
	public boolean canPlaceItem(int slot, @Nonnull ItemStack stack) {
		if (slot == 0) {
			return FIRST_SLOT_ITEMS.isEmpty() || FIRST_SLOT_ITEMS.stream().anyMatch(i -> i.test(stack));
		} else if (slot == 1) {
			return SECOND_SLOT_ITEMS.isEmpty() || SECOND_SLOT_ITEMS.stream().anyMatch(i -> i.test(stack));
		}
		return crystallizer.isValidShard(stack);
	}

	public static void reload(RecipeManager recipeManager) {
		var recipes = recipeManager.getAllRecipesFor(ECRecipeTypes.CRYSTALLIZATION.get());

		FIRST_SLOT_ITEMS.clear();
		SECOND_SLOT_ITEMS.clear();
		for (var holder : recipes) {
			var recipe = holder.value();
			var ingredients = recipe.getIngredients();

			if (!ingredients.isEmpty()) {
				FIRST_SLOT_ITEMS.add(ingredients.get(0));
			}
			if (ingredients.size() > 1) {
				SECOND_SLOT_ITEMS.add(ingredients.get(1));
			}
		}
	}

}
