package sirttas.elementalcraft.item;

import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class TooltipHelper {

    private TooltipHelper() {}

    public static void addAttributeMultiMapToTooltip(Consumer<Component> builder, Multimap<Holder<Attribute>, AttributeModifier> multiMap, @Nullable Component title) {
        if (!multiMap.isEmpty()) {
            builder.accept(Component.empty());
            if (title != null) {
                builder.accept(title);
            }
            for (var entry : multiMap.entries()) {
                addAttributeTooltip(builder, entry.getKey(), entry.getValue());
            }
        }
    }

    public static void addAttributeTooltip(Consumer<Component> builder, Holder<Attribute> attribute, AttributeModifier attributemodifier) {
        ItemAttributeModifiers.Display.attributeModifiers().apply(builder, null, attribute, attributemodifier);
    }
}
