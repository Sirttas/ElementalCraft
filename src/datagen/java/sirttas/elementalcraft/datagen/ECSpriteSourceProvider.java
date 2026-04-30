package sirttas.elementalcraft.datagen;

import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.AtlasIds;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.data.SpriteSourceProvider;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.synthesizer.solar.SolarSynthesizerRenderer;
import sirttas.elementalcraft.client.renderer.state.SourceRenderState;
import sirttas.elementalcraft.gui.GuiHandler;
import sirttas.elementalcraft.spell.airshield.AirShieldSpellRenderer;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ECSpriteSourceProvider extends SpriteSourceProvider {


    public ECSpriteSourceProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, ElementalCraftApi.MODID);
    }

    @Override
    protected void gather() {
        atlas(AtlasIds.BLOCKS)
                .addSource(single(SolarSynthesizerRenderer.BEAM))
                .addSource(single(AirShieldSpellRenderer.BACKGROUND))
                .addSource(single(AirShieldSpellRenderer.BLADE))
                .addSource(single(SourceRenderState.OUTER))
                .addSource(single(SourceRenderState.MIDDLE))
                .addSource(single(GuiHandler.TRANSLOCATION_ANCHOR_MARKER));
        atlas(AtlasIds.ITEMS)
                .addSource(directory("elementalcraft/jewels"))
                .addSource(manager(ElementalCraftApi.RUNE_MANAGER));
    }

    private SingleFile single(Material material) {
        return new SingleFile(material.sprite(), Optional.empty());
    }

    private DirectoryLister directory(String sourcePath) {
        return new DirectoryLister(sourcePath, sourcePath + "/");
    }

    private DirectoryLister manager(IDataManager<?> manager) {
        return directory(manager.getFolder());
    }

}
