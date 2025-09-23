package sirttas.elementalcraft.test.block.source.trait;

import com.mojang.serialization.JsonOps;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.network.connection.ConnectionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.block.source.SourceElementStorage;
import sirttas.elementalcraft.block.source.trait.SourceTraits;
import sirttas.elementalcraft.test.annotation.ElementalCraftTest;

import java.util.function.Function;
import java.util.stream.Stream;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ElementalCraftTest
public class SourceTraitTests {

    @ParameterizedTest
    @DisplayName("Check that a source trait can serialize and deserialize its value using nbt.")
    @MethodSource("provideSourceTraitAndValue")
    public void should_serializeAndDeserializeValue_using_Nbt(SourceTrait trait, ISourceTraitValue value) {
        // Given

        // When
        var nbt = trait.save(value);
        var loaded = trait.load(nbt);

        // Then
        assertThat(loaded).isEqualTo(value);
    }

    @ParameterizedTest
    @DisplayName("Check that a source trait can serialize and deserialize its value using codec.")
    @MethodSource("provideSourceTraitAndValue")
    public void should_serializeAndDeserializeValue_using_Codec(SourceTrait trait, ISourceTraitValue value, MinecraftServer server) {
        // Given
        var codec = trait.valueCodec();
        var ops = server.registryAccess().createSerializationContext(JsonOps.INSTANCE);

        // When
        var json = codec.encode(value, ops, ops.empty()).getOrThrow();
        var loaded = codec.decode(ops, json).getOrThrow().getFirst();

        // Then
        assertThat(loaded).isEqualTo(value);
    }

    @ParameterizedTest
    @DisplayName("Check that a source trait can serialize and deserialize its value using stream codec.")
    @MethodSource("provideSourceTraitAndValue")
    public void should_serializeAndDeserializeValue_using_StreamCodec(SourceTrait trait, ISourceTraitValue value, MinecraftServer server) {
        // Given
        var streamCodec = trait.valueStreamCodec();
        var buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), server.registryAccess(), ConnectionType.NEOFORGE);

        // When
        streamCodec.encode(buffer, value);
        var loaded = streamCodec.decode(buffer);

        // Then
        assertThat(loaded).isEqualTo(value);
    }


    public static Stream<Arguments> provideSourceTraitAndValue() {
        return Stream.of(
                createSourceTraitAndValueArguments(SourceTraits.ELEMENT_CAPACITY.value(), t -> t.load(FloatTag.valueOf(SourceElementStorage.DEFAULT_CAPACITY))),
                createSourceTraitAndValueArguments(SourceTraits.DIURNAL_NOCTURNAL.value(), t -> t.load(StringTag.valueOf("diurnal_5"))),
                createSourceTraitAndValueArguments(SourceTraits.GENEROSITY.value(), t -> t.load(StringTag.valueOf("generous_5"))),
                createSourceTraitAndValueArguments(SourceTraits.THRIFTINESS.value(), t -> t.load(StringTag.valueOf("thrifty_5"))),
                createSourceTraitAndValueArguments(SourceTraits.FERTILITY.value(), t -> t.load(StringTag.valueOf("fertile_5")))
        );
    }

    private static Arguments createSourceTraitAndValueArguments(SourceTrait trait, Function<SourceTrait, ISourceTraitValue> valueProvider) {
        return Arguments.of(Named.of(trait.getId().toString(), trait), valueProvider.apply(trait));
    }
}
