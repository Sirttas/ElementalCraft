package sirttas.elementalcraft.datagen.interaction.patchouli.builder;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.datagen.interaction.patchouli.builder.page.PageBuilder;

import java.util.ArrayList;
import java.util.List;

public class EntryBuilder implements PatchouliFile {

    private final CategoryBuilder category;
    private final String fileName;
    private final List<PageBuilder> pages;
    private String name;
    private BookIcon icon;
    private boolean priority;
    private Identifier advancement;
    private Identifier turnIn;
    private int sortNum;
    private boolean ignoreValidation;

    protected EntryBuilder(CategoryBuilder category, String name) {
        this.category = category;
        this.fileName = name;
        this.pages = new ArrayList<>();
        this.name = category.getNamespace() + ".entry." + name;
        this.priority = false;
        this.sortNum = 0;
        this.ignoreValidation = false;
    }

    public static Codec<EntryBuilder> codec(HolderLookup.Provider lookupProvider) {
        return RecordCodecBuilder.create(builder -> builder.group(
                Codec.STRING.fieldOf("name").forGetter(b -> b.name),
                Identifier.CODEC.fieldOf("category").forGetter(b -> Identifier.fromNamespaceAndPath(b.category.getNamespace(), b.category.getFileName())),
                BookIcon.codec(lookupProvider).optionalFieldOf("icon", null).forGetter(b -> b.icon),
                Codec.BOOL.optionalFieldOf("priority", false).forGetter(b -> b.priority),
                Identifier.CODEC.optionalFieldOf("advancement", null).forGetter(b -> b.advancement),
                Identifier.CODEC.optionalFieldOf("turnin", null).forGetter(b -> b.turnIn),
                Codec.INT.fieldOf("sortnum").forGetter(b -> b.sortNum),
                PageBuilder.codec(lookupProvider).listOf().fieldOf("pages").forGetter(b -> b.pages)
        ).apply(builder, (a1, a2, a3, a4, a5, a6, a7, a8) -> {
            throw new UnsupportedOperationException("Builder deserialization is not supported.");
        }));
    }

    public EntryBuilder name(String name) {
        this.name = name;
        return this;
    }

    public EntryBuilder sortNum(int sortNum) {
        this.sortNum = sortNum;
        return this;
    }

    public EntryBuilder icon(Identifier icon) {
        this.icon = new BookIcon.TextureIcon(icon);
        return this;
    }

    public EntryBuilder icon(String icon) {
        return this.icon(Identifier.fromNamespaceAndPath(category.getNamespace(), icon));
    }

    public EntryBuilder icon(ItemStack icon) {
        this.icon = new BookIcon.StackIcon(icon);
        return this;
    }

    public EntryBuilder icon(ItemLike icon) {
        return this.icon(new ItemStack(icon));
    }

    public EntryBuilder priority() {
        this.priority = true;
        return this;
    }

    public EntryBuilder advancement(Identifier advancement) {
        this.advancement = advancement;
        return this;
    }

    public EntryBuilder turnIn(Identifier turnIn) {
        this.turnIn = turnIn;
        return this;
    }

    public EntryBuilder page(PageBuilder page) {
        this.pages.add(page);
        return this;
    }

    public EntryBuilder ignoreValidation() {
        this.ignoreValidation = true;
        return this;
    }

    @Override
    public @NotNull String getPath() {
        return "assets/" + category.getRadical() + "/en_us/entries/" + category.getFileName() + "/" + fileName + ".json";
    }

    @Override
    public void validate() {
        if (ignoreValidation) {
            return;
        }
        if (StringUtils.isNotBlank(name)) {
            category.book.translationKeyValidator.checkHasKey(name);
        }
        for (PageBuilder page : pages) {
            page.validate(category.book.translationKeyValidator);
        }
    }
}
