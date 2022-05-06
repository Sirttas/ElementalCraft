package sirttas.elementalcraft.api.element;

import java.util.function.Supplier;

public interface IElementTypeProvider {

	ElementType getElementType();

	default Supplier<ElementType> getElementTypeSupplier() {
		return this::getElementType;
	}
}
