package sirttas.elementalcraft.datagen;

import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.client.renderer.texture.atlas.sources.PalettedPermutations;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.synthesizer.solar.SolarSynthesizerRenderer;
import sirttas.elementalcraft.client.renderer.state.SourceRenderState;
import sirttas.elementalcraft.gui.GuiHandler;
import sirttas.elementalcraft.spell.airshield.AirShieldSpellRenderer;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ECSpriteSourceProvider extends SpriteSourceProvider {


    public ECSpriteSourceProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super(output, lookupProvider, ElementalCraftApi.MODID, fileHelper);
    }

    @Override
    protected void gather() {
        atlas(BLOCKS_ATLAS)
                .addSource(directory("elementalcraft/jewels"))
                .addSource(manager(ElementalCraftApi.RUNE_MANAGER))
                .addSource(single(SolarSynthesizerRenderer.BEAM))
                .addSource(single(AirShieldSpellRenderer.BACKGROUND))
                .addSource(single(AirShieldSpellRenderer.BLADE))
                .addSource(single(SourceRenderState.OUTER))
                .addSource(single(SourceRenderState.MIDDLE))
                .addSource(single(GuiHandler.TRANSLOCATION_ANCHOR_MARKER));
        atlas(Identifier.withDefaultNamespace("armor_trims"))
                .addSource(new PalettedPermutations(List.of(
                        createTrimPattern("coast"),
                        createTrimPattern("coast_leggings"),
                        createTrimPattern("sentry"),
                        createTrimPattern("sentry_leggings"),
                        createTrimPattern("dune"),
                        createTrimPattern("dune_leggings"),
                        createTrimPattern("wild"),
                        createTrimPattern("wild_leggings"),
                        createTrimPattern("ward"),
                        createTrimPattern("ward_leggings"),
                        createTrimPattern("eye"),
                        createTrimPattern("eye_leggings"),
                        createTrimPattern("vex"),
                        createTrimPattern("vex_leggings"),
                        createTrimPattern("tide"),
                        createTrimPattern("tide_leggings"),
                        createTrimPattern("snout"),
                        createTrimPattern("snout_leggings"),
                        createTrimPattern("rib"),
                        createTrimPattern("rib_leggings"),
                        createTrimPattern("spire"),
                        createTrimPattern("spire_leggings"),
                        createTrimPattern("wayfinder"),
                        createTrimPattern("wayfinder_leggings"),
                        createTrimPattern("shaper"),
                        createTrimPattern("shaper_leggings"),
                        createTrimPattern("silence"),
                        createTrimPattern("silence_leggings"),
                        createTrimPattern("raiser"),
                        createTrimPattern("raiser_leggings"),
                        createTrimPattern("host"),
                        createTrimPattern("host_leggings"),
                        createTrimPattern("bolt"),
                        createTrimPattern("bolt_leggings"),
                        createTrimPattern("flow"),
                        createTrimPattern("flow_leggings")
                ), Identifier.withDefaultNamespace("trims/color_palettes/trim_palette"), Map.of(
                        "drenched_iron", createTrimPermutation("drenched_iron"),
                        "swift_alloy", createTrimPermutation("swift_alloy"),
                        "fireite", createTrimPermutation("fireite"),
                        "fireite_flame", createTrimPermutation("fireite_flame"),
                        "springaline", createTrimPermutation("springaline")
                )));
    }

    private SingleFile single(Material material) {
        return new SingleFile(material.texture(), Optional.empty());
    }

    private DirectoryLister directory(String sourcePath) {
        return new DirectoryLister(sourcePath, sourcePath + "/");
    }

    private DirectoryLister manager(IDataManager<?> manager) {
        return directory(manager.getFolder());
    }

    private Identifier createTrimPermutation(String name) {
        return ElementalCraftApi.createRL("trims/color_palettes/" + name);
    }

    private Identifier createTrimPattern(String name) {
        return Identifier.withDefaultNamespace("trims/models/armor/" + name);
    }
}
