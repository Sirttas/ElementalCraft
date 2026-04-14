package sirttas.elementalcraft.datagen.interaction.patchouli.builder.page;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import sirttas.elementalcraft.datagen.language.TranslationKeyValidator;

public record CraftingPageBuilder(
    Identifier recipeId,
    Identifier recipe2Id
) implements PageBuilder {

    private static final MapCodec<CraftingPageBuilder> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Identifier.CODEC.optionalFieldOf("recipe", null).forGetter(b -> b.recipeId),
            Identifier.CODEC.optionalFieldOf("recipe2", null).forGetter(b -> b.recipe2Id)
    ).apply(builder, (a1, a2) -> {
        throw new UnsupportedOperationException("Builder deserialization is not supported.");
    }));

    private static final PageBuilderType TYPE = PageBuilderType.register("crafting", CODEC);

    @Override
    public PageBuilderType getType() {
        return TYPE;
    }

    @Override
    public void validate(TranslationKeyValidator translationKeyValidator) {
    }
}
