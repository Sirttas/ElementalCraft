package sirttas.elementalcraft.block.source.trait;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueOutput;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.block.source.SourceElementStorage;
import sirttas.elementalcraft.block.source.trait.holder.SourceTraitHolder;

import java.util.Map;

public class SourceTraitTestHelper {

    private SourceTraitTestHelper() {}

    public static Map<Holder<@NotNull SourceTrait>, ISourceTraitValue> getDefaultTraits() {
        var map = SourceTraits.createTraitMap();

        map.put(SourceTraits.ELEMENT_CAPACITY, SourceTraits.ELEMENT_CAPACITY.value().load(FloatTag.valueOf(SourceElementStorage.DEFAULT_CAPACITY)));
        return map;
    }

    public static CompoundTag serializeTraits(Map<Holder<@NotNull SourceTrait>, ISourceTraitValue> traits) {
        var holder = new SourceTraitHolder();
        var valueOutput = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, registries);

        holder.setTraits(traits);

    }
}
