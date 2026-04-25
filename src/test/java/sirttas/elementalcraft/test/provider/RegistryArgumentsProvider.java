package sirttas.elementalcraft.test.provider;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.testframework.junit.EphemeralTestServerProvider;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.jupiter.params.support.ParameterDeclarations;
import sirttas.elementalcraft.test.annotation.RegistrySource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class RegistryArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<RegistrySource> {

    private ResourceKey<Registry<Object>> key;
    private List<ResourceKey<Object>> excludeKeys;

    @Override
    public void accept(RegistrySource registrySource) {
        key = ArgumentProviderHelper.getRegistryKey(registrySource.namespace(), registrySource.value());
        excludeKeys = Arrays.stream(registrySource.exclude())
                .map(exclude -> ResourceKey.create(key, ArgumentProviderHelper.getIdentifier(exclude)))
                .toList();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ParameterDeclarations parameters, ExtensionContext context) {
        return EphemeralTestServerProvider.grabServer().registryAccess().get(key).stream()
                .flatMap(registry -> registry.value().entrySet().stream())
                .filter(entry -> excludeKeys.stream().noneMatch(excludeKeys -> excludeKeys.equals(entry.getKey())))
                .map(entry -> Arguments.of(Named.of(entry.getKey().identifier().toString(), entry.getValue())));
    }
}
