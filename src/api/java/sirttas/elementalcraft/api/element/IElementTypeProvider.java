package sirttas.elementalcraft.api.element;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public interface IElementTypeProvider {

	@Nonnull
	ElementType getElementType();

	default Supplier<ElementType> getElementTypeSupplier() {
		return this::getElementType;
	}
}
