package sirttas.elementalcraft.client.model;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientRegistryLayer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.PlaceholderLookupProvider;
import net.minecraft.util.StrictJsonParser;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import net.neoforged.neoforge.client.model.standalone.UnbakedStandaloneModel;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@OnlyIn(Dist.CLIENT)
public class ECModelResolver<T> {

    private static final Map<Identifier, ECModelResolver<?>> RESOLVERS = new HashMap<>();

    private final ModelManager modelManager;
    private final HashMap<Identifier, T> modelCache;
    private final HashMap<Identifier, StandaloneModelKey<@NotNull T>> keys;
    private final FileToIdConverter lister;
    private final Codec<? extends UnbakedStandaloneModel<@NotNull T>> codec;


    protected ECModelResolver(ModelManager modelManager, FileToIdConverter lister, Codec<? extends UnbakedStandaloneModel<@NotNull T>> codec) {
        this.modelManager = modelManager;
        this.modelCache = new HashMap<>();
        this.keys = new HashMap<>();
        this.lister = lister;
        this.codec = codec;
    }

    public static List<ECModelResolver<?>> getAll() {
        return List.copyOf(RESOLVERS.values());
    }

    @SuppressWarnings("unchecked")
    public static <T, R extends ECModelResolver<T>> R get(Identifier identifier) {
        return (R) RESOLVERS.get(identifier);
    }

    public static void register(Identifier identifier, ECModelResolver<?> resolver) {
        RESOLVERS.put(identifier, resolver);
    }

    public T getModel(Identifier identifier) {
        return modelCache.computeIfAbsent(identifier, i -> modelManager.getStandaloneModel(keys.get(i)));
    }

    public void registerModels(BiConsumer<StandaloneModelKey<@NotNull T>, UnbakedStandaloneModel<@NotNull T>> consumer) {
        keys.clear();
        modelCache.clear();
        resolveModels().forEach((id, model) -> {
            StandaloneModelKey<@NotNull T> key = new StandaloneModelKey<>(id::toString);

            keys.put(id, key);
            consumer.accept(key, model);
        });
    }

    protected Map<Identifier, UnbakedStandaloneModel<@NotNull T>> resolveModels() {
        RegistryAccess.Frozen staticRegistries = ClientRegistryLayer.createRegistryAccess().compositeAccess();
        PlaceholderLookupProvider lookup = new PlaceholderLookupProvider(staticRegistries);
        DynamicOps<JsonElement> ops = lookup.createSerializationContext(JsonOps.INSTANCE);

        return resolveModels(Minecraft.getInstance().getResourceManager(), ops);
    }

    private Map<Identifier, UnbakedStandaloneModel<@NotNull T>> resolveModels(ResourceManager resourceManager, DynamicOps<JsonElement> ops) {
        Map<Identifier, UnbakedStandaloneModel<@NotNull T>> models = new HashMap<>();

        lister.listMatchingResources(resourceManager).forEach((resourceId, resource) -> {
            Identifier modelId = lister.fileToId(resourceId);

            try (Reader reader = resource.openAsReader()) {
                codec.parse(ops, StrictJsonParser.parse(reader))
                        .result()
                        .ifPresent(model -> models.put(modelId, model));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return models;
    }
}
