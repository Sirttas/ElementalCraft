package sirttas.elementalcraft.interaction.jei.ingredient.element;

import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;

import java.util.List;

public record IngredientElementType(
		ElementType elementType,
		int amount
) implements IElementTypeProvider {

	public static final IIngredientType<IngredientElementType> TYPE = () -> IngredientElementType.class;

	public IngredientElementType(ElementType elementType, int amount) {
		this.elementType = elementType;
		this.amount = Mth.clamp(amount, -1, 4);
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}

	public Component getDisplayName() {
		return elementType.getDisplayName();
	}

	public IngredientElementType copy() {
		return new IngredientElementType(elementType, amount);
	}

	public static List<IngredientElementType> all() {
		return all(-1);
	}

	public static List<IngredientElementType> all(int amount) {
		return ElementType.ALL_VALID.stream()
				.map(type -> new IngredientElementType(type, amount))
				.toList();
	}
}
