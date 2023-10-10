package sirttas.elementalcraft.datagen.managed.pure.ore.loader.loader;

import net.minecraft.core.HolderSet;
import net.minecraft.world.item.Item;
import org.codehaus.plexus.util.StringUtils;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.pureore.loader.IPureOreLoader;
import sirttas.elementalcraft.pureore.loader.PatternPureOreLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class PatternPureOreLoaderBuilder extends AbstractPureOreLoaderBuilder {

    private final String tagPattern;
    private final List<String> patterns;
    private String namespace;
    private String namespacePattern;

    protected PatternPureOreLoaderBuilder(HolderSet<Item> source, String tagPattern) {
        super(source);
        this.tagPattern = tagPattern;
        this.patterns = new ArrayList<>();
        this.namespace = ECNames.FORGE;
        this.namespacePattern = "";
    }

    public PatternPureOreLoaderBuilder pattern(String pattern) {
        this.patterns.add(pattern);
        return this;
    }

    public PatternPureOreLoaderBuilder patterns(String... patterns) {
        this.patterns.addAll(Arrays.asList(patterns));
        return this;
    }

    public PatternPureOreLoaderBuilder namespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public PatternPureOreLoaderBuilder namespacePattern(String namespacePattern) {
        this.namespacePattern = namespacePattern;
        return this;
    }

    @Override
    public IPureOreLoader build() {
        return new PatternPureOreLoader(this.source, this.elementConsumption, this.inputSize, this.outputSize, this.luckRatio, this.order, Pattern.compile(this.tagPattern), this.patterns.stream()
                .map(Pattern::compile)
                .toList(), this.namespace, StringUtils.isNotBlank(this.namespacePattern) ? Optional.of(Pattern.compile(this.namespacePattern)) : Optional.empty());
    }
}
