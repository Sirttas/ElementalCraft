package sirttas.elementalcraft.recipe.instrument;

import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.IInstrument;

public abstract class AbstractInstrumentRecipe<T extends IInstrument> implements ISingleElementInstrumentRecipe<T> {

	protected final ElementType elementType;

	protected AbstractInstrumentRecipe(ElementType type) {
		this.elementType = type;
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}
}
