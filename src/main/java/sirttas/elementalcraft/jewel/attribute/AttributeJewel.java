package sirttas.elementalcraft.jewel.attribute;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.util.Lazy;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.item.ECItem;
import sirttas.elementalcraft.jewel.Jewel;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class AttributeJewel extends Jewel {

	private final Lazy<Multimap<Attribute, AttributeModifier>> attributes;
	
	protected AttributeJewel(ElementType elementType, int consumption, Supplier<Multimap<Attribute, AttributeModifier>> supplier) {
		super(elementType, consumption);
		this.attributes = Lazy.of(() -> ImmutableMultimap.copyOf(supplier.get()));
	}

	public Multimap<Attribute, AttributeModifier> getAttributes() {
		return attributes.get();
	}

	@Nullable
	protected Component getAttributesTitle() {
		return null;
	}

	@Override
	public void appendHoverText(List<Component> tooltip) {
		ECItem.addAttributeMultiMapToTooltip(tooltip, attributes.get(), this.getAttributesTitle());
		super.appendHoverText(tooltip);
	}

}
