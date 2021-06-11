package sirttas.elementalcraft.interaction.jei.category.element;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.interaction.jei.category.AbstractECRecipeCategory;
import sirttas.elementalcraft.interaction.jei.ingredient.ECIngredientTypes;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;
import sirttas.elementalcraft.item.elemental.ElementalItem;

public abstract class AbstractElementFromItemRecipeCategory extends AbstractECRecipeCategory<Ingredient> {
	
	protected AbstractElementFromItemRecipeCategory(String translationKey, IDrawable icon, IDrawable background) {
		super(translationKey, icon, background);
	}

	@Override
	public void setIngredients(Ingredient recipe, IIngredients ingredients) {
		List<List<ItemStack>> inputs = new ArrayList<>();

		inputs.add(Stream.of(recipe.getItems()).collect(Collectors.toList()));
		ingredients.setInputLists(VanillaTypes.ITEM, inputs);
		ingredients.setOutput(ECIngredientTypes.ELEMENT, new IngredientElementType(getElementType(recipe), 1));
	}

	protected ElementType getElementType(Ingredient recipe) {
		ItemStack[] stacks = recipe.getItems();

		if (stacks != null && stacks.length > 0) {
			Item item = stacks[0].getItem();

			if (item instanceof ElementalItem) {
				return ((ElementalItem) item).getElementType();
			}
		}
		return ElementType.NONE;
	}
	
	@Override
	public Class<Ingredient> getRecipeClass() {
		return Ingredient.class;
	}
	
}
