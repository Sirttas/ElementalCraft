package sirttas.elementalcraft.client.model;

import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


@OnlyIn(Dist.CLIENT)
public class ECModelShapers {

    private static final Map<ResourceLocation,  Function<ModelManager, AbstractECModelShaper<?>>> FACTORIES = new HashMap<>();
    private static final Map<ResourceLocation,  AbstractECModelShaper<?>> SHAPERS = new HashMap<>();

    private ECModelShapers() { }

    @SuppressWarnings("unchecked")
    public static <T> AbstractECModelShaper<T> get(ResourceLocation name) {
        return (AbstractECModelShaper<T>) SHAPERS.get(name);
    }

    public static List<AbstractECModelShaper<?>> getAll() {
        return List.copyOf(SHAPERS.values());
    }

    public static void register(ResourceLocation name, Function<ModelManager, AbstractECModelShaper<?>> factory) {
        FACTORIES.put(name, factory);
    }

    static void init(ModelManager modelManager) {
        if (!SHAPERS.isEmpty()) {
            throw new IllegalStateException("Model shapers already initialized");
        }
        FACTORIES.forEach((name, factory) -> SHAPERS.put(name, factory.apply(modelManager)));
    }

}
