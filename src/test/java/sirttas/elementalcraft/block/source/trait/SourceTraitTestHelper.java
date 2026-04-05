package sirttas.elementalcraft.block.source.trait;

import net.minecraft.core.Holder;
import net.minecraft.nbt.FloatTag;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.block.source.SourceElementStorage;

import java.util.Map;

public class SourceTraitTestHelper {

    private SourceTraitTestHelper() {}

    public static Map<Holder<@NotNull SourceTrait>, ISourceTraitValue> getDefaultTraits() {
        var map = SourceTraits.createTraitMap();

        map.put(SourceTraits.ELEMENT_CAPACITY, SourceTraits.ELEMENT_CAPACITY.value().load(FloatTag.valueOf(SourceElementStorage.DEFAULT_CAPACITY)));
        return map;
    }
}
