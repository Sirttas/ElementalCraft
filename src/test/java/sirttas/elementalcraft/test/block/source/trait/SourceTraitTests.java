package sirttas.elementalcraft.test.block.source.trait;

import com.mojang.serialization.JsonOps;
import io.netty.buffer.Unpooled;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.network.connection.ConnectionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sirttas.dpanvil.api.codec.CodecHelper;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.block.source.SourceElementStorage;
import sirttas.elementalcraft.block.source.trait.SourceTraits;
import sirttas.elementalcraft.test.annotation.ElementalCraftTest;

import java.util.stream.Stream;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ElementalCraftTest
public class SourceTraitTests {

    @ParameterizedTest
    @DisplayName("Check that a source trait can serialize and deserialize its value using codec.")
    @MethodSource("provideSourceTraitAndValue")
    public void should_serializeAndDeserializeValue_using_Codec(SourceTrait trait, Tag valueTag, MinecraftServer server) {
        // Given
        var codec = trait.valueCodec();
        var ops = server.registryAccess().createSerializationContext(JsonOps.INSTANCE);
        var value = loadValue(server.registryAccess(), trait, valueTag);

        // When
        var json = codec.encode(value, ops, ops.empty()).getOrThrow();
        var loaded = codec.decode(ops, json).getOrThrow().getFirst();

        // Then
        assertThat(loaded).isEqualTo(value);
    }

    @ParameterizedTest
    @DisplayName("Check that a source trait can serialize and deserialize its value using stream codec.")
    @MethodSource("provideSourceTraitAndValue")
    public void should_serializeAndDeserializeValue_using_StreamCodec(SourceTrait trait, Tag valueTag, MinecraftServer server) {
        // Given
        var streamCodec = trait.valueStreamCodec();
        var buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), server.registryAccess(), ConnectionType.NEOFORGE);
        var value = loadValue(server.registryAccess(), trait, valueTag);

        // When
        streamCodec.encode(buffer, value);
        var loaded = streamCodec.decode(buffer);

        // Then
        assertThat(loaded).isEqualTo(value);
    }

    public static Stream<Arguments> provideSourceTraitAndValue() {
        return Stream.of(
                createSourceTraitAndValueArguments(SourceTraits.ELEMENT_CAPACITY, FloatTag.valueOf(SourceElementStorage.DEFAULT_CAPACITY)),
                createSourceTraitAndValueArguments(SourceTraits.DIURNAL_NOCTURNAL, StringTag.valueOf("diurnal_5")),
                createSourceTraitAndValueArguments(SourceTraits.GENEROSITY, StringTag.valueOf("generous_5")),
                createSourceTraitAndValueArguments(SourceTraits.THRIFTINESS, StringTag.valueOf("thrifty_5")),
                createSourceTraitAndValueArguments(SourceTraits.FERTILITY, StringTag.valueOf("fertile_5"))
        );
    }

    private static Arguments createSourceTraitAndValueArguments(Holder<SourceTrait> holder, Tag tag) {
        var trait = holder.value();

        return Arguments.of(Named.of(trait.getId().toString(), trait), tag);
    }

    private static ISourceTraitValue loadValue(HolderLookup.Provider provider, SourceTrait trait, Tag valueTag) {
        return CodecHelper.decode(trait.valueCodec(), provider.createSerializationContext(NbtOps.INSTANCE), valueTag);
    }
}
