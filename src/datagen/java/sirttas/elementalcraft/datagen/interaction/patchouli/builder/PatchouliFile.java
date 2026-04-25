package sirttas.elementalcraft.datagen.interaction.patchouli.builder;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface PatchouliFile {

    static Codec<ItemStack> stackCodec(HolderLookup.Provider lookupProvider) {
        return new Codec<>() {

            @Override
            public <T> DataResult<T> encode(ItemStack input, DynamicOps<T> ops, T prefix) {
                var itemInput = new ItemInput(input.typeHolder(), input.getComponentsPatch());

                return DataResult.success(ops.createString(serialize(itemInput, lookupProvider)));
            }

            private String serialize(ItemInput itemInput, HolderLookup.Provider levelRegistry) {
                StringBuilder stringbuilder = new StringBuilder(getItemName(itemInput));
                String s = this.serializeComponents(itemInput, levelRegistry);

                if (!s.isEmpty()) {
                    stringbuilder.append('[');
                    stringbuilder.append(s);
                    stringbuilder.append(']');
                }

                return stringbuilder.toString();
            }

            private String getItemName(ItemInput itemInput) {
                return itemInput.item().unwrapKey()
                        .map(ResourceKey::identifier)
                        .map(Identifier::toString)
                        .orElseGet(() -> "unknown[" + itemInput.item() + "]");
            }

            private String serializeComponents(ItemInput itemInput, HolderLookup.Provider levelRegistries) {
                DynamicOps<Tag> dynamicops = levelRegistries.createSerializationContext(NbtOps.INSTANCE);

                return itemInput.components().entrySet().stream().flatMap((entry) -> {
                    DataComponentType<?> datacomponenttype = entry.getKey();
                    Identifier identifier = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(datacomponenttype);
                    if (identifier == null) {
                        return Stream.empty();
                    } else {
                        var optional = entry.getValue();
                        if (optional.isPresent()) {
                            TypedDataComponent<?> typeddatacomponent = TypedDataComponent.createUnchecked(datacomponenttype, optional.get());
                            return typeddatacomponent.encodeValue(dynamicops).result().stream().map((tag) -> identifier + "=" + tag);
                        } else {
                            return Stream.of("!" + identifier);
                        }
                    }
                }).collect(Collectors.joining(String.valueOf(',')));
            }

            @Override
            public <T> DataResult<Pair<ItemStack, T>> decode(DynamicOps<T> ops, T input) {
                throw new UnsupportedOperationException("Deserialization not supported");
            }
        };
    }

    Codec<BlockState> STATE_CODEC = new Codec<>() {

        @Override
        public <T> DataResult<T> encode(BlockState input, DynamicOps<T> ops, T prefix) {
            var block = input.getBlock();
            var id = BuiltInRegistries.BLOCK.getKey(block);
            var builder = new StringBuilder(id.toString());
            var defaultState = block.defaultBlockState();

            var properties = input.getValues()
                    .filter(value -> !value.value().equals(defaultState.getValue(value.property())))
                    .toList();

            if (!properties.isEmpty()) {
                builder.append('[');
                builder.append(properties.stream()
                        .map(Property.Value::toString)
                        .collect(Collectors.joining(",")));
                builder.append(']');
            }

            return DataResult.success(ops.createString(builder.toString()));
        }

        @Override
        public <T> DataResult<Pair<BlockState, T>> decode(DynamicOps<T> ops, T input) {
            throw new UnsupportedOperationException("Deserialization not supported");
        }
    };

    default void validate() {

    }

    @NotNull String getPath();
}
