package sirttas.elementalcraft.test.provider;

import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.neoforged.testframework.junit.EphemeralTestServerProvider;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.jupiter.params.support.ParameterDeclarations;
import sirttas.elementalcraft.test.annotation.TagSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TagArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<TagSource> {

    private ResourceKey<Registry<Object>> registryKey;
    private List<TagKey<Object>> tagKeys;
    private List<ResourceKey<Object>> excludeKeys;

    @Override
    public void accept(TagSource tagSource) {
        registryKey = ArgumentProviderHelper.getRegistryKey(tagSource.registryNamespace(), tagSource.registry());
        tagKeys = Arrays.stream(tagSource.value())
                .map(tag -> TagKey.create(registryKey, ArgumentProviderHelper.getIdentifier(tag)))
                .toList();
        excludeKeys = Arrays.stream(tagSource.exclude())
                .map(exclude -> ResourceKey.create(registryKey, ArgumentProviderHelper.getIdentifier(exclude)))
                .toList();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ParameterDeclarations parameters, ExtensionContext context) {
        return EphemeralTestServerProvider.grabServer().registryAccess().get(registryKey).stream()
                .flatMap(registry -> tagKeys.stream()
                        .flatMap(tagKey -> registry.value().get(tagKey).stream()))
                .flatMap(HolderSet.ListBacked::stream)
                .distinct()
                .filter(holder -> excludeKeys.stream().noneMatch(holder::is))
                .map(holder -> {
                    var key = holder.getKey();

                    if (key != null) {
                        return Arguments.of(Named.of(key.identifier().toString(), holder.value()));
                    }
                    return Arguments.of(holder.value());
                });
    }
}
