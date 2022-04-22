package sirttas.elementalcraft.interaction.jei.category.element;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.item.elemental.ElementalItem;

import javax.annotation.Nonnull;

public abstract class AbstractElementFromItemRecipeCategory extends AbstractECRecipeCategory<Ingredient> {
	
	protected AbstractElementFromItemRecipeCategory(String translationKey, IDrawable icon, IDrawable background) {
		super(translationKey, icon, background);
	}

	protected ElementType getElementType(Ingredient recipe) {
		ItemStack[] stacks = recipe.getItems();

		if (stacks.length > 0) {
			Item item = stacks[0].getItem();

			if (item instanceof ElementalItem) {
				return ((ElementalItem) item).getElementType();
			}
		}
		return ElementType.NONE;
	}

	@Nonnull
	protected IngredientElementType getOutput(@Nonnull Ingredient ingredient) {
		return new IngredientElementType(getElementType(ingredient), 1);
	}
	
}
