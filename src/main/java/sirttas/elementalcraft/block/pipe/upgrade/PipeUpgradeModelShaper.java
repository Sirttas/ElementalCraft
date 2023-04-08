package sirttas.elementalcraft.block.pipe.upgrade;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeType;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.client.model.AbstractECModelShaper;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class PipeUpgradeModelShaper extends AbstractECModelShaper<PipeUpgradeType<?>> {

    public static final ResourceLocation NAME = ElementalCraft.createRL("pipe_upgrade");

    public PipeUpgradeModelShaper(ModelManager modelManager) {
        super(modelManager);
    }

    @Override
    protected Map<? extends PipeUpgradeType<?>, ? extends BakedModel> getModels() {
        var map = new IdentityHashMap<PipeUpgradeType<?>, BakedModel>();

        PipeUpgradeTypes.REGISTRY.get().getValues().forEach(type -> map.put(type, modelManager.getModel(getModelLocation(type))));
        return map;
    }

    @Override
    public void registerModels(Consumer<ResourceLocation> addModel) {
        PipeUpgradeTypes.REGISTRY.get().getValues().forEach(type -> addModel.accept(getModelLocation(type)));
    }

    private ResourceLocation getModelLocation(PipeUpgradeType<?> type) {
        var key = type.getKey();

        return new ResourceLocation(key.getNamespace(), PipeUpgrade.FOLDER + key.getPath());
    }
}
