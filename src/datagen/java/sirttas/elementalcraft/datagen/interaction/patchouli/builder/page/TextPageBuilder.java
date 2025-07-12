package sirttas.elementalcraft.datagen.interaction.patchouli.builder.page;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.apache.commons.lang3.StringUtils;
import sirttas.elementalcraft.datagen.language.TranslationKeyValidator;

public record TextPageBuilder(String text) implements PageBuilder {

    private static final MapCodec<TextPageBuilder> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.STRING.fieldOf("text").forGetter(b -> b.text)
    ).apply(builder, (a1) -> {
        throw new UnsupportedOperationException("Builder deserialization is not supported.");
    }));
    private static final PageBuilderType TYPE = PageBuilderType.register("text", CODEC);

    @Override
    public PageBuilderType getType() {
        return TYPE;
    }

    @Override
    public void validate(ExistingFileHelper existingFileHelper, TranslationKeyValidator translationKeyValidator) {
        if (StringUtils.isNotBlank(text)) {
            translationKeyValidator.checkHasKey(text);
        }
    }
}
