package sirttas.elementalcraft.jewel;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.jewel.attribute.AttributeJewel;
import sirttas.elementalcraft.jewel.handler.IJewelHandler;
import sirttas.elementalcraft.nbt.NBTHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JewelHelper {

    private JewelHelper() {}

    public static Jewel getJewel(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }

        CompoundTag nbt = NBTHelper.getECTag(stack);

        if (nbt != null && nbt.contains(ECNames.JEWEL, 8)) {
            return Jewels.REGISTRY.get().getValue(new ResourceLocation(nbt.getString(ECNames.JEWEL)));
        }
        return null;
    }

    public static void setJewel(ItemStack stack, Jewel jewel) {
        if (stack.isEmpty()) {
            return;
        }

        CompoundTag nbt = NBTHelper.getOrCreateECTag(stack);

        nbt.putString(ECNames.JEWEL, jewel.getKey().toString());
    }

    public static List<Jewel> getAllJewels(Entity entity) {
        var list = new ArrayList<Jewel>();

        for (var item: entity.getAllSlots()) {
            var jewel = getJewel(item);

            if (jewel != null) {
                list.add(jewel);
            }
        }
        return list;
    }

    public static List<Jewel> getActiveJewels(Entity entity) {
        return entity.getCapability(IJewelHandler.CAPABILITY)
                .map(IJewelHandler::getActiveJewels)
                .orElse(Collections.emptyList());
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
