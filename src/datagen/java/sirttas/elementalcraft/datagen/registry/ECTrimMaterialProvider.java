package sirttas.elementalcraft.datagen.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
import net.minecraft.world.item.equipment.trim.TrimMaterial;

import java.util.Collections;
import java.util.Map;

public class ECTrimMaterialProvider extends AbstractECRegistryBootstrap<TrimMaterial> {

    public ECTrimMaterialProvider() {
        super(Registries.TRIM_MATERIAL);
    }

    @Override
    protected void gather() {
        addTrim("drenched_iron", 0xcddff2);
        addTrim("swift_alloy", 0xeeb961);
        addTrim("fireite", 0x644245, Map.of(EquipmentAssets.NETHERITE, "fireite_flame"));
        addTrim("springaline", 0x9accfc);
    }

    private Holder.Reference<TrimMaterial> addTrim(String name, int color) {
        return addTrim(name, color, Collections.emptyMap());
    }

    private Holder.Reference<TrimMaterial> addTrim(String name, int color, Map<ResourceKey<EquipmentAsset>, String> overrides) {
        return add(name, new TrimMaterial(MaterialAssetGroup.create(name, overrides), Component.translatable("trim_material.elementalcraft." + name).withStyle(s -> s.withColor(color))));
    }
}
