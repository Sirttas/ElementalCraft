package sirttas.elementalcraft.datagen.interaction.patchouli.builder.page;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.StringUtils;
import sirttas.elementalcraft.datagen.interaction.patchouli.builder.PatchouliFile;
import sirttas.elementalcraft.datagen.language.TranslationKeyValidator;

public record SpotlightPageBuilder(
        ItemStack item,
        String text,
        boolean linkRecipe
) implements PageBuilder {

    public static final PageBuilderType TYPE = PageBuilderType.register(new PageBuilderType() {
        @Override
        public String name() {
            return "spotlight";
        }

        @Override
        public MapCodec<SpotlightPageBuilder> codec(HolderLookup.Provider lookupProvider) {
            return RecordCodecBuilder.mapCodec(builder -> builder.group(
                    Codec.STRING.optionalFieldOf("text", "").forGetter(SpotlightPageBuilder::text),
                    Codec.BOOL.fieldOf("link_recipe").forGetter(SpotlightPageBuilder::linkRecipe),
                    PatchouliFile.stackCodec(lookupProvider).fieldOf("item").forGetter(SpotlightPageBuilder::item)
            ).apply(builder, (a1, a2, a3) -> {
                throw new UnsupportedOperationException("Builder deserialization is not supported.");
            }));
        }
    });

    @Override
    public PageBuilderType getType() {
        return TYPE;
    }

    @Override
    public void validate(TranslationKeyValidator translationKeyValidator) {
        if (StringUtils.isNotBlank(text)) {
            translationKeyValidator.checkHasKey(text);
        }
    }
}
