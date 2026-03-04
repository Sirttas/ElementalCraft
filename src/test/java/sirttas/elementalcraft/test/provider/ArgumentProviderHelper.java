package sirttas.elementalcraft.test.provider;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.test.annotation.RegistrySource;
import sirttas.elementalcraft.test.annotation.TagSource;

public class ArgumentProviderHelper {

    private ArgumentProviderHelper() { }

    public static <T> ResourceKey<Registry<T>> getRegistryKey(String namespace, String path) {
        return ResourceKey.createRegistryKey(getIdentifier(namespace, path));
    }

    public static @NotNull Identifier getIdentifier(TagSource.Tag tag) {
        return getIdentifier(tag.namespace(), tag.value());
    }

    public static @NotNull Identifier getIdentifier(RegistrySource.Exclude exclude) {
        return getIdentifier(exclude.namespace(), exclude.value());
    }

    public static @NotNull Identifier getIdentifier(String namespace, String path) {
        Identifier location;

        if (StringUtils.isNotBlank(namespace)) {
            location = Identifier.fromNamespaceAndPath(namespace, path);
        } else {
            location = Identifier.parse(path);
        }
        return location;
    }
}
