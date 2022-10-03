package sirttas.elementalcraft.datagen;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import sirttas.dpanvil.api.codec.CodecHelper;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.BiConsumer;

public abstract class AbstractECJsonCodecProvider<T> extends JsonCodecProvider<T> {

    protected AbstractECJsonCodecProvider(DataGenerator dataGenerator, ExistingFileHelper existingFileHelper, ResourceKey<Registry<T>> registry) {
        super(dataGenerator, existingFileHelper, ElementalCraftApi.MODID, CodecHelper.getRegistryOps(JsonOps.INSTANCE), PackType.SERVER_DATA, getDirectory(registry), getCodec(registry), new HashMap<>());
    }

    public <U> HolderSet<U> createHolderSet(TagKey<U> tag) {
        return getRegistry(tag.registry()).getOrCreateTag(tag);
    }

    public <U> HolderSet<U> createHolderSet(ResourceKey<Registry<U>> registry, String ... names) {
        return createHolderSet(registry, Arrays.stream(names).map(ElementalCraft::createRL).toArray(ResourceLocation[]::new));
    }

    public <U> HolderSet<U> createHolderSet(ResourceKey<Registry<U>> registry, ResourceLocation ... names) {
        var reg = getRegistry(registry);

        return HolderSet.direct(Arrays.stream(names)
                .map(n -> reg.getOrCreateHolderOrThrow(ResourceKey.create(registry, n)))
                .toList());
    }

    public <U> Holder<U> getReference(ResourceKey<Registry<U>> registry, String name) {
        return getReference(registry, ElementalCraft.createRL(name));
    }

    public <U> Holder<U> getReference(ResourceKey<Registry<U>> registry, ResourceLocation name) {
        return getRegistry(registry).getOrCreateHolderOrThrow(ResourceKey.create(registry, name));
    }

    @Nonnull
    protected <U> Registry<U> getRegistry(ResourceKey<? extends Registry<U>> registry) {
        return getRegistryOps().registry(registry).orElseThrow(() -> new IllegalStateException("Registry " + registry + " not found"));
    }

    protected RegistryOps<JsonElement> getRegistryOps() {
        return (RegistryOps<JsonElement>) dynamicOps;
    }

    private static String getDirectory(ResourceKey<? extends Registry<?>> registry) {
        final ResourceLocation registryId = registry.location();

        return registryId.getNamespace().equals("minecraft") ? registryId.getPath()  : registryId.getNamespace() + "/" + registryId.getPath();
    }

    @SuppressWarnings("unchecked")
    private static <T> Codec<T> getCodec(ResourceKey<Registry<T>> registry) {
        return (Codec<T>) RegistryAccess.REGISTRIES.get(registry).codec();
    }

    protected abstract void gather();

    @Override
    protected void gather(@Nonnull BiConsumer<ResourceLocation, T> consumer) {
        entries.clear();
        gather();
       super.gather(consumer);
    }

    protected T add(String name, T entry) {
        entries.compute(ElementalCraft.createRL(name), (k, v) -> {
            if (v != null) {
                throw new IllegalStateException("Duplicate entry id: " + k);
            }
            return entry;
        });
        return entry;
    }

}
