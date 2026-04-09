package sirttas.elementalcraft.jewel.attribute;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.item.TooltipHelper;
import sirttas.elementalcraft.jewel.Jewel;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AttributeJewel extends Jewel {

	private final Lazy<@NotNull Multimap<Holder<@NotNull Attribute>, AttributeModifier>> attributes;
	
	protected AttributeJewel(ElementType elementType, int consumption, Supplier<Multimap<Holder<@NotNull Attribute>, AttributeModifier>> supplier) {
		super(elementType, consumption, true);
		this.attributes = Lazy.of(() -> ImmutableMultimap.copyOf(supplier.get()));
	}

	public Multimap<Holder<@NotNull Attribute>, AttributeModifier> getAttributes() {
		return attributes.get();
	}

	@Nullable
	protected Component getAttributesTitle() {
		return null;
	}

	@Override
    public void appendHoverText(@NotNull Consumer<Component> builder) {
		TooltipHelper.addAttributeMultiMapToTooltip(builder, attributes.get(), this.getAttributesTitle());
		super.appendHoverText(builder);
	}

}
