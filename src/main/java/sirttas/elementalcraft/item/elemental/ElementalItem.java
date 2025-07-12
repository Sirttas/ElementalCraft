package sirttas.elementalcraft.item.elemental;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;

public class ElementalItem extends Item implements IElementTypeProvider {

	protected final ElementType elementType;

	public ElementalItem(ElementType elementType, Properties properties) {
		super(properties);
		this.elementType = elementType;
	}

	@Override
	public @NotNull ElementType getElementType() {
		return elementType;
	}
}
