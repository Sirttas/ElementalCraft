package sirttas.elementalcraft.block.pipe.upgrade;

import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.client.model.ECModelResolver;

public class PipeUpgradeModelResolver extends ECModelResolver<PipeUpgradeModel, PipeUpgradeModel.Unbaked> {

    public static final Identifier IDENTIFIER = ElementalCraftApi.RUNE_MANAGER_KEY.identifier();
    private static final FileToIdConverter LISTER = FileToIdConverter.json(PipeUpgrade.FOLDER);

    public PipeUpgradeModelResolver(ModelManager modelManager) {
        super(modelManager, LISTER, null); // FIXME
    }
}
