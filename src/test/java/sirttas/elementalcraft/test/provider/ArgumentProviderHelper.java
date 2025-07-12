package sirttas.elementalcraft.test.provider;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.test.annotation.RegistrySource;
import sirttas.elementalcraft.test.annotation.TagSource;

public class ArgumentProviderHelper {

    private ArgumentProviderHelper() { }

    public static <T> ResourceKey<Registry<T>> getRegistryKey(String namespace, String path) {
        return ResourceKey.createRegistryKey(getResourceLocation(namespace, path));
    }

    public static @NotNull ResourceLocation getResourceLocation(TagSource.Tag tag) {
        return getResourceLocation(tag.namespace(), tag.value());
    }

    public static @NotNull ResourceLocation getResourceLocation(RegistrySource.Exclude exclude) {
        return getResourceLocation(exclude.namespace(), exclude.value());
    }

    public static @NotNull ResourceLocation getResourceLocation(String namespace, String path) {
        ResourceLocation location;

        if (StringUtils.isNotBlank(namespace)) {
            location = ResourceLocation.fromNamespaceAndPath(namespace, path);
        } else {
            location = ResourceLocation.parse(path);
        }
        return location;
    }
}
