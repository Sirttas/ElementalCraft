package sirttas.elementalcraft.datagen.interaction.patchouli.builder;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Collectors;

public interface PatchouliFile {

    static Codec<ItemStack> stackCodec(HolderLookup.Provider lookupProvider) {
        return new Codec<>() {

            @Override
            public <T> DataResult<T> encode(ItemStack input, DynamicOps<T> ops, T prefix) {
                var itemInput = new ItemInput(input.getItemHolder(), input.getComponentsPatch());

                return DataResult.success(ops.createString(itemInput.serialize(lookupProvider)));
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

            var properties = input.getValues().entrySet().stream()
                    .filter(e -> !e.getValue().equals(defaultState.getValue(e.getKey())))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            if (!properties.isEmpty()) {
                builder.append('[');
                builder.append(properties.entrySet().stream()
                        .map(e -> StateHolder.PROPERTY_ENTRY_TO_STRING_FUNCTION.apply(Map.entry(e.getKey(), e.getValue())))
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
