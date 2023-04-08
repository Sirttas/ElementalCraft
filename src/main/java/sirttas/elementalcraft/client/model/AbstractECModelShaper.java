package sirttas.elementalcraft.client.model;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;


@OnlyIn(Dist.CLIENT)
public abstract class AbstractECModelShaper<T> {

    private final Map<T, BakedModel> cache = new IdentityHashMap<>();

    protected final ModelManager modelManager;

    protected AbstractECModelShaper(ModelManager modelManager) {
        this.modelManager = modelManager;
    }

    public ModelManager getModelManager() {
        return modelManager;
    }

    public BakedModel getBlockModel(T target) {
        var model = this.cache.get(target);

        if (model == null) {
            model = this.modelManager.getMissingModel();
        }
        return model;
    }

    public void rebuildCache() {
        this.cache.clear();
        this.cache.putAll(getModels());
    }

    protected abstract Map<? extends T, ? extends BakedModel> getModels();


    public abstract void registerModels(Consumer<ResourceLocation> addModel);
}
