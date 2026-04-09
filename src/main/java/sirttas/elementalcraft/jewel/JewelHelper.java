package sirttas.elementalcraft.jewel;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.jewel.attribute.AttributeJewel;
import sirttas.elementalcraft.jewel.handler.IJewelHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JewelHelper {

    private JewelHelper() {}

    @Nullable
    public static Jewel getJewel(@Nonnull ItemStack stack) {
        return stack.get(ECDataComponents.JEWEL);
    }

    public static void setJewel(@Nonnull ItemStack stack, @Nonnull Jewel jewel) {
        if (stack.isEmpty()) {
            return;
        }
        stack.set(ECDataComponents.JEWEL, jewel);
    }

    public static List<Jewel> getAllJewels(Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return Collections.emptyList();
        }

        var list = new ArrayList<Jewel>();

        for (var slot : EquipmentSlot.values()) {
            var item = livingEntity.getItemBySlot(slot);
            var jewel = getJewel(item);

            if( jewel != null) {
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

    public static Multimap<Holder<@NotNull Attribute>, AttributeModifier> getJewelsAttribute(Entity entity) {
        Multimap<Holder<@NotNull Attribute>, AttributeModifier>  map = ArrayListMultimap.create();

        for (var jewel : getActiveJewels(entity)) {
            if (jewel.isTicking() && jewel instanceof AttributeJewel attributeJewel) {
                map.putAll(attributeJewel.getAttributes());
            }
        }
        return map;

    }
}
