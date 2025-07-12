package sirttas.elementalcraft.pureore.loader;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import sirttas.dpanvil.api.codec.Codecs;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.pureore.PureOreException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PatternPureOreLoader extends AbstractPureOreLoader {

    public static final MapCodec<PatternPureOreLoader> CODEC = RecordCodecBuilder.mapCodec(builder -> {
        var b1 = AbstractPureOreLoader.codec(builder);

        return new Products.P10<>(b1.t1(), b1.t2(), b1.t3(), b1.t4(), b1.t5(), b1.t6(),
                Codecs.PATTERN.fieldOf("tag_pattern").forGetter(l -> l.tagPattern),
                Codecs.PATTERN.listOf().optionalFieldOf("patterns", Collections.emptyList()).forGetter(l -> l.patterns),
                Codec.STRING.optionalFieldOf("namespace", ECNames.COMMON_TAGS_NAMESPACE).forGetter(l -> l.namespace),
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
    public PureOreLoaderType<PatternPureOreLoader> type() {
        return PureOreLoaderTypes.PATTERN.get();
    }

    @Override
    protected PureOreTagGroup load(Map<ResourceLocation, LoadedPureOre> pureOres, Holder<Item> ore) {
        var np = namespacePattern.orElseGet(() -> Pattern.compile("^" + namespace + "$"));
        var key = ore.getKey();

        if (key == null) {
            throw new PureOreException("Holder " + ore + " has no key");
        }

        var id = key.location();
        var tags = ore.tags()
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
            id = ResourceLocation.fromNamespaceAndPath(namespace, cleanPath(tagPattern.matcher(tags.getFirst().location().getPath()).replaceAll("")));
        } else {
            id = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), cleanPath(id.getPath()));
        }

        return new PureOreTagGroup(id, tags);
    }

    private String cleanPath(String path) {
        for (var pattern : patterns) {
            path = pattern.matcher(path).replaceAll("");
        }
        return path;
    }
}
