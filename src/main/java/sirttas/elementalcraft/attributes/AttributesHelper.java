package sirttas.elementalcraft.attributes;

import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class AttributesHelper {

    private AttributesHelper() {}

    public static void addAttributes(AttributeMap attributeMap, Multimap<Holder<Attribute>, AttributeModifier> attributes) {
        attributes.forEach((a, m) -> {
            var instance = attributeMap.getInstance(a);

            if (instance != null) {
                instance.removeModifier(m.id());
                instance.addTransientModifier(m);
            }
        });
    }

    public static void removeAttributes(AttributeMap attributeMap, Multimap<Holder<Attribute>, AttributeModifier> attributes) {
        attributes.forEach((a, m) -> {
            var instance = attributeMap.getInstance(a);

            if (instance != null) {
                instance.removeModifier(m);
            }
        });
    }
}
