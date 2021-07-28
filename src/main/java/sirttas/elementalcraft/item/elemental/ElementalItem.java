package sirttas.elementalcraft.item.elemental;

import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.item.ECItem;
import sirttas.elementalcraft.property.ECProperties;

public class ElementalItem extends ECItem implements IElementTypeProvider {

	protected final ElementType elementType;

	public ElementalItem(ElementType elementType) {
		this(ECProperties.Items.DEFAULT_ITEM_PROPERTIES, elementType);
	}

	public ElementalItem(Properties properties, ElementType elementType) {
		super(properties);
		this.elementType = elementType;
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}
}
