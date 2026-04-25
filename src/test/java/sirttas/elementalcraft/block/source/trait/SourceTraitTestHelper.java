package sirttas.elementalcraft.block.source.trait;

import com.mojang.serialization.DataResult;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import sirttas.dpanvil.api.codec.CodecHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.block.source.SourceElementStorage;

import java.util.Map;

public class SourceTraitTestHelper {

    private SourceTraitTestHelper() {}

    public static CompoundTag createDefaultTraits() {
        return createTraitTag(Map.of(
                SourceTraits.ELEMENT_CAPACITY, FloatTag.valueOf(SourceElementStorage.DEFAULT_CAPACITY)
        ));
    }

    public static Map<Holder<@NotNull SourceTrait>, ISourceTraitValue> deserializeTraits(CompoundTag traits) {
        return CodecHelper.decode(SourceTrait.VALUE_MAP_CODEC, ECGameTestUtils.registryAccess().createSerializationContext(NbtOps.INSTANCE), traits);
    }

    public static CompoundTag createTraitTag(Map<Holder<@NotNull SourceTrait>, Tag> traits) {
        var ops = ECGameTestUtils.registryAccess().createSerializationContext(NbtOps.INSTANCE);
        var builder = ops.mapBuilder();

        for (var entry : traits.entrySet()) {
            builder.add(SourceTrait.HOLDER_CODEC.encodeStart(ops, entry.getKey()), DataResult.success(entry.getValue()));
        }
        return (CompoundTag) CodecHelper.handleResult(builder.build(new CompoundTag()));
    }


}
