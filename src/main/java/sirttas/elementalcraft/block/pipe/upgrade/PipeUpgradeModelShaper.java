package sirttas.elementalcraft.block.pipe.upgrade;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelIdentifier;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeType;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.client.model.AbstractECModelShaper;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class PipeUpgradeModelShaper extends AbstractECModelShaper<PipeUpgradeType<?>> {

    public static final Identifier NAME = ElementalCraftApi.createRL("pipe_upgrade");

    public PipeUpgradeModelShaper(ModelManager modelManager) {
        super(modelManager);
    }

    @Override
    protected Map<? extends PipeUpgradeType<?>, ? extends BakedModel> getModels() {
        var map = new IdentityHashMap<PipeUpgradeType<?>, BakedModel>();

        PipeUpgradeTypes.REGISTRY.forEach(type -> map.put(type, modelManager.getModel(getModelLocation(type))));
        return map;
    }

    @Override
    public void registerModels(Consumer<ModelIdentifier> addModel) {
        PipeUpgradeTypes.REGISTRY.forEach(type -> addModel.accept(getModelLocation(type)));
    }

    private ModelIdentifier getModelLocation(PipeUpgradeType<?> type) {
        var key = type.getKey();

        return ModelIdentifier.standalone(Identifier.fromNamespaceAndPath(key.getNamespace(), PipeUpgrade.FOLDER + key.getPath()));
    }
}
