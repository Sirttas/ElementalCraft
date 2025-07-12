package sirttas.elementalcraft.datagen.interaction.patchouli.builder.page.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.apache.commons.lang3.StringUtils;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.datagen.interaction.patchouli.builder.page.PageBuilder;
import sirttas.elementalcraft.datagen.interaction.patchouli.builder.page.PageBuilderType;
import sirttas.elementalcraft.datagen.language.TranslationKeyValidator;

public record CheckPageBuilder(
        String header,
        String valid,
        String paused,
        String invalid
) implements PageBuilder {

    private static final MapCodec<CheckPageBuilder> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.STRING.fieldOf("header").forGetter(b -> b.header),
            Codec.STRING.fieldOf("valid").forGetter(b -> b.valid),
            Codec.STRING.fieldOf("paused").forGetter(b -> b.paused),
            Codec.STRING.fieldOf("invalid").forGetter(b -> b.invalid)
    ).apply(builder, (a1, a2, a3, a4) -> {
        throw new UnsupportedOperationException("Builder deserialization is not supported.");
    }));
    private static final PageBuilderType TYPE = PageBuilderType.register(ElementalCraftApi.createRL("check"), CODEC);

    @Override
    public PageBuilderType getType() {
        return TYPE;
    }

    @Override
    public void validate(ExistingFileHelper existingFileHelper, TranslationKeyValidator translationKeyValidator) {
        if (StringUtils.isNotBlank(header)) {
            translationKeyValidator.checkHasKey(header);
        }
        if (StringUtils.isNotBlank(valid)) {
            translationKeyValidator.checkHasKey(valid);
        }
        if (StringUtils.isNotBlank(paused)) {
            translationKeyValidator.checkHasKey(paused);
        }
        if (StringUtils.isNotBlank(invalid)) {
            translationKeyValidator.checkHasKey(invalid);
        }
    }
}
