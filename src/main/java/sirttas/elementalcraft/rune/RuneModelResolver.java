package sirttas.elementalcraft.rune;

import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Holder;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.client.model.ECModelResolver;

public class RuneModelResolver extends ECModelResolver<RuneModel> {

    public static final Identifier IDENTIFIER = ElementalCraftApi.RUNE_MANAGER_KEY.identifier();
    private static final FileToIdConverter LISTER = FileToIdConverter.json(ElementalCraftApi.RUNE_MANAGER.getFolder());

    public RuneModelResolver(ModelManager modelManager) {
        super(modelManager, LISTER, RuneModel.Unbaked.CODEC);
    }

    public Material.Baked getSprite(Holder<Rune> runeHolder) {
        return getModel(runeHolder.getKey().identifier()).getSprite();
    }
}
