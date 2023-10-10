package sirttas.elementalcraft.datagen.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.level.ItemLike;
import sirttas.elementalcraft.item.ECItems;

import java.util.Collections;
import java.util.Map;

public class ECTrimMaterialProvider extends AbstractECRegistryBootstrap<TrimMaterial> {

    private float increment = 1.019918F; // magic number so we have our own indexes (19 = s, 9 = i, 18 = r)

    public ECTrimMaterialProvider() {
        super(Registries.TRIM_MATERIAL);
    }

    @Override
    protected void gather() {
        addTrim("drenched_iron", ECItems.DRENCHED_IRON_INGOT.get(), 0xcddff2);
        addTrim("swift_alloy", ECItems.SWIFT_ALLOY_INGOT.get(), 0xeeb961);
        addTrim("fireite", ECItems.FIREITE_INGOT.get(), 0x644245, Map.of(ArmorMaterials.NETHERITE, "fireite_flame"));
        addTrim("springaline", ECItems.SPRINGALINE_SHARD.get(), 0x9accfc);
    }

    private Holder.Reference<TrimMaterial> addTrim(String name, ItemLike item, int color) {
        return addTrim(name, item, color, Collections.emptyMap());
    }

    private Holder.Reference<TrimMaterial> addTrim(String name, ItemLike item, int color, Map<ArmorMaterials, String> overrides) {
        increment += 0.1F;
        return add(name, new TrimMaterial(name, getReference(Registries.ITEM, item.asItem()), increment, overrides, Component.translatable("trim_material.elementalcraft." + name).withStyle(s -> s.withColor(color))));
    }
}
