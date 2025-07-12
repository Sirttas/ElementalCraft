package sirttas.elementalcraft.datagen.interaction.patchouli.builder.page;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.apache.commons.lang3.StringUtils;
import sirttas.elementalcraft.datagen.language.TranslationKeyValidator;

import java.util.List;

public record ImagePageBuilder(
        List<ResourceLocation> images,
        String text,
        boolean border
) implements PageBuilder {

    private static final MapCodec<ImagePageBuilder> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            ResourceLocation.CODEC.listOf().fieldOf("images").forGetter(b -> b.images),
            Codec.STRING.optionalFieldOf("text", "").forGetter(b -> b.text),
            Codec.BOOL.optionalFieldOf("border", false).forGetter(b -> b.border)
    ).apply(builder, (a1, a2, a3) -> {
        throw new UnsupportedOperationException("Builder deserialization is not supported.");
    }));

    private static final PageBuilderType TYPE = PageBuilderType.register("image", CODEC);

    @Override
    public PageBuilderType getType() {
        return TYPE;
    }

    @Override
    public void validate(ExistingFileHelper existingFileHelper, TranslationKeyValidator translationKeyValidator) {
        for (ResourceLocation image : images) {
            Preconditions.checkState(existingFileHelper.exists(image, PackType.CLIENT_RESOURCES), "Image %s does not exist.", image);
        }
        if (StringUtils.isNotBlank(text)) {
            translationKeyValidator.checkHasKey(text);
        }
    }

}
