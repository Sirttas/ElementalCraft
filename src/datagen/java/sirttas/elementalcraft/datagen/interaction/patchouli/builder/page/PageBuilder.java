package sirttas.elementalcraft.datagen.interaction.patchouli.builder.page;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sirttas.elementalcraft.datagen.language.TranslationKeyValidator;

import java.util.List;

public interface PageBuilder {

    static Codec<PageBuilder> codec(HolderLookup.Provider lookupProvider) {
        return PageBuilderType.CODEC.dispatch(PageBuilder::getType, type -> type.codec(lookupProvider));
    }

    static PageBuilder text(String text) {
        return new TextPageBuilder(text);
    }

    static CraftingPageBuilder crafting(Identifier recipeId, Identifier recipe2Id) {
        return new CraftingPageBuilder(recipeId, recipe2Id);
    }

    static CraftingPageBuilder crafting(ItemLike item, ItemLike item2) {
        return new CraftingPageBuilder(BuiltInRegistries.ITEM.getKey(item.asItem()), BuiltInRegistries.ITEM.getKey(item2.asItem()));
    }

    static CraftingPageBuilder crafting(ItemLike item) {
        return new CraftingPageBuilder(BuiltInRegistries.ITEM.getKey(item.asItem()), null);
    }

    static SpotlightPageBuilder spotlight(ItemStack stack, String text, boolean linkRecipe) {
        return new SpotlightPageBuilder(stack, text, linkRecipe);
    }

    static SpotlightPageBuilder spotlight(Item stack, boolean linkRecipe) {
        return spotlight(new ItemStack(stack), "", linkRecipe);
    }

    static SpotlightPageBuilder spotlight(ItemStack stack) {
        return spotlight(stack, "", false);
    }

    static MultiblockPageBuilder multiblock() {
        return new MultiblockPageBuilder();
    }

    static ImagePageBuilder image(String text, boolean border, Identifier... images) {
        return new ImagePageBuilder(List.of(images), text, border);
    }

    static ImagePageBuilder image(String text,  Identifier... images) {
        return new ImagePageBuilder(List.of(images), text, false);
    }

    PageBuilderType getType();

    default void validate(ExistingFileHelper existingFileHelper, TranslationKeyValidator translationKeyValidator) {}
}
