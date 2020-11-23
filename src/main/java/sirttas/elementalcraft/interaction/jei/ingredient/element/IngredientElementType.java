package sirttas.elementalcraft.interaction.jei.ingredient.element;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import sirttas.elementalcraft.api.element.ElementType;

public class IngredientElementType {

	private final ElementType type;
	private int amount;

	public IngredientElementType(ElementType type) {
		this(type, -1);
	}

	public IngredientElementType(ElementType type, int amount) {
		this.type = type;
		this.setAmount(amount);
	}

	public ElementType getType() {
		return type;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = MathHelper.clamp(amount, -1, 4);
	}

	public ITextComponent getDisplayName() {
		return type.getDisplayName();
	}

	public IngredientElementType copy() {
		return new IngredientElementType(type, amount);
	}

	public static List<IngredientElementType> all() {
		return all(-1);
	}

	public static List<IngredientElementType> all(int amount) {
		return ElementType.allValid().stream().map(type -> new IngredientElementType(type, amount)).collect(Collectors.toList());
	}
}
