package sirttas.elementalcraft.block.source.trait;

import net.minecraft.nbt.FloatTag;
import net.minecraft.resources.ResourceKey;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;

import java.util.Map;

public class SourceTraitGameTestHelper {

    private SourceTraitGameTestHelper() {}

    public static Map<ResourceKey<SourceTrait>, ISourceTraitValue> getDefaultTraits() {
        var map = SourceTraits.createTraitMap();

        map.put(SourceTraits.ELEMENT_CAPACITY, ElementalCraftApi.SOURCE_TRAIT_MANAGER.get(SourceTraits.ELEMENT_CAPACITY.location()).load(FloatTag.valueOf(500000)));
        map.put(SourceTraits.RECOVER_RATE, ElementalCraftApi.SOURCE_TRAIT_MANAGER.get(SourceTraits.RECOVER_RATE.location()).load(FloatTag.valueOf(50)));
        return map;
    }

}
