package sirttas.elementalcraft.datagen.interaction.patchouli.builder;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.datagen.language.TranslationKeyValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BookBuilder implements PatchouliFile {

    public static final Codec<BookBuilder> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.STRING.fieldOf("name").forGetter(b -> b.name),
            Codec.STRING.fieldOf("landing_text").forGetter(b -> b.landingText),
            Identifier.CODEC.fieldOf("model").forGetter(b -> b.model),
            Codec.STRING.fieldOf("version").forGetter(b -> b.version),
            Identifier.CODEC.optionalFieldOf("creative_tab", null).forGetter(b -> b.creativeTab),
            Codec.BOOL.optionalFieldOf("i18n", false).forGetter(b -> b.i18n),
            Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("macros").forGetter(b -> b.macros),
            Codec.BOOL.fieldOf("use_resource_pack").forGetter(b -> true)
    ).apply(builder, (a1, a2, a3, a4, a5, a6, a7, a8) -> {
        throw new UnsupportedOperationException("Builder deserialization is not supported.");
    }));

    private static final ExistingFileHelper.ResourceType MODEL_RESOURCE_TYPE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".json", "models/item");

    private final Identifier id;
    private final Map<String, String> macros;
    final ExistingFileHelper existingFileHelper;
    final TranslationKeyValidator translationKeyValidator;

    private final List<CategoryBuilder> categories;

    private String name;
    private String landingText;
    private Identifier model;
    private String version;
    @Nullable
    private Identifier creativeTab;
    boolean i18n;

    public BookBuilder(Identifier id, ExistingFileHelper existingFileHelper, TranslationKeyValidator translationKeyValidator) {
        this.id = id;
        this.existingFileHelper = existingFileHelper;
        this.translationKeyValidator = translationKeyValidator;
        this.macros = new HashMap<>();
        this.categories = new ArrayList<>();
        this.name = "item." + id.getNamespace() + "." + id.getPath();
        this.landingText = "patchouli.default_landing_text";
        this.model = Identifier.fromNamespaceAndPath(id.getNamespace(), id.getPath());
        this.version = "0";
        this.i18n = false;
    }

    public BookBuilder name(String name) {
        this.name = name;
        return this;
    }

    public BookBuilder landingText(String landingText) {
        this.landingText = landingText;
        return this;
    }

    public BookBuilder model(Identifier model) {
        this.model = model;
        return this;
    }

    public BookBuilder version(String version) {
        this.version = version;
        return this;
    }

    public BookBuilder creativeTab(Holder<CreativeModeTab> creativeTab) {
        this.creativeTab = Objects.requireNonNull(creativeTab.getKey()).identifier();
        return this;
    }

    public BookBuilder i18n() {
        this.i18n = true;
        return this;
    }

    public BookBuilder macro(String key, String value) {
        this.macros.put(key, value);
        return this;
    }

    public CategoryBuilder category(String name) {
        var category = new CategoryBuilder(this, name)
                .sortNum(this.categories.size());

        this.categories.add(category);
        return category;
    }

    public @NotNull String getPath() {
        return "data/" + getRadical() + "/book.json";
    }

    protected @NotNull String getRadical() {
        return id.getNamespace() + "/patchouli_books/" + id.getPath();
    }

    public List<CategoryBuilder> getCategories() {
        return List.copyOf(categories);
    }

    public String getNamespace() {
        return id.getNamespace();
    }

    @Override
    public void validate() {
        if (StringUtils.isNotBlank(name)) {
            translationKeyValidator.checkHasKey(name);
        }
        if (StringUtils.isNotBlank(landingText)) {
            translationKeyValidator.checkHasKey(landingText);
        }
        if (model != null) {
            Preconditions.checkState(existingFileHelper.exists(model, MODEL_RESOURCE_TYPE), "Model %s does not exist.", model);
        }
        categories.forEach(CategoryBuilder::validate);
    }
}
