package sirttas.elementalcraft.datagen.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import sirttas.elementalcraft.api.ElementalCraftApi;

import javax.annotation.Nonnull;
import java.util.Arrays;

public abstract class AbstractECRegistryBootstrap<T> implements RegistrySetBuilder.RegistryBootstrap<T> {

    private final ResourceKey<Registry<T>> key;
    private BootstapContext<T> context;

    protected AbstractECRegistryBootstrap(ResourceKey<Registry<T>> key) {
        this.key = key;
    }

    @Override
    public void run(@Nonnull BootstapContext<T> context) {
        this.context = context;
        gather();
    }

    public <U> HolderLookup.RegistryLookup<U> registryLookup(ResourceKey<Registry<U>> registry) {
        return context.registryLookup(registry).orElseThrow();
    }

    public <U> HolderSet<U> createHolderSet(TagKey<U> tag) {
        return context.lookup(tag.registry()).getOrThrow(tag);
    }

    public <U> HolderSet<U> createHolderSet(ResourceKey<Registry<U>> registry, String ... names) {
        return createHolderSet(registry, Arrays.stream(names)
                .map(ElementalCraftApi::createRL)
                .toArray(ResourceLocation[]::new));
    }

    public <U> HolderSet<U> createHolderSet(ResourceKey<Registry<U>> registry, ResourceLocation ... names) {
        var reg = context.lookup(registry);

        return HolderSet.direct(Arrays.stream(names)
                .map(n -> reg.getOrThrow(ResourceKey.create(registry, n)))
                .toList());
    }

    public <U> Holder<U> getReference(ResourceKey<Registry<U>> registry, String name) {
        return getReference(registry, ElementalCraftApi.createRL(name));
    }

    public <U> Holder<U> getReference(ResourceKey<Registry<U>> registry, ResourceLocation name) {
        return context.lookup(registry).getOrThrow(ResourceKey.create(registry, name));
    }

    public <U> Holder<U> getReference(ResourceKey<Registry<U>> registry, U entry) {
        return context.registryLookup(registry).orElseThrow().listElements()
                .filter(e -> e.value() == entry)
                .findFirst()
                .orElseThrow();
    }

    protected abstract void gather();

    protected Holder.Reference<T> add(String name, T entry) {
        return add(ResourceKey.create(key, ElementalCraftApi.createRL(name)), entry);
    }

    protected Holder.Reference<T> add(ResourceKey<T> k, T entry) {
        return context.register(k, entry);
    }

}
