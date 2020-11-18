package sirttas.elementalcraft.item;

import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.property.ECProperties;

public class ItemElemental extends ItemEC {

	protected final ElementType elementType;

	public ItemElemental(ElementType elementType) {
		this(ECProperties.Items.DEFAULT_ITEM_PROPERTIES, elementType);
	}

	public ItemElemental(Properties properties, ElementType elementType) {
		super(properties);
		this.elementType = elementType;
	}

	public ElementType getElementType() {
		return elementType;
	}
}
