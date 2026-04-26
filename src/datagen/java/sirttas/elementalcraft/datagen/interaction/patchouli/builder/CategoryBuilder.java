package sirttas.elementalcraft.datagen.interaction.patchouli.builder;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CategoryBuilder implements PatchouliFile {

    final BookBuilder book;
    private final String fileName;

    private final List<EntryBuilder> entries;

    private String name;
    private String description;
    private BookIcon icon;
    private int sortNum;

    protected CategoryBuilder(BookBuilder book, String name) {
        this.book = book;
        this.fileName = name;
        this.entries = new ArrayList<>();
        this.name = this.book.getNamespace() + ".category." + name;
        this.description = this.name + ".desc";
        this.sortNum = 0;
    }

    public static Codec<CategoryBuilder> codec(HolderLookup.Provider lookupProvider) {
        return RecordCodecBuilder.create(builder -> builder.group(
                Codec.STRING.fieldOf("name").forGetter(b -> b.name),
                Codec.STRING.fieldOf("description").forGetter(b -> b.description),
                BookIcon.codec(lookupProvider).optionalFieldOf("icon", null).forGetter(b -> b.icon),
                Codec.INT.fieldOf("sortnum").forGetter(b -> b.sortNum)
        ).apply(builder, (a1, a2, a3, a4) -> {
            throw new UnsupportedOperationException("Builder deserialization is not supported.");
        }));
    }

    public CategoryBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CategoryBuilder description(String description) {
        this.description = description;
        return this;
    }

    public CategoryBuilder icon(Identifier icon) {
        this.icon = new BookIcon.TextureIcon(icon);
        return this;
    }

    public CategoryBuilder icon(String icon) {
        return this.icon(Identifier.fromNamespaceAndPath(book.getNamespace(), icon));
    }

    public CategoryBuilder icon(ItemStack icon) {
        this.icon = new BookIcon.StackIcon(icon);
        return this;
    }

    public CategoryBuilder icon(ItemLike icon) {
        return this.icon(new ItemStack(icon));
    }

    public CategoryBuilder sortNum(int sortNum) {
        this.sortNum = sortNum;
        return this;
    }

    public EntryBuilder entry(String name) {
        var entry = new EntryBuilder(this, name)
                .sortNum(entries.size());
        entries.add(entry);
        return entry;
    }

    public EntryBuilder entry(ItemLike item) {
        var resolvedItem = item.asItem();
        var id = BuiltInRegistries.ITEM.getKey(resolvedItem);

        return entry(id.getPath())
                .name(resolvedItem.getDescriptionId())
                .icon(item);
    }

    @Override
    public @NotNull String getPath() {
        return "assets/" + book.getRadical() + "/en_us/categories/" + fileName + ".json";
    }

    protected @NotNull String getRadical() {
        return book.getRadical();
    }

    public String getNamespace() {
        return book.getNamespace();
    }

    public String getFileName() {
        return fileName;
    }

    public Collection<EntryBuilder> getEntries() {
        return List.copyOf(entries);
    }

    @Override
    public void validate() {
        if (icon != null) {
            icon.validate();
        }
        entries.forEach(EntryBuilder::validate);
    }
}
