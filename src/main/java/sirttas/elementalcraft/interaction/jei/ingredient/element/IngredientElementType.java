package sirttas.elementalcraft.interaction.jei.ingredient.element;

import java.util.List;
import java.util.stream.Collectors;

import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;

public class IngredientElementType implements IElementTypeProvider {

	public static final IIngredientType<IngredientElementType> TYPE = () -> IngredientElementType.class;
	
	private final ElementType elementType;
	private int amount;

	public IngredientElementType(ElementType type) {
		this(type, -1);
	}

	public IngredientElementType(ElementType type, int amount) {
		this.elementType = type;
		this.setAmount(amount);
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = Mth.clamp(amount, -1, 4);
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
		return ElementType.ALL_VALID.stream().map(type -> new IngredientElementType(type, amount)).collect(Collectors.toList());
	}
}
