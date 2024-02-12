package sirttas.elementalcraft.pureore.loader;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import sirttas.dpanvil.api.codec.Codecs;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.pureore.PureOre;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PatternPureOreLoader extends AbstractPureOreLoader {

    public static final Codec<PatternPureOreLoader> CODEC = RecordCodecBuilder.create(builder -> {
        var b1 = AbstractPureOreLoader.codec(builder);

        return new Products.P10<>(b1.t1(), b1.t2(), b1.t3(), b1.t4(), b1.t5(), b1.t6(),
                Codecs.PATTERN.fieldOf("tag_pattern").forGetter(l -> l.tagPattern),
                Codecs.PATTERN.listOf().optionalFieldOf("patterns", Collections.emptyList()).forGetter(l -> l.patterns),
                Codec.STRING.optionalFieldOf("namespace", ECNames.FORGE).forGetter(l -> l.namespace),
                Codecs.PATTERN.optionalFieldOf("namespace_pattern").forGetter(l -> l.namespacePattern)
        ).apply(builder, PatternPureOreLoader::new);
    });

    final Pattern tagPattern;
    final List<Pattern> patterns;
    final String namespace;
    final Optional<Pattern> namespacePattern;

    public PatternPureOreLoader(HolderSet<Item> source, int elementConsumption, int inputSize, int outputSize, double luckRatio, int order, Pattern tagPattern, List<Pattern> patterns, String namespace, Optional<Pattern> namespacePattern) {
        super(source, elementConsumption, inputSize, outputSize, luckRatio, order);
        this.tagPattern = tagPattern;
        this.patterns = patterns;
        this.namespace = namespace;
        this.namespacePattern = namespacePattern;
    }

    @Override
    public Codec<PatternPureOreLoader> codec() {
        return CODEC;
    }

    @Override
    protected GeneratedPureOre load(Map<ResourceLocation, PureOre> pureOres, Item ore) {
        var np = namespacePattern.orElseGet(() -> Pattern.compile("^" + namespace + "$"));
        var id = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(ore));
        var tags = ore.builtInRegistryHolder().tags()
                .filter(t -> {
                    var location = t.location();

                    return np.matcher(location.getNamespace()).find() && tagPattern.matcher(location.getPath()).find();
                }).toList();

        if (!tags.isEmpty()) {
            if (tags.size() > 1) {
                ElementalCraftApi.LOGGER.warn("Item {} has multiple tags matching \"{}:{}\":\r\n\t{}",
                        id::toString,
                        np::pattern,
                        tagPattern::pattern,
                        () -> tags.stream().map(t -> t.location().toString()).collect(Collectors.joining(", ")));
            }
            id = new ResourceLocation(namespace, cleanPath(tagPattern.matcher(tags.get(0).location().getPath()).replaceAll("")));
        } else {
            id = new ResourceLocation(id.getNamespace(), cleanPath(id.getPath()));
        }

        return new GeneratedPureOre(id, tags);
    }

    private String cleanPath(String path) {
        for (var pattern : patterns) {
            path = pattern.matcher(path).replaceAll("");
        }
        return path;
    }
}
