package sirttas.elementalcraft.item;

import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public class TooltipHelper {

    private TooltipHelper() {}

    public static void addAttributeMultiMapToTooltip(List<Component> tooltip, Multimap<Holder<Attribute>, AttributeModifier> multiMap, @Nullable Component title) {
        if (!multiMap.isEmpty()) {
            tooltip.add(Component.empty());
            if (title != null) {
                tooltip.add(title);
            }
            for (var entry : multiMap.entries()) {
                tooltip.add(getAttributeTooltip(entry.getKey(), entry.getValue()));
            }
        }
    }

    public static MutableComponent getAttributeTooltip(Holder<Attribute> attribute, AttributeModifier attributemodifier) {
        var tooltip = Component.empty();

        ItemStack.EMPTY.addModifierTooltip(tooltip::append, null, attribute, attributemodifier);
        return tooltip;
    }
}
