package sirttas.elementalcraft.datagen;

import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.client.renderer.texture.atlas.sources.PalettedPermutations;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.client.resources.model.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SpriteSourceProvider;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.source.SourceRendererHelper;
import sirttas.elementalcraft.block.source.displacement.plate.SourceDisplacementPlateRenderer;
import sirttas.elementalcraft.block.synthesizer.solar.SolarSynthesizerRenderer;
import sirttas.elementalcraft.gui.GuiHandler;
import sirttas.elementalcraft.spell.airshield.AirShieldSpellRenderer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ECSpriteSourceProvider extends SpriteSourceProvider {


    public ECSpriteSourceProvider(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, fileHelper, ElementalCraftApi.MODID);
    }

    @Override
    protected void addSources() {
        atlas(BLOCKS_ATLAS)
                .addSource(directory("elementalcraft/jewels"))
                .addSource(manager(ElementalCraftApi.RUNE_MANAGER))
                .addSource(single(SolarSynthesizerRenderer.BEAM))
                .addSource(single(AirShieldSpellRenderer.BACKGROUND))
                .addSource(single(AirShieldSpellRenderer.BLADE))
                .addSource(single(SourceDisplacementPlateRenderer.SOURCE_DISPLACEMENT))
                .addSource(single(SourceDisplacementPlateRenderer.CIRCLE))
                .addSource(single(SourceRendererHelper.OUTER))
                .addSource(single(SourceRendererHelper.MIDDLE))
                .addSource(single(GuiHandler.TRANSLOCATION_ANCHOR_MARKER));
        atlas(new ResourceLocation("armor_trims"))
                .addSource(new PalettedPermutations(List.of(
                        new ResourceLocation("trims/models/armor/coast"),
                        new ResourceLocation("trims/models/armor/coast_leggings"),
                        new ResourceLocation("trims/models/armor/sentry"),
                        new ResourceLocation("trims/models/armor/sentry_leggings"),
                        new ResourceLocation("trims/models/armor/dune"),
                        new ResourceLocation("trims/models/armor/dune_leggings"),
                        new ResourceLocation("trims/models/armor/wild"),
                        new ResourceLocation("trims/models/armor/wild_leggings"),
                        new ResourceLocation("trims/models/armor/ward"),
                        new ResourceLocation("trims/models/armor/ward_leggings"),
                        new ResourceLocation("trims/models/armor/eye"),
                        new ResourceLocation("trims/models/armor/eye_leggings"),
                        new ResourceLocation("trims/models/armor/vex"),
                        new ResourceLocation("trims/models/armor/vex_leggings"),
                        new ResourceLocation("trims/models/armor/tide"),
                        new ResourceLocation("trims/models/armor/tide_leggings"),
                        new ResourceLocation("trims/models/armor/snout"),
                        new ResourceLocation("trims/models/armor/snout_leggings"),
                        new ResourceLocation("trims/models/armor/rib"),
                        new ResourceLocation("trims/models/armor/rib_leggings"),
                        new ResourceLocation("trims/models/armor/spire"),
                        new ResourceLocation("trims/models/armor/spire_leggings"),
                        new ResourceLocation("trims/models/armor/wayfinder"),
                        new ResourceLocation("trims/models/armor/wayfinder_leggings"),
                        new ResourceLocation("trims/models/armor/shaper"),
                        new ResourceLocation("trims/models/armor/shaper_leggings"),
                        new ResourceLocation("trims/models/armor/silence"),
                        new ResourceLocation("trims/models/armor/silence_leggings"),
                        new ResourceLocation("trims/models/armor/raiser"),
                        new ResourceLocation("trims/models/armor/raiser_leggings"),
                        new ResourceLocation("trims/models/armor/host"),
                        new ResourceLocation("trims/models/armor/host_leggings")
                ), new ResourceLocation("trims/color_palettes/trim_palette"), Map.of(
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

    private ResourceLocation createTrimPermutation(String name) {
        return ElementalCraft.createRL("trims/color_palettes/" + name);
    }

}
