package sirttas.elementalcraft.jewel;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.data.attachment.ECDataAttachments;
import sirttas.elementalcraft.jewel.attribute.AttributeJewel;
import sirttas.elementalcraft.jewel.handler.IJewelHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JewelHelper {

    private JewelHelper() {}

    @Nonnull
    public static Jewel getJewel(@Nonnull ItemStack stack) {
        if (stack.isEmpty() || !stack.hasData(ECDataAttachments.JEWEL)) {
            return Jewels.NONE.get();
        }
        return stack.getData(ECDataAttachments.JEWEL);
    }

    public static void setJewel(@Nonnull ItemStack stack, @Nonnull Jewel jewel) {
        if (stack.isEmpty()) {
            return;
        }
        stack.setData(ECDataAttachments.JEWEL, jewel);
    }

    public static List<Jewel> getAllJewels(Entity entity) {
        var list = new ArrayList<Jewel>();

        for (var item: entity.getAllSlots()) {
            var jewel = getJewel(item);

            if (jewel != Jewels.NONE.get()) {
                list.add(jewel);
            }
        }
        return list;
    }

    public static List<Jewel> getActiveJewels(Entity entity) {
        var handler = entity.getCapability(IJewelHandler.CAPABILITY);

        if (handler != null) {
            return handler.getActiveJewels();
        }
        return Collections.emptyList();
    }

    public static boolean hasJewel(Entity entity, Jewel jewel) {
        return getActiveJewels(entity).contains(jewel);
    }

    public static Multimap<Attribute, AttributeModifier> getJewelsAttribute(Entity entity) {
        Multimap<Attribute, AttributeModifier>  map = ArrayListMultimap.create();

        for (var jewel : getActiveJewels(entity)) {
            if (jewel.isTicking() && jewel instanceof AttributeJewel attributeJewel) {
                map.putAll(attributeJewel.getAttributes());
            }
        }
        return map;

    }
}
