package sirttas.elementalcraft.block.pipe.upgrade.render.model;

import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeType;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.client.model.ECModelResolver;

public class PipeUpgradeModelResolver extends ECModelResolver<PipeUpgradeModel> {

    public static final Identifier IDENTIFIER = PipeUpgradeTypes.REGISTRY.key().identifier();
    private static final FileToIdConverter LISTER = FileToIdConverter.json(PipeUpgrade.FOLDER);

    public PipeUpgradeModelResolver(ModelManager modelManager) {
        super(modelManager, LISTER, PipeUpgradeModel.Unbaked.CODEC);
    }

    public PipeUpgradeModel getModel(PipeUpgradeType<?> type) {
        return getModel(type.getKey());
    }

    public PipeUpgradeModel getModel(PipeUpgrade upgrade) {
        if  (upgrade == null) {
            return null;
        }
        return getModel(upgrade.getKey());
    }
}
