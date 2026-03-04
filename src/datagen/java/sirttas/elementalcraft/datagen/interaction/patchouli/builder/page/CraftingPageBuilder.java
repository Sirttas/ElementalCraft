package sirttas.elementalcraft.datagen.interaction.patchouli.builder.page;

import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sirttas.elementalcraft.datagen.language.TranslationKeyValidator;
import sirttas.elementalcraft.datagen.recipe.ECRecipeProvider;

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
    public void validate(ExistingFileHelper existingFileHelper, TranslationKeyValidator translationKeyValidator) {
        if (recipeId != null) {
            Preconditions.checkState(existingFileHelper.exists(recipeId, ECRecipeProvider.RECIPE), "Recipe %s does not exist.", recipeId);
        }
        if (recipe2Id != null) {
            Preconditions.checkState(existingFileHelper.exists(recipe2Id, ECRecipeProvider.RECIPE), "Recipe %s does not exist.", recipe2Id);
        }
    }
}
