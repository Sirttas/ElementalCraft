package sirttas.elementalcraft.rune;

import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Holder;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.client.model.ECModelResolver;

@OnlyIn(Dist.CLIENT)
public class RuneModelResolver extends ECModelResolver<RuneModel> {

    public static final Identifier IDENTIFIER = ElementalCraftApi.RUNE_MANAGER_KEY.identifier();
    private static final FileToIdConverter LISTER = FileToIdConverter.json(ElementalCraftApi.RUNE_MANAGER.getFolder());

    public RuneModelResolver(ModelManager modelManager) {
        super(modelManager, LISTER, RuneModel.Unbaked.CODEC);
    }

    public Material.Baked getSprite(Holder<@NotNull Rune> runeHolder) {
        return getModel(runeHolder.getKey().identifier()).getSprite();
    }
}
