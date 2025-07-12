package sirttas.elementalcraft.datagen.interaction.patchouli.builder.page;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public interface PageBuilderType {

    Map<String, PageBuilderType> TYPES = new HashMap<>();
    Codec<PageBuilderType> CODEC = Codec.STRING.xmap(TYPES::get, PageBuilderType::name);

    String name();

    MapCodec<? extends PageBuilder> codec(HolderLookup.Provider lookupProvider);

    static PageBuilderType register(PageBuilderType type) {
        TYPES.put(type.name(), type);
        return type;
    }

    static PageBuilderType register(String name, MapCodec<? extends PageBuilder> codec) {
        return register(new PageBuilderType() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public MapCodec<? extends PageBuilder> codec(HolderLookup.Provider lookupProvider) {
                return codec;
            }
        });
    }

    static PageBuilderType register(ResourceLocation name, MapCodec<? extends PageBuilder> codec) {
        return register(name.toString(), codec);
    }

}
