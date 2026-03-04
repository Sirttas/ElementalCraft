package sirttas.elementalcraft.datagen.interaction.patchouli.builder;

import com.google.common.base.Preconditions;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

interface BookIcon {

    Codec<TextureIcon> TEXTURE_CODEC = Identifier.CODEC.xmap(TextureIcon::new, TextureIcon::texture);

    static Codec<StackIcon> stackCodec(HolderLookup.Provider lookupProvider) {
        return PatchouliFile.stackCodec(lookupProvider).xmap(StackIcon::new, StackIcon::stack);
    }

    static Codec<BookIcon> codec(HolderLookup.Provider lookupProvider) {
        return new Codec<>() {
            @Override
            public <T> DataResult<T> encode(BookIcon input, DynamicOps<T> ops, T prefix) {
                return switch (input) {
                    case TextureIcon textureIcon -> TEXTURE_CODEC.encode(textureIcon, ops, prefix);
                    case StackIcon stackIcon -> stackCodec(lookupProvider).encode(stackIcon, ops, prefix);
                    default -> throw new UnsupportedOperationException("Unknown icon type.");
                };
            }

            @Override
            public <T> DataResult<Pair<BookIcon, T>> decode(DynamicOps<T> ops, T input) {
                throw new UnsupportedOperationException("Icon deserialization is not supported.");
            }
        };
    }

    default void validate(ExistingFileHelper existingFileHelper) {}

    record TextureIcon(
            Identifier texture
    ) implements BookIcon {

        @Override
        public void validate(ExistingFileHelper existingFileHelper) {
            Preconditions.checkState(existingFileHelper.exists(texture, PackType.CLIENT_RESOURCES), "Texture %s does not exist.", texture);
        }
    }

    record StackIcon(
            ItemStack stack
    ) implements BookIcon {}
}
