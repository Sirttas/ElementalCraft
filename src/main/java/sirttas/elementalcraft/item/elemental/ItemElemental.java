package sirttas.elementalcraft.item.elemental;

import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.property.ECProperties;

public class ItemElemental extends ItemEC implements IElementTypeProvider {

	protected final ElementType elementType;

	public ItemElemental(ElementType elementType) {
		this(ECProperties.Items.DEFAULT_ITEM_PROPERTIES, elementType);
	}

	public ItemElemental(Properties properties, ElementType elementType) {
		super(properties);
		this.elementType = elementType;
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}
}
